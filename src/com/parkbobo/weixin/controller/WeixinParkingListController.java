package com.parkbobo.weixin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.parkbobo.model.Carpark;
import com.parkbobo.model.WeixinConfig;
import com.parkbobo.service.CarparkService;
import com.parkbobo.service.WeixinConfigService;
import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.HttpRequest;
import com.parkbobo.utils.weixin.WxSign;

@Controller
public class WeixinParkingListController implements Serializable{

	/**
	 * 
	 */
	@Resource
	private CarparkService carparkService;
	@Resource
	private WeixinConfigService weixinConfigService;
	private static final long serialVersionUID = -4587798744919627352L;
	private static final String appid =Configuration.getInstance().getValue("hik_appid");
	private static final String MAPURL = Configuration.getInstance().getValue("mapUrl");
	
	@RequestMapping("wxParkingList_list")
	public ModelAndView weixinPort(HttpServletResponse response,HttpServletRequest request) throws IOException, NoSuchAlgorithmException{
		ModelAndView mv = new ModelAndView();
		try {
			WeixinConfig config = weixinConfigService.nameToConfig("hik");
			if(config!=null){
				String jsapi_ticket = config.getJsapiTicket();
		        StringBuffer url = request.getRequestURL();
			    Map<String, String> ret = WxSign.sign(jsapi_ticket, url.toString());
			    mv.addObject("timestamp", ret.get("timestamp"));
			    mv.addObject("nonceStr", ret.get("nonceStr"));
			    mv.addObject("jsapi_ticket", ret.get("jsapi_ticket"));
			    mv.addObject("signature", ret.get("signature"));
			    mv.addObject("appid", appid);
			    List<Carpark> carparks = carparkService.getAll();
			    mv.addObject("hikParking", carparks);
			    mv.addObject("map_url", MAPURL);
			    mv.setViewName("weixin/tsp/parking_list");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
}
