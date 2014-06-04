package com.mycompany.store;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.hibernate.Query;
import org.hibernate.Session;

import com.sun.jersey.api.view.Viewable;

@Path("/hello")
@Produces(MediaType.TEXT_HTML)
public class StoreController { // Jersey

	@GET
	@Path("/say/{param}")
	// @Produces(MediaType.TEXT_XML)
	public Response getMsg(@PathParam("param") String msg) {
		String output = "Jersey say : " + msg;
		return Response.status(200).entity(output).build();
	}

	@GET
	@Path("/list")
	public Response list(@Context HttpServletRequest request) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery("from DeliveryDetail");
		request.setAttribute("list", query.list());
		session.getTransaction().commit();
		return Response.ok(new Viewable("/list.jsp", null)).build();
	}

	@GET
	@Path("/new")
	public Response create(@Context HttpServletRequest request,
			@PathParam("productId") String productIdString) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		DeliveryDetail deliveryDetail = new DeliveryDetail();

		deliveryDetail.setDeliveryManName("");
		deliveryDetail.setExpiryDate(new Date());
		deliveryDetail.setNote("");
		deliveryDetail.setProductInformation("");
		deliveryDetail.setProductName("");
		deliveryDetail.setReceiverName("");
		deliveryDetail.setReceiveDate(new Date());
		deliveryDetail.setState("");
		deliveryDetail.setTelephoneNum("");

		String output = "before:" + deliveryDetail.getProductId();
		session.save(deliveryDetail);
		output += "\n" + "after:" + deliveryDetail.getProductId();

		session.getTransaction().commit();
		return Response.status(200).entity(output).build();
	}

	@GET
	@Path("/edit/id/{productId}")
	public Response edit(@Context HttpServletRequest request,
			@Context UriInfo uriInfo,
			@PathParam("productId") String productIdString) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		int productId = 0;
		try {
			productId = Integer.parseInt(productIdString);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		session.beginTransaction();

		Query query = session
				.createQuery("from DeliveryDetail dd where dd.productId = :productId");
		query.setParameter("productId", productId);
		DeliveryDetail dd = (DeliveryDetail) query.uniqueResult();

		session.getTransaction().commit();

		if (dd == null) {
			URI uri = uriInfo.getBaseUriBuilder().path("hello/list").build();
			return Response.seeOther(uri).build();
		}

		request.setAttribute("productId", dd.getProductId());
		request.setAttribute("deliveryManName", dd.getDeliveryManName());
		request.setAttribute("telephoneNum", dd.getTelephoneNum());
		request.setAttribute("productName", dd.getProductName());
		request.setAttribute("productInformation", dd.getProductInformation());
		request.setAttribute("note", dd.getNote());
		request.setAttribute("receiverName", dd.getReceiverName());
		request.setAttribute("receiveDate", dd.getReceiveDate());
		request.setAttribute("state", dd.getState());
		request.setAttribute("expiryDate", dd.getExpiryDate());
		return Response.ok(new Viewable("/edit.jsp", null)).build();
	}

	@GET
	@Path("/test")
	public Response test(@Context HttpServletRequest request) {
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
		return Response.status(200).entity("test ok").build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/edit/submit")
	public Response createMessage(@Context HttpServletRequest request,
			@Context UriInfo uriInfo,
			@FormParam("product_id") String productIdString,
			@FormParam("delivery_man_name") String deliveryManNameString,
			@FormParam("telephone_num") String telephoneNumString,
			@FormParam("product_name") String productNameString,
			@FormParam("product_information") String productInformationString,
			@FormParam("note") String noteString,
			@FormParam("receiver_name") String receiverNameString,
			@FormParam("receive_date") String receiveDateString,
			@FormParam("state") String stateString,
			@FormParam("expiry_date") String expiryDateString/*
															 * ,
															 * 
															 * @FormParam("thelist"
															 * ) List<String>
															 * list
															 */) {

		// if (productIdString.trim().length() > 0
		// && !list.isEmpty() ) {
		// }
		Session session = HibernateUtil.getSessionFactory().openSession();

		int productId = 0;
		String deliveryManName = deliveryManNameString.trim();
		String telephoneNum = telephoneNumString.trim();

		String productName = productNameString.trim();
		String productInformation = productInformationString.trim();

		String note = noteString.trim();

		String receiverName = receiverNameString.trim();
		Date receiveDate = new Date();
		String state = stateString.trim();
		Date expiryDate = new Date();

		try {
			productId = Integer.parseInt(productIdString);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			receiveDate = formatter.parse(receiveDateString.trim());
			expiryDate = formatter.parse(expiryDateString.trim());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		session.beginTransaction();

		DeliveryDetail deliveryDetail = new DeliveryDetail();

		deliveryDetail.setProductId(productId);
		deliveryDetail.setDeliveryManName(deliveryManName);
		deliveryDetail.setExpiryDate(expiryDate);
		deliveryDetail.setNote(note);
		deliveryDetail.setProductInformation(productInformation);
		deliveryDetail.setProductName(productName);
		deliveryDetail.setReceiverName(receiverName);
		deliveryDetail.setReceiveDate(receiveDate);
		deliveryDetail.setState(state);
		deliveryDetail.setTelephoneNum(telephoneNum);

		session.saveOrUpdate(deliveryDetail);
		// session.evict(deliveryDetail);

		session.getTransaction().commit();

		URI uri = uriInfo.getBaseUriBuilder().path("hello/list").build();
		return Response.seeOther(uri).build();
	}
}
