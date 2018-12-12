package com.enze.ep.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WlAmdUser {
	
	public static final String TYPE_DOCTOR="DOCTOR";
	public static final String TYPE_NURSE="NURSE";
	
	private String Id;
	private String Level;
	private String Name;
	private String Type;
	
}
