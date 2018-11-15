package com.enze.ep.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enze.ep.entity.EpInpatient;
import com.enze.ep.entity.TbProduct;
import com.enze.ep.service.EpInpatientService;
import com.enze.ep.service.EpProductService;

@Controller
@RequestMapping(value="/inpatient")
public class InpatientController {
	
	@Autowired
	EpInpatientService epInpatientServiceImpl;
	
	@ResponseBody
	@PostMapping("/select")
	public Map<String,List<EpInpatient>> productSelect(HttpServletRequest  request,HttpServletResponse response) {
		String inpatientName=request.getParameter("keyword");
		List<EpInpatient> list=epInpatientServiceImpl.findInpatientByInpatientName(inpatientName);
		Map<String,List<EpInpatient>> map=new HashMap<String,List<EpInpatient>>();
		map.put("data", list);
		return map;
	}
	

}
