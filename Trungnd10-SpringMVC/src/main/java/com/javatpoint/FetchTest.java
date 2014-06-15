package com.javatpoint;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FetchTest {
	public static void main(String[] args) {
//		Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
		Configuration cfg = new Configuration().configure("javatpoint/hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();

		Session session1 = factory.openSession();
		Employee emp1 = (Employee) session1.load(Employee.class, 121);
		System.out.println(emp1.getId() + " " + emp1.getName() + " "
				+ emp1.getSalary());
		session1.close();

		Session session2 = factory.openSession();
		Employee emp2 = (Employee) session2.load(Employee.class, 121);
		System.out.println(emp2.getId() + " " + emp2.getName() + " "
				+ emp2.getSalary());
		session2.close();

	}
}