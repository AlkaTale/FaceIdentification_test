package com.face.baidu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.face.baidu.util.*;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaiduFaceMain {

    public static String match(byte[] bytes1,byte[] bytes2) throws Exception{
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/match";
        String score;

        String image1 = Base64Util.encode(bytes1);
        String image2 = Base64Util.encode(bytes2);
        List<Map<String, Object>> images = new ArrayList<>();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("image", image1);
        map1.put("image_type", "BASE64");
        map1.put("face_type", "LIVE");
        map1.put("quality_control", "LOW");
        map1.put("liveness_control", "NORMAL");

        Map<String, Object> map2 = new HashMap<>();
        map2.put("image", image2);
        map2.put("image_type", "BASE64");
        map2.put("face_type", "LIVE");
        map2.put("quality_control", "LOW");
        map2.put("liveness_control", "NORMAL");

        images.add(map1);
        images.add(map2);

        String param = GsonUtils.toJson(images);

        String accessToken = "24.20475ab11d75661a4500e65a9c03a5cb.2592000.1547194847.282335-15140700";

        String result = HttpUtil.post(url, accessToken, "application/json", param);
        System.out.println("请求相应结果"+result);
        JSONObject jsonObject= JSON.parseObject(result);
        String err = jsonObject.getString("error_msg");
        String data = jsonObject.getString("result");
        JSONObject jsondata= JSON.parseObject(data);
        try{
            score = jsondata.getString("score");
            System.out.println("score:"+score);
        }catch (Exception e){
            System.out.println("错误信息："+err);
            return err;
        }
        return score;
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
                    System.out.println("文件名"+s);
                    byte[] bytes2 = FileUtil.readFileByBytes(FilePath.BASE_FILE + "\\" + s +"\\" + f.getName());
                    String result = BaiduFaceMain.match(bytes1,bytes2);
                    System.out.println("输出结果："+ result);
                    score.put(s,result);
                    Thread.sleep(3000);
                    System.out.println(score);
                }
                listresult.add(score);
            }
        }
        System.out.println(listresult);
        Excel excel = new Excel();
        excel.downUserList(listresult);
    }
}
