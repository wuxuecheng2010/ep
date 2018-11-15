package com.enze.ep.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enze.ep.dao.EpCounterDAO;
import com.enze.ep.dao.EpUserDAO;
import com.enze.ep.entity.TbCounter;
import com.enze.ep.entity.EpUser;
import com.enze.ep.service.EpCounterService;
import com.enze.ep.service.EpUserService;

@Service
public class EpCounterServiceImpl implements EpCounterService {

	@Autowired
	EpCounterDAO epCounterDAO;

	@Override
	public List<TbCounter> findCounterBySectionid(int sectionid) {
		return epCounterDAO.selectCounterBySectionid(sectionid);
	}

	@Override
	public String findCouteridsBySectionid(int sectionid) {
		String counterids="";
		List<TbCounter> counterlist=findCounterBySectionid(sectionid);
		for(TbCounter epCounter:counterlist) {
			int _icounterid=epCounter.getIcounterid();
			counterids+=_icounterid+",";
		}
		if(!"".equals(counterids))
		counterids=counterids.substring(0, counterids.length()-1);
		return counterids;
	}
	
}
