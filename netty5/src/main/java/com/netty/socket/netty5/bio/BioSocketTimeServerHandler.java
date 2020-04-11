package com.netty.socket.netty5.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BioSocketTimeServerHandler implements Runnable{
    private Socket socket;

    public BioSocketTimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(),true);
            while (true){
                String body = in.readLine();
                if(body == null){
                    break;
                }
                if("QUERY TIME ORDER".equalsIgnoreCase(body)){
                    out.println(new java.util.Date(System.currentTimeMillis()).toString());
                }else{
                    out.println("BAD ORDER");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
