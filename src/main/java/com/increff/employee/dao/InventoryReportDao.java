package com.increff.employee.dao;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

@Repository
public class InventoryReportDao extends AbstractDao {

	private static String select_all = "select b.brand, b.category, sum(i.quantity) from brandp b, InventoryPojo i, "
			+ "product p where p.id = i.id and b.id = p.brandId group by b.brand, b.category";

	public List<Object[]> getAll() {
		Query query = em.createNativeQuery(select_all);
		return query.getResultList();
	}

}
