package com.parkbobo.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public abstract class GZipUtils { 

    public static final int BUFFER = 1024; 
    public static final String EXT = ".gz"; 

    /**
     * 数据压缩
     * 
     * @param data
     * @return
     * @throws Exception
     */ 
    public static byte[] compress(byte[] data) throws Exception { 
        ByteArrayInputStream bais = new ByteArrayInputStream(data); 
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 

        // 压缩 
        compress(bais, baos); 

        byte[] output = baos.toByteArray(); 

        baos.flush(); 
        baos.close(); 

        bais.close(); 

        return output; 
    } 

    /**
     * 文件压缩
     * 
     * @param file
     * @throws Exception
     */ 
    public static void compress(File file) throws Exception { 
        compress(file, true); 
    } 

    /**
     * 文件压缩
     * 
     * @param file
     * @param delete
     *            是否删除原始文件
     * @throws Exception
     */ 
    public static void compress(File file, boolean delete) throws Exception { 
        FileInputStream fis = new FileInputStream(file); 
        FileOutputStream fos = new FileOutputStream(file.getPath() + EXT); 

        compress(fis, fos); 

        fis.close(); 
        fos.flush(); 
        fos.close(); 

        if (delete) { 
            file.delete(); 
        } 
    } 

    /**
     * 数据压缩
     * 
     * @param is
     * @param os
     * @throws Exception
     */ 
    public static void compress(InputStream is, OutputStream os) 
            throws Exception { 

        GZIPOutputStream gos = new GZIPOutputStream(os); 

        int count; 
        byte data[] = new byte[BUFFER]; 
        while ((count = is.read(data, 0, BUFFER)) != -1) { 
            gos.write(data, 0, count); 
        } 

        gos.finish(); 

        gos.flush(); 
        gos.close(); 
    } 

    /**
     * 文件压缩
     * 
     * @param path
     * @throws Exception
     */ 
    public static void compress(String path) throws Exception { 
        compress(path, true); 
    } 

    /**
     * 文件压缩
     * 
     * @param path
     * @param delete
     *            是否删除原始文件
     * @throws Exception
     */ 
    public static void compress(String path, boolean delete) throws Exception { 
        File file = new File(path); 
        compress(file, delete); 
    } 

//    /**
//     * 数据解压缩
//     * 
//     * @param data
//     * @return
//     * @throws Exception
//     */ 
//    public static byte[] decompress(byte[] data) throws Exception { 
//        ByteArrayInputStream bais = new ByteArrayInputStream(data); 
//        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
//
//        // 解压缩 
//
//        decompress(bais, baos); 
//
//        data = baos.toByteArray(); 
//
//        baos.flush(); 
//        baos.close(); 
//
//        bais.close(); 
//
//        return data; 
//    } 

//    /**
//     * 文件解压缩
//     * 
//     * @param file
//     * @throws Exception
//     */ 
//    public static void decompress(File file) throws Exception { 
//        decompress(file, true); 
//    } 

//    /**
//     * 文件解压缩
//     * 
//     * @param file
//     * @param delete
//     *            是否删除原始文件
//     * @throws Exception
//     */ 
//    public static void decompress(File file, boolean delete) throws Exception { 
//        FileInputStream fis = new FileInputStream(file); 
//        FileOutputStream fos = new FileOutputStream(file.getPath().replace(EXT, 
//                "")); 
//        decompress(fis, fos); 
//        fis.close(); 
//        fos.flush(); 
//        fos.close(); 
//
//        if (delete) { 
//            file.delete(); 
//        } 
//    } 

    /**
     * 数据解压缩
     * 
     * @param is
     * @param os
     * @return 
     * @throws Exception
     */ 
    public static String decompress(InputStream is) 
            throws Exception { 

        GZIPInputStream gis = new GZIPInputStream(is); 
        StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(gis));
		String line = null;
		while ((line = br.readLine())!=null) {
			sb.append(line);
		}
		gis.close(); 
		return new String(sb.toString().getBytes("gbk"),"utf-8");

    }
    public static String decompress2(InputStream is) throws Exception { 
		GZIPInputStream gis = new GZIPInputStream(is); 
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(gis));
		String line = null;
		while ((line = br.readLine())!=null) {
			sb.append(line);
		}
	gis.close(); 
	return new String(sb.toString().getBytes(),"utf-8");

} 

//    /**
//     * 文件解压缩
//     * 
//     * @param path
//     * @throws Exception
//     */ 
//    public static void decompress(String path) throws Exception { 
//        decompress(path, true); 
//    } 

//    /**
//     * 文件解压缩
//     * 
//     * @param path
//     * @param delete
//     *            是否删除原始文件
//     * @throws Exception
//     */ 
//    public static void decompress(String path, boolean delete) throws Exception { 
//        File file = new File(path); 
//        decompress(file, delete); 
//    } 
}
