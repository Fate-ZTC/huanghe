package com.parkbobo.weixin.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.parkbobo.model.Carpark;
import com.parkbobo.model.HandleMonthlyVehicle;
import com.parkbobo.model.Users;
import com.parkbobo.model.UsersCars;
import com.parkbobo.model.VehicleCostWechat;
import com.parkbobo.service.CarparkService;
import com.parkbobo.service.HandleMonthlyVehicleService;
import com.parkbobo.service.UsersCarsService;
import com.parkbobo.service.UsersService;
import com.parkbobo.utils.AESOperator;
import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.http.HttpUtils;
import com.parkbobo.utils.httpmodel.MonthlyVehicleRequest;
import com.parkbobo.utils.httpmodel.MonthlypaymentRule;
import com.parkbobo.utils.httpmodel.QueryVehicleMonthly;
import com.parkbobo.utils.weixin.WeixinUtils;
import com.parkbobo.wxpay.WXPayConfigImpl;
import com.parkbobo.wxpay.WXPayRequest;
import com.parkbobo.wxpay.WXPayUtil;

@Controller
public class MonthlyCarController implements Serializable{
	private static final long serialVersionUID = -2902447954794622012L;
	private static final String APPID = Configuration.getInstance().getValue("hik_appid");
	private static final String mchID = Configuration.getInstance().getValue("mch_id");
	//private static final String subMchID = Configuration.getInstance().getValue("sub_mch_id");
	private static final String key = Configuration.getInstance().getValue("key");
	@Resource(name="usersService")
	private UsersService usersService;
	@Resource(name="usersCarsService")
	private UsersCarsService usersCarsService;
	@Resource
	private CarparkService carparkService;
	@Resource
	private HandleMonthlyVehicleService handleMonthlyVehicleService;
	
