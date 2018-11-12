package com.enze.ep.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.enze.ep.dao.EpConfigDAO;
import com.enze.ep.entity.EpConfig;
import com.enze.ep.entity.EpOrders;
import com.enze.ep.service.EpConfigService;

@Service
public class EpConfigServiceImpl implements EpConfigService {

	@Autowired
	EpConfigDAO epConfigDAO;

	@Autowired
	RedisTemplate redisTemplate;

	public EpConfig findEpConfigByCfgName(String cfgName) {

		String key = EpConfig.Prefix_Redis_Key + EpConfig.Prefix_Redis_Key_Separtor + cfgName;

		EpConfig epConfig = (EpConfig) redisTemplate.opsForValue().get(key);
		if (epConfig == null) {
			epConfig = epConfigDAO.selectConfigByCfgname(cfgName);
			redisTemplate.opsForValue().set(key, epConfig);
			redisTemplate.expire(key, 300, TimeUnit.SECONDS);
		}
		return epConfig;

	}

}
