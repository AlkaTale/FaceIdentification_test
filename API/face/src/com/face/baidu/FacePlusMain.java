package com.face.baidu;

import com.face.baidu.util.Base64Util;
import com.face.baidu.util.FileUtil;
import com.face.baidu.util.GsonUtils;
import com.face.baidu.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacePlusMain {
    public static String match() {
        // 请求url
        String url = "https://api-cn.faceplusplus.com/facepp/v3/compare";
        try {

            byte[] bytes1 = FileUtil.readFileByBytes("D:\\1NJUST\\人脸识别软件测试\\pf_10_imgs\\Aaron Eckhart\\newcpp_1.jpg");
            byte[] bytes2 = FileUtil.readFileByBytes("D:\\1NJUST\\人脸识别软件测试\\pf_10_imgs\\Aaron Eckhart\\newcpp_2.jpg");
            String image1 = Base64Util.encode(bytes1);
            String image2 = Base64Util.encode(bytes2);


            Map<String, Object> map1 = new HashMap<>();
            map1.put("api_key", "zbZsTY5PyUGtM-7uJgZL2-xv5eJsSn-A");
            map1.put("api_secret", "kkxJIUPiVqTJ7dZKXF_vr627boao9Eex");
            map1.put("image_base64_1", image1);
            map1.put("image_base64_2", image2);

            System.out.println(map1);

            String param = GsonUtils.toJson(map1);
            System.out.println("param:"+param);

            String result = HttpUtil.sendPost(url, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        FacePlusMain.match();
    }
}
