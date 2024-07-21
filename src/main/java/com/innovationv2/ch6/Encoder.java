package com.innovationv2.ch6;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

class Int2ByteEncoder extends MessageToByteEncoder<Integer> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Integer msg, ByteBuf out) {
        out.writeInt(msg);
        System.out.println("Encoding: " + msg);
    }
}

class IntProduceHandler extends ChannelOutboundHandlerAdapter {
    public static final IntProduceHandler INSTANCE = new IntProduceHandler();

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        System.out.println("Received: " + msg);
        if (!Objects.equals(msg, "Start"))
            return;
        System.out.println("Start Produce Integer");
        IntStream.range(0, 100)
                .forEach(value -> {
                    ctx.write(value);
                    ctx.flush();
                });
    }
}

class Int2DoubleEncoder extends MessageToMessageEncoder<Integer> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Integer msg, List<Object> out) {
        out.add(msg.doubleValue());
        System.out.println("Int2DoubleEncoder: " + msg);
    }
}

class Double2ByteEncoder extends MessageToByteEncoder<Double> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Double msg, ByteBuf out) throws Exception {
        out.writeDouble(msg);
        System.out.println("Double2ByteEncoder: " + msg);
    }
}
