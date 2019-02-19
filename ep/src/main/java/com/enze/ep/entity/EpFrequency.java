package com.enze.ep.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpFrequency {
	public static final String Prefix_Redis_Key="EpFrequency";
	public static final String Prefix_Redis_Key_Separtor="-";
	private String title;
	private int frequencyid;
	private String frequency;
	private String vccode;
}
