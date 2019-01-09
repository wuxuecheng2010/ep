package com.enze.ep.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enze.ep.entity.EpFrequency;
import com.enze.ep.entity.EpUsage;
import com.enze.ep.service.EpFrequencyService;
import com.enze.ep.service.EpUsageService;

@Controller
@RequestMapping(value="/data")
public class DataController {
	
	@Autowired
	EpFrequencyService epFrequencyServiceImpl;
	
	@Autowired
	EpUsageService epUsageServiceImpl;
	
	
	
	@ResponseBody
	@PostMapping("/usage")
	/**
	 * 
	* @Title: usageSelect
	* @Description: 模糊查询用法
	* @param @param request
	* @param @param response
	* @param @return    参数
	* @author wuxuecheng
	* @return Map<String,List<EpUsage>>    返回类型
	* @throws
	 */
	public Map<String,List<EpUsage>> usageSelect(HttpServletRequest  request,HttpServletResponse response) {
		String usage=request.getParameter("keyword");
		List<EpUsage> list=epUsageServiceImpl.findUsageListByUsage(usage);
		Map<String,List<EpUsage>> map=new HashMap<String,List<EpUsage>>();
		map.put("data", list);
		return map;
	}
	
	
	@ResponseBody
	@PostMapping("/frequency")
	/**
	 * 
	* @Title: frequencySelect
	* @Description: 模糊查询频度
	* @param @param request
	* @param @param response
	* @param @return    参数
	* @author wuxuecheng
	* @return Map<String,List<EpFrequency>>    返回类型
	* @throws
	 */
	public Map<String,List<EpFrequency>> frequencySelect(HttpServletRequest  request,HttpServletResponse response) {
		String frequency=request.getParameter("keyword");
		List<EpFrequency> list=epFrequencyServiceImpl.findFrequencyListByFrequency(frequency);
		Map<String,List<EpFrequency>> map=new HashMap<String,List<EpFrequency>>();
		map.put("data", list);
		return map;
	}
	
	
	/**
	 * 
	* @Title: mockSelect
	* @Description: 假的请求链接  解决前端插件 bigAutocomplete  同行最后一个不能自动关闭问题 放一个假的请求路径
	* @param @param request
	* @param @param response
	* @param @return    参数
	* @author wuxuecheng
	* @return Map<String,List<EpFrequency>>    返回类型
	* @throws
	 */
	@ResponseBody
	@PostMapping("/mock")
	public Map<String,List<EpFrequency>> mockSelect(HttpServletRequest  request,HttpServletResponse response) {
		String frequency=request.getParameter("keyword");
		List<EpFrequency> list=new ArrayList<EpFrequency>();
		Map<String,List<EpFrequency>> map=new HashMap<String,List<EpFrequency>>();
		map.put("data", list);
		return map;
	}
	

}
