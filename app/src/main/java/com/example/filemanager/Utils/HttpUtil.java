package com.example.filemanager.Utils;


import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author yt
 * 处理拖拽功能的类
 */
public class HttpUtil {
    //调用登录的http后台接口
    public Map getLoginDate(Map map) throws IOException {
        //根据地址创建URL对象(网络访问
        //发布文章的url)
        URL url = new URL("http://150.158.28.238:3000/users/login");
        HttpURLConnection conn = (HttpURLConnection)
                //设置请求的方式
                url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);//发送POST请求必须设置允许输出
        conn.setDoOutput(true);//发送POST请求必须设置允许输入
        //设置请求的头
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Charset", "utf-8");
        JSONObject json = new JSONObject(map);
        //获取输出流
        OutputStream os = conn.getOutputStream();
        os.write(json.toJSONString().getBytes());
        os.flush();
        //获取响应的输入流对象
        InputStreamReader is = new InputStreamReader(conn.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(is);
        StringBuffer strBuffer = new StringBuffer();
        String line = null;
        //读取服务器返回信息
        while ((line = bufferedReader.readLine()) != null) {
            strBuffer.append(line);
        }
        String result = strBuffer.toString();//接收从服务器返回的数据
        System.out.println("收到的信息"+result);
        JSONObject rejson = JSONObject.parseObject(result);
        Map<String, Object> resultmap = (Map<String, Object>)rejson;
        //关闭InputStream、关闭http连接
        is.close();
        conn.disconnect();
        return resultmap;
    }
    //调用注册的http后台接口
    public Map getRegDate(Map map) throws IOException {
        //根据地址创建URL对象(网络访问
        //发布文章的url)
        URL url = new URL("http://150.158.28.238:3000/users/reg");
        HttpURLConnection conn = (HttpURLConnection)
                //设置请求的方式
                url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);//发送POST请求必须设置允许输出
        conn.setDoOutput(true);//发送POST请求必须设置允许输入
        //设置请求的头
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Charset", "utf-8");
        JSONObject json = new JSONObject(map);
        //获取输出流
        OutputStream os = conn.getOutputStream();
        os.write(json.toJSONString().getBytes());
        os.flush();
        //获取响应的输入流对象
        InputStreamReader is = new InputStreamReader(conn.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(is);
        StringBuffer strBuffer = new StringBuffer();
        String line = null;
        //读取服务器返回信息
        while ((line = bufferedReader.readLine()) != null) {
            strBuffer.append(line);
        }
        String result = strBuffer.toString();//接收从服务器返回的数据
        System.out.println("收到的信息"+result);
        JSONObject rejson = JSONObject.parseObject(result);
        Map<String, Object> resultmap = (Map<String, Object>)rejson;
        //关闭InputStream、关闭http连接
        is.close();
        conn.disconnect();
        return resultmap;
    }
}
