package com.enze.ep.entity;

import lombok.Data;

@Data
public class EpInpatient {
	
	public static final String Prefix_Redis_Key="EpInpatient";
	public static final String Prefix_Redis_Key_Separtor="-";

	private int inid;
	private String hisinid;
	private String name;
	private String idcard;
	private String age;
	private String bedno;
	private int sectionid;
	private String describe;
	private String memo;
	private int usestatus;
	private String address;
	private String title;
	
	private String tel;
	private String type;
	private String socialstatus;
	private String room;
	private String dob;
	private String sex;
	private String regno;
	

}
