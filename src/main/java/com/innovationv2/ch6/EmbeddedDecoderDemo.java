package com.innovationv2.ch6;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.innovationv2.tools.Tool.bytesToHex;

class IntegerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int i = in.readInt();
        System.out.println("Decode: " + i);
        out.add(i);
    }
}

@ChannelHandler.Sharable
class StringHandler extends ChannelInboundHandlerAdapter {
    public static final MsgHandler INSTANCE = new MsgHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String in = (String) msg;
        System.out.println("Received Msg: " + in);
    }
}

class StringDecoder_ extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        byte[] msg = new byte[in.readableBytes()];
        in.readBytes(msg);
        out.add(new String(msg, StandardCharsets.UTF_8));
    }
}

class BytesPrintDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        byte[] msg = new byte[in.readableBytes()];
        in.readBytes(msg);
        System.out.println("Received: " + bytesToHex(msg));
    }
}