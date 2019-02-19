package com.enze.ep.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.enze.ep.dao.EpUsageDAO;
import com.enze.ep.entity.EpSection;
import com.enze.ep.entity.EpUsage;
import com.enze.ep.service.EpUsageService;
import com.enze.ep.utils.StringUtils;

@Service
public class EpUsageServiceImpl implements EpUsageService {

	@Autowired
	EpUsageDAO epUsageDAO;
	
	
	@Autowired
	RedisTemplate redisTemplate;

	@Override
	public List<EpUsage> findUsageListByUsage(String usage) {
		List<EpUsage> list =  new ArrayList<EpUsage>();
		if (usage != null && !"".equals(usage)) {
			usage=StringUtils.formatForSql(usage);
		}
		list = epUsageDAO.selectUsageListByUsageInfo(usage);
		return list;
	}

	@Override
	public List<EpUsage> findAllUsage() {
		//List<EpUsage> list =  epUsageDAO.selectAllUsage();
		//return list;
		String key = EpUsage.Prefix_Redis_Key + EpUsage.Prefix_Redis_Key_Separtor ;
		List<EpUsage> list = (List<EpUsage>) redisTemplate.opsForValue().get(key);
		if (list == null) {
			list =  epUsageDAO.selectAllUsage();
			redisTemplate.opsForValue().set(key, list);
			redisTemplate.expire(key, 1, TimeUnit.HOURS);
		}
		return list;
		
	}

}
