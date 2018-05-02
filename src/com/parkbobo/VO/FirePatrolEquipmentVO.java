package com.parkbobo.VO;

/**
 * Created by lijunhong on 18/4/8.
 */
public class FirePatrolEquipmentVO {

    private String coordinate;      //经纬度
    private String floorid;         //楼层id
    private String icon;            //图标
    private String name;            //名称
    private Integer pointid;        //id
    private float lon;              //经度
    private float lat;              //纬度
    private ThematicPointCategory thematicPointCategory;


    public FirePatrolEquipmentVO() {}

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getFloorid() {
        return floorid;
    }

    public void setFloorid(String floorid) {
        this.floorid = floorid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPointid() {
        return pointid;
    }

    public void setPointid(Integer pointid) {
        this.pointid = pointid;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public ThematicPointCategory getThematicPointCategory() {
        return thematicPointCategory;
    }

    public void setThematicPointCategory(ThematicPointCategory thematicPointCategory) {
        this.thematicPointCategory = thematicPointCategory;
    }

    @Override
    public String toString() {
        return "FirePatrolEquipmentVO{" +
                "coordinate='" + coordinate + '\'' +
                ", floorid='" + floorid + '\'' +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", pointid=" + pointid +
                ", lon=" + lon +
                ", lat=" + lat +
                ", thematicPointCategory=" + thematicPointCategory +
                '}';
    }
}
