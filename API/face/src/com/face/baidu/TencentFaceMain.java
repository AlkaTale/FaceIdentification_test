package com.face.baidu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.face.baidu.sign.TencentAISign;
import com.face.baidu.sign.TencentAISignSort;
import com.face.baidu.util.*;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TencentFaceMain {
    public static String request(byte[] bytes1,byte[] bytes2) throws Exception{
        String image1 = Base64Util.encode(bytes1);
        String image2 = Base64Util.encode(bytes2);
        //时间戳
        String time_stamp = System.currentTimeMillis()/1000+"";
        //随机字符串
        String nonce_str = TencentAISign.getRandomString(10);
        Map<String,String> body = new HashMap<>();
        body.put("app_id", String.valueOf(TencentAPI.APP_ID_AI));
        body.put("time_stamp",time_stamp);
        body.put("nonce_str", nonce_str);
        body.put("image_a", image1);
        body.put("image_b", image2);
        String sign = TencentAISignSort.getSignature(body);
        body.put("sign", sign);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        try{
            HttpResponse responseBD = HttpsUtil4Tencent.doPostTencentAI(TencentAPI.FACE_API, headers, body);
            System.out.println(responseBD);
            String json = EntityUtils.toString(responseBD.getEntity());
            System.out.println("Result:"+json);
            JSONObject jsonObject= JSON.parseObject(json);
            String data = jsonObject.getString("data");
            JSONObject jsondata= JSON.parseObject(data);
            String similarity = jsondata.getString("similarity");
            return similarity;
        }catch (Exception e){
            return "0";
        }
    }


    public static void main(String[] args) throws Exception{

        String [] folderlist = {"Bald", "Bangs", "Black_Hair", "Blond_Hair", "Brown_Hair", "Bushy_Eyebrows", "Eyeglasses", "Male", "Mouth_Slightly_Open", "Mustache", "No_Beard", "Pale_Skin", "Young"};
        File original = new File(FilePath.ORINIGAL);
        File[] fs = original.listFiles();
        List<Map<String,String>> listresult = new ArrayList<Map<String,String>>();
//        Map<String,String> score = new HashMap<String,String>();
        for(File f:fs){
            if(!f.isDirectory()){
                Map<String,String> score = new HashMap<String,String>();
                System.out.println(f.toString());
                System.out.println(f.getName());
                byte[] bytes1 = FileUtil.readFileByBytes(f.toString());
                for (String s: folderlist){
                    System.out.println("文件:" + s + "\\" +f.getName());
                    byte[] bytes2 = FileUtil.readFileByBytes(FilePath.BASE_FILE + "\\" + s +"\\" + f.getName());
                    String result = request(bytes1, bytes2);
//                    System.out.println("输出结果："+ result);
                    score.put(s,result);
                    Thread.sleep(2000);
                    System.out.println(score);
                }
                listresult.add(score);
            }
        }
        System.out.println(listresult);
        Excel excel = new Excel();
        excel.downUserList(listresult);




////        创建参数队列
//        byte[] bytes1 = FileUtil.readFileByBytes("D:\\1NJUST\\人脸识别软件测试\\pf_10_imgs\\Aaron Eckhart\\newcpp_1.jpg");
//        byte[] bytes2 = FileUtil.readFileByBytes("D:\\1NJUST\\人脸识别软件测试\\pf_10_imgs\\Aaron Eckhart\\newcpp_2.jpg");
////        byte[] bytes2 = FileUtil.readFileByBytes("C:\\Users\\Lemon\\Desktop\\test3.jpg");
//        String image1 = Base64Util.encode(bytes1);
//        String image2 = Base64Util.encode(bytes2);
//        //时间戳
//        String time_stamp = System.currentTimeMillis()/1000+"";
//        //随机字符串
//        String nonce_str = TencentAISign.getRandomString(10);
//        Map<String,String> body = new HashMap<>();   //【接口属性】（注意修改成自己要的参数）
//        body.put("app_id", String.valueOf(TencentAPI.APP_ID_AI));
//        body.put("time_stamp",time_stamp);
//        body.put("nonce_str", nonce_str);
//        body.put("image_a", image1);
//        body.put("image_b", image2);
//        String sign = TencentAISignSort.getSignature(body);
//        body.put("sign", sign);
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type", "application/x-www-form-urlencoded");
//        HttpResponse responseBD = HttpsUtil4Tencent.doPostTencentAI(TencentAPI.FACE_API, headers, body);
//        System.out.println(responseBD);
//        String json = EntityUtils.toString(responseBD.getEntity());
//        System.out.println("Result:"+json);
//        JSONObject jsonObject= JSON.parseObject(json);
//        String data = jsonObject.getString("data");
//        JSONObject jsondata= JSON.parseObject(data);
//        String similarity = jsondata.getString("similarity");
//        System.out.println(similarity);
    }
}
