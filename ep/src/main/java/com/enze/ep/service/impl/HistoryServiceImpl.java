package com.enze.ep.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.enze.ep.dao.EpSectionDAO;
import com.enze.ep.entity.EpInpatient;
import com.enze.ep.entity.EpSection;
import com.enze.ep.entity.EpUser;
import com.enze.ep.entity.WlAmd;
import com.enze.ep.entity.WlAmdParam;
import com.enze.ep.entity.WlAmdPatient;
import com.enze.ep.entity.WlAmdResponse;
import com.enze.ep.entity.WlAmdUser;
import com.enze.ep.entity.WlLoginParam;
import com.enze.ep.entity.WlToken;
import com.enze.ep.service.EpInpatientService;
import com.enze.ep.service.EpSectionService;
import com.enze.ep.service.EpUserService;
import com.enze.ep.service.HistoryService;
import com.enze.ep.utils.HttpClientUtil;

@Service
public class HistoryServiceImpl implements HistoryService {

	@Value("${wlyy.server.path}")
	private String url;
	
	
	@Value("${wlyy.server.login.action}")		
	private String loginAction;
	
	@Value("${wlyy.server.login.user}")
	private String loginUser;
	
	@Value("${wlyy.server.login.pwd}")
	private String loginPwd;
	
	
	@Autowired
	RedisTemplate redisTemplate;

	@Autowired
	EpSectionDAO epSectionDAO;
	
	@Autowired
	EpSectionService epSectionServiceImpl;
	
	@Autowired
	EpUserService epUserServiceImpl;
	
	@Autowired
	EpInpatientService epInpatientServiceImpl;
	
	

	@Override
	public WlAmdResponse getAmdInfo(WlAmdParam wlAmdParam) {
		//redies中查找是否已经存在
		String key =  WlAmdResponse.Prefix_Redis_Key + WlAmdResponse.Prefix_Redis_Key_Separtor 
				      +wlAmdParam.getAction() 
				      + WlAmdResponse.Prefix_Redis_Key_Separtor
				      +wlAmdParam.getAdm()
				      + WlAmdResponse.Prefix_Redis_Key_Separtor
				      +wlAmdParam.getUser()
				      + WlAmdResponse.Prefix_Redis_Key_Separtor
				      +wlAmdParam.getPatient()
				      ;
		WlAmdResponse wlAmdResponse = (WlAmdResponse) redisTemplate.opsForValue().get(key);
		if(wlAmdResponse==null) {
			//没有就http获取并解析
			//WlAmdResponse wlAmdResponse = null;
			Map<String, String> map = new HashMap<String, String>();
			map = wlAmdParam.toMap();
			String resStr = HttpClientUtil.postMap(url, map);
			if (resStr != null && !"".equals(resStr))
				wlAmdResponse = formatWlAmdInfoStringToEntity(resStr);
			//保存解析结果中的基本信息
			//存入redies
			redisTemplate.opsForValue().set(key, wlAmdResponse);
			redisTemplate.expire(key, 2, TimeUnit.HOURS);//保存59分钟
			//保存基础信息
			
			
		}
		return wlAmdResponse;
	}

	@Override
	public WlAmdResponse formatWlAmdInfoStringToEntity(String resStr) {
		
		JSONObject jsonObject=(JSONObject) JSON.parse(resStr);
		String RetCode=jsonObject.getString("jsonObject");
		String RetInfo=jsonObject.getString("RetInfo");
		String SysTime=jsonObject.getString("SysTime");
		
		JSONObject jsonObjectUser=jsonObject.getJSONObject("User");
		String Id=jsonObjectUser.getString("Id");
		String Level=jsonObjectUser.getString("Level");
		String Name=jsonObjectUser.getString("Name");
		String Type=jsonObjectUser.getString("Type");
		WlAmdUser wlAmdUser=new WlAmdUser(
				Id,
				Level,
				Name,
				Type);
		
		JSONObject jsonObjectPatient=jsonObject.getJSONObject("Patient");
		String Address=jsonObjectPatient.getString("Address");
		String Age=jsonObjectPatient.getString("Age");
		String Dob=jsonObjectPatient.getString("Dob");
		String IDNo=jsonObjectPatient.getString("IDNo");
		String _Name=jsonObjectPatient.getString("Name");
		String RegNo=jsonObjectPatient.getString("RegNo");
		String Sex=jsonObjectPatient.getString("Sex");
		String SocialStatus=jsonObjectPatient.getString("SocialStatus");
		String Tel=jsonObjectPatient.getString("Tel");
		WlAmdPatient wlAmdPatient=new WlAmdPatient(
				 Address,
				 Age,
				 Dob,
				 IDNo,
				 _Name,
				 RegNo,
				 Sex,
				 SocialStatus,
				 Tel);
		
		JSONObject jsonObjectAdm=jsonObject.getJSONObject("Adm");
		String Bed=jsonObjectAdm.getString("Bed");
		String Datetime=jsonObjectAdm.getString("Datetime");
		String DepartmentId=jsonObjectAdm.getString("DepartmentId");
		String DepartmentName=jsonObjectAdm.getString("DepartmentName");
		String Diagnose=jsonObjectAdm.getString("Diagnose");
		String _Id=jsonObjectAdm.getString("Id");
		String Room=jsonObjectAdm.getString("Room");
		String _Type=jsonObjectAdm.getString("Type");
		String WardId=jsonObjectAdm.getString("WardId");
		String WardName=jsonObjectAdm.getString("WardName");
		
		WlAmd wlAmd=new WlAmd( Bed,
		 Datetime,
		 DepartmentId,
		 DepartmentName,
		 Diagnose,
		 _Id,
		 Room,
		 _Type,
		 WardId,
		 WardName);
		
		
		WlAmdResponse WlAmdResponse=new WlAmdResponse( RetCode,
		 RetInfo,
		 SysTime,
		 wlAmdUser,
		 wlAmdPatient,
		 wlAmd);
		
		return WlAmdResponse;
	}

