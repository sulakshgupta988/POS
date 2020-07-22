package com.increff.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderItemsDao;
import com.increff.employee.pojo.OrderItemsPojo;

@Service
public class OrderItemsService {
	
	@Autowired
	private OrderItemsDao dao;
	
	public void add(OrderItemsPojo orderItemsPojo) {
		dao.add(orderItemsPojo);
	}
	
	public List<OrderItemsPojo> getOrder(int orderId){
		return dao.getOrder(orderId);
	}
}
