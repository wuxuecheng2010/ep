package com.enze.ep.entity;

import lombok.Data;

@Data
public class EpConfig {
	
	
	public static final String Prefix_Redis_Key="EpConfig";
	public static final String Prefix_Redis_Key_Separtor="-";
	
	private int cfgid;
	private String cfgname;
	private String cfgvalue;
	private String cfgmemo;
}
