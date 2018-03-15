package com.parkbobo.utils.httpmodel;

import java.io.Serializable;
import java.util.List;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.parkbobo.utils.AESOperator;
import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.http.HttpUtils;

public class MonthlyVehicleRequest implements Serializable {

	/**
	 * 月租续费请求操作
	 */
	private static final long serialVersionUID = 2106677633338644461L;
	/**
	 * 查询是否包期车
	 * */
	public static QueryVehicleMonthly queryVehicleMonthly(String parkingCode,String plateNo,Integer plateColor) throws Exception{
		QueryVehicleMonthly vehicleMonthly = null;
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
			JSONObject decObject = JSONObject.parseObject(decrypt);
			Integer errorCode = decObject.getInteger("errorCode");
			if(errorCode.equals(300000) && decObject.getInteger("isBagVehicle").equals(1)){
				vehicleMonthly = new QueryVehicleMonthly();
				vehicleMonthly.setIsBagVehicle(decObject.getInteger("isBagVehicle"));
				vehicleMonthly.setRuleId(decObject.getInteger("ruleId"));
				vehicleMonthly.setRuleType(decObject.getInteger("ruleType"));
				vehicleMonthly.setPayPee(decObject.getInteger("payPee"));
				vehicleMonthly.setEndTime(decObject.getString("endTime"));
				vehicleMonthly.setRuleName(decObject.getString("ruleName"));
			}else{
				System.out.println(decrypt);
			}
		}else{
			System.out.println("请求失败");
		}
		return vehicleMonthly;
	}
	/**
	 * 查询包期规则
	 * @throws Exception 
	 * */
	public static List<MonthlypaymentRule> getMonthlypaymentRule(String parkingCode) throws Exception{
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
			JSONObject decObject = JSONObject.parseObject(decrypt);
			Integer errorCode = decObject.getInteger("errorCode");
			if(errorCode.equals(300000)){
				String dataArray = decObject.getString("data");
				List<MonthlypaymentRule> list = JSON.parseArray(dataArray, MonthlypaymentRule.class);
				return list;
			}
			return null;
		}else{
			return null;
		}
	}

}
