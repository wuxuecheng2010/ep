package com.enze.ep.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.enze.ep.entity.EpCounter;
import com.enze.ep.entity.EpUser;
import com.enze.ep.enums.UserType;
import com.enze.ep.service.EpCounterService;
import com.enze.ep.utils.MyAuthUtils;

@Controller
@RequestMapping(value = "/index")
public class IndexController {
	
	@Autowired
	MyAuthUtils myAuthUtils;
	
	@Autowired
	EpCounterService epCounterServiceImpl;
	

	@RequestMapping(value = "/doctor", method = RequestMethod.GET)
	public ModelAndView dtIndex(HttpServletRequest request, HttpServletResponse response) {
		//1、获取cookie身份认证信息
		ModelMap map=myAuthUtils.getAuthInfo(request,response);
		//return "index/doctor";
		return new ModelAndView("index/doctor",map);
	}

	@RequestMapping(value = "/nurse", method = RequestMethod.GET)
	public ModelAndView nsIndex(HttpServletRequest request, HttpServletResponse response) {
		ModelMap map=myAuthUtils.getAuthInfo(request,response);
		return new ModelAndView("index/nurse",map);
		//return "index/nurse";
	}
	
	/*private ModelMap getAuthInfo(HttpServletRequest request, HttpServletResponse response) {
		ModelMap map=new ModelMap();
		EpUser epUser=myAuthUtils.getEpUserByCookie(request, response);
		map.put("epUser", epUser);

        //护士用户所对应的柜台 
		if(epUser.getUsertype()==UserType.NURSE.getTypeValue()) {
			List<EpCounter> epCounterList=epCounterServiceImpl.findCounterBySectionid(epUser.getSectionid());
			map.put("epCounterList", epCounterList);
			String counterids="";
			for(EpCounter c : epCounterList) {
				counterids+=c.getIcounterid()+",";
			}
			if(!"".equals(counterids))
				counterids=counterids.substring(0, counterids.length()-1);
			map.put("counterids", counterids);
		}
		
		
		return map;
	}*/

}
