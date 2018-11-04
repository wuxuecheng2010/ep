package com.enze.ep.entity;

import lombok.Data;

@Data
public class EpInpatient {

	private int inid;
	private int hisinid;
	private String name;
	private String idcard;
	private int age;
	private String bedno;
	private int sectionid;
	private String describe;
	private String memo;
	private int usestatus;
	private String address;
	private String title;

}
