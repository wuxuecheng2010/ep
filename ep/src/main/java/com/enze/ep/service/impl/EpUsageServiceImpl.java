package com.enze.ep.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enze.ep.dao.EpUsageDAO;
import com.enze.ep.entity.EpUsage;
import com.enze.ep.service.EpUsageService;
import com.enze.ep.utils.StringUtils;

@Service
public class EpUsageServiceImpl implements EpUsageService {

	@Autowired
	EpUsageDAO epUsageDAO;

	@Override
	public List<EpUsage> findUsageListByUsage(String usage) {
		List<EpUsage> list =  new ArrayList<EpUsage>();
		if (usage != null && !"".equals(usage)) {
			usage=StringUtils.formatForSql(usage);
		}
		list = epUsageDAO.selectUsageListByUsageInfo(usage);
		return list;
	}

}
