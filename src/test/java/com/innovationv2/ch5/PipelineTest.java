package com.innovationv2.ch5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;
import com.innovationv2.ch5.PipelineDemo.*;

public class PipelineTest {
    @Test
    public void OutTest() {
        ChannelInitializer<EmbeddedChannel> i = new ChannelInitializer<>() {
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new InHandlerA());
                ch.pipeline().addLast(new InHandlerB());
                ch.pipeline().addLast(new InHandlerC());

                ch.pipeline().addLast(new OutHandlerA());
                ch.pipeline().addLast(new OutHandlerB());
                ch.pipeline().addLast(new OutHandlerC());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(i);
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(1);
        channel.writeInbound(buf);
        channel.writeOutbound(buf);
    }
}
