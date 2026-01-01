package com.fmx.xiaomeng.common.utils.jsoup;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GetSafeCode {

    private String url_safecode = "http://jwxt.gdufe.edu.cn/jsxsd/verifycode.servlet"; // 验证码


    // TODO: 2023/9/24  https://github.com/WindrunnerMax/SWVerifyCode
    public String resolveSafeCode(byte[] bytes) throws IOException {
        ImgIdenfy imgIdenfy = new ImgIdenfy();
        //InputStream inputStream = new URL(url_safecode).openStream();

        InputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage sourceImg = ImageIO.read(inputStream);
//        imgIdenfy.writeImage(sourceImg);
        int[][] imgArr = imgIdenfy.binaryImg(sourceImg); // 二值化
        imgIdenfy.removeByLine(imgArr); // 去除干扰先 引用传递
        int[][][] imgArrArr = imgIdenfy.imgCut(imgArr,
                new int[][]{new int[]{4, 13}, new int[]{14, 23}, new int[]{24, 33}, new int[]{34, 43}},
                new int[][]{new int[]{4, 16}, new int[]{4, 16}, new int[]{4, 16}, new int[]{4, 16}},
                4);
        return imgIdenfy.matchCode(imgArrArr);
    }

//    public String ocrResolveSafeCode(byte[] bytes) {
//        //InputStream inputStream = new URL(url_safecode).openStream();
//
//        try {
//            InputStream inputStream = new ByteArrayInputStream(bytes);
//            BufferedImage sourceImg = ImageIO.read(inputStream);
////        imgIdenfy.writeImage(sourceImg);
//
//            Tesseract tesseract = new Tesseract();
//            tesseract.setDatapath("D:\\Java\\IdeaProjects\\xiaomeng\\src\\main\\resources\\tessdata");
//            String result = tesseract.doOCR(sourceImg);
//
//            return result;
//        }catch (Exception e){
//            System.out.println("识别错误");
//        }
//        return "";
//
//    }
}
