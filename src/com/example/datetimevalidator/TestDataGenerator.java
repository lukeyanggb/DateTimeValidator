package com.example.datetimevalidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class TestDataGenerator {
    private final Path path;
    private final int size;
    private final int duplicates;
    private final String dateTimeFormat;

    public TestDataGenerator(Path path, int size, int duplicates, String dateTimeFormat) {
        this.path = path;
        this.size = size;
        this.duplicates = duplicates;
        this.dateTimeFormat = dateTimeFormat;
    }

    public void generateTestData() throws IOException {
        Files.deleteIfExists(path);
        BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()));
        String[] args = {"YYYY", "MM", "DD", "hh", "mm", "ss", "TZD"};
        int[] data = new int[7];
        String newRecord;
        for (int i = 0; i < size; i++) {
            Random random = new Random();
            newRecord = dateTimeFormat;
            data[0] = random.nextInt(131) + 1900; // year
            data[1] = random.nextInt(12) + 1; // month
            data[2] = random.nextInt(31) + 1; // day
            data[3] = random.nextInt(24); // hour
            data[4] = random.nextInt(60); // minute
            data[5] = random.nextInt(60); // second
            data[6] = random.nextInt(24) - 11; // timezone
            for (int j = 0; j < 6; j++) {
                newRecord = newRecord.replaceAll(args[j], String.format("%02d", data[j]));
            }
            if (data[6] == 0) {
                newRecord = newRecord.replaceAll(args[6],"Z");
            } else if (data[6] > 0){
                newRecord = newRecord.replaceAll(args[6],"+" + String.format("%02d", data[6]) + ":00");
            } else {
                newRecord = newRecord.replaceAll(args[6],"-" + String.format("%02d", data[6]*-1) + ":00");
            }
            writer.write(newRecord);
            writer.newLine();
        }
        writer.flush();
        // add date time values as duplicates (at least 10 % are duplicated date times)
        BufferedReader reader = new BufferedReader(new FileReader(path.toFile()));
        String line;
        for (int i = 0; i < duplicates; i++){
            line = reader.readLine();
            writer.write(line);
            writer.newLine();
        }
        reader.close();
        writer.close();
    }

    public Path getPath() {
        return path;
    }

    public int getSize() {
        return size;
    }

    public int getDuplicates() {
        return duplicates;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }
}
