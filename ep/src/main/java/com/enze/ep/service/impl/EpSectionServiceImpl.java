package com.enze.ep.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.enze.ep.dao.EpSectionDAO;
import com.enze.ep.entity.EpSection;
import com.enze.ep.service.EpSectionService;

@Service
public class EpSectionServiceImpl implements EpSectionService {

	@Autowired
	EpSectionDAO epSectionDAO;

	@Autowired
	RedisTemplate redisTemplate;



	@Override
	public EpSection findEpSectionByID(int sectionid) {


		String key = EpSection.Prefix_Redis_Key + EpSection.Prefix_Redis_Key_Separtor +sectionid ;

		EpSection epSection = (EpSection) redisTemplate.opsForValue().get(key);
		if (epSection == null) {
			epSection = epSectionDAO.selectSectionByID(sectionid);
			redisTemplate.opsForValue().set(key, epSection);
			redisTemplate.expire(key, 12, TimeUnit.HOURS);
		}
		return epSection;

	
	}

}
