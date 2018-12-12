package com.enze.ep.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WlToken {
	public static final String Prefix_Redis_Key="WlToken";
	public static final String Prefix_Redis_Key_Separtor="-";
	private String RetCode;
	private String Token;
	private String ExpireIn;
	private String Expires;

}
