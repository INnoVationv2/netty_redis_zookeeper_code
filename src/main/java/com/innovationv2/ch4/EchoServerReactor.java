package com.innovationv2.ch4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

// Reactor类
public class EchoServerReactor implements Runnable {
    Selector selector;
    ServerSocketChannel serverSocket;

    EchoServerReactor() throws IOException {
        selector = Selector.open();

        serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.bind(new InetSocketAddress(8889));
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        sk.attach(new AcceptorHandler(selector, serverSocket));
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                // 阻塞直到连接到来
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey sk = iterator.next();
                    iterator.remove();
                    // 对到来的连接调用dispatch发送给handler
                    dispatch(sk);
                }
                // 发送完成后清空
                selectionKeys.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void dispatch(SelectionKey sk) {
        Runnable handler = (Runnable) sk.attachment();
        if (handler != null)
            handler.run();
    }

    public static void main(String[] args) throws IOException {
        new Thread(new EchoServerReactor()).start();
    }
}

// 第一个Handler: 负责建立连接
class AcceptorHandler implements Runnable {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    AcceptorHandler(Selector s, ServerSocketChannel channel) {
        this.selector = s;
        this.serverSocketChannel = channel;
    }

    public void run() {
        try {
            SocketChannel channel = serverSocketChannel.accept();
            System.out.println("New connection from " + channel.getRemoteAddress());
            if (channel != null)
                new EchoHandler(selector, channel);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

// 第二个Handler: 负责Echo服务
class EchoHandler implements Runnable {
    private SocketChannel channel;
    private SelectionKey sk;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    static final int RECEIVING = 0, SENDING = 1;
    int state = RECEIVING;

    EchoHandler(Selector selector, SocketChannel c) throws IOException {
        c.configureBlocking(false);
        channel = c;
        // Channel初始化为Read模式
        sk = channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        sk.attach(this);
        // 更新selector监测状态
        selector.wakeup();
    }

    public void run() {
        try {
            System.out.println("EchoHandler for " + channel.getRemoteAddress());
            if (state == SENDING) {
                channel.write(byteBuffer);
                byteBuffer.clear();
                state = RECEIVING;
            } else if (state == RECEIVING) {
                int len = channel.read(byteBuffer);
                if(len == -1) {
                    channel.close();
                    sk.cancel();
                } else {
                    System.out.println(new String(byteBuffer.array(), 0, len));
                    byteBuffer.flip();
                    state = SENDING;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("EchoHandler Complete");
    }
}


