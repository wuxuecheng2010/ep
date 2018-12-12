package com.enze.ep.entity;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WlLoginParam {
	private String action;
	private String user;
	private String pwd;

	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("action", action);
		map.put("user", user);
		map.put("pwd", pwd);
		return map;
	}
}
