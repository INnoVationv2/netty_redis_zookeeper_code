package com.innovationv2.ch2;

import com.innovationv2.util.Utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelPractice {
    public static void main(String[] args) throws IOException {
        String destFilename = Utils.getResourcePath("dest.txt");
        File outFile = new File(destFilename);

        if(!outFile.exists())
            outFile.createNewFile();

        FileChannelPractice fileChannelPractice = new FileChannelPractice();

        Utils.truncateFile(destFilename);
        fileChannelPractice.copyFile();

        Utils.truncateFile(destFilename);
        fileChannelPractice.fastCopyFile();
    }

    public void copyFile() throws IOException {
        long startTime = System.currentTimeMillis();
        String  srcFilename = Utils.getResourcePath("Shakespeare.txt"),
                destFilename = Utils.getResourcePath("dest.txt");
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            File inFile = new File(srcFilename);
            fis = new FileInputStream(inFile);
            inChannel = fis.getChannel();

            File outFile = new File(destFilename);
            fos = new FileOutputStream(outFile);
            outChannel = fos.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(10240);
            while(inChannel.read(buffer) != -1) {
                buffer.flip();
                int writeLen;
                while((writeLen = outChannel.write(buffer)) != 0)
                    System.out.println("Read " + writeLen + " Bytes");
                buffer.clear();
            }
            outChannel.force(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            outChannel.close();
            inChannel.close();
            fos.close();
            fis.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("base 复制毫秒数：" + (endTime - startTime));
    }

    public void fastCopyFile() throws IOException {
        long startTime = System.currentTimeMillis();
        String  srcFilename = Utils.getResourcePath("Shakespeare.txt"),
                destFilename = Utils.getResourcePath("dest.txt");
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new RandomAccessFile(srcFilename, "rw").getChannel();
            outChannel = new RandomAccessFile(destFilename, "rw").getChannel();
            outChannel.transferFrom(inChannel, 0, inChannel.size());
            outChannel.force(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            outChannel.close();
            inChannel.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("base 复制毫秒数：" + (endTime - startTime));
    }
}
