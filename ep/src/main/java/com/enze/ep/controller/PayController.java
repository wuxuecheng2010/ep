package com.enze.ep.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enze.ep.utils.wx.WeChatParams;
import com.enze.ep.utils.wx.WeixinPay;

@Controller
@RequestMapping("/pay")
public class PayController {
	
	@RequestMapping(value="/wx",method=RequestMethod.GET)
	public String getPayQRCode(HttpServletRequest request,HttpServletResponse response) {
		WeChatParams ps=new WeChatParams();
		ps.setTotal_fee("1");
		ps.setBody("货物");
		ps.setMemberid("100000000");
		ps.setOut_trade_no("JY0001000001");
		ps.setAttach("没有了");
		String codeurl="";
		try {
			codeurl = WeixinPay.getCodeUrl(ps);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return codeurl;
	}
	@ResponseBody
	@RequestMapping(value="/qr",method=RequestMethod.GET)
	public byte[] getPayQrCodeByte(HttpServletRequest request,HttpServletResponse response) {
		String c="1234560";
		byte[] bytes=WeixinPay.encodeQrcodeToByte(c);
		return bytes;
	}
	
}
