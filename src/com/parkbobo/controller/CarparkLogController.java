package com.parkbobo.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.parkbobo.model.Carpark;
import com.parkbobo.model.CarparkBerth;
import com.parkbobo.model.ParkPassVehicle;
import com.parkbobo.model.Users;
import com.parkbobo.model.UsersCars;
import com.parkbobo.model.VehicleCost;
import com.parkbobo.model.WeixinConfig;
import com.parkbobo.service.CarparkBerthService;
import com.parkbobo.service.CarparkService;
import com.parkbobo.service.ParkPassVehicleService;
import com.parkbobo.service.UsersCarsService;
import com.parkbobo.service.UsersService;
import com.parkbobo.service.VehicleCostService;
import com.parkbobo.service.WeixinConfigService;
import com.parkbobo.utils.AESOperator;
import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.HttpRequest;
import com.parkbobo.utils.StringUtils;
import com.parkbobo.utils.httpmodel.PassCarPort;
import com.parkbobo.utils.httpmodel.PassVehicle;
import com.parkbobo.utils.weixin.WeixinUtils;

/**
 * 向海康提供的过车接口于车位接口
 * @author gjx
 * @version 1.0
 * 
 * @version 1.1
 * @author RY
 * @since 2017-7-14 15:02:23
 * 车位接口增加向地图推送车位信息逻辑
 *
 */

@Controller
public class CarparkLogController {
	@Resource
	private CarparkBerthService carparkBerthService;
	@Resource
	private ParkPassVehicleService parkPassVehicleService;
	@Resource(name="usersCarsService")
	private UsersCarsService usersCarsService;
	@Resource(name="usersService")
	private UsersService usersService;
	@Resource
	private CarparkService carparkService;
	@Resource
	private WeixinConfigService weixinConfigService;
	@Resource(name="vehicleCostService")
	private VehicleCostService vehicleCostService;
	private static final String MAPURL = Configuration.getInstance().getValue("mapUrl");
	
