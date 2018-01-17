package com.parkbobo.weixin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.model.MobileCheckcode;
import com.parkbobo.model.Users;
import com.parkbobo.model.UsersCars;
import com.parkbobo.service.MobileCheckcodeService;
import com.parkbobo.service.UsersService;
import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.weixin.WeixinUtils;

/**
 * 用户绑定处理
 * @author RY
 * @version 1.0
 * @since 2017-6-26 09:47:43
 *
 */

@Controller
public class WeixinUsersManageController implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7014815079759005776L;
	@Resource(name="mobileCheckcodeService")
	private MobileCheckcodeService mobileCheckcodeService;
	@Resource(name="usersService")
	private UsersService usersService;
	private String code;//微信CODE
	private String state;//微信重定向参数
	
	/**
	 * 进入手机号码绑定页面
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("wxUsersManage_toBind")
	public ModelAndView to(String code, String stat, String link, HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		ModelAndView mv = new ModelAndView();
		String domain = Configuration.getInstance().getValue("domainName");
		String path = request.getContextPath();
		String appID = Configuration.getInstance().getValue("hik_appid");
		if(code==null){
			String URL = domain+path+"/wxUsersManage_toBind";
//			String URL = "http://192.168.4.234:8080/hik/wxUsersManage_toBind";
			String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + 
			"appid="+appID+"&" + 
			"redirect_uri=" + URLEncoder.encode(URL, "UTF-8") + 
			"&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
			response.setStatus(301);
			response.setHeader("Location",url);
			response.setHeader("Connection","close");
			return null;
		}
		mv.setViewName("weixin/tsp/bind_mobile");
		mv.addObject("code", code);
		mv.addObject("link", link);
		return mv;
	}
	
//	@RequestMapping("wxUsersManage_bind")
//	public ModelAndView bind(String phone, String valicode, HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
//		ModelAndView mv = new ModelAndView();
//		String domain = Configuration.getInstance().getValue("domainName");
//		String path = request.getContextPath();
//		String appID = Configuration.getInstance().getValue("hik_appid");
//		if(code==null){
//			String URL = domain+path+"/wxUsersManage_toBind";
//			String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + 
//			"appid="+appID+"&" + 
//			"redirect_uri=" + URLEncoder.encode(URL, "UTF-8") + 
//			"&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
//			response.setStatus(301);
//			response.setHeader("Location",url);
//			response.setHeader("Connection","close");
//			return null;
//		}
//		mv.setViewName("weixin/tsp/binding");
//		return mv;
//	}
	
	/**
	 * 注册发送短信验证码
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("wxUsersManage_sendSms")
	public String sendSms(String phone, HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			int randomCode = (int)((Math.random() * 9 + 1) * 1000);
			System.out.println(randomCode);
			if(mobileCheckcodeService.save(phone, randomCode, (short) 0).equals("2"))
			{
				out.print("{\"status\":\"true\"}");
			}
			else if((mobileCheckcodeService.save(phone, randomCode, (short) 0).equals("4085")))
			{
				out.print("{\"status\":\"false\",\"errorcode\":\"27\"}");
			}
			else
			{
				out.print("{\"status\":\"false\",\"errorcode\":\"03\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorcode\":\"11\"}");
		}
		
		out.flush();
		out.close();
		return null;
	}
	
	/**
	 * 绑定手机号码
	 * @param phone
	 * @param code
	 * @param validate
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("wxUsersManage_band")
	public String band(String phone,String code, String validate, HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		String appID = Configuration.getInstance().getValue("hik_appid");
		String appsecret = Configuration.getInstance().getValue("hik_appsecret");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			MobileCheckcode checkCode = mobileCheckcodeService.getByTelephone(phone, (short) 0);
			if(checkCode == null){
				out.print("{\"status\":\"false\",\"errorcode\":\"00\",\"errMsg\":\"请重新获取验证码\"}");
			}
			else if(!validate.equals(checkCode.getCheckcode())){
				out.print("{\"status\":\"false\",\"errorcode\":\"00\",\"errMsg\":\"验证码输入错误\"}");
			}
			else{
				//获取OPENID
				JSONObject jsonMap = WeixinUtils.getOpenId(appID, appsecret, code);
				System.out.println(jsonMap.toString());
				String openid = jsonMap.getString("openid");//openid
				System.out.println(openid);
				//获取access_token
//				WeixinConfig token = configService.getNameToToken("hik");//access_token
//				//获取用户信息
//				jsonMap = WeixinUtils.getUserInfo(token.getAccess_token(), openid);
//				
//				//更新存储用户信息
//				String nickname = jsonMap.getString("nickname");//昵称
//				Integer sex = jsonMap.getInt("sex");//性别
//				String city = jsonMap.getString("city");//城市
//				String province = jsonMap.getString("province");//省份
//				String country = jsonMap.getString("country");//国家
//				String head = jsonMap.getString("headimgurl");//头像
				
				Users user = new Users();
				user.setMobile(phone);
				user.setOpenid(openid);
				user.setSex(3);
//				user.setNickname(nickname);
//				user.setSex(sex);
//				user.setCity(city);
//				user.setProvince(province);
//				user.setCountry(country);
//				user.setHead(head);
				user.setPosttime(new Date());
				usersService.save(user);
				
				out.print("{\"status\":\"true\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorcode\":\"00\",\"errMsg\":\"绑定失败，请重试\"}");
		}
		
		out.flush();
		out.close();
		return null;
	}
	
	/**
	 * 判断用户驾驶证是否审核通过
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("wxUsersManage_checkDriver")
	public void checkDriver(HttpServletRequest request,HttpServletResponse response,String mobile) throws IOException{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Users users = usersService.get(mobile);
			if(users!=null){
				if(users.getAuthStatus()!=null && users.getAuthStatus().equals(1)){
					out.print("{\"status\":\"true\",\"errorCode\":\"0\",\"errorMsg\":\"审核通过\"}");
				}else{
					out.print("{\"status\":\"false\",\"errorCode\":\"-1\",\"errorMsg\":\"未通过审核\"}");
				}
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":\"-1\",\"errorMsg\":\"未通过审核\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorCode\":\"-1\",\"errorMsg\":\"未通过审核\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 进入我的地盘
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("wxUsersManage_myZone")
	public ModelAndView myZone(String code,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		
		ModelAndView mv = new ModelAndView();
		String domain = Configuration.getInstance().getValue("domainName");
		String path = request.getContextPath();
		String appID = Configuration.getInstance().getValue("hik_appid");
		String appsecret = Configuration.getInstance().getValue("hik_appsecret");
		try {
			if(code==null){
				String URL = domain+path+"/wxUsersManage_myZone";
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + 
				"appid="+appID+"&" + 
				"redirect_uri=" + URLEncoder.encode(URL, "UTF-8") + 
				"&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
				response.setStatus(301);
				response.setHeader("Location",url);
				response.setHeader("Connection","close");
				return null;
			}else{
				net.sf.json.JSONObject jsonMap = WeixinUtils.getOpenId(appID, appsecret, code);
				if(!jsonMap.containsKey("openid")){
					String URL = domain+path+"/wxUsersManage_myZone";
					String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + 
					"appid="+appID+"&" + 
					"redirect_uri=" + URLEncoder.encode(URL, "UTF-8") + 
					"&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
					response.setStatus(301);
					response.setHeader("Location",url);
					response.setHeader("Connection","close");
					return null;
				}else{
					String openid = jsonMap.getString("openid");
					Users user = usersService.loadByOpenid(openid);
					if(user == null){
						mv.setViewName("redirect:/wxUsersManage_toBind?link=/wxCarManage_toBind");
						mv.addObject("url", "/wxCarManage_toBind");
					}else{
						mv.addObject("user", user);
						mv.setViewName("weixin/tsp/myZone");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errMsg", "系统错误");
			mv.setViewName("weixin/tsp/myZone");
		}
		return mv;
		
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
