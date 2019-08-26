package com.mobile.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;


/**
 * EnumerateMain entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="lq_enumerate_main"
    ,schema="public"
)

public class EnumerateMain  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 7849517495012735944L;
	private Integer id;
     private String classcode;
     private String classnameen;
     private String classnamecn;
     private Integer maxparanum;
     private short isallowinsordel;
     private short isMx;
     private String paranameDispTitle;
     private String paravalueDispTitle;
     private short isEditable;
     private String remarks;
     private Set<EnumerateDetail> enumerateDetails = new HashSet<EnumerateDetail>(0);


    // Constructors

    /** default constructor */
    public EnumerateMain() {
    }

    
    /** full constructor */
    public EnumerateMain(String classcode, String classnameen, String classnamecn, Integer maxparanum, short isallowinsordel, short isMx, String paranameDispTitle, String paravalueDispTitle, short isEditable, String remarks, Set<EnumerateDetail> enumerateDetails) {
        this.classcode = classcode;
        this.classnameen = classnameen;
        this.classnamecn = classnamecn;
        this.maxparanum = maxparanum;
        this.isallowinsordel = isallowinsordel;
        this.isMx = isMx;
        this.paranameDispTitle = paranameDispTitle;
        this.paravalueDispTitle = paravalueDispTitle;
        this.isEditable = isEditable;
        this.remarks = remarks;
        this.enumerateDetails = enumerateDetails;
    }

   
    // Property accessors
    @SequenceGenerator(name="generator")@Id
    @GeneratedValue(strategy=SEQUENCE, generator="generator")
    
    @Column(name="id", unique=true, nullable=false)

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    @Column(name="classcode", length=40)

    public String getClasscode() {
        return this.classcode;
    }
    
    public void setClasscode(String classcode) {
        this.classcode = classcode;
    }
    
    @Column(name="classnameen", length=60)

    public String getClassnameen() {
        return this.classnameen;
    }
    
    public void setClassnameen(String classnameen) {
        this.classnameen = classnameen;
    }
    
    @Column(name="classnamecn", length=60)

    public String getClassnamecn() {
        return this.classnamecn;
    }
    
    public void setClassnamecn(String classnamecn) {
        this.classnamecn = classnamecn;
    }
    
    @Column(name="maxparanum")

    public Integer getMaxparanum() {
        return this.maxparanum;
    }
    
    public void setMaxparanum(Integer maxparanum) {
        this.maxparanum = maxparanum;
    }
    
    @Column(name="isallowinsordel")

    public short getIsallowinsordel() {
        return this.isallowinsordel;
    }
    
    public void setIsallowinsordel(short isallowinsordel) {
        this.isallowinsordel = isallowinsordel;
    }
    
    @Column(name="is_mx")

    public short getIsMx() {
        return this.isMx;
    }
    
    public void setIsMx(short isMx) {
        this.isMx = isMx;
    }
    
    @Column(name="paraname_disp_title", length=60)

    public String getParanameDispTitle() {
        return this.paranameDispTitle;
    }
    
    public void setParanameDispTitle(String paranameDispTitle) {
        this.paranameDispTitle = paranameDispTitle;
    }
    
    @Column(name="paravalue_disp_title", length=60)

    public String getParavalueDispTitle() {
        return this.paravalueDispTitle;
    }
    
    public void setParavalueDispTitle(String paravalueDispTitle) {
        this.paravalueDispTitle = paravalueDispTitle;
    }
    
    @Column(name="is_editable")

    public short getIsEditable() {
        return this.isEditable;
    }
    
    public void setIsEditable(short isEditable) {
        this.isEditable = isEditable;
    }
    
    @Column(name="remarks")

    public String getRemarks() {
        return this.remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    @OneToMany(cascade= CascadeType.ALL, fetch= FetchType.LAZY, mappedBy="enumerateMain")
    public Set<EnumerateDetail> getEnumerateDetails() {
        return this.enumerateDetails;
    }
    
    public void setEnumerateDetails(Set<EnumerateDetail> enumerateDetails) {
        this.enumerateDetails = enumerateDetails;
    }
   








}