package com.enze.ep.service;

import java.util.List;

import com.enze.ep.entity.EpCounter;

public interface EpCounterService {
	 List<EpCounter> findCounterBySectionid(int sectionid);
	 String findCouteridsBySectionid(int sectionid);
	 
}
