package com.example.datetimevalidator;

import java.util.HashMap;
import java.util.HashSet;

public class DateTimeValidationChecker {
    private final HashSet<Long> storedUniqueMoments = new HashSet<>();
    private final HashSet<Long> storedUniqueStrings = new HashSet<>();
    String year, month, day, hour, minute, second, timezone, dateTimeValue;
    // absoluteTime is calculated based on GMT time: year+month+day+hour+minute+second
    // integer hashing should be better than string hashing.
    // modifiedAbsoluteTime = absoluteTime + timeZoneKey*(TimeZone+12): to differentiate different time zone for absoluteTime
    Long absoluteTime, modifiedAbsoluteTime, timeZoneAdjustment;
    Long timeZoneKey = 32140800000L; // 1000*12*31*24*60*60 = 32140800000. No collision for every 1,000 years.
    StringBuilder stringBuilder = new StringBuilder();
    HashSet<String> validTimeZones = new HashSet<>();

    public DateTimeValidationChecker() {
        // create valid time zones that can be used for DateTimeValidationChecker.validate()
        validTimeZones.add("Z");
        validTimeZones.add("+12:00");
        // add "-01:00 to -11:00 and +01:00 to +11:00"
        for (int i = 1; i < 12; i++) {
            validTimeZones.add("+" + String.format("%02d", i) + ":00");
            validTimeZones.add("-" + String.format("%02d", i) + ":00");
        }
    }

    // check the string line is a valid date-time value that has all the required
    // year, month, day, hour, minute, second, timezone information and it's not a duplicated.
    public boolean validate(String line, PatternParser patternParser){
        HashMap<String, Integer> positions = patternParser.getPositions();
        HashMap<String, Integer> requiresAdjustment = patternParser.getRequiresAdjustment();
        int index, adjustment;
        adjustment = line.charAt(positions.get("TZD")) == 'Z' ? -2 : 3;
        index = positions.get("YYYY") + requiresAdjustment.get("YYYY")*adjustment;
        year = line.substring(index, index+4);
        // check to see if the input line is a validity date time or not
        if (isNumeric(year) || (Integer.parseInt(year) < 1900 && Integer.parseInt(year) > 2030)) {
            return false;
        }
        index = positions.get("MM") + requiresAdjustment.get("MM")*adjustment;
        month = line.substring(index, index+2);
        if (isNumeric(month) || (Integer.parseInt(month) < 1 && Integer.parseInt(month) > 12)) {
            return false;
        }
        index = positions.get("DD") + requiresAdjustment.get("DD")*adjustment;
        day = line.substring(index, index+2);
        if (isNumeric(day) || (Integer.parseInt(day) < 1 && Integer.parseInt(day) > 31)) {
            return false;
        }
        index = positions.get("hh") + requiresAdjustment.get("hh")*adjustment;
        hour = line.substring(index, index+2);
        if (isNumeric(hour) || (Integer.parseInt(hour) < 0 && Integer.parseInt(hour) > 23)) {
            return false;
        }
        index = positions.get("mm") + requiresAdjustment.get("mm")*adjustment;
        minute = line.substring(index, index+2);
        if (isNumeric(minute) || (Integer.parseInt(minute) < 0 && Integer.parseInt(minute) > 59)) {
            return false;
        }
        index = positions.get("ss") + requiresAdjustment.get("ss")*adjustment;
        second = line.substring(index, index+2);
        if (isNumeric(second) || (Integer.parseInt(second) < 0 && Integer.parseInt(second) > 59)) {
            return false;
        }
        index = positions.get("TZD") + requiresAdjustment.get("TZD")*adjustment;
        timezone = line.substring(index, index+3+adjustment);
        if (!validTimeZones.contains(timezone)) {
            return false;
        }
        timeZoneAdjustment = timezone.equals("Z")? 0 : Long.parseLong(timezone.substring(0, 3));
        absoluteTime = (Long.parseLong(year)*12*31*24*60*60) + (Long.parseLong(month)*31*24*60*60)
                + (Long.parseLong(day)*24*60*60) + (Long.parseLong(hour)-timeZoneAdjustment)*60*60
                + (Long.parseLong(minute)*60) + Long.parseLong(second);
        // store the date-time value as dateTimeValue
        stringBuilder.append(year).append("-").append(month).append("-")
                .append(day).append("T").append(hour).append(":")
                .append(minute).append(":").append(second).append(timezone);
        dateTimeValue = stringBuilder.toString();
        stringBuilder.setLength(0); // reset stringBuilder to empty
        return true;
    }

    // check to see if the input date-time value is a duplicate (same moment)
    public String checkUniqueMoments() {
        if (!storedUniqueMoments.contains(absoluteTime)) {
            storedUniqueMoments.add(absoluteTime);
            return dateTimeValue;
        } else {
            return null;
        }
    }

    // check to see if the input date-time value is a duplicate (same moment allowed as long as different timezone)
    public String checkUniqueStrings() {
        // modify the key to differentiate different time zones at the same moment.
        modifiedAbsoluteTime = absoluteTime+timeZoneKey*(timeZoneAdjustment+12);
        if (!storedUniqueStrings.contains(modifiedAbsoluteTime)) {
            storedUniqueStrings.add(modifiedAbsoluteTime);
            return dateTimeValue;
        } else {
            return null;
        }
    }

    // check if the input str is a valid integer number
    public static boolean isNumeric(String str) {
        int length = str.length();
        // first char can be an operand
        if (length == 0 || str.charAt(0) != '+' || str.charAt(0) != '-' || !Character.isDigit(str.charAt(0))){
            return false;
        }
        // check to see if all chars are digits
        for (int i = 1; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public int getUniqueMomentsSize(){
        return storedUniqueMoments.size();
    }

    public int getUniqueStringsSize(){
        return storedUniqueStrings.size();
    }
}
