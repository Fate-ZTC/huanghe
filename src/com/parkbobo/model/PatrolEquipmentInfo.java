package com.parkbobo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 巡更定位设备信息
 * @version 1.0
 * @author ZQ
 * @since 2018-12-28 14:20:37
 */
@Entity
@Table(name="patrol_equipment_info")
@SequenceGenerator(name="generator", sequenceName="patrol_equipment_info_equipmen_id_seq", allocationSize = 1)
public class PatrolEquipmentInfo implements Serializable{



    private static final long serialVersionUID = 985014630996726582L;
    /**
     * 设备id
      */
    private Integer     equipmenId;
    /**
     *厂商id
      */
    private PatrolEquipmentManufacturer patrolEquipmentManufacturer;
    /**
     *编号
      */
    private String    number;
    /**
     *名称
      */
    private String     name;
    /**
     *更新时间
      */
    private Date updateTime;
    /**
     *条形码
      */
    private String     barCode;

    @Id
    @Column(name = "equipmen_id", unique = true, nullable = false)
    @GeneratedValue(generator = "generator", strategy =  GenerationType.AUTO)
    public Integer getEquipmenId() {
        return equipmenId;
    }

    public void setEquipmenId(Integer equipmenId) {
        this.equipmenId = equipmenId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    public PatrolEquipmentManufacturer getPatrolEquipmentManufacturer() {
        return patrolEquipmentManufacturer;
    }

    public void setPatrolEquipmentManufacturer(PatrolEquipmentManufacturer patrolEquipmentManufacturer) {
        this.patrolEquipmentManufacturer = patrolEquipmentManufacturer;
    }

    @Column(name = "number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "bar_code")
    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}
