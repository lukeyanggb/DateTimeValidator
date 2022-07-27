package com.example.datetimevalidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {
        // INPUTS HERE
        Path input = Paths.get("data/test_data/100000_Size_10000_Duplicates_Format1.txt");
        long size = 100000L, duplicates = 10000L, writeCount = 0L; // duplicates number has to be smaller than size
        String dateTimeFormat = "YYYY-MM-DDThh:mm:ssTZD";
        Path output = Paths.get("data/output_data/100000_Size_10000_Duplicates_UniqueStrings_Format1.txt");

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
            if (dateTimeValidationChecker.validate(line, patternParser)){
                dateTimeValue = dateTimeValidationChecker.checkUniqueMoments();
//                 Use the following method for checking unique ISO 8601 date-time strings.
//                dateTimeValue = dateTimeValidationChecker.checkUniqueStrings();
                if (dateTimeValue != null) {
                    writeCount ++;
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
        writeLogs.write("The size of valid date-time value is " + writeCount + "\n");
        writeLogs.newLine();
        writeLogs.close();
    }
}
