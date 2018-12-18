package com.face.baidu.util;

import com.google.gson.Gson;
import sun.security.provider.MD5;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * http 工具类
 */
public class HttpUtil {

    public static String post(String requestUrl, String accessToken, String params)
            throws Exception {
        String contentType = "application/x-www-form-urlencoded";
        return HttpUtil.post(requestUrl, accessToken, contentType, params);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params)
            throws Exception {
        String encoding = "UTF-8";
        if (requestUrl.contains("nlp")) {
            encoding = "GBK";
        }
        return HttpUtil.post(requestUrl, accessToken, contentType, params, encoding);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params, String encoding)
            throws Exception {
        String url = requestUrl + "?access_token=" + accessToken;
        return HttpUtil.postGeneralUrl(url, contentType, params, encoding);
    }

    public static String postGeneralUrl(String generalUrl, String contentType, String params, String encoding)
            throws Exception {
        URL url = new URL(generalUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(params.getBytes(encoding));
        out.flush();
        out.close();

        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> headers = connection.getHeaderFields();
        // 遍历所有的响应头字段
//        for (String key : headers.keySet()) {
//            System.err.println(key + "--->" + headers.get(key));
//        }
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), encoding));
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result += getLine;
        }
        in.close();
//        System.err.println("result:" + result);
        return result;
    }

    public static String sendPost(String url, String param) throws Exception {
        //创建连接
        URL urlPost = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlPost.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("connection", "keep-alive");
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(30000);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.connect();
        // POST请求
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        String json = param.toString();
        out.writeChars(json); // 这行是关键我之前写的是 out.write(json.getBytes());
        System.out.println(json);
        out.flush();
        // 读取响应
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String lines;
        StringBuffer sb = new StringBuffer("");
        while ((lines = reader.readLine()) != null) {
            lines = new String(lines.getBytes(), "utf-8");
            sb.append(lines);
        }
        String jsStr = GsonUtils.toJson(sb.toString());
        //获取响应值，判断是否验证通过
//        String code = (String) jsStr.get("code");
//        String msg=(String) jsStr.get("msg");
//        System.out.println("code:"+code+",msg:"+msg);
//        //接口返回验证数据是否通过
//        if("0".equals(code)){
//            result = "success";
//        } else{
//            result = "fail";
//            System.out.println("下发出错:错误原因为" + msg + "下发内容为:" + json);
//        }
        reader.close();
        // 断开连接
        connection.disconnect();
        return jsStr;
    }

    /**
     * MD5加密
     * @param s
     * @return
     */
    public final static String encode(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4',
                '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] md = mdInst.digest();
            //把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 科大讯飞
     * @param url
     * @param param
     * @param apiKey
     * @return
     * @throws Exception
     */
    public static String kdsendPost(String url, String param,String appid, String apiKey) throws Exception {
        //创建连接
        URL urlPost = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlPost.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("connection", "keep-alive");
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(30000);
        connection.setUseCaches(false);
        long s = System.currentTimeMillis()/1000;

        String originalInput = "{auto_rotate: false,}";
        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());

        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        connection.setRequestProperty("X-Appid", appid);
        connection.setRequestProperty("X-Param",encodedString);
        connection.setRequestProperty("X-CurTime",Long.toString(s));
        String temp = "ad3a4f686e020217a71be609317db205"+Long.toString(s) + encodedString;
        System.out.println("need MD5:"+temp);
        connection.setRequestProperty("X-CheckSum", encode(apiKey + Long.toString(s) + encodedString));
        System.out.println("X-CurTime:"+s);
        System.out.println("PARAMBASE64:"+encodedString);
        System.out.println("X-CheckSum:"+encode(apiKey + Long.toString(s) + encodedString));
        connection.connect();
        // POST请求
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        String json = param.toString();
        out.writeChars(json); // 这行是关键我之前写的是 out.write(json.getBytes());
        System.out.println(json);
        out.flush();
        // 读取响应
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String lines;
        StringBuffer sb = new StringBuffer("");
        while ((lines = reader.readLine()) != null) {
            lines = new String(lines.getBytes(), "utf-8");
            sb.append(lines);
        }
        String jsStr = GsonUtils.toJson(sb.toString());
        //获取响应值，判断是否验证通过
//        String code = (String) jsStr.get("code");
//        String msg=(String) jsStr.get("msg");
//        System.out.println("code:"+code+",msg:"+msg);
//        //接口返回验证数据是否通过
//        if("0".equals(code)){
//            result = "success";
//        } else{
//            result = "fail";
//            System.out.println("下发出错:错误原因为" + msg + "下发内容为:" + json);
//        }
        reader.close();
        // 断开连接
        connection.disconnect();
        return jsStr;
    }

}
