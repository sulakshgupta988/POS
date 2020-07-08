package com.increff.employee.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;

@Service
public class BrandService {

	@Autowired
	private BrandDao dao;
	@Autowired
	ProductDao pdao;

	@Transactional(rollbackFor = ApiException.class)
	public void add(BrandPojo p) throws ApiException {
		normalize(p);
		if (StringUtil.isEmpty(p.getBrand())) {
			throw new ApiException("Please Enter some Brand Name");
		}
		if (StringUtil.isEmpty(p.getCategory())) {
			throw new ApiException("Please Enter some Brand Category");
		}
		if (dao.select(p.getBrand(), p.getCategory()) != null) {
			throw new ApiException("Database requires unique combination of Brand and its category.");
		}
		dao.add(p);

	}

	@Transactional()
	public BrandPojo get(int id) throws ApiException {
		getCheck(id);
		return dao.select(id);
	}

	@Transactional()
	public List<BrandPojo> getAll() {
		return dao.selectAll();
	}
	
	@Transactional
	public void delete(int id) {
		List<ProductPojo> list = new ArrayList<ProductPojo>();
		list = pdao.selectByBrandCategory(id);
		for(ProductPojo p : list) {
			pdao.delete(p.getBarcode());
		}
		dao.delete(id);
	}

	@Transactional(rollbackFor = ApiException.class)
	public void update(int id, BrandPojo p) throws ApiException {
		normalize(p);
		if (StringUtil.isEmpty(p.getBrand())) {
			throw new ApiException("Please Enter some Brand Name");
		}
		if (StringUtil.isEmpty(p.getCategory())) {
			throw new ApiException("Please Enter some Brand Category");
		}
		if (dao.select(p.getBrand(), p.getCategory()) != null) {
			throw new ApiException("Database requires unique combination of Brand and its category.");
		}
		BrandPojo ex = getCheck(id);
		ex.setCategory(p.getCategory());
		ex.setBrand(p.getBrand());
		// dao.update(ex);
	}

	private BrandPojo getCheck(int id) throws ApiException {
		BrandPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("id = " + id + " does not exists. ");
		}
		return p;
	}

	private void normalize(BrandPojo p) {
		p.setBrand(StringUtil.toLowerCase(p.getBrand()));
		p.setCategory(StringUtil.toLowerCase(p.getCategory()));

	}

}