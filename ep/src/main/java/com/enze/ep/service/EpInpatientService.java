package com.enze.ep.service;

import java.util.List;

import com.enze.ep.entity.EpCounter;
import com.enze.ep.entity.EpInpatient;

public interface EpInpatientService {
	 List<EpInpatient> findInpatientByInpatientName(String inpatientName);
}
