package com.system.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * UsersResourcesId entity. @author MyEclipse Persistence Tools
 */
@Embeddable

public class ManagerResourcesId  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1830011059263731127L;
	private int resourcesId;
     private int userId;


    // Constructors

    /** default constructor */
    public ManagerResourcesId() {
    }

    
    /** full constructor */
    public ManagerResourcesId(int resourcesId, int userId) {
        this.resourcesId = resourcesId;
        this.userId = userId;
    }

   
    // Property accessors

    @Column(name="resources_id", nullable=false)

    public int getResourcesId() {
        return this.resourcesId;
    }
    
    public void setResourcesId(int resourcesId) {
        this.resourcesId = resourcesId;
    }

    @Column(name="user_id", nullable=false)

    public int getUserId() {
        return this.userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof ManagerResourcesId) ) return false;
		 ManagerResourcesId castOther = ( ManagerResourcesId ) other; 
         
		 return (this.getResourcesId()==castOther.getResourcesId())
 && (this.getUserId()==castOther.getUserId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + (int) this.getResourcesId();
         result = 37 * result + (int) this.getUserId();
         return result;
   }   





}