package com.example.datetimevalidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        // INPUTS HERE
        Path input = Paths.get("data/test_data/100000_Size_10000_Duplicates_Format1.txt");
        long size = 100000L, duplicates = 10000L; // duplicates value has to be smaller than the size value.
        if (duplicates > size) {
            throw new IllegalArgumentException("Duplicates number is greater than the size number!");
        }
        String dateTimeFormat = "YYYY-MM-DDThh:mm:ssTZD";
        // generate test data
        // constructor signature: Path path, int size, int duplicates, String dateTimeFormat
        TestDataGenerator testDataGenerator = new TestDataGenerator(input, size, duplicates, dateTimeFormat);
        testDataGenerator.generateTestData();
        // output file path
        Path output = Paths.get("data/output_data/100000_Size_10000_Duplicates_UniqueMoments_Format1.txt");

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
                dateTimeValue = dateTimeValidationChecker.checkUniqueMoments();
//                 Use the following method for checking unique ISO 8601 date-time strings.
//                dateTimeValue = dateTimeValidationChecker.checkUniqueStrings();
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
    }
}
