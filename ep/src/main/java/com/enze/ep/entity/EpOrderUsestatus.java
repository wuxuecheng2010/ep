package com.enze.ep.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class EpOrderUsestatus {

	//作废
	public static final int cancel = -1;
	
	// 初始
	public static final int initial = 0;

	// 支付
	public static final int payed = 1;

	// 支付
	public static final int all = 0;

	private int usestatus;
	private String usestatusname;


	public List<EpOrderUsestatus> getEpOrderUsestatusList() {

		List<EpOrderUsestatus> list = new ArrayList<EpOrderUsestatus>();
		EpOrderUsestatus epOrderUsestatus0 = new EpOrderUsestatus();
		epOrderUsestatus0.setUsestatus(0);
		epOrderUsestatus0.setUsestatusname("未支付");
		list.add(epOrderUsestatus0);

		EpOrderUsestatus epOrderUsestatus1 = new EpOrderUsestatus();
		epOrderUsestatus1.setUsestatus(1);
		epOrderUsestatus1.setUsestatusname("已支付");
		list.add(epOrderUsestatus1);

		EpOrderUsestatus epOrderUsestatus2 = new EpOrderUsestatus();
		epOrderUsestatus2.setUsestatus(2);
		epOrderUsestatus2.setUsestatusname("全部");
		list.add(epOrderUsestatus2);

		return list;

	}

}
