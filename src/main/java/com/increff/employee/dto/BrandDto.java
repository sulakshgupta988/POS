package com.increff.employee.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;

@Service
public class BrandDto {

	@Autowired
	private BrandService service;

	public void add(BrandForm form) throws ApiException {
		BrandPojo p = convert(form);
		service.add(p);
	}

	public void delete(int id) {
		service.delete(id);
	}

	public List<BrandData> getAll() throws ApiException {
		List<BrandPojo> brandList = service.getAll();
		List<BrandData> data = new ArrayList<BrandData>();
		for (BrandPojo p : brandList) {
			data.add(convert(p));
		}
		return data;
	}

	public BrandData get(int id) throws ApiException {
		return convert(service.get(id));
	}

	public void update(int id, BrandForm form) throws ApiException {
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
