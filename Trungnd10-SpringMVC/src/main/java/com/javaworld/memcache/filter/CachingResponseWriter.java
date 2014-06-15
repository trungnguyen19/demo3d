package com.javaworld.memcache.filter;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;

public class CachingResponseWriter extends PrintWriter{
	
	StringBuffer sb = new StringBuffer();

	public CachingResponseWriter(Writer out) {
		
		super(out);
	}

	
	@Override
	public void write(int c) {
		sb.append(c);
		super.write(c);
	}

	@Override
	public void write(char[] buf, int off, int len) {

		sb.append(buf,off,len);
		super.write(buf, off, len);
	}


	@Override
	public void write(String s, int off, int len) {

		sb.append(s,off,len);
		super.write(s, off, len);
	}

/*

	@Override
	public PrintWriter append(CharSequence csq) {
		System.out.println("Inside CollectResponseWriter.append");
		sb.append(csq);
		return super.append(csq);
	}

	@Override
	public PrintWriter append(CharSequence csq, int start, int end) {
		System.out.println("Inside CollectResponseWriter.append");
		sb.append(csq,start,end);
		return super.append(csq, start, end);
	}

	@Override
	public PrintWriter append(char c) {
		System.out.println("Inside CollectResponseWriter.append");
		sb.append(c);
		return super.append(c);
	}
*/
	@Override
	public void write(String s) {
		sb.append(s);
		super.write(s);
	}
	

	@Override
	public void write(char[] buf) {
		sb.append(buf);
		super.write(buf);
	}

	public String getCollectedResponse(){
		
		return sb.toString();
	}

}

