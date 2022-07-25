package com.example.datetimevalidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static com.example.datetimevalidator.TestDataGenerator.testDataGenerator;

public class Main {
    public static void main(String[] args) throws IOException {

        Path input = Paths.get("data/test_data/1.txt"); // test data path
        int size = 100, duplicates = 10; // duplicates number has to be equal or less than the randomized size
        String dateTimeFormat = "YYYY.MM.DD at hh:mm:ss TZD";
        // generate test data
        testDataGenerator(input, size, duplicates, dateTimeFormat);

        // intended output file
        Path output = Paths.get("data/output_data/1.txt");
        Files.deleteIfExists(output);
        BufferedWriter writer = new BufferedWriter(new FileWriter(output.toFile()));
        BufferedReader reader = new BufferedReader(new FileReader(input.toFile()));

        // Parse input date-time to ISO 8601:
        // e.g. "MM/DD/YYYY: hh:mm:ss, TZD": 07/20/2022: 15:01:02, -03:00
        // date time positions to remark positions
        HashMap<String, Integer> positions = new HashMap<>();
        // date time information after time zone requires adjustment: Z: -2, +hh:mm or -hh:mm: + 3
        HashMap<String, Integer> requiresAdjustment = new HashMap<>();
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

        // Write valid date-time values to the output file.
        HashSet<String> storedDateTime = new HashSet<>();
        String year, month, day, hour, minute, second, timezone;
        StringBuilder stringBuilder = new StringBuilder();
        String line, dateTimeValue;
        int adjustment, index;
        while ((line = reader.readLine()) != null){
            adjustment = line.charAt(positions.get("TZD")) == 'Z' ? -2 : 3;
            index = positions.get("YYYY") + requiresAdjustment.get("YYYY")*adjustment;
            year = line.substring(index, index+4);
            index = positions.get("MM") + requiresAdjustment.get("MM")*adjustment;
            month = line.substring(index, index+2);
            index = positions.get("DD") + requiresAdjustment.get("DD")*adjustment;
            day = line.substring(index, index+2);
            index = positions.get("hh") + requiresAdjustment.get("hh")*adjustment;
            hour = line.substring(index, index+2);
            index = positions.get("mm") + requiresAdjustment.get("mm")*adjustment;
            minute = line.substring(index, index+2);
            index = positions.get("ss") + requiresAdjustment.get("ss")*adjustment;
            second = line.substring(index, index+2);
            index = positions.get("TZD") + requiresAdjustment.get("TZD")*adjustment;
            timezone = line.substring(index, index+3+adjustment);
            stringBuilder.append(year).append("-").append(month).append("-")
                    .append(day).append("T").append(hour).append(":")
                    .append(minute).append(":").append(second).append(timezone);
            dateTimeValue = stringBuilder.toString();
            if (!storedDateTime.contains(dateTimeValue)) {
                writer.write(dateTimeValue);
                storedDateTime.add(dateTimeValue);
            }
            stringBuilder.setLength(0); // reset stringBuilder to empty
            writer.newLine();
        }

        reader.close();
        writer.close();
    }
}
