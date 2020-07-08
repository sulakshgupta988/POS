package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandController {

	@Autowired
	private BrandService service;

	@ApiOperation(value = "Add 1 Brand")
	@RequestMapping(path = "/api/brand", method = RequestMethod.POST)
	public void add(@RequestBody BrandForm form) throws ApiException {
		BrandPojo p = convert(form);
		service.add(p);
	}

	@ApiOperation(value = "Deletes a Brand")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Get all Brands")
	@RequestMapping(path = "/api/brand", method = RequestMethod.GET)
	public List<BrandData> getAll() throws ApiException {
		List<BrandPojo> brandList = service.getAll();
		List<BrandData> data = new ArrayList<BrandData>();
		for (BrandPojo p : brandList) {
			data.add(convert(p));
		}
		return data;
	}

	@ApiOperation(value = "Get a Brand by Id")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.GET)
	public BrandData get(@PathVariable int id) throws ApiException {
		return convert(service.get(id));
	}

	@ApiOperation(value = "Update a Brand")
	@RequestMapping(path = "/api/brand/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody BrandForm form) throws ApiException {
		BrandPojo p = convert(form);
		service.update(id, p);
	}

	protected static BrandData convert(BrandPojo p) {
		BrandData d = new BrandData();
		d.setId(p.getId());
		d.setCategory(p.getCategory());
		d.setBrand(p.getBrand());
		return d;
	}

	protected static BrandPojo convert(BrandForm form) {
		BrandPojo p = new BrandPojo();
		p.setBrand(form.getBrand());
		p.setCategory(form.getCategory());
		return p;
	}

}
