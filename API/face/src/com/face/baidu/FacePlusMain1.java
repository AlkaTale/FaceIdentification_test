package com.face.baidu;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.face.baidu.util.Base64Util;
import com.face.baidu.util.Excel;
import com.face.baidu.util.FilePath;
import com.face.baidu.util.FileUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class FacePlusMain1 {


    public static void main(String[] args) throws Exception {

        String [] folderlist = {"Bald", "Bangs", "Black_Hair", "Blond_Hair", "Brown_Hair", "Bushy_Eyebrows", "Eyeglasses", "Male", "Mouth_Slightly_Open", "Mustache", "No_Beard", "Pale_Skin", "Young"};
//        String [] folderlist = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
        File original = new File(FilePath.ORINIGAL);
        File[] fs = original.listFiles();
        List<Map<String,String>> listresult = new ArrayList<>();
//        Map<String,String> score = new HashMap<>();
        for(File f:fs){
            if(!f.isDirectory()){
                Map<String,String> score = new HashMap<String,String>();
                System.out.println(f.toString());
                System.out.println(f.getName());
                byte[] bytes1 = FileUtil.readFileByBytes(f.toString());
                for (String s: folderlist){
                    System.out.println(s);
                    byte[] bytes2 = FileUtil.readFileByBytes(FilePath.BASE_FILE + "\\" + s +"\\" + f.getName());
                    String result = (String)paramHandle(bytes1,bytes2);
                    score.put(s,result);
                }
                listresult.add(score);
            }
        }
        System.out.println(listresult);
        Excel excel = new Excel();
        excel.downUserList(listresult);
    }

    public static Object paramHandle(byte[] bytes1, byte[] bytes2){
        String url = "https://api-cn.faceplusplus.com/facepp/v3/compare";
        String image1 = Base64Util.encode(bytes1);
        String image2 = Base64Util.encode(bytes2);

        List<BasicNameValuePair> formparams = new ArrayList<>();
        formparams.add(new BasicNameValuePair("api_key", "zbZsTY5PyUGtM-7uJgZL2-xv5eJsSn-A"));
        formparams.add(new BasicNameValuePair("api_secret", "kkxJIUPiVqTJ7dZKXF_vr627boao9Eex"));
        formparams.add(new BasicNameValuePair("image_base64_1", image1));
        formparams.add(new BasicNameValuePair("image_base64_2", image2));
//      发送请求
        return post(formparams,url);
    }


    /**
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
     */
    public static String post(List<BasicNameValuePair> formparams,String url) {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(url);
        UrlEncodedFormEntity uefEntity;
        String finalScore = "0";
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
//            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
//                    System.out.println("--------------------------------------");
                    String res = EntityUtils.toString(entity, "UTF-8");
                    System.out.println("Response: " + res);
                    JSONObject jsonObject= JSON.parseObject(res);

                    try{
                        String data = jsonObject.getString("confidence");
                        if (data == null){
                            System.out.println("识别错误!");
                            return finalScore;
                        }else {
                            finalScore = data;
                            System.out.println("confidence:"+finalScore);
                        }
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }

                    JsonObject json = new JsonParser().parse(res).getAsJsonObject();
                    System.out.println(json.get("confidence").getAsString());
                    finalScore = json.get("confidence").getAsString();

//                    System.out.println("--------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return finalScore;
    }
}
