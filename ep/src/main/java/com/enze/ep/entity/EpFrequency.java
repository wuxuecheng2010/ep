package com.enze.ep.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpFrequency {
	private String title;
	private int frequencyid;
	private String frequency;
	private String vccode;
}