	@RequestMapping("PassVehicle")
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
				System.out.println("进出记录："+praJson);
				PassVehicle passVehicle = JSONObject.parseObject(praJson, PassVehicle.class);
				if(passVehicle!=null){
					String parkId = passVehicle.getParkId();
					Carpark carpark = this.carparkService.thIdToPark(parkId);
					if(carpark!=null){
						Long carparkid = carpark.getCarparkid();
						ParkPassVehicle parkPassVehicle = new ParkPassVehicle();
						parkPassVehicle.setId(UUID.randomUUID().toString().replace("-", ""));
						parkPassVehicle.setCarparkid(carparkid);
						parkPassVehicle.setCardno(passVehicle.getCardNo());
						parkPassVehicle.setCarplate(passVehicle.getCarPlate());
						parkPassVehicle.setCartype(passVehicle.getCarType());
						parkPassVehicle.setDirect(passVehicle.getDirect());
						parkPassVehicle.setGateid(passVehicle.getGateId());
						parkPassVehicle.setImageurl(passVehicle.getImageUrl());
						parkPassVehicle.setParktype(passVehicle.getParkType());
						parkPassVehicle.setPassTime(StringUtils.getDefaultInstance().str2time(passVehicle.getPassTime()));
						parkPassVehicle.setPlatecolor(passVehicle.getPlateColor());
						parkPassVehicle.setPlatetype(passVehicle.getPlateType());
						parkPassVehicle.setInUnid(passVehicle.getUnid());
						parkPassVehicleService.add(parkPassVehicle);
						String parkName = carpark.getName();
						WeixinConfig config = weixinConfigService.nameToConfig("hik");
						if(config!=null){
							String token = config.getAccessToken();
							List<UsersCars> carList = usersCarsService.getByHql("from UsersCars where carPlate = '" + passVehicle.getCarPlate() + "'");
							String mobiles = "";
							for(UsersCars uc : carList){
								mobiles += "'" + uc.getMobile() + "',";
							}
							List<Users> userList = new ArrayList<Users>();
							if(mobiles.length() > 0){
								mobiles = mobiles.substring(0, mobiles.length() - 1);
								userList = usersService.getByHql("from Users where mobile in (" + mobiles + ") and openid is not null");
							}
							if(passVehicle.getDirect().equals(0)){
								for(Users u : userList){
									String sendGet = WeixinUtils.sendEnterNotice(u.getOpenid(), token, parkName, Long.parseLong(String.valueOf(carparkid)), passVehicle.getCarPlate(), passVehicle.getPassTime(), passVehicle.getUnid());
									JSONObject jsonOb = JSONObject.parseObject(sendGet);
									if(jsonOb.getInteger("errcode") == 42001){
										net.sf.json.JSONObject tokenObject = WeixinUtils.getToken();
										token = tokenObject.getString("access_token");
										net.sf.json.JSONObject ticketObject = WeixinUtils.getTicket(token);
										String jsapiTicket = ticketObject.getString("ticket");
										config.setJsapiTicket(jsapiTicket);
										config.setAccessToken(token);
										weixinConfigService.merge(config);
										WeixinUtils.sendEnterNotice(u.getOpenid(), token, parkName, Long.parseLong(String.valueOf(carparkid)), passVehicle.getCarPlate(), passVehicle.getPassTime(), passVehicle.getUnid());
									}
								}
							}else{
//								if(config!=null){
//									VehicleCost vc = vehicleCostService.carout2Ve(passVehicle.getCarPlate(),parkPassVehicle.getPassTime());
//									if(vc != null){
//										for(Users u : userList){
//											String sendGet = "";
//											if(Double.valueOf(vc.formatTotalMoney())<50000){
//												sendGet = WeixinUtils.sendLeaveNotice(u.getOpenid(), token, parkName, vc.formateParkTime(), passVehicle.getCarPlate(), passVehicle.getPassTime(), vc.formatePayMoney());												
//											}
//											JSONObject jsonOb = JSONObject.parseObject(sendGet);
//											if(jsonOb.getInteger("errcode") == 42001){
//												net.sf.json.JSONObject tokenObject = WeixinUtils.getToken();
//												token = tokenObject.getString("access_token");
//												net.sf.json.JSONObject ticketObject = WeixinUtils.getTicket(token);
//												String jsapiTicket = ticketObject.getString("ticket");
//												config.setAccessToken(token);
//												config.setJsapiTicket(jsapiTicket);
//												if(Double.valueOf(vc.formatTotalMoney())<50000){
//													sendGet = WeixinUtils.sendLeaveNotice(u.getOpenid(), token, parkName, vc.formateParkTime(), passVehicle.getCarPlate(), passVehicle.getPassTime(), vc.formatePayMoney());
//												}
//											}
//										}
//									}
//								}	
								
							}
						}
						String reStr = "{\"message\": \"success\",\"errorCode\":300000,\"token\": \"\"}";
						String encrypt = AESOperator.getInstance().encrypt(reStr);
						out.print("{\"package\": \""+encrypt+"\"}");
					}else{
						String reStr = "{\"message\": \"停车场ID不正确\",\"errorCode\": 300009,\"token\": \"\"}";
						String encrypt = AESOperator.getInstance().encrypt(reStr);
						out.print("{\"package\": \""+encrypt+"\"}");
					}
				}else{
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
	@RequestMapping("PassCarPort")
	public String passCarPort(HttpServletResponse response,HttpServletRequest request) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			ServletInputStream is = request.getInputStream();
			String param = StringUtils.getDefaultInstance().ins2str(is);
			if(!StringUtils.getDefaultInstance().isNoBlank(param)){
				String reStr = "{\"message\": \"请求参数不正确\",\"errorCode\": 300009,\"token\": \"\"}";
				String encrypt = AESOperator.getInstance().encrypt(reStr);
				out.print("{\"package\": \""+encrypt+"\"}");
			}else{
				JSONObject parseObject = JSONObject.parseObject(param);
				String enStr = parseObject.getString("package");
				String praJson = AESOperator.getInstance().decrypt(enStr);
				System.out.println("车位记录："+praJson);
				PassCarPort passCarPort = JSONObject.parseObject(praJson, PassCarPort.class);
				if(passCarPort!=null){
					String parkId = passCarPort.getParkId();
					Carpark carpark = carparkService.thIdToPark(parkId);
					if(carpark!=null){
						Long carparkid = carpark.getCarparkid();
						CarparkBerth carparkBerth = new CarparkBerth();
						carparkBerth.setSpotId(passCarPort.getSpotId());
						carparkBerth.setCarparkid(carparkid);
						carparkBerth.setCarPlate(passCarPort.getCarPlate());
						carparkBerth.setCarType(passCarPort.getCarType()+"");
						carparkBerth.setDirect(passCarPort.getDirect());
						carparkBerth.setImageUrl(passCarPort.getImageUrl());
						carparkBerth.setParkType(passCarPort.getParkType());
						carparkBerth.setPassTime(StringUtils.getDefaultInstance().str2time(passCarPort.getPassTime()));
						carparkBerth.setPlateColor(passCarPort.getPlateColor());
						carparkBerth.setPlateType(passCarPort.getPlateType());
						carparkBerth.setFloorCode(passCarPort.getFlourCode());
						//删除该停车场该车位数据
						this.carparkBerthService.deleteBerths(carparkid, passCarPort.getSpotId(), passCarPort.getDirect());
						carparkBerthService.add(carparkBerth );
//						System.out.println(passCarPort.getCarPlate() + "--" + passCarPort.getFlourCode());
						//推送车位信息到地图
//						System.out.println("推送车位信息到地图");
//						sendGet = HttpRequest.sendGet(MAPURL+"/map_updateParkLotStatus",
//								"floorid="+passCarPort.getFlourCode()+"&name="+passCarPort.getSpotId()+"&status="+passCarPort.getDirect()+"&plateNum="+passCarPort.getCarPlate());
//						String sendPost = HttpRequest.postPort(MAPURL+"/map_updateParkLotStatus",
//								"floorid="+passCarPort.getFlourCode()+"&name="+passCarPort.getSpotId()+"&status="+passCarPort.getDirect()+"&plateNum="+passCarPort.getCarPlate());
//						System.out.println("推送返回：" + sendPost);
						HttpClient httpClient = new HttpClient();
						httpClient.getHostConfiguration().setHost("www.jiuchengpark.cn", 80);
						PostMethod method = new PostMethod("/jcmap/map_updateParkLotStatus");
						try {
							
							method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
							NameValuePair floorid = new NameValuePair("floorid", passCarPort.getFlourCode());
							NameValuePair name = new NameValuePair("name", passCarPort.getSpotId());
							NameValuePair portStatus = new NameValuePair("status", passCarPort.getDirect() + "");
							NameValuePair plateNum = new NameValuePair("plateNum", passCarPort.getCarPlate());
							method.addParameters(new NameValuePair[] {floorid, name, portStatus, plateNum});
							int status = httpClient.executeMethod(method);
							System.out.println(status);
							System.out.println(method.getResponseBodyAsString());
						} catch (Exception e) {
							e.printStackTrace();
						}
						finally{
							if(method != null){
								method.releaseConnection();
							}
						}
						
						String reStr = "{\"message\": \"success\",\"errorCode\":300000,\"token\": \"\"}";
						String encrypt = AESOperator.getInstance().encrypt(reStr);
						out.print("{\"package\": \""+encrypt+"\"}");
					}else{
						String reStr = "{\"message\": \"停车场ID不正确\",\"errorCode\": 300009,\"token\": \"\"}";
						String encrypt = AESOperator.getInstance().encrypt(reStr);
						out.print("{\"package\": \""+encrypt+"\"}");
					}
				}else{
					String reStr = "{\"message\": \"参数解析失败\",\"errorCode\": 300009,\"token\": \"\"}";
					String encrypt = AESOperator.getInstance().encrypt(reStr);
					out.print("{\"package\": \""+encrypt+"\"}");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
//			System.out.println(e.getMessage());
			out.print("{\"message\": \""+e.getMessage()+"\",\"errorCode\": 300009,\"token\": \"\"}");						

		}finally{
			out.flush();
			out.close();
		}
		return null;
	}
	
	
	
	
	public static void main(String[] args) throws HttpException, IOException {
		System.out.println("推送车位信息到地图");
		
//		String sendGet = HttpRequest.postPort(MAPURL+"/map_updateParkLotStatus",
//				"floorid=104010&name=A87&status=0&plateNum=川A12345");
//		System.out.println("推送返回：" + sendGet);
		
		HttpClient httpClient = new HttpClient();
		httpClient.getHostConfiguration().setHost("www.jiuchengpark.cn", 80);
		PostMethod method = new PostMethod("/jcmap/map_updateParkLotStatus");
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		NameValuePair floorid = new NameValuePair("floorid", "104010");
		NameValuePair name = new NameValuePair("name", "A87");
		NameValuePair portStatus = new NameValuePair("status", "0");
		NameValuePair plateNum = new NameValuePair("plateNum", "川A12345");
		method.addParameters(new NameValuePair[] {floorid, name, portStatus, plateNum});
		int status = httpClient.executeMethod(method);
		System.out.println(status);
		System.out.println(method.getResponseBodyAsString());
		method.releaseConnection();
		
	}
	
}
