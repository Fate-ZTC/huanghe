package com.parkbobo.utils.weixin;

import java.io.Serializable;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Location implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8818287591028840423L;
	
	private String toUserName;
	private String fromUserName;
	private String createTime;
	private String msgType;
	private String event;
	private String latitude;
	private String longitude;
	private String precision;
	
	public Location(String xml){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader sr = new StringReader(xml);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);
			Element root = document.getDocumentElement();
			NodeList toUserName = root.getElementsByTagName("ToUserName");
			NodeList fromUserName = root.getElementsByTagName("FromUserName");
			NodeList createTime = root.getElementsByTagName("CreateTime");
			NodeList msgType = root.getElementsByTagName("MsgType");
			NodeList event = root.getElementsByTagName("Event");
			NodeList latitude = root.getElementsByTagName("Latitude");
			NodeList longitude = root.getElementsByTagName("Longitude");
			NodeList precision = root.getElementsByTagName("Precision");
			this.toUserName = toUserName.item(0).getTextContent();
			this.fromUserName = fromUserName.item(0).getTextContent();
			this.createTime = createTime.item(0).getTextContent();
			this.msgType = msgType.item(0).getTextContent();
			this.event = event.item(0).getTextContent();
			this.latitude = latitude.item(0).getTextContent();
			this.longitude = longitude.item(0).getTextContent();
			this.precision = precision.item(0).getTextContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	@Override
	public String toString() {
		return "Location [createTime=" + createTime + ", event=" + event
				+ ", fromUserName=" + fromUserName + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", msgType=" + msgType
				+ ", precision=" + precision + ", toUserName=" + toUserName
				+ "]";
	}
	
	
	
	
	
}
