package com.javaworld.memcache.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class CachingResponseWrapper extends HttpServletResponseWrapper{
	HttpServletResponse originalResponse;
	PrintWriter responseWriter;
	
	
	public CachingResponseWrapper(HttpServletResponse response) {
		super(response);
		originalResponse = response;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if(responseWriter == null)
		 responseWriter = new CachingResponseWriter(originalResponse.getWriter());
		return responseWriter;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return super.getOutputStream();
	}

}
