package com.innovationv2.ch6;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.util.stream.IntStream;

public class EncoderTester {

    @Test
    public void testInteger2ByteEncoder() {
        ChannelInitializer<EmbeddedChannel> c = new ChannelInitializer<>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new Int2ByteEncoder());
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(c);
        for (int i = 0; i < 100; i++)
            channel.write(i);
        channel.flush();

        ByteBuf buf = channel.readOutbound();
        while (buf != null) {
            System.out.println(buf.readInt());
            buf = channel.readOutbound();
        }
    }

    @Test
    public void testInteger2ByteEncoder_2() throws InterruptedException {
        ChannelInitializer<EmbeddedChannel> c = new ChannelInitializer<>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline()
                        .addLast(new Int2ByteEncoder())
                        .addLast(IntProduceHandler.INSTANCE);

            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(c);
        channel.write("Sta");
        Thread.sleep(1000);

        channel.write("Star");
        Thread.sleep(1000);

        channel.write("Start");
        channel.flush();

        ByteBuf buf = channel.readOutbound();
        while (buf != null) {
            System.out.println(buf.readInt());
            buf = channel.readOutbound();
        }
    }


    @Test
    public void testInt2DoubleEncoder() {
        ChannelInitializer<EmbeddedChannel> c = new ChannelInitializer<>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline()
                        .addLast(new Double2ByteEncoder())
                        .addLast(new Int2DoubleEncoder());
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(c);
        IntStream.range(0, 100).forEach(value -> {
            channel.write(value);
            channel.flush();
        });

        ByteBuf buf = channel.readOutbound();
        while (buf != null) {
            System.out.println(buf.readDouble());
            buf = channel.readOutbound();
        }
    }
}
