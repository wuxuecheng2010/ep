package com.enze.ep.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WlAmdPatient {
	
	public static final String Prefix_Redis_Key="WlAmdPatient";
	public static final String Prefix_Redis_Key_Separtor="-";

	private String Address;
	private String Age;
	private String Dob;
	private String IDNo;
	private String Name;
	private String RegNo;
	private String Sex;
	private String SocialStatus;
	private String Tel;
    	
}
