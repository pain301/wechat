package com.pain.tom.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) {
        try {
            // 负责轮询是否有新的连接
            Selector serverSelector = Selector.open();

            // 负责轮询是否有数据可读
            Selector clientSelector = Selector.open();

            new Thread(() -> {
                try {
                    ServerSocketChannel listenerChannel = ServerSocketChannel.open();
                    listenerChannel.socket().bind(new InetSocketAddress(8000));
                    listenerChannel.configureBlocking(false);
                    listenerChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

                    while (true) {
                        if (serverSelector.select(1) > 0) {
                            Set<SelectionKey> selectionKeys = serverSelector.selectedKeys();
                            Iterator<SelectionKey> iterator = selectionKeys.iterator();

                            while (iterator.hasNext()) {
                                SelectionKey selectionKey = iterator.next();

                                if (selectionKey.isAcceptable()) {
                                    try {
                                        SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
                                        clientChannel.configureBlocking(false);
                                        clientChannel.register(clientSelector, SelectionKey.OP_READ);
                                    } finally {
                                        iterator.remove();
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();

            new Thread(() -> {
                while (true) {
                    try {
                        if (clientSelector.select(1) > 0) {
                            Set<SelectionKey> selectionKeys = clientSelector.selectedKeys();
                            Iterator<SelectionKey> iterator = selectionKeys.iterator();

                            while (iterator.hasNext()) {
                                SelectionKey selectionKey = iterator.next();

                                if (selectionKey.isReadable()) {
                                    try {
                                        SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
                                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                                        clientChannel.read(buffer);
                                        buffer.flip();
                                        System.out.println(Charset.defaultCharset().newDecoder().decode(buffer).toString());
                                    } finally {
                                        iterator.remove();
                                        selectionKey.interestOps(SelectionKey.OP_READ);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
