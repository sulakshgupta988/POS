package com.increff.employee.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.ProductService;

@Service
public class ProductDto {
	@Autowired
	private BrandService brandService;

	@Autowired
	private ProductService service;

	public List<ProductData> getAll() throws ApiException {
		List<ProductData> data = new ArrayList<ProductData>();
		List<ProductPojo> list = service.selectAll();
		for (ProductPojo p : list) {
			data.add(convert(p));
		}
		return data;
	}

	public ProductData get(String barcode) throws ApiException {
		ProductData d = new ProductData();
		ProductPojo p = service.select(barcode);
		d = convert(p);
		return d;
	}

	public void add(ProductForm form) throws ApiException {
		ProductPojo p = convert(form);
		service.add(p);
	}

	public void delete(String barcode) throws ApiException {
		service.delete(barcode);
	}

	public void update(@PathVariable int id, @RequestBody ProductForm form) throws ApiException {
		ProductPojo p = convert(form);
		service.update(id, p);
	}

	protected ProductData convert(ProductPojo p) throws ApiException {
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

	protected ProductPojo convert(ProductForm form) throws ApiException {
		ProductPojo p = new ProductPojo();
		BrandPojo bp = brandService.get(form.getBrand_category());
		if (bp == null) {
			throw new ApiException("The given brand and category does not exist is Brand master.");
		}
		p.setBarcode(form.getBarcode());
		p.setBrand_category(form.getBrand_category());
		p.setMrp(form.getMrp());
		p.setName(form.getName());

		return p;
	}
}
