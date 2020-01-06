package com.wen.web_socket.websocket_h5;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static ServerSocket mServerSocker;
    private static Socket sc;
    public void init() throws IOException {
        mServerSocker=new ServerSocket(8888);
        while(true){
            sc=mServerSocker.accept();
            System.out.println("aaaaaaaa");
            //Server s=new Server();
            Thread r=new ReciveThreat(sc);
            r.start();
            Thread s=new SendThread(sc);
            s.start();
            System.out.println("kaishi");
        }
    }
}
