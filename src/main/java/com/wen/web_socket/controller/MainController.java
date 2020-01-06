package com.wen.web_socket.controller;

import com.alibaba.fastjson.JSONObject;
import com.wen.web_socket.WebSocketClient_2;
import com.wen.web_socket.utils.HttpClient;
import com.wen.web_socket.utils.MobileUtil;
import com.wen.web_socket.utils.ResolveVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.net.URI;

@CrossOrigin(origins = "*", maxAge = 10000)   //处理跨域请求(接收来自某个地址的跨域请求)
@RestController
@RequestMapping("/wen")
public class MainController {

    @Autowired/* 在工具类中获取配置参数时，必须自动注解方式生成，不能new */
    private MobileUtil mobileUtil;

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private ResolveVideo resolveVideo;

    /**
     * 向websocket服务端发送消息
     * @param userid
     * @param message
     */
    @RequestMapping("/sendMessage")
    public void sendMessage(@PathParam("userid") String userid,@PathParam("message") String message){
        System.out.println("userid:"+userid+"   message:"+message);
        String path = "ws://125.69.67.42:8085/web_socket/websocket/data/"+userid;
        URI uri = URI.create(path);
        WebSocketClient_2 webSocketClient2 = new WebSocketClient_2(uri);
        try {
            webSocketClient2.connectBlocking();
            webSocketClient2.send(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/callRecord")
    public void getCallRecord(@PathParam("xl_cdr") String xl_cdr){
        System.out.println("开始接收推送的通话信息");
        xl_cdr = xl_cdr == null ? "" : xl_cdr;
        JSONObject json = null; //转换成JSON格式
        if(!"".equals(xl_cdr)){
            json = JSONObject.parseObject(xl_cdr); //转换成JSON格式

            String variables = json.getString("variables");//获取variables的信息
            JSONObject variablesJson =null;
            if(!"".equals(variables)){
                variablesJson =JSONObject.parseObject(variables); //将variables转为json对象
            }
            String employee_username = variablesJson.getString("employee_username");//坐席工号
            System.out.println("坐席工号"+employee_username);
            String path = "ws://125.69.67.42:8085/web_socket/websocket/data/"+employee_username;
            URI uri = URI.create(path);
            WebSocketClient_2 webSocketClient2 = new WebSocketClient_2(uri);
            try {
                webSocketClient2.connectBlocking();
                webSocketClient2.send(variablesJson.toJSONString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 获取手机号码归属地
     * @param mobile //手机号码
     * @return //返回自定义封装json数据
     */
    @RequestMapping("/getMobileInfo")
    public String getMobileInfo(@PathParam("mobile") String mobile){
        return mobileUtil.getMobileInfo(mobile);
    }

    @RequestMapping("/resolveHtml")
    public String resolveHtml(@PathParam("url") String url) throws IOException {
        return resolveVideo.doHttp(url);
    }

    @RequestMapping("/doPost")
    public String doPost(@PathParam("url") String url) throws IOException {
        String s = httpClient.doPost("https://www.nxflv.com/api.php","url=https://v.qq.com/x/cover/ix6w4wausx518m8.html&referer=&ref=0&time=1573225211&type=");
        System.out.println(s);
        return "";
    }

    @RequestMapping("/test")
    public String testMessage(){
        System.out.println("测试日志");
        return "test";
    }

}
