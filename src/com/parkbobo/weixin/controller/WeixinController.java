package com.parkbobo.weixin.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.HttpRequest;
import com.parkbobo.utils.SHA1;
import com.parkbobo.utils.weixin.Location;

@Controller
public class WeixinController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4587798744919627352L;
	private static final String TOKEN = "91510502356248883E";
	
	@RequestMapping("wxPort_verify")
	public String weixinPort(String timestamp,String nonce,String signature,String echostr,HttpServletResponse response,HttpServletRequest request) throws IOException, NoSuchAlgorithmException{
		ServletInputStream in = request.getInputStream();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while ((line = br.readLine())!=null) {
			sb.append(line);
		}
		String xmltext = sb.toString();
		if(StringUtils.isNotBlank(xmltext)){
			String event = this.extract(xmltext, "Event");
			System.out.println(event);
			if(event.equals("LOCATION")){
				Location location = new Location(xmltext);
				StringBuilder sbs = new StringBuilder();
				sbs.append("{\"openid\":\""+location.getFromUserName()+"\",");
				sbs.append("\"subscribe\":\"\",");
				sbs.append("\"latitude\":\""+location.getLatitude()+"\",");
				sbs.append("\"longitude\":\""+location.getLongitude()+"\",");
				sbs.append("\"nickname\":\"\",");
				sbs.append("\"wxHeadimgurl\":\"\"}");
				//String sendGet = HttpRequest.sendPost(requestUrl+"/wxUsers_save", sbs.toString());
				//System.out.println(sendGet);
			}
		}else{
			String[] str = {TOKEN,timestamp,nonce};
			Arrays.sort(str); // 字典序排序
			String bigStr = str[0] + str[1] + str[2];        // SHA1加密
			String digest = new SHA1().getDigestOfString(bigStr.getBytes()).toLowerCase();        
			// 确认请求来至微信
			if (digest.equals(signature)) {
				response.getWriter().print(echostr);
			}
		}
		return null;
	}
	
	
	private String extract(String xmltext,String xmlName)  {
		String result = "";
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader sr = new StringReader(xmltext);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);

			Element root = document.getDocumentElement();
			NodeList nodelist1 = root.getElementsByTagName(xmlName);
			result = nodelist1.item(0).getTextContent();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
