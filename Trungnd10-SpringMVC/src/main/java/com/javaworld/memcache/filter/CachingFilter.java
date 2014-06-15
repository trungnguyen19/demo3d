package com.javaworld.memcache.filter;

import java.io.IOException;
import java.util.NoSuchElementException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.javaworld.memcache.ContactServlet;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;


/**
 * Servlet Filter implementation class CachingFilter
 */
public class CachingFilter implements Filter {
	Logger log = LoggerFactory.getLogger(ContactServlet.class);

    public CachingFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		log.debug("Inside CachingFilter.doFilter() " );
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest)request;
			HttpServletResponse httpServletResponse = (HttpServletResponse)response;
			MemcachedClient  memCachedClient = new MemcachedClient(AddrUtil.getAddresses("localhost:11211"));
			StringBuffer cacheKeyBuffer = new StringBuffer();
			cacheKeyBuffer.append(httpServletRequest.getContextPath());
			cacheKeyBuffer.append(httpServletRequest.getServletPath());
			if(httpServletRequest.getQueryString() != null){
				cacheKeyBuffer.append("?");
				cacheKeyBuffer.append(httpServletRequest.getQueryString());
			}
			
			String cacheKey = httpServletResponse.encodeURL(cacheKeyBuffer.toString());
			log.debug ("Get Path Info  " + cacheKey);
			String cachedResponse =(String) memCachedClient.get(cacheKey);
			
			if( cachedResponse == null){
				log.debug ("Response is not cached forwarding control to servlet");
				CachingResponseWrapper cachingResponseWrapper =new CachingResponseWrapper(httpServletResponse);
				chain.doFilter(request, cachingResponseWrapper);
				CachingResponseWriter collectResponseWriter = (CachingResponseWriter)cachingResponseWrapper.getWriter();
				String collectedResponseStr = collectResponseWriter.getCollectedResponse();
				log.debug ( "Set value in the memcached for key " + collectedResponseStr);
				memCachedClient.set(cacheKey, 300, collectedResponseStr).get();
			}else{
				log.debug ("Returning cached response ");
				response.setContentType("text/html");
				response.getWriter().println(cachedResponse);
			}
			memCachedClient.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	
	}

}
