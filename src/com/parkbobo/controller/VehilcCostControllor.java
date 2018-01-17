package com.parkbobo.controller;

import java.io.PrintWriter;
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
import com.parkbobo.model.Users;
import com.parkbobo.model.UsersCars;
import com.parkbobo.model.VehicleCost;
import com.parkbobo.model.WeixinConfig;
import com.parkbobo.service.CarparkService;
import com.parkbobo.service.UsersCarsService;
import com.parkbobo.service.UsersService;
import com.parkbobo.service.VehicleCostService;
import com.parkbobo.service.WeixinConfigService;
import com.parkbobo.utils.AESOperator;
import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.HttpRequest;
import com.parkbobo.utils.StringUtils;
import com.parkbobo.utils.httpmodel.VehicleCostModel;
import com.parkbobo.utils.weixin.WeixinUtils;

/**
 * 向海康提供的收费数据上传接口
 * @author RY
 * @version 1.0
 * @since 2017-6-27 14:22:54
 *
 */

@Controller
public class VehilcCostControllor {
	@Resource(name="vehicleCostService")
	private VehicleCostService vehicleCostService;
	@Resource(name="usersCarsService")
	private UsersCarsService usersCarsService;
	@Resource(name="usersService")
	private UsersService usersService;
	@Resource
	private CarparkService carparkService;
	@Resource
	private WeixinConfigService weixinConfigService;
	
	@RequestMapping("CostData")
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
				System.out.println("收费记录："+praJson);
				VehicleCostModel vcm = JSONObject.parseObject(praJson, VehicleCostModel.class);
				if(vcm!=null){
					String parkId = vcm.getParkId();
					Carpark carpark = carparkService.thIdToPark(parkId);
					if(carpark!=null){
						Long carparkid = carpark.getCarparkid();
						VehicleCost vc = new VehicleCost();
						vc.setBookUnid(vcm.getBookUnid());
						vc.setCardNo(vcm.getCardNo());
						vc.setCarPlate(vcm.getCarPlate());
						vc.setCarType(vcm.getCarType());
						vc.setCollectorCode(vcm.getCollectorCode());
						vc.setCollectorID(vcm.getCollectorID());
						vc.setCollectorName(vcm.getCollectorName());
						vc.setCollectorPhone(vcm.getCollectorPhone());
						vc.setGateId(vcm.getGateId());
						vc.setInTime(vcm.formatInTime());
						vc.setInUnid(vcm.getInUnid());
						vc.setIsLeave(vcm.getIsLeave());
						vc.setNeedPay(vcm.getNeedPay());
						vc.setOutTime(vcm.formatOutTime());
						vc.setOutUnid(vcm.getOutUnid());
						vc.setParkid(carparkid);
						vc.setParkTime(vcm.getParkTime());
						vc.setPayMoney(vcm.getPayMoney());
						vc.setPayType(vcm.getPayType());
						vc.setPlateColor(vcm.getPlateColor());
						vc.setPlateType(vcm.getPlateType());
						vc.setParkType(vcm.getParkType());
						vc.setRemark(vcm.getRemark());
						vc.setTotalMoney(vcm.getTotalMoney());
						
						vc = vehicleCostService.add(vc);
						
						List<UsersCars> carList = usersCarsService.getByHql("from UsersCars where carPlate = '" + vc.getCarPlate() + "'");
						String mobiles = "";
						for(UsersCars uc : carList){
							mobiles += "'" + uc.getMobile() + "',";
						}
						List<Users> userList = new ArrayList<Users>();
						if(mobiles.length() > 0){
							mobiles = mobiles.substring(0, mobiles.length() - 1);
							userList = usersService.getByHql("from Users where mobile in (" + mobiles + ") and openid is not null");
						}
						
						if(vc != null){
							String parkName = carpark.getName();
							WeixinConfig weixinConfig = weixinConfigService.nameToConfig("hik");
							if(weixinConfig!=null){
								String token = weixinConfig.getAccessToken();
							
								for(Users u : userList){
	//								WeixinUtils.sendLeaveNotice(u.getOpenid(), token, parkName, "3小时50分钟", passVehicle.getCarPlate(), passVehicle.getPassTime(), "15.00元");
									String sendGet = "";
									if(Double.valueOf(vc.getTotalMoney())<50000){
										sendGet = WeixinUtils.sendLeaveNotice(u.getOpenid(), token, parkName, vc.formateParkTime(), vc.getCarPlate(), vc.formatOutTime(), vc.formatTotalMoney());
									}
									JSONObject jsonOb = JSONObject.parseObject(sendGet);
									if(jsonOb!=null && jsonOb.getInteger("errcode") == 42001){
										net.sf.json.JSONObject tokenObject = WeixinUtils.getToken();
										token = tokenObject.getString("access_token");
										net.sf.json.JSONObject ticketObject = WeixinUtils.getTicket(token);
										String jsapiTicket = ticketObject.getString("ticket");
										weixinConfig.setAccessToken(token);
										weixinConfig.setJsapiTicket(jsapiTicket);
										if(Double.valueOf(vc.formatTotalMoney())<50000){
											WeixinUtils.sendLeaveNotice(u.getOpenid(), token, parkName, vc.formateParkTime(), vc.getCarPlate(), vc.formatOutTime(), vc.formatTotalMoney());											
										}
									}
								}
							}
						}
						
						
//						System.out.println("保存成功");
						String reStr = "{\"message\": \"success\",\"errorCode\":300000,\"token\": \"\"}";
						String encrypt = AESOperator.getInstance().encrypt(reStr);
						out.print("{\"package\": \""+encrypt+"\"}");
					}else{
//						System.out.println(3);
						String reStr = "{\"message\": \"停车场ID不正确\",\"errorCode\": 300009,\"token\": \"\"}";
						String encrypt = AESOperator.getInstance().encrypt(reStr);
						out.print("{\"package\": \""+encrypt+"\"}");
					}
				}else{
//					System.out.println(4);
					String reStr = "{\"message\": \"参数解析失败\",\"errorCode\": 300009,\"token\": \"\"}";
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

}
