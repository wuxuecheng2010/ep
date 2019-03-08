package com.enze.ep.service;

import java.util.Map;

import com.enze.ep.entity.EpInpatient;
import com.enze.ep.entity.WlAmdParam;
import com.enze.ep.entity.WlAmdPatient;
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
	
	WlAmdPatient formatWlAmdPatientStringToEntity(String resStr);

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
	
	/**
	 * 
	* @Title: getEpInpatientByCardNo
	* @Description: 根据刷卡卡号获取病人信息
	* @param @param map  action + cardNo +token   
	* @param @return    参数
	* @author wuxuecheng
	* @return EpInpatient    返回类型
	* @throws
	 */
	WlAmdPatient getWlAmdPatientByCardNo(Map<String,String> map);
	

}
