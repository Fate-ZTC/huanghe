package com.parkbobo.weixin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.parkbobo.model.CarImportLog;
import com.parkbobo.model.Carpark;
import com.parkbobo.model.CarparkBerth;
import com.parkbobo.model.ParkPassVehicle;
import com.parkbobo.model.ParkPassVehicleMonth;
import com.parkbobo.service.CarExitLogService;
import com.parkbobo.service.CarImportLogService;
import com.parkbobo.service.CarparkBerthService;
import com.parkbobo.service.CarparkService;
import com.parkbobo.service.ParkPassVehicleMonthService;
import com.parkbobo.service.ParkPassVehicleService;
import com.parkbobo.service.WeixinConfigService;
import com.parkbobo.utils.AESOperator;
import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.HttpRequest;
import com.parkbobo.utils.http.HttpUtils;
import com.parkbobo.utils.httpmodel.VehicleOrder;

@Controller
public class WeixinSearchCarNumberController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4587798744919627352L;
	@Resource
	private CarparkBerthService carparkBerthService;
	@Resource
	private ParkPassVehicleMonthService parkPassVehicleMonthService;
	@Resource
	private CarparkService carparkService;
	@Resource
	private WeixinConfigService weixinConfigService;
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@RequestMapping("wxSearchCarNumber_toSearch")
	public ModelAndView toSearch(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("weixin/hik/search");
		return mv;
	}
	@RequestMapping("carNumberSer")
	public String carNumberSer(String carNumber,HttpServletResponse response,HttpServletRequest request) throws IOException, NoSuchAlgorithmException{
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = null;
		try {
			carNumber = URLDecoder.decode(URLEncoder.encode(carNumber, "ISO8859_1"), "UTF-8");
			out = response.getWriter();
			ParkPassVehicleMonth parkPassVehicle = parkPassVehicleMonthService.retentionCar(carNumber);
			if(parkPassVehicle!=null){
				StringBuffer sb = new StringBuffer();
				Long carparkid = parkPassVehicle.getCarparkid();
				Carpark carpark = carparkService.get(carparkid);
				if(carpark!=null){
					String name = carpark.getName();
					String parkCode = carpark.getThirdId();
					String reqIp = Configuration.getInstance().getValue("hikip");
					String url = reqIp+"/GetVehicleCost";
					StringBuilder body = new StringBuilder();
					body.append("{");
					body.append("\"token\":\"\",");
					body.append("\"parkingCode\":\""+parkCode+"\",");
					body.append("\"plateInfo\":\""+parkPassVehicle.getCarplate()+"\",");
					body.append("\"plateColor\":"+parkPassVehicle.getPlatecolor()+",");
					body.append("\"carType\":"+parkPassVehicle.getCartype()+",");
					body.append("\"inUnid\":\""+parkPassVehicle.getInUnid()+"\",");
					body.append("\"inTime\":\""+SDF.format(parkPassVehicle.getPassTime())+"\"");
					body.append("}");
					String enStr = AESOperator.getInstance().encrypt(body.toString());
				    String reJson = HttpUtils.getInstance().requestPostJson(url, "{\"package\":\""+enStr+"\"}", 20);
				    Integer shouldPayMoney = 0;
				    if(reJson!=null){
				    	JSONObject jsonObject = JSONObject.parseObject(reJson);
						String decrypt = AESOperator.getInstance().decrypt(jsonObject.getString("package"));
						System.out.println("停车费用信息：" + decrypt);
						VehicleOrder vo = JSONObject.parseObject(decrypt, VehicleOrder.class);
						if(vo.getErrorCode().equals(300000)){
							shouldPayMoney = vo.getShouldPayMoney();
						}
				    }
			        CarparkBerth carparkBerth = this.carparkBerthService.newBerth(parkPassVehicle.getCarplate(),this.formatDateStr(parkPassVehicle.getPassTime().getTime()));
			        sb.append(" {\"status\": \"true\",\"para\": {");
			        sb.append("\"carNumber\": \""+parkPassVehicle.getCarplate()+"\",");
			        sb.append("\"carparkName\": \""+name+"\",");
			        sb.append("\"berthNum\": \""+(carparkBerth==null?"":carparkBerth.getSpotId())+"\",");
			        sb.append("\"enterTime\": \""+this.formatDateStr(parkPassVehicle.getPassTime().getTime())+"\",");
			        sb.append("\"timed\": \""+this.formatMillisecondToHour(new Date().getTime()-(parkPassVehicle.getPassTime().getTime()))+"\",");
			        sb.append("\"shouldPayMoney\": \""+(shouldPayMoney!=0?String.format("%.2f", shouldPayMoney/100d):0)+"\",");
			        sb.append("\"parktype\": \""+(parkPassVehicle.getParktype()==null?"":this.parktypeToStr(parkPassVehicle.getParktype()))+"\",");//1：临时车，2：包白天，3：包晚上，4：包全天
			        sb.append("\"carNumImgPath\": \""+(carparkBerth==null?"":carparkBerth.getImageUrl())+"\"");
			        sb.append("}}");
			        out.print(sb.toString());
				}else{
					out.print("{\"status\":\"false\",\"error\":\"C1\"}");	
				}
			}else{
				out.print("{\"status\":\"false\",\"error\":\"C1\"}");	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"status\":\"false\",\"error\":\"C1\"}");		
		}
		return null;
	}
	
	private String formatMillisecondToHour(Long millisecond)
	{
		if((millisecond % (1000L * 60 * 60)) == 0)
		{
			return millisecond / (1000L * 60 * 60) + "小时";
		}
		else
		{
			if(millisecond < 1000l * 60 * 60)
			{
				return (millisecond % (1000L * 60 * 60)) / (1000L * 60) + "分钟";
			}
			else
			{
				return millisecond / (1000L * 60 * 60) + "小时" 
				+ (millisecond % (1000L * 60 * 60)) / (1000L * 60) + "分钟";
			}
		}
	}
	
	private String formatDateStr(Long millisecond)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(millisecond);
		return sdf.format(date);
	}
	private String parktypeToStr(Integer parkType){
		if(parkType.equals(1)){
			return "临时车";
		}
		if(parkType.equals(2)){
			return "包白天";
		}
		if(parkType.equals(3)){
			return "包晚上";
		}
		if(parkType.equals(4)){
			return "包全天";
		}
		return "";
	}
}
