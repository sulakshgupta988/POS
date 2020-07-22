package com.increff.employee.dto;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.model.OrderItemsForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemsPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.model.ApiException;
import com.increff.employee.model.OrderItemsData;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.OrderItemsService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;

@Service
public class OrderDto {
	@Autowired
	private ProductService productService;

	@Autowired
	private OrderItemsService orderItemsService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private InventoryService inventoryService;

	@Transactional(rollbackOn = ApiException.class)
	public void add(List<OrderItemsForm> orderItemsForm) throws ApiException {
		OrderPojo order = new OrderPojo();
		order = orderService.add(order);
		for (OrderItemsForm form : orderItemsForm) {
			orderItemsService.add(convert(form, order));
		}
	}

	@Transactional
	public OrderItemsData getProduct(String barcode) throws ApiException {
		ProductPojo product = productService.select(barcode);
		return convert(product, barcode);
	}

	private OrderItemsData convert(ProductPojo product, String barcode) throws ApiException {
		OrderItemsData orderItemsData = new OrderItemsData();
		InventoryPojo inventoryPojo = inventoryService.get(product.getId());
		orderItemsData.setBarcode(barcode);
		orderItemsData.setMrp(product.getMrp());
		orderItemsData.setProductName(product.getName());
		orderItemsData.setProductQuantity(inventoryPojo.getQuantity());
		return orderItemsData;

	}

	private OrderItemsPojo convert(OrderItemsForm orderItemsForm, OrderPojo orderPojo) throws ApiException {
		OrderItemsPojo orderItemsPojo = new OrderItemsPojo();
		ProductPojo productPojo = productService.select(orderItemsForm.getBarcode());
		InventoryPojo inventoryPojo = new InventoryPojo();
		inventoryPojo.setQuantity(0 - orderItemsForm.getQuantity());
		inventoryService.updatePlus(productPojo.getId(), inventoryPojo);

		orderItemsPojo.setOrderId(orderPojo.getId());
		orderItemsPojo.setProductId(productPojo.getId());
		orderItemsPojo.setQuantity(orderItemsForm.getQuantity());
		orderItemsPojo.setSellingPrice(productPojo.getMrp());

		return orderItemsPojo;
	}

}
