package com.example.datetimevalidator;

import java.util.HashMap;
import java.util.HashSet;

public class DateTimeValidationChecker {
    private final HashSet<String> storedDateTime = new HashSet<>();
    String year, month, day, hour, minute, second, timezone;
    StringBuilder stringBuilder = new StringBuilder();


    public String validate(String line, PatternParser patternParser){
        HashMap<String, Integer> positions = patternParser.getPositions();
        HashMap<String, Integer> requiresAdjustment = patternParser.getRequiresAdjustment();
        int index, adjustment;
        String dateTimeValue;
        HashSet<String> validTimeZones = new HashSet<>();
        validTimeZones.add("Z");
        validTimeZones.add("+12:00");
        // add "-01:00 to -11:00 and +01:00 to +11:00"
        for (int i = 1; i < 12; i++) {
            validTimeZones.add("+" + String.format("%02d", i) + ":00");
            validTimeZones.add("-" + String.format("%02d", i) + ":00");
        }
        adjustment = line.charAt(positions.get("TZD")) == 'Z' ? -2 : 3;
        index = positions.get("YYYY") + requiresAdjustment.get("YYYY")*adjustment;
        year = line.substring(index, index+4);
        // check validity
        if (notNumeric(year) || (Integer.parseInt(year) < 1900 && Integer.parseInt(year) > 2030)) {
            return null;
        }
        index = positions.get("MM") + requiresAdjustment.get("MM")*adjustment;
        month = line.substring(index, index+2);
        if (notNumeric(month) || (Integer.parseInt(month) < 1 && Integer.parseInt(month) > 12)) {
            return null;
        }
        index = positions.get("DD") + requiresAdjustment.get("DD")*adjustment;
        day = line.substring(index, index+2);
        if (notNumeric(day) || (Integer.parseInt(day) < 1 && Integer.parseInt(day) > 31)) {
            return null;
        }
        index = positions.get("hh") + requiresAdjustment.get("hh")*adjustment;
        hour = line.substring(index, index+2);
        if (notNumeric(hour) || (Integer.parseInt(hour) < 0 && Integer.parseInt(hour) > 23)) {
            return null;
        }
        index = positions.get("mm") + requiresAdjustment.get("mm")*adjustment;
        minute = line.substring(index, index+2);
        if (notNumeric(minute) || (Integer.parseInt(minute) < 0 && Integer.parseInt(minute) > 59)) {
            return null;
        }
        index = positions.get("ss") + requiresAdjustment.get("ss")*adjustment;
        second = line.substring(index, index+2);
        if (notNumeric(second) || (Integer.parseInt(second) < 0 && Integer.parseInt(second) > 59)) {
            return null;
        }
        index = positions.get("TZD") + requiresAdjustment.get("TZD")*adjustment;
        timezone = line.substring(index, index+3+adjustment);
        if (!validTimeZones.contains(timezone)) {
            return null;
        }
        stringBuilder.append(year).append("-").append(month).append("-")
                .append(day).append("T").append(hour).append(":")
                .append(minute).append(":").append(second).append(timezone);
        dateTimeValue = stringBuilder.toString();
        stringBuilder.setLength(0); // reset stringBuilder to empty
        if (!storedDateTime.contains(dateTimeValue)) {
            storedDateTime.add(dateTimeValue);
            return dateTimeValue;
        } else {
            return null;
        }
    }

    public static boolean notNumeric(String str) {
        try {
            Integer.parseInt(str);
            return false;
        } catch(NumberFormatException e){
            return true;
        }
    }
}
