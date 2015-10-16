package com.example;

import java.io.IOException;
import java.util.zip.DataFormatException;

public class Main {

    public static final String RESULT_FILE_NAME = "result.txt";

    public static void main(String[] args) throws IOException, DataFormatException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Two arguments expected");
        }

        Painter painter = new Painter(args[0], args[1]);
        painter.paint(RESULT_FILE_NAME);
    }
}
