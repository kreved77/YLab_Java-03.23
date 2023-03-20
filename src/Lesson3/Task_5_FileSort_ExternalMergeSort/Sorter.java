package Lesson3.Task_5_FileSort_ExternalMergeSort;

import java.util.*;
import java.io.*;

/**
 Во вложении тестовые файлы на 1к записей.
 checkSizeOfChunk() - проверяет возможность бить на чанки такого размера, исходя из имеющейся freeMemory
 В Validator'е дописан счетчик: считаются количество записей при распарсинге файла, а потом в Validator'е - и выводятся в консоль для контроля (должны совпасть с параметром из рандом-генератора).
 */

public class Sorter {
    static int counterElements = 0;
    static long chunkSize = 100_000_000L; // in bytes

    public static void checkSizeOfChunk() {
        long freeMem = Runtime.getRuntime().freeMemory();
        if (chunkSize >= freeMem / 2) {
            System.err.println(" Can be reached \"out of memory\" for selected chunkSize!  -> chunkSize is reduced");
            chunkSize = freeMem / 2;
        }
    }

    public File sortFile(File dataFile) throws IOException {

        File resultFile = new File("src/Lesson3/Task_5_FileSort_ExternalMergeSort/data/result.txt");
        checkSizeOfChunk();

        Comparator<String> comparator = new Comparator<String>() {
            public int compare(String s1, String s2){
                if (Long.parseLong(s1) < Long.parseLong(s2)) return -1;
                else if (Long.parseLong(s1) > Long.parseLong(s2)) return 1;
                return 0;
            }
        };

        List<File> l = sortInBatch(dataFile, comparator) ;
        mergeSortedFiles(l, resultFile, comparator);

        System.out.println(" Input elements counter  = " + counterElements);
        return resultFile;
    }

    public static List<File> sortInBatch(File file, Comparator<String> comparator) throws IOException {
        List<File> files = new ArrayList<File>();
        BufferedReader fbr = new BufferedReader(new FileReader(file));

        try{
            List<String> tmpList =  new ArrayList<String>();
            String line = "";
            try {
                while(line != null) {
                    long currentChunkSize = 0;
                    while((currentChunkSize < chunkSize)
                            &&(   (line = fbr.readLine()) != null) ){
                        tmpList.add(line);
                        counterElements++;
                        currentChunkSize += line.length() / 2 + 40;
                    }
                    files.add(sortAndSave(tmpList,comparator));
                    tmpList.clear();
                }
            } catch(EOFException oef) {
                if(tmpList.size()>0) {
                    files.add(sortAndSave(tmpList,comparator));
                    tmpList.clear();
                }
            }
        } finally {
            fbr.close();
        }
        return files;
    }


    public static File sortAndSave(List<String> tmpList, Comparator<String> comparator) throws IOException  {
        Collections.sort(tmpList,comparator);  //
        File newtmpfile = File.createTempFile("sortInBatch", "flatFile");
        newtmpfile.deleteOnExit();
        BufferedWriter fbw = new BufferedWriter(new FileWriter(newtmpfile));
        try {
            for(String s : tmpList) {
                fbw.write(s);
                fbw.newLine();
            }
        } finally {
            fbw.close();
        }
        return newtmpfile;
    }


    public static int mergeSortedFiles(List<File> files, File outputFile, final Comparator<String> comparator) throws IOException {
        PriorityQueue<BinaryFileBuffer> pq = new PriorityQueue<BinaryFileBuffer>(11,
                new Comparator<BinaryFileBuffer>() {
                    public int compare(BinaryFileBuffer a, BinaryFileBuffer b) {
                        return comparator.compare(a.peek(), b.peek());
                    }
                }
        );
        for (File f : files) {
            BinaryFileBuffer bfb = new BinaryFileBuffer(f);
            pq.add(bfb);
        }
        BufferedWriter fbw = new BufferedWriter(new FileWriter(outputFile));
        int rowCounter = 0;
        try {
            while(pq.size()>0) {
                BinaryFileBuffer bfb = pq.poll();
                String s = bfb.pop();
                fbw.write(s);
                fbw.newLine();
                ++rowCounter;
                if(bfb.empty()) {
                    bfb.fbr.close();
                    bfb.originalFile.delete();// we don't need you anymore
                } else {
                    pq.add(bfb); // add it back
                }
            }
        } finally {
            fbw.close();
            for(BinaryFileBuffer bfb : pq ) bfb.close();
        }
        return rowCounter;
    }
}