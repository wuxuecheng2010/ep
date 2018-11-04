package com.enze.ep.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
@Slf4j
@WebFilter(filterName="logfilter",urlPatterns="/*")
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
       // log.info("init..........");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        
    	
    	HttpServletRequest request=(HttpServletRequest) servletRequest;
    	HttpServletResponse response=(HttpServletResponse) servletResponse;
    	

        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
    	//log.info("destroy.......");
    }
}
