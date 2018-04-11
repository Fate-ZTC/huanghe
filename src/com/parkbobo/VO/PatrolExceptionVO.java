package com.parkbobo.VO;

/**
 * Created by lijunhong on 18/4/3.
 */
public class PatrolExceptionVO {

    private Integer id;             //异常记录id
    private Integer usregid;        //用户区域id
    private String username;        //用户名
    private Integer jobNum;         //工号
    private String exception_name;  //异常名称(描述)
    private String exception_type;  //异常类型
    private String createtime;      //创建时间

    public PatrolExceptionVO() {}


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsregid() {
        return usregid;
    }

    public void setUsregid(Integer usregid) {
        this.usregid = usregid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getJobNum() {
        return jobNum;
    }

    public void setJobNum(Integer jobNum) {
        this.jobNum = jobNum;
    }

    public String getException_name() {
        return exception_name;
    }

    public void setException_name(String exception_name) {
        this.exception_name = exception_name;
    }

    public String getException_type() {
        return exception_type;
    }

    public void setException_type(String exception_type) {
        this.exception_type = exception_type;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
