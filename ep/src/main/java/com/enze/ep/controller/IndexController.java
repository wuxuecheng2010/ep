package com.enze.ep.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.enze.ep.entity.EpConfig;
import com.enze.ep.entity.EpInpatient;
import com.enze.ep.entity.EpSection;
import com.enze.ep.entity.EpUser;
import com.enze.ep.entity.WlAmdParam;
import com.enze.ep.entity.WlAmdResponse;
import com.enze.ep.entity.WlToken;
import com.enze.ep.enums.UserType;
import com.enze.ep.service.EpConfigService;
import com.enze.ep.service.EpCounterService;
import com.enze.ep.service.HistoryService;
import com.enze.ep.utils.DateUtils;
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
	
	@Autowired
    RedisTemplate redisTemplate;
	
	@Autowired
	EpConfigService epConfigServiceImpl;
	
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
		EpInpatient epInpatient=(EpInpatient)amdmap.get("epInpatient");
		EpSection epSection=(EpSection)amdmap.get("epSection");
		ModelMap map=myAuthUtils.getAuthInfoByEpUser(epUser);
		map.put("epInpatient", epInpatient);
		map.put("epSection", epSection);
		
		//就诊日期
		String credate=DateUtils.formatDateToShortStr(new Date());
		map.put("credate", credate);
		
		EpConfig epConfig=epConfigServiceImpl.findEpConfigByCfgName("ep_contact");
		String contact=epConfig.getCfgvalue();
		map.put("contact", contact);
		
		//获取操作员账号类型  医生或者护士
		String url="";
		if(UserType.DOCTOR.getTypeValue()==epUser.getUsertype()) {
			url="index/embed/doctor";
		}else if(UserType.NURSE.getTypeValue()==epUser.getUsertype()) {
			//护士开出的单据 所对应的柜台  由病人的病区决定
			int sectionid=epInpatient.getSectionid();
			String counterids=epCounterServiceImpl.findCouteridsBySectionid(sectionid);
			map.put("counterids", counterids);//覆盖原来的柜台id字符串
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
	
	

}
