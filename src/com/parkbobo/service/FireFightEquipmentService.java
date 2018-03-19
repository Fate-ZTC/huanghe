package com.parkbobo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.parkbobo.dao.FireFightEquipmentDao;

@Service
public class FireFightEquipmentService {

	@Resource(name="fireFightEquipmentDaoImpl")
	private FireFightEquipmentDao fireFightEquipmentDao;
}
