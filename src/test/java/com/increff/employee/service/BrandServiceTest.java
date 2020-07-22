package com.increff.employee.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.model.ApiException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.BrandPojo;

public class BrandServiceTest extends AbstractUnitTest {

	@Autowired
	private BrandService brandService;

	private BrandPojo brandPojo;

	@Autowired
	private BrandDao brandDao;

	@Before
	public void init(){
		brandPojo = new BrandPojo();
		brandPojo.setCategory("electronics");
		brandPojo.setBrand("HP");
	}

	//Category not set
	@Test
	public void testAddOne() throws ApiException {
		BrandPojo brand = new BrandPojo();
		brand.setBrand(" P&G ");
		try {
			brandService.add(brand);
		}catch(Exception e){
			assertTrue(true);
		}
	}

	//Test Add
	@Test
	public void testAddTwo() throws ApiException {
		brandDao.add(brandPojo);
		assertEquals(1,brandDao.selectAll().size());
	}

	//Brand not Set
	@Test
	public void testAddThree() throws ApiException {
		BrandPojo brand = new BrandPojo();
		brand.setCategory(" health ");
		try {
			brandService.add(brand);
		}catch(Exception e){
			assertTrue(true);
		}
	}

	
	@Test
	public void testGetAll() throws ApiException {
		BrandPojo newBrand=new BrandPojo();
		BrandPojo newBrand2=new BrandPojo();
		newBrand.setBrand("Levis");
		newBrand.setCategory("Apparrel");
		newBrand2.setBrand("Classmate");
		newBrand2.setCategory("Edu");
		brandDao.add(brandPojo);
		brandDao.add(newBrand);
		brandDao.add(newBrand2);
	assertEquals(3,brandService.getAll().size());
	}
	
	//Update in Category
	@Test
	public void testUpdate() throws ApiException {
		brandDao.add(brandPojo);
		BrandPojo newBrand=new BrandPojo();
		newBrand.setBrand("HP");
		newBrand.setCategory("elec");
		brandService.update(brandPojo.getId(),newBrand);
		assertEquals("elec",brandDao.select(brandPojo.getId()).getCategory());
	}
	
	//Update in Both brand and Category
	@Test
	public void testUpdateTwo() throws ApiException {
		brandDao.add(brandPojo);
		BrandPojo newBrand=new BrandPojo();
		newBrand.setBrand("Classmate");
		newBrand.setCategory("Edu");
		brandService.update(brandPojo.getId(),newBrand);
		assertEquals("edu",brandDao.select(brandPojo.getId()).getCategory());
		assertEquals("classmate",brandDao.select(brandPojo.getId()).getBrand());
	}

	//Whether GetCheck works properly
	@Test
	public void testGetCheck() throws ApiException {
		brandDao.add(brandPojo);
		assertEquals(brandPojo,brandService.getCheck(brandPojo.getId()));
	}

	
	// Test for gettin a brand
	@Test
	public void testGet() throws ApiException {
		brandDao.add(brandPojo);
		assertEquals(brandPojo,brandService.get(brandPojo.getId()));
	}

	//Test for Normalize
	@Test
	public void testNormalize() {
		BrandService.normalize(brandPojo);
		assertEquals("hp", brandPojo.getBrand());
		assertEquals("electronics",brandPojo.getCategory());
	}

}
