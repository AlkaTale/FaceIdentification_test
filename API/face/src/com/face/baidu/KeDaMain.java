package com.face.baidu;

import com.face.baidu.util.Base64Util;
import com.face.baidu.util.FileUtil;
import com.face.baidu.util.GsonUtils;
import com.face.baidu.util.HttpUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class KeDaMain {
    public static void main(String[] args) throws Exception{
        //设置网址
        String url = "http://api.xfyun.cn/v1/service/v1/image_identify/face_verification";
        String appid = "5c10f9c0";
        String appkey = "ad3a4f686e020217a71be609317db205";

//        创建参数队列
        byte[] bytes1 = FileUtil.readFileByBytes("D:\\1NJUST\\人脸识别软件测试\\pf_10_imgs\\Aaron Eckhart\\newcpp_1.jpg");
        byte[] bytes2 = FileUtil.readFileByBytes("D:\\1NJUST\\人脸识别软件测试\\pf_10_imgs\\Aaron Eckhart\\newcpp_2.jpg");
        String image1 = Base64Util.encode(bytes1);
        String image2 = Base64Util.encode(bytes2);
        System.out.println("image1:"+image1);
        System.out.println("image2:"+image2);
        List<BasicNameValuePair> formparams = new ArrayList<>();
        formparams.add(new BasicNameValuePair("first_image", image1));
        formparams.add(new BasicNameValuePair("second_image", image2));

        String param = GsonUtils.toJson(formparams);
//      发送请求
        String result = HttpUtil.kdsendPost(url, param, appid,appkey);
        System.out.println(result);
    }

    /**
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
     */
    public static void post(List<BasicNameValuePair> formparams,String url) {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(url);
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);

            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    System.out.println("--------------------------------------");
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
    }
}
