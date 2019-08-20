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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * Role entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="lq_role"
)
@SequenceGenerator(name="generator",sequenceName="lq_role_role_id_seq",allocationSize=1)
public class Role  implements java.io.Serializable {


    // Fields

    /**
     *
     */
    private static final long serialVersionUID = 3654631483837955600L;
    private Integer roleId;
    private String name;
    private String enname;
    private Integer enable;
    private Integer iscore;
    private Integer orderid;
    private Date createTime;
    private Integer roleType;
    private String memo;
    private Set<RoleResources> roleResourceses = new HashSet<RoleResources>(0);

    public Role(){}
    public Role(Integer roleId){
        this.roleId = roleId;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO, generator="generator")
    @Column(name="role_id", unique=true, nullable=false)

    public Integer getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Column(name="name", nullable=false, length=50)

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

    @Column(name="enable", nullable=false)

    public Integer getEnable() {
        return this.enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    @Column(name="iscore", nullable=false)

    public Integer getIscore() {
        return this.iscore;
    }

    public void setIscore(Integer iscore) {
        this.iscore = iscore;
    }

    @Column(name="orderid")

    public Integer getOrderid() {
        return this.orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    @Column(name="create_time", nullable=false, length=29)

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name="memo")

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="role")

    public Set<RoleResources> getRoleResourceses() {
        return this.roleResourceses;
    }

    public void setRoleResourceses(Set<RoleResources> roleResourceses) {
        this.roleResourceses = roleResourceses;
    }
    @Column(name="role_type")
    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

//    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="role")
//    public Set<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<Role> roles) {
//        this.roles = roles;
//    }
}