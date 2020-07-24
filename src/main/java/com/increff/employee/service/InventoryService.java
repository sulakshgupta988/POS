package com.increff.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.ApiException;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;

@Service
public class InventoryService {

	@Autowired
	private InventoryDao dao;
	
	@Autowired
	private ProductDao productDao;
	

	@Transactional(rollbackFor = ApiException.class)
	public void add(InventoryPojo inventoryPojo) throws ApiException {
		if (inventoryPojo.getQuantity() < 0) {
			throw new ApiException("Quantity cannot be negative.");
		}
		if(dao.get(inventoryPojo.getId())!= null) {
			throw new ApiException("The inventory for this barcode already exists.");
		}
		dao.add(inventoryPojo);

	}

	@Transactional(readOnly = true)
	public InventoryPojo get(int id) throws ApiException {
		getCheck(id);
		return dao.get(id);
	}

	@Transactional(readOnly = true)
	public List<InventoryPojo> getAll() {
		return dao.getAll();
	}

	@Transactional(rollbackFor = ApiException.class)
	public void updatePlus(int id, InventoryPojo inventoryPojo) throws ApiException {
		InventoryPojo existing = getCheck(id);
		int newQuantity = inventoryPojo.getQuantity() + existing.getQuantity();
		if (newQuantity < 0) {
			ProductPojo productPojo = productDao.select(id);
			throw new ApiException("Total Quantity of " + productPojo.getName() +  " cannot be negative. " + existing.getQuantity() +  " left.");
		}
		existing.setQuantity(newQuantity);
	}

	protected InventoryPojo getCheck(int id) throws ApiException {
		InventoryPojo pojo = dao.get(id);
		if (pojo == null) {
			throw new ApiException("Product id = " + id + " does not exists. ");
		}
		return pojo;
	}

}
