package com.enze.ep.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
* @ClassName: WlAmdParam
* @Description: 温岭医院信息请求结果
* @author wuxuecheng
* @date 2018年12月10日
*
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WlAmdResponse {
	public static final String Prefix_Redis_Key="WlAmdResponse";
	public static final String Prefix_Redis_Key_Separtor="-";
	
	private String RetCode;
	private String RetInfo;
	private String SysTime;
	private WlAmdUser User;
	private WlAmdPatient Patient;
	private WlAmd Adm;
	
}
