package com.enze.ep.utils;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.enze.ep.entity.EpConfig;
import com.enze.ep.entity.TbCounter;
import com.enze.ep.entity.EpUser;
import com.enze.ep.enums.UserType;
import com.enze.ep.service.EpConfigService;
import com.enze.ep.service.EpCounterService;
import com.enze.ep.service.EpSectionService;

@Service
public class MyAuthUtils {
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	EpCounterService epCounterServiceImpl;
	
	@Autowired
	EpSectionService epSectionServiceImpl;
	
	@Autowired
	EpConfigService epConfigServiceImpl;
	
	public  final String cookiename="authtoken";//给cookie设置的name名称
	public final String secretKey="ezyysoftezyysoftezyysoft";
	
	public String getAuthToken(HttpServletRequest request, HttpServletResponse response) {
		String cookieValue="";
		Cookie[] cookies=request.getCookies();
		for(Cookie cookie:cookies) {
			String _cookieName=cookie.getName();
			String _cookieValue=cookie.getValue();
			if(cookiename.equals(_cookieName)) {
				cookieValue=_cookieValue;
			}
			continue;
		}
		return cookieValue;
	} 
	
	public EpUser getEpUserByCookie(HttpServletRequest request, HttpServletResponse response) {
		String cookieValue=getAuthToken(request,response);
		EpUser epUser=(EpUser) redisTemplate.opsForValue().get(cookieValue);
		return epUser;
	} 
	
	
	public  ModelMap getAuthInfo(HttpServletRequest request, HttpServletResponse response) {
		ModelMap map=new ModelMap();
		EpUser epUser=getEpUserByCookie(request, response);
		map.put("epUser", epUser);

		EpConfig epConfigHospital=epConfigServiceImpl.findEpConfigByCfgName("ep_hospital");
		map.put("hospital", epConfigHospital.getCfgvalue());
		map.put("sectionname", epSectionServiceImpl.findEpSectionByID(epUser.getSectionid()).getSectionname());
		
        //护士用户所对应的柜台 
		if(epUser.getUsertype()==UserType.NURSE.getTypeValue()) {
			List<TbCounter> epCounterList=epCounterServiceImpl.findCounterBySectionid(epUser.getSectionid());
			map.put("epCounterList", epCounterList);
			String counterids="";
			for(TbCounter c : epCounterList) {
				counterids+=c.getIcounterid()+",";
			}
			if(!"".equals(counterids))
				counterids=counterids.substring(0, counterids.length()-1);
			map.put("counterids", counterids);
		}else if(epUser.getUsertype()==UserType.DOCTOR.getTypeValue()){
			//获取配置文件信息
			EpConfig epConfig =epConfigServiceImpl.findEpConfigByCfgName("ep_doctor_counter_set");
			map.put("counterids", epConfig.getCfgvalue());
		}
		
		
		return map;
	}

}
