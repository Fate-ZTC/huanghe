package com.system.model;

import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.UniqueConstraint;


/**
 * Resources entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="lq_resources"
    ,schema="public"
, uniqueConstraints = @UniqueConstraint(columnNames="enname")
)
@SequenceGenerator(name="generator",sequenceName="lq_resources_resources_id_seq",allocationSize=1)
public class Resources  implements java.io.Serializable {


	private static final long serialVersionUID = 8294127325241937144L;
	private Integer resourcesId;
     private Menu menu;
     private Resources resources;
     private String name;
     private String enname;
     private Integer resourcetype;
     private String target;
     private Integer isopen;
     private Integer isleaf;
     private Integer iscore;
     private Integer enable;
     private Date createTime;
     private Integer orderid;
     private String memo;
     //private Set<RoleResources> roleResourceses = new HashSet<RoleResources>(0);
     private Set<Resources> resourceses = new HashSet<Resources>(0);

   
    
    @Id 
    @GeneratedValue(strategy=GenerationType.AUTO, generator="generator")
    
    @Column(name="resources_id", unique=true, nullable=false)

    public Integer getResourcesId() {
        return this.resourcesId;
    }
    
    public void setResourcesId(Integer resourcesId) {
        this.resourcesId = resourcesId;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="menu_id")

    public Menu getMenu() {
        return this.menu;
    }
    
    public void setMenu(Menu menu) {
        this.menu = menu;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="parentid")

    public Resources getResources() {
        return this.resources;
    }
    
    public void setResources(Resources resources) {
        this.resources = resources;
    }
    
    @Column(name="name", nullable=false, length=100)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="enname", unique=true, nullable=false, length=100)

    public String getEnname() {
        return this.enname;
    }
    
    public void setEnname(String enname) {
        this.enname = enname;
    }
    
    @Column(name="resourcetype")

    public Integer getResourcetype() {
        return this.resourcetype;
    }
    
    public void setResourcetype(Integer resourcetype) {
        this.resourcetype = resourcetype;
    }
    
    @Column(name="target", length=100)

    public String getTarget() {
        return this.target;
    }
    
    public void setTarget(String target) {
        this.target = target;
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
/*@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="resources")

    public Set<RoleResources> getRoleResourceses() {
        return this.roleResourceses;
    }

    public void setRoleResourceses(Set<RoleResources> roleResourceses) {
        this.roleResourceses = roleResourceses;
    }*/
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="resources")

    public Set<Resources> getResourceses() {
        return this.resourceses;
    }
    
    public void setResourceses(Set<Resources> resourceses) {
        this.resourceses = resourceses;
    }
   


}