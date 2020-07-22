package com.increff.employee.service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.model.ApiException;
import com.increff.employee.pojo.InventoryPojo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InventoryServiceTest extends AbstractUnitTest {

	@Autowired
	private InventoryService inventoryService;

	private InventoryPojo inventoryPojo;

	@Autowired
	private InventoryDao dao;

	@Before
	public void init() {
		inventoryPojo = new InventoryPojo();
		inventoryPojo.setQuantity(100);
		inventoryPojo.setId(1);
	}

	// Test for adding into inventory
	@Test
	public void testAdd() throws ApiException {
		inventoryService.add(inventoryPojo);
		assertEquals(1, dao.getAll().size());
	}

	//Test for getting the inventory
	@Test
	public void testGet() throws ApiException {
		inventoryService.add(inventoryPojo);
		assertEquals(1, inventoryService.get(inventoryPojo.getId()).getId());
	}
	
	
	//Test for getting whole inventory
	@Test
	public void testGetAll() throws ApiException {
		InventoryPojo newInventory = new InventoryPojo();
		InventoryPojo newInventory2 = new InventoryPojo();
		newInventory.setQuantity(100);
		newInventory.setId(2);
		newInventory2.setQuantity(25);
		newInventory.setId(3);
		dao.add(inventoryPojo);
		dao.add(newInventory);
		dao.add(newInventory2);
		assertEquals(3, inventoryService.getAll().size());
	}

	// Test GetCheck if Inventory for a product Exists
	@Test
	public void getCheckTestOne() throws ApiException {
		dao.add(inventoryPojo);
		assertEquals(inventoryPojo, inventoryService.getCheck(inventoryPojo.getId()));
	}

	// Test GetCheck if Inventory for a product does not Exist
	@Test
	public void getCheckTestTwo() throws ApiException {
		try {
			inventoryService.getCheck(33);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	// Test for adding into inventory
	@Test
	public void testUpdatePlus() throws ApiException {
		dao.add(inventoryPojo);
		inventoryService.updatePlus(inventoryPojo.getId(), inventoryPojo);
		assertEquals(200, dao.get(inventoryPojo.getId()).getQuantity());
	}

	// Test for subtracting from inventory
	@Test
	public void testUpdatePlusTwo() throws ApiException {
		dao.add(inventoryPojo);
		InventoryPojo inventoryPojo2 = new InventoryPojo();
		inventoryPojo2.setQuantity(-30);
		inventoryPojo2.setId(inventoryPojo.getId());
		inventoryService.updatePlus(inventoryPojo.getId(), inventoryPojo2);
		assertEquals(70, dao.get(inventoryPojo.getId()).getQuantity());
	}

}
