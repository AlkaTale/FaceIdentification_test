package com.face.baidu.util;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class NewExcel {
    public XSSFWorkbook createUserListExcel(List<Map<Integer,String>> listresult){
        // 1.创建HSSFWorkbook，一个HSSFWorkbook对应一个Excel文件
        XSSFWorkbook wb = new XSSFWorkbook();
        // 2.在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = wb.createSheet("sheet1");
        // 3.设置表头，即每个列的列名
        String[] titel = {"Bald", "Bangs", "Black_Hair", "Blond_Hair", "Brown_Hair", "Bushy_Eyebrows", "Eyeglasses", "Male", "Mouth_Slightly_Open", "Mustache", "No_Beard", "Pale_Skin", "Young"};
        // 3.1创建第一行
        XSSFRow row = sheet.createRow(0);
        // 此处创建一个序号列
        row.createCell(0).setCellValue("序号");
        // 将列名写入
        for (int i = 0; i < titel.length; i++) {
            // 给列写入数据,创建单元格，写入数据
            row.createCell(i+1).setCellValue(titel[i]);
        }
        // 写入正式数据
        for (int i = 0; i < listresult.size(); i++) {
            // 创建行
            row = sheet.createRow(i+1);
            // 序号
            row.createCell(0).setCellValue(i+1);

            row.createCell(1).setCellValue(listresult.get(i).get(1).toString());
            row.createCell(2).setCellValue(listresult.get(i).get(2).toString());
            row.createCell(3).setCellValue(listresult.get(i).get(3).toString());
            row.createCell(4).setCellValue(listresult.get(i).get(4).toString());
            row.createCell(5).setCellValue(listresult.get(i).get(5).toString());
            row.createCell(6).setCellValue(listresult.get(i).get(6).toString());
            row.createCell(7).setCellValue(listresult.get(i).get(7).toString());
            row.createCell(8).setCellValue(listresult.get(i).get(8).toString());
            row.createCell(9).setCellValue(listresult.get(i).get(9).toString());
            row.createCell(10).setCellValue(listresult.get(i).get(10).toString());
            row.createCell(11).setCellValue(listresult.get(i).get(11).toString());
            row.createCell(12).setCellValue(listresult.get(i).get(12).toString());
//            row.createCell(13).setCellValue(listresult.get(i).get(13).toString());

        }
        /**
         * 上面的操作已经是生成一个完整的文件了，只需要将生成的流转换成文件即可；
         * 下面的设置宽度可有可无，对整体影响不大
         */
        // 设置单元格宽度
        int curColWidth = 0;
        for (int i = 0; i <= titel.length; i++) {
            // 列自适应宽度，对于中文半角不友好，如果列内包含中文需要对包含中文的重新设置。
            sheet.autoSizeColumn(i, true);
            // 为每一列设置一个最小值，方便中文显示
            curColWidth = sheet.getColumnWidth(i);
            if(curColWidth<2500){
                sheet.setColumnWidth(i, 2500);
            }
            // 第3列文字较多，设置较大点。
//            sheet.setColumnWidth(3, 8000);
        }
        return wb;
    }
    /**
     * 用户列表导出
     * @param
     */
    public String downUserList(List<Map<Integer,String>> listresult){
        // getTime()是一个返回当前时间的字符串，用于做文件名称
//        String name = getTime();
        //  csvFile是我的一个路径，自行设置就行
//      String name = "BaiduResult";
        String name = "FacePlusResult";
//        String name = "TencentResult";
        String base = "D:\\1Pro\\JavaProject";
        String ys = base+ "\\"  + name + ".xlsx";
        // 1.生成Excel
        XSSFWorkbook userListExcel = createUserListExcel(listresult);
        try{
            // 输出成文件
            File file = new File(base);
            if(file.exists() || !file.isDirectory()) {
                file.mkdirs();
            }
            FileOutputStream outputStream = new FileOutputStream(new File(ys));
            userListExcel.write(outputStream);
            outputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return name;
    }
}
