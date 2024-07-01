package com.innovationv2.ch4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class EchoClient {
    public static void startClient() throws IOException {
        Selector selector = Selector.open();
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("localhost", 8889));
        channel.register(selector, SelectionKey.OP_CONNECT);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("hello world".getBytes());
        buffer.flip();

        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                SocketChannel socketChannel = (SocketChannel) key.channel();
                if (key.isConnectable()) {
                    if(socketChannel.finishConnect())
                        key.interestOps(SelectionKey.OP_WRITE);
                } else if (key.isWritable()) {
                    socketChannel.write(buffer);
                    key.interestOps(SelectionKey.OP_READ);
                    System.out.println("Send: " + new String(buffer.array()));
                } else if(key.isReadable()) {
                    buffer.clear();
                    socketChannel.read(buffer);
                    System.out.println("Received: " + new String(buffer.array()));

                    socketChannel.shutdownOutput();
                    socketChannel.close();
                    selector.close();
                    return;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        startClient();
    }
}
