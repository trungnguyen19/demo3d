package com.trungnd10.hibernate.one2one.join;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			// Configuration config = new
			// AnnotationConfiguration().configure(new
			// File("D:\\Latest Setup\\eclipse_juno_workspace\\hibernate-test-project\\hibernate.cfg.xml"));
			Configuration config = new AnnotationConfiguration()
					.configure("com/trungnd10/hibernate/one2one/join/hibernate.cfg.xml");
			return config.buildSessionFactory();

		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		// Close caches and connection pools
		getSessionFactory().close();
	}
}
