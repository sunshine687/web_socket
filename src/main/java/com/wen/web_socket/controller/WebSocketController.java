package com.wen.web_socket.controller;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */

@ServerEndpoint("/websocket/chat/{chatGroup}")
public class WebSocketController {
    //用于存储所有连接
    private static HashMap<String, CopyOnWriteArraySet> mst = new HashMap<String, CopyOnWriteArraySet>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("chatGroup") String chatGroup) {
        Boolean flag = isExist(chatGroup);
        //如果没有该线路，添加聊天线路
        if (!flag) {
            CopyOnWriteArraySet<WebSocketController> webSocketSet = new CopyOnWriteArraySet<WebSocketController>();
            this.session = session;
            webSocketSet.add(this);     //加入map中
            mst.put(chatGroup, webSocketSet);
        }
        //如果已经有了该线路则直接加入该线路
        else {
            for (Map.Entry<String, CopyOnWriteArraySet> entry : mst.entrySet()) {
                if (chatGroup.equals(entry.getKey())) {
                    CopyOnWriteArraySet<WebSocketController> s = entry.getValue();
                    this.session = session;
                    s.add(this);//加入map中
                    mst.put(chatGroup, s);
                }
            }
        }
        if (mst.size() != 0) {
            System.out.println("[线路名称【chat】： " + chatGroup + "] / [在线人数 " + mst.get(chatGroup).size() + "]");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("chatGroup") String chatGroup) {
        Boolean flag = isExist(chatGroup);
        //如果有该线路，删除聊天线路
        if (flag) {
            for (Map.Entry<String, CopyOnWriteArraySet> entry : mst.entrySet()) {
                if (chatGroup.equals(entry.getKey())) {
                    CopyOnWriteArraySet<WebSocketController> s = entry.getValue();
                    s.remove(this);//将用户从当前线路中移除
                    mst.put(chatGroup, s);
                }
            }
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("chatGroup") String chatGroup) {
        for (Map.Entry<String, CopyOnWriteArraySet> entry : mst.entrySet()) {
            if (chatGroup.equals(entry.getKey())) {
                CopyOnWriteArraySet<WebSocketController> s = entry.getValue();
                for (WebSocketController webSocketTest : s) {
                    try {
                        webSocketTest.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 判断是否存在当前线路
     *
     * @param chatGroup
     * @return
     */
    public Boolean isExist(String chatGroup) {
        boolean flag = false;
        for (Map.Entry<String, CopyOnWriteArraySet> entry : mst.entrySet()) {
            if (chatGroup.equals(entry.getKey())) {
                flag = true;
            }
        }
        return flag;
    }
}
