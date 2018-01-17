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

import com.parkbobo.model.Users;
import com.parkbobo.model.UsersCars;
import com.parkbobo.service.UsersCarsService;
import com.parkbobo.service.UsersService;
import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.weixin.WeixinUtils;

/**
 * 车牌号管理
 * @author RY
 * @version 1.0
 * @since 2017-6-26 09:20:57
 * 
 * @version 1.1
 * @author RY
 * @since 2017-7-16 15:35:45
 * 增加反向寻车
 *
 */

@Controller
public class WeixinCarManageController implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7295247448417070576L;
	@Resource(name="usersCarsService")
	private UsersCarsService usersCarsService;
	@Resource(name="usersService")
	private UsersService usersService;
	private static final String MAPURL = Configuration.getInstance().getValue("mapUrl");

	/**
	 * 进入绑定页面
	 * @param code
	 * @param state
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("wxCarManage_toBind")
	public ModelAndView toBind(String code,String state,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		ModelAndView mv = new ModelAndView();
		String domain = Configuration.getInstance().getValue("domainName");
		String path = request.getContextPath();
		String appID = Configuration.getInstance().getValue("hik_appid");
		String appsecret = Configuration.getInstance().getValue("hik_appsecret");
		if(code==null){
			String URL = domain+path+"/wxCarManage_toBind";
			String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + 
			"appid="+appID+"&" + 
			"redirect_uri=" + URLEncoder.encode(URL, "UTF-8") + 
			"&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
			response.setStatus(301);
			response.setHeader("Location",url);
			response.setHeader("Connection","close");
			return null;
		}else{
			
			JSONObject jsonObject = WeixinUtils.getOpenId(appID, appsecret, code);
			System.out.println(jsonObject.toString());
			if(!jsonObject.containsKey("openid")){
				String URL = domain+path+"/wxCarManage_toBind";
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + 
				"appid="+appID+"&" + 
				"redirect_uri=" + URLEncoder.encode(URL, "UTF-8") + 
				"&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
				response.setStatus(301);
				response.setHeader("Location",url);
				response.setHeader("Connection","close");
				return null;
			}
			else{
				String openid = jsonObject.get("openid").toString();
				System.out.println(openid);
				Users user = usersService.loadByOpenid(openid);
				if(user == null){
					mv.setViewName("redirect:/wxUsersManage_toBind?link=/wxCarManage_toBind");
					mv.addObject("url", "/wxCarManage_toBind");
				}
				else{
					List<UsersCars> carList = usersCarsService.loadByMobile(user.getMobile());
					mv.setViewName("weixin/tsp/bind_car");
					mv.addObject("carList", carList);
					mv.addObject("mobile", user.getMobile());
				}
			}
		}
//		Users user = usersService.get("18780119202");
		return mv;
	}
	
	/**
	 * 删除
	 * @param kid
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("wxCarManage_delete")
	public String delete(Integer kid, HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			
			usersCarsService.delete(kid);
			out.print("{\"status\":\"true\"}");
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorcode\":\"11\"}");
		}
		
		out.flush();
		out.close();
		return null;
	}
	
	/**
	 * 编辑
	 * @param kid
	 * @param carPlate
	 * @param mobile
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("wxCarManage_update")
	public String update(Integer kid, String carPlate, String mobile, HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			if(usersCarsService.existWithPlateMobile(carPlate, mobile)){
				out.print("{\"status\":\"false\",\"errorcode\":\"11\",\"errMsg\":\"您已经绑定过该车牌号码\"}");
			}
			else{
				UsersCars usrCar = usersCarsService.get(kid);
				usrCar.setCarPlate(carPlate);
				usersCarsService.update(usrCar);
				out.print("{\"status\":\"true\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorcode\":\"11\",\"errMsg\":\"绑定失败，请重试\"}");
		}
		
		out.flush();
		out.close();
		return null;
	}
	
	/**
	 * 添加
	 * @param mobile
	 * @param carPlate
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("wxCarManage_add")
	public String add(String mobile, String carPlate, HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			if(usersCarsService.existWithPlateMobile(carPlate, mobile)){
				out.print("{\"status\":\"false\",\"errorcode\":\"11\",\"errMsg\":\"您已经绑定过该车牌号码\"}");
			}
			else{
				
				UsersCars usrCar = new UsersCars();
				usrCar.setCarPlate(carPlate);
				usrCar.setMobile(mobile);
				usrCar.setPosttime(new Date());
				UsersCars newCar = usersCarsService.add(usrCar);
				out.print("{\"status\":\"true\",\"kid\":\"" + newCar.getKid() + "\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorcode\":\"11\",\"errMsg\":\"绑定失败，请重试\"}");
		}
		
		out.flush();
		out.close();
		return null;
	}
	
	/**
	 * 放心新车
	 * @param code
	 * @param state
	 * @param zoneid
	 * @param startParkLot
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("wxCarManage_findCar")
	public ModelAndView findCar(String code,String state, String zoneid, String startParkLot, String floorid, HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException{
		ModelAndView mv = new ModelAndView();
		String domain = Configuration.getInstance().getValue("domainName");
		String path = request.getContextPath();
		String appID = Configuration.getInstance().getValue("hik_appid");
		String appsecret = Configuration.getInstance().getValue("hik_appsecret");
		System.out.println(zoneid + "," + startParkLot + ",11111");
		if(code==null){
			System.out.println(zoneid + "," + startParkLot + ",22222");
			String URL = domain+path+"/wxCarManage_findCar";
			String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + 
			"appid="+appID+"&" + 
			"redirect_uri=" + URLEncoder.encode(URL, "UTF-8") + 
			"&response_type=code&scope=snsapi_base&state=" + zoneid + "," + startParkLot + "," + floorid + "#wechat_redirect";
			response.setStatus(301);
			response.setHeader("Location",url);
			response.setHeader("Connection","close");
			return null;
		}else{
			System.out.println(zoneid + "," + startParkLot + ",33333");
			JSONObject jsonObject = WeixinUtils.getOpenId(appID, appsecret, code);
			System.out.println(jsonObject.toString());
			if(!jsonObject.containsKey("openid")){
				String URL = domain+path+"/wxCarManage_findCar";
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + 
				"appid="+appID+"&" + 
				"redirect_uri=" + URLEncoder.encode(URL, "UTF-8") + 
				"&response_type=code&scope=snsapi_base&state=" + zoneid + "," + startParkLot + "," + floorid + "#wechat_redirect";
				response.setStatus(301);
				response.setHeader("Location",url);
				response.setHeader("Connection","close");
				return null;
			}
			System.out.println(state);
			String[] params = state.split(",");
			zoneid = params[0];
			startParkLot = params[1];
			floorid = params[2];
			String openid = jsonObject.get("openid").toString();
			System.out.println(openid);
			Users user = usersService.loadByOpenid(openid);
			if(user == null){
				mv.setViewName("redirect:/wxUsersManage_toBind?link=/wxCarManage_findCar");
				mv.addObject("url", "/wxCarManage_findCar");
			}
			else{
				List<UsersCars> carList = usersCarsService.loadByMobile(user.getMobile());
				String carPlates = "";
				for(UsersCars uc : carList){
					carPlates += "|" + uc.getCarPlate();
				}
				if(carPlates.length() > 0){
					carPlates = carPlates.substring(1);
				}
				String carUrl = MAPURL + "/map_routeToCar?zoneid=" + zoneid + "&startParkLot=" + startParkLot + "&plateNums=" + carPlates + "&floorid=" + floorid;
				mv.setViewName("weixin/tsp/find_car");
				mv.addObject("carUrl", carUrl);
			}
		}
//		Users user = usersService.get("18780119202");
		return mv;
	}

}
