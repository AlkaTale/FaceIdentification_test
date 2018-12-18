# ATTGAN使用说明
***
## 环境要求
1. Tensorflow 1.7或1.8
2. Python 2.7或者3.6
***
## 使用过程
1. 下载本项目
2. 下载训练好的模型
   * 模型解压到 AttGAN-Tensorflow\output\384_shortcut1_inject1_none_hd\checkpoints 文件夹下
   * 模型下载地址：链接: https://pan.baidu.com/s/1TyxmEiZAyoo-GHTIQiZhfw 提取码: 6q29
3. 待转换的图片放在 AttGAN-Tensorflow\data\img_align_celeba 文件夹下
4. 在主目录输入命令：`python test.py --experiment_name 128_shortcut1_inject1_none --test_int 1.0` 开始转换
## 说明
1. 该模型会将待处理的图片转换为384 * 384的图片之后在进行各种转换，可能有些图片转换为384 * 384之后与原图差异较大。
2. 处理完成的图片路径：AttGAN-Tensorflow\output\384_shortcut1_inject1_none_hd\sample_testing\*，每一种转换对应一个文件夹：0-13个文件夹分别对应的变换为：Reconstruction, Bald, Bangs, Black_Hair, Blond_Hair, Brown_Hair, Bushy_Eyebrows, Eyeglasses, Male, Mouth_Slightly_Open, Mustache, No_Beard, Pale_Skin, Young。
3.  原项目训练与测试所使用的数据集地址在[这里](https://pan.baidu.com/s/1eSNpdRG#list/path=%2FCelebA%2FImg )