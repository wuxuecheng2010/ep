package com.enze.ep.entity;

import lombok.Data;

@Data
public class EpPayInfo {
	private int payinfoid;
	private int paytypeid;//支付类型
	private int busstypeid;//业务类型
	private String ordercode ;//订单编号
	private String plordercode; //平台交易ID
	private String baseinfo;//支付平台相关信息组合
	private String fee;//订单金额   单位分
	private String attach ;//附加信息
	private String paydate;//支付时间
	
	@Override
	public String toString() {
		return "EpPayInfo [payinfoid=" + payinfoid + ", paytypeid=" + paytypeid + ", busstypeid=" + busstypeid
				+ ", ordercode=" + ordercode + ", plordercode=" + plordercode + ", baseinfo=" + baseinfo + ", fee="
				+ fee + ", attach=" + attach + ", paydate=" + paydate + "]";
	}
	
}
