package com.increff.employee.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.BrandPojo;

@Repository
public class BrandDao extends AbstractDao {

	private static String select_id = "select p from BrandPojo p where id=:id";
	private static String select_all = "select p from BrandPojo p";
	private static String delete_id = "delete from BrandPojo p where id=:id";
	private static String get_name_category = "select p from BrandPojo p where brand=:name and category=:category";

	public void add(BrandPojo p) {
		em.persist(p);
	}

	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public BrandPojo select(int id) {
		TypedQuery<BrandPojo> query = getQuery(select_id, BrandPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query = getQuery(select_all, BrandPojo.class);
		return query.getResultList();
	}

	public BrandPojo select(String name, String category) {
		TypedQuery<BrandPojo> query = getQuery(get_name_category, BrandPojo.class);
		query.setParameter("name", name);
		query.setParameter("category", category);
		return getSingle(query);
	}

}
