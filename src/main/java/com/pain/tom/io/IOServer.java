package com.pain.tom.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class IOServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);

        new Thread(() -> {
            while (true) {
                // do sth.
                try {
                    Socket socket = serverSocket.accept();

                    new Thread(() -> {
                        int len;
                        byte[] data = new byte[1024];
                        try {
                            InputStream inputStream = socket.getInputStream();
                            while ((len = inputStream.read(data, 0, 1024)) != -1) {
                                System.out.println(new String(data, 0, len));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
