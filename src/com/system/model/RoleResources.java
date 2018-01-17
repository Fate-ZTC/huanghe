package com.system.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * RoleResources entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="lq_role_resources"
)
public class RoleResources  implements java.io.Serializable {



     /**
	 * 
	 */
	private static final long serialVersionUID = -7180466272474998060L;
	private RoleResourcesId id;
     private Role role;
     private Resources resources;
     private Integer type;
     private String memo;


    // Constructors

    /** default constructor */
    public RoleResources() {
    }

	/** minimal constructor */
    public RoleResources(RoleResourcesId id, Role role, Resources resources, Integer type) {
        this.id = id;
        this.role = role;
        this.resources = resources;
        this.type = type;
    }
    
    /** full constructor */
    public RoleResources(RoleResourcesId id, Role role, Resources resources, Integer type, String memo) {
        this.id = id;
        this.role = role;
        this.resources = resources;
        this.type = type;
        this.memo = memo;
    }

   
    // Property accessors
    @EmbeddedId
    
    @AttributeOverrides( {
        @AttributeOverride(name="roleId", column=@Column(name="role_id", nullable=false) ), 
        @AttributeOverride(name="resourcesId", column=@Column(name="resources_id", nullable=false) ) } )

    public RoleResourcesId getId() {
        return this.id;
    }
    
    public void setId(RoleResourcesId id) {
        this.id = id;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="role_id", nullable=false, insertable=false, updatable=false)

    public Role getRole() {
        return this.role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
	@ManyToOne(fetch=FetchType.LAZY)
        @JoinColumn(name="resources_id", nullable=false, insertable=false, updatable=false)

    public Resources getResources() {
        return this.resources;
    }
    
    public void setResources(Resources resources) {
        this.resources = resources;
    }
    
    @Column(name="type", nullable=false)

    public Integer getType() {
        return this.type;
    }
    
    public void setType(Integer type) {
        this.type = type;
    }
    
    @Column(name="memo")

    public String getMemo() {
        return this.memo;
    }
    
    public void setMemo(String memo) {
        this.memo = memo;
    }
   








}