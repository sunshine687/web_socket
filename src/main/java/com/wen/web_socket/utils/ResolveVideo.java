package com.wen.web_socket.utils;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 视频解析工具类
 */
@Component
public class ResolveVideo {
    public String resolveHtml(String urlString){
        Document doc = null;
        InputStream in = null;
        String videoUrl = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //使用代理且需要登录，添加这段代码
            /*conn.setRequestProperty("Proxy-Authorization", " Basic " +
            new BASE64Encoder().encode("用户名:密码".getBytes()));*/
            //该项必须配置，很多网站会拒绝非浏览器的访问，不设置会返回403，访问被服务器拒绝
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "text/html");
            conn.setRequestProperty("Connection", "close");
            conn.setUseCaches(false);
            conn.setConnectTimeout(5 * 1000);
            String encode = "utf-8";
            in = conn.getInputStream();
            doc = Jsoup.parse(in,encode,urlString);
            videoUrl = doc.toString();
            System.out.println(videoUrl);
            return videoUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(null != in){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public String doHttp(String url) throws IOException {
        //构造一个webClient 模拟Chrome 浏览器
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //支持JavaScript
        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
//        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
        //设置一个运行JavaScript的时间
        webClient.waitForBackgroundJavaScript(30000);

        HtmlPage page = null;
        try {
            page = webClient.getPage(url);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            webClient.close();
        }

        String pageXml = page.asXml();//直接将加载完成的页面转换成xml格式的字符串
        Document document = Jsoup.parse(pageXml);//获取html文档
        System.out.println(document.select("title").text());
//        List<Element> infoListEle = document.getElementById("feedCardContent").getElementsByAttributeValue("class", "feed-card-item");//获取元素节点等
//        for (int i = 0; i < infoListEle.size(); i++) {
//            System.out.println(infoListEle.get(i).getElementsByTag("h2").first().getElementsByTag("a").text());
//            System.out.println(infoListEle.get(i).getElementsByTag("h2").first().getElementsByTag("a").attr("href"));
//        }
        return "";
    }
}
