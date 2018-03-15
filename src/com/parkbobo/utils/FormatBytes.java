package com.parkbobo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;


/**
 * <p>格式化文件大小辅助类<p>
 * @author LH
 * 
 */
public class FormatBytes {
	private static FormatBytes  formatBytes = null;
	/**
	 * 
	 * 私有构造方法.   
	 *
	 */
	private FormatBytes(){}
	/**
	 * 获得一个单一实例
	 * @return SendMail   
	 */
	public synchronized static FormatBytes getDefaultInstance(){
		if(formatBytes==null){
			formatBytes = new FormatBytes();
		}
		return formatBytes;
	}
	/**
	 * 格式化文件大小
	 * @param bytes 文件字节数
	 * @return 格式化后文文件大小
	 */
	public String Format(long bytes){
		String temp = "";
		DecimalFormat df = new DecimalFormat("#.00");
		if(bytes >= 1073741824) {
			temp = df.format((bytes / 1073741824.0)) + "GB";
		} else if(bytes >= 1048576.0) {
			temp = df.format((bytes / 1048576.0)) + "MB";
		} else if(bytes >= 1024) {
			temp = df.format((bytes / 1024.0)) + "KB";
		} else {
			temp = bytes + "Bytes";
		}
		return temp;
	}
	public String Format(File file){
		String temp="";
		Long bytes = getFileSize(file);
		DecimalFormat df = new DecimalFormat("#.00");
		if(bytes >= 1073741824) {
			temp = df.format((bytes / 1073741824.0)) + "GB";
		} else if(bytes >= 1048576.0) {
			temp = df.format((bytes / 1048576.0)) + "MB";
		} else if(bytes >= 1024) {
			temp = df.format((bytes / 1024.0)) + "KB";
		} else {
			temp = bytes + "Bytes";
		}
		return temp;
	}
	/**
	 * 获取文件字节数(Bytes)
	 * @param file 文件
	 * @return 文件字节数
	 */
	public long getFileSize(File file){
		try {
			FileInputStream ins = new FileInputStream(file);
			return ins.available();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	public static void main(String[] args) {
		System.out.println(2022/1024.0);
		System.out.println(FormatBytes.getDefaultInstance().Format(2022L));
	}
}
