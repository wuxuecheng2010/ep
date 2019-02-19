package com.enze.ep.entity;

import lombok.Data;

@Data
public class EpUsage {
	public static final String Prefix_Redis_Key="epUsage";
	public static final String Prefix_Redis_Key_Separtor="-";
	private String title;
	private int usageid;
	private String usage;
	private String vccode;
}
