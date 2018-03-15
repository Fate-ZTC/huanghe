package com.system.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

public class ImageUtil {
	private static String DEFAULT_PREVFIX = "thumb_";
    private static Boolean DEFAULT_FORCE = false;
    /**
     * 
     * @param imgFile 源图
     * @param w 缩略图宽度
     * @param h 缩略图高度
     * @param prevfix 前缀
     * @param force 是否强制按照高宽生成缩略图（如果为false，则生成最佳比例缩略图）
     */
	public static void thumbnailImage(File imgFile, int w, int h, String prevfix, boolean force){
		if(imgFile.exists()){
            try {
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames());
                String suffix = null;
                // 获取图片后缀
                if(imgFile.getName().indexOf(".") > -1) {
                    suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀全部小写，然后判断后缀是否合法
                if(suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0){
                    return ;
                }
                Image img = ImageIO.read(imgFile);
                String p = imgFile.getPath();
                if(!force){
                    // 根据原图与要求的缩略图比例，找到最合适的缩略图比例
                    int width = img.getWidth(null);
                    int height = img.getHeight(null);
                    if(width == w && height == h){
                    	FileUtils.copyFile(imgFile, new File(p.substring(0,p.lastIndexOf(File.separator)) + File.separator + prevfix +imgFile.getName()));
                    	return ;
                    }
                    if((width*1.0)/w < (height*1.0)/h){
                        if(width > w){
                            h = Integer.parseInt(new java.text.DecimalFormat("0").format(height * w/(width*1.0)));
                        }
                    } else {
                        if(height > h){
                            w = Integer.parseInt(new java.text.DecimalFormat("0").format(width * h/(height*1.0)));
                        }
                    }
                }
                BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics2d = bufferedImage.createGraphics();
                if(suffix.equalsIgnoreCase("png")||suffix.equalsIgnoreCase("gif")){
                	bufferedImage = graphics2d.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
                	graphics2d.dispose();
                	graphics2d = bufferedImage.createGraphics();
                }
                graphics2d.drawImage(img, 0, 0, w, h, null);
                graphics2d.dispose();
                
                
                // 将图片保存在原目录并加上前缀
                ImageIO.write(bufferedImage, suffix, new File(p.substring(0,p.lastIndexOf(File.separator)) + File.separator + prevfix +imgFile.getName()));
            } catch (IOException e) {
            }
        }else{
        	
        }
    }
	public static void thumbnailImage(String imagePath, int w, int h, String prevfix, boolean force){
        File imgFile = new File(imagePath);
        thumbnailImage(imgFile, w, h, prevfix, force);
    }
    
    public static void thumbnailImage(String imagePath, int w, int h, boolean force){
        thumbnailImage(imagePath, w, h, DEFAULT_PREVFIX, force);
    }
    
    public static void thumbnailImage(String imagePath, int w, int h){
        thumbnailImage(imagePath, w, h, DEFAULT_FORCE);
    }
}
