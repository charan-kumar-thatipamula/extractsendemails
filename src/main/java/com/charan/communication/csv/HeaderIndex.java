package com.charan.communication.csv;

public enum HeaderIndex {
    AUTHOR(0),
    EMAIL(1),
    TITLE(2);

    private final int position;
    HeaderIndex(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
