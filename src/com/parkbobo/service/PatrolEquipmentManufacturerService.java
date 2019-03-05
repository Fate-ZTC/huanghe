package com.parkbobo.service;


import com.parkbobo.dao.PatrolEquipmentManufacturerDao;
import com.parkbobo.model.PatrolEquipmentManufacturer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component("PatrolEquipmentManufacturerService")
public class PatrolEquipmentManufacturerService {
    @Resource(name = "PatrolEquipmentManufacturerDaoImpl")
    private PatrolEquipmentManufacturerDao patrolEquipmentManufacturerDao;

    public void update(PatrolEquipmentManufacturer patrolEquipmentManufacturer){
        patrolEquipmentManufacturerDao.update(patrolEquipmentManufacturer);
    }
    public void merge(PatrolEquipmentManufacturer patrolEquipmentManufacturer){
        patrolEquipmentManufacturerDao.merge(patrolEquipmentManufacturer);
    }

    public List<PatrolEquipmentManufacturer> getAll(){
        return  patrolEquipmentManufacturerDao.getAll();
    }

    public void delete(Integer manufacturerId) {
        patrolEquipmentManufacturerDao.delete(manufacturerId);
    }

    public List<PatrolEquipmentManufacturer> getByHql(String hql) {
        return patrolEquipmentManufacturerDao.getByHQL(hql);
    }
}
