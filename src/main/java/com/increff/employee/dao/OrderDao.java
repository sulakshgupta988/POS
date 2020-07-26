package com.increff.employee.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.OrderPojo;

@Repository
public class OrderDao extends AbstractDao {
	private static String select_by_date = "select p from OrderPojo p where date between :startDate and :endDate";
	private static String select_all = "select p from OrderPojo p order by p.id desc";

	public OrderPojo add(OrderPojo order) {
		order.setDate(new Date());
		em.persist(order);
		return order;
	}

	public List<OrderPojo> getOrdersBetweenDates(Date startDate, Date endDate) {
		TypedQuery<OrderPojo> query = getQuery(select_by_date, OrderPojo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	public List<OrderPojo> getAll() {
		TypedQuery<OrderPojo> query = getQuery(select_all, OrderPojo.class);
		return query.getResultList();
	}
}
