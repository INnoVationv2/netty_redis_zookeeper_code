package com.innovationv2.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;

public class Utils {
    public static String getResourcePath(String fileName) {
        URL resource = Utils.class.getResource("/");
        assert resource != null;
        return resource.getPath() + fileName;
    }

    public static void truncateFile(String filename) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(filename, "rw");
        raf.seek(0);
        raf.setLength(0);
        raf.close();
    }
}
