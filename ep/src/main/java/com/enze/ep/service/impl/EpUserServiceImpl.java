package com.enze.ep.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enze.ep.dao.EpUserDAO;
import com.enze.ep.entity.EpUser;
import com.enze.ep.service.EpUserService;

@Service
public class EpUserServiceImpl implements EpUserService {

	@Autowired
	EpUserDAO epUserDAO;
	
	@Override
	public EpUser findEpUserByUserName(String username) {
		return epUserDAO.selectUserByUserName(username);
	}

}
