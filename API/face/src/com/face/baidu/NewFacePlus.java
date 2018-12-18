package com.face.baidu;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.activation.ActivationGroup_Stub;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.face.baidu.util.*;
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

public class NewFacePlus {


    public static void main(String[] args) throws Exception {

        String [] folderlist = {"Bald", "Bangs", "Black_Hair", "Blond_Hair", "Brown_Hair", "Bushy_Eyebrows", "Eyeglasses", "Male", "Mouth_Slightly_Open", "Mustache", "No_Beard", "Pale_Skin", "Young"};
//        String [] folderlist = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
        File base = new File(FilePath.NEW_BASE);
        File[] fs = base.listFiles();
        List<Map<Integer,String>> listresult = new ArrayList<>();
        for(File f:fs){
            System.out.println("当前运行目录："+f.getName());
            Map<Integer,String> score = new HashMap<Integer,String>();
            byte[] bytes1 = null;
            Integer i =1;
            if(f.isDirectory()){
                File person = new File(FilePath.NEW_BASE + "\\" + f.getName());
                File[] facelist = person.listFiles();
                for (File e:facelist){
                    String filename = e.getName();
                    String temp = "0.jpg";
                    byte[] bytes2 = null;
                    if (filename.equals(temp)){
                        bytes1 = FileUtil.readFileByBytes(e.toString());
                    }else{
                        bytes2 = FileUtil.readFileByBytes(e.toString());
                    }
                    if (bytes1 != null && bytes2 != null) {
                        String result = (String)paramHandle(bytes1,bytes2);
                        score.put(i,result);
                        i++;
                    }
                }
                listresult.add(score);
            }
        }
        System.out.println(listresult);
        NewExcel excel = new NewExcel();
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
