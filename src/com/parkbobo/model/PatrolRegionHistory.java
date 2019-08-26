package com.parkbobo.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTWriter;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 巡更历史区域
 * @vesion:2.1
 * @author:wys
 * @date:2019/4/10
 */
@Entity
@Table(name="patrol_region_history")
@SequenceGenerator(name="generator", sequenceName="patrol_region_history_history_id_seq", allocationSize = 1)
public class PatrolRegionHistory implements Serializable {
    /**
     * 历史区域id
     */
    private Integer historyId;
    /**
     * 区域id
     */
    private Integer regionId;
    /**
     * 区域名称
     */
    private String regionName;
    /**
     * 校区编号，0南校区，1北校区
     */
    private Integer campusNum;
    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 最后更新时间
     */
    private Date lastUpdateTime;

    /**
     * 颜色16进制值
     */
    private String color;



    /**
     * 空间几何信息
     */
    @JSONField(serialize=false)
    private Geometry regionLocation;


    /**
     * 字符串类型的空间经纬度信息
     */
    private String geometry;
    /**
     * 面图元几何中心点
     */
    private String geometryCentroid;

    /**
     * 区域面
     */
    private List coordinates;
    /**
     * 是否删除
     */
    private Short isDel;
    @Id
    @Column(name="history_id",unique=true,nullable=false)
    @GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    @Column(name="region_id")
    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }
    @Column(name="region_name")
    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
    @Column(name="campus_num")
    public Integer getCampusNum() {
        return campusNum;
    }

    public void setCampusNum(Integer campusNum) {
        this.campusNum = campusNum;
    }
    @Column(name="createtime")
    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
    @Column(name="region_location")
    @Type(type="org.hibernatespatial.GeometryUserType",parameters ={
            @Parameter(name="dialect",value="postgis")
    })
    public Geometry getRegionLocation() {
        return regionLocation;
    }

    public void setRegionLocation(Geometry regionLocation) {
        this.regionLocation = regionLocation;
    }
    @Column(name="is_del")
    public Short getIsDel() {
        return isDel;
    }

    public void setIsDel(Short isDel) {
        this.isDel = isDel;
    }

    @Transient
    public String getGeometry() {
        WKTWriter wr = new WKTWriter();
        if(regionLocation!=null){
            geometry =  wr.write(this.regionLocation);
        }
        if(geometry != null) {
            if (geometry.length() > 0) {
                geometry = geometry.replaceAll(", ", ",");
            }
            return geometry;
        }
        return null;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    @Transient
    public String getGeometryCentroid() {
        WKTWriter wr = new WKTWriter();
        if(regionLocation!=null){
            geometryCentroid = wr.write(this.regionLocation.getCentroid());
            geometryCentroid = geometryCentroid.substring(geometryCentroid.indexOf("(")+1, geometryCentroid.indexOf(")")).replace(" ", ",");
            return geometryCentroid;
        }
        return null;
    }
    @Column(name="last_update_time")
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Column(name = "color")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if(color == null) {
            //设置默认颜色
            this.color = "#02a6cf";
        }else {
            this.color = color;
        }
    }

    @Transient
    public List getCoordinates() {
        return formatCoordinates();
    }

    public void setCoordinates() {
        this.coordinates = formatCoordinates();
    }

    /**
     * 进行格式化操作
     * @return
     */
    public List formatCoordinates() {
        String geometry = getGeometry();
        String[] geometrys = null;
        List array = new ArrayList();
        if(geometry != null && !"".equals(geometry)) {
            geometrys = geometry.replace("POLYGON","")
                    .replace("(","")
                    .replace(")","")
                    .trim()
                    .split(",");
            for(int i = 0;i < geometrys.length;i++) {
                List temp = new ArrayList();
                String[] point = geometrys[i].split(" ");
                temp.add(point[0]);
                temp.add(point[1]);
                array.add(temp);
            }
        }

        return array;
    }
}
