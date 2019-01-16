package com.enze.ep.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/help")
public class HelpController {
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
     public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
    	 Map<String,Object> map=new ModelMap();
    	 String viewName="help/index";
    	 ModelAndView mv=new ModelAndView(viewName, map);
    	 return mv;
     }
}
