package com.face.baidu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.face.baidu.util.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewBaidu {
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
        File base = new File("D:\\111");
        File[] fs = base.listFiles();
        List<Map<Integer,String>> listresult = new ArrayList<>();
        for(File f:fs){
            System.out.println("当前运行目录："+f.getName());
            Map<Integer,String> score = new HashMap<Integer,String>();
            byte[] bytes1 = null;
            Integer i =1;
            if(f.isDirectory()){
                File person = new File("D:\\111\\"+f.getName());
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
                        String result = (String)match(bytes1,bytes2);
                        System.out.println("识别结果-"+result);
                        score.put(i,result);
                        i++;
                        Thread.sleep(3000);
                    }
                }
                listresult.add(score);
            }
        }
        System.out.println(listresult);
        NewExcel excel = new NewExcel();
        excel.downUserList(listresult);
    }
}
