package com.enze.ep.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpResult {

	public static final String SUCCESS = "success";
	public static final String FAIL = "fail";

	private String code;
	private String msg;
	private String memo;

	
	@Override
	public String toString() {
		return "EpResult [code=" + code + ", msg=" + msg + ", memo=" + memo + "]";
	}

}
