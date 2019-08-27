package com.mobile.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 系统应用模块实体
 * @author RY
 * @version 1.0
 * @since 2016-8-8 15:57:44
 * @version 1.1
 * @since 2016-9-1 14:43:40
 * 面向用户类型，增加4：新生
 * @version  1.2
 * @since  2018-4-17 17:51:58
 * 面向用户烈性，增加5：指定用户
 *
 */
@Entity
@Table(name = "lq_module")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@SequenceGenerator(name = "generator", sequenceName = "lq_module_module_id_seq", allocationSize = 1)
public class Module implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -352874489321809060L;
	/**
	 * 模块ID
	 */
	private Integer moduleid;
	/**
	 * 父级模块
	 */
	private Module parentModule;
	/**
	 * 模块名称
	 */
	private String modulename;
	/**
	 * 英文名称
	 */
	private String enname;
	/**
	 * 面向用户类型，0公共，1老生，2教师，3管理员，4新生，5指定用户
	 */
	private Integer userType;
	/**
	 * 类型，1开发型，2链接接入型
	 */
	private Integer type;
	/**
	 * 模块图标，链接接入型使用
	 */
	private String icon;
	/**
	 * 目标路径，后台管理模块为调用方法路径，链接型接入应用为链接地址
	 */
	private String target;
	/**
	 * 是否显示：是否显示在首页模块、后台菜单栏，1是，0否
	 */
	private Integer display;
	/**
	 * 是否需要传递用户ID，1是，0否
	 */
	private Integer needParam;
	/**
	 * 是否展开，1是，0否
	 */
	private Integer isOpen;
	/**
	 * 是否为叶节点，1是，0否
	 */
	private Integer isLeaf;
	/**
	 * 是否为核心模块，1是，0否
	 */
	private Integer isScore;
	/**
	 * 是否可用，1是，0否
	 */
	private Integer enable;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 渠道，0除后台、PC外公用，1PC，2微信，3APP，4大屏，5后台管理
	 */
	private Integer channel;
	/**
	 * 排序
	 */
	private Integer orderid;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 有权限的用户ID
	 */
	private String privateUsers;
	
	private List<Module> moduleList = new ArrayList<Module>();

	@Id
	@Column(name = "module_id", nullable = false, unique = true)
	@GeneratedValue(generator = "generator", strategy = GenerationType.AUTO)
	public Integer getModuleid() {
		return moduleid;
	}

	public void setModuleid(Integer moduleid) {
		this.moduleid = moduleid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "parentid")
	public Module getParentModule() {
		return parentModule;
	}

	public void setParentModule(Module parentModule) {
		this.parentModule = parentModule;
	}

	@Column(name = "module_name")
	public String getModulename() {
		return modulename;
	}

	public void setModulename(String modulename) {
		this.modulename = modulename;
	}

	@Column(name = "enname")
	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	@Column(name = "user_type")
	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	@Column(name = "type")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "icon")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "target")
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Column(name = "display")
	public Integer getDisplay() {
		return display;
	}

	public void setDisplay(Integer display) {
		this.display = display;
	}

	@Column(name = "need_parm")
	public Integer getNeedParam() {
		return needParam;
	}

	public void setNeedParam(Integer needParam) {
		this.needParam = needParam;
	}

	@Column(name = "isopen")
	public Integer getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	@Column(name = "isleaf")
	public Integer getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}

	@Column(name = "iscore")
	public Integer getIsScore() {
		return isScore;
	}

	public void setIsScore(Integer isScore) {
		this.isScore = isScore;
	}

	@Column(name = "enable")
	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "channel")
	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	@Column(name = "orderid")
	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	@Column(name = "memo")
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "private_users")
	public String getPrivateUsers() {
		return privateUsers;
	}

	public void setPrivateUsers(String privateUsers) {
		this.privateUsers = privateUsers;
	}

	@OneToMany(mappedBy="parentModule")
	public List<Module> getModuleList() {
		return moduleList;
	}

	public void setModuleList(List<Module> moduleList) {
		this.moduleList = moduleList;
	}

}
