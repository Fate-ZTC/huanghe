package com.system.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * RoleResourcesId entity. @author MyEclipse Persistence Tools
 */
@Embeddable

public class RoleResourcesId  implements java.io.Serializable {



     /**
	 * 
	 */
	private static final long serialVersionUID = -4672731222712329081L;
	private int roleId;
     private int resourcesId;


    // Constructors

    /** default constructor */
    public RoleResourcesId() {
    }

    
    /** full constructor */
    public RoleResourcesId(int roleId, int resourcesId) {
        this.roleId = roleId;
        this.resourcesId = resourcesId;
    }

   
    // Property accessors

    @Column(name="role_id", nullable=false)

    public int getRoleId() {
        return this.roleId;
    }
    
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Column(name="resources_id", nullable=false)

    public int getResourcesId() {
        return this.resourcesId;
    }
    
    public void setResourcesId(int resourcesId) {
        this.resourcesId = resourcesId;
    }
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof RoleResourcesId) ) return false;
		 RoleResourcesId castOther = ( RoleResourcesId ) other; 
         
		 return (this.getRoleId()==castOther.getRoleId())
 && (this.getResourcesId()==castOther.getResourcesId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + (int) this.getRoleId();
         result = 37 * result + (int) this.getResourcesId();
         return result;
   }   





}