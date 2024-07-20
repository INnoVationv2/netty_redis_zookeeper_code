package com.innovationv2.ch5.echo_demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class EchoClient {
    private int serverPort;
    private String serverHost;
    Bootstrap b = new Bootstrap();

    public EchoClient(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void runClient() {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.remoteAddress(serverHost, serverPort);
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(NettyEchoClientHandler.INSTANCE);
                }
            });
            ChannelFuture future = b.connect();
            future.addListener((ChannelFutureListener) -> {
                if (future.isSuccess())
                    System.out.println("EchoClient 连接成功");
                else
                    System.out.println("EchoClient 连接失败");
            });
            future.sync();
            Channel channel = future.channel();
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入要发送的内容:");
            while (scanner.hasNext()) {
                String next = scanner.next();
                ByteBuf buffer = channel.alloc().buffer();
                buffer.writeBytes(next.getBytes(StandardCharsets.UTF_8));
                channel.writeAndFlush(buffer);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new EchoClient("localhost", 8889).runClient();
    }
}

@ChannelHandler.Sharable
class NettyEchoClientHandler extends ChannelInboundHandlerAdapter {
    public static final NettyEchoClientHandler INSTANCE = new NettyEchoClientHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        int len = in.readableBytes();
        byte[] array = new byte[len];
        in.getBytes(0, array);
        System.out.println("Client received: " + new String(array, StandardCharsets.UTF_8));
        in.release();
    }
}
