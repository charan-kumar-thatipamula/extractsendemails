package com.charan.communication.csv;

import java.io.IOException;
import java.nio.file.Files;
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
            Stream<String> stream = Files.lines(Paths.get(filePath));
            stream.forEach(s -> {
                String[] contents = s.split(",");
                list.add(contents);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
