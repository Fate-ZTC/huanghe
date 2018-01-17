package com.system.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class RequestUtils {
	private static RequestUtils requestUtils = new RequestUtils();
	private RequestUtils(){}
	public static RequestUtils getDefaultInstance(){
		if(requestUtils == null){
			requestUtils = new RequestUtils();
		}
		return requestUtils;
	}
	public String getRemoteAddr(HttpServletRequest req){
		
		try {
			String ip = req.getHeader("x-forwarded-for");
			if(StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)){
				ip = req.getHeader("Proxy-Client-IP"); 
			}
			if(StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)){
				ip = req.getHeader("WL-Proxy-Client-IP");
			}
			if(StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)){
				ip = req.getRemoteAddr();
			}
			final String[] arr = ip.split(",");
			for(final String str : arr){
				if(!"unknown".equalsIgnoreCase(str)){
					ip = str;
					break;
				}
			}
			return ip;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
