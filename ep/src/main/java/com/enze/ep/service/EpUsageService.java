package com.enze.ep.service;

import java.util.List;

import com.enze.ep.entity.TbCounter;
import com.enze.ep.entity.EpInpatient;
import com.enze.ep.entity.EpUsage;
import com.enze.ep.entity.EpUser;

public interface EpUsageService {
	 List<EpUsage> findUsageListByUsage(String usage);
}
