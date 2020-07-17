package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.dto.ProductDto;
import com.increff.employee.model.ApiException;
import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductController {

	@Autowired
	private ProductDto dto;

	@ApiOperation(value = "Select All Employees")
	@RequestMapping(path = "/api/product", method = RequestMethod.GET)
	public List<ProductData> getAll() throws ApiException {
		return dto.getAll();
	}

	@ApiOperation(value = "Select 1 Employees")
	@RequestMapping(path = "/api/product/{barcode}", method = RequestMethod.GET)
	public ProductData get(@PathVariable String barcode) throws ApiException {
		return dto.get(barcode);
	}

	@ApiOperation(value = "Adds a product")
	@RequestMapping(path = "/api/product", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm form) throws ApiException {
		dto.add(form);
	}

	@ApiOperation(value = "Deletes a product")
	@RequestMapping(path = "/api/product/{barcode}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String barcode) throws ApiException {
		dto.delete(barcode);
	}

	@ApiOperation(value = "Updates a product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm form) throws ApiException {
		dto.update(id, form);
	}

}