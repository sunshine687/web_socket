package com.wen.web_socket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * websocket客户端，可直接与线路通信
 */
public class WebSocketClient_2 extends WebSocketClient {

   public WebSocketClient_2(URI uri){
     super(uri);
   }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onMessage(String s) {

    }
}