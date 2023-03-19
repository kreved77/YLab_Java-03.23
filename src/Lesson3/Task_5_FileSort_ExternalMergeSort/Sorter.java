package Lesson3.Task_5_FileSort_ExternalMergeSort;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class Sorter {

    public File sortFile(File dataFile) throws IOException {
        int counterElements = 0;
        int chunkLength = 375_000_000;
        Long[] arr = new Long[chunkLength];

        try (FileInputStream fileInputStream = new FileInputStream(dataFile)) {
            int i = 0;
            Scanner scanner = new Scanner(fileInputStream);

            while (scanner.hasNextLong()) {
                arr[i++] = scanner.nextLong();
                counterElements++;
            }
        }


        System.out.println(" Input elements counter  = " + counterElements);
        if (counterElements < chunkLength) {        // "trim" array for small input files <375_000_000 elements
            arr = Arrays.copyOfRange(arr, 0, counterElements);
        }


        File resultFile = new File("src/Lesson3/Task_5_FileSort_ExternalMergeSort/data/result.txt");
        try (PrintWriter printWriter = new PrintWriter(resultFile)) {
            for (Long el : mergeSort(arr)) {
                printWriter.println(el);
            }
            printWriter.flush();
        }

        return resultFile;
    }

    public static Long[] mergeSort(Long[] chunk) {
        if (chunk.length <= 1) {
            return chunk;
        }
        Long[] left = Arrays.copyOfRange(chunk, 0, chunk.length / 2);
        Long[] right = Arrays.copyOfRange(chunk, left.length, chunk.length);
        return merge(mergeSort(left), mergeSort(right));
    }

    private static Long[] merge(Long[] left, Long[] right) {
        int resIndex = 0;
        int leftIndex = 0;
        int rightIndex = 0;
        Long[] result = new Long[left.length + right.length];

        while (leftIndex < left.length && rightIndex < right.length)
            if (left[leftIndex] < right[rightIndex]) {
                result[resIndex++] = left[leftIndex++];
            } else {
                result[resIndex++] = right[rightIndex++];
            }

        while (resIndex < result.length)
            if (leftIndex != left.length) {
                result[resIndex++] = left[leftIndex++];
            } else {
                result[resIndex++] = right[rightIndex++];
            }

        return result;
    }
}