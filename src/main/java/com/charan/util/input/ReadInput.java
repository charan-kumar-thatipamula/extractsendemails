package com.charan.util.input;

import com.charan.communication.ParellelProcessCSVsForEmail;

public class ReadInput {
    ReadCSVInput readCSVInput;
    ReadURLInput readURLInput;

    public ReadInput() {
        readCSVInput = new ReadCSVInput();
        readURLInput = new ReadURLInput();
    }
    public void runReadCSVinput() {
        readCSVInput.readInput();
    }

    public void runReadURLInput() {
        readURLInput.readInput();
    }
}
