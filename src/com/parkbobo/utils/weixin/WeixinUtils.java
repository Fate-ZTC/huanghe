package com.parkbobo.utils.weixin;

import java.io.UnsupportedEncodingException;

import net.sf.json.JSONObject;

import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.HttpRequest;
import com.parkbobo.utils.http.HttpUtils;

public class WeixinUtils {
//	public final static String TEMPLATE_ID_LEAVE = "Vv_kkBo-XaJ3lOz7EL87YrZRRJm7ePNxbaudR0TOG4k";//离开通知模板
//	public final static String TEMPLATE_ID_ENTER = "jIMquoAduj-UDkD16FKjxz5irsqLy6L6SI9bxJNLR6A";//入场通知模板
//	public final static String TEMPLATE_ID_OVERTIME = "DMXWCr3ap9ZTwOct6gRxiMn9SBKfLrliBs0oEX1PiD4";//超时离场模板
//	public final static String TEMPLATE_ID_PAYMENT = "W_d7sj2T6__t1wPXqNvyeZMb3X9tBoC54Xw-st5SYUk";//缴费成功通知
	/**
	 * 测试
	 * */
	public final static String TEMPLATE_ID_LEAVE = "BgGVd37PvwW6l9_oyhB-ySJdOd5ITfsAalZrGkEIFZA";//离开通知模板
	public final static String TEMPLATE_ID_ENTER = "m8qIf-HfByqt4D-LVHCvBOh2IiMf39U5C7RiYQHnABk";//入场通知模板
	public final static String TEMPLATE_ID_OVERTIME = "zePV9B3RIG2OberCEuLYRNlFIXE-A09pVUsrEegPTqc";//超时离场模板
	public final static String TEMPLATE_ID_PAYMENT = "V_DWSEw9tf90ctsaoliDd0QIaeLmSF38gIRDdlMobwM";//缴费成功通知
	public final static String TEMPLATE_ID_AUTH = "AlJWWKAIfD-ioFiNaKsQXjHGLpLQIGgi5bYSOav5kBo";//认证审核通知
	
	/**
	 * 获取openid
	 * */
	public static JSONObject getOpenId(String appid, String secret, String code) {
			String get_openid_url = "https://api.weixin.qq.com/sns/oauth2/access_token";
			String s = HttpRequest.sendGet(get_openid_url,"appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code");
			JSONObject jsonObject = JSONObject.fromObject(s);
			return jsonObject;
	}
	/**
	 * 获取jsapi_ticket
	 * 
	 * */
	public static JSONObject getTicket(String token){
		String get_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
		String sendGet = HttpRequest.sendGet(get_ticket_url, "access_token="+token+"&type=jsapi");
		JSONObject jsonObject = JSONObject.fromObject(sendGet);
		return jsonObject;
	}
	/**
	 * 获取access_token
	 * 
	 * */
	public static JSONObject getToken(){
		String get_token_url = "https://api.weixin.qq.com/cgi-bin/token";
		String appid = Configuration.getInstance().getValue("hik_appid");
		String secret = Configuration.getInstance().getValue("hik_appsecret");
		String sendGet = HttpRequest.sendGet(get_token_url, "grant_type=client_credential&appid="+appid+"&secret="+secret);
		JSONObject jsonObject = JSONObject.fromObject(sendGet);
		return jsonObject;
	}
	/**
	 * 获取用户信息
	 * https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
	 * */
	
