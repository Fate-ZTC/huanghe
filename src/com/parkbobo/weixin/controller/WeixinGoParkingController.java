package com.parkbobo.weixin.controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.HttpRequest;

@Controller
public class WeixinGoParkingController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4587798744919627352L;
	
	@RequestMapping("wxGoParking_to")
	public ModelAndView to(String code,String state,String longitude,String latitude,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		ModelAndView mv = new ModelAndView();
//		String domain = Configuration.getInstance().getValue("domainName");
//		String path = request.getContextPath();
//		String appID = Configuration.getInstance().getValue("hik_appid");
//		String appsecret = Configuration.getInstance().getValue("hik_appsecret");
//		if(code==null){
//			String URL = domain+path+"/wxGoParking_to";
//			String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + 
//			"appid="+appID+"&" + 
//			"redirect_uri=" + URLEncoder.encode(URL, "UTF-8") + 
//			"&response_type=code&scope=snsapi_base&state="+longitude+","+latitude+"#wechat_redirect";
//			response.setStatus(301);
//			response.setHeader("Location",url);
//			response.setHeader("Connection","close");
//			return null;
//		}else{
//			String uri = "https://api.weixin.qq.com/sns/oauth2/access_token";
//			String ret = HttpRequest.sendGet(uri, "appid="+appID+"&secret="+appsecret+"&code="+code+"&grant_type=authorization_code");
//			if(!ret.contains("access_token"))
//			{
//				String URL = domain+path+"/wxGoParking_to";
//				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + 
//				"appid="+appID+"&" + 
//				"redirect_uri=" + URLEncoder.encode(URL, "UTF-8") + 
//				"&response_type=code&scope=snsapi_base&state="+longitude+","+latitude+"#wechat_redirect";
//				response.setStatus(301);
//				response.setHeader("Location",url);
//				response.setHeader("Connection","close");
//				return null;
//			}
//			String[] lonlat = state.split(",");
//			longitude = lonlat[0];
//			latitude = lonlat[1];
//			JSONObject jsonObject = JSONObject.fromObject(ret);
//			String openid = jsonObject.get("openid").toString();
//			//根据openid获取经纬度
//			System.out.println(openid);
//			try {
//				String requestUrl = Configuration.getInstance().getValue("requestUrl");
//				String sendGet = HttpRequest.sendGet(requestUrl+"/wxUsers_getOpenid2loc", "openid="+openid);
//				System.out.println(sendGet);
//				if(sendGet!=null){
//					JSONObject fromObject = JSONObject.fromObject(sendGet);
//					String status = fromObject.getString("status");
//					if(status.equals("true")){
//						String strLat = fromObject.getString("latitude");
//						mv.addObject("strLat", strLat);
//						String strLon = fromObject.getString("longitude");
//						mv.addObject("strLat", strLat);
//						mv.addObject("strLon", strLon);
//						mv.addObject("longitude", longitude);
//						mv.addObject("latitude", latitude);
//						mv.setViewName("weixin/hik/go_parking");
//					}else{
//						mv.setViewName("weixin/hik/error");
//					}
//				}else{
//					mv.setViewName("weixin/hik/error");
//				}
//			} catch (Exception e) {
//				mv.setViewName("weixin/hik/error");
//			}
//		}
		return mv;
	}
}
