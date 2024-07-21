package com.innovationv2.ch6;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

class Byte2StringDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 4) return;

        in.markReaderIndex();
        int msgLen = in.readInt();

        if (in.readableBytes() < msgLen) {
            in.resetReaderIndex();
            return;
        }

        byte[] msg = new byte[msgLen];
        in.readBytes(msg);
        out.add(new String(msg, StandardCharsets.UTF_8));
    }
}

@ChannelHandler.Sharable
class MsgHandler extends ChannelInboundHandlerAdapter {
    public static final MsgHandler INSTANCE = new MsgHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String in = (String) msg;
        System.out.println("Received Msg: " + in);
    }
}

class Int2StringDecoder extends MessageToMessageDecoder<Integer> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) {
        String str = msg.toString();
        System.out.println("Decode String: " + str);
        out.add(msg.toString());
    }
}