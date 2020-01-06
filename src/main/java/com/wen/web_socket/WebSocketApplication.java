package com.wen.web_socket;

import com.wen.web_socket.websocket_h5.ReciveThreat;
import com.wen.web_socket.websocket_h5.SendThread;
import com.wen.web_socket.websocket_h5.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootApplication(scanBasePackages="com")
public class WebSocketApplication extends SpringBootServletInitializer {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(WebSocketApplication.class, args);
        Server s = new Server();
        s.init();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        //Application的类名
        return application.sources(WebSocketApplication.class);
    }
}
