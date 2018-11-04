package com.enze.ep.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enze.ep.dao.EpCounterDAO;
import com.enze.ep.dao.EpUserDAO;
import com.enze.ep.entity.EpCounter;
import com.enze.ep.entity.EpUser;
import com.enze.ep.service.EpCounterService;
import com.enze.ep.service.EpUserService;

@Service
public class EpCounterServiceImpl implements EpCounterService {

	@Autowired
	EpCounterDAO epCounterDAO;

	@Override
	public List<EpCounter> findCounterBySectionid(int sectionid) {
		return epCounterDAO.selectCounterBySectionid(sectionid);
	}
	
}
