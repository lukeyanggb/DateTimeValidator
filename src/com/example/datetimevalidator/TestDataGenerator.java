package com.example.datetimevalidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class TestDataGenerator {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("data/isodata.txt");
        Files.deleteIfExists(path);
        BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()));
        // test data size: 100,000
        int size = 20;
        int year, month, day, hour, minute, second, timezone;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size*0.9; i++) {
            Random random = new Random();
            year = random.nextInt(123) + 1900;
            month = random.nextInt(12) + 1;
            day = random.nextInt(31) + 1;
            hour = random.nextInt(24);
            minute = random.nextInt(60);
            second = random.nextInt(60);
            timezone = random.nextInt(24) - 11;
            stringBuilder.append(year).append("-").append(String.format("%02d", month)).append("-")
                    .append(String.format("%02d", day)).append("T").append(String.format("%02d", hour)).append(":")
                    .append(String.format("%02d", minute)).append(":").append(String.format("%02d", second));
            if (timezone == 0) {
                stringBuilder.append ("Z" + "\n");
            } else if (timezone > 0){
                stringBuilder.append("+").append(String.format("%02d", timezone)).append(":00\n");
            } else {
                stringBuilder.append("-").append(String.format("%02d", timezone * -1)).append(":00\n");
            }
            writer.write(stringBuilder.toString());
            stringBuilder.setLength(0); // reset stringBuilder to empty
        }
        writer.flush();
        // add 10 % date time values as duplicates (at least 10 % are duplicated date times)
        BufferedReader reader = new BufferedReader(new FileReader(path.toFile()));
        String line;
        for (int i = 0; i < size*0.1; i++){
            line = reader.readLine();
            writer.write(line);
            writer.newLine();
        }
        reader.close();
        writer.close();
    }
}
