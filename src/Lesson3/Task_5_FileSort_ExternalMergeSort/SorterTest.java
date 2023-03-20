package Lesson3.Task_5_FileSort_ExternalMergeSort;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class SorterTest {

    public static void main(String[] args) throws IOException {
        long beginTime = System.currentTimeMillis();

//        File dataFile = new Generator().generate("src/Lesson3/Task_5_FileSort_ExternalMergeSort/data/data.txt", 375_000_000);
        File dataFile = new Generator().generate("src/Lesson3/Task_5_FileSort_ExternalMergeSort/data/data.txt", 1_000);
        System.out.println("Data file is generated.");
        System.out.println(new Validator(dataFile).isSorted()); // false


        File sortedFile = new Sorter().sortFile(dataFile);
        System.out.println((System.currentTimeMillis() - beginTime) / 1000 + " (sec) = time for sortFile \nWaiting for check:");
        System.out.println(new Validator(sortedFile).isSorted()); // true



//        filePrinter(dataFile);    // for small files (<20) check
//        filePrinter(sortedFile);    // for small files (<20) check


        // delete files when Process finished
//        dataFile.deleteOnExit();
//        sortedFile.deleteOnExit();
    }

    private static void filePrinter(File dataFile) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(dataFile)) {
            Scanner scanner = new Scanner(fileInputStream);
            while (scanner.hasNextLine()) {
                String stringFromFile = scanner.nextLine();
                System.out.print(stringFromFile + " ");
            }
        }
        System.out.println("\n =============================== ");
    }
}