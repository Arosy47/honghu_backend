package com.fmx.xiaomeng.common.utils;

import lombok.extern.slf4j.Slf4j;
import java.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

//import sun.misc.BASE64Encoder;

/**
 * @Description
 * @Date 2025-12-20
 * @Author honghu
 **/
@Slf4j
public class QRUtil {

    //字符集
    private static final String CHARSET = "utf-8";
    public static final String FORMAT_NAME = "png";
    // 二维码尺寸
    private static int QRCODE_SIZE = 300;
    // 二维码尺寸
    private static int QRCODE_SIZE_HEIGHT = 440;
    // LOGO宽度
    private static  int WIDTH = 80;
    // LOGO高度
    private static  int HEIGHT = 80;

    private static  int TOP_FONT_SIZE = 26;
    private static  int TOP_FONT_HEIGHT = 30;
    private static  int UP_FONT_SIZE = 20;
    private static  int UP_FONT_HEIGHT = 20;

    private static String simsunPath="/opt/skechers/temp/wxaterial/font/simsun.ttc";
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    private static void init() {
        QRCODE_SIZE = 300;
        QRCODE_SIZE_HEIGHT = 440;
        WIDTH = 60;
        HEIGHT = 60;
        TOP_FONT_SIZE = 26;
        TOP_FONT_HEIGHT = 30;
        UP_FONT_SIZE = 20;
        UP_FONT_HEIGHT = 20;
    }
//            for (int y = 0; y < height; y++) {
//                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
//            }
//        }
//        // 插入图片
//        if(StringUtils.isNotEmpty(logoImgUrl)){
//            insertImage(image, logoImgUrl, needCompress,QRCODE_SIZE);
//        }
//        return image;
//    }
//
//    /**
//     *
//     * @param content 二维码
//     * @param logoImgUrl logo
//     * @param topText 头部内容
//     * @param upText 底部内容(支持英文逗号换行, 如：YV山西省晋中市,榆次区喜宝时光,儿童城KIDS|123456789) 最多支持3行显示 否则超出范围我不管自己解决
//     * @param needCompress
//     * @param qrcodesize
//     * @return
//     * @throws Exception
//     */
//    public static BufferedImage createImageAndLogoText(String content, String logoImgUrl, String topText,String upText,boolean needCompress,Integer qrcodesize) throws Exception {
//        init();
//        if(qrcodesize!=null && qrcodesize>0){
//            if(qrcodesize>QRCODE_SIZE){
//                //加上头部、底部文字高度
//                if(qrcodesize==900){
//                    if(StringUtils.isNotEmpty(topText) && StringUtils.isNotEmpty(upText)){
//                        TOP_FONT_SIZE=TOP_FONT_SIZE+30;
//                        UP_FONT_SIZE=UP_FONT_SIZE+20;
//                        TOP_FONT_HEIGHT=TOP_FONT_SIZE;
//                        UP_FONT_HEIGHT=UP_FONT_SIZE;
//                    }
//
//
//                    WIDTH=WIDTH*2;
//                    HEIGHT=HEIGHT*2;
//                }
//                if(qrcodesize==1500){
//                    if(StringUtils.isNotEmpty(topText) && StringUtils.isNotEmpty(upText)){
//                        TOP_FONT_SIZE=TOP_FONT_SIZE+60;
//                        UP_FONT_SIZE=UP_FONT_SIZE+50;
//                        TOP_FONT_HEIGHT=TOP_FONT_SIZE;
//                        UP_FONT_HEIGHT=UP_FONT_SIZE;
//                    }
//
//
//                    WIDTH=WIDTH*2;
//                    HEIGHT=HEIGHT*2;
//                }
//                if(StringUtils.isNotEmpty(topText) || StringUtils.isNotEmpty(upText)){
//                    int up=UP_FONT_HEIGHT*upText.split(",").length;
//                    QRCODE_SIZE_HEIGHT=qrcodesize+140+TOP_FONT_HEIGHT+up+20;
//                }
//            }else{
//                init();
//            }
//            QRCODE_SIZE=qrcodesize;
//        }
//        Hashtable hints = new Hashtable();
//        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
//        hints.put(EncodeHintType.MARGIN, 1);
//        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, // 修改二维码底部高度
//                hints);
//        int width = bitMatrix.getWidth();
//        int height = bitMatrix.getHeight();
//        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
//            }
//        }
//        // 插入图片
//        if(StringUtils.isNotEmpty(logoImgUrl)){
//            insertImage(image, logoImgUrl, needCompress,0);
//        }
//        if(StringUtils.isNotEmpty(topText)){
//            addFontUp(image, topText);
//        }
//        if(StringUtils.isNotEmpty(upText)){
//            String[] upTexts=upText.split(",");
//            int step=3;
//            int up=UP_FONT_SIZE*(upTexts.length-1);
//            int y=QRCODE_SIZE_HEIGHT - (20 * step) - up-40;
//            for(int i=0;i<upTexts.length;i++){
//                if(i>0){
//                    y=y+UP_FONT_SIZE;
//                }
//                addFontImage(image, upTexts[i],step,y);
//                step--;
//            }
//        }
//        return image;
//    }
//
//    private static void insertImage(BufferedImage source, String logoImgUrl, boolean needCompress,int qrcodeheight) throws Exception {
//        Image src = ImageIO.read(new URL(logoImgUrl));
//        int width = src.getWidth(null);
//        int height = src.getHeight(null);
//        if (needCompress) { // 压缩LOGO
//            if (width > WIDTH) {
//                width = WIDTH;
//            }
//            if (height > HEIGHT) {
//                height = HEIGHT;
//            }
//            Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
//            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//            Graphics g = tag.getGraphics();
//            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
//            g.dispose();
//            src = image;
//        }
//        // 插入LOGO
//        Graphics2D graph = source.createGraphics();
//        int x = (QRCODE_SIZE - width) / 2;
//        int y = (QRCODE_SIZE - height) / 2;
////        if(qrcodeheight>0){
////            y = (qrcodeheight - height) / 2;
////        }else{
////            y = (QRCODE_SIZE_HEIGHT - height) / 2;
////        }
//        graph.drawImage(src, x, y, width, height, null);
//        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
//        graph.setStroke(new BasicStroke(6f));
//        graph.draw(shape);
//
////        int padding=5;
////        int canvasWidth = width + padding * 2;
////        int canvasHeight = height + padding * 2;
////        int radius = (width+height) / 6;
////        graph.drawRoundRect(padding, padding, canvasWidth - 2 * padding, canvasHeight - 2 * padding, radius, radius);
//        graph.dispose();
//    }

//    /**
//     * 添加 头部图片文字
//     *
//     * @param source      图片源
//     * @param declareText 文字本文
//     */
//    private static void addFontUp(BufferedImage source, String declareText) {
//        BufferedImage textImage = strToImage(declareText, QRCODE_SIZE, TOP_FONT_HEIGHT,TOP_FONT_SIZE);
//        Graphics2D graph = source.createGraphics();
//        //开启文字抗锯齿
//        graph.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//
//        int width = textImage.getWidth(null);
//        int height = textImage.getHeight(null);
//
//        Image src = textImage;
//        int y=52;
//        if(QRCODE_SIZE==900){
//            y=TOP_FONT_HEIGHT+30;
//        }
//        if(QRCODE_SIZE==1500){
//            y=TOP_FONT_HEIGHT+3;
//        }
//        graph.drawImage(src, 0, y, width, height, null);
//        graph.dispose();
//    }

//    /**
//     * 添加 底部图片文字
//     *
//     * @param source      图片源
//     * @param declareText 文字本文
//     */
//    private static void addFontImage(BufferedImage source, String declareText,int step,int y) {
//        BufferedImage textImage = strToImage(declareText, QRCODE_SIZE, UP_FONT_HEIGHT,UP_FONT_SIZE);
//        Graphics2D graph = source.createGraphics();
//        //开启文字抗锯齿
//        graph.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//
//        int width = textImage.getWidth(null);
//        int height = textImage.getHeight(null);
//
//        Image src = textImage;
////        int y=QRCODE_SIZE_HEIGHT - (20 * step) - 20;
//        if(QRCODE_SIZE<=300){
//            y=QRCODE_SIZE_HEIGHT - (20 * step) - 20;
//        }
//        graph.drawImage(src, 0,y , width, height, null);
//        graph.dispose();
//    }
//
//    @SuppressWarnings("restriction")
//    private static BufferedImage strToImage(String str, int width, int height,int fontSize) {
//        BufferedImage textImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
//        Graphics2D g2 = (Graphics2D)textImage.getGraphics();
//        //开启文字抗锯齿
//        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        g2.setBackground(Color.WHITE);
//        g2.clearRect(0, 0, width, height);
//        g2.setPaint(Color.BLACK);
//        FontRenderContext context = g2.getFontRenderContext();
//        Font font = new Font(null, Font.PLAIN, fontSize);
//        try {
//            font = SystemLoadFont.styleFont(simsunPath,Font.PLAIN,fontSize);
//        }catch (Exception e){
//            log.info(e.getMessage());
//        }
//        if(font==null){
//            font = new Font(null, Font.PLAIN, fontSize);
//        }
//        g2.setFont(font);
//        LineMetrics lineMetrics = font.getLineMetrics(str, context);
//        FontMetrics fontMetrics = FontDesignMetrics.getMetrics(font);
//        float offset = (width - fontMetrics.stringWidth(str)) / 2;
//        float y = (height + lineMetrics.getAscent() - lineMetrics.getDescent() - lineMetrics.getLeading()) / 2;
//
//        g2.drawString(str, (int)offset, (int)y);
//
//        return textImage;
//    }

    public static String convert(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        String png_base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
        return "data:image/png;base64,"+png_base64;
    }

    public static String convertByte(byte[] bytes) throws IOException {
        String png_base64 = Base64.getEncoder().encodeToString(bytes);
        return "data:image/png;base64,"+png_base64;
    }

    public static String splitStrLength(String item,int spLength){
        String regex = "(.{"+spLength+"})";//其中的12表示按照每12位进行分割
        item = item.replaceAll(regex, "$1,");
        return item;
    }
//
//    public static void main(String[] strings){
//        try {
//            String content="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQGe8TwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyUGtWMElvWFRiaF8xMDAwME0wN0YAAgSx-r5lAwQAAAAA";
//            String logurl="https://ndwl.oss-cn-shanghai.aliyuncs.com/2024/01/30/dd084c44cbaa45fba95593b111da26df";
////            BufferedImage image=encodeLogo(content,logurl,false,900);
//            String topText="",upText="=";
//            upText=splitStrLength(upText,14);//300:14  900:18  1500:20
//            BufferedImage image=createImageAndLogoText(content,logurl,topText,upText,true,300);
//            String baseData=convert(image);
//            System.out.println("------>"+baseData);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }


}
