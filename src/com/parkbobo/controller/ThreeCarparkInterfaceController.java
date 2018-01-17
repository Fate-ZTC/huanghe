package com.parkbobo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.HttpRequest;

@Controller
public class ThreeCarparkInterfaceController {
	
	@RequestMapping("threeCarparkInterface_getEnLe")
	public void getEnLe(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException{
//		response.setContentType("text/html;charset=utf-8");
//		PrintWriter out = response.getWriter();
//		try {
//			String requestUrl = Configuration.getInstance().getValue("requestUrl");
//			ServletInputStream is = request.getInputStream();
//			StringBuilder sb = new StringBuilder();
//			InputStreamReader isr = new InputStreamReader(is,"UTF-8");
//			BufferedReader br = new BufferedReader(isr);
//			String line;
//			while ((line = br.readLine()) != null) {
//				sb.append(line);
//			}
//			String requ = sb.toString();
//			String sendGet = HttpRequest.sendPost(requestUrl+"/redOrder_save", requ);
//			out.print(sendGet);
//		} catch (Exception e) {
//			out.print("{\"head\": {\"code\": \"C0002\",\"msg\": \"数据参数有误\"}}");
//		}
	}
}


