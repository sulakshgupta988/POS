package com.increff.employee.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.pojo.OrderPojo;

@Service
public class OrderService {

	@Autowired
	private OrderDao dao;

	public OrderPojo add(OrderPojo orderPojo) {
		OrderPojo order = dao.add(orderPojo);
		return order;
	}

	public List<OrderPojo> getOrdersBetweenDates(Date startDate, Date endDate) {
		return dao.getOrdersBetweenDates(startDate, endDate);
	}

	public List<OrderPojo> getAll() {
		return dao.getAll();
	}
}
