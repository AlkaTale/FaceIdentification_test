package com.face.baidu.sign;

import com.face.baidu.util.TencentAPI;

import java.net.URLEncoder;
import java.util.Random;

public class TencentAISign {
    /**
     * 腾讯AI获取SIGN方法
     * @param appId 控制台获取
     * @param image 接口请求数据，UTF-8编码
     * @param nonce_str 请求随机字符串
     * @param mode 检测模式，0-正常，1-大脸模式（默认1）
     * @return
     * @throws Exception
     */
    public static String appSignAI4FaceDetect(Integer appId,String nonce_str,String image,Integer mode) throws Exception {
        return appSignBaseAI4FaceDetect(appId,nonce_str,image,mode);
    }
    /**
     * 计算SIGN
     * @param appId 控制台获取
     * @param nonce_str 请求随机字符串，用于保证签名不可预测
     * @return
     * @throws Exception
     */
    private static String appSignBaseAI4FaceDetect(Integer appId,
                                                   String nonce_str,String image,Integer mode) throws Exception {
        String time_stamp = System.currentTimeMillis()/1000+"";
        String plain_text = "app_id=" + URLEncoder.encode(appId.toString(),"UTF-8")
                + "&image=" + URLEncoder.encode(image,"UTF-8")
                +"&nonce_str=" + URLEncoder.encode(nonce_str,"UTF-8")
                +"&mode=" + URLEncoder.encode(mode.toString(),"UTF-8")
                +"&time_stamp=" + URLEncoder.encode(time_stamp,"UTF-8");
        System.out.println(plain_text);
        String plain_text_encode = plain_text+"&app_key="+ TencentAPI.APP_KEY_AI;
        System.out.println(plain_text_encode);
        String sign = MD5.getMD5(plain_text_encode);
        System.out.println("sign:"+sign);
        return sign;
    }
    public static String appSignBaseAI4PersionDetect(Integer appId,
                                                     String nonce_str,String image,Integer card_type) throws Exception {
        String time_stamp = System.currentTimeMillis()/1000+"";
        String plain_text = "app_id=" + URLEncoder.encode(appId.toString(),"UTF-8")
                +"&card_type=" + URLEncoder.encode(card_type.toString(),"UTF-8")
                +"&nonce_str=" + URLEncoder.encode(nonce_str,"UTF-8")
                +"&time_stamp=" + URLEncoder.encode(time_stamp,"UTF-8")
                +"&image="+URLEncoder.encode(image,"UTF-8");
        String plain_text_encode = plain_text+"&app_key="+TencentAPI.APP_KEY_AI;
        System.out.println(plain_text_encode);
        String sign = MD5.getMD5(plain_text_encode);
        System.out.println("sign:"+sign);
        return sign;
    }


    /**
     *
     * @param length 表示生成字符串的长度
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
