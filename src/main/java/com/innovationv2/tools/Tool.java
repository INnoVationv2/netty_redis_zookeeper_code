package com.innovationv2.tools;

import io.netty.buffer.ByteBuf;

import java.util.Formatter;

public class Tool {
    public static String getByteBufType(ByteBuf buf) {
        return buf.hasArray() ? "堆内存" : "直接内存";
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        Formatter formatter = new Formatter(sb);
        int idx = 0;
        for (byte b : bytes) {
            formatter.format("%02X", b);
            if(++idx == 2){
                idx = 0;
                sb.append(' ');
            }
        }
        formatter.close();
        return sb.toString();
    }
}

