package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductController {

	@Autowired
	private BrandService brandService;

	@Autowired
	private ProductService service;

	@ApiOperation(value = "Select All Employees")
	@RequestMapping(path = "/api/product", method = RequestMethod.GET)
	public List<ProductData> getAll() throws ApiException {
		List<ProductData> data = new ArrayList<ProductData>();
		List<ProductPojo> list = service.selectAll();
		for (ProductPojo p : list) {
			data.add(convert(p));
		}
		return data;
	}

	@ApiOperation(value = "Select 1 Employees")
	@RequestMapping(path = "/api/product/{barcode}", method = RequestMethod.GET)
	public ProductData get(@PathVariable String barcode) throws ApiException {
		ProductData d = new ProductData();
		ProductPojo p = service.select(barcode);
		d = convert(p);
		return d;
	}

	@ApiOperation(value = "Adds a product")
	@RequestMapping(path = "/api/product", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm form) throws ApiException {
		ProductPojo p = convert(form);
		service.add(p);
	}

	@ApiOperation(value = "Deletes a product")
	@RequestMapping(path = "/api/product/{barcode}", method = RequestMethod.DELETE)
	public void delete(@PathVariable String barcode) throws ApiException {
		service.delete(barcode);
	}

	@ApiOperation(value = "Updates a product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm form) throws ApiException {
		ProductPojo p = convert(form);
		service.update(id, p);
	}

	private ProductData convert(ProductPojo p) throws ApiException {
		ProductData d = new ProductData();
		BrandPojo bp = brandService.get(p.getBrand_category());
		d.setBarcode(p.getBarcode());
		d.setBrand_category(p.getBrand_category());
		d.setName(p.getName());
		d.setId(p.getId());
		d.setMrp(p.getMrp());
		d.setBrandName(bp.getBrand());
		d.setCategory(bp.getCategory());
	
		return d;
	}

	private ProductPojo convert(ProductForm form) throws ApiException {
		ProductPojo p = new ProductPojo();
		BrandPojo bp = brandService.get(form.getBrand_category());
		if(bp == null) {
			throw new ApiException("The given brand and category does not exist is Brand master.");
		}
		p.setBarcode(form.getBarcode());
		p.setBrand_category(form.getBrand_category());
		p.setMrp(form.getMrp());
		p.setName(form.getName());
		
		return p;
	}
}