package com.soong.vcode.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 随机生成验证码工具
 */
public class VerifyCode {
    // 验证码图片的宽和高
    private int w = 70;
    private int h = 35;

    // 获取一个随机数
    private Random r = new Random();
    // {"宋体", "华文楷体", "黑体", "华文新魏", "华文隶书", "微软雅黑", "楷体_GB2312"}
    private String[] fontNames  = {"宋体", "华文楷体", "黑体", "微软雅黑", "楷体_GB2312"};
    // 验证码图片上会出现的文字
    private String codes  = "23456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
    // 图片的背景色为白色
    private Color bgColor  = new Color(255, 255, 255);
    // 图片上的文本
    private String text ;

    /**
     * 生成随机颜色
     */
    private Color randomColor () {
        // 将红、绿、蓝都控制在 150 以内，避免颜色过浅，区别背景色
        int red = r.nextInt(150);
        int green = r.nextInt(150);
        int blue = r.nextInt(150);
        return new Color(red, green, blue);
    }

    /**
     * 生成随机字体
     */
    private Font randomFont () {
        // 在可选字体数组中选择一个字体
        int index = r.nextInt(fontNames.length);
        String fontName = fontNames[index];

        int style = r.nextInt(4); // 4 种字体样式(0 无样式, 1 斜体，2 加粗， 3 斜体且加粗)中选一种
        int size = r.nextInt(5) + 24; // 字的大小
        return new Font(fontName, style, size);
    }

    /**
     * 画干扰线
     */
    private void drawLine (BufferedImage image) {
        // 干扰线数量
        int num  = 3;
        Graphics2D g2 = (Graphics2D)image.getGraphics();
        for(int i = 0; i < num; i++) {
            // 每条干扰线的两个坐标
            int x1 = r.nextInt(w);
            int y1 = r.nextInt(h);
            int x2 = r.nextInt(w);
            int y2 = r.nextInt(h);
            g2.setStroke(new BasicStroke(1.5F));
            // 干扰线颜色
            g2.setColor(Color.BLUE);
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 取得随机字符
     */
    private char randomChar () {
        int index = r.nextInt(codes.length());
        return codes.charAt(index);
    }

    /**
     * 创造图片缓冲区
     */
    private BufferedImage createImage () {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D)image.getGraphics();
        g2.setColor(this.bgColor);
        g2.fillRect(0, 0, w, h);
        return image;
    }


    /**
     * 生成验证码图片
     */
    public BufferedImage getImage () {
        BufferedImage image = createImage(); // 图片缓冲区
        Graphics2D g2 = (Graphics2D)image.getGraphics(); // 图片绘制环境
        StringBuilder sb = new StringBuilder(); // 字符串容器
        // 向图片中画4个字符
        for(int i = 0; i < 4; i++)  {
            String s = randomChar() + ""; // 将 char 转成 String
            sb.append(s);
            float x = i * 1.0F * w / 4; // 确定字符的 x 坐标
            g2.setFont(randomFont()); // 确定字体
            g2.setColor(randomColor()); // 确定颜色
            g2.drawString(s, x, h-5); // 确定 y 坐标后绘制
        }
        this.text = sb.toString();
        drawLine(image); // 画干扰线
        return image;
    }

    /**
     * @return 图片上的文字
     */
    public String getText () {
        return text;
    }

    /**
     * 保存图片
     * @param image 画好图的图片缓冲区
     * @param out 用于保存图片的输出流
     */
    public static void output (BufferedImage image, OutputStream out)
            throws IOException {
        ImageIO.write(image, "JPEG", out);
    }
}



