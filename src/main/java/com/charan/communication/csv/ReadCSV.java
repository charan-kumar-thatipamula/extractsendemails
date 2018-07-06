package com.charan.communication.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ReadCSV {
    private String filePath;

    public ReadCSV(String filePath) {
        this.filePath = filePath;
    }

    public List<String[]> readFile() {
        List<String[]> list = new ArrayList<>();
        try {
//            Path path = Paths.get(filePath);
//            Stream<String> stream = Files.lines(path);
//            stream.forEach(s -> {
//                String[] contents = s.split(",");
//                list.add(contents);
//            });

            // FileReader reads text files in the default encoding.
            FileReader fileReader =
                    new FileReader(filePath);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            String line = null;
            int i=0;
            while((line = bufferedReader.readLine()) != null) {
//                System.out.println(line);
                String[] contents = line.split(",");
                list.add(contents);
                i++;
            }
            System.out.println("line: " + i);

            // Always close files.
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
