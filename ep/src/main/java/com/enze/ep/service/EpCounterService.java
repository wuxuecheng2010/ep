package com.enze.ep.service;

import java.util.List;

import com.enze.ep.entity.TbCounter;

public interface EpCounterService {
	 List<TbCounter> findCounterBySectionid(int sectionid);
	 String findCouteridsBySectionid(int sectionid);
	 
}
