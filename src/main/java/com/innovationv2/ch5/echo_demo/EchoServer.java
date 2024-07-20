package com.innovationv2.ch5.echo_demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;

import static com.innovationv2.tools.Tool.getByteBufType;

public class EchoServer {
    private final int serverPort;
    ServerBootstrap b = new ServerBootstrap();

    public EchoServer(int port) {
        this.serverPort = port;
    }

    public void runServer() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.localAddress(serverPort);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(NettyEchoServerHandler.INSTANCE);
                }
            });
            ChannelFuture channelFuture = b.bind().sync();
            System.out.println("Server Start At Port: " + channelFuture.channel().localAddress());

            ChannelFuture closedFuture = channelFuture.channel().closeFuture();
            closedFuture.sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new EchoServer(8889).runServer();
    }
}

@ChannelHandler.Sharable
class NettyEchoServerHandler extends ChannelInboundHandlerAdapter {
    public static final NettyEchoServerHandler INSTANCE = new NettyEchoServerHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Msg type: " + getByteBufType(in));
        int len = in.readableBytes();
        byte[] array = new byte[len];
        in.getBytes(0, array);
        System.out.println("Server received: " + new String(array, StandardCharsets.UTF_8));
        System.out.println("写回前: msg.refCnt:" + ((ByteBuf) msg).refCnt());
        ChannelFuture future = ctx.writeAndFlush(msg);
        future.addListener((ChannelFutureListener) -> System.out.println("写回后: msg.refCnt:" + ((ByteBuf) msg).refCnt()));
    }
}

