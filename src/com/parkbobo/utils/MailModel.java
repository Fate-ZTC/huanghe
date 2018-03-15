package com.parkbobo.utils;

public class MailModel  implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6647177705206769026L;
	private String title;//标题
	private String attachment;//附件
	private String from;//发件人
	private String fromName;
	private String frompsw;
	private String smtp;
	private String to;//收件人
	private String cc;
	private String bcc;
	private String content;//内容
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getFrompsw() {
		return frompsw;
	}
	public void setFrompsw(String frompsw) {
		this.frompsw = frompsw;
	}
	public String getSmtp() {
		return smtp;
	}
	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
}
