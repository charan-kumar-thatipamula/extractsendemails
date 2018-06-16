package com.charan.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileUility {

    String outputFileName;

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void writeOutputFile(String contents) {
        BufferedWriter writer = null;
        try {
            String outputFileName = getOutputFileName();
            System.out.println(outputFileName);
            writer = new BufferedWriter(new FileWriter(outputFileName));
            writer.write(contents);
        } catch (IOException e) {
            System.out.println("Error in writing output file");
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
