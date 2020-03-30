package com.netty.socket.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BioSocketTimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("The Time Server is running on " + port);
            Socket socket = null;
            while (true) {
                socket = serverSocket.accept();
                new Thread(new BioSocketTimeServerHandler(socket)).start();
            }
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
            }
        }
    }
}
