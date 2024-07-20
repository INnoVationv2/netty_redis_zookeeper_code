package com.innovationv2.ch5;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.*;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.SystemPropertyUtil;
import org.junit.Test;

public class ByteBufDemo {
    @Test
    public void testWriteAndRead() {
        PooledByteBufAllocator pooledByteBufAllocator = new PooledByteBufAllocator();
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);
        System.out.println("创建ByteBuf:");
        print(buffer);
        buffer.writeBytes(new byte[]{1,2,3,4});
        System.out.println("写入ByteBuf:");
        print(buffer);
        System.out.println("取ByteBuf:");
        getByteBuf(buffer);
        print(buffer);
        System.out.println("读ByteBuf:");
        readByteBuf(buffer);
        print(buffer);
    }

    @Test
    public void testReference(){
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        System.out.println(buffer.refCnt()); // 开始为1
        buffer.retain();
        buffer.retain();
        System.out.println(buffer.refCnt()); // 此时为3

        buffer.release();
        buffer.release();
        buffer.release();
        System.out.println(buffer.refCnt()); // 此时为0
        try {
            buffer.retain();
        } catch (Exception ex) {
            System.out.println("Ref cnt is 0, can't access");
        }
    }

    @Test
    public void testByteBuf(){
        ByteBuf header = ByteBufAllocator.DEFAULT.buffer();
        header.writeBytes(new byte[]{1,2,3,4});
        ByteBuf body = ByteBufAllocator.DEFAULT.buffer();
        body.writeBytes(new byte[]{5,6,7,8});
        ByteBuf byteBuf = Unpooled.wrappedBuffer(header, body);
        ByteBuf byteBuf1 = Unpooled.wrappedBuffer(new byte[]{1, 2, 3, 4});
        CompositeByteBuf compositeBuffer = Unpooled.compositeBuffer();
        compositeBuffer.addComponents(header, body);
        System.out.println(compositeBuffer);
    }


    public static void checkByteBufType(ByteBuf buffer){
        System.out.println(buffer.hasArray() ? "Heap Buffer":"Direct Buffer");
    }


    private void readByteBuf(ByteBuf buffer) {
        while(buffer.isReadable()) {
            System.out.println("  Read Bytes: " + buffer.readByte());
        }
    }

    private void getByteBuf(ByteBuf buffer) {
        for(int i = 0; i < buffer.readableBytes(); i++) {
            System.out.println("  Get Bytes: " + buffer.getByte(i));
        }
    }

    private void print(ByteBuf buffer) {
        System.out.println("  isReadable(): " + buffer.isReadable());
        System.out.println("  readerIndex(): " + buffer.readerIndex());
        System.out.println("  readableBytes(): " + buffer.readableBytes());
        System.out.println("  isWritable(): " + buffer.isWritable());
        System.out.println("  writerIndex(): " + buffer.writerIndex());
        System.out.println("  writableBytes(): " + buffer.writableBytes());
        System.out.println("  capacity(): " + buffer.capacity());
        System.out.println("  maxCapacity(): " + buffer.maxCapacity());
        System.out.println("  maxWritableBytes(): " + buffer.maxWritableBytes());
    }
}
