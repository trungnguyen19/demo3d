package com.mycompany.store;

import java.util.Date;

import org.hibernate.Session;

public class Test {
	public static void main(String[] args) {
		System.out.println("Maven + Hibernate + MySQL");
		Session session = HibernateUtil.getSessionFactory().openSession();

		session.beginTransaction();
		DeliveryDetail deliveryDetail = new DeliveryDetail();

		deliveryDetail.setDeliveryManName("trungnd10");
		deliveryDetail.setExpiryDate(new Date());
		deliveryDetail.setNote("note");
		deliveryDetail.setProductInformation("product info");
		deliveryDetail.setProductName("product name");
		deliveryDetail.setReceiverName("receiver name");
		deliveryDetail.setReceiveDate(new Date());
		deliveryDetail.setState("Tot");
		deliveryDetail.setTelephoneNum("0123456789");

		session.save(deliveryDetail);
		session.getTransaction().commit();
	}
}
