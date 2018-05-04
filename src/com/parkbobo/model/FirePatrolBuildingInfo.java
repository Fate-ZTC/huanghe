package com.parkbobo.model;

import javax.persistence.*;

import org.postgis.Geometry;

/**
 * @author lijunhong
 * @since 18/5/2 下午10:32
 *
 * 大楼信息
 */
@Entity
@Table(name="fire_patrol_building_info")
@SequenceGenerator(name="generator", sequenceName="fire_patrol_building_info_seq", allocationSize = 1)
public class FirePatrolBuildingInfo {

    private Integer id;             //主键id
    private String name;            //大楼名称
    private Geometry geometry;      //范围信息
    private String buildingId;      //大楼id
    private Integer type;           //分类
    private Integer campusId;       //校区id


    public FirePatrolBuildingInfo() {}

    @Id
    @Column(name="id",nullable=false,unique=true)
    @GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "geometry")
    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Column(name = "building_id")
    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "campus_id")
    public Integer getCampusId() {
        return campusId;
    }

    public void setCampusId(Integer campusId) {
        this.campusId = campusId;
    }
}
