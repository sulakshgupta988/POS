package com.increff.employee.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InventoryPojo {

	@Id
	private int id;
	private int quantity;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
