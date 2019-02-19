package com.enze.ep.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.enze.ep.dao.EpFrequencyDAO;
import com.enze.ep.entity.EpFrequency;
import com.enze.ep.entity.EpUsage;
import com.enze.ep.service.EpFrequencyService;
import com.enze.ep.utils.StringUtils;

@Service
public class EpFrequencyServiceImpl implements EpFrequencyService {

	@Autowired
	EpFrequencyDAO epFrequencyDAO;

	@Autowired
	RedisTemplate redisTemplate;

	@Override
	public List<EpFrequency> findFrequencyListByFrequency(String frequencyInfo) {
		List<EpFrequency> list = new ArrayList<EpFrequency>();
		if (frequencyInfo != null && !"".equals(frequencyInfo)) {
			frequencyInfo=StringUtils.formatForSql(frequencyInfo);
		}
		list = epFrequencyDAO.selectFrequencyListByFrequencyInfo(frequencyInfo);
		return list;
	}


	@Override
	public List<EpFrequency> findAllFrequency() {
		//List<EpFrequency> list =epFrequencyDAO.selectAllFrequency();
		//return list;
		String key = EpFrequency.Prefix_Redis_Key + EpFrequency.Prefix_Redis_Key_Separtor ;
		List<EpFrequency> list = (List<EpFrequency>) redisTemplate.opsForValue().get(key);
		if (list == null) {
			list =epFrequencyDAO.selectAllFrequency();
			redisTemplate.opsForValue().set(key, list);
			redisTemplate.expire(key, 1, TimeUnit.HOURS);
		}
		return list;
	}

}
