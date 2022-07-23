package com.example.datetimevalidator;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadWrite {
    public static void main(String[] args) {
        try{
            Path path = Paths.get("data/datetime.txt");
            File myObj = path.toFile();
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                FileWriter dateWriter = new FileWriter(myObj);
                dateWriter.write("Files in Java might be tricky, but it is fun enough!");
                dateWriter.close();
                System.out.println("Successfully wrote to the file.");
            } else {
                System.out.println("File already existed.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
