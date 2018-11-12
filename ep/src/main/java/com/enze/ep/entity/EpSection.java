package com.enze.ep.entity;

import lombok.Data;

@Data
public class EpSection {
	
	
	public static final String Prefix_Redis_Key="EpSection";
	public static final String Prefix_Redis_Key_Separtor="-";
	
	private int sectionid;
	private String hissectionid;
	private String sectionname;
	private String usestatus;
}
