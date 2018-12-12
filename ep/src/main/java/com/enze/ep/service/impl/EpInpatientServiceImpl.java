package com.enze.ep.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.enze.ep.dao.EpInpatientDAO;
import com.enze.ep.entity.EpInpatient;
import com.enze.ep.service.EpInpatientService;

@Service
public class EpInpatientServiceImpl implements EpInpatientService {

	@Autowired
	EpInpatientDAO epInpatientDao;
	
	@Autowired
	RedisTemplate redisTemplate;
	

	@Override
	public List<EpInpatient> findInpatientByInpatientName(String inpatientName) {
		return epInpatientDao.selectInpatientByInpatientName(inpatientName);
	}

	@Override
	public EpInpatient saveOrFindEpInpatientByWlAmdResponseSEpInpatient(EpInpatient WlAmdResponseSInpatient) {

		//从redies查询
		String key = EpInpatient.Prefix_Redis_Key + EpInpatient.Prefix_Redis_Key_Separtor +WlAmdResponseSInpatient.getHisinid();
		EpInpatient epInpatient=(EpInpatient)redisTemplate.opsForValue().get(key);
		if(epInpatient==null) {
			//根据hissectionid查询数据库中是否含有这个section
			epInpatient=epInpatientDao.selectInpatientByHisinid(Integer.valueOf(WlAmdResponseSInpatient.getHisinid()));
			//查不到就保存到数据库
			if(epInpatient==null) {
				//int inid=epInpatientDao.addInpatient(WlAmdResponseSInpatient);
				//WlAmdResponseSInpatient.setInid(inid);
				epInpatientDao.addInpatient(WlAmdResponseSInpatient);
				epInpatient=WlAmdResponseSInpatient;
			}
			//保存到redirs
			redisTemplate.opsForValue().set(key, epInpatient);
			redisTemplate.expire(key, 1, TimeUnit.DAYS);
		}
		
		
		return epInpatient;
	
	}


}
