package com.example.datetimevalidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args){
        // args = {Path input, long size, long duplicates, String dateTimeFormat, bool generateTestData,
        // bool requireUniqueMoments, Path output}
        try {
            Path input = Paths.get(args[0]); // e.g. "data/test_data/100000_Size_10000_Duplicates_Format1.txt"
            long size = Long.parseLong(args[1]); // e.g. 100000
            long duplicates = Long.parseLong(args[2]); // e.g., 10000
            String dateTimeFormat = args[3]; // e.g. "YYYY-MM-DDThh:mm:ssTZD";
            boolean generateTestData = Boolean.parseBoolean(args[4]); // e.g. "true"
            boolean requireUniqueMoment = Boolean.parseBoolean(args[5]); // e.g. "true"
            // output file path
            Path output = Paths.get(args[6]); // "data/output_data/100000_Size_10000_Duplicates_UniqueMoments_Format1.txt"

            // duplicates value has to be smaller than the size value.
            if (duplicates > size) {
                throw new IllegalArgumentException("Duplicates number is greater than the size number!");
            }
            // generate test data
            // constructor signature: Path path, int size, int duplicates, String dateTimeFormat
            if (generateTestData){
                TestDataGenerator testDataGenerator = new TestDataGenerator(input, size, duplicates, dateTimeFormat);
                testDataGenerator.generateTestData();
            }

            // intended output file
            Files.deleteIfExists(output);
            BufferedWriter writer = new BufferedWriter(new FileWriter(output.toFile()));
            BufferedReader reader = new BufferedReader(new FileReader(input.toFile()));

            // Parse input date-time to ISO 8601:
            // e.g. "MM/DD/YYYY: hh:mm:ss, TZD": 07/20/2022: 15:01:02, -03:00
            PatternParser patternParser = new PatternParser(dateTimeFormat);
            patternParser.parse();

            // Write valid date-time values to the output file.
            DateTimeValidationChecker dateTimeValidationChecker = new DateTimeValidationChecker();
            String line, dateTimeValue;
            while ((line = reader.readLine()) != null){
                if (dateTimeValidationChecker.validate(line, patternParser)){
                    if (requireUniqueMoment){
                        // for checking unique date-time moments.
                        dateTimeValue = dateTimeValidationChecker.checkUniqueMoments();
                    } else {
                        // for checking unique ISO 8601 date-time strings.
                        dateTimeValue = dateTimeValidationChecker.checkUniqueStrings();
                    }
                    if (dateTimeValue != null) {
                        writer.write(dateTimeValue);
                        writer.newLine();
                    }
                }
            }
            reader.close();
            writer.close();

            // logs:
            BufferedWriter writeLogs = new BufferedWriter(new FileWriter(Paths.get("data/logs.txt").toFile(), true));
            LocalDateTime now = LocalDateTime.now();
            writeLogs.write(now + "\n");
            writeLogs.write("Input file: " + input + "\n");
            writeLogs.write("Input format: " + dateTimeFormat + "\n");
            writeLogs.write("Input size: " + (size+duplicates) + "\n");
            writeLogs.write("Input copied duplicates: " + duplicates + "\n");
            writeLogs.write("The size of valid date-time value is " + dateTimeValidationChecker.getUniqueMomentsSize() + "\n");
            writeLogs.newLine();
            writeLogs.close();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Arguments");
        }
    }
}
