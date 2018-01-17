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
 * UsersResources entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="lq_manager_resources"
)
public class ManagerResources  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = -8647158316934987554L;
	private ManagerResourcesId id;
     private Manager manager;
     private Resources resources;
     private Integer type;
     private String memo;



   
    // Property accessors
    @EmbeddedId
    
    @AttributeOverrides( {
        @AttributeOverride(name="resourcesId", column=@Column(name="resources_id", nullable=false) ), 
        @AttributeOverride(name="userId", column=@Column(name="user_id", nullable=false) ) } )

    public ManagerResourcesId getId() {
        return this.id;
    }
    
    public void setId(ManagerResourcesId id) {
        this.id = id;
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
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false, insertable=false, updatable=false)
	public Manager getManager() {
		return manager;
	}

	public void setManager(Manager manager) {
		this.manager = manager;
	}
   








}