	@Override
	public WlToken getWlToken() {
		//先去redis缓存中去取 没有就去生成
		String key =  WlToken.Prefix_Redis_Key + WlToken.Prefix_Redis_Key_Separtor +loginUser ;
		WlToken wlToken = (WlToken) redisTemplate.opsForValue().get(key);
		if(wlToken==null || "-1".equals(wlToken.getRetCode())) {
			//重新登入获取
			WlLoginParam wlLoginParam=new WlLoginParam(loginAction,loginUser,loginPwd);
			Map<String,String > map=wlLoginParam.toMap();
			String resStr = HttpClientUtil.postMap(url, map);
			JSONObject jsonObject=(JSONObject) JSON.parse(resStr);
			String RetCode=jsonObject.getString("RetCode");
			String Token="";
			String ExpireIn="";
			String Expires="";
			if("0".equals(RetCode)) {
				 Token=jsonObject.getString("Token");
				 ExpireIn=jsonObject.getString("ExpireIn");
				 Expires=jsonObject.getString("Expires");
			}
			wlToken=new WlToken( RetCode,
								 Token,
								 ExpireIn,
								 Expires);
			//存入redies
			redisTemplate.opsForValue().set(key, wlToken);
			redisTemplate.expire(key, 59, TimeUnit.MINUTES);//保存59分钟
		}
		return wlToken;
	}

	@Override
	public Map<String,Object> saveDataByWlAmdResponse(WlAmdResponse wlAmdResponse) {
		Map<String,Object> map=new HashMap<String,Object>();
		//科室列表信息
		EpSection epSection=new EpSection();
		epSection.setSectionname(wlAmdResponse.getAdm().getDepartmentName());//部门
		epSection.setHissectionid(wlAmdResponse.getAdm().getDepartmentId());//部门ID
		epSection.setUsestatus("1");
		//System.out.println("x");
		epSection=epSectionServiceImpl.saveOrFindEpSectionByWlAmdResponseSSection(epSection);
		map.put("epSection", epSection);
		//医生或者护士的工号信息
		EpUser epUser=new EpUser();
		epUser.setHisuserid(Integer.valueOf(wlAmdResponse.getUser().getId()));
		epUser.setUsername(wlAmdResponse.getUser().getName());
		epUser.setUsercode(wlAmdResponse.getUser().getId());
		int usertype=WlAmdUser.TYPE_DOCTOR.equals(wlAmdResponse.getUser().getType())?2:1;
		epUser.setUsertype(usertype);
		epUser.setMemo(wlAmdResponse.getUser().getLevel());
		epUser.setSectionid(epSection.getSectionid());
		epUser.setPassword("0");
		epUser=epUserServiceImpl.saveOrFindEpUserByWlAmdResponseSUser(epUser);
		map.put("epUser", epUser);
		
		//病人基本信息
		EpInpatient epInpatient=new EpInpatient(); 
		epInpatient.setHisinid(Integer.valueOf(wlAmdResponse.getAdm().getId()));
		epInpatient.setName(wlAmdResponse.getPatient().getName());
		epInpatient.setIdcard(wlAmdResponse.getPatient().getIDNo());
		epInpatient.setAge(wlAmdResponse.getPatient().getAge());
		epInpatient.setBedno(wlAmdResponse.getAdm().getBed());
		epInpatient.setSectionid(epSection.getSectionid());
		epInpatient.setDescribe(wlAmdResponse.getAdm().getDiagnose());
		epInpatient.setAddress(wlAmdResponse.getPatient().getAddress());
		epInpatient.setTel(wlAmdResponse.getPatient().getTel());
		epInpatient.setType(wlAmdResponse.getAdm().getType());
		epInpatient.setSocialstatus(wlAmdResponse.getPatient().getSocialStatus());
		epInpatient.setRoom(wlAmdResponse.getAdm().getRoom());
		epInpatient.setDob(wlAmdResponse.getPatient().getDob());
		epInpatient.setSex(wlAmdResponse.getPatient().getSex());
		epInpatient.setRegno(wlAmdResponse.getPatient().getRegNo());
		epInpatient=epInpatientServiceImpl.saveOrFindEpInpatientByWlAmdResponseSEpInpatient(epInpatient);
		map.put("epInpatient", epInpatient);
		
		return map;
	}

}
