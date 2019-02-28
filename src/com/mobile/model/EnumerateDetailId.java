package com.mobile.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * EnumerateDetailId entity. @author MyEclipse Persistence Tools
 */
@Embeddable

public class EnumerateDetailId  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1661507223253897495L;
	private long id;
     private long ino;


    // Constructors

    /** default constructor */
    public EnumerateDetailId() {
    }

    
    /** full constructor */
    public EnumerateDetailId(long id, long ino) {
        this.id = id;
        this.ino = ino;
    }

   
    // Property accessors

    @Column(name="id", nullable=false)
    
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    @Column(name="ino", nullable=false)

    public long getIno() {
        return this.ino;
    }
    
    public void setIno(long ino) {
        this.ino = ino;
    }
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof EnumerateDetailId) ) return false;
		 EnumerateDetailId castOther = ( EnumerateDetailId ) other; 
         
		 return (this.getId()==castOther.getId())
 && (this.getIno()==castOther.getIno());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + (int) this.getId();
         result = 37 * result + (int) this.getIno();
         return result;
   }   





}