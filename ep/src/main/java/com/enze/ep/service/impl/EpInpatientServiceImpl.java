package com.enze.ep.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.enze.ep.dao.EpInpatientDao;
import com.enze.ep.entity.EpInpatient;
import com.enze.ep.service.EpInpatientService;

@Service
public class EpInpatientServiceImpl implements EpInpatientService {

	@Autowired
	EpInpatientDao epInpatientDao;

	@Override
	public List<EpInpatient> findInpatientByInpatientName(String inpatientName) {
		return epInpatientDao.selectInpatientByInpatientName(inpatientName);
	}


}
