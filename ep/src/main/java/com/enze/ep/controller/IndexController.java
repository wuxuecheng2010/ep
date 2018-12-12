package com.enze.ep.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.enze.ep.entity.EpUser;
import com.enze.ep.entity.WlAmdParam;
import com.enze.ep.entity.WlAmdResponse;
import com.enze.ep.entity.WlAmdUser;
import com.enze.ep.entity.WlToken;
import com.enze.ep.enums.UserType;
import com.enze.ep.service.EpCounterService;
import com.enze.ep.service.HistoryService;
import com.enze.ep.utils.MyAuthUtils;

@Controller
@RequestMapping(value = "/index")
public class IndexController {
	
	@Autowired
	MyAuthUtils myAuthUtils;
	
	@Autowired
	EpCounterService epCounterServiceImpl;
	
	@Autowired
	HistoryService historyServiceImpl;
	
	
	//嵌入到对方系统中去的index主页方式
	@RequestMapping(value = "/embed", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		//参数格式http://电子处方?action=xxx&user=xxx&adm=xxx&patient=xxx&card=xxx
		
		//提取参数
		String action=request.getParameter("action");
		String user=request.getParameter("user");
		String adm=request.getParameter("adm");
		String patient=request.getParameter("patient");
		//String card=request.getParameter("card");
		
		WlToken wlToken= historyServiceImpl.getWlToken();
		String token=wlToken.getToken();
		//校验 如果参数不完整，跳转到错误页面
		
		//http方式获取信息
		WlAmdParam wlAmdParam=new WlAmdParam(action,
		 token,
		 user,
		 adm,
		 patient);
		WlAmdResponse  wlAmdResponse =historyServiceImpl.getAmdInfo(wlAmdParam);
		Map<String,Object> amdmap=historyServiceImpl.saveDataByWlAmdResponse(wlAmdResponse);//保存基础信息
		
		//获取传递参数
		EpUser epUser=(EpUser) amdmap.get("epUser");
		ModelMap map=myAuthUtils.getAuthInfoByEpUser(epUser);
		
		//获取操作员账号类型  医生或者护士
		String url="";
		if(UserType.DOCTOR.getTypeValue()==epUser.getUsertype()) {
			url="index/embed/doctor";
		}else if(UserType.NURSE.getTypeValue()==epUser.getUsertype()) {
			url="index/embed/nurse";
		}
		return new ModelAndView(url,map);
	}
	

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
