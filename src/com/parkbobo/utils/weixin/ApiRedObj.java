package com.parkbobo.utils.weixin;

public class ApiRedObj {
	private String act_name;
	private String client_ip;
	private String mch_billno;
	private String mch_id;
	private String nonce_str;
	private String remark;
	private String re_openid;
	private String send_name;
	private String total_amount;
	private String total_num;
	private String wxappid;
	private String wishing;
	private String sign;
	
	public ApiRedObj() {
		super();
	}
	public ApiRedObj(String actName, String clientIp, String mchBillno,
			String mchId, String nonceStr, String remark, String reOpenid,
			String sendName, String totalAmount, String totalNum,
			String wxappid, String wishing) {
		super();
		act_name = actName;
		client_ip = clientIp;
		mch_billno = mchBillno;
		mch_id = mchId;
		nonce_str = nonceStr;
		this.remark = remark;
		re_openid = reOpenid;
		send_name = sendName;
		total_amount = totalAmount;
		total_num = totalNum;
		this.wxappid = wxappid;
		this.wishing = wishing;
	}
	
	public String getAct_name() {
		return act_name;
	}
	public void setAct_name(String actName) {
		act_name = actName;
	}
	public String getClient_ip() {
		return client_ip;
	}
	public void setClient_ip(String clientIp) {
		client_ip = clientIp;
	}
	public String getMch_billno() {
		return mch_billno;
	}
	public void setMch_billno(String mchBillno) {
		mch_billno = mchBillno;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mchId) {
		mch_id = mchId;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonceStr) {
		nonce_str = nonceStr;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRe_openid() {
		return re_openid;
	}
	public void setRe_openid(String reOpenid) {
		re_openid = reOpenid;
	}
	public String getSend_name() {
		return send_name;
	}
	public void setSend_name(String sendName) {
		send_name = sendName;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String totalAmount) {
		total_amount = totalAmount;
	}
	public String getTotal_num() {
		return total_num;
	}
	public void setTotal_num(String totalNum) {
		total_num = totalNum;
	}
	public String getWxappid() {
		return wxappid;
	}
	public void setWxappid(String wxappid) {
		this.wxappid = wxappid;
	}
	public String getWishing() {
		return wishing;
	}
	public void setWishing(String wishing) {
		this.wishing = wishing;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	@Override
	public String toString() {
		return "ApiRedObj [act_name=" + act_name + ", client_ip=" + client_ip
				+ ", mch_billno=" + mch_billno + ", mch_id=" + mch_id
				+ ", nonce_str=" + nonce_str + ", re_openid=" + re_openid
				+ ", remark=" + remark + ", send_name=" + send_name + ", sign="
				+ sign + ", total_amount=" + total_amount + ", total_num="
				+ total_num + ", wishing=" + wishing + ", wxappid=" + wxappid
				+ "]";
	}
	
}
