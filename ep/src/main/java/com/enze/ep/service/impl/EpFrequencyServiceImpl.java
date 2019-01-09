package com.enze.ep.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enze.ep.dao.EpFrequencyDAO;
import com.enze.ep.entity.EpFrequency;
import com.enze.ep.service.EpFrequencyService;
import com.enze.ep.utils.StringUtils;

@Service
public class EpFrequencyServiceImpl implements EpFrequencyService {

	@Autowired
	EpFrequencyDAO epFrequencyDAO;


	@Override
	public List<EpFrequency> findFrequencyListByFrequency(String frequencyInfo) {
		List<EpFrequency> list = new ArrayList<EpFrequency>();
		if (frequencyInfo != null && !"".equals(frequencyInfo)) {
			frequencyInfo=StringUtils.formatForSql(frequencyInfo);
		}
		list = epFrequencyDAO.selectFrequencyListByFrequencyInfo(frequencyInfo);
		return list;
	}

}
