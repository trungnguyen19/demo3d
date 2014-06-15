package com.javaworld.memcache;

import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.w3c.dom.Document;

public class HibernateHelper {
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory(){
		try {
			if(sessionFactory == null)
				sessionFactory = new Configuration().configure().buildSessionFactory();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sessionFactory;
	}
	
	public static Document parseConfiguration(String resourcePath) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		FileInputStream fileInputStream = new FileInputStream("C:/writing/memcache/ManageContact/src/main/resources/hibernate.cfg.xml");
		System.out.println("Resource Path " + resourcePath);
		System.out.println("Resource Input Stream " + fileInputStream);
		return builder.parse(fileInputStream);
	}
}
