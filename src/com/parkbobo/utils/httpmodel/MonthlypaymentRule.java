package com.parkbobo.utils.httpmodel;

import java.io.Serializable;

public class MonthlypaymentRule implements Serializable{

	/**
	 * 停车场包期规则
	 */
	private static final long serialVersionUID = 7022764978284417175L;
	private Integer ruleId;//ID
	private String ruleName;//包月规则
	private Integer payPee;//单价
	private Integer ruleType;//包期类型 1包月，2包年
	public Integer getRuleId() {
		return ruleId;
	}
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public Integer getPayPee() {
		return payPee;
	}
	public void setPayPee(Integer payPee) {
		this.payPee = payPee;
	}
	public Integer getRuleType() {
		return ruleType;
	}
	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}
	
	
	
}
