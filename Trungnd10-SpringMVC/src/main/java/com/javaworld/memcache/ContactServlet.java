package com.javaworld.memcache;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContactServlet extends HttpServlet {
	Logger log = LoggerFactory.getLogger(ContactServlet.class);
	
	

	@Override
	public void init() throws ServletException {
		ContactDAO contactDAO = new ContactDAOImpl();
		for(int i = 0 ; i < 5 ; i++){
		Contact contact = new Contact();
		contact.setFirstName("FirstName-" +i);
		contact.setLastName("LastName-"+i);
		contact.setEmail("Email-"+i);
		contactDAO.insertContact(contact);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(req, resp);
	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String action = request.getParameter("action");
			log.debug("Value of action parameter " + action);
			if (action == null) {
				log.debug("Action parameter is not set, let show list ");
				action = "list";
			}

			if (action.equals("select")) {
				log.debug("Value of action parameter is select ");
				String contactId = request.getParameter("contactId");
				log.debug("Value of contactId " + contactId);
				ContactDAO contactDAO = new ContactDAOImpl();
				Contact contact = contactDAO.getContact(Integer
						.parseInt(contactId));
				request.setAttribute("contact", contact);
				getServletContext().getRequestDispatcher("/detail.jsp")
						.forward(request, response);
			} else if (action.equals("list")) {
				ContactDAO contactDAO = new ContactDAOImpl();
				List<Contact> contactList = contactDAO.getContacts();
				request.setAttribute("contactList", contactList);
				getServletContext().getRequestDispatcher("/summary.jsp")
						.forward(request, response);
			} else if (action.equals("update")) {
				ContactDAO contactDAO = new ContactDAOImpl();
				if (request.getParameter("Cancel") == null) {
					System.out.println("Cancel button was clicked skipping");
					Contact contact = new Contact();
					BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
					beanUtilsBean.populate(contact, request.getParameterMap());
					System.out.println("Contact bean after populating "
							+ contact);

					contactDAO.updateContact(contact);
				}

				List<Contact> contactList = contactDAO.getContacts();
				request.setAttribute("contactList", contactList);
				getServletContext().getRequestDispatcher("/summary.jsp")
						.forward(request, response);

			} else if (action.equals("insert")) {
				ContactDAO contactDAO = new ContactDAOImpl();
				if (request.getParameter("Cancel") == null) {
					System.out.println("Cancel button was clicked skipping");
					Contact contact = new Contact();
					BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
					beanUtilsBean.populate(contact, request.getParameterMap());
					System.out.println("Contact bean after populating "
							+ contact);

					contactDAO.insertContact(contact);
				}
				List<Contact> contactList = contactDAO.getContacts();
				request.setAttribute("contactList", contactList);
				getServletContext().getRequestDispatcher("/summary.jsp")
						.forward(request, response);
			} else if (action.equals("insertDisplay")) {
				getServletContext().getRequestDispatcher("/new.jsp").forward(
						request, response);
			} else if (action.equals("delete")) {
				String contactId = request.getParameter("contactId");
				log.debug("Value of contactId " + contactId);
				ContactDAO contactDAO = new ContactDAOImpl();
				contactDAO.deleteContact(Integer.parseInt(contactId));
				List<Contact> contactList = contactDAO.getContacts();
				request.setAttribute("contactList", contactList);
				getServletContext().getRequestDispatcher("/summary.jsp")
						.forward(request, response);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
