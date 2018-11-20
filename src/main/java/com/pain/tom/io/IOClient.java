package com.pain.tom.io;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

public class IOClient {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8000);
                OutputStream outputStream = socket.getOutputStream();

                while (true) {
                    outputStream.write((new Date() + ", hello socket").getBytes());
                    Thread.sleep(2000);
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
    }
}