	/**
	 * 获取包期规则 
	 * 
	 */
	@RequestMapping("getMonthlypaymentRule")
	public void getMonthlypaymentRule(String parkingCode,HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			String reqIp = Configuration.getInstance().getValue("hikip");
			String url = reqIp+"/GetMonthlypaymentRule";
			StringBuilder body = new StringBuilder();
			System.out.println("parkingCode:"+parkingCode);
			body.append("{");
			body.append("\"parkingCode\":\""+parkingCode+"\"");
			body.append("}");
			System.out.println(url);
			String enStr = AESOperator.getInstance().encrypt(body.toString());
			System.out.println(enStr);
			String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
			if(reJson!=null){
				JSONObject jsonObject = JSONObject.parseObject(reJson);
				String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
				System.out.println("停车场收费信息：" + decrypt);
				out.print(decrypt);
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"网络错误\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\""+e.getMessage()+"\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	/**
	 * 查询是否为包期车
	 * */
	@RequestMapping("queryVehicleMonthly")
	public void queryVehicleMonthly(String parkingCode,String plateNo,Integer plateColor,HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			String reqIp = Configuration.getInstance().getValue("hikip");
			String url = reqIp+"/QueryVehicleMonthly";
			StringBuilder body = new StringBuilder();
			body.append("{");
			body.append("\"parkingCode\":\""+parkingCode+"\",");
			body.append("\"plateNo\":\""+plateNo+"\",");
			body.append("\"plateColor\":"+plateColor+"");
			body.append("}");
			String enStr = AESOperator.getInstance().encrypt(body.toString());
			String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
			if(reJson!=null){
				JSONObject jsonObject = JSONObject.parseObject(reJson);
				String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
				System.out.println("停车场收费信息：" + decrypt);
				out.print(decrypt);
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"网络错误\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\""+e.getMessage()+"\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 月租充值或续费
	 * */
	@RequestMapping("handleBagVehicle")
	public void handleBagVehicle(String parkingCode,String plateNo,Integer plateColor,Integer ruleId,String startTime,String endTime,String vehicleHost,Integer vehicleType,Integer payFee,Integer payType,HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String reqIp = Configuration.getInstance().getValue("hikip");
			String url = reqIp+"/HandleBagVehicle";
			StringBuilder body = new StringBuilder();
			body.append("{");
			body.append("\"payUnid\":\""+UUID.randomUUID().toString().replaceAll("-", "")+"\",");
			body.append("\"parkingCode\":\""+parkingCode+"\",");
			body.append("\"plateNo\":\""+plateNo+"\",");
			body.append("\"plateColor\":"+plateColor+",");
			body.append("\"ruleId\":"+ruleId+",");
			body.append("\"startTime\":\""+startTime+"\",");
			body.append("\"endTime\":\""+endTime+"\",");
			body.append("\"vehicleHost\":\""+vehicleHost+"\",");
			body.append("\"vehicleType\":"+vehicleType+",");
			body.append("\"payFee\":"+payFee+",");
			body.append("\"payType\":"+payType+",");
			body.append("\"dataSource\":"+2+",");
			body.append("\"handleTime\":\""+sdf.format(new Date())+"\"");
			body.append("}");
			System.out.println("发送数据JSON:"+body);
			String enStr = AESOperator.getInstance().encrypt(body.toString());
			String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
			if(reJson!=null){
				JSONObject jsonObject = JSONObject.parseObject(reJson);
				String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
				System.out.println("停车场收费信息：" + decrypt);
				out.print(decrypt);
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":-2,\"errorMsg\":\"网络错误\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorCode\":-1,\"errorMsg\":\""+e.getMessage()+"\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 月租续费列表
	 * */
	@RequestMapping("monthlyRenewList")
	public ModelAndView monthlyRenewList(String code,HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		String domain = Configuration.getInstance().getValue("domainName");
		String path = request.getContextPath();
		String appID = Configuration.getInstance().getValue("hik_appid");
		String appsecret = Configuration.getInstance().getValue("hik_appsecret");
		try {
			if(code==null){
				String URL = domain+path+"/monthlyRenewList";
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
					String URL = domain+path+"/monthlyRenewList";
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
						System.out.println(carList.size());
						List<Carpark> carparks = carparkService.getAll();
						List<QueryVehicleMonthly> qvMonthly = new ArrayList<QueryVehicleMonthly>();
						mv.addObject("carNum", (carList==null?0:carList.size()));
						if(carList!=null && carList.size()>0){
							for (int i = 0; i < carList.size(); i++) {
								if(carList.get(i).getAuthStatus()!=null && carList.get(i).getAuthStatus().equals(1)){
									int inx = 0;
									for (int j = 0; j < carparks.size(); j++) {
										QueryVehicleMonthly queryVehicleMonthly = MonthlyVehicleRequest.queryVehicleMonthly(carparks.get(j).getThirdId(), carList.get(i).getCarPlate(), carList.get(i).getPlateColor());
										if(queryVehicleMonthly!=null){
											queryVehicleMonthly.setCarparkid(carparks.get(j).getCarparkid());
											queryVehicleMonthly.setCarparkName(carparks.get(j).getName());
											queryVehicleMonthly.setCarPlate(carList.get(i).getCarPlate());
											queryVehicleMonthly.setKid(carList.get(i).getKid());
											qvMonthly.add(queryVehicleMonthly);
											inx = 1;
										}
									}
									if(inx == 1){
										carList.remove(i);
										i--;
									}
								}else{
									carList.remove(i);
									i--;
								}
							}
						}
						mv.addObject("carList", carList);
						mv.addObject("qvMonthly",qvMonthly);
						mv.addObject("user", user);
						mv.setViewName("weixin/tsp/monthlyRenewList");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errMsg", "系统错误");
			mv.setViewName("weixin/tsp/monthlyRenewList");
		}
		return mv;
	}
	
	/**
	 * 月租续费
	 * */
	@RequestMapping("monthlyRenew")
	public ModelAndView monthlypaymentRule(Integer kid,Integer ruleType,Long carparkid,String plateNo,Integer ruleId,String ruleName,Integer payPee,String endTime,HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		try {
			if(StringUtils.isNotBlank(plateNo)){
				plateNo = URLDecoder.decode(URLEncoder.encode(plateNo, "ISO8859_1"), "UTF-8");
			}
			if(StringUtils.isNotBlank(ruleName)){
				ruleName = URLDecoder.decode(URLEncoder.encode(ruleName, "ISO8859_1"), "UTF-8");
			}
			Carpark carpark = carparkService.get(carparkid);
			List<MonthlypaymentRule> monthlypaymentRule = MonthlyVehicleRequest.getMonthlypaymentRule(carpark.getThirdId());		
			mv.addObject("monthlypaymentRule", monthlypaymentRule);
			mv.addObject("ruleId", ruleId);
			mv.addObject("plateNo", plateNo);
			mv.addObject("ruleName", ruleName);
			mv.addObject("payPee", payPee);
			mv.addObject("kid", kid);
			mv.addObject("endTime", endTime);
			mv.addObject("ruleType", ruleType);
			mv.addObject("carparkName", carpark.getName());
			mv.addObject("carparkid", carpark.getCarparkid());
			mv.setViewName("weixin/tsp/monthlyRenew");
		} catch (Exception e) {
			e.printStackTrace();
			mv.addObject("errMsg", "系统错误");
			mv.setViewName("weixin/tsp/monthlyRenew");
		}
		return mv;
	}
	
	@RequestMapping("monthly_temPay")
	public String temPay(Integer hTime,Integer kid,Integer ruleType,Long carparkid,String plateNo,Integer ruleId,String ruleName,Integer totalPay,String endTime,HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String domain = Configuration.getInstance().getValue("domainName");
		String path = request.getContextPath();
		try {
			UsersCars usersCars = usersCarsService.get(kid);
			String mobile = usersCars.getMobile();
			Users users = usersService.get(mobile);
			Carpark carpark = carparkService.get(carparkid);
			Date startTime = this.endTimeToStart(endTime);
			HandleMonthlyVehicle handleMonthlyVehicle = new HandleMonthlyVehicle();
			handleMonthlyVehicle.setRuleType(ruleType);
			handleMonthlyVehicle.setCarpark(carpark);
			handleMonthlyVehicle.setCarPlate(plateNo);
			handleMonthlyVehicle.setRuleId(ruleId);
			handleMonthlyVehicle.setDataSource(2);
			handleMonthlyVehicle.setMid(UUID.randomUUID().toString().replaceAll("-", ""));
			handleMonthlyVehicle.setHandleTime(new Date());
			handleMonthlyVehicle.setIsRenew(1);
			handleMonthlyVehicle.setPayFee(totalPay);
			handleMonthlyVehicle.setPayType(5);
			handleMonthlyVehicle.setPlateColor(usersCars.getPlateColor());
			handleMonthlyVehicle.setPushStatus(0);
			handleMonthlyVehicle.setStatus(0);
			handleMonthlyVehicle.setVehicleType(usersCars.getVehicleType());
			handleMonthlyVehicle.setVehicleHost(usersCars.getVehicleHost());
			handleMonthlyVehicle.setRuleName(ruleName);
			handleMonthlyVehicle.setStartTime(startTime);
			handleMonthlyVehicle.setEndTime(this.getEndTime(startTime, hTime, ruleType));
			handleMonthlyVehicle.setUsers(users);
			handleMonthlyVehicle.setOpenid(users.getOpenid());
			handleMonthlyVehicleService.add(handleMonthlyVehicle);
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
			data.put("out_trade_no", handleMonthlyVehicle.getMid());
			data.put("fee_type", "CNY");
			data.put("time_start", this.dateFormat(now));
			data.put("time_expire", this.dateFormat(new Date(now.getTime()+(2*60*1000))));
			data.put("total_fee", String.valueOf(handleMonthlyVehicle.getPayFee()));
			data.put("spbill_create_ip", this.getIpAddr(request));
			data.put("notify_url", domain+path+"/monthly_wxPaySuccess/");
			data.put("trade_type", "JSAPI");
			data.put("limit_pay", "no_credit");
			data.put("openid", users.getOpenid());
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
		    				"\"mid\":\""+handleMonthlyVehicle.getMid()+"\"," +
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
	 * 支付成功通知海康
	 * renewPay
	 * @throws Exception 
	 * */
	@RequestMapping("monthly_wxPaySuccess")
	public void wxPaySuccess(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		System.out.println("执行monthly_wxPaySuccess");
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
					String mid = paySuRe.get("out_trade_no");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					HandleMonthlyVehicle handleMonthlyVehicle = handleMonthlyVehicleService.get(mid);
					if(handleMonthlyVehicle==null){
						// 退款
						System.out.println("1111111111111111111111111111111111111111111111");
						Map<String, String> refundMap = new HashMap<String, String>();
						String nonce_str = WXPayUtil.generateNonceStr();
						String out_refund_no = WXPayUtil.generateUUID();
						refundMap.put("appid", APPID);
						refundMap.put("mch_id", mchID);
						refundMap.put("nonce_str", nonce_str);
						refundMap.put("out_trade_no", String.valueOf(mid));
						refundMap.put("out_refund_no", out_refund_no);
						refundMap.put("total_fee", paySuRe.get("total_fee")+ "");
						refundMap.put("refund_fee", paySuRe.get("total_fee")+ "");
						refundMap.put("refund_desc", "停车场网络中断");
						String refundXml = WXPayUtil.generateSignedXml(refundMap, key);
						WXPayConfigImpl config = WXPayConfigImpl.getInstance();
						WXPayRequest wxPayRequest = new WXPayRequest(config);
						String refXml = wxPayRequest.requestWithCert("/secapi/pay/refund", nonce_str, refundXml,true);
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
					}else{
						if(handleMonthlyVehicle.getStatus().equals(0)){
							handleMonthlyVehicle.setStatus(1);
							handleMonthlyVehicleService.merge(handleMonthlyVehicle);
						}
						if(handleMonthlyVehicle.getPushStatus().equals(0)){
							String reqIp = Configuration.getInstance().getValue("hikip");
							String url = reqIp+"/HandleBagVehicle";
							Long carparkid = handleMonthlyVehicle.getCarpark().getCarparkid();
							Carpark carpark = carparkService.get(carparkid);
							StringBuilder body = new StringBuilder();
							body.append("{");
							body.append("\"payUnid\":\""+UUID.randomUUID().toString().replaceAll("-", "")+"\",");
							body.append("\"parkingCode\":\""+carpark.getThirdId()+"\",");
							body.append("\"plateNo\":\""+handleMonthlyVehicle.getCarPlate()+"\",");
							body.append("\"plateColor\":"+handleMonthlyVehicle.getPlateColor()+",");
							body.append("\"ruleId\":"+handleMonthlyVehicle.getRuleId()+",");
							body.append("\"startTime\":\""+sdf.format(handleMonthlyVehicle.getStartTime())+"\",");
							body.append("\"endTime\":\""+sdf.format(handleMonthlyVehicle.getEndTime())+"\",");
							body.append("\"vehicleHost\":\""+handleMonthlyVehicle.getVehicleHost()+"\",");
							body.append("\"vehicleType\":"+handleMonthlyVehicle.getVehicleType()+",");
							body.append("\"payFee\":"+handleMonthlyVehicle.getPayFee()+",");
							body.append("\"payType\":"+handleMonthlyVehicle.getPayType()+",");
							body.append("\"dataSource\":"+handleMonthlyVehicle.getDataSource()+",");
							body.append("\"handleTime\":\""+handleMonthlyVehicle.getHandleTime()+"\"");
							body.append("}");
							System.out.println("发送数据JSON:"+body);
							String enStr = AESOperator.getInstance().encrypt(body.toString());
							String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
							if(reJson!=null){
								JSONObject jsonObject = JSONObject.parseObject(reJson);
								String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
								System.out.println("停车场收费信息：" + decrypt);
								JSONObject reMsg = JSON.parseObject(decrypt);
								Integer errorCode = reMsg.getInteger("errorCode");
								if(errorCode.equals(300000)){
									//推送成功
									handleMonthlyVehicle.setPushStatus(1);
									handleMonthlyVehicleService.update(handleMonthlyVehicle);
									Map<String, String> returnMap = new HashMap<String, String>();
									returnMap.put("return_code", "SUCCESS");
									returnMap.put("return_msg", "OK");
									String returnXml = WXPayUtil.mapToXml(returnMap);
									out.print(returnXml);
								}else{
									Map<String, String> refundMap = new HashMap<String, String>();
									String nonce_str = WXPayUtil.generateNonceStr();
									String out_refund_no = WXPayUtil.generateUUID();
									refundMap.put("appid", APPID);
									refundMap.put("mch_id", mchID);
									refundMap.put("nonce_str", nonce_str);
									refundMap.put("out_trade_no", String.valueOf(mid));
									refundMap.put("out_refund_no", out_refund_no);
									refundMap.put("total_fee", paySuRe.get("total_fee")+ "");
									refundMap.put("refund_fee", paySuRe.get("total_fee")+ "");
									refundMap.put("refund_desc", "停车场网络中断");
									String refundXml = WXPayUtil.generateSignedXml(refundMap, key);
									WXPayConfigImpl config = WXPayConfigImpl.getInstance();
									WXPayRequest wxPayRequest = new WXPayRequest(config);
									String refXml = wxPayRequest.requestWithCert("/secapi/pay/refund", nonce_str, refundXml,true);
									Map<String, String> refMap = WXPayUtil.xmlToMap(refXml);
									if (refMap.get("return_code").equals("SUCCESS")) {
										if (refMap.get("result_code").equals("SUCCESS")) {
											System.out.println("退款申请成功");
										}
									}
									handleMonthlyVehicle.setPushStatus(-1);
									handleMonthlyVehicle.setStatus(-1);
									handleMonthlyVehicleService.update(handleMonthlyVehicle);
									Map<String, String> returnMap = new HashMap<String, String>();
									returnMap.put("return_code", "SUCCESS");
									returnMap.put("return_msg", "OK");
									String returnXml = WXPayUtil.mapToXml(returnMap);
									out.print(returnXml);
								}
							}else{
								//退款
								Map<String, String> refundMap = new HashMap<String, String>();
								String nonce_str = WXPayUtil.generateNonceStr();
								String out_refund_no = WXPayUtil.generateUUID();
								refundMap.put("appid", APPID);
								refundMap.put("mch_id", mchID);
								refundMap.put("nonce_str", nonce_str);
								refundMap.put("out_trade_no", String.valueOf(mid));
								refundMap.put("out_refund_no", out_refund_no);
								refundMap.put("total_fee", paySuRe.get("total_fee")+ "");
								refundMap.put("refund_fee", paySuRe.get("total_fee")+ "");
								refundMap.put("refund_desc", "停车场网络中断");
								String refundXml = WXPayUtil.generateSignedXml(refundMap, key);
								WXPayConfigImpl config = WXPayConfigImpl.getInstance();
								WXPayRequest wxPayRequest = new WXPayRequest(config);
								String refXml = wxPayRequest.requestWithCert("/secapi/pay/refund", nonce_str, refundXml,true);
								Map<String, String> refMap = WXPayUtil.xmlToMap(refXml);
								if (refMap.get("return_code").equals("SUCCESS")) {
									if (refMap.get("result_code").equals("SUCCESS")) {
										System.out.println("退款申请成功");
									}
								}
								handleMonthlyVehicle.setPushStatus(-1);
								handleMonthlyVehicle.setStatus(-1);
								handleMonthlyVehicleService.update(handleMonthlyVehicle);
								Map<String, String> returnMap = new HashMap<String, String>();
								returnMap.put("return_code", "SUCCESS");
								returnMap.put("return_msg", "OK");
								String returnXml = WXPayUtil.mapToXml(returnMap);
								out.print(returnXml);
							}
						}else{
							Map<String, String> returnMap = new HashMap<String, String>();
							returnMap.put("return_code", "SUCCESS");
							returnMap.put("return_msg", "OK");
							String returnXml = WXPayUtil.mapToXml(returnMap);
							out.print(returnXml);
						}
					}
				}else{
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
			System.out.println(e.getMessage());
			Map<String, String> returnMap = new HashMap<String, String>();
			returnMap.put("return_code", "SUCCESS");
			returnMap.put("return_msg", "OK");
			String returnXml = WXPayUtil.mapToXml(returnMap);
			out.print(returnXml);
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
	@RequestMapping("monthly_paySuccess")
	public ModelAndView paySuccess(String mid,HttpServletRequest request,HttpServletResponse response) throws Exception{
		System.out.println("执行wxCost_paySuccess");
		ModelAndView mv = new ModelAndView();
		HandleMonthlyVehicle hmv = handleMonthlyVehicleService.get(mid);
		try {
			// 向下级客户端推送
			Map<String,String> queryMap = new HashMap<String, String>();
			String nonce_str_query = WXPayUtil.generateNonceStr();
			queryMap.put("appid", APPID);
			queryMap.put("mch_id", mchID);
			queryMap.put("nonce_str", nonce_str_query);
			queryMap.put("out_trade_no", String.valueOf(mid));
			String queryXml = WXPayUtil.generateSignedXml(queryMap,key);
			WXPayConfigImpl config = WXPayConfigImpl.getInstance();
		    WXPayRequest wxPayRequest = new WXPayRequest(config);
		    String queXml = wxPayRequest.requestWithCert("/pay/orderquery", nonce_str_query, queryXml, true);
		    System.out.println("支付查询结果："+queXml);
		    Map<String, String> queMap = WXPayUtil.xmlToMap(queXml);
		    if(queMap.get("return_code").equals("SUCCESS")){
		    	if(queMap.get("result_code").equals("SUCCESS")){
		    		if(hmv==null){
		    			mv.setViewName("weixin/tsp/payMonthlyFailed");
		    		}else{
		    			if(hmv.getStatus()!=null && hmv.getPushStatus()!=null && hmv.getStatus().equals(1)&& hmv.getPushStatus().equals(1)){
		    				Carpark carpark = this.carparkService.get(hmv.getCarpark().getCarparkid());
		    				hmv.setCarpark(carpark);
		    				mv.addObject("hmv", hmv);
		    				mv.setViewName("weixin/tsp/monthlyRenewSuccess");
		    			}else if((hmv.getPushStatus()==null || hmv.getPushStatus().equals(0)) && (hmv.getStatus().equals(0) || hmv.getStatus().equals(1))){
		    				Carpark carpark = this.carparkService.get(hmv.getCarpark().getCarparkid());
		    				String parkName = carpark.getName();
		    				hmv.setCarpark(carpark);
		    				mv.addObject("hmv", hmv);
		    				mv.addObject("mid", mid);
		    				mv.addObject("parkName", parkName);
		    				mv.setViewName("weixin/tsp/monthlyLoad");
		    			}else{
		    				mv.setViewName("weixin/tsp/payMonthlyFailed");
		    			}
		    		}
		    	}else{
					mv.setViewName("weixin/tsp/payMonthlyFailed");
		    	}
		    }else{
				mv.setViewName("weixin/tsp/payMonthlyFailed");
		    }
		} catch (Exception e) {
			e.printStackTrace();
			mv.setViewName("weixin/tsp/payMonthlyFailed");
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
	@RequestMapping("monthly_payStatus")
	public void payStatus(String mid,HttpServletRequest request,HttpServletResponse response) throws Exception{
		HandleMonthlyVehicle hmv = handleMonthlyVehicleService.get(mid);
		PrintWriter out = response.getWriter();
		try {
			if(hmv==null){
    			out.print("{\"status\",false:\"errorCode\":-1,\"errorMsg\":\"支付失败\"}");
    		}else{
    			if(hmv.getStatus()!=null && hmv.getPushStatus()!=null && hmv.getStatus().equals(1)&& hmv.getPushStatus().equals(1)){
        			out.print("{\"status\":true,\"errorCode\":0,\"errorMsg\":\"支付成功\"}");
    			}else if((hmv.getPushStatus()==null || hmv.getPushStatus().equals(0)) && (hmv.getStatus().equals(0) || hmv.getStatus().equals(1))){
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
	 * 通过到期时间获取开启时间
	 * @throws ParseException 
	 * */
	private Date endTimeToStart(String endTime) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = sdf.parse(endTime);
		Date now = new Date();
		if(parse.getTime()<=now.getTime()){//如果到期时间小于当前时间 月租过期
			String format = ymd.format(now)+" 00:00:00";
			Date startTime = sdf.parse(format);
			return startTime;
		}else{
			Long startTime = parse.getTime()+1000L;
			return new Date(startTime);
		}
	}
	
	/**
	 * 通过到期时间获取开启时间
	 * @throws ParseException 
	 * */
	private Date getEndTime(Date startTime,Integer hTime,Integer ruleType) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat mdhms = new SimpleDateFormat("MM-dd HH:mm:ss");
		SimpleDateFormat dhms = new SimpleDateFormat("dd HH:mm:ss");
		SimpleDateFormat mm = new SimpleDateFormat("MM");
		SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
		if(ruleType.equals(1)){
			Integer month = Integer.parseInt(mm.format(startTime));
			Integer year = Integer.parseInt(yyyy.format(startTime));
			year = year+hTime/12;
			hTime = hTime-hTime/12*12;
			if(month+hTime>12){
				month = month+hTime-12;
				year = year+1;
			}else{
				month = month+hTime;
			}
			String endTime = year+"-"+month + "-"+dhms.format(startTime);
			Date parse = sdf.parse(endTime);
			return new Date(parse.getTime()-1000L);
		}else{
			Integer year = Integer.parseInt(yyyy.format(startTime));
			year  = year+hTime;
			String endTime = year+"-"+mdhms.format(startTime);
			Date parse = sdf.parse(endTime);
			return new Date(parse.getTime()-1000L);
		}
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
	
	
	
	public static void main(String[] args) throws ParseException {
		String endTime = "2020-11-09 23:59:59";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
		Date parse = sdf.parse(endTime);
		Date now = new Date();
		Date startTime = null;
		if(parse.getTime()<=now.getTime()){//如果到期时间小于当前时间 月租过期
			String format = ymd.format(now)+" 00:00:00";
			startTime = sdf.parse(format);
			System.out.println(sdf.format(startTime));
		}else{
			Long startTimeL = parse.getTime()+1000L;
			startTime = new Date(startTimeL);
			System.out.println(sdf.format(new Date(startTimeL)));
		}
		
		
		Integer ruleType = 1;
		Integer hTime = 1;
		SimpleDateFormat mdhms = new SimpleDateFormat("MM-dd HH:mm:ss");
		SimpleDateFormat dhms = new SimpleDateFormat("dd HH:mm:ss");
		SimpleDateFormat mm = new SimpleDateFormat("MM");
		SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
		if(ruleType.equals(1)){
			Integer month = Integer.parseInt(mm.format(startTime));
			Integer year = Integer.parseInt(yyyy.format(startTime));
			year = year+hTime/12;
			hTime = hTime-hTime/12*12;
			if(month+hTime>12){
				month = month+hTime-12;
				year = year+1;
			}else{
				month = month+hTime;
			}
			String endTimeStr = year+"-"+month + "-"+dhms.format(startTime);
			Date reparse = sdf.parse(endTimeStr);
			System.out.println(sdf.format(new Date(reparse.getTime()-1000L)));
		}else{
			Integer year = Integer.parseInt(yyyy.format(startTime));
			year  = year+hTime;
			String endTimeStr = year+"-"+mdhms.format(startTime);
			Date reparse = sdf.parse(endTimeStr);
			System.out.println(sdf.format(new Date(reparse.getTime()-1000L)));
		}
		
	}
	private String dateFormat(Date date){
		SimpleDateFormat dsf = new SimpleDateFormat("yyyyMMddHHmmss");
		return dsf.format(date);
	}
	@RequestMapping("updateT")
	public void updateT(String mid){
		HandleMonthlyVehicle handleMonthlyVehicle = this.handleMonthlyVehicleService.get(mid);
		System.out.println(handleMonthlyVehicle.getCarPlate());
		System.out.println(handleMonthlyVehicle.getMid());
		handleMonthlyVehicle.setStatus(1);
		handleMonthlyVehicle.setPushStatus(1);
		handleMonthlyVehicleService.merge(handleMonthlyVehicle);
	}
}
