package com.system.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * Menu entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="lq_menu")
@SequenceGenerator(name = "generator", sequenceName = "lq_menu_menu_id_seq", allocationSize = 1)
public class Menu  implements java.io.Serializable {


	private static final long serialVersionUID = 4593557419431403263L;
	private Integer menuId;
     private Menu menu;
     private String name;
     private String enname;
     private String menutype;
     private String target;
     private String icon;
     private Integer isopen;
     private Integer isleaf;
     private Integer iscore;
     private Integer enable;
     private Date createTime;
     private Integer orderid;
     private String memo;
     private Set<Menu> menus = new HashSet<Menu>(0);
     private Set<Resources> resourceses = new HashSet<Resources>(0);
     private List<Menu> childrenMenu  = new ArrayList<Menu>(0);

   
    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO, generator="generator")
    
    @Column(name="menu_id", unique=true, nullable=false)

    public Integer getMenuId() {
        return this.menuId;
    }
    
    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
	@ManyToOne(fetch=FetchType.EAGER)
        @JoinColumn(name="parentid")

    public Menu getMenu() {
        return this.menu;
    }
    
    public void setMenu(Menu menu) {
        this.menu = menu;
    }
    
    @Column(name="name", length=50)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="enname", length=50)

    public String getEnname() {
        return this.enname;
    }
    
    public void setEnname(String enname) {
        this.enname = enname;
    }
    
    @Column(name="menutype", length=2)

    public String getMenutype() {
        return this.menutype;
    }
    
    public void setMenutype(String menutype) {
        this.menutype = menutype;
    }
    
    @Column(name="target")

    public String getTarget() {
        return this.target;
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
    
    @Column(name="icon")

    public String getIcon() {
        return this.icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    @Column(name="isopen", nullable=false)

    public Integer getIsopen() {
        return this.isopen;
    }
    
    public void setIsopen(Integer isopen) {
        this.isopen = isopen;
    }
    
    @Column(name="isleaf", nullable=false)

    public Integer getIsleaf() {
        return this.isleaf;
    }
    
    public void setIsleaf(Integer isleaf) {
        this.isleaf = isleaf;
    }
    
    @Column(name="iscore", nullable=false)

    public Integer getIscore() {
        return this.iscore;
    }
    
    public void setIscore(Integer iscore) {
        this.iscore = iscore;
    }
    
    @Column(name="enable", nullable=false)

    public Integer getEnable() {
        return this.enable;
    }
    
    public void setEnable(Integer enable) {
        this.enable = enable;
    }
    
    @Column(name="create_time", nullable=false, length=29)

    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    @Column(name="orderid")

    public Integer getOrderid() {
        return this.orderid;
    }
    
    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }
    
    @Column(name="memo")

    public String getMemo() {
        return this.memo;
    }
    
    public void setMemo(String memo) {
        this.memo = memo;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="menu")

    public Set<Menu> getMenus() {
        return this.menus;
    }
    
    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="menu")

    public Set<Resources> getResourceses() {
        return this.resourceses;
    }
    
    public void setResourceses(Set<Resources> resourceses) {
        this.resourceses = resourceses;
    }
   
	@Transient
	public List<Menu> getChildrenMenu() {
		return childrenMenu;
	}

	public void setChildrenMenu(List<Menu> childrenMenu) {
		this.childrenMenu = childrenMenu;
	}








}