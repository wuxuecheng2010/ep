package com.enze.ep.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.enze.ep.entity.EpUser;
import com.enze.ep.utils.MyAuthUtils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(filterName = "authfilter", urlPatterns = "/index/*")
public class AuthFilter implements Filter {
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private MyAuthUtils myAuthUtils;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		EpUser epUser=null;
		int usertype=0;
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/";
		String reqmapping=request.getServletPath();

		Cookie[] cookies = request.getCookies();
		String loginurl = basePath + "login";
		if (cookies == null) {
			response.sendRedirect(loginurl);
		}else {
			/*for(Cookie cookie:cookies) {
				//authtoken.
				String _cookiename= cookie.getName();
				String _cookievalue=cookie.getValue();
				if("authtoken".equals(_cookiename)) {
					//根绝authtoken 获取用户信息
					EpUser epUser=(EpUser) redisTemplate.opsForValue().get(_cookievalue);
					usertype=epUser.getUsertype();
					System.out.println(epUser.getUsercode());
					continue;
				}
			}*/
		    epUser=myAuthUtils.getEpUserByCookie(request, response);
		}
		if(epUser!=null)
			usertype=epUser.getUsertype();
		//根据usertype 和当前请求的路径  ，判断当前用户是否进入正确的页面
		if("/index/doctor".equals(reqmapping)||"/index/nurse".equals(reqmapping))
		if((usertype==1 && reqmapping.equals("/index/doctor"))||(usertype==2 && reqmapping.equals("/index/nurse"))) {
			filterChain.doFilter(servletRequest, servletResponse);
		}else {
			//response.sendRedirect(loginurl);
			switch (usertype) {
			case 1:
				response.sendRedirect(basePath+"index/doctor");
				break;
			case 2:
				response.sendRedirect(basePath+"index/nurse");
				break;
			default:
				response.sendRedirect(loginurl);
				break;
			}
			
		}

		//filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {

	}
}
