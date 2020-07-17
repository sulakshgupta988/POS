package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.dto.InventoryDto;
import com.increff.employee.model.ApiException;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryController {

	@Autowired
	private InventoryDto dto;
	
	@ApiOperation(value="Add product to inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm inventoryForm) throws ApiException {
		dto.add(inventoryForm);
	}
	
	@ApiOperation(value = "Get Inventory By id")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable int id) throws ApiException {
		return dto.get(id);
	}
	
	@ApiOperation(value = "Get Whole Inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() throws ApiException {
		return dto.getAll();
	}
	
	@ApiOperation(value = "Update a product in Inventory")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody InventoryForm inventoryForm) throws ApiException {
		dto.update(id, inventoryForm);
	}
	
	
	
}