	public static JSONObject getUserInfo(String token,String openid){
		String get_info_url = "https://api.weixin.qq.com/cgi-bin/user/info";
		String sendGet = HttpRequest.sendGet(get_info_url, "access_token="+token+"&openid="+openid+"&lang=zh_CN");
		JSONObject jsonObject = JSONObject.fromObject(sendGet);
		return jsonObject;
	}
	/**
	 * 发送模板消息-入场通知模板
	 * https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN
	 * */
	public static String sendEnterNotice(String openid,String token,String parkName,Long carparkid,String carNumber,String enterTime, String inunid){
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
		StringBuffer sb = new StringBuffer();
		String reurl = Configuration.getInstance().getValue("domainName")+"/wxCost_toCost?inunid="+inunid+"&openid="+openid;
//		String reurl = Configuration.getInstance().getValue("domainName")+"/wxCost_toTest?inunid="+inunid+"&openid="+openid;
		sb.append("{"+
           "\"touser\":\""+openid+"\","+
           "\"template_id\":\""+TEMPLATE_ID_ENTER+"\","+
           "\"url\":\""+reurl+"\","+            
           "\"data\":{"+
                   "\"first\": {"+
                       "\"value\":\"欢迎入场\","+
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"keyword1\":{"+
                       "\"value\":\""+parkName+"\","+
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"keyword2\": {"+
                       "\"value\":\""+carNumber+"\","+
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"keyword3\": {"+
                       "\"value\":\""+enterTime+"\","+
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"remark\":{"+
                       "\"value\":\"点击缴费离场\","+
                       "\"color\":\"#173177\""+
                   "}"+
           "}"+
       "}");
		try {
			String sendPost = HttpRequest.sendPost(url, sb.toString());
			System.out.println(sendPost);
			return sendPost;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 发送模板消息-离场模板
	 * https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN
	 * */
	public static String sendLeaveNotice(String openid,String token,String parkName,String parkingTime,String carNumber,String leaveTime,String payAmount){
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
		StringBuffer sb = new StringBuffer();
		sb.append("{"+
           "\"touser\":\""+openid+"\","+
           "\"template_id\":\""+TEMPLATE_ID_LEAVE+"\","+
           "\"url\":\"\","+ 
           "\"data\":{"+
                   "\"first\": {"+
                       "\"value\":\"您已离开车场\","+
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"keyword1\":{"+
                       "\"value\":\""+carNumber+"\","+//车牌号
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"keyword2\": {"+
                       "\"value\":\""+parkName+"\","+//停车场名称
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"keyword3\": {"+
                       "\"value\":\""+leaveTime+"\","+//离场时间
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"keyword4\": {"+
                       "\"value\":\""+parkingTime+"\","+//停车时长
                       "\"color\":\"#173177\""+
	               "},"+
	               "\"keyword5\": {"+
	                   "\"value\":\""+payAmount+"\","+//支付金额
	                   "\"color\":\"#173177\""+
	               "},"+
                   "\"remark\":{"+
                       "\"value\":\"祝您一路顺风！\","+
                       "\"color\":\"#173177\""+
                   "}"+
           "}"+
       "}");
		try {
			String sendPost = HttpRequest.sendPost(url, sb.toString());
			System.out.println(sendPost);
			return sendPost;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 发送模板消息-超时离场模板
	 * https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN
	 * */
	public static String sendOvertimeNotice(String openid,String token,String parkName,Long carparkid,String carNumber,String payTime,String enterTime, String inunid){
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
		StringBuffer sb = new StringBuffer();
		String reurl = Configuration.getInstance().getValue("domainName")+"/wxCost_toCost?inunid="+inunid+"&openid="+openid;
//		String reurl = Configuration.getInstance().getValue("domainName")+"/wxCost_toTest?inunid="+inunid+"&openid="+openid;
		sb.append("{"+
           "\"touser\":\""+openid+"\","+
           "\"template_id\":\""+TEMPLATE_ID_OVERTIME+"\","+
           "\"url\":\""+reurl+"\","+            
           "\"data\":{"+
                   "\"first\": {"+
                       "\"value\":\"您未在规定时间内离场\","+
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"keyword1\":{"+
                       "\"value\":\""+carNumber+"\","+//车牌号
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"keyword2\": {"+
                       "\"value\":\""+parkName+"\","+//停车场名称
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"keyword3\": {"+
                       "\"value\":\""+payTime+"\","+//缴费时间
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"remark\":{"+
                       "\"value\":\"未在规定 时间内离开 ，已产生新的停车费用，点击缴费\","+
                       "\"color\":\"#173177\""+
                   "}"+
           "}"+
       "}");
		try {
			String sendPost = HttpRequest.sendPost(url, sb.toString());
			System.out.println(sendPost);
			return sendPost;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 发送模板消息-缴费成功模板
	 * https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN
	 * */
	public static String sendPaymentNotice(String openid,String token,String carNumber,String payTime,String enterTime,String parkingTime,String payAmount){
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
		StringBuffer sb = new StringBuffer();
		sb.append("{"+
           "\"touser\":\""+openid+"\","+
           "\"template_id\":\""+TEMPLATE_ID_PAYMENT+"\","+
           "\"url\":\"\","+            
           "\"data\":{"+
                   "\"first\": {"+
                       "\"value\":\"您已成功缴纳停车费用\","+
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"keyword1\":{"+
                       "\"value\":\""+carNumber+"\","+//车牌号
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"keyword2\": {"+
                       "\"value\":\""+enterTime+"\","+//入场时间
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"keyword3\": {"+
                       "\"value\":\""+parkingTime+"\","+//停车时长
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"keyword4\": {"+
                       "\"value\":\""+payAmount+"\","+//缴费金额
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"keyword5\": {"+
                       "\"value\":\""+payTime+"\","+//缴费时间
                       "\"color\":\"#173177\""+
                   "},"+
                   "\"remark\":{"+
                       "\"value\":\"如未在规定时间内离开 ，将产生新的停车费用。\","+
                       "\"color\":\"#173177\""+
                   "}"+
           "}"+
       "}");
		try {
			String sendPost = HttpRequest.sendPost(url, sb.toString());
			System.out.println(sendPost);
			return sendPost;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 发送模板消息-认证审核通过通知模板
	 * https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN
	 * */
	public static String authSuccessNotice(String openid,String token,String authResult,String authTime){
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
		StringBuffer sb = new StringBuffer();
		sb.append("{"+
				"\"touser\":\""+openid+"\","+
				"\"template_id\":\""+TEMPLATE_ID_AUTH+"\","+
				"\"url\":\"\","+            
				"\"data\":{"+
				"\"first\": {"+
				"\"value\":\"信息认证审核结果通知。\","+
				"\"color\":\"#173177\""+
				"},"+
				"\"keyword1\":{"+
				"\"value\":\""+authResult+"\","+//审核结果
				"\"color\":\"#173177\""+
				"},"+
				"\"keyword2\": {"+
				"\"value\":\""+authTime+"\","+//审核时间
				"\"color\":\"#173177\""+
				"},"+
				"\"remark\":{"+
				"\"value\":\"您上传的证件信息认证已通过审核。\","+
				"\"color\":\"#173177\""+
				"}"+
				"}"+
		"}");
		try {
			String sendPost = HttpRequest.sendPost(url, sb.toString());
			System.out.println(sendPost);
			return sendPost;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 发送模板消息-认证审核未通过通知模板
	 * https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN
	 * */
	public static String authFailNotice(String openid,String token,String authResult,String authTime,String authReason){
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+token;
		StringBuffer sb = new StringBuffer();
		sb.append("{"+
				"\"touser\":\""+openid+"\","+
				"\"template_id\":\""+TEMPLATE_ID_AUTH+"\","+
				"\"url\":\"\","+            
				"\"data\":{"+
				"\"first\": {"+
				"\"value\":\"信息认证审核结果通知。\","+
				"\"color\":\"#173177\""+
				"},"+
				"\"keyword1\":{"+
				"\"value\":\""+authResult+"\","+//审核结果
				"\"color\":\"#173177\""+
				"},"+
				"\"keyword2\": {"+
				"\"value\":\""+authTime+"\","+//审核时间
				"\"color\":\"#173177\""+
				"},"+
				"\"remark\":{"+
				"\"value\":\""+authReason+"\","+//未通过原因
				"\"color\":\"#173177\""+
				"}"+
				"}"+
		"}");
		try {
			String sendPost = HttpRequest.sendPost(url, sb.toString());
			System.out.println(sendPost);
			return sendPost;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void main(String[] args) {
//		String send = WeixinUtils.sendEnterNotice("BbZe9POXN6kXKGNY5cvHD_eb_hT5JiRgRPuTl9JilGiGgH5PD8s6vkSG8uCNgOl8oklRIHVAp0WKni4Pw_f5UCojeUy_0v2tYrQzKPyoPgUXXQfAIARRK");
//		System.out.println(send);
		
//		String get_token_url = "https://api.weixin.qq.com/cgi-bin/token";
//		String appid = "wx6e7c385d5064820b";
//		String secret = "9413167454efeac4779914da6d755879";
//		String sendGet = HttpRequest.sendGet(get_token_url, "grant_type=client_credential&appid="+appid+"&secret="+secret);
//		JSONObject jsonObject = JSONObject.fromObject(sendGet);
//		String token = jsonObject.getString("access_token");
//		
////		System.out.println(token);
////		String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="+token;
////		sendGet = HttpRequest.sendGet(url, "");
////		System.out.println(sendGet);
//		
//		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+token;
//		StringBuffer sb = new StringBuffer();
//		sb.append("{"
//				  + "\"button\":["
//				  +  "{"
//				  +  	"\"type\":\"view\","
//				  +		"\"name\":\"停车缴费\","
//				  +		"\"url\":\"http://www.jiuchengpark.cn/wxCost_qrCode\""
//				  +	 "},"
//				  +  "{"
//				  +  	"\"type\":\"view\","
//				  +		"\"name\":\"车场列表\","
//				  +		"\"url\":\"http://www.jiuchengpark.cn/wxParkingList_list\""
//				  +	 "},"
//				  +  "{"
//				  +      "\"name\":\"车服务\","
//				  +      "\"sub_button\":["
//				  +      "{"
//				  +      	"\"type\":\"view\","
//				  +			"\"name\":\"停车查询\","
//				  +			"\"url\":\"http://www.jiuchengpark.cn/wxSearchCarNumber_toSearch\""
//				  +		"},"
//				  +      "{"
//				  +      	"\"type\":\"view\","
//				  +			"\"name\":\"我的地盘\","
//				  +			"\"url\":\"http://www.jiuchengpark.cn/wxUsersManage_myZone\""
//				  +		"}]"
//				  +	 "}]"
//				  + "}");
//		try {
//			String sendPost = HttpRequest.sendPost(url, sb.toString());
//			System.out.println(sendPost);
////			return sendPost;
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
////			return null;
//		}
		JSONObject ticket = WeixinUtils.getTicket("kn4BhKTAWFVr-WOOSD6w5SBl2MguGel9LTU8bacHLrhKpRNYUwwICinCB8Jt4_GkOwp5UNSc3yULYmmvi30-yw4GBMyuTZx8Cs0ongA3l9yrWsFlVEf1dabg0Rdf8GtvMLUdAIARQZ");
		System.out.println(ticket);
	}
	
}
