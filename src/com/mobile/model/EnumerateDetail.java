package com.mobile.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;


/**
 * EnumerateDetail entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="lq_enumerate_detail"
    ,schema="public"
)
@Cache(usage= CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EnumerateDetail implements java.io.Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = -8271881222988909390L;
	// Fields    

    private EnumerateDetailId id;
    private EnumerateMain enumerateMain;
    private String paraname;
    private String paravalue;
    private String maxValue;
    private String minValue;
    private String remarks;


   
    // Property accessors
    @EmbeddedId
    
    @AttributeOverrides( {
        @AttributeOverride(name="id", column=@Column(name="id", nullable=false) ),
        @AttributeOverride(name="ino", column=@Column(name="ino", nullable=false) ) } )

    public EnumerateDetailId getId() {
        return this.id;
    }
    
    public void setId(EnumerateDetailId id) {
        this.id = id;
    }
	@ManyToOne(fetch= FetchType.LAZY)
        @JoinColumn(name="id", nullable=false, insertable=false, updatable=false)

    public EnumerateMain getEnumerateMain() {
        return this.enumerateMain;
    }
    
    public void setEnumerateMain(EnumerateMain enumerateMain) {
        this.enumerateMain = enumerateMain;
    }
    
    @Column(name="paraname")

    public String getParaname() {
        return this.paraname;
    }
    
    public void setParaname(String paraname) {
        this.paraname = paraname;
    }
    
    @Column(name="paravalue")

    public String getParavalue() {
        return this.paravalue;
    }
    
    public void setParavalue(String paravalue) {
        this.paravalue = paravalue;
    }
    
    @Column(name="max_value", length=60)

    public String getMaxValue() {
        return this.maxValue;
    }
    
    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }
    
    @Column(name="min_value", length=60)

    public String getMinValue() {
        return this.minValue;
    }
    
    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }
    
    @Column(name="remarks")

    public String getRemarks() {
        return this.remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
   








}