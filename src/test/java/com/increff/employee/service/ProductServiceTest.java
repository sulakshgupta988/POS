package com.increff.employee.service;

import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.ApiException;
import com.increff.employee.pojo.ProductPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProductServiceTest extends AbstractUnitTest {

	@Autowired
	private ProductService productService;

	private ProductPojo productPojo;

	@Autowired
	private ProductDao dao;

	@Before
	public void init() {
		productPojo = new ProductPojo();
		productPojo.setBarcode("qwerty");
		productPojo.setName(" Omen 500 ");
		productPojo.setBrandId(1);
		productPojo.setMrp(200000.32);
	}

	// Insert test
	@Test
	public void testAdd1() throws ApiException {
		productService.add(productPojo);
		assertEquals(1, dao.selectAll().size());
	}

	// Same entries Test
	@Test
	public void testAdd2() throws ApiException {
		productService.add(productPojo);
		try {
			productService.add(productPojo);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	//
	@Test
	public void testSelectAll() throws ApiException {
		ProductPojo newProduct = new ProductPojo();
		ProductPojo newProduct2 = new ProductPojo();
		newProduct.setName(" drishti ");
		newProduct.setBarcode("pat5r5");
		newProduct.setBrandId(3);
		newProduct.setMrp(99.99);
		newProduct2.setName(" milk 300ml ");
		newProduct2.setBarcode("rdfg");
		newProduct2.setBrandId(2);
		newProduct2.setMrp(99.99);
		dao.add(productPojo);
		dao.add(newProduct);
		dao.add(newProduct2);
		assertEquals(3, productService.selectAll().size());
	}

	@Test
	public void testUpdate() throws ApiException {
		dao.add(productPojo);
		ProductPojo newProduct = new ProductPojo();
		newProduct.setName(" shamPoo 500ml ");
		newProduct.setBarcode("pan5");
		newProduct.setBrandId(1);
		newProduct.setMrp(99.99);
		productService.update(productPojo.getId(), newProduct);
		assertEquals("pan5", dao.select(productPojo.getId()).getBarcode());
	}

	// Test for getCheck
	@Test
	public void getCheckTest() throws ApiException {
		dao.add(productPojo);
		assertEquals(productPojo, productService.getCheck(productPojo.getId()));
	}

	@Test
	public void getTest() throws ApiException {
		dao.add(productPojo);
		assertEquals(productPojo, productService.select(productPojo.getId()));
	}
	
	// Select 1 product test
	@Test
	public void selectTest() throws ApiException {
		dao.add(productPojo);
		assertEquals(productPojo, productService.select(productPojo.getBarcode()));
	}

	//Test for removing extra spaces in strings
	@Test
	public void testNormalizeOne() throws ApiException {
		productService.normalize(productPojo);
		assertEquals("omen 500", productPojo.getName());
	}

	// Empty product name test
	@Test
	public void testNormalizeTwo() throws ApiException {
		productPojo.setName("");
		try {
			productService.normalize(productPojo);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

}
