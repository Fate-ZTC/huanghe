package com.parkbobo.model;

import javax.persistence.*;

/**
 * @author lijunhong
 * @since 18/5/2 下午10:29
 * 大楼分类信息
 */
@Entity
@Table(name="fire_patrol_building_type")
@SequenceGenerator(name="generator", sequenceName="file_patrol_building_type_seq", allocationSize = 1)
public class FirePatrolBuildingType {

    private Integer id;             //主键
    private String name;            //分类名称
    private Integer type;           //分类
    private Integer campusId;       //校区id
    private Integer sort;           //排序

    public FirePatrolBuildingType() {}


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

    @Column(name = "sort")
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
