package com.venticas.domain;

import java.util.Date;

public class Transaction {
	private int id;
	private float amount;
	private String creditCardNumber;
	private String type;
	private String bank;
	private Date dateTransaction;
	
	
	public Transaction() {
		
	}
	public Transaction(int id, float amount, String creditCardNumber, String type, String bank, Date dateTransaction) {
		this.id = id;
		this.amount = amount;
		this.creditCardNumber = creditCardNumber;
		this.type = type;
		this.bank = bank;
		this.dateTransaction = dateTransaction;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public Date getDateTransaction() {
		return dateTransaction;
	}
	public void setDateTransaction(Date dateTransaction) {
		this.dateTransaction = dateTransaction;
	}
	
	

}
