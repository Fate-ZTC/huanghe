package com.parkbobo.quartz.task;

import java.text.ParseException;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import com.parkbobo.model.WeixinConfig;
import com.parkbobo.service.WeixinConfigService;
import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.HttpRequest;
import com.parkbobo.utils.weixin.WeixinUtils;

public class GetTokenTicketTask {
	@Resource
	private WeixinConfigService weixinConfigService;
	/**
	 * 启动任务
	 * @throws ParseException
	 */
	public void startJob() throws ParseException
	{
		getTokenTicket();
	}
	/**
	 * 1.5小时获取access_token，jsapi_ticket
	 */
	private void getTokenTicket(){
		JSONObject tokenObject = WeixinUtils.getToken();
		String token = tokenObject.getString("access_token");
		JSONObject ticketObject = WeixinUtils.getTicket(token);
		String jsapiTicket = ticketObject.getString("ticket");
		try {
			WeixinConfig config = weixinConfigService.nameToConfig("hik");
			if(config!=null){
				config.setAccessToken(token);
				config.setJsapiTicket(jsapiTicket);
				weixinConfigService.merge(config);
			}else{
				config = new WeixinConfig();
				config.setAccessToken(token);
				config.setJsapiTicket(jsapiTicket);
				config.setWxName("hik");
				weixinConfigService.add(config);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
	}
}
