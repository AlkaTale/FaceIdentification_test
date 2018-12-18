package com.face.baidu.util;

public class TencentAPI {

    public static final Integer APP_ID_AI = 2110478919;
    //自己的APPKEY
    public static final String APP_KEY_AI = "LhXr0DsGHWpnSAye";

    public static final String FACE_API = "https://api.ai.qq.com/fcgi-bin/face/face_facecompare";
    public static final String PERSON_ID = "https://api.ai.qq.com/fcgi-bin/ocr/ocr_idcardocr";  //身份证识别
    public static final String PHOTO_SPEAK = "https://api.ai.qq.com/fcgi-bin/vision/vision_imgtotext"; //看图说话
    public static final String SCENE_RECOGNITION  = "https://api.ai.qq.com/fcgi-bin/vision/vision_scener"; //场景识别：对图行进行场景识别，快速找出图片中包含的场景信息
    public static final String OBJECT_RECOGNITION = "https://api.ai.qq.com/fcgi-bin/vision/vision_objectr"; //物体识别:对图行进行物体识别，快速找出图片中包含的物体信息
    public static final String IMAGE_LABEL = "https://api.ai.qq.com/fcgi-bin/image/image_tag"; //图像标签识别:识别一个图像的标签信息,对图像分类。
    public static final String FACEMERGE="https://api.ai.qq.com/fcgi-bin/ptu/ptu_facemerge";  //人脸融合
}