package com.parkbobo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备厂商
 * @version 1.0
 * @author ZQ
 * @since 2018-12-28 14:30:18
 */
@Entity
@Table(name="patrol_equipment_manufacturer")
@SequenceGenerator(name="generator", sequenceName="patrol_equipment_manufacturer_manufacturer_id_seq", allocationSize = 1)
public class PatrolEquipmentManufacturer implements Serializable{


    private static final long serialVersionUID = 985014630996726583L;
    /**
     *主键id
     */
    private Integer  manufacturerId   ;
    /**
     *厂商名称
     */
    private String    manufacturerName  ;
    /**
     *对接地址
     */
    private String    dockingAddress  ;



    @Id
    @Column(name = "manufacturer_id", unique = true, nullable = false)
    @GeneratedValue(generator = "generator", strategy =  GenerationType.AUTO)
    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    @Column(name = "manufactu_name")
    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    @Column(name = "docking_address")
    public String getDockingAddress() {
        return dockingAddress;
    }

    public void setDockingAddress(String dockingAddress) {
        this.dockingAddress = dockingAddress;
    }



}
