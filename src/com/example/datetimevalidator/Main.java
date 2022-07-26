package com.example.datetimevalidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        // INPUTS HERE
        Path input = Paths.get("data/test_data/1.txt");
        int size = 100000, duplicates = 1000; // duplicates number has to be smaller than size
        String dateTimeFormat = "YYYY.MM.DD at hh:mm:ss TZD";
        Path output = Paths.get("data/output_data/1.txt");

        // generate test data
        // constructor signature: Path path, int size, int duplicates, String dateTimeFormat
        TestDataGenerator testDataGenerator = new TestDataGenerator(input, size, duplicates, dateTimeFormat);
        testDataGenerator.generateTestData();

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
            dateTimeValue = dateTimeValidationChecker.validate(line, patternParser);
            if (dateTimeValue != null) {
                writer.write(dateTimeValue);
                writer.newLine();
            }
        }
        reader.close();
        writer.close();
    }
}
