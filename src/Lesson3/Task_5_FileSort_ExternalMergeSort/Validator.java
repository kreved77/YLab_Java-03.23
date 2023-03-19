package Lesson3.Task_5_FileSort_ExternalMergeSort;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Validator {
    int counter = 0;
    private File file;
    public Validator(File file) {
        this.file = file;
    }
    public boolean isSorted() {
        try (Scanner scanner = new Scanner(new FileInputStream(file))) {
            long prev = Long.MIN_VALUE;
            while (scanner.hasNextLong()) {
                counter++;
                long current = scanner.nextLong();
                if (current < prev) {
                    return false;
                } else {
                    prev = current;
                }
            }
            System.out.println(" Result elements counter = " + counter);
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}