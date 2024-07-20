package com.innovationv2.ch5;

import io.netty.channel.*;

public class PipelineDemo {
    public static class InHandlerA extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("InHandlerA");
            super.channelRead(ctx, msg);
        }
    }

    public static class InHandlerB extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("InHandlerB");
            super.channelRead(ctx, msg);
        }
    }

    public static class InHandlerC extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("InHandlerC");
            super.channelRead(ctx, msg);
        }
    }


    public static class OutHandlerA extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("OutHandlerA");
            super.write(ctx, msg, promise);
        }
    }

    public static class OutHandlerB extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("OutHandlerB");
            super.write(ctx, msg, promise);
        }
    }

    public static class OutHandlerC extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("OutHandlerC");
            super.write(ctx, msg, promise);
        }
    }
}
