package com.javaworld.memcache;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactDAOImpl implements ContactDAO {
	Logger log = LoggerFactory.getLogger(ContactDAOImpl.class);

	@Override
	public Contact getContact(int contactId) {
		log.debug("Entering ContactDAOImpl.getContact " + contactId);
		SessionFactory sessionFactory = HibernateHelper.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Contact returnContact = (Contact)session.get(Contact.class, contactId);
		transaction.commit();
		session.close();
		log.debug("Exiting ContactDAOImpl.getContact " + returnContact);

		return returnContact;
	}

	@Override
	public List<Contact> getContacts() {
		log.debug("Entering ContactDAOImpl.getContacts " );

		SessionFactory sessionFactory = HibernateHelper.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
		List<Contact> contactList=(List<Contact>) session.createQuery("from Contact").list();
		t.commit();
		session.close();
		log.debug("Exiting ContactDAOImpl.getContacts " + contactList );

		return contactList;
	}

	@Override
	public void deleteContact(int contactId) {
		log.debug("Entering ContactDAOImpl.deleteContact " + contactId );
		SessionFactory sessionFactory = HibernateHelper.getSessionFactory();

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
		Contact deleteContact = new Contact();
		deleteContact.setContactId(contactId);
		session.delete(deleteContact);
		t.commit();
		session.close();
		log.debug("Exiting ContactDAOImpl.deleteContact " );

	}

	@Override
	public void insertContact(Contact contact) {
		log.debug("Entering ContactDAOImpl.insertContact " + contact );
		SessionFactory sessionFactory = HibernateHelper.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
		session.save(contact);
		t.commit();
		session.close();
		log.debug("Exiting ContactDAOImpl.insertContact " + contact );

	}

	@Override
	public void updateContact(Contact contact) {
		log.debug("Entering ContactDAOImpl.updateContact " + contact );
		SessionFactory sessionFactory = HibernateHelper.getSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
		session.saveOrUpdate(contact);
		t.commit();
		session.close();
		log.debug("Exiting ContactDAOImpl.updateContact " + contact );

	}

}
