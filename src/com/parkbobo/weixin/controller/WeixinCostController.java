package com.parkbobo.weixin.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
import com.parkbobo.utils.http.HttpUtils;
import com.parkbobo.utils.httpmodel.VehicleOrder;
import com.parkbobo.utils.weixin.WeixinUtils;
import com.parkbobo.wxpay.WXPayConfigImpl;
import com.parkbobo.wxpay.WXPayRequest;
import com.parkbobo.wxpay.WXPayUtil;
import com.pay.pingpg.ChargeUtils;

/**
 * 微信支付
 * @author RY
 * @version 1.0
 * @since 2017-6-26 09:20:57
 *
 */

@Controller
public class WeixinCostController implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8366846930145145351L;
	@Resource(name="vehicleCostWechatService")
	private VehicleCostWechatService vehicleCostWechatService;
	@Resource(name="usersCarsService")
	private UsersCarsService usersCarsService;
	@Resource(name="usersService")
	private UsersService usersService;
	@Resource(name="parkPassVehicleMonthService")
	private ParkPassVehicleMonthService parkPassVehicleMonthService;
	@Resource
	private CarparkService carparkService;
	@Resource
	private WeixinConfigService weixinConfigService;
	private static final String APPID = Configuration.getInstance().getValue("hik_appid");
	private static final String mchID = Configuration.getInstance().getValue("mch_id");
	//private static final String subMchID = Configuration.getInstance().getValue("sub_mch_id");
	private static final String key = Configuration.getInstance().getValue("key");

	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@RequestMapping("wxCost_toCost")
	public ModelAndView toCost(String code,String state,String inunid, String openid,HttpServletRequest request,HttpServletResponse response) throws Exception{
		ModelAndView mv = new ModelAndView();
		String domain = Configuration.getInstance().getValue("domainName");
		String path = request.getContextPath();
		String appID = Configuration.getInstance().getValue("hik_appid");
		try {
			if(code==null){
				String URL = domain+path+"/wxCost_toCost";
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + 
				"appid="+appID+"&" + 
				"redirect_uri=" + URLEncoder.encode(URL, "UTF-8") + 
				"&response_type=code&scope=snsapi_base&state="+inunid+","+openid+"#wechat_redirect";
				response.setStatus(301);
				response.setHeader("Location",url);
				response.setHeader("Connection","close");
				return null;
			}else{
				String[] params = state.split(",");
				inunid = params[0];
				openid = params[1];
				
			    List<Carpark> carparks = this.carparkService.getAll();
			    
				Users user = usersService.loadByOpenid(openid);
				List<UsersCars> carList = usersCarsService.loadByMobile(user.getMobile());
				
				ParkPassVehicleMonth ppv = parkPassVehicleMonthService.loadByInunid(inunid);
				ParkPassVehicleMonth isIn = parkPassVehicleMonthService.retentionCar(ppv.getCarplate());
				if(isIn != null){
//					String hql = "from VehicleCostWechat where inUnid = '" + inunid + "' and status = 1 and pushStatus = 0";
//					List<VehicleCostWechat> needPusthList = vehicleCostWechatService.getByHql(hql);
//					if(needPusthList.size() > 0){
//						push(inunid);
//					}
//					//再次检测，如果仍存在，停止请求账单，返回错误
//					needPusthList = vehicleCostWechatService.getByHql(hql);
//					if(needPusthList.size() > 0){
//						mv.addObject("errMsg", "停车费用获取失败");
//						mv.setViewName("weixin/tsp/toWxCost");	
//					}
//					else{
						
					//获取停车费用信息
					String parkCode = "";
					Carpark carpark = this.carparkService.get(isIn.getCarparkid());
					parkCode = carpark.getThirdId();
					String parkName = carpark.getName();
					
					String reqIp = Configuration.getInstance().getValue("hikip");
					String url = reqIp+"/GetVehicleCost";
					StringBuilder body = new StringBuilder();
					body.append("{");
					body.append("\"token\":\"\",");
					body.append("\"parkingCode\":\""+parkCode+"\",");
					body.append("\"plateInfo\":\""+isIn.getCarplate()+"\",");
					body.append("\"plateColor\":"+isIn.getPlatecolor()+",");
					body.append("\"carType\":"+isIn.getCartype()+",");
					body.append("\"inUnid\":\""+inunid+"\",");
					body.append("\"inTime\":\""+SDF.format(isIn.getPassTime())+"\"");
					body.append("}");
//					System.out.println(body.toString());
					//加密
					String enStr = AESOperator.getInstance().encrypt(body.toString());
					String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
//					System.out.println(reJson);
					if(reJson!=null){
						//解密停车费用信息
						JSONObject jsonObject = JSONObject.parseObject(reJson);
						String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
						System.out.println("停车费用信息：" + decrypt);
						VehicleOrder vo = JSONObject.parseObject(decrypt, VehicleOrder.class);
						if(vo.getErrorCode().equals(300000)){
							//删除未支付账单
							vehicleCostWechatService.deleteUnpayed(isIn.getInUnid());
							//获取已支付最近一次账单
							VehicleCostWechat lastPayed = vehicleCostWechatService.loadByInunid(vo.getInUnid());
							Date lastPayTime = lastPayed == null ? null : lastPayed.getPlanPayTime();
//							System.out.println("删除未支付账单！！");
							//根据payUnid检查是否已经存储
							VehicleCostWechat newvcw = vehicleCostWechatService.loadByPayUnid(vo.getPayUnid());
							if(newvcw == null){
								VehicleCostWechat vcw = new VehicleCostWechat();
								vcw.setCarparkid(isIn.getCarparkid());
								vcw.setCarPlate(isIn.getCarplate());
								vcw.setInTime(isIn.getPassTime());
								vcw.setInUnid(isIn.getInUnid());
//									vcw.setLastPayTime(vo.formatLastPayTime());
								vcw.setLastPayTime(lastPayTime);
								vcw.setPayUnid(vo.getPayUnid());
								vcw.setPlanPayTime(vo.formatPlanPayTime());
								vcw.setShouldPayMoney(vo.getShouldPayMoney());
								vcw.setTerminCode(vo.getTerminCode());
								vcw.setStatus(0);
								vcw.setPushStatus(0);
								newvcw = vehicleCostWechatService.add(vcw);
							}
							else{
								newvcw.setLastPayTime(lastPayTime);
								vehicleCostWechatService.update(newvcw);
							}
							//根据inUnid获取账单记录，计算应缴，已缴，本次需缴
							List<VehicleCostWechat> vcwList = vehicleCostWechatService.loadListWithInUnid(vo.getInUnid());
							
							Integer shouldPay, hasPayed, needPay;
							shouldPay = hasPayed = needPay = 0;
							for(VehicleCostWechat vcw1 : vcwList){
								shouldPay += vcw1.getShouldPayMoney();
								if(vcw1.getStatus() == 1){
									hasPayed += vcw1.getShouldPayMoney();
								}
								else{
									needPay += vcw1.getShouldPayMoney();
								}
							}
							
							mv.addObject("vcw", newvcw);
							mv.addObject("shouldPay", formatMoney(shouldPay));
							mv.addObject("hasPayed", formatMoney(hasPayed));
							mv.addObject("needPay", formatMoney(needPay));
							mv.addObject("parkId", isIn.getCarparkid());
							if(needPay>20000){
								mv.addObject("errMsg", "停车费用获取失败");
								mv.setViewName("weixin/tsp/toWxCost");
							}else{
								mv.setViewName("weixin/tsp/toWxCost");								
							}
						}else{
							mv.addObject("errMsg", "停车费用获取失败");
							mv.setViewName("weixin/tsp/toWxCost");							
						}
					}else{
						mv.addObject("errMsg", "停车费用获取失败");
						mv.setViewName("weixin/tsp/toWxCost");			
					}
					mv.addObject("parkName", parkName);
					mv.addObject("ppv", isIn);
				}
					
//				}
				else{
					mv.addObject("errMsg", "该车辆不在停车场内");
					mv.setViewName("weixin/tsp/toWxCost");
				}
				mv.addObject("hikParking", carparks);
			    mv.addObject("carList", carList);
			    mv.addObject("users", user);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mv.addObject("errMsg", "系统错误");
			mv.setViewName("weixin/tsp/toWxCost");
		}
		return mv;
	}
	
	/**
	 * 缴费成功后推送信息给海康
	 * @param kid
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("wxCost_pushCost")
	public String puchCost(Integer kid, String chargeid, HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			//更新缴费记录为已支付
			VehicleCostWechat vcw = vehicleCostWechatService.get(kid);
			vcw.setStatus(1);
			vcw.setPayTime(new Date());
			vehicleCostWechatService.update(vcw);
			
			//向下级客户端推送
			ParkPassVehicleMonth ppv = parkPassVehicleMonthService.loadByInunid(vcw.getInUnid());
			String parkCode = "";
			Carpark carpark = this.carparkService.get(vcw.getCarparkid());
			parkCode = carpark.getThirdId();
			
			String reqIp = Configuration.getInstance().getValue("hikip");
			String url = reqIp+"/PushPayCostStatus";
			StringBuilder body = new StringBuilder();
			body.append("{");
			body.append("\"token\":\"\",");
			body.append("\"terminCode\":\""+vcw.getTerminCode()+"\",");
			body.append("\"parkingCode\":\""+parkCode+"\",");
			body.append("\"plateInfo\":\""+vcw.getCarPlate()+"\",");
			body.append("\"plateColor\":"+ppv.getPlatecolor()+",");
			body.append("\"payUnid\":\""+vcw.getPayUnid()+"\",");
			body.append("\"parkType\":"+ppv.getParktype()+",");
			body.append("\"collectorName\":\"微信支付\",");
			body.append("\"inTime\":\""+SDF.format(ppv.getPassTime())+"\",");
			body.append("\"payTime\":\""+SDF.format(vcw.getPlanPayTime())+"\",");
			body.append("\"inUnid\":\""+vcw.getInUnid()+"\",");
			body.append("\"startTime\":\""+(vcw.getLastPayTime() == null ? SDF.format(vcw.getInTime()) : SDF.format(vcw.getLastPayTime()))+"\",");
			body.append("\"endTime\":\""+SDF.format(vcw.getPlanPayTime())+"\",");
			body.append("\"payType\":3,");
			body.append("\"payStatus\":" + vcw.getStatus() + ",");
			body.append("\"payMoney\":"+vcw.getShouldPayMoney()+"");
			body.append("}");
			//加密
			String enStr = AESOperator.getInstance().encrypt(body.toString());
//			System.out.println(body.toString());
			String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
			if(reJson!=null){
				JSONObject jsonObject = JSONObject.parseObject(reJson);
				String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
				System.out.println(decrypt);
				jsonObject = JSONObject.parseObject(decrypt);
				if(jsonObject.getInteger("errorCode")==300000){
					vcw.setPushStatus(1);
					vcw.setLeaveOut(jsonObject.getInteger("freeTime"));
					vehicleCostWechatService.update(vcw);
					WeixinConfig config = this.weixinConfigService.nameToConfig("hik");
					if(config!=null){
						String token = config.getAccessToken();
						List<UsersCars> carList = usersCarsService.getByHql("from UsersCars where carPlate = '" + vcw.getCarPlate() + "'");
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
							String sendGet = WeixinUtils.sendPaymentNotice(u.getOpenid(), token, vcw.getCarPlate(), vcw.formatPayTime(), vcw.formatInTime(), vcw.formatParkTime(), vcw.formateShouldPayMoney());
							JSONObject jsonOb = JSONObject.parseObject(sendGet);
							if(jsonOb.getInteger("errcode") == 42001){
								net.sf.json.JSONObject tokenObject = WeixinUtils.getToken();
								token = tokenObject.getString("access_token");
								net.sf.json.JSONObject ticketObject = WeixinUtils.getTicket(token);
								String jsapiTicket = ticketObject.getString("ticket");
								config.setAccessToken(token);
								config.setJsapiTicket(jsapiTicket);
								weixinConfigService.merge(config);
								WeixinUtils.sendPaymentNotice(u.getOpenid(), token, vcw.getCarPlate(), vcw.formatPayTime(), vcw.formatInTime(), vcw.formatParkTime(), vcw.formateShouldPayMoney());
							}
						}
					}
					out.print("{\"status\":\"success\",\"errorcode\":\"00\"}");
				}
				else{
					//退款处理
					ChargeUtils.getInstance().refund(chargeid, vcw.getShouldPayMoney() + 0l, "网络连接异常");
					vcw.setStatus(0);
					vcw.setPayTime(null);
					vehicleCostWechatService.update(vcw);
//					vehicleCostWechatService.delete(vcw.getKid());
//					System.out.println("退款");
//					vehicleCostWechatService.deleteUnpayed(inunid)
					out.print("{\"status\":\"false\",\"errorcode\":\"11\"}");
				}
				
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
	 * 缴费成功后推送信息给海康
	 * @param kid
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("wxCost_xyPushCost")
	public String xyPushCost(Integer kid, String chargeid, HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			//更新缴费记录为已支付
			VehicleCostWechat vcw = vehicleCostWechatService.get(kid);
			vcw.setStatus(1);
			vcw.setPayTime(new Date());
			vehicleCostWechatService.update(vcw);
			
			//向下级客户端推送
			ParkPassVehicleMonth ppv = parkPassVehicleMonthService.loadByInunid(vcw.getInUnid());
			String parkCode = "";
			Carpark carpark = this.carparkService.get(vcw.getCarparkid());
			parkCode = carpark.getThirdId();
			
			String reqIp = Configuration.getInstance().getValue("hikip");
			String url = reqIp+"/PushPayCostStatus";
			StringBuilder body = new StringBuilder();
			body.append("{");
			body.append("\"token\":\"\",");
			body.append("\"terminCode\":\""+vcw.getTerminCode()+"\",");
			body.append("\"parkingCode\":\""+parkCode+"\",");
			body.append("\"plateInfo\":\""+vcw.getCarPlate()+"\",");
			body.append("\"plateColor\":"+ppv.getPlatecolor()+",");
			body.append("\"payUnid\":\""+vcw.getPayUnid()+"\",");
			body.append("\"parkType\":"+ppv.getParktype()+",");
			body.append("\"collectorName\":\"微信支付\",");
			body.append("\"inTime\":\""+SDF.format(ppv.getPassTime())+"\",");
			body.append("\"payTime\":\""+SDF.format(vcw.getPlanPayTime())+"\",");
			body.append("\"inUnid\":\""+vcw.getInUnid()+"\",");
			body.append("\"startTime\":\""+(vcw.getLastPayTime() == null ? SDF.format(vcw.getInTime()) : SDF.format(vcw.getLastPayTime()))+"\",");
			body.append("\"endTime\":\""+SDF.format(vcw.getPlanPayTime())+"\",");
			body.append("\"payType\":3,");
			body.append("\"payStatus\":" + vcw.getStatus() + ",");
			body.append("\"payMoney\":"+vcw.getShouldPayMoney()+"");
			body.append("}");
			//加密
			String enStr = AESOperator.getInstance().encrypt(body.toString());
//			System.out.println(body.toString());
			String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
			if(reJson!=null){
				JSONObject jsonObject = JSONObject.parseObject(reJson);
				String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
				System.out.println(decrypt);
				jsonObject = JSONObject.parseObject(decrypt);
				if(jsonObject.getInteger("errorCode")==300000){
					vcw.setPushStatus(1);
					vcw.setLeaveOut(jsonObject.getInteger("freeTime"));
					vehicleCostWechatService.update(vcw);
					WeixinConfig config = weixinConfigService.nameToConfig("hik");
					if(config!=null){
						String token = config.getAccessToken();
						List<UsersCars> carList = usersCarsService.getByHql("from UsersCars where carPlate = '" + vcw.getCarPlate() + "'");
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
							String sendGet = WeixinUtils.sendPaymentNotice(u.getOpenid(), token, vcw.getCarPlate(), vcw.formatPayTime(), vcw.formatInTime(), vcw.formatParkTime(), vcw.formateShouldPayMoney());
							JSONObject jsonOb = JSONObject.parseObject(sendGet);
							if(jsonOb.getInteger("errcode") == 42001){
								net.sf.json.JSONObject tokenObject = WeixinUtils.getToken();
								token = tokenObject.getString("access_token");
								net.sf.json.JSONObject ticketObject = WeixinUtils.getTicket(token);
								String jsapiTicket = ticketObject.getString("ticket");
								config.setAccessToken(token);
								config.setJsapiTicket(jsapiTicket);
								weixinConfigService.merge(config);
								WeixinUtils.sendPaymentNotice(u.getOpenid(), token, vcw.getCarPlate(), vcw.formatPayTime(), vcw.formatInTime(), vcw.formatParkTime(), vcw.formateShouldPayMoney());
							}
						}
					}
					out.print("{\"status\":\"success\",\"errorcode\":\"00\"}");
				}
				else{
					//退款处理
					ChargeUtils.getInstance().refund(chargeid, vcw.getShouldPayMoney() + 0l, "网络连接异常");
					vcw.setStatus(0);
					vcw.setPayTime(null);
					vehicleCostWechatService.update(vcw);
//					vehicleCostWechatService.delete(vcw.getKid());
//					System.out.println("退款");
//					vehicleCostWechatService.deleteUnpayed(inunid)
					out.print("{\"status\":\"false\",\"errorcode\":\"11\"}");
				}
				
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
	 * 扫描二维码进入缴费界面
	 * @param code
	 * @param state
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("wxCost_qrCode")
	public ModelAndView qrCode(String code,String state,HttpServletRequest request,HttpServletResponse response) throws Exception{
		ModelAndView mv = new ModelAndView();
		String domain = Configuration.getInstance().getValue("domainName");
		String path = request.getContextPath();
		String appID = Configuration.getInstance().getValue("hik_appid");
		String appsecret = Configuration.getInstance().getValue("hik_appsecret");
		try {
			//重定向code
			if(code==null){
				String URL = domain+path+"/wxCost_qrCode";
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + 
				"appid="+appID+"&" + 
				"redirect_uri=" + URLEncoder.encode(URL, "UTF-8") + 
				"&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
				response.setStatus(301);
				response.setHeader("Location",url);
				response.setHeader("Connection","close");
				return null;
			}else{
				//获取OPENID
				net.sf.json.JSONObject jsonMap = WeixinUtils.getOpenId(appID, appsecret, code);
//				System.out.println(jsonMap.toString());
				if(!jsonMap.containsKey("openid")){
					String URL = domain+path+"/wxCost_qrCode";
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
					String openid = jsonMap.getString("openid");//openid
//					System.out.println("openid:" + openid);
					
					Users u = usersService.loadByOpenid(openid);
					if(u == null){
						mv.setViewName("redirect:/wxUsersManage_toBind");
					}
					else{
						
						//获取停车场列表
					    List<Carpark> carparks = this.carparkService.getAll();
					    //获取车辆列表
						List<UsersCars> carList = usersCarsService.loadByMobile(u.getMobile());
						
						List<UsersCars> ucList = usersCarsService.loadByMobile(u.getMobile());
						String inunid = "";
						ParkPassVehicleMonth ppv = null;
						for(UsersCars uc : ucList){
							if(ppv == null){
								ppv = parkPassVehicleMonthService.retentionCar(uc.getCarPlate());
							}
						}
//						System.out.println(ppv == null);
//						System.out.println(ppv.getInUnid());
						//获取停车信息
						if(ppv != null){
							
							inunid = ppv.getInUnid();
//							System.out.println("inunid:" + inunid);
							//检测是否有已缴费未推送成功的记录
							//如果存在，重新推送一遍
//							String hql = "from VehicleCostWechat where inUnid = '" + inunid + "' and status = 1 and pushStatus = 0";
//							List<VehicleCostWechat> needPusthList = vehicleCostWechatService.getByHql(hql);
//							if(needPusthList.size() > 0){
//								push(inunid);
//							}
//							//再次检测，如果仍存在，停止请求账单，返回错误
//							needPusthList = vehicleCostWechatService.getByHql(hql);
//							if(needPusthList.size() > 0){
//								mv.addObject("errMsg", "停车费用获取失败");
//								mv.setViewName("weixin/tsp/toWxCost");	
//							}
//							else{
								//获取停车费用信息
								String parkCode = "";
								Carpark carpark = carparkService.get(ppv.getCarparkid());
								parkCode = carpark.getThirdId();
								String parkName = carpark.getName();
								
								String reqIp = Configuration.getInstance().getValue("hikip");
								String url = reqIp+"/GetVehicleCost";
								StringBuilder body = new StringBuilder();
								body.append("{");
								body.append("\"token\":\"\",");
								body.append("\"parkingCode\":\""+parkCode+"\",");
								body.append("\"plateInfo\":\""+ppv.getCarplate()+"\",");
								body.append("\"plateColor\":"+ppv.getPlatecolor()+",");
								body.append("\"carType\":"+ppv.getCartype()+",");
								body.append("\"inUnid\":\""+inunid+"\",");
								body.append("\"inTime\":\""+SDF.format(ppv.getPassTime())+"\"");
								body.append("}");
//								System.out.println(body.toString());
								//加密
								String enStr = AESOperator.getInstance().encrypt(body.toString());
								String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
//								System.out.println(reJson);
								if(reJson!=null){
									//解密停车费用信息
									JSONObject jsonObject = JSONObject.parseObject(reJson);
									String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
									System.out.println("停车费用信息：" + decrypt);
									VehicleOrder vo = JSONObject.parseObject(decrypt, VehicleOrder.class);
									if(vo.getErrorCode().equals(300000)){
										//删除未支付账单
										vehicleCostWechatService.deleteUnpayed(ppv.getInUnid());
										//获取已支付最近一次账单
										VehicleCostWechat lastPayed = vehicleCostWechatService.loadByInunid(vo.getInUnid());
										Date lastPayTime = lastPayed == null ? null : lastPayed.getPlanPayTime();
//										System.out.println("删除未支付账单！！");
										//根据payUnid检查是否已经存储
										VehicleCostWechat newvcw = vehicleCostWechatService.loadByPayUnid(vo.getPayUnid());
										if(newvcw == null){
											VehicleCostWechat vcw = new VehicleCostWechat();
											vcw.setCarparkid(ppv.getCarparkid());
											vcw.setCarPlate(ppv.getCarplate());
											vcw.setInTime(ppv.getPassTime());
											vcw.setInUnid(ppv.getInUnid());
//											vcw.setLastPayTime(vo.formatLastPayTime());
											vcw.setLastPayTime(lastPayTime);
											vcw.setPayUnid(vo.getPayUnid());
											vcw.setPlanPayTime(vo.formatPlanPayTime());
											vcw.setShouldPayMoney(vo.getShouldPayMoney());
											vcw.setTerminCode(vo.getTerminCode());
											vcw.setStatus(0);
											vcw.setPushStatus(0);
											newvcw = vehicleCostWechatService.add(vcw);
										}
										else{
											newvcw.setLastPayTime(lastPayTime);
											vehicleCostWechatService.update(newvcw);
										}
										//根据inUnid获取账单记录，计算应缴，已缴，本次需缴
										List<VehicleCostWechat> vcwList = vehicleCostWechatService.loadListWithInUnid(vo.getInUnid());
										
										Integer shouldPay, hasPayed, needPay;
										shouldPay = hasPayed = needPay = 0;
										for(VehicleCostWechat vcw1 : vcwList){
											shouldPay += vcw1.getShouldPayMoney();
											if(vcw1.getStatus() == 1){
												hasPayed += vcw1.getShouldPayMoney();
											}
											else{
												needPay += vcw1.getShouldPayMoney();
											}
										}
										
										mv.addObject("vcw", newvcw);
										mv.addObject("shouldPay", formatMoney(shouldPay));
										mv.addObject("hasPayed", formatMoney(hasPayed));
										mv.addObject("needPay", formatMoney(needPay));
										mv.addObject("parkId", ppv.getCarparkid());
										if(needPay>20000){
											mv.addObject("errMsg", "停车费用获取失败");
											mv.setViewName("weixin/tsp/toWxCost");	
										}else{
											mv.setViewName("weixin/tsp/toWxCost");	
										}
//									    mv.addObject("mobile", user.getMobile());
									}else{
										mv.addObject("errMsg", "停车费用获取失败");
										mv.setViewName("weixin/tsp/toWxCost");								
									}
								}else{
									mv.addObject("errMsg", "停车费用获取失败");
									mv.setViewName("weixin/tsp/toWxCost");				
								}
								mv.addObject("parkName", parkName);
								mv.addObject("ppv", ppv);
							}
							
//						}
						else{
							mv.addObject("errMsg", "绑定车辆不在停车场内");
							mv.setViewName("weixin/tsp/toWxCost");
						}
						mv.addObject("hikParking", carparks);
						mv.addObject("users", u);
						mv.addObject("carList", carList);
					}
				}
				
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mv.addObject("errMsg", "系统错误");
			mv.setViewName("weixin/tsp/toWxCost");
		}
		return mv;
	}
	
	/**
	 * 缴费成功跳转
	 * @param kid
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("wxCost_paySuccess")
	public ModelAndView paySuccess(Integer kid,HttpServletRequest request,HttpServletResponse response) throws Exception{
		System.out.println("执行wxCost_paySuccess");
		ModelAndView mv = new ModelAndView();
		VehicleCostWechat vcw = vehicleCostWechatService.get(kid);
		try {
			// 向下级客户端推送
			Map<String,String> queryMap = new HashMap<String, String>();
			String nonce_str_query = WXPayUtil.generateNonceStr();
			queryMap.put("appid", APPID);
			queryMap.put("mch_id", mchID);
			queryMap.put("nonce_str", nonce_str_query);
			queryMap.put("out_trade_no", String.valueOf(kid));
			String queryXml = WXPayUtil.generateSignedXml(queryMap,key);
			WXPayConfigImpl config = WXPayConfigImpl.getInstance();
		    WXPayRequest wxPayRequest = new WXPayRequest(config);
		    String queXml = wxPayRequest.requestWithCert("/pay/orderquery", nonce_str_query, queryXml, true);
		    System.out.println("支付查询结果："+queXml);
		    Map<String, String> queMap = WXPayUtil.xmlToMap(queXml);
		    if(queMap.get("return_code").equals("SUCCESS")){
		    	if(queMap.get("result_code").equals("SUCCESS")){
		    		if(vcw==null){
		    			mv.setViewName("weixin/tsp/payFailed");
		    		}else{
		    			if(vcw.getStatus()!=null && vcw.getPushStatus()!=null && vcw.getStatus().equals(1)&& vcw.getPushStatus().equals(1)){
		    				Carpark carpark = this.carparkService.get(vcw.getCarparkid());
		    				String parkName = carpark.getName();
		    				mv.addObject("vcw", vcw);
		    				mv.addObject("parkName", parkName);
		    				mv.setViewName("weixin/tsp/paySuccess");
		    			}else if((vcw.getPushStatus()==null || vcw.getPushStatus().equals(0)) && (vcw.getStatus().equals(0) || vcw.getStatus().equals(1))){
		    				Carpark carpark = this.carparkService.get(vcw.getCarparkid());
		    				String parkName = carpark.getName();
		    				mv.addObject("vcw", vcw);
		    				mv.addObject("kid", kid);
		    				mv.addObject("parkName", parkName);
		    				mv.setViewName("weixin/tsp/load");
		    			}else{
		    				mv.setViewName("weixin/tsp/payFailed");
		    			}
		    		}
		    	}else{
					mv.setViewName("weixin/tsp/payFailed");
		    	}
		    }else{
				mv.setViewName("weixin/tsp/payFailed");
		    }
		} catch (Exception e) {
			e.printStackTrace();
			mv.setViewName("weixin/tsp/payFailed");
		}
		return mv;
	}
	
	/**
	 * 缴费成功跳转
	 * @param kid
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("wxCost_payStatus")
	public void payStatus(Integer kid,HttpServletRequest request,HttpServletResponse response) throws Exception{
		VehicleCostWechat vcw = vehicleCostWechatService.get(kid);
		PrintWriter out = response.getWriter();
		try {
			if(vcw==null){
    			out.print("{\"status\",false:\"errorCode\":-1,\"errorMsg\":\"支付失败\"}");
    		}else{
    			if(vcw.getStatus()!=null && vcw.getPushStatus()!=null && vcw.getStatus().equals(1)&& vcw.getPushStatus().equals(1)){
        			out.print("{\"status\":true,\"errorCode\":0,\"errorMsg\":\"支付成功\"}");
    			}else if((vcw.getPushStatus()==null || vcw.getPushStatus().equals(0)) && (vcw.getStatus().equals(0) || vcw.getStatus().equals(1))){
        			out.print("{\"status\":true,\"errorCode\":1,\"errorMsg\":\"支付结果获取中\"}");
    			}else{
        			out.print("{\"status\":false,\"errorCode\":-1,\"errorMsg\":\"支付失败\"}");
    			}
    		}
		} catch (Exception e) {
			out.print("{\"status\":true,\"errorCode\":0,\"errorMsg\":\"支付失败\"}");
			e.printStackTrace();
			
		}finally{
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 缴费成功跳转
	 * @param kid
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("wxCost_wxPaySuccess")
	public void wxPaySuccess(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		System.out.println("执行wxCost_wxPaySuccess");
		try {
			ServletInputStream is = request.getInputStream();
			StringBuilder sb = new StringBuilder();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			String requ = sb.toString();
			Map<String, String> paySuRe = WXPayUtil.xmlToMap(requ);
			if (paySuRe.get("return_code").equals("SUCCESS")) {
				if (paySuRe.get("result_code").equals("SUCCESS")) {
					System.out.println("支付成功");
					String kid = paySuRe.get("out_trade_no");
					VehicleCostWechat vcw = vehicleCostWechatService
							.get(Integer.valueOf(kid));
					if (vcw == null) {
						// 退款
						Map<String, String> refundMap = new HashMap<String, String>();
						String nonce_str = WXPayUtil.generateNonceStr();
						String out_refund_no = WXPayUtil.generateUUID();
						refundMap.put("appid", APPID);
						refundMap.put("mch_id", mchID);
						refundMap.put("nonce_str", nonce_str);
						refundMap.put("out_trade_no", String.valueOf(kid));
						refundMap.put("out_refund_no", out_refund_no);
						refundMap.put("total_fee", paySuRe.get("total_fee")
								+ "");
						refundMap.put("refund_fee", paySuRe.get("total_fee")
								+ "");
						refundMap.put("refund_desc", "停车场网络中断");
						String refundXml = WXPayUtil.generateSignedXml(
								refundMap, key);
						WXPayConfigImpl config = WXPayConfigImpl.getInstance();
						WXPayRequest wxPayRequest = new WXPayRequest(config);
						String refXml = wxPayRequest.requestWithCert(
								"/secapi/pay/refund", nonce_str, refundXml,
								true);
						Map<String, String> refMap = WXPayUtil.xmlToMap(refXml);
						if (refMap.get("return_code").equals("SUCCESS")) {
							if (refMap.get("result_code").equals("SUCCESS")) {
								System.out.println("退款申请成功");
							}
						}
						Map<String, String> returnMap = new HashMap<String, String>();
						returnMap.put("return_code", "SUCCESS");
						returnMap.put("return_msg", "OK");
						String returnXml = WXPayUtil.mapToXml(returnMap);
						out.print(returnXml);
					} else {
						if(vcw.getStatus().equals(1) && vcw.getPushStatus().equals(1)){
							Map<String, String> returnMap = new HashMap<String, String>();
							returnMap.put("return_code", "SUCCESS");
							returnMap.put("return_msg", "OK");
							String returnXml = WXPayUtil.mapToXml(returnMap);
							out.print(returnXml);
						}else{
							if (vcw.getStatus() == null
									|| vcw.getStatus().equals(0)) {
								vcw.setStatus(1);
								vcw.setPayTime(new Date());
								vehicleCostWechatService.update(vcw);
							}
							// 向下级客户端推送
							if (vcw.getPushStatus() == null|| vcw.getPushStatus().equals(0)) {
								ParkPassVehicleMonth ppv = parkPassVehicleMonthService.loadByInunid(vcw.getInUnid());
								String parkCode = "";
								Carpark carpark = this.carparkService.get(vcw.getCarparkid());
			    				String parkName = carpark.getName();
								parkCode = carpark.getThirdId();

								String reqIp = Configuration.getInstance()
										.getValue("hikip");
								String url = reqIp + "/PushPayCostStatus";
								StringBuilder body = new StringBuilder();
								body.append("{");
								body.append("\"token\":\"\",");
								body.append("\"terminCode\":\""
										+ vcw.getTerminCode() + "\",");
								body
										.append("\"parkingCode\":\"" + parkCode
												+ "\",");
								body.append("\"plateInfo\":\"" + vcw.getCarPlate()
										+ "\",");
								body.append("\"plateColor\":" + ppv.getPlatecolor()
										+ ",");
								body.append("\"payUnid\":\"" + vcw.getPayUnid()
										+ "\",");
								body.append("\"parkType\":" + ppv.getParktype()
										+ ",");
								body.append("\"collectorName\":\"微信支付\",");
								body.append("\"inTime\":\""
										+ SDF.format(ppv.getPassTime()) + "\",");
								body.append("\"payTime\":\""
										+ SDF.format(vcw.getPlanPayTime()) + "\",");
								body.append("\"inUnid\":\"" + vcw.getInUnid()
										+ "\",");
								body.append("\"startTime\":\""
										+ (vcw.getLastPayTime() == null ? SDF
												.format(vcw.getInTime()) : SDF
												.format(vcw.getLastPayTime()))
										+ "\",");
								body.append("\"endTime\":\""
										+ SDF.format(vcw.getPlanPayTime()) + "\",");
								body.append("\"payType\":3,");
								body.append("\"payStatus\":" + vcw.getStatus()
										+ ",");
								body.append("\"payMoney\":"
										+ vcw.getShouldPayMoney() + "");
								body.append("}");
								// 加密
								String enStr = AESOperator.getInstance().encrypt(
										body.toString());
								// System.out.println(body.toString());
								String reJson = HttpUtils.getInstance()
										.requestPostJson(url,
												"{\"package\":\"" + enStr + "\"}",
												20);
								if (reJson != null) {
									JSONObject jsonObject = JSONObject.parseObject(reJson);
									String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
									System.out.println(decrypt);
									jsonObject = JSONObject.parseObject(decrypt);
									if (jsonObject.getInteger("errorCode") == 300000) {
										vcw.setPushStatus(1);
										vcw.setLeaveOut(jsonObject.getInteger("freeTime"));
										vehicleCostWechatService.update(vcw);
										WeixinConfig config = this.weixinConfigService.nameToConfig("hik");
										if (config!=null) {
											String token = config.getAccessToken();
											List<UsersCars> carList = usersCarsService
											.getByHql("from UsersCars where carPlate = '"
													+ vcw.getCarPlate() + "'");
											String mobiles = "";
											for (UsersCars uc : carList) {
												mobiles += "'" + uc.getMobile() + "',";
											}
											List<Users> userList = new ArrayList<Users>();
											if (mobiles.length() > 0) {
												mobiles = mobiles.substring(0,mobiles.length() - 1);
												userList = usersService.getByHql("from Users where mobile in ("+ mobiles+ ") and openid is not null");
											}
											for (Users u : userList) {
												String sendGet = WeixinUtils.sendPaymentNotice(u.getOpenid(), token, vcw.getCarPlate(),vcw.formatPayTime(),vcw.formatInTime(), vcw.formatParkTime(),vcw.formateShouldPayMoney());
												JSONObject jsonOb = JSONObject.parseObject(sendGet);
												if (jsonOb.getInteger("errcode") == 42001) {
													net.sf.json.JSONObject tokenObject = WeixinUtils.getToken();
													token = tokenObject.getString("access_token");
													net.sf.json.JSONObject ticketObject = WeixinUtils.getTicket(token);
													String jsapiTicket = ticketObject.getString("ticket");
													config.setAccessToken(token);
													config.setJsapiTicket(jsapiTicket);
													WeixinUtils.sendPaymentNotice(u.getOpenid(), token, vcw.getCarPlate(), vcw.formatPayTime(), vcw.formatInTime(), vcw.formatParkTime(), vcw.formateShouldPayMoney());
												}
											}
										}
										System.out.println("111");
										Map<String, String> returnMap = new HashMap<String, String>();
										returnMap.put("return_code", "SUCCESS");
										returnMap.put("return_msg", "OK");
										String returnXml = WXPayUtil.mapToXml(returnMap);
										out.print(returnXml);
									}else if(jsonObject.getInteger("errorCode") == 300033){
										Map<String, String> returnMap = new HashMap<String, String>();
										returnMap.put("return_code", "SUCCESS");
										returnMap.put("return_msg", "OK");
										String returnXml = WXPayUtil.mapToXml(returnMap);
										out.print(returnXml);
									}else{
										// 退款处理
										System.out.println("3333");
										Map<String,String> refundMap = new HashMap<String, String>();
										String nonce_str = WXPayUtil.generateNonceStr();
										String out_refund_no = WXPayUtil.generateUUID();
										refundMap.put("appid", APPID);
										refundMap.put("mch_id", mchID);
										refundMap.put("nonce_str", nonce_str);
										refundMap.put("out_trade_no", String.valueOf(kid));
										refundMap.put("out_refund_no", out_refund_no);
										refundMap.put("total_fee", vcw.getShouldPayMoney()+"");
										refundMap.put("refund_fee",vcw.getShouldPayMoney()+"");
										refundMap.put("refund_desc", "停车场网络中断");
										String refundXml = WXPayUtil.generateSignedXml(refundMap,key);
										WXPayConfigImpl config = WXPayConfigImpl.getInstance();
										WXPayRequest wxPayRequest = new WXPayRequest(config);
										String refXml = wxPayRequest.requestWithCert("/secapi/pay/refund", nonce_str, refundXml, true);
										Map<String, String> refMap = WXPayUtil.xmlToMap(refXml);
										if(refMap.get("return_code").equals("SUCCESS")){
											if(refMap.get("result_code").equals("SUCCESS")){
												System.out.println("退款申请成功");
												vcw.setStatus(-1);
											}
										}
										vcw.setPushStatus(-1);
										vehicleCostWechatService.update(vcw);
										Map<String, String> returnMap = new HashMap<String, String>();
										returnMap.put("return_code", "SUCCESS");
										returnMap.put("return_msg", "OK");
										String returnXml = WXPayUtil.mapToXml(returnMap);
										out.print(returnXml);
									}
								} else {
									// 退款处理
									Map<String, String> refundMap = new HashMap<String, String>();
									String nonce_str = WXPayUtil.generateNonceStr();
									String out_refund_no = WXPayUtil.generateUUID();
									refundMap.put("appid", APPID);
									refundMap.put("mch_id", mchID);
									refundMap.put("nonce_str", nonce_str);
									refundMap.put("transaction_id", paySuRe
											.get("transaction_id"));
									refundMap.put("out_refund_no", out_refund_no);
									refundMap.put("total_fee", paySuRe
											.get("cash_fee"));
									refundMap.put("refund_fee", paySuRe
											.get("cash_fee"));
									refundMap.put("refund_desc", "停车场网络中断");
									String refundXml = WXPayUtil.generateSignedXml(
											refundMap, key);
									WXPayConfigImpl config = WXPayConfigImpl
											.getInstance();
									WXPayRequest wxPayRequest = new WXPayRequest(
											config);
									String refXml = wxPayRequest.requestWithCert(
											"/secapi/pay/refund", nonce_str,
											refundXml, true);
									Map<String, String> refMap = WXPayUtil
											.xmlToMap(refXml);
									if (refMap.get("return_code").equals("SUCCESS")) {
										if (refMap.get("result_code").equals(
												"SUCCESS")) {
											System.out.println("退款申请成功");
											vcw.setStatus(-1);
										}
									}
									vcw.setPushStatus(-1);
									vehicleCostWechatService.update(vcw);
									Map<String, String> returnMap = new HashMap<String, String>();
									returnMap.put("return_code", "SUCCESS");
									returnMap.put("return_msg", "OK");
									String returnXml = WXPayUtil
											.mapToXml(returnMap);
									out.print(returnXml);
								}
							} else {
								// 推送失败
								Map<String, String> refundMap = new HashMap<String, String>();
								String nonce_str = WXPayUtil.generateNonceStr();
								String out_refund_no = WXPayUtil.generateUUID();
								refundMap.put("appid", APPID);
								refundMap.put("mch_id", mchID);
								refundMap.put("nonce_str", nonce_str);
								refundMap.put("transaction_id", paySuRe
										.get("transaction_id"));
								refundMap.put("out_refund_no", out_refund_no);
								refundMap.put("total_fee", paySuRe.get("cash_fee"));
								refundMap
										.put("refund_fee", paySuRe.get("cash_fee"));
								refundMap.put("refund_desc", "停车场网络中断");
								String refundXml = WXPayUtil.generateSignedXml(
										refundMap, key);
								WXPayConfigImpl config = WXPayConfigImpl
										.getInstance();
								WXPayRequest wxPayRequest = new WXPayRequest(config);
								String refXml = wxPayRequest.requestWithCert(
										"/secapi/pay/refund", nonce_str, refundXml,
										true);
								Map<String, String> refMap = WXPayUtil
										.xmlToMap(refXml);
								if (refMap.get("return_code").equals("SUCCESS")) {
									if (refMap.get("result_code").equals("SUCCESS")) {
										System.out.println("退款申请成功");
										vcw.setStatus(-1);
									}
								}
								vcw.setPushStatus(-1);
								vehicleCostWechatService.update(vcw);
								Map<String, String> returnMap = new HashMap<String, String>();
								returnMap.put("return_code", "SUCCESS");
								returnMap.put("return_msg", "OK");
								String returnXml = WXPayUtil.mapToXml(returnMap);
								out.print(returnXml);
							}
						}
						
					}
				} else {
					// 支付失败
					Map<String, String> returnMap = new HashMap<String, String>();
					returnMap.put("return_code", "SUCCESS");
					returnMap.put("return_msg", "OK");
					String returnXml = WXPayUtil.mapToXml(returnMap);
					out.print(returnXml);
				}
			}else{
				// 支付失败
				Map<String, String> returnMap = new HashMap<String, String>();
				returnMap.put("return_code", "SUCCESS");
				returnMap.put("return_msg", "OK");
				String returnXml = WXPayUtil.mapToXml(returnMap);
				out.print(returnXml);
			}
		} catch (Exception e) {
			// 支付失败
			e.printStackTrace();
			Map<String, String> returnMap = new HashMap<String, String>();
			returnMap.put("return_code", "SUCCESS");
			returnMap.put("return_msg", "OK");
			String returnXml = WXPayUtil.mapToXml(returnMap);
			out.print(returnXml);
		}
}
	
	/**
	 * 缴费失败跳转
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("wxCost_payFailed")
	public ModelAndView payFailed(HttpServletRequest request,HttpServletResponse response) throws Exception{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("weixin/tsp/payFailed");
		return mv;
	}
	
	/**
	 * 用户切换停车场、车牌号码后
	 * ajax请求费用
	 * @param carparkid
	 * @param carPlate
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("wxCost_ajaxCountPay")
	public String ajaxCountPay(Long carparkid, String carPlate, HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			System.out.println(carPlate);
			ParkPassVehicleMonth ppv = parkPassVehicleMonthService.retentionCar(carPlate);
			if(ppv != null && ppv.getCarparkid().equals(carparkid)){
				//检测是否有已缴费未推送成功的记录
				//如果存在，重新推送一遍
//				String hql = "from VehicleCostWechat where inUnid = '" + ppv.getInUnid() + "' and status = 1 and pushStatus = 0";
//				List<VehicleCostWechat> needPusthList = vehicleCostWechatService.getByHql(hql);
//				if(needPusthList.size() > 0){
//					push(ppv.getInUnid());
//				}
//				//再次检测，如果仍存在，停止请求账单，返回错误
//				needPusthList = vehicleCostWechatService.getByHql(hql);
//				if(needPusthList.size() > 0){
//					out.print("{\"status\":\"false\",\"errorcode\":\"11\",\"errMsg\":\"获取停车费用失败\"}");
//				}
//				else{
					//获取停车费用信息
				String parkCode = "";
				Carpark carpark = this.carparkService.get(ppv.getCarparkid());
				parkCode = carpark.getThirdId();
				
				String reqIp = Configuration.getInstance().getValue("hikip");
				String url = reqIp+"/GetVehicleCost";
				StringBuilder body = new StringBuilder();
				body.append("{");
				body.append("\"token\":\"\",");
				body.append("\"parkingCode\":\""+parkCode+"\",");
				body.append("\"plateInfo\":\""+ppv.getCarplate()+"\",");
				body.append("\"plateColor\":"+ppv.getPlatecolor()+",");
				body.append("\"carType\":"+ppv.getCartype()+",");
				body.append("\"inUnid\":\""+ppv.getInUnid()+"\",");
				body.append("\"inTime\":\""+SDF.format(ppv.getPassTime())+"\"");
				body.append("}");
//					System.out.println(body.toString());
				//加密
				String enStr = AESOperator.getInstance().encrypt(body.toString());
				String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
//					System.out.println(reJson);
				if(reJson!=null){
					//解密停车费用信息
					JSONObject jsonObject = JSONObject.parseObject(reJson);
					String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
					System.out.println("停车费用信息：" + decrypt);
					VehicleOrder vo = JSONObject.parseObject(decrypt, VehicleOrder.class);
					if(vo.getErrorCode().equals(300000)){
						//删除未支付账单
						vehicleCostWechatService.deleteUnpayed(vo.getInUnid());
						//获取已支付最近一次账单
						VehicleCostWechat lastPayed = vehicleCostWechatService.loadByInunid(vo.getInUnid());
						Date lastPayTime = lastPayed == null ? vo.formatInTime() : lastPayed.getPlanPayTime();
//							System.out.println("删除未支付账单！！");
						//根据payUnid检查是否已经存储
						VehicleCostWechat newvcw = vehicleCostWechatService.loadByPayUnid(vo.getPayUnid());
						if(newvcw == null){
							VehicleCostWechat vcw = new VehicleCostWechat();
							vcw.setCarparkid(ppv.getCarparkid());
							vcw.setCarPlate(ppv.getCarplate());
							vcw.setInTime(ppv.getPassTime());
							vcw.setInUnid(ppv.getInUnid());
//								vcw.setLastPayTime(vo.formatLastPayTime());
							vcw.setLastPayTime(lastPayTime);
							vcw.setPayUnid(vo.getPayUnid());
							vcw.setPlanPayTime(vo.formatPlanPayTime());
							vcw.setShouldPayMoney(vo.getShouldPayMoney());
							vcw.setTerminCode(vo.getTerminCode());
							vcw.setStatus(0);
							newvcw = vehicleCostWechatService.add(vcw);
						}
						else{
							newvcw.setLastPayTime(lastPayTime);
							vehicleCostWechatService.update(newvcw);
						}
						//根据inUnid获取账单记录，计算应缴，已缴，本次需缴
						List<VehicleCostWechat> vcwList = vehicleCostWechatService.loadListWithInUnid(vo.getInUnid());
						
						Integer shouldPay, hasPayed, needPay;
						shouldPay = hasPayed = needPay = 0;
						for(VehicleCostWechat vcw1 : vcwList){
							shouldPay += vcw1.getShouldPayMoney();
							if(vcw1.getStatus() == 1){
								hasPayed += vcw1.getShouldPayMoney();
							}
							else{
								needPay += vcw1.getShouldPayMoney();
							}
						}
						out.print("{\"status\":\"success\",\"errorcode\":\"00\",\"kid\":\""
								+ newvcw.getKid() +"\",\"inTime\":\""
								+ newvcw.formatInTime() +"\",\"parkTime\":\""
								+ newvcw.formatParkTime() +"\",\"shouldPay\":\""
								+ formatMoney(shouldPay) +"\",\"hasPay\":\""
								+ formatMoney(hasPayed) +"\",\"needPay\":\""
								+ formatMoney(needPay) +"\"}");
					}
					else{
						out.print("{\"status\":\"false\",\"errorcode\":\"11\",\"errMsg\":\"获取停车费用失败\"}");
					}
				}
				else{
					out.print("{\"status\":\"false\",\"errorcode\":\"11\",\"errMsg\":\"获取停车费用失败\"}");
				}
			}
				
//			}
			else{
				out.print("{\"status\":\"false\",\"errorcode\":\"11\",\"errMsg\":\"车辆不在该停车场内\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorcode\":\"11\",\"errMsg\":\"系统错误\"}");
		}
		
		out.flush();
		out.close();
		return null;
	}
	
	/**
	 * 根据入场ID
	 * 推送已缴费但未推送成功的缴费记录
	 * @param inunid
	 * @throws Exception
	 */
	public void push(String inunid) throws Exception{
		String hql = "from VehicleCostWechat where inUnid = '" + inunid + "' and status = 1 and pushStatus = 0";
		List<VehicleCostWechat> needPusthList = vehicleCostWechatService.getByHql(hql);
		for(VehicleCostWechat vcw : needPusthList){
			//向下级客户端推送
			ParkPassVehicleMonth ppv = parkPassVehicleMonthService.loadByInunid(vcw.getInUnid());
			String parkCode = "";
			Carpark carpark = this.carparkService.get(vcw.getCarparkid());
			parkCode = carpark.getThirdId();
			
			String reqIp = Configuration.getInstance().getValue("hikip");
			String url = reqIp+"/PushPayCostStatus";
			StringBuilder body = new StringBuilder();
			body.append("{");
			body.append("\"token\":\"\",");
			body.append("\"terminCode\":\""+vcw.getTerminCode()+"\",");
			body.append("\"parkingCode\":\""+parkCode+"\",");
			body.append("\"plateInfo\":\""+vcw.getCarPlate()+"\",");
			body.append("\"plateColor\":"+ppv.getPlatecolor()+",");
			body.append("\"payUnid\":\""+vcw.getPayUnid()+"\",");
			body.append("\"parkType\":"+ppv.getParktype()+",");
			body.append("\"collectorName\":\"微信支付\",");
			body.append("\"inTime\":\""+SDF.format(ppv.getPassTime())+"\",");
			body.append("\"payTime\":\""+SDF.format(vcw.getPlanPayTime())+"\",");
			body.append("\"inUnid\":\""+vcw.getInUnid()+"\",");
			body.append("\"startTime\":\""+(vcw.getLastPayTime() == null ? SDF.format(vcw.getInTime()) : SDF.format(vcw.getLastPayTime()))+"\",");
			body.append("\"endTime\":\""+SDF.format(vcw.getPlanPayTime())+"\",");
			body.append("\"payType\":3,");
			body.append("\"payStatus\":" + vcw.getStatus() + ",");
			body.append("\"payMoney\":"+vcw.getShouldPayMoney()+"");
			body.append("}");
			//加密
			String enStr = AESOperator.getInstance().encrypt(body.toString());
//			System.out.println(body.toString());
			String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
			if(reJson!=null){
				JSONObject jsonObject = JSONObject.parseObject(reJson);
				String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
//				System.out.println(decrypt);
				jsonObject = JSONObject.parseObject(decrypt);
				if(jsonObject.getInteger("errorCode")==300000){
					vcw.setPushStatus(1);
					vcw.setLeaveOut(jsonObject.getInteger("freeTime"));
					vehicleCostWechatService.update(vcw);
				}
			}
		}
	}
	
	/**
	 * 构建微信公众号支付对象
	 * @param inunid
	 * @param carNumber
	 * @param openid
	 * @param amount
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
//	@RequestMapping("wxCost_temPay")
//	public String temPay(String inunid, String carNumber, String openid, Long amount, Integer kid, HttpServletRequest request,HttpServletResponse response) throws IOException
//	{
//		response.setContentType("text/html;charset=utf-8");
//		PrintWriter out = response.getWriter();
//		try {
//			out = response.getWriter();
//			VehicleCostWechat vcw = vehicleCostWechatService.get(kid);
//			if(vcw == null){
//				out.print("{\"status\":\"false\",\"errorCode\":\"-1\",\"errorMsg\":\"账单过期，请刷新重试\"}");
//			}
//			else{
//				String ipAddress = IpUtils.getInstance().getIp(request);
//				Map<String, String> extra = new HashMap<String, String>();
//				extra.put("open_id", openid);
//				Charge charge = ChargeUtils.getInstance().getWeixinReCharge(amount , "酒城停车临停缴费", "支付车牌号为"+carNumber+"临停费用", String.valueOf(new Date().getTime()) , "wx_pub", "cny", ipAddress, extra,  System.currentTimeMillis()/1000 + 120, "tem");
//				out.print("{\"status\":\"true\",\"errorCode\":\"0\",\"errorMsg\":\"\",\"charge\":"+charge+"}");
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			out.print("{\"status\":\"false\",\"errorCode\":\"-1\",\"errorMsg\":\"支付失败\"}");
//		}finally{
//			out.flush();
//			out.close();
//		}
//		return null;
//	}
	@RequestMapping("wxCost_temPay")
	public String temPay(String inunid, String carNumber, String openid, Long amount, Integer kid, HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String domain = Configuration.getInstance().getValue("domainName");
		String path = request.getContextPath();
		try {
			out = response.getWriter();
			VehicleCostWechat vcw = vehicleCostWechatService.get(kid);
			if(vcw == null){
				out.print("{\"status\":\"false\",\"errorCode\":\"-1\",\"errorMsg\":\"账单过期，请刷新重试\"}");
			}
			else{
				Date now = new Date();
				Map<String,String> data = new HashMap<String, String>();
				String nonce_str = UUID.randomUUID().toString().replaceAll("-","");
				data.put("appid", APPID);
				data.put("mch_id", mchID);
				//data.put("sub_mch_id", subMchID);
				data.put("device_info","WEB");
				data.put("nonce_str", nonce_str);
				data.put("sign_type", "MD5");
				data.put("body", "酒城停车-临停缴费");
				data.put("out_trade_no", String.valueOf(vcw.getKid()));
				data.put("fee_type", "CNY");
				data.put("time_start", this.dateFormat(now));
				data.put("time_expire", this.dateFormat(new Date(now.getTime()+(2*60*1000))));
				data.put("total_fee", String.valueOf(vcw.getShouldPayMoney()));
				data.put("spbill_create_ip", this.getIpAddr(request));
				data.put("notify_url", domain+path+"/wxCost_wxPaySuccess/");
				data.put("trade_type", "JSAPI");
				data.put("limit_pay", "no_credit");
				data.put("openid", openid);
				String generateSignedXml = WXPayUtil.generateSignedXml(data,key);
				System.out.println(generateSignedXml);
				WXPayConfigImpl config = WXPayConfigImpl.getInstance();
			    WXPayRequest wxPayRequest = new WXPayRequest(config);
			    System.out.println("1");
			    String resultXml = wxPayRequest.requestWithCert("/pay/unifiedorder", nonce_str, generateSignedXml, true);
			    System.out.println("2");
			    System.out.println(resultXml);
			    Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXml);
			    if(resultMap.get("return_code").equals("SUCCESS")){
			    	String resultCode = resultMap.get("result_code");
			    	if(resultCode.equals("SUCCESS")){
			    		//计算jsAPI签名
			    		Map<String,String> jsAPIData = new HashMap<String, String>();
			    		String nonceStr = WXPayUtil.generateNonceStr();
			    		long timeStamp = WXPayUtil.getCurrentTimestamp();
			    		jsAPIData.put("appId", resultMap.get("appid"));
			    		jsAPIData.put("timeStamp", String.valueOf(timeStamp));
			    		jsAPIData.put("nonceStr", nonceStr);
			    		jsAPIData.put("package", "prepay_id="+resultMap.get("prepay_id"));
			    		jsAPIData.put("signType", "MD5");
			    		String jsAPISign = WXPayUtil.generateSignature(jsAPIData, key);
			    		out.print("{\"status\":\"true\",\"errorCode\":\"0\",\"errorMsg\":\"\"," +
			    				"\"appId\":\""+resultMap.get("appid")+"\"," +
			    				"\"timeStamp\":\""+timeStamp+"\"," +
			    				"\"nonceStr\":\""+nonceStr+"\"," +
			    				"\"package\":\""+"prepay_id="+resultMap.get("prepay_id")+"\"," +
			    				"\"signType\":\"MD5\"," +
			    				"\"paySign\":\""+jsAPISign+"\"}");
			    	}else{
						out.print("{\"status\":\"false\",\"errorCode\":\""+resultMap.get("err_code")+"\",\"errorMsg\":\""+resultMap.get("err_code_des")+"\"}");
			    	}
			    }else{
					out.print("{\"status\":\"false\",\"errorCode\":\"-1\",\"errorMsg\":\""+resultMap.get("return_msg")+"\"}");
			    }
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorCode\":\"-1\",\"errorMsg\":\"支付失败\"}");
		}finally{
			out.flush();
			out.close();
		}
		return null;
	}
	
	
	
	
	/**
	 * 海康测试
	 * @param inunid
	 * @param openid
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("Hik_test")
	public String Hik_test(String inunid, String openid,HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
				//获取停车信息
				ParkPassVehicleMonth ppv = parkPassVehicleMonthService.loadByInunid(inunid);
//				System.out.println(inunid + "--" + ppv.getCarparkid());
				ParkPassVehicleMonth isIn = parkPassVehicleMonthService.retentionCar(ppv.getCarplate());
				if(isIn != null){
					//检测是否有已缴费未推送成功的记录
					//如果存在，重新推送一遍
					String hql = "from VehicleCostWechat where inUnid = '" + inunid + "' and status = 1 and pushStatus = 0";
					List<VehicleCostWechat> needPusthList = vehicleCostWechatService.getByHql(hql);
					if(needPusthList.size() > 0){
						push(inunid);
					}
					//再次检测，如果仍存在，停止请求账单，返回错误
					needPusthList = vehicleCostWechatService.getByHql(hql);
					if(needPusthList.size() > 0){
						out.print("{\"status\":\"false\",\"errorCode\":\"-1\",\"errorMsg\":\"上次缴费记录推送失败\"}");
					}
					else{
						//获取停车费用信息
						String parkCode = "";
						Carpark carpark = this.carparkService.get(ppv.getCarparkid());
//						System.out.println(jsonOb.toString());
						parkCode = carpark.getThirdId();
						String parkName = carpark.getName();
						
						String reqIp = Configuration.getInstance().getValue("hikip");
						String url = reqIp+"/GetVehicleCost";
						StringBuilder body = new StringBuilder();
						body.append("{");
						body.append("\"token\":\"\",");
						body.append("\"parkingCode\":\""+parkCode+"\",");
						body.append("\"plateInfo\":\""+ppv.getCarplate()+"\",");
						body.append("\"plateColor\":"+ppv.getPlatecolor()+",");
						body.append("\"carType\":"+ppv.getCartype()+",");
						body.append("\"inUnid\":\""+inunid+"\",");
						body.append("\"inTime\":\""+SDF.format(ppv.getPassTime())+"\"");
						body.append("}");
//						System.out.println(body.toString());
						//加密
						String enStr = AESOperator.getInstance().encrypt(body.toString());
						String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
//						System.out.println(reJson);
						if(reJson!=null){
							//解密停车费用信息
							JSONObject jsonObject = JSONObject.parseObject(reJson);
							String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
//							System.out.println("停车费用信息：" + decrypt);
							VehicleOrder vo = JSONObject.parseObject(decrypt, VehicleOrder.class);
							if(vo.getErrorCode().equals(300000)){
								//删除未支付账单
								vehicleCostWechatService.deleteUnpayed(vo.getInUnid());
								//获取已支付最近一次账单
								VehicleCostWechat lastPayed = vehicleCostWechatService.loadByInunid(vo.getInUnid());
								Date lastPayTime = lastPayed == null ? null : lastPayed.getPlanPayTime();
//								System.out.println("删除未支付账单！！");
								//根据payUnid检查是否已经存储
								VehicleCostWechat newvcw = vehicleCostWechatService.loadByPayUnid(vo.getPayUnid());
								if(newvcw == null){
									VehicleCostWechat vcw = new VehicleCostWechat();
									vcw.setCarparkid(ppv.getCarparkid());
									vcw.setCarPlate(ppv.getCarplate());
									vcw.setInTime(ppv.getPassTime());
									vcw.setInUnid(ppv.getInUnid());
//									vcw.setLastPayTime(vo.formatLastPayTime());
									vcw.setLastPayTime(lastPayTime);
									vcw.setPayUnid(vo.getPayUnid());
									vcw.setPlanPayTime(vo.formatPlanPayTime());
									vcw.setShouldPayMoney(vo.getShouldPayMoney());
									vcw.setTerminCode(vo.getTerminCode());
									vcw.setStatus(0);
									vcw.setPushStatus(0);
									newvcw = vehicleCostWechatService.add(vcw);
								}
								else{
									newvcw.setLastPayTime(lastPayTime);
									vehicleCostWechatService.update(newvcw);
								}
								out.print("{\"status\":\"true\",\"errorCode\":\"0\",\"errorMsg\":\"成功\",\"up_json\":" + decrypt + ",\"push_url\":\"wx.parkbobo.com/wxCost_pushCost?kid=" + newvcw.getKid() + "\"}");
//							    mv.addObject("mobile", user.getMobile());
							}else{
								out.print("{\"status\":\"false\",\"errorCode\":\"-1\",\"errorMsg\":\"停车费用json请求错误\",\"up_json\":" + decrypt + "}");							
							}
						}else{
							out.print("{\"status\":\"false\",\"errorCode\":\"-1\",\"errorMsg\":\"停车费用json为空\",\"up_json\":" + reJson + "}");		
						}
					}
					
				}
				else{
					out.print("{\"status\":\"false\",\"errorCode\":\"-1\",\"errorMsg\":\"该车辆不在停车场内\"}");
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorCode\":\"-1\",\"errorMsg\":\"系统错误\"}");
		}
		return null;
	}
	
	
	@RequestMapping("wxCost_toTest")
	public ModelAndView toTest(String code,String state,String inunid, String openid,HttpServletRequest request,HttpServletResponse response) throws Exception{
		ModelAndView mv = new ModelAndView();
		String domain = Configuration.getInstance().getValue("domainName");
		String path = request.getContextPath();
		String appID = Configuration.getInstance().getValue("hik_appid");
//		String appsecret = Configuration.getInstance().getValue("hik_appsecret");
		try {
			//重定向code
			if(code==null){
				String URL = domain+path+"/wxCost_toTest";
				String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" + 
				"appid="+appID+"&" + 
				"redirect_uri=" + URLEncoder.encode(URL, "UTF-8") + 
				"&response_type=code&scope=snsapi_base&state="+inunid+","+openid+"#wechat_redirect";
				response.setStatus(301);
				response.setHeader("Location",url);
				response.setHeader("Connection","close");
				return null;
			}else{
				//获取停车信息
				String[] params = state.split(",");
				inunid = params[0];
				openid = params[1];
				ParkPassVehicleMonth ppv = parkPassVehicleMonthService.loadByInunid(inunid);
//				System.out.println(inunid + "--" + ppv.getCarparkid());
				ParkPassVehicleMonth isIn = parkPassVehicleMonthService.retentionCar(ppv.getCarplate());
				if(isIn != null){
					//检测是否有已缴费未推送成功的记录
					//如果存在，重新推送一遍
					String hql = "from VehicleCostWechat where inUnid = '" + inunid + "' and status = 1 and pushStatus = 0";
					List<VehicleCostWechat> needPusthList = vehicleCostWechatService.getByHql(hql);
					for(VehicleCostWechat vcw : needPusthList){
						vcw.setPushStatus(1);
						vehicleCostWechatService.update(vcw);
					}
//					if(needPusthList.size() > 0){
//						push(inunid);
//					}
					//再次检测，如果仍存在，停止请求账单，返回错误
					needPusthList = vehicleCostWechatService.getByHql(hql);
					if(needPusthList.size() > 0){
						mv.addObject("errMsg", "停车费用获取失败");
						mv.setViewName("weixin/tsp/error");	
					}
					else{
						//获取停车费用信息
						String parkCode = "";
						Carpark carpark = this.carparkService.get(ppv.getCarparkid());
						parkCode = carpark.getThirdId();
						String parkName = carpark.getName();
						
//						String reqIp = Configuration.getInstance().getValue("hikip");
//						String url = reqIp+"/GetVehicleCost";
//						StringBuilder body = new StringBuilder();
//						body.append("{");
//						body.append("\"token\":\"\",");
//						body.append("\"parkingCode\":\""+parkCode+"\",");
//						body.append("\"plateInfo\":\""+ppv.getCarplate()+"\",");
//						body.append("\"plateColor\":"+ppv.getPlatecolor()+",");
//						body.append("\"carType\":"+ppv.getCartype()+",");
//						body.append("\"inUnid\":\""+inunid+"\",");
//						body.append("\"inTime\":\""+SDF.format(ppv.getPassTime())+"\"");
//						body.append("}");
//						System.out.println(body.toString());
//						//加密
//						String enStr = AESOperator.getInstance().encrypt(body.toString());
//						String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
//						System.out.println(reJson);
//						if(reJson!=null){
							//解密停车费用信息
//							JSONObject jsonObject = JSONObject.parseObject(reJson);
//							String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
//							System.out.println("停车费用信息：" + decrypt);
//							VehicleOrder vo = JSONObject.parseObject(decrypt, VehicleOrder.class);
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							VehicleOrder vo = new VehicleOrder();
							vo.setErrorCode(300000);
							vo.setInTime(sdf.format(ppv.getPassTime()));
							vo.setInUnid(ppv.getInUnid());
							vo.setLastPayTime(null);
							vo.setMessage("获取成功");
							vo.setParkingcode(parkCode);
							vo.setPayUnid(UUID.randomUUID().toString().replace("-", ""));
							vo.setPlanPayTime(sdf.format(new Date()));
							vo.setPlateInfo(ppv.getCarplate());
							vo.setShouldPayMoney(500);
							vo.setTerminCode("2342345234");
							if(vo.getErrorCode().equals(300000)){
								//删除未支付账单
								vehicleCostWechatService.deleteUnpayed(vo.getInUnid());
								//获取已支付最近一次账单
								VehicleCostWechat lastPayed = vehicleCostWechatService.loadByInunid(vo.getInUnid());
								Date lastPayTime = lastPayed == null ? null : lastPayed.getPlanPayTime();
//								System.out.println("删除未支付账单！！");
								//根据payUnid检查是否已经存储
								VehicleCostWechat newvcw = vehicleCostWechatService.loadByPayUnid(vo.getPayUnid());
								if(newvcw == null){
									VehicleCostWechat vcw = new VehicleCostWechat();
									vcw.setCarparkid(ppv.getCarparkid());
									vcw.setCarPlate(ppv.getCarplate());
									vcw.setInTime(ppv.getPassTime());
									vcw.setInUnid(ppv.getInUnid());
//									vcw.setLastPayTime(vo.formatLastPayTime());
									vcw.setLastPayTime(lastPayTime);
									vcw.setPayUnid(vo.getPayUnid());
									vcw.setPlanPayTime(vo.formatPlanPayTime());
									vcw.setShouldPayMoney(vo.getShouldPayMoney());
									vcw.setTerminCode(vo.getTerminCode());
									vcw.setStatus(0);
									vcw.setPushStatus(0);
									newvcw = vehicleCostWechatService.add(vcw);
								}
								else{
									newvcw.setLastPayTime(lastPayTime);
									vehicleCostWechatService.update(newvcw);
								}
								//根据inUnid获取账单记录，计算应缴，已缴，本次需缴
								List<VehicleCostWechat> vcwList = vehicleCostWechatService.loadListWithInUnid(vo.getInUnid());
								
								Integer shouldPay, hasPayed, needPay;
								shouldPay = hasPayed = needPay = 0;
								for(VehicleCostWechat vcw1 : vcwList){
									shouldPay += vcw1.getShouldPayMoney();
									if(vcw1.getStatus() == 1){
										hasPayed += vcw1.getShouldPayMoney();
									}
									else{
										needPay += vcw1.getShouldPayMoney();
									}
								}
								
								mv.addObject("vcw", newvcw);
								mv.addObject("shouldPay", formatMoney(shouldPay));
								mv.addObject("hasPayed", formatMoney(hasPayed));
								mv.addObject("needPay", formatMoney(needPay));
								mv.addObject("parkId", ppv.getCarparkid());
								mv.addObject("parkName", parkName);
								mv.setViewName("weixin/tsp/toWxCost");	
								
								//获取停车场列表
							    List<Carpark> carparks = this.carparkService.getAll();
							    
							    //获取车辆列表
//							    net.sf.json.JSONObject json = WeixinUtils.getOpenId(appID, appsecret, code);
//							    System.out.println(json.toString());
//								String openid = json.get("openid").toString();
//								System.out.println(openid);
								Users user = usersService.loadByOpenid(openid);
								List<UsersCars> carList = usersCarsService.loadByMobile(user.getMobile());
								
							    mv.addObject("hikParking", carparks);
							    mv.addObject("carList", carList);
							    mv.addObject("users", user);
//							    mv.addObject("mobile", user.getMobile());
							}else{
								mv.addObject("errMsg", "停车费用获取失败");
								mv.setViewName("weixin/tsp/error");								
							}
//						}else{
//							mv.addObject("errMsg", "停车费用获取失败");
//							mv.setViewName("weixin/tsp/error");				
//						}
					}
					
				}
				else{
					mv.addObject("errMsg", "该车辆不在停车场内");
					mv.setViewName("weixin/tsp/error");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mv.addObject("errMsg", "系统错误");
			mv.setViewName("weixin/tsp/error");
		}
		return mv;
	}
	
	
	/**
	 * 用户切换停车场、车牌号码后
	 * ajax请求费用
	 * @param carparkid
	 * @param carPlate
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("wxCost_ajaxCountTest")
	public String ajaxCountTest(Long carparkid, String carPlate, HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			ParkPassVehicleMonth ppv = parkPassVehicleMonthService.retentionCar(carPlate);
			if(ppv != null && ppv.getCarparkid().equals(carparkid)){
				//检测是否有已缴费未推送成功的记录
				//如果存在，重新推送一遍
				String hql = "from VehicleCostWechat where inUnid = '" + ppv.getInUnid() + "' and status = 1 and pushStatus = 0";
				List<VehicleCostWechat> needPusthList = vehicleCostWechatService.getByHql(hql);
				if(needPusthList.size() > 0){
					push(ppv.getInUnid());
				}
				//再次检测，如果仍存在，停止请求账单，返回错误
				needPusthList = vehicleCostWechatService.getByHql(hql);
				if(needPusthList.size() > 0){
					out.print("{\"status\":\"false\",\"errorcode\":\"11\",\"errMsg\":\"获取停车费用失败\"}");
				}
				else{
					//获取停车费用信息
					String parkCode = "";
					Carpark carpark = this.carparkService.get(ppv.getCarparkid());
//					System.out.println(jsonOb.toString());
					parkCode = carpark.getThirdId();
//					String parkName = jsonOb.getString("parkName");
					
//					String reqIp = Configuration.getInstance().getValue("hikip");
//					String url = reqIp+"/GetVehicleCost";
//					StringBuilder body = new StringBuilder();
//					body.append("{");
//					body.append("\"token\":\"\",");
//					body.append("\"parkingCode\":\""+parkCode+"\",");
//					body.append("\"plateInfo\":\""+ppv.getCarplate()+"\",");
//					body.append("\"plateColor\":"+ppv.getPlatecolor()+",");
//					body.append("\"carType\":"+ppv.getCartype()+",");
//					body.append("\"inUnid\":\""+ppv.getInUnid()+"\",");
//					body.append("\"inTime\":\""+SDF.format(ppv.getPassTime())+"\"");
//					body.append("}");
//					System.out.println(body.toString());
//					//加密
//					String enStr = AESOperator.getInstance().encrypt(body.toString());
//					String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
//					System.out.println(reJson);
//					if(reJson!=null){
//						//解密停车费用信息
//						JSONObject jsonObject = JSONObject.parseObject(reJson);
//						String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
//						System.out.println("停车费用信息：" + decrypt);
//						VehicleOrder vo = JSONObject.parseObject(decrypt, VehicleOrder.class);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						VehicleOrder vo = new VehicleOrder();
						vo.setErrorCode(300000);
						vo.setInTime(sdf.format(ppv.getPassTime()));
						vo.setInUnid(ppv.getInUnid());
						vo.setLastPayTime(null);
						vo.setMessage("获取成功");
						vo.setParkingcode(parkCode);
						vo.setPayUnid(UUID.randomUUID().toString().replace("-", ""));
						vo.setPlanPayTime(sdf.format(new Date()));
						vo.setPlateInfo(ppv.getCarplate());
						vo.setShouldPayMoney(500);
						vo.setTerminCode("2342345234");
						if(vo.getErrorCode().equals(300000)){
							//删除未支付账单
							vehicleCostWechatService.deleteUnpayed(vo.getInUnid());
							//获取已支付最近一次账单
							VehicleCostWechat lastPayed = vehicleCostWechatService.loadByInunid(vo.getInUnid());
							Date lastPayTime = lastPayed == null ? vo.formatInTime() : lastPayed.getPlanPayTime();
//							System.out.println("删除未支付账单！！");
							//根据payUnid检查是否已经存储
							VehicleCostWechat newvcw = vehicleCostWechatService.loadByPayUnid(vo.getPayUnid());
							if(newvcw == null){
								VehicleCostWechat vcw = new VehicleCostWechat();
								vcw.setCarparkid(ppv.getCarparkid());
								vcw.setCarPlate(ppv.getCarplate());
								vcw.setInTime(ppv.getPassTime());
								vcw.setInUnid(ppv.getInUnid());
//								vcw.setLastPayTime(vo.formatLastPayTime());
								vcw.setLastPayTime(lastPayTime);
								vcw.setPayUnid(vo.getPayUnid());
								vcw.setPlanPayTime(vo.formatPlanPayTime());
								vcw.setShouldPayMoney(vo.getShouldPayMoney());
								vcw.setTerminCode(vo.getTerminCode());
								vcw.setStatus(0);
								newvcw = vehicleCostWechatService.add(vcw);
							}
							else{
								newvcw.setLastPayTime(lastPayTime);
								vehicleCostWechatService.update(newvcw);
							}
							//根据inUnid获取账单记录，计算应缴，已缴，本次需缴
							List<VehicleCostWechat> vcwList = vehicleCostWechatService.loadListWithInUnid(vo.getInUnid());
							
							Integer shouldPay, hasPayed, needPay;
							shouldPay = hasPayed = needPay = 0;
							for(VehicleCostWechat vcw1 : vcwList){
								shouldPay += vcw1.getShouldPayMoney();
								if(vcw1.getStatus() == 1){
									hasPayed += vcw1.getShouldPayMoney();
								}
								else{
									needPay += vcw1.getShouldPayMoney();
								}
							}
							out.print("{\"status\":\"success\",\"errorcode\":\"00\",\"inTime\":\""
									+ newvcw.formatInTime() +"\",\"parkTime\":\""
									+ newvcw.formatParkTime() +"\",\"shouldPay\":\""
									+ formatMoney(shouldPay) +"\",\"hasPay\":\""
									+ formatMoney(hasPayed) +"\",\"needPay\":\""
									+ formatMoney(needPay) +"\"}");
						}
						else{
							out.print("{\"status\":\"false\",\"errorcode\":\"11\",\"errMsg\":\"获取停车费用失败\"}");
						}
//					}
//					else{
//						out.print("{\"status\":\"false\",\"errorcode\":\"11\",\"errMsg\":\"获取停车费用失败\"}");
//					}
				}
				
			}
			else{
				out.print("{\"status\":\"false\",\"errorcode\":\"11\",\"errMsg\":\"车辆不在该停车场内\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorcode\":\"11\",\"errMsg\":\"系统错误\"}");
		}
		
		out.flush();
		out.close();
		return null;
	}
	
	/*@RequestMapping("wxCost_pushTest")
	public String pushTest(Integer kid, HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			//更新缴费记录为已支付
			VehicleCostWechat vcw = vehicleCostWechatService.get(kid);
			vcw.setStatus(1);
			vcw.setPushStatus(1);
			vcw.setPayTime(new Date());
			vehicleCostWechatService.update(vcw);
			
			//向下级客户端推送
				List<UsersCars> carList = usersCarsService.getByHql("from UsersCars where carPlate = '" + vcw.getCarPlate() + "'");
				String mobiles = "";
				for(UsersCars uc : carList){
					mobiles += "'" + uc.getMobile() + "',";
				}
				List<Users> userList = new ArrayList<Users>();
				if(mobiles.length() > 0){
					mobiles = mobiles.substring(0, mobiles.length() - 1);
					userList = usersService.getByHql("from Users where mobile in (" + mobiles + ") and openid is not null");
				}
				String token = configService.getNameToToken("hik").getAccess_token();
				for(Users u : userList){
					String sendGet = WeixinUtils.sendPaymentNotice(u.getOpenid(), token, vcw.getCarPlate(), vcw.formatPayTime(), vcw.formatInTime(), vcw.formatParkTime(), vcw.formateShouldPayMoney());
					JSONObject jsonOb = JSONObject.parseObject(sendGet);
					if(jsonOb.getInteger("errcode") == 42001){
						net.sf.json.JSONObject tokenObject = WeixinUtils.getToken();
						token = tokenObject.getString("access_token");
						net.sf.json.JSONObject ticketObject = WeixinUtils.getTicket(token);
						String jsapiTicket = ticketObject.getString("ticket");
						HttpRequest.sendGet(requestUrl+"/wxConfig_save", "name=hik&token="+token+"&jsapiTicket="+jsapiTicket);
						WeixinUtils.sendPaymentNotice(u.getOpenid(), token, vcw.getCarPlate(), vcw.formatPayTime(), vcw.formatInTime(), vcw.formatParkTime(), vcw.formateShouldPayMoney());
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorcode\":\"11\"}");
		}
		
		out.flush();
		out.close();
		return null;
	}*/
	
	
	
	/**
	 * 把单位分转成元
	 * @param money
	 * @return
	 */
	public String formatMoney(Integer money){
		if(money < 10){
			return "0.0" + money;
		}
		else if (money < 100){
			return "0." + money;
		}
		else{
			return money / 100 +  "." + money % 100;
		}
	}
	
	private String dateFormat(Date date){
		SimpleDateFormat dsf = new SimpleDateFormat("yyyyMMddHHmmss");
		return dsf.format(date);
	}
	
	 private String getIpAddr(HttpServletRequest request) {     
	      String ip = request.getHeader("x-forwarded-for");     
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	         ip = request.getHeader("Proxy-Client-IP");     
	     }     
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	         ip = request.getHeader("WL-Proxy-Client-IP");     
	      }     
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	          ip = request.getRemoteAddr();     
	     }     
	     return ip;     
	}    
	public static void main(String[] args) {
		System.out.println(new Date().getTime());
	}

}
