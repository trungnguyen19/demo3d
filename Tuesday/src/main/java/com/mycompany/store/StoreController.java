package com.mycompany.store;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	@Path("/product/edit/id/{productId}")
	public Viewable index(@Context HttpServletRequest request,
			@PathParam("productId") String productIdString) {
		Session session = HibernateUtil.getSessionFactory().openSession();

		int productId = 0;
//		String deliveryManName;
//		String telephoneNum;
//
//		String productName;
//		String productInformation;
//
//		String note;
//
//		String receiverName;
//		Date receiveDate;
//		String state;
//		Date expiryDate;
		try {
			productId = Integer.parseInt(productIdString);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		session.beginTransaction();
		DeliveryDetail deliveryDetail = new DeliveryDetail();

		// deliveryDetail.setDeliveryManName("trungnd10");
		// deliveryDetail.setExpiryDate(new Date());
		// deliveryDetail.setNote("note");
		// deliveryDetail.setProductInformation("product info");
		// deliveryDetail.setProductName("product name");
		// deliveryDetail.setReceiverName("receiver name");
		// deliveryDetail.setReceiveDate(new Date());
		// deliveryDetail.setState("Tot");
		// deliveryDetail.setTelephoneNum("0123456789");
		//
		// session.save(deliveryDetail);

		Query query = session
				.createQuery("from DeliveryDetail dd where dd.productId = :productId");
		query.setParameter("productId", productId);
		DeliveryDetail dd = (DeliveryDetail) query.uniqueResult();
//		List<DeliveryDetail> list = query.list();
//		System.out.println(list);

		session.getTransaction().commit();

//		request.setAttribute("productId", list.get(productId).getProductId());
//		request.setAttribute("deliveryManName", list.get(productId).getDeliveryManName());
//		request.setAttribute("telephoneNum", list.get(productId).getTelephoneNum());
//		request.setAttribute("productName", list.get(productId).getProductName());
//		request.setAttribute("productInformation", list.get(productId).getProductInformation());
//		request.setAttribute("note", list.get(productId).getNote());
//		request.setAttribute("receiverName", list.get(productId).getReceiverName());
//		request.setAttribute("receiveDate", list.get(productId).getReceiveDate());
//		request.setAttribute("state", list.get(productId).getState());
//		request.setAttribute("expiryDate", list.get(productId).getExpiryDate());
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
		return new Viewable("/edit.jsp", null);
	}
}
