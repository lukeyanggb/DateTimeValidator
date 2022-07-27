package com.example.datetimevalidator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class ArgumentsTest {
    // input, size, duplicates, dateTimeFormat, generateTestData, requireUniqueMoment, output
    private String[] args = {"data/test_data/100000_Size_10000_Duplicates_Format1.txt", "100000", "10000",
                             "YYYY-MM-DDThh:mm:ssTZD", "true", "true",
                             "data/output_data/100000_Size_10000_Duplicates_UniqueMoments_Format1.txt"};

    // test if input path is valid
    @org.junit.jupiter.api.Test
    void test1() {
        String[] copy = args.clone();
        copy[0] = "empty/test.txt";
        assertThrows(IllegalArgumentException.class,
                () -> Main.main(copy));
    }

    // test if size is a number
    @org.junit.jupiter.api.Test
    void test2() {
        String[] copy = args.clone();
        copy[1] = "not number";
        assertThrows(IllegalArgumentException.class,
                () -> Main.main(copy));
    }

    // test if duplicates is a number
    @org.junit.jupiter.api.Test
    void test3() {
        String[] copy = args.clone();
        copy[2] = "not number";
        assertThrows(IllegalArgumentException.class,
                () -> Main.main(copy));
    }

    // test must greater than duplicates
    @org.junit.jupiter.api.Test
    void test4() {
        String[] copy = args.clone();
        copy[1] = "1000";
        copy[2] = "10000";
        assertThrows(IllegalArgumentException.class,
                () -> Main.main(copy));
    }

    // test must greater than duplicates
    @org.junit.jupiter.api.Test
    void test5() {
        String[] copy = args.clone();
        copy[1] = "1000";
        copy[2] = "10000";
        assertThrows(IllegalArgumentException.class,
                () -> Main.main(copy));
    }

    // test if output path is valid
    @org.junit.jupiter.api.Test
    void test6() {
        String[] copy = args.clone();
        copy[6] = "empty/test.txt";
        assertThrows(IllegalArgumentException.class,
                () -> Main.main(copy));
    }

    // test all args
    @org.junit.jupiter.api.Test
    void test7() {
        String[] copy = args.clone();
        Main.main(copy);
    }
//    @org.junit.jupiter.api.Test
//    void main() {
//        Main.main(args);
//        assertEquals("stderr output does not match", "Usage: texttool [ -f | -o output_file_name | -i | -r old new | -p prefix | -c n | -d n ] FILE", errStream.toString().strip());
//        assertTrue("stdout output should be empty", outStream.toString().isEmpty());
//        assertEquals("input file content not matched", input, getFileContent(inputFile.getPath()));
//    }

}