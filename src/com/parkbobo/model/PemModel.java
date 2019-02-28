package com.parkbobo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/1/17/017.
 */
public class PemModel {
    List<PatrolEquipmentManufacturer> pList=new ArrayList<>();

    public List<PatrolEquipmentManufacturer> getpList() {
        return pList;
    }

    public void setpList(List<PatrolEquipmentManufacturer> pList) {
        this.pList = pList;
    }
}
