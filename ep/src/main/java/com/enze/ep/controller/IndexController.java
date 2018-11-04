package com.enze.ep.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.enze.ep.entity.EpUser;
import com.enze.ep.utils.MyAuthUtils;

@Controller
@RequestMapping(value = "/index")
public class IndexController {
	
	@Autowired
	MyAuthUtils myAuthUtils;

	@RequestMapping(value = "/doctor", method = RequestMethod.GET)
	public ModelAndView dtIndex(HttpServletRequest request, HttpServletResponse response) {
		//1、获取cookie身份认证信息
		ModelMap map=getAuthInfo(request,response);
		//return "index/doctor";
		return new ModelAndView("index/doctor",map);
	}

	@RequestMapping(value = "/nurse", method = RequestMethod.GET)
	public ModelAndView nsIndex(HttpServletRequest request, HttpServletResponse response) {
		ModelMap map=getAuthInfo(request,response);
		return new ModelAndView("index/nurse",map);
		//return "index/nurse";
	}
	
	private ModelMap getAuthInfo(HttpServletRequest request, HttpServletResponse response) {
		ModelMap map=new ModelMap();
		EpUser epUser=myAuthUtils.getEpUserByCookie(request, response);
		map.put("epUser", epUser);
		//String authToken=myAuthUtils.getAuthToken(request, response);
		return map;
	}

}
