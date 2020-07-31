package com.increff.employee.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sun.istack.NotNull;

@Entity
@Table(name = "Product", uniqueConstraints = { @UniqueConstraint(columnNames = "barcode") })
public class ProductPojo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	private String barcode;
	
	@ManyToOne(optional=false)
	@JoinColumn(name ="brandId")
	private int brandId;
	private String name;
	private double mrp;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brand_category) {
		this.brandId = brand_category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getMrp() {
		return mrp;
	}

	public void setMrp(double mrp) {
		this.mrp = mrp;
	}

}
