package com.increff.employee.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.InventoryPojo;

@Repository
public class InventoryDao extends AbstractDao {

	private String select_id = "select p from InventoryPojo p where id = :id";
	private String select_all = "select p from InventoryPojo p";

	public void add(InventoryPojo inventoryPojo) {
		if (get(inventoryPojo.getId()) == null) {
			em.persist(inventoryPojo);
		} else {
			delete(inventoryPojo.getId());
			em.persist(inventoryPojo);
			
		}
	}

	public InventoryPojo get(int id) {
		TypedQuery<InventoryPojo> query = em.createQuery(select_id, InventoryPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<InventoryPojo> getAll() {
		TypedQuery<InventoryPojo> query = em.createQuery(select_all, InventoryPojo.class);
		return query.getResultList();
	}

	public void delete(int id) {
		InventoryPojo inventoryPojo = em.find(InventoryPojo.class, id);
		if (inventoryPojo == null) {
			return;
		}
		em.remove(inventoryPojo);
	}
}
