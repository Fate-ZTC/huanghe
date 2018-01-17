package com.parkbobo.weixin.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.parkbobo.model.Users;
import com.parkbobo.model.UsersCars;
import com.parkbobo.service.UsersCarsService;
import com.parkbobo.service.UsersService;
import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.weixin.WeixinUtils;

@Controller
public class UserAuthController implements Serializable{
	@Resource(name="usersService")
	private UsersService usersService;
	@Resource(name="usersCarsService")
	private UsersCarsService usersCarsService;

	/**
	 * 获取包期规则 
	 * 
	 */
	private static final long serialVersionUID = -2902447954794622012L;
	/**
	 * 车辆认证列表
	 * */
	@RequestMapping("authList")
	public ModelAndView authList(String code,HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		String domain = Configuration.getInstance().getValue("domainName");
		String path = request.getContextPath();
		String appID = Configuration.getInstance().getValue("hik_appid");
		String appsecret = Configuration.getInstance().getValue("hik_appsecret");
		try {
			if(code==null){
				String URL = domain+path+"/authList";
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
					String URL = domain+path+"/authList";
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
						List<UsersCars> carList = usersCarsService.loadByMobile(user.getMobile());						
						mv.addObject("carList", carList);
						mv.addObject("carNum", (carList==null?0:carList.size()));
						mv.addObject("user", user);
						mv.setViewName("weixin/tsp/auth_list");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errMsg", "系统错误");
			mv.setViewName("weixin/tsp/auth_list");
		}
		return mv;
	}
	/**
	 * 跳转添加驾驶证页面
	 * */
	@RequestMapping("addDriver")
	public ModelAndView addDriver(String mobile){
		ModelAndView mv = new ModelAndView();
		mv.addObject("mobile", mobile);
		mv.setViewName("weixin/tsp/add_driver");
		return mv;
	}
	
	
	/**
	 * 上传驾驶证
	 * */
	@RequestMapping("uploadDriver")
	public ModelAndView uploadDriver(@RequestParam("fileDriver") MultipartFile fileDriver,String mobile,HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		  String pathval = request.getSession().getServletContext().getRealPath("/");
	      // 根据配置文件获取服务器图片存放路径
	      String newFileName = String.valueOf( System.currentTimeMillis());
	      String saveFilePath = "uploadFile\\";
	      /* 构建文件目录 */
	      File fileDir = new File(pathval + saveFilePath);
	      if (!fileDir.exists()) {
	          fileDir.mkdirs();
	      }
	      //上传的文件名
	      String filename=fileDriver.getOriginalFilename();
	      //文件的扩张名
	      String extensionName = filename.substring(filename.lastIndexOf(".") + 1);
	      try {
	    	  
	          String imgPath = saveFilePath + newFileName + "." +extensionName;
	          System.out.println(pathval + imgPath);//打印图片位置
	          FileOutputStream out = new FileOutputStream(pathval + imgPath);
	          out.write(fileDriver.getBytes());
	          out.flush();
	          out.close();
	          Users users = usersService.get(mobile);
	          users.setAuthStatus(0);
	          users.setAuthReason(null);
	          users.setDriverUrl(imgPath);
	          usersService.update(users);
	          mv.setViewName("redirect:/authList");
	      } catch (Exception e) {
	          e.printStackTrace();
	          mv.addObject("errorMsg","上传失败,请联系管理员");
	          mv.setViewName("weixin/tsp/add_driver");
	      }
		return mv;
	}
	
	/**
	 * 跳转添加驾驶证页面
	 * */
	@RequestMapping("addDriving")
	public ModelAndView addDriving(Integer kid,String mobile){
		ModelAndView mv = new ModelAndView();
		if(kid!=null){
			UsersCars cars = this.usersCarsService.get(kid);
			mv.addObject("cars", cars);	
		}
		mv.addObject("kid",kid);
		mv.addObject("mobile", mobile);
		mv.setViewName("weixin/tsp/add_driving");
		return mv;
	}
	
	

	/**
	 * 上传行驶证
	 * */
	@RequestMapping("uploadDriving")
	public ModelAndView uploadDriving(@RequestParam("fileDriving") MultipartFile fileDriving,String mobile,Integer kid,String carPlate,Integer vehicleType,Integer plateColor,String vehicleHost,String contactNum,String userHost,HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		  String pathval = request.getSession().getServletContext().getRealPath("/");
	      // 根据配置文件获取服务器图片存放路径
	      String newFileName = String.valueOf( System.currentTimeMillis());
	      String saveFilePath = "uploadFile\\";
	      /* 构建文件目录 */
	      File fileDir = new File(pathval + saveFilePath);
	      if (!fileDir.exists()) {
	          fileDir.mkdirs();
	      }
	      //上传的文件名
	      String filename=fileDriving.getOriginalFilename();
	      //文件的扩张名
	      String extensionName = filename.substring(filename.lastIndexOf(".") + 1);
	      try {
	    	  
	          String imgPath = saveFilePath + newFileName + "." +extensionName;
	          System.out.println(pathval + imgPath);//打印图片位置
	          FileOutputStream out = new FileOutputStream(pathval + imgPath);
	          out.write(fileDriving.getBytes());
	          out.flush();
	          out.close();
	          if(kid!=null){
	        	  UsersCars usersCars = usersCarsService.get(kid);
	        	  usersCars.setAuthStatus(0);
	        	  usersCars.setAuthReason(null);
	        	  usersCars.setPlateColor(plateColor);
	        	  usersCars.setCarPlate(carPlate);
	        	  usersCars.setContactNum(contactNum);
	        	  usersCars.setDrivingUrl(imgPath);
	        	  usersCars.setUserHost(userHost);
	        	  usersCars.setVehicleHost(vehicleHost);
	        	  usersCars.setVehicleType(vehicleType);
	        	  usersCarsService.update(usersCars);
	          }else{
	        	  UsersCars usersCars = new UsersCars();
	        	  usersCars.setAuthStatus(0);
	        	  usersCars.setAuthReason(null);
	        	  usersCars.setCarPlate(carPlate);
	        	  usersCars.setContactNum(contactNum);
	        	  usersCars.setDrivingUrl(imgPath);
	        	  usersCars.setMobile(mobile);
	        	  usersCars.setPlateColor(plateColor);
	        	  usersCars.setPosttime(new Date());
	        	  usersCars.setUserHost(userHost);
	        	  usersCars.setVehicleHost(vehicleHost);
	        	  usersCars.setVehicleType(vehicleType);
	        	  usersCarsService.add(usersCars);
	          }
	          mv.setViewName("redirect:/authList");
	      } catch (Exception e) {
	          e.printStackTrace();
	          mv.addObject("errorMsg","上传失败,请联系管理员");
	          mv.setViewName("weixin/tsp/add_driving");
	      }
		return mv;
	}
}
