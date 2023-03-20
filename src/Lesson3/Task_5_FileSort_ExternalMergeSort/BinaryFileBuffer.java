package Lesson3.Task_5_FileSort_ExternalMergeSort;

import java.io.*;

public class BinaryFileBuffer  {
    public static int BUFFERSIZE = 2048;
    public BufferedReader fbr;
    public File originalFile;
    private String cache;
    private boolean empty;

    public BinaryFileBuffer(File f) throws IOException {
        originalFile = f;
        fbr = new BufferedReader(new FileReader(f), BUFFERSIZE);
        reload();
    }

    public boolean empty() {
        return empty;
    }

    private void reload() throws IOException {
        try {
            if((this.cache = fbr.readLine()) == null){
                empty = true;
                cache = null;
            }
            else{
                empty = false;
            }
        } catch(EOFException oef) {
            empty = true;
            cache = null;
        }
    }

    public void close() throws IOException {
        fbr.close();
    }


    public String peek() {
        if(empty()) return null;
        return cache.toString();
    }
    public String pop() throws IOException {
        String answer = peek();
        reload();
        return answer;
    }
}
