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
import com.increff.employee.util.StringUtil;

@Service
public class ProductService {

	@Autowired
	private ProductDao dao;
	
	@Autowired 
	private InventoryDao inventoryDao;

	@Transactional
	public void add(ProductPojo p) throws ApiException {
		normalize(p);
		validate(p);
		dao.add(p);
	}

	@Transactional
	public void delete(String barcode) throws ApiException {
		barcode = StringUtil.toLowerCase(barcode);
		checkBarcode(barcode);
		ProductPojo productPojo = dao.select(barcode);
		InventoryPojo inventoryPojo = inventoryDao.get(productPojo.getId());
		if(inventoryPojo != null) {
			inventoryDao.delete(inventoryPojo.getId());
		}
		dao.delete(barcode);
	}

	@Transactional(readOnly = true)
	public ProductPojo select(String barcode) throws ApiException {
		checkBarcode(barcode);
		return dao.select(barcode);
	}

	@Transactional(readOnly = true)
	public List<ProductPojo> selectAll() {
		return dao.selectAll();
	}

	@Transactional
	public void update(int id, ProductPojo p) throws ApiException {
		normalize(p);
		validate(p);
		getCheck(id);
		ProductPojo ex = dao.select(id);
		ex.setBarcode(p.getBarcode());
		ex.setBrandId(p.getBrandId());
		ex.setId(id);
		ex.setMrp(p.getMrp());
		ex.setName(p.getName());
	}

	@Transactional(readOnly = true)
	public ProductPojo select(int id) throws ApiException {
		return getCheck(id);
	}

	protected ProductPojo getCheck(int id) throws ApiException {
		ProductPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Id = " + id + " does not exists.");
		}
		return p;
	}

	private void checkBarcode(String barcode) throws ApiException {
		if (dao.select(barcode) == null) {
			throw new ApiException("Barcode = " + barcode + " does not exists. ");
		}

	}

	protected void validate(ProductPojo p) throws ApiException {
		if (StringUtil.isEmpty(p.getName())) {
			throw new ApiException("name cannot be empty");
		}
		if (StringUtil.isEmpty(p.getBarcode())) {
			throw new ApiException("Barcode cannot be empty");
		}
		if (p.getMrp() < 0) {
			throw new ApiException("MRP cannot be negative");
		}
		if (dao.select(p.getBarcode()) != null) {
			throw new ApiException("Barcode " + p.getBarcode() + " is assigned to product " + dao.select(p.getBarcode()).getName() + ".");
		}

	}

	protected void normalize(ProductPojo p) {
		p.setBarcode(StringUtil.toLowerCase(p.getBarcode()));
		p.setName(StringUtil.toLowerCase(p.getName()));

	}
}