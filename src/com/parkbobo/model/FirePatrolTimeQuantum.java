package com.parkbobo.model;

import java.util.Date;
import javax.persistence.*;

/**
 * @author lijunhong
 * @since 18/5/3 下午5:11
 */
@Entity
@Table(name="fire_patrol_time_quantum")
@SequenceGenerator(name="generator", sequenceName="fire_patrol_time_quantum_id_seq", allocationSize = 1)
public class FirePatrolTimeQuantum {

    private Integer id;         //主键
    private Date startTime;     //开始时间
    private Date endTime;       //结束时间
    private String jobNum;      //工号
    private Integer campusNum;  //校区id
    private int isNew;          //是否是最新，0不是，1是

    public FirePatrolTimeQuantum() {}

    @Id
    @Column(name="id",nullable=false,unique=true)
    @GeneratedValue(generator="generator", strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "start_time")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Column(name = "end_time")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "job_num")
    public String getJobNum() {
        return jobNum;
    }

    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    @Column(name = "campus_num")
    public Integer getCampusNum() {
        return campusNum;
    }

    public void setCampusNum(Integer campusNum) {
        this.campusNum = campusNum;
    }

    @Column(name = "is_new")
    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }
}
