package com.enze.ep.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.enze.ep.dao.EpUserDAO;
import com.enze.ep.entity.EpSection;
import com.enze.ep.entity.EpUser;
import com.enze.ep.service.EpUserService;

@Service
public class EpUserServiceImpl implements EpUserService {

	@Autowired
	EpUserDAO epUserDAO;
	
	@Autowired
	RedisTemplate redisTemplate;
	
	@Override
	public EpUser findEpUserByUserName(String username) {
		return epUserDAO.selectUserByUserName(username);
	}

	@Override
	public EpUser saveOrFindEpUserByWlAmdResponseSUser(EpUser wlAmdResponseSUser) {
		
		//从redies查询
		String key = EpUser.Prefix_Redis_Key + EpUser.Prefix_Redis_Key_Separtor +wlAmdResponseSUser.getUsercode();
		EpUser epUser=(EpUser)redisTemplate.opsForValue().get(key);
		if(epUser==null) {
			//根据hissectionid查询数据库中是否含有这个section
			epUser=epUserDAO.selectUserByUserCode(wlAmdResponseSUser.getUsercode());//.selectSectionByHissectionid(WlAmdResponseSSection.getHissectionid());
			//查不到就保存到数据库
			if(epUser==null) {
				//int userid=epUserDAO.addUser(wlAmdResponseSUser);
				//wlAmdResponseSUser.setUserid(userid);
				epUserDAO.addUser(wlAmdResponseSUser);
				epUser=wlAmdResponseSUser;
			}
			//保存到redirs
			redisTemplate.opsForValue().set(key, epUser);
			redisTemplate.expire(key, 1, TimeUnit.DAYS);
		}
		
		
		return epUser;
	}

	@Override
	public EpUser findEpUserByUserCode(String usercode) {
		return epUserDAO.selectUserByUserCode(usercode);
	}

}
