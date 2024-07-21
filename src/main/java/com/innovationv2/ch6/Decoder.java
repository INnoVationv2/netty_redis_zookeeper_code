package com.innovationv2.ch6;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class Decoder {

}

class Byte2IntegerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        while (in.readableBytes() >= 4) {
            int i = in.readInt();
            System.out.println("Decode Integer: " + i);
            out.add(i);
        }
    }
}


class Byte2IntegerReplayingDecoder extends ReplayingDecoder<Integer> {
    Byte2IntegerReplayingDecoder() {
        state(0);
    }
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        System.out.println("Byte2IntegerReplayingDecoder/decode()" + state());
        if(state() >= 100)
            in.readByte();
        state(state() + 1);
    }
}

@ChannelHandler.Sharable
class IntegerHandler extends ChannelInboundHandlerAdapter {
    public static final IntegerHandler INSTANCE = new IntegerHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Integer in = (Integer) msg;
        System.out.println("Received Integer: " + in);
    }
}

