package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.dto.BrandDto;
import com.increff.employee.model.ApiException;
import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandController {

	@Autowired
	private BrandDto dto;

	@ApiOperation(value = "Add 1 Brand")
	@RequestMapping(path = "/api/brand", method = RequestMethod.POST)
	public void add(@RequestBody BrandForm form) throws ApiException {
		dto.add(form);
	}

	@ApiOperation(value = "Deletes a Brand")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		dto.delete(id);
	}

	@ApiOperation(value = "Get all Brands")
	@RequestMapping(path = "/api/brand", method = RequestMethod.GET)
	public List<BrandData> getAll() throws ApiException {
		return dto.getAll();
	}

	@ApiOperation(value = "Get a Brand by Id")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
	public BrandData get(@PathVariable int id) throws ApiException {
		return dto.get(id);
	}

	@ApiOperation(value = "Update a Brand")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandForm form) throws ApiException {
		dto.update(id, form);
	}

}