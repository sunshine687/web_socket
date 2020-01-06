package com.wen.web_socket.websocket_h5;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReciveThreat extends Thread{
    private Socket sc;
    private BufferedReader in;
    //private PrintWriter out;
    public ReciveThreat(Socket s) throws IOException {
        sc=s;
        in=new BufferedReader(new InputStreamReader(sc.getInputStream()));
        //out=new PrintWriter(new BufferedWriter(new OutputStreamWriter(sc.getOutputStream())),true);
    }
    @Override
    public void run() {
        while(true){
            try {
                String str;
                str = in.readLine();
                if(str!=null){
                    System.out.println("read:"+str);
                    System.out.println("run......");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}