package com.venticas.domain;

public class Order {
	private int id;
	private String orderDate;
	private String shipDate;
	private String trackingNumber;
	private float totalValue;
	private String shippingAddress;
	private OrderStatus status;
	
	public Order() {
		
	}
	
	public Order(int id, String orderDate, String shipDate, String trackingNumber, float totalValue, String shippingAddress,
			OrderStatus status) {
		super();
		this.id = id;
		this.orderDate = orderDate;
		this.shipDate = shipDate;
		this.trackingNumber = trackingNumber;
		this.totalValue = totalValue;
		this.shippingAddress = shippingAddress;
		this.status = status;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public float getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(float totalValue) {
		this.totalValue = totalValue;
	}
	public String getShippingAddress() {
		return shippingAddress;
	}
	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getShipDate() {
		return shipDate;
	}

	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	
	
	

}
