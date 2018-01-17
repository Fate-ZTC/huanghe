package com.parkbobo.quartz.task;

import java.text.ParseException;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.parkbobo.model.Carpark;
import com.parkbobo.service.CarparkService;
import com.parkbobo.utils.AESOperator;
import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.HttpRequest;


public class GetThreeParkingTask {
	/**
	 * 启动任务
	 * 
	 * */
	@Resource(name="carparkService")
	private CarparkService carparkService;
	public void startJob() throws ParseException
	{
		getThreeCarprkInfoTask();
	}
	/**
	 * 
	 * 
	 * */
	@SuppressWarnings("deprecation")
	private void getThreeCarprkInfoTask() {
		try {
			String hikDomain = Configuration.getInstance().getValue("hikip");
			String enStr = AESOperator.getInstance().encrypt("{\"version\":\"V1.0\"}");
			String sendPost = HttpRequest.sendPost(hikDomain+"/GetAllParkingList", "{\"package\":\""+enStr+"\"}");
			if(sendPost!=null && !"".equals(sendPost) && !"null".equals(sendPost)){
				JSONObject jsonObject = JSONObject.fromObject(sendPost);
				String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
				JSONObject within = JSONObject.fromObject(decrypt);
				Object errorCode = within.get("errorCode");
				System.out.println("停车场列表=========================");
				System.out.println(decrypt);
				if(errorCode!=null){
					if(300000==((Integer)errorCode)){
						//返回成功
						StringBuffer sb = new StringBuffer();
						JSONArray array = (JSONArray) within.get("data");
						sb.append("{");
						sb.append("\"version\": \"V1.0\",");
						sb.append("\"totalNum\": \""+array.size()+"\",");
						sb.append("\"data\": [");
						for (int i = 0; i < array.size(); i++) {
							JSONObject jsonObject2 = array.getJSONObject(i);
							Object parkingCode = jsonObject2.get("parkingCode");
							sb.append("{");
							sb.append("\"parkingCode\": \""+parkingCode+"\"");
							if(array.size()-1 == i){
								sb.append("}");
							}else{
								sb.append("},");					
							}
						}
						sb.append("]}");
						String ret = AESOperator.getInstance().encrypt(sb.toString());
						sendPost = HttpRequest.sendPost(hikDomain+"/GetParkingInfo","{\"package\":\""+ret+"\"}");
						System.out.println("=====================停车场详情==========");
						System.out.println(sendPost);
						
						
						if(sendPost!=null && !"".equals(sendPost) && !"null".equals(sendPost)){
							jsonObject = JSONObject.fromObject(sendPost);
							decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
							within = JSONObject.fromObject(decrypt);
							System.out.println(decrypt);
							errorCode = within.get("errorCode");
							if(errorCode!=null){
								if(300000==((Integer)errorCode)){
									//返回成功
									array = (JSONArray) within.get("data");
									for (int i = 0; i < array.size(); i++) {
										JSONObject jsonObject2 = array.getJSONObject(i);
										Object leftBerthNum = jsonObject2.get("LeftBerthNum");
										Object address = jsonObject2.get("address");
										Object desc = jsonObject2.get("desc");
										String latitude = String.valueOf(jsonObject2.get("latitude"));
										String longitude = String.valueOf(jsonObject2.get("longitude"));
										Object operationState = jsonObject2.get("operationState");
										String parkingCode = jsonObject2.getString("parkingCode");
										Object parkingLevel = jsonObject2.get("parkingLevel");
										String parkingName = jsonObject2.getString("parkingName");
										Object parkingType = jsonObject2.get("parkingType");
										Object totalBerthNum = jsonObject2.get("totalBerthNum");
										Carpark carpark = carparkService.nameToPark(parkingName);
										if(carpark!=null){
											if(leftBerthNum!=null){
												carpark.setEnableBerth((Integer)leftBerthNum);
											}
											if(address!=null){
												String addr = (String)address;
												if(StringUtils.isNotBlank(addr)){
													carpark.setAddress(addr);
												}
											}
											if(StringUtils.isNotBlank(latitude)){
												String lat = (String) latitude;
												carpark.setLatitude(Double.parseDouble(lat));
											}
											if(StringUtils.isNotBlank(longitude)){
												String lon = (String) longitude;
												carpark.setLongitude(Double.parseDouble(lon));
											}
											if(totalBerthNum!=null){
												carpark.setTotalBerth((Integer) totalBerthNum);
											}
											carpark.setThirdId(parkingCode);
											carparkService.merge(carpark);
										}else{
											carpark = new Carpark();
											if(leftBerthNum!=null){
												carpark.setEnableBerth((Integer)leftBerthNum);
											}
											if(address!=null){
												String addr = (String)address;
												if(StringUtils.isNotBlank(addr)){
													carpark.setAddress(addr);
												}
											}
											if(StringUtils.isNotBlank(latitude)){
												String lat = (String) latitude;
												carpark.setLatitude(Double.parseDouble(lat));
											}
											if(StringUtils.isNotBlank(longitude)){
												String lon = (String) longitude;
												carpark.setLongitude(Double.parseDouble(lon));
											}
											if(totalBerthNum!=null){
												carpark.setTotalBerth((Integer) totalBerthNum);
											}
											carpark.setThirdId(parkingCode);
											carparkService.add(carpark);
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public static void main(String[] args) {
		/*try {
			String sendPost = HttpRequest.sendPost("http://183.221.196.231:18080/GetAllParkingList", "{\"version\":\"V1.0\"}");
			System.out.println(sendPost);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		
		
		try {
			String hikDomain = "http://183.221.196.231:18080";
			String enStr = AESOperator.getInstance().encrypt("{\"version\":\"V1.0\"}");
			String sendPost = HttpRequest.sendPost(hikDomain+"/GetAllParkingList", "{\"package\":\""+enStr+"\"}");
			if(sendPost!=null && !"".equals(sendPost) && !"null".equals(sendPost)){
				JSONObject jsonObject = JSONObject.fromObject(sendPost);
				String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
				JSONObject within = JSONObject.fromObject(decrypt);
				Object errorCode = within.get("errorCode");
				System.out.println(decrypt);
				if(errorCode!=null){
					if(300000==((Integer)errorCode)){
						//返回成功
						StringBuffer sb = new StringBuffer();
						JSONArray array = (JSONArray) within.get("data");
						sb.append("{");
						sb.append("\"version\": \"V1.0\",");
						sb.append("\"totalNum\": \""+array.size()+"\",");
						sb.append("\"data\": [");
						for (int i = 0; i < array.size(); i++) {
							JSONObject jsonObject2 = array.getJSONObject(i);
							Object parkingCode = jsonObject2.get("parkingCode");
							sb.append("{");
							sb.append("\"parkingCode\": \""+parkingCode+"\"");
							if(array.size()-1 == i){
								sb.append("}");
							}else{
								sb.append("},");					
							}
						}
						sb.append("]}");
						String ret = AESOperator.getInstance().encrypt(sb.toString());
						sendPost = HttpRequest.sendPost(hikDomain+"/GetParkingInfo","{\"package\":\""+ret+"\"}");
						System.out.println("=====================停车场详情==========");
						System.out.println(sendPost);
						
						
						if(sendPost!=null && !"".equals(sendPost) && !"null".equals(sendPost)){
							jsonObject = JSONObject.fromObject(sendPost);
							decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
							within = JSONObject.fromObject(decrypt);
							System.out.println(decrypt);
							errorCode = within.get("errorCode");
							if(errorCode!=null){
								if(300000==((Integer)errorCode)){
									//返回成功
									array = (JSONArray) within.get("data");
									for (int i = 0; i < array.size(); i++) {
										JSONObject jsonObject2 = array.getJSONObject(i);
										Object leftBerthNum = jsonObject2.get("LeftBerthNum");
										Object address = jsonObject2.get("address");
										Object desc = jsonObject2.get("desc");
										String latitude = String.valueOf(jsonObject2.get("latitude"));
										String longitude = String.valueOf(jsonObject2.get("longitude"));
										Object operationState = jsonObject2.get("operationState");
										String parkingCode = jsonObject2.getString("parkingCode");
										Object parkingLevel = jsonObject2.get("parkingLevel");
										String parkingName = jsonObject2.getString("parkingName");
										Object parkingType = jsonObject2.get("parkingType");
										Object totalBerthNum = jsonObject2.get("totalBerthNum");
										System.out.println(parkingName);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
