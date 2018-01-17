package com.parkbobo.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
import com.parkbobo.model.VehicleCostWechat;
import com.parkbobo.service.CarparkService;
import com.parkbobo.service.ParkPassVehicleMonthService;
import com.parkbobo.service.VehicleCostWechatService;
import com.parkbobo.utils.AESOperator;
import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.HttpRequest;
import com.parkbobo.utils.StringUtils;
import com.parkbobo.utils.httpmodel.BillInfo;

/**
 * 向海康提供的获取订单接口
 * @author RY
 * @version 1.0
 * @since 2017-6-27 14:22:54
 *
 */

@Controller
public class GetBillInfoControllor {
	@Resource(name="vehicleCostWechatService")
	private VehicleCostWechatService vehicleCostWechatService;
	@Resource(name="parkPassVehicleMonthService")
	private ParkPassVehicleMonthService parkPassVehicleMonthService;
	@Resource
	private CarparkService carparkService;
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@RequestMapping("GetBillInfo")
	public String passVehicle(HttpServletResponse response,HttpServletRequest request) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			ServletInputStream is = request.getInputStream();
			String param = StringUtils.getDefaultInstance().ins2str(is);
			
			StringBuffer s = new StringBuffer();
			StringBuffer tmpStr = new StringBuffer();
			if(!StringUtils.getDefaultInstance().isNoBlank(param)){
				String reStr = "{\"message\": \"请求参数不正确\",\"errorCode\": 300009,\"token\": \"\"}";
				String encrypt = AESOperator.getInstance().encrypt(reStr);
//				System.out.println(encrypt);
				out.print("{\"package\": \""+encrypt+"\"}");
			}else{
				JSONObject parseObject = JSONObject.parseObject(param);
				String enStr = parseObject.getString("package");
				String praJson = AESOperator.getInstance().decrypt(enStr);
//				System.out.println("订单请求信息："+praJson);
				BillInfo bi = JSONObject.parseObject(praJson, BillInfo.class);
				if(bi!=null){
					String parkId = bi.getParkId();
					Carpark carpark = carparkService.thIdToPark(parkId);
					if(carpark!=null){
						
						ParkPassVehicleMonth ppv = parkPassVehicleMonthService.loadByInunid(bi.getInUnid());
						String inTime = SDF.format(ppv.getPassTime());
						
						String hql = "from VehicleCostWechat where inUnid = '" + bi.getInUnid() + "' and status = 1 order by planPayTime desc";
						List<VehicleCostWechat> vcwList = vehicleCostWechatService.getByHql(hql);
						
						for(VehicleCostWechat vcw : vcwList){
							tmpStr.append("{");
							tmpStr.append("\"payUnid\":\"" + vcw.getPayUnid() + "\",");
							tmpStr.append("\"payType\":3,");
							tmpStr.append("\"payStatus\":1,");
							tmpStr.append("\"payTime\":\"" + SDF.format(vcw.getPlanPayTime()) + "\",");
							tmpStr.append("\"payMoney\":" + vcw.getShouldPayMoney() + "");
							tmpStr.append("},");
						}
						if(tmpStr.length() > 0){
							tmpStr.deleteCharAt(tmpStr.length() - 1);
						}
						s.append("{");
						s.append("\"message\": \"success\",");
						s.append("\"errorCode\": 300000,");
						s.append("\"parkingCode\":\"" + bi.getParkId() + "\",");
						s.append("\"plateInfo\":\"" + bi.getCarPlate() + "\",");
						s.append("\"plateColor\":\"" + bi.getPlateColor() + "\",");
						s.append("\"inTime\":\"" + inTime + "\",");
						s.append("\"billData\":[" + tmpStr + "]");
						s.append("}");
//						System.out.println("获取订单成功");
//						System.out.println(s.toString());
						String encrypt = AESOperator.getInstance().encrypt(s.toString());
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

}
