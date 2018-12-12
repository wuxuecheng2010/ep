package com.enze.ep.entity;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @ClassName: WlAmdParam
 * @Description: 温岭医院电子处方所需信息请求参数
 * @author wuxuecheng
 * @date 2018年12月10日
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WlAmdParam {
	private String action;// 查询方法名称
	private String token;// 令牌
	private String user;// 操作员
	private String adm;// 就诊ID
	private String patient;// 病人ID
	// private String callback;//jsonp回调

	public Map<String ,String> toMap() {
		Map<String ,String> map = new HashMap<String ,String>();
		map.put("action", action);
		map.put("token", token);
		map.put("user", user);
		map.put("adm", adm);
		map.put("patient", patient);
		return map;

	}
}
