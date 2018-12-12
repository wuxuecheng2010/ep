package com.enze.ep.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EpSection {
	
	
	public static final String Prefix_Redis_Key="EpSection";
	public static final String Prefix_Redis_Key_Separtor="-";
	public static final String Prefix_Redis_Key_Separtor_H="--";//adm信息提取物分隔符
	
	private int sectionid;
	private String hissectionid;
	private String sectionname;
	private String usestatus;
}
