package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.dto.OrderDto;
import com.increff.employee.model.ApiException;
import com.increff.employee.model.OrderItemsData;
import com.increff.employee.model.OrderItemsForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderController {
	
	@Autowired
	private OrderDto dto;
	@ApiOperation(value = "Get a product by barcode")
	@RequestMapping(path = "/api/order/{barcode}", method = RequestMethod.GET)
	public OrderItemsData getProduct(@PathVariable String barcode) throws ApiException {
		return dto.getProduct(barcode);
	}
	
	@ApiOperation(value = "Creates an order")
	@RequestMapping(path= "/api/order", method = RequestMethod.POST)
	public void addOrder(@RequestBody List<OrderItemsForm> orderItemsForm) throws ApiException {
		dto.add(orderItemsForm);
	}
	
	
}
