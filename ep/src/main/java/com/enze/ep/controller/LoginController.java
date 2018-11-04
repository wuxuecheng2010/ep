package com.enze.ep.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.enze.ep.entity.EpUser;
import com.enze.ep.service.EpUserService;
import com.enze.ep.utils.DESUtils;
import com.enze.ep.utils.MyAuthUtils;
import com.enze.ep.utils.URLCodeUtils;
import com.enze.ep.utils.UUIDUtils;

@Controller
@RequestMapping(value = "/")
public class LoginController {
	
	//private String secretKey="ezyysoftezyysoftezyysoft";
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private MyAuthUtils myAuthUtils;
	
	@Autowired
	EpUserService epUserServiceImpl;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
		
		//创建token赋值给登入页面
		String token=UUIDUtils.getUUID();
		ModelMap map=new ModelMap();
		map.put("token", token);
		
		//获取提示信息
	    String info =request.getParameter("info");
	    try {
	    	if(info!=null)
			info=URLCodeUtils.decoding(info,"UTF-8");//java.net.URLDecoder.decode(info,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("info", info);
		
		//在session会话中保存token，备下次提交试用
		request.getSession().setAttribute("token", token);
		
		return new ModelAndView("login",map);
	}
	
	@PostMapping("/checkLogin")
	public String checkLogin(HttpServletRequest request, HttpServletResponse response) {
		
		ModelMap map=new ModelMap();
		String info="";//信息
		boolean flag=true;
		
		//检查会话token与提交的token是否存在一致;
		String token = request.getParameter("token");
		String _token=(String)request.getSession().getAttribute("token");
		
		if("".equals(token)||token==null) {
			info="非法提交";
			flag=false;
		}else {
			if(token.equals(_token)){
				//校验正确，销毁token
				request.getSession().removeAttribute("token");
			}else {
				//非法提交
				info="非法提交";
				flag=false;
			}
		}
		
		if(!flag) {
			try {
				info=URLCodeUtils.encoding(info,"UTF-8");//java.net.URLEncoder.encode(info,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String url="redirect:/login?info="+info;
			//token检验失败  直接重定向到登入页面
			return "redirect:/login?info="+info;
		}
		
		//检查账号用户名
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		EpUser epUser=epUserServiceImpl.findEpUserByUserName(username);
		if(epUser==null) {
			info="用户不存在";
			try {
				info=URLCodeUtils.encoding(info,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "redirect:/login?info="+info;
		}else if(epUser.getPassword().equals(password)) {
			//密码正确 设置cookie   并把cookie信息存放到redis中去
			String authtoken=UUIDUtils.getUUID();
			authtoken=DESUtils.encrypt(authtoken, myAuthUtils.secretKey);
			Cookie cookie=new Cookie(myAuthUtils.cookiename,authtoken);
			response.addCookie(cookie);
			redisTemplate.opsForValue().set(authtoken, epUser);
			//stringRedisTemplate.opsForValue().set(authtoken, epUser);
			
		}else {
			info="密码不正确";
			try {
				info=URLCodeUtils.encoding(info,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return "redirect:/login?info="+info;
		}
		
		//判断账号类型
		int usertype=epUser.getUsertype();
		//定向到对应主页
		String target="";
		switch(usertype) {
		case 1:
			target= "redirect:/index/doctor";
			break;
		case 2:
			target= "redirect:/index/nurse";
			break;
		default:
			target= "redirect:/login?info="+info;
			break;
		}
		return target;
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		String authtoken=myAuthUtils.getAuthToken(request, response);
		//1、设置cookie
		Cookie cookie=new Cookie(myAuthUtils.cookiename, "");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		//2、设置redis信息
		redisTemplate.delete(authtoken);//.opsForValue()..set(authtoken, epUser);
		//3、重定向到登入页面
		return "redirect:/login";
	}

}
