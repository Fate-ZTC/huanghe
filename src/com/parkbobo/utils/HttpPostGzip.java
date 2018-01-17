package com.parkbobo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

public class HttpPostGzip {
	public static String sendPost(String url,String json){
		
		String decompress = "";
		if(!StringUtils.isNotBlank(url)){
			System.out.println("url为空");
			return "";
		}
		try {
			URL url1 = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
			conn.setConnectTimeout(5000);
			// 设置允许输出
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			// 设置User-Agent: Fiddler
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0");
			// 设置contentType
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("contentType", "utf-8");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			OutputStream os = conn.getOutputStream();   
			os.write(json.getBytes("utf-8"));
			os.close();
			int code = conn.getResponseCode();
			if (code == 200) {
				InputStream is = conn.getInputStream();
				decompress = GZipUtils.decompress(is);
			}else{
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decompress;
	}
public static String sendPost2(String url,String json){
		
		String decompress = "";
		if(!StringUtils.isNotBlank(url)){
			System.out.println("url为空");
			return "";
		}
		try {
			URL url1 = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
			conn.setConnectTimeout(5000);
			// 设置允许输出
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			// 设置User-Agent: Fiddler
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:44.0) Gecko/20100101 Firefox/44.0");
			// 设置contentType
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("contentType", "utf-8");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			OutputStream os = conn.getOutputStream();   
			os.write(json.getBytes("utf-8"));
			os.close();
			int code = conn.getResponseCode();
			if (code == 200) {
				InputStream is = conn.getInputStream();
				decompress = GZipUtils.decompress2(is);
			}else{
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return decompress;
	}
}
