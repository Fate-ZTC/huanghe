package com.mobile.model;


import com.mobile.util.CutHtml;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * 新闻信息实例类
 * @author RenYong
 *
 */
@Entity
@Table(name = "hhxy_infolist")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SequenceGenerator(sequenceName = "hhxy_infolist_infoid_seq", name = "generator", allocationSize = 1)
public class InfoList implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5671589034657406899L;
	/**
	 * 信息ID
	 */
	private Integer infoid;
	/**
	 * 类型，1焦点图，2学校简介，3院系简介，4学校新闻，5院系新闻，6迎新引导，7新生手册
	 */
	private Integer type;
	/**
	 * 院系编码，院系简介专用
	 */
	private String aCode;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 图片
	 */
	private String pic;
	/**
	 * 链接地址
	 */
	private String url;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 最后编辑时间
	 */
	private Date posttime;
	/**
	 * 排序
	 */
	private Integer orderid;
	/**
	 * 是否需要导航，新生引导专用字段
	 */
	private Integer isnavigation;
	/**
	 * 导航经纬度，新生引导专用字段
	 */
	private String lonlat;
	
	@Id
	@Column(name = "infoid", unique = true, nullable = false)
	@GeneratedValue(generator = "generator", strategy = GenerationType.AUTO)
	public Integer getInfoid() {
		return infoid;
	}
	
	public void setInfoid(Integer infoid) {
		this.infoid = infoid;
	}
	
	@Column(name = "type")
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	@Column(name = "title")
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "pic")
	public String getPic() {
		return pic;
	}
	
	public void setPic(String pic) {
		this.pic = pic;
	}
	
	@Column(name = "content")
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name = "posttime")
	public Date getPosttime() {
		return posttime;
	}
	
	public void setPosttime(Date posttime) {
		this.posttime = posttime;
	}
	
	@Column(name = "orderid")
	public Integer getOrderid() {
		return orderid;
	}
	
	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	@Column(name = "url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Transient
    public String getSubContent(int len){
    	return CutHtml.getDefaultInstance().splitAndFilterString(content, len, "...");
    }

	@Column(name = "is_navigation")
	public Integer getIsnavigation() {
		return isnavigation;
	}

	public void setIsnavigation(Integer isnavigation) {
		this.isnavigation = isnavigation;
	}

	@Column(name = "a_code")
	public String getaCode() {
		return aCode;
	}

	public void setaCode(String aCode) {
		this.aCode = aCode;
	}

	@Column(name = "lonlat")
	public String getLonlat() {
		return lonlat;
	}

	public void setLonlat(String lonlat) {
		this.lonlat = lonlat;
	}

}
