package com.parkbobo.utils.http;

/**
 * Created by free on 16-9-6.
 */
public enum  HttpTimeOutFeature implements HttpFeature{

    Threed_Second(3),Five_Second(5),Ten_Second(10),Fifteen_Second(15);

    private int time_out;

    HttpTimeOutFeature(int time_out){
        this.time_out=time_out;
    }

    public int getTime_out() {
        return time_out;
    }

    public void setTime_out(int time_out) {
        this.time_out = time_out;
    }
}
