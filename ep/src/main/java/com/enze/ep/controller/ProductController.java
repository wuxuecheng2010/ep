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

import com.enze.ep.entity.EpProduct;
import com.enze.ep.service.EpProductService;

@Controller
@RequestMapping(value="/product")
public class ProductController {
	
	@Autowired
	EpProductService epProductServiceImpl;
	
	@ResponseBody
	@PostMapping("/select/{counterids}")
	public Map<String,List<EpProduct>> productSelect(HttpServletRequest  request,HttpServletResponse response,@PathVariable(name="counterids")String counterids) {
		String productName=request.getParameter("keyword");
		List<EpProduct> list=epProductServiceImpl.findEpProductListByProductNameAndCounters(counterids, productName);
		Map<String,List<EpProduct>> map=new HashMap<String,List<EpProduct>>();
		map.put("data", list);
		return map;
	}
	

}
