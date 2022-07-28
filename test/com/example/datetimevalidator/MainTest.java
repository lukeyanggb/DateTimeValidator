package com.example.datetimevalidator;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainTest {
    // 0        1       2           3               4                   5               6
    // input, size, duplicates, dateTimeFormat, generateTestData, requireUniqueMoment, output
    private String[] args = {
            "data/test_data/100000_Size_10000_Duplicates_Format1.txt",
            "100000",
            "10000",
            "YYYY-MM-DDThh:mm:ssTZD",
            "true",
            "true",
            "data/output_data/100000_Size_10000_Duplicates_UniqueMoments_Format1.txt"};

    // test data size: data/test_data/100000_Size_10000_Duplicates_Format1.txt
    @org.junit.jupiter.api.Test
    void test1() throws IOException {
        int expectedTestDataLines = 110000, expectedOutputLines = 100000;
        String[] copy = args.clone();
        Main.main(copy);
        // test data size
        try (Stream<String> fileStream = Files.lines(Paths.get(copy[0]))) {
            int noOfLines = (int) fileStream.count();
            Assertions.assertEquals(expectedTestDataLines, noOfLines);
        }
        // output data size
        try (Stream<String> fileStream = Files.lines(Paths.get(copy[6]))) {
            int noOfLines = (int) fileStream.count();
            Assertions.assertTrue(expectedOutputLines >= noOfLines);
        }
    }

    // test data size: Manually_typed_test_data.txt
    @org.junit.jupiter.api.Test
    void test2() throws IOException {
        int expectedTestDataLines = 9, expectedOutputLines = 4; // 4 unique data-time moments
        String[] copy = args.clone();
        copy[0] = "data/test_data/Manually_typed_test_data.txt";
        copy[4] = "false";
        copy[6] = "data/output_data/Manually_typed_test_data_UniqueMoments.txt";
        Main.main(copy);
        // test data size
        try (Stream<String> fileStream = Files.lines(Paths.get(copy[0]))) {
            int noOfLines = (int) fileStream.count();
            Assertions.assertEquals(expectedTestDataLines, noOfLines);
        }
        // output data size
        try (Stream<String> fileStream = Files.lines(Paths.get(copy[6]))) {
            int noOfLines = (int) fileStream.count();
            Assertions.assertEquals(expectedOutputLines, noOfLines);
        }
    }

    // test data size: Manually_typed_test_data.txt
    @org.junit.jupiter.api.Test
    void test3() throws IOException {
        int expectedTestDataLines = 9, expectedOutputLines = 6; // 6 unique date-time strings
        String[] copy = args.clone();
        copy[0] = "data/test_data/Manually_typed_test_data.txt";
        copy[4] = "false";
        copy[5] = "false";
        copy[6] = "data/output_data/Manually_typed_test_data_UniqueString.txt";
        Main.main(copy);
        // test data size
        try (Stream<String> fileStream = Files.lines(Paths.get(copy[0]))) {
            int noOfLines = (int) fileStream.count();
            Assertions.assertEquals(expectedTestDataLines, noOfLines);
        }
        // output data size
        try (Stream<String> fileStream = Files.lines(Paths.get(copy[6]))) {
            int noOfLines = (int) fileStream.count();
            Assertions.assertEquals(expectedOutputLines, noOfLines);
        }
    }
}
