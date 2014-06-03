package com.mycompany.store;

import java.util.Date;

public class DeliveryDetail implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9170572703191287052L;

	private Integer productId;

	private String deliveryManName;
	private String telephoneNum;

	private String productName;
	private String productInformation;

	private String note;

	private String receiverName;
	private Date receiveDate;
	private String state;
	private Date expiryDate;

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getDeliveryManName() {
		return deliveryManName;
	}

	public void setDeliveryManName(String deliveryManName) {
		this.deliveryManName = deliveryManName;
	}

	public String getTelephoneNum() {
		return telephoneNum;
	}

	public void setTelephoneNum(String telephoneNum) {
		this.telephoneNum = telephoneNum;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductInformation() {
		return productInformation;
	}

	public void setProductInformation(String productInformation) {
		this.productInformation = productInformation;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
}
