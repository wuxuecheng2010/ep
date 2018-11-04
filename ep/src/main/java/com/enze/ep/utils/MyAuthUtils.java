package com.enze.ep.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.enze.ep.entity.EpUser;

@Service
public class MyAuthUtils {
	@Autowired
	private RedisTemplate redisTemplate;
	
	public  final String cookiename="authtoken";//给cookie设置的name名称
	public final String secretKey="ezyysoftezyysoftezyysoft";
	
	public String getAuthToken(HttpServletRequest request, HttpServletResponse response) {
		String cookieValue="";
		Cookie[] cookies=request.getCookies();
		for(Cookie cookie:cookies) {
			String _cookieName=cookie.getName();
			String _cookieValue=cookie.getValue();
			if(cookiename.equals(_cookieName)) {
				cookieValue=_cookieValue;
			}
			continue;
		}
		return cookieValue;
	} 
	
	public EpUser getEpUserByCookie(HttpServletRequest request, HttpServletResponse response) {
		String cookieValue=getAuthToken(request,response);
		EpUser epUser=(EpUser) redisTemplate.opsForValue().get(cookieValue);
		return epUser;
	} 
	

}
