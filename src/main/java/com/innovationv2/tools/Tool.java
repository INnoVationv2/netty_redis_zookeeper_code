package com.innovationv2.tools;

import io.netty.buffer.ByteBuf;

public class Tool {
    public static String getByteBufType(ByteBuf buf) {
        return buf.hasArray() ? "堆内存" : "直接内存";
    }
}
