package com.parkbobo.weixin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.parkbobo.utils.AESOperator;
import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.HttpRequest;
import com.parkbobo.utils.MD5;
import com.parkbobo.utils.http.HttpUtils;
import com.parkbobo.utils.httpmodel.Carpark;
import com.parkbobo.utils.httpmodel.CarparkAll;
import com.parkbobo.utils.httpmodel.CarparkIncome;
import com.parkbobo.utils.httpmodel.CarparkIncomePre;
import com.parkbobo.utils.httpmodel.CarparkIncomes;
import com.parkbobo.utils.httpmodel.CarparkOccupyPre;
import com.parkbobo.utils.httpmodel.CarparkUsagePre;
import com.parkbobo.utils.httpmodel.LoginToken;
import com.parkbobo.utils.httpmodel.ParkFlowList;
import com.parkbobo.utils.httpmodel.SendCarpark;
import com.parkbobo.utils.httpmodel.SendSysIncome;

@Controller
public class WeixinManageController implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -282386011473695613L;
	

	@RequestMapping("manageLoginIn")
	public String loginIn(String userName,String password,HttpServletResponse response,HttpServletRequest request) throws IOException, NoSuchAlgorithmException{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if(StringUtils.isNotBlank(userName)&& StringUtils.isNotBlank(password)){
				String reqIp = Configuration.getInstance().getValue("hikip");
				String url = reqIp+"/LoginIn";
				StringBuilder body = new StringBuilder();
				body.append("{");
				body.append("\"userName\":\""+userName+"\",");
				body.append("\"password\":\""+MD5.getDefaultInstance().MD5Encode(password)+"\"");
				body.append("}");
				//加密
				String enStr = AESOperator.getInstance().encrypt(body.toString());
				String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
				if(reJson!=null){
					//解密
					JSONObject jsonObject = JSONObject.parseObject(reJson);
					String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
					LoginToken loginToken = JSONObject.parseObject(decrypt, LoginToken.class);
					if(loginToken.getErrorCode().equals(300000)){
						request.getSession().setAttribute("loginToken", loginToken);
						out.print("{\"status\":\"true\",\"errorCode\":\"0\",\"errorMsg\":\"登录成功\"}");						
					}else{
						out.print("{\"status\":\"false\",\"errorCode\":\"-100\",\"errorMsg\":\""+loginToken.getMessage()+"\"}");						
					}
				}else{
					out.print("{\"status\":\"false\",\"errorCode\":\"-101\",\"errorMsg\":\"网络连接超时\"}");						
				}
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":\"-102\",\"errorMsg\":\"用户名或密码不能为空\"}");						
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorCode\":\"-103\",\"errorMsg\":\"未知错误\"}");						

		}finally{
			out.flush();
			out.close();
		}
		return null;
	}
	@RequestMapping("managetoLogin")
	public ModelAndView toLogin(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("weixin/manage/login");
		return mv;
	}
	@RequestMapping("toOperation")
	public ModelAndView toOperation(HttpServletResponse response,HttpServletRequest request){
		ModelAndView mv = new ModelAndView();
		LoginToken loginToken = (LoginToken) request.getSession().getAttribute("loginToken");
		if(loginToken!=null){
			mv.setViewName("weixin/manage/operation-analysisNew");
		}else{
			mv.setViewName("redirect:/managetoLogin");			
		}
		return mv;
	}
	@RequestMapping("carparkAllQuery")
	public String carparkAllQuery(String token,HttpServletResponse response,HttpServletRequest request) throws IOException, NoSuchAlgorithmException{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String reqIp = Configuration.getInstance().getValue("hikip");
			String url = reqIp+"/GetAllParkingList";
			StringBuilder body = new StringBuilder();
			body.append("{");
			body.append("\"token\":\""+token+"\"");
			body.append("}");
			String enStr = AESOperator.getInstance().encrypt(body.toString());
			String reJson = HttpUtils.getInstance().requestPostJson(url,"{\"package\":\""+enStr+"\"}", 20);
			if(reJson!=null){
				JSONObject jsonObject = JSONObject.parseObject(reJson);
				String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
				CarparkAll carparkAll = JSONObject.parseObject(decrypt, CarparkAll.class);
				if(carparkAll.getErrorCode().equals(300000)){
					out.print(decrypt);
				}else{
					out.print("{\"status\":\"false\",\"errorCode\":\"-101\",\"errorMsg\":\""+carparkAll.getMessage()+"\"}");
				}
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":\"-1001\",\"errorMsg\":\"请求超时\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorCode\":\"-103\",\"errorMsg\":\"未知错误\"}");						

		}finally{
			out.flush();
			out.close();
		}
		return null;
	}
	
	
	
	
	@RequestMapping("parkIncomeQuery")
	public String parkIncomeQuery(String parkingCode,String parkingName,String beginTime,String endTime,Integer queryType,String token,HttpServletResponse response,HttpServletRequest request) throws IOException, NoSuchAlgorithmException{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String reqIp = Configuration.getInstance().getValue("hikip");
			if(parkingCode.equals("1")){
				String url = reqIp+"/GetAllParkingList";
				StringBuilder body = new StringBuilder();
				body.append("{");
				body.append("\"token\":\""+token+"\"");
				body.append("}");
				String enStr = AESOperator.getInstance().encrypt(body.toString());
				String reJson = HttpUtils.getInstance().requestPostJson(url,"{\"package\":\""+enStr+"\"}", 20);
				if(reJson!=null){
					JSONObject jsonObject = JSONObject.parseObject(reJson);
					String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
					CarparkAll carparkAll = JSONObject.parseObject(decrypt, CarparkAll.class);
					if(carparkAll.getErrorCode().equals(300000)){
						Integer totalNum = carparkAll.getTotalNum();
						SendSysIncome sendSysIncome = new SendSysIncome();
						sendSysIncome.setToken(token);
						sendSysIncome.setTotalNum(totalNum);
						List<SendCarpark> data = new ArrayList<SendCarpark>();
						List<Carpark> list = carparkAll.getData();
						for (Carpark carpark : list) {
							SendCarpark sendCarpark = new SendCarpark();
							sendCarpark.setBeginTime(beginTime);
							sendCarpark.setEndTime(endTime);
							sendCarpark.setParkingCode(carpark.getParkingCode());
							sendCarpark.setParkingName(carpark.getParkingName());
							sendCarpark.setQueryType(queryType);
							data.add(sendCarpark);
						}
						sendSysIncome.setData(data);
						String json = JSONObject.toJSONString(sendSysIncome);
						enStr = AESOperator.getInstance().encrypt(json);
						url = reqIp+"/EarnQuery";
						reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
						if(reJson!=null){
							jsonObject = JSONObject.parseObject(reJson);
							decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
							CarparkIncomes carparkIncomes = JSONObject.parseObject(decrypt, CarparkIncomes.class);
							if(carparkIncomes.getErrorCode().equals(300000)){
								out.print(decrypt);
							}else{
								out.print("{\"status\":\"false\",\"errorCode\":\"-101\",\"errorMsg\":\""+carparkIncomes.getMessage()+"\"}");
							}				
						}else{
							out.print("{\"status\":\"false\",\"errorCode\":\"-1001\",\"errorMsg\":\"请求超时\"}");
						}
					}else{
						out.print("{\"status\":\"false\",\"errorCode\":\"-101\",\"errorMsg\":\""+carparkAll.getMessage()+"\"}");
					}
				}else{
					out.print("{\"status\":\"false\",\"errorCode\":\"-1001\",\"errorMsg\":\"请求超时\"}");
				}
			}else{
				String url = reqIp+"/EarnQuery";
				SendSysIncome sendSysIncome = new SendSysIncome();
				sendSysIncome.setToken(token);
				sendSysIncome.setTotalNum(1);
				List<SendCarpark> data = new ArrayList<SendCarpark>();
				SendCarpark sendCarpark = new SendCarpark();
				sendCarpark.setBeginTime(beginTime);
				sendCarpark.setEndTime(endTime);
				sendCarpark.setParkingCode(parkingCode);
				sendCarpark.setParkingName(parkingName);
				sendCarpark.setQueryType(queryType);
				data.add(sendCarpark);
				sendSysIncome.setData(data);
				String json = JSONObject.toJSONString(sendSysIncome);
				String enStr = AESOperator.getInstance().encrypt(json);
				String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
				if(reJson!=null){
					JSONObject jsonObject = JSONObject.parseObject(reJson);
					String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
					CarparkIncomes carparkIncomes = JSONObject.parseObject(decrypt, CarparkIncomes.class);
					if(carparkIncomes.getErrorCode().equals(300000)){
						out.print(decrypt);
					}else{
						out.print("{\"status\":\"false\",\"errorCode\":\"-101\",\"errorMsg\":\""+carparkIncomes.getMessage()+"\"}");
					}				
				}else{
					out.print("{\"status\":\"false\",\"errorCode\":\"-1001\",\"errorMsg\":\"请求超时\"}");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorCode\":\"-103\",\"errorMsg\":\"未知错误\"}");						

		}finally{
			out.flush();
			out.close();
		}
		return null;
	}
	
	@RequestMapping("parkIncomePreQuery")
	public String parkIncomePreQuery(String beginTime,String endTime,Integer queryType,String token,HttpServletResponse response,HttpServletRequest request) throws IOException, NoSuchAlgorithmException{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String reqIp = Configuration.getInstance().getValue("hikip");
			String url = reqIp+"/GetAllParkingList";
			StringBuilder body = new StringBuilder();
			body.append("{");
			body.append("\"token\":\""+token+"\"");
			body.append("}");
			String enStr = AESOperator.getInstance().encrypt(body.toString());
			String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
			JSONObject jsonObject = JSONObject.parseObject(reJson);
			String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
			CarparkAll carparkAll = JSONObject.parseObject(decrypt, CarparkAll.class);
			if(carparkAll.getErrorCode().equals(300000)){
				List<Carpark> carparks = carparkAll.getData();
				if(carparks!=null && carparks.size()>0){
					url = reqIp+"/EarnQuery";
					SendSysIncome sendSysIncome = new SendSysIncome();
					sendSysIncome.setToken(token);
					sendSysIncome.setTotalNum(carparks.size());
					List<SendCarpark> data = new ArrayList<SendCarpark>();
					for (Carpark carpark : carparks) {
						SendCarpark sendCarpark = new SendCarpark();
						sendCarpark.setBeginTime(beginTime);
						sendCarpark.setEndTime(endTime);
						sendCarpark.setParkingCode(carpark.getParkingCode());
						sendCarpark.setParkingName(carpark.getParkingName());
						sendCarpark.setQueryType(queryType);
						data.add(sendCarpark);
					}
					sendSysIncome.setData(data);
					String json = JSONObject.toJSONString(sendSysIncome);
					enStr = AESOperator.getInstance().encrypt(json);
					reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
					if(reJson!=null){
						jsonObject = JSONObject.parseObject(reJson);
						decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
						CarparkIncomes carparkIncomes = JSONObject.parseObject(decrypt, CarparkIncomes.class);
						if(carparkIncomes.getErrorCode().equals(300000)){
							List<CarparkIncome> carparkIncome = carparkIncomes.getData();
							StringBuilder print = new StringBuilder();
							if(carparkIncome.size()>0){
								for (int i = 0; i < carparkIncome.size(); i++) {
									print.append("{");
									print.append("\"parkingCode\":\""+carparkIncome.get(i).getParkingCode()+"\",");
									print.append("\"parkingName\":\""+carparkIncome.get(i).getParkingName()+"\",");
									print.append("\"pre\":\""+(carparkIncomes.getTotalShouldPay()>0 && carparkIncome.get(i).getAllPay()>0?Float.valueOf(carparkIncome.get(i).getAllPay())/carparkIncomes.getTotalShouldPay():0)+"\"");
									print.append("}");
									if(i!=carparkIncome.size()-1){
										print.append(",");
									}
								}
								
							}
							out.print("{\"status\":\"true\",\"errorCode\":\"0\",\"errorMsg\":\"查询成功\",\"data\":["+print.toString()+"]}");
							
						}else{
							out.print("{\"status\":\"false\",\"errorCode\":\"-101\",\"errorMsg\":\"收益信息获取失败\"}");
						}
					}else{
						out.print("{\"status\":\"false\",\"errorCode\":\"-1001\",\"errorMsg\":\"请求超时\"}");
					}
				}
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":\"-101\",\"errorMsg\":\"获取停车场失败\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorCode\":\"-103\",\"errorMsg\":\"未知错误\"}");						

		}finally{
			out.flush();
			out.close();
		}
		return null;
	}
	
	
	@RequestMapping("occupyPreQuery")
	public String occupyPreQuery(String beginTime,String endTime,Integer queryType,String token,HttpServletResponse response,HttpServletRequest request) throws IOException, NoSuchAlgorithmException{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String reqIp = Configuration.getInstance().getValue("hikip");
			String url = reqIp+"/GetAllParkingList";
			StringBuilder body = new StringBuilder();
			body.append("{");
			body.append("\"token\":\""+token+"\"");
			body.append("}");
			String enStr = AESOperator.getInstance().encrypt(body.toString());
			String reJson = HttpUtils.getInstance().requestPostJson(url,"{\"package\":\""+enStr+"\"}", 20);
			JSONObject jsonObject = JSONObject.parseObject(reJson);
			String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
			CarparkAll carparkAll = JSONObject.parseObject(decrypt, CarparkAll.class);
			if(carparkAll.getErrorCode().equals(300000)){
				List<Carpark> carparks = carparkAll.getData();
				if(carparks!=null && carparks.size()>0){
					url = reqIp+"/OccupyQuery";
					SendSysIncome sendSysIncome = new SendSysIncome();
					sendSysIncome.setToken(token);
					sendSysIncome.setTotalNum(carparks.size());
					List<SendCarpark> data = new ArrayList<SendCarpark>();
					for (Carpark carpark : carparks) {
						SendCarpark sendCarpark = new SendCarpark();
						sendCarpark.setBeginTime(beginTime);
						sendCarpark.setEndTime(endTime);
						sendCarpark.setParkingCode(carpark.getParkingCode());
						sendCarpark.setParkingName(carpark.getParkingName());
						sendCarpark.setQueryType(queryType);
						data.add(sendCarpark);
					}
					sendSysIncome.setData(data);
					String json = JSONObject.toJSONString(sendSysIncome);
					enStr = AESOperator.getInstance().encrypt(json);
					reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
					if(reJson!=null){
						jsonObject = JSONObject.parseObject(reJson);
						decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
						CarparkOccupyPre carparkOccupyPre = JSONObject.parseObject(decrypt, CarparkOccupyPre.class);
						if(carparkOccupyPre.getErrorCode().equals(300000)){
							out.print(decrypt);
						}else{
							out.print("{\"status\":\"false\",\"errorCode\":\"-101\",\"errorMsg\":\"收益信息获取失败\"}");
						}
					}else{
						out.print("{\"status\":\"false\",\"errorCode\":\"-1001\",\"errorMsg\":\"请求超时\"}");
					}
				}
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":\"-101\",\"errorMsg\":\"获取停车场失败\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			out.print("{\"status\":\"false\",\"errorCode\":\"-103\",\"errorMsg\":\"未知错误\"}");						
		}finally{
			out.flush();
			out.close();
		}
		return null;
	}
	
	@RequestMapping("usagePreQuery")
	public String usagePreQuery(String beginTime,String endTime,Integer queryType,String token,HttpServletResponse response,HttpServletRequest request) throws IOException, NoSuchAlgorithmException{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String reqIp = Configuration.getInstance().getValue("hikip");
			String url = reqIp+"/GetAllParkingList";
			StringBuilder body = new StringBuilder();
			body.append("{");
			body.append("\"token\":\""+token+"\"");
			body.append("}");
			String enStr = AESOperator.getInstance().encrypt(body.toString());
			String reJson = HttpUtils.getInstance().requestPostJson(url,"{\"package\":\""+enStr+"\"}", 20);
			JSONObject jsonObject = JSONObject.parseObject(reJson);
			String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
			CarparkAll carparkAll = JSONObject.parseObject(decrypt, CarparkAll.class);
			if(carparkAll.getErrorCode().equals(300000)){
				List<Carpark> carparks = carparkAll.getData();
				if(carparks!=null && carparks.size()>0){
					url = reqIp+"/UsageQuery";
					SendSysIncome sendSysIncome = new SendSysIncome();
					sendSysIncome.setToken(token);
					sendSysIncome.setTotalNum(carparks.size());
					List<SendCarpark> data = new ArrayList<SendCarpark>();
					for (Carpark carpark : carparks) {
						SendCarpark sendCarpark = new SendCarpark();
						sendCarpark.setBeginTime(beginTime);
						sendCarpark.setEndTime(endTime);
						sendCarpark.setParkingCode(carpark.getParkingCode());
						sendCarpark.setParkingName(carpark.getParkingName());
						sendCarpark.setQueryType(queryType);
						data.add(sendCarpark);
					}
					sendSysIncome.setData(data);
					String json = JSONObject.toJSONString(sendSysIncome);
					enStr = AESOperator.getInstance().encrypt(json);
					reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
					if(reJson!=null){
						jsonObject = JSONObject.parseObject(reJson);
						decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
						CarparkUsagePre carparkUsagePre = JSONObject.parseObject(decrypt, CarparkUsagePre.class);
						if(carparkUsagePre.getErrorCode().equals(300000)){
							out.print(decrypt);
						}else{
							out.print("{\"status\":\"false\",\"errorCode\":\"-101\",\"errorMsg\":\"收益信息获取失败\"}");
						}
					}else{
						out.print("{\"status\":\"false\",\"errorCode\":\"-1001\",\"errorMsg\":\"请求超时\"}");
					}
				}
			}else{
				out.print("{\"status\":\"false\",\"errorCode\":\"-101\",\"errorMsg\":\"获取停车场失败\"}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorCode\":\"-103\",\"errorMsg\":\"未知错误\"}");						

		}finally{
			out.flush();
			out.close();
		}
		return null;
	}
	
	
	@RequestMapping("flowQuery")
	public String flowQuery(String parkingCode,String parkingName,String beginTime,String endTime,Integer queryType,String token,HttpServletResponse response,HttpServletRequest request) throws IOException, NoSuchAlgorithmException{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String reqIp = Configuration.getInstance().getValue("hikip");
			if(parkingCode.equals("1")){
				String url = reqIp+"/GetAllParkingList";
				StringBuilder body = new StringBuilder();
				body.append("{");
				body.append("\"token\":\""+token+"\"");
				body.append("}");
				String enStr = AESOperator.getInstance().encrypt(body.toString());
				String reJson = HttpUtils.getInstance().requestPostJson(url,"{\"package\":\""+enStr+"\"}", 20);
				if(reJson!=null){
					JSONObject jsonObject = JSONObject.parseObject(reJson);
					String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
					CarparkAll carparkAll = JSONObject.parseObject(decrypt, CarparkAll.class);
					if(carparkAll.getErrorCode().equals(300000)){
						Integer totalNum = carparkAll.getTotalNum();
						SendSysIncome sendSysIncome = new SendSysIncome();
						sendSysIncome.setToken(token);
						sendSysIncome.setTotalNum(totalNum);
						List<SendCarpark> data = new ArrayList<SendCarpark>();
						List<Carpark> list = carparkAll.getData();
						for (Carpark carpark : list) {
							SendCarpark sendCarpark = new SendCarpark();
							sendCarpark.setBeginTime(beginTime);
							sendCarpark.setEndTime(endTime);
							sendCarpark.setParkingCode(carpark.getParkingCode());
							sendCarpark.setParkingName(carpark.getParkingName());
							sendCarpark.setQueryType(queryType);
							data.add(sendCarpark);
						}
						sendSysIncome.setData(data);
						String json = JSONObject.toJSONString(sendSysIncome);
						enStr = AESOperator.getInstance().encrypt(json);
						url = reqIp+"/UsageQuery";
						reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
						if(reJson!=null){
							jsonObject = JSONObject.parseObject(reJson);
							decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
							ParkFlowList flowList = JSONObject.parseObject(decrypt, ParkFlowList.class);
							if(flowList.getErrorCode().equals(300000)){
								out.print(decrypt);
							}else{
								out.print("{\"status\":\"false\",\"errorCode\":\"-102\",\"errorMsg\":\"进出信息获取失败\"}");
							}			
						}else{
							out.print("{\"status\":\"false\",\"errorCode\":\"-1001\",\"errorMsg\":\"请求超时\"}");
						}
					}else{
						out.print("{\"status\":\"false\",\"errorCode\":\"-101\",\"errorMsg\":\""+carparkAll.getMessage()+"\"}");
					}
				}else{
					out.print("{\"status\":\"false\",\"errorCode\":\"-1001\",\"errorMsg\":\"请求超时\"}");
				}
			}else{
				String url = reqIp+"/UsageQuery";
				SendSysIncome sendSysIncome = new SendSysIncome();
				sendSysIncome.setToken(token);
				sendSysIncome.setTotalNum(1);
				List<SendCarpark> data = new ArrayList<SendCarpark>();
				SendCarpark sendCarpark = new SendCarpark();
				sendCarpark.setBeginTime(beginTime);
				sendCarpark.setEndTime(endTime);
				sendCarpark.setParkingCode(parkingCode);
				sendCarpark.setParkingName(parkingName);
				sendCarpark.setQueryType(queryType);
				data.add(sendCarpark);
				sendSysIncome.setData(data);
				String json = JSONObject.toJSONString(sendSysIncome);
				String enStr = AESOperator.getInstance().encrypt(json);
				String reJson = HttpUtils.getInstance().requestPostJson(url,"{\"package\":\""+enStr+"\"}", 20);
				if(reJson!=null){
					JSONObject jsonObject = JSONObject.parseObject(reJson);
					String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
					ParkFlowList flowList = JSONObject.parseObject(decrypt, ParkFlowList.class);
					if(flowList.getErrorCode().equals(300000)){
						out.print(decrypt);
					}else{
						out.print("{\"status\":\"false\",\"errorCode\":\"-102\",\"errorMsg\":\"进出信息获取失败\"}");
					}
				}else{
					out.print("{\"status\":\"false\",\"errorCode\":\"-1001\",\"errorMsg\":\"请求超时\"}");
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"errorCode\":\"-103\",\"errorMsg\":\"未知错误\"}");						

		}finally{
			out.flush();
			out.close();
		}
		return null;
	}
	
	

}
