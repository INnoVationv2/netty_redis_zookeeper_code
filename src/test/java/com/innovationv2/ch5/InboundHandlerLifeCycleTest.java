package com.innovationv2.ch5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

public class InboundHandlerLifeCycleTest {
    @Test
    public void testInboundHandlerLifeCycle() {
        InHandlerLifeCycleDemo inHandler = new InHandlerLifeCycleDemo();
        InHandlerLifeCycleDemo2 inHandler2 = new InHandlerLifeCycleDemo2();
        ChannelInitializer<EmbeddedChannel> i = new ChannelInitializer<>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(inHandler).addLast(inHandler2);
            }
        };

        EmbeddedChannel channel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        channel.writeInbound(buf);
        channel.flush();
        System.out.println("\nSecond Time\n");
        channel.writeInbound(buf);
        channel.flush();
        channel.close();
    }
}
