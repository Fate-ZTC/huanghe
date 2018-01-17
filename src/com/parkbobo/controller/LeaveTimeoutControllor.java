package com.parkbobo.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.parkbobo.model.Carpark;
import com.parkbobo.model.ParkPassVehicleMonth;
import com.parkbobo.model.Users;
import com.parkbobo.model.UsersCars;
import com.parkbobo.model.VehicleCostWechat;
import com.parkbobo.model.WeixinConfig;
import com.parkbobo.service.CarparkService;
import com.parkbobo.service.ParkPassVehicleMonthService;
import com.parkbobo.service.UsersCarsService;
import com.parkbobo.service.UsersService;
import com.parkbobo.service.VehicleCostWechatService;
import com.parkbobo.service.WeixinConfigService;
import com.parkbobo.utils.AESOperator;
import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.HttpRequest;
import com.parkbobo.utils.StringUtils;
import com.parkbobo.utils.httpmodel.LeaveTimeOut;
import com.parkbobo.utils.weixin.WeixinUtils;

/**
 * 向海康提供的超时离开推送接口
 * @author RY
 * @version 1.0
 * @since 2017-6-27 14:22:54
 *
 */

@Controller
public class LeaveTimeoutControllor {
	@Resource(name="usersCarsService")
	private UsersCarsService usersCarsService;
	@Resource(name="usersService")
	private UsersService usersService;
	@Resource(name="vehicleCostWechatService")
	private VehicleCostWechatService vehicleCostWechatService;
	@Resource(name="parkPassVehicleMonthService")
	ParkPassVehicleMonthService parkPassVehicleMonthService;
	@Resource
	private CarparkService carparkService;
	@Resource
	private WeixinConfigService weixinConfigService;
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@RequestMapping("LeaveTimeOut")
	public String passVehicle(HttpServletResponse response,HttpServletRequest request) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			ServletInputStream is = request.getInputStream();
			String param = StringUtils.getDefaultInstance().ins2str(is);
			if(!StringUtils.getDefaultInstance().isNoBlank(param)){
				String reStr = "{\"message\": \"请求参数不正确\",\"errorCode\": 300009,\"token\": \"\"}";
				String encrypt = AESOperator.getInstance().encrypt(reStr);
//				System.out.println(encrypt);
				out.print("{\"package\": \""+encrypt+"\"}");
			}else{
				JSONObject parseObject = JSONObject.parseObject(param);
				String enStr = parseObject.getString("package");
				String praJson = AESOperator.getInstance().decrypt(enStr);
//				System.out.println("超时信息："+praJson);
				LeaveTimeOut lto = JSONObject.parseObject(praJson, LeaveTimeOut.class);
				if(lto!=null){
					String parkId = lto.getParkId();
					Carpark carpark = carparkService.thIdToPark(parkId);
					if(carpark!=null){
						Long carparkid = carpark.getCarparkid();
						String parkName = carpark.getName();
						WeixinConfig config = weixinConfigService.nameToConfig("hik");
						if(config!=null){
							String token = config.getAccessToken();
							VehicleCostWechat vcw = vehicleCostWechatService.loadByInunid(lto.getInUnid());
							String payTime = vcw == null ? "" : SDF.format(vcw.getPlanPayTime());
							
							ParkPassVehicleMonth ppv = parkPassVehicleMonthService.loadByInunid(lto.getInUnid());
							
							List<UsersCars> carList = usersCarsService.getByHql("from UsersCars where carPlate = '" + lto.getCarPlate() + "'");
							String mobiles = "";
							for(UsersCars uc : carList){
								mobiles += "'" + uc.getMobile() + "',";
							}
							List<Users> userList = new ArrayList<Users>();
							if(mobiles.length() > 0){
								mobiles = mobiles.substring(0, mobiles.length() - 1);
								userList = usersService.getByHql("from Users where mobile in (" + mobiles + ") and openid is not null");
							}
							
							for(Users u : userList){
								String sendGet = WeixinUtils.sendOvertimeNotice(u.getOpenid(), token, parkName, (carparkid + 0L), lto.getCarPlate(), payTime, SDF.format(ppv.getPassTime()), lto.getInUnid());
								JSONObject jsonOb = JSONObject.parseObject(sendGet);
								if(jsonOb.getInteger("errcode") == 42001){
									net.sf.json.JSONObject tokenObject = WeixinUtils.getToken();
									token = tokenObject.getString("access_token");
									net.sf.json.JSONObject ticketObject = WeixinUtils.getTicket(token);
									String jsapiTicket = ticketObject.getString("ticket");
									config.setAccessToken(token);
									config.setJsapiTicket(jsapiTicket);
									weixinConfigService.merge(config);
									WeixinUtils.sendOvertimeNotice(u.getOpenid(), token, parkName, (carparkid + 0L), lto.getCarPlate(), payTime, SDF.format(ppv.getPassTime()), lto.getInUnid());
								}
							}
						}
							
//						System.out.println("推送成功");
						String reStr = "{\"message\": \"success\",\"errorCode\":300000}";
						String encrypt = AESOperator.getInstance().encrypt(reStr);
						out.print("{\"package\": \""+encrypt+"\"}");
					}else{
//						System.out.println(3);
						String reStr = "{\"message\": \"停车场ID不正确\",\"errorCode\": 300009}";
						String encrypt = AESOperator.getInstance().encrypt(reStr);
						out.print("{\"package\": \""+encrypt+"\"}");
					}
				}else{
//					System.out.println(4);
					String reStr = "{\"message\": \"参数解析失败\",\"errorCode\": 300009}";
					String encrypt = AESOperator.getInstance().encrypt(reStr);
					out.print("{\"package\": \""+encrypt+"\"}");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			String reStr = "{\"message\": \""+e.getMessage()+"\",\"errorCode\": 300009,\"token\": \"\"}";
			String encrypt = AESOperator.getInstance().encrypt(reStr);
			out.print("{\"package\": \""+encrypt+"\"}");
		}finally{
			out.flush();
			out.close();
		}
		return null;
	}
	
