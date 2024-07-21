package com.innovationv2.ch6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.Test;

import static com.innovationv2.tools.Tool.bytesToHex;

public class EmbeddedDecoderTester {
    @Test
    public void testFixedLengthFrameDecoder() throws InterruptedException {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {

            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new FixedLengthFrameDecoder(4));
                ch.pipeline().addLast(new IntegerDecoder());
                ch.pipeline().addLast(IntegerHandler.INSTANCE);
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);

        for (int val = 0; val < 10; val++) {
            for (int j = 0; j < 4; j++) {
                int idx = 24 - j * 8;
                System.out.printf("第%d次发送: %d\n", j, idx);
                Thread.sleep(100);
                ByteBuf buffer = Unpooled.buffer();
                buffer.writeByte(val >> idx);
                channel.writeInbound(buffer);
            }
        }
    }

    @Test
    public void testLineBasedFrameDecoder() throws InterruptedException {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {

            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(MsgHandler.INSTANCE);
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);

        String msg = "hello\r\nwor\nld\n";
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeBytes(msg.substring(0, 3).getBytes());
        channel.writeInbound(buffer);

        buffer = Unpooled.buffer();
        buffer.writeBytes(msg.substring(3).getBytes());
        channel.writeInbound(buffer);
    }

    @Test
    public void testLengthFieldBasedFrameDecoder() throws InterruptedException {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {

            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 2, 0));
                ch.pipeline().addLast(new StringDecoder_());
                ch.pipeline().addLast(MsgHandler.INSTANCE);
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);

        String msg = "xxxhello,world";
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(msg.length());
        buffer.writeChar('c');
        buffer.writeBytes(msg.substring(0,3).getBytes());
        channel.writeInbound(buffer);

        buffer = Unpooled.buffer();
        buffer.writeBytes(msg.substring(3).getBytes());
        channel.writeInbound(buffer);
    }

    @Test
    public void testLengthFieldBasedFrameDecoder_2() throws InterruptedException {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {

            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 0));
                ch.pipeline().addLast(new BytesPrintDecoder());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);

        String msg = "hello,world";
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(msg.length() + 4);
        buffer.writeBytes(msg.getBytes());

        buffer.markReaderIndex();
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes);
        buffer.resetReaderIndex();

        System.out.println("Send: " + bytesToHex(bytes));

        channel.writeInbound(buffer);
    }
}