package com.wen.web_socket.websocket_h5;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SendThread extends Thread{
    private Socket sc;
    int i=0;
    //private BufferedReader in;
    private PrintWriter out;
    public SendThread(Socket s) throws IOException {
        sc=s;
        //in=new BufferedReader(new InputStreamReader(sc.getInputStream()));
        out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(sc.getOutputStream())),true);
        //System.out.println("ok");
    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            while(true){
                Thread.sleep(3000);
                System.out.println("aa");
                out.println(i++);
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}