	/*@RequestMapping("LeaveTimeOutTest")
	public String LeaveTimeOutTest(HttpServletResponse response,HttpServletRequest request) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			ServletInputStream is = request.getInputStream();
//			String param = StringUtils.getDefaultInstance().ins2str(is);
//			if(!StringUtils.getDefaultInstance().isNoBlank(param)){
//				String reStr = "{\"message\": \"请求参数不正确\",\"errorCode\": 300009,\"token\": \"\"}";
//				String encrypt = AESOperator.getInstance().encrypt(reStr);
//				System.out.println(encrypt);
//				out.print("{\"package\": \""+encrypt+"\"}");
//			}else{
//				JSONObject parseObject = JSONObject.parseObject(param);
//				String enStr = parseObject.getString("package");
//				String praJson = AESOperator.getInstance().decrypt(enStr);
//				System.out.println("超时信息："+praJson);
//				LeaveTimeOut lto = JSONObject.parseObject(praJson, LeaveTimeOut.class);
			
				ParkPassVehicleMonth ppv1 = parkPassVehicleMonthService.retentionCar("川A11111");
			
				LeaveTimeOut lto = new LeaveTimeOut();
				lto.setCarPlate("川A11111");
				lto.setInUnid(ppv1.getInUnid());
				lto.setOverTime(3);
				lto.setParkId(String.valueOf(ppv1.getCarparkid()));
				if(lto!=null){
//					String parkId = lto.getParkId();
//					String sendGet = HttpRequest.sendGet(requestUrl+"/wxParkingList_thparkId2myParkid","parkid="+parkId);
//					JSONObject jsonOb = JSONObject.parseObject(sendGet);
//					if(jsonOb.getString("status").equals("true")){
						Integer carparkid = Integer.parseInt(lto.getParkId());
						String parkName = "";
						String sendGet = HttpRequest.sendGet(requestUrl+"/wxParkingList_getHikParkId","carparkid="+carparkid);
						JSONObject jsonOb = JSONObject.parseObject(sendGet);
						parkName = jsonOb.getString("parkName");
						String token = configService.getNameToToken("hik").getAccess_token();
						
						VehicleCostWechat vcw = vehicleCostWechatService.loadByInunid(lto.getInUnid());
						String payTime = vcw == null ? "" : SDF.format(vcw.getPlanPayTime());
						
						ParkPassVehicleMonth ppv = parkPassVehicleMonthService.loadByInunid(lto.getInUnid());
						
						List<UsersCars> carList = usersCarsService.getByHql("from UsersCars where carPlate = '" + lto.getCarPlate() + "'");
						String mobiles = "";
						for(UsersCars uc : carList){
							mobiles += "'" + uc.getMobile() + "',";
						}
						List<Users> userList = new ArrayList<Users>();
						if(mobiles.length() > 0){
							mobiles = mobiles.substring(0, mobiles.length() - 1);
							userList = usersService.getByHql("from Users where mobile in (" + mobiles + ") and openid is not null");
						}
						
						for(Users u : userList){
							sendGet = WeixinUtils.sendOvertimeNotice(u.getOpenid(), token, parkName, (carparkid + 0L), lto.getCarPlate(), payTime, SDF.format(ppv.getPassTime()), lto.getInUnid());
							jsonOb = JSONObject.parseObject(sendGet);
							if(jsonOb.getInteger("errcode") == 42001){
								net.sf.json.JSONObject tokenObject = WeixinUtils.getToken();
								token = tokenObject.getString("access_token");
								net.sf.json.JSONObject ticketObject = WeixinUtils.getTicket(token);
								String jsapiTicket = ticketObject.getString("ticket");
								HttpRequest.sendGet(requestUrl+"/wxConfig_save", "name=hik&token="+token+"&jsapiTicket="+jsapiTicket);
								WeixinUtils.sendOvertimeNotice(u.getOpenid(), token, parkName, (carparkid + 0L), lto.getCarPlate(), payTime, SDF.format(ppv.getPassTime()), lto.getInUnid());
							}
						}
							
						System.out.println("推送成功");
						String reStr = "{\"message\": \"success\",\"errorCode\":300000}";
						String encrypt = AESOperator.getInstance().encrypt(reStr);
						out.print("{\"package\": \""+encrypt+"\"}");
//					}else{
//						System.out.println(3);
//						String reStr = "{\"message\": \"停车场ID不正确\",\"errorCode\": 300009}";
//						String encrypt = AESOperator.getInstance().encrypt(reStr);
//						out.print("{\"package\": \""+encrypt+"\"}");
//					}
				}else{
					System.out.println(4);
					String reStr = "{\"message\": \"参数解析失败\",\"errorCode\": 300009}";
					String encrypt = AESOperator.getInstance().encrypt(reStr);
					out.print("{\"package\": \""+encrypt+"\"}");
				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
			String reStr = "{\"message\": \""+e.getMessage()+"\",\"errorCode\": 300009,\"token\": \"\"}";
			String encrypt = AESOperator.getInstance().encrypt(reStr);
			out.print("{\"package\": \""+encrypt+"\"}");
		}finally{
			out.flush();
			out.close();
		}
		return null;
	}*/

}
