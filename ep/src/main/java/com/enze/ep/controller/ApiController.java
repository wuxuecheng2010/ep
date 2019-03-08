package com.enze.ep.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enze.ep.entity.WlAmdPatient;
import com.enze.ep.entity.WlToken;
import com.enze.ep.service.HistoryService;

@Controller
@RequestMapping(value="/api",method=RequestMethod.GET)
public class ApiController {
	
	@Autowired
	HistoryService historyServiceImpl;
	
	@RequestMapping(value="/getPatientIdcardByCardNo/{cardNo}",method=RequestMethod.GET)
	@ResponseBody
	public String getPatientIdcard(@PathVariable(name = "cardNo") String cardNo) {
		
		String action ="JSON.LQYF.GetInfo";
		String card=cardNo;
		WlToken wlToken= historyServiceImpl.getWlToken();
		String token=wlToken.getToken();
		Map<String,String> map=new HashMap<String,String>();
		map.put("action", action);
		map.put("card", card);
		map.put("token", token);
		WlAmdPatient wlAmdPatient =historyServiceImpl.getWlAmdPatientByCardNo(map);
		//String idcard="";
		String regno="";
		if(wlAmdPatient!=null) {
			//idcard=wlAmdPatient.getIDNo();
			regno=wlAmdPatient.getRegNo();
		}
		return regno;
		
	}

}
