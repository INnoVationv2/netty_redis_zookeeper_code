package com.innovationv2.ch6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.nio.ByteBuffer;

public class DecoderTester {
    @Test
    public void testByteToIntegerDecoder() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {

            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new Byte2IntegerDecoder());
                ch.pipeline().addLast(IntegerHandler.INSTANCE);
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        for (int j = 0; j < 100; j++) {
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(j);
            channel.writeInbound(buf);
        }
    }

    @Test
    public void testByteToIntegerReplayingDecoder() throws InterruptedException {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new Byte2IntegerReplayingDecoder());
                ch.pipeline().addLast(IntegerHandler.INSTANCE);
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(i);

        int x = 128;
        for(int j = 0; j < 4; j++) {
            int idx = 24 - j * 8;
            System.out.printf("第%d次发送: %d\n", j, idx);
            Thread.sleep(1000);
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeByte(x >> idx);
            channel.writeInbound(buffer);
        }
    }
}
