package com.enze.ep.service;

import java.util.Map;

import com.enze.ep.entity.WlAmdParam;
import com.enze.ep.entity.WlAmdResponse;
import com.enze.ep.entity.WlToken;

public interface HistoryService {

	/**
	 * 
	 * @Title: getAmdInfo @Description: 获取就诊信息 @param @param
	 *         wlAmdParam @param @return 参数 @author wuxuecheng @return WlAmdResponse
	 *         返回类型 @throws
	 */
	WlAmdResponse getAmdInfo(WlAmdParam wlAmdParam);

	WlAmdResponse formatWlAmdInfoStringToEntity(String resStr);

	/**
	 * 
	* @Title: getWlToken
	* @Description: 登入
	* @param @param wlLoginParam
	* @param @return    参数
	* @author wuxuecheng
	* @return String    返回类型   
	* @throws
	 */
	WlToken getWlToken();
	
	Map<String,Object>  saveDataByWlAmdResponse(WlAmdResponse wlAmdResponse);
	

}
