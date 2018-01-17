package com.parkbobo.weixin.controller;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
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
import com.parkbobo.utils.weixin.WeixinUtils;
import com.parkbobo.utils.weixin.WxSign;

@Controller
public class WeixinGoPaController implements Serializable{

	/**
	 * 
	 */
	@Resource
	private WeixinConfigService weixinConfigService;
	@Resource
	private CarparkService carparkService;
	private static final long serialVersionUID = -4587798744919627352L;
	private static final String requestUrl1 = Configuration.getInstance().getValue("requestUrl");
	private static final String appid =Configuration.getInstance().getValue("hik_appid");
	
	@RequestMapping("goParking")
	public ModelAndView to(String sin,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		ModelAndView mv = new ModelAndView();
		try {
			WeixinConfig config = weixinConfigService.nameToConfig("hik");
			if(config!=null){
				String jsapi_ticket = config.getJsapiTicket();
				System.out.println(jsapi_ticket);
		        StringBuffer url = request.getRequestURL();
				System.out.println(url.toString());
				  Map<String, String> ret = WxSign.sign(jsapi_ticket, url.toString());
				  mv.addObject("timestamp", ret.get("timestamp"));
				  mv.addObject("nonceStr", ret.get("nonceStr"));
				  mv.addObject("jsapi_ticket", ret.get("jsapi_ticket"));
				  mv.addObject("signature", ret.get("signature"));
				  mv.addObject("appid", appid);
				  for (Map.Entry entry : ret.entrySet()) {
			             System.out.println(entry.getKey() + ", " + entry.getValue());
			      }
				  List<Carpark> carparks = this.carparkService.getAll();
				  mv.addObject("hikParking", carparks);
				  mv.setViewName("weixin/hik/test");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 获取访问路径
	 * @return
	 */
//	private String getUrl(){
//        HttpServletRequest request = ServletActionContext.getRequest();
//         
//        StringBuffer requestUrl = request.getRequestURL();
//        String queryString = request.getQueryString();
//        String url = requestUrl +"?"+queryString;
//        return url;
//    }
}
