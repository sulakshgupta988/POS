package com.increff.employee.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.model.ApiException;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.StringUtil;

@Service
public class InventoryDto {

	@Autowired
	private InventoryService service;

	@Autowired
	private ProductService productService;

	public void add(InventoryForm inventoryForm) throws ApiException {
		InventoryPojo inventoryPojo = convert(inventoryForm);
		service.add(inventoryPojo);
	}

	public InventoryData get(int id) throws ApiException {
		InventoryPojo inventoryPojo = service.get(id);
		return convert(inventoryPojo);
	}

	public List<InventoryData> getAll() throws ApiException {
		List<InventoryPojo> inventoryList = service.getAll();
		List<InventoryData> dataList = new ArrayList<InventoryData>();

		for (InventoryPojo p : inventoryList) {
			dataList.add(convert(p));
		}

		return dataList;
	}

	public void update(int id, InventoryForm inventoryForm) throws ApiException {
		InventoryPojo inventoryPojo = convert(inventoryForm);
		if (id != inventoryPojo.getId()) {
			throw new ApiException("The product id and barcode does not match");
		}
		service.update(id, inventoryPojo);
	}

	private InventoryPojo convert(InventoryForm inventoryForm) throws ApiException {
		InventoryPojo inventoryPojo = new InventoryPojo();
		if (StringUtil.isEmpty(inventoryForm.getBarcode())) {
			throw new ApiException("Barcode cannot be empty");
		}
		ProductPojo productPojo = productService.select(inventoryForm.getBarcode());
		if (productPojo == null) {
			throw new ApiException(
					"Barcode " + inventoryForm.getBarcode() + " is not assigned to any existing product.");
		}
		inventoryPojo.setId(productPojo.getId());
		inventoryPojo.setQuantity(inventoryForm.getQuantity());

		return inventoryPojo;
	}

	private InventoryData convert(InventoryPojo inventoryPojo) throws ApiException {
		InventoryData inventoryData = new InventoryData();
		ProductPojo productPojo = productService.select(inventoryPojo.getId());
		inventoryData.setBarcode(productPojo.getBarcode());
		inventoryData.setId(productPojo.getId());
		inventoryData.setProductName(productPojo.getName());
		inventoryData.setQuantity(inventoryPojo.getQuantity());

		return inventoryData;
	}
}
