package com.enze.ep.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WlAmd {
	private String Bed;
	private String Datetime;
	private String DepartmentId;
	private String DepartmentName;
	private String Diagnose;
	private String Id;
	private String Room;
	private String Type;
	private String WardId;
	private String WardName;
}

