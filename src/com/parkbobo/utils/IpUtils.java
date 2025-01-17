package com.parkbobo.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

public class IpUtils {
	private static IpUtils ipUtils;
	private IpUtils(){}
	/**
	 * 
	 * 获得一个单一实例
	 * @return MD5   
	 */
	public synchronized static IpUtils getInstance(){
		if(ipUtils==null){
			ipUtils = new IpUtils();
		}
		return ipUtils;
	}
	public String getIp(HttpServletRequest request)
	{
		//获取客户端真实IP
		
		try {
			String ipAddress = request.getHeader("x-forwarded-for");
			if (ipAddress == null || ipAddress.length() == 0
					|| "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0
					|| "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ipAddress == null || ipAddress.length() == 0
					|| "unknown".equalsIgnoreCase(ipAddress)) {
				ipAddress = request.getRemoteAddr();
				if (ipAddress.equals("127.0.0.1")
						|| ipAddress.equals("0:0:0:0:0:0:0:1")) {
					//根据网卡取本机配置的IP  
					InetAddress inet = null;
					try {
						inet = InetAddress.getLocalHost();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					ipAddress = inet.getHostAddress();
				}
			}
			//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
			if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15  
				if (ipAddress.indexOf(",") > 0) {
					ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
				}
			}
			return ipAddress;
		} catch (Exception e) {
			return "127.0.0.1";
		}
	}
}
