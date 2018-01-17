package com.parkbobo.model;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * 短信验证码
 * @author LH
 *
 */
@Entity
@Table(name="lq_mobile_checkcode")
@SequenceGenerator(name = "generator", sequenceName = "lq_mobile_checkcode_codeid_seq", allocationSize = 1)
public class MobileCheckcode  implements java.io.Serializable {

	private static final long serialVersionUID = 5831032053289614906L;
	 /**
	 * 记录ID
	 */
	private Long codeid;
	 /**
	 * 手机号码
	 */
	private String telephone;
	 /**
	 * 验证码
	 */
	private String checkcode;
	 /**
	 * 生成时间
	 */
	private Long posttime;
	/**
	 * 验证码类型，0：绑定手机号，1：找回密码
	 */
	private Short type;
	 /**
	 * 
	 */
	private String memo;


    public MobileCheckcode() {
    }

    public MobileCheckcode(Long codeid) {
        this.codeid = codeid;
    }
    
    public MobileCheckcode(Long codeid, String telephone, String checkcode, Long posttime, Short type, String memo) {
        this.codeid = codeid;
        this.telephone = telephone;
        this.checkcode = checkcode;
        this.posttime = posttime;
        this.type = type;
        this.memo = memo;
    }

   
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "generator")
    @Column(name="codeid", unique=true, nullable=false)

    public Long getCodeid() {
        return this.codeid;
    }
    
    public void setCodeid(Long codeid) {
        this.codeid = codeid;
    }
    
    @Column(name="telephone", length=20)

    public String getTelephone() {
        return this.telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    @Column(name="checkcode", length=20)

    public String getCheckcode() {
        return this.checkcode;
    }
    
    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }
    
    @Column(name="posttime")

    public Long getPosttime() {
        return this.posttime;
    }
    
    public void setPosttime(Long posttime) {
        this.posttime = posttime;
    }
    
    @Column(name="memo")

    public String getMemo() {
        return this.memo;
    }
    
    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Column(name="type")
    
	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}
   








}