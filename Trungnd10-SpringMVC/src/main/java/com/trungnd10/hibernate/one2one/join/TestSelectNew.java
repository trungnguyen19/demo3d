package com.trungnd10.hibernate.one2one.join;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.trungnd10.hibernate.one2one.join.dto.foreignKeyAsso.AccountEntity;
import com.trungnd10.hibernate.one2one.join.dto.foreignKeyAsso.EmployeeEntity;

public class TestSelectNew {
	public static void main(String[] args) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		AccountEntity account = new AccountEntity();
		account.setAccountNumber("123-345-65454");

		// Add new Employee object
		EmployeeEntity emp = new EmployeeEntity();
		emp.setEmail("demo-user@mail.com");
		emp.setFirstName("demo");
		emp.setLastName("user");

		// Save Account
		session.saveOrUpdate(account);
		// Save Employee
		emp.setAccount(account);
		session.saveOrUpdate(emp);

		session.getTransaction().commit();

		test(session);

		HibernateUtil.shutdown();
	}

	public static void test(Session session) {
		Query query = session
				.createQuery("select new ForeignKeyAssEmployeeEntity(ee.employeeId,ee.email,ee.firstName,ee.lastName,a) from ForeignKeyAssEmployeeEntity ee inner join ee.account as a");
		EmployeeEntity item = (EmployeeEntity) query.uniqueResult();
		System.out.println(item.getFirstName() + " " + item.getLastName() + " "
				+ item.getEmployeeId());
		System.out.println(item);
	}
}
