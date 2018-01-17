package com.parkbobo.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "weixin_config")
@SequenceGenerator(name="generator", sequenceName="weixin_config_id_seq", allocationSize = 1)
public class WeixinConfig implements Serializable{

	/**
	 * 微信配置参数
	 */
	private static final long serialVersionUID = -4285284405165607904L;
	private Long id;
	private String accessToken;
	private String jsapiTicket;
	private String wxName;
	@Id
	@Column(name="id", unique = true, nullable = false)
	@GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="access_token")
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	@Column(name="jsapi_ticket")
	public String getJsapiTicket() {
		return jsapiTicket;
	}
	public void setJsapiTicket(String jsapiTicket) {
		this.jsapiTicket = jsapiTicket;
	}
	@Column(name="wx_name")
	public String getWxName() {
		return wxName;
	}
	public void setWxName(String wxName) {
		this.wxName = wxName;
	}

}
