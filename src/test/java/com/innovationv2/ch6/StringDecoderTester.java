package com.innovationv2.ch6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

public class StringDecoderTester {
    @Test
    public void testByteToIntegerDecoder() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {

            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new Byte2StringDecoder());
                ch.pipeline().addLast(MsgHandler.INSTANCE);
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        String str = "abcdefg";
        byte[] bytes = str.substring(0,3).getBytes();
        buf.writeInt(str.length());
        buf.writeBytes(bytes);
        channel.writeInbound(buf);

        bytes = str.substring(3).getBytes();
        buf = Unpooled.buffer();
        buf.writeBytes(bytes);
        channel.writeInbound(buf);
    }

    @Test
    public void testInt2StringDecoder() {
        ChannelInitializer i = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new Byte2IntegerDecoder());
                ch.pipeline().addLast(new Int2StringDecoder());
                ch.pipeline().addLast(MsgHandler.INSTANCE);
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(2);
        channel.writeInbound(buf);
    }
}
