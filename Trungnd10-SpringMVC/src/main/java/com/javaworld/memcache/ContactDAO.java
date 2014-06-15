package com.javaworld.memcache;

import java.util.List;

public interface ContactDAO {

	public Contact getContact(int contactId);
	
	public List<Contact> getContacts();
	
	public void deleteContact(int contactId);
	
	public void insertContact(Contact contact);
	
	public void updateContact(Contact contact);
}
