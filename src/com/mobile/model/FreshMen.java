package com.mobile.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 新生实体
 * @author RY
 * @version 1.0
 * @since 2016-9-1 15:11:43
 *
 */
@Entity
@Table(name = "eas_freshmen")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FreshMen implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7365610238919310210L;
	/**
	 * 准考证号
	 */
	private String examcode;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 性别，1男，2女
	 */
	private Integer gender;
	/**
	 * 生日
	 */
	private Date birth;
	/**
	 * 毕业学校
	 */
	private String graduatedSchool;
	/**
	 * 身份证号
	 */
	private String idNum;
	/**
	 * 专业
	 */
	private String major;
	/**
	 * 院系
	 */
	private String acadamey;
	/**
	 * 省份
	 */
	private String province;
	/**
	 * 班级
	 */
	private String classname;
	/**
	 * 辅导员
	 */
	private String counselor;
	/**
	 * 辅导员联系电话
	 */
	private String counselorTel;

	@Id
	@Column(name = "examcode", nullable = false, unique = true)
	public String getExamcode() {
		return examcode;
	}

	public void setExamcode(String examcode) {
		this.examcode = examcode;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "gender")
	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	@Column(name = "birth")
	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	@Column(name = "graduate_school")
	public String getGraduatedSchool() {
		return graduatedSchool;
	}

	public void setGraduatedSchool(String graduatedSchool) {
		this.graduatedSchool = graduatedSchool;
	}

	@Column(name = "id_num")
	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	@Column(name = "major")
	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	@Column(name = "academy")
	public String getAcadamey() {
		return acadamey;
	}

	public void setAcadamey(String acadamey) {
		this.acadamey = acadamey;
	}

	@Column(name = "province")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "classname")
	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	@Column(name = "counselor")
	public String getCounselor() {
		return counselor;
	}

	public void setCounselor(String counselor) {
		this.counselor = counselor;
	}

	@Column(name = "counselor_tel")
	public String getCounselorTel() {
		return counselorTel;
	}

	public void setCounselorTel(String counselorTel) {
		this.counselorTel = counselorTel;
	}

}
