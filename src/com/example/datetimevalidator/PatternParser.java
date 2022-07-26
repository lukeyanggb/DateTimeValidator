package com.example.datetimevalidator;

import java.util.HashMap;

public class PatternParser {
    private final String dateTimeFormat;
    // date time positions to remark positions
    private final HashMap<String, Integer> positions = new HashMap<>();
    // date time information after time zone requires adjustment: Z: -2, +hh:mm or -hh:mm: + 3
    private final HashMap<String, Integer> requiresAdjustment = new HashMap<>();

    public PatternParser(String dateTimeFormat) {
        this.dateTimeFormat = dateTimeFormat;
    }

    public HashMap<String, Integer> getPositions() {
        return positions;
    }

    public HashMap<String, Integer> getRequiresAdjustment() {
        return requiresAdjustment;
    }

    public void parse(){
        // Parse input date-time to ISO 8601:
        // e.g. "MM/DD/YYYY: hh:mm:ss, TZD": 07/20/2022: 15:01:02, -03:00
        int i = 0, afterTimeZone = 0;
        while (i < dateTimeFormat.length()) {
            switch (dateTimeFormat.charAt(i)) {
                case 'Y': if ((i <= dateTimeFormat.length() - 4) && dateTimeFormat.startsWith("YYYY", i)){
                    positions.put("YYYY", i);
                    requiresAdjustment.put("YYYY", afterTimeZone);
                    i += 4;
                } else {i++;} break;
                case 'M': if ((i <= dateTimeFormat.length() - 2) && dateTimeFormat.startsWith("MM", i)){
                    positions.put("MM", i);
                    requiresAdjustment.put("MM", afterTimeZone);
                    i += 2;
                } else {i++;} break;
                case 'D': if ((i <= dateTimeFormat.length() - 2) && dateTimeFormat.startsWith("DD", i)){
                    positions.put("DD", i);
                    requiresAdjustment.put("DD", afterTimeZone);
                    i += 2;
                } else {i++;} break;
                case 'h': if ((i <= dateTimeFormat.length() - 2) && dateTimeFormat.startsWith("hh", i)){
                    positions.put("hh", i);
                    requiresAdjustment.put("hh", afterTimeZone);
                    i += 2;
                } else {i++;} break;
                case 'm': if ((i <= dateTimeFormat.length() - 2) && dateTimeFormat.startsWith("mm", i)){
                    positions.put("mm", i);
                    requiresAdjustment.put("mm", afterTimeZone);
                    i += 2;
                } else {i++;} break;
                case 's': if ((i <= dateTimeFormat.length() - 2) && dateTimeFormat.startsWith("ss", i)){
                    positions.put("ss", i);
                    requiresAdjustment.put("ss", afterTimeZone);
                    i += 2;
                } else {i++;} break;
                case 'T': if ((i <= dateTimeFormat.length() - 3) && dateTimeFormat.startsWith("TZD", i)){
                    positions.put("TZD", i);
                    requiresAdjustment.put("TZD", afterTimeZone);
                    afterTimeZone = 1;
                    i += 3;
                } else {i++;} break;
                default: i++;
            }
        }
    }
}
