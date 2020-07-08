package com.increff.employee.dao;

import java.util.List;


import javax.persistence.Query;
import javax.persistence.TypedQuery;


import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;

@Repository
public class ProductDao extends AbstractDao {

	private String select_product = "select p from ProductPojo p where barcode =:barcode";
	private String select_id = "select p from ProductPojo p where id =:id";
	private String select_all = "select p from ProductPojo p";
	private String delete_product = "delete from ProductPojo p where barcode =:barcode";


	public void add(ProductPojo p) {
		em.persist(p);
	}

	public int delete(String barcode) {
		Query query = em.createQuery(delete_product);
		query.setParameter("barcode", barcode);
		return query.executeUpdate();
	}
	
	public ProductPojo select(int id) {
		TypedQuery<ProductPojo> query = getQuery(select_id, ProductPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public ProductPojo select(String barcode) {
		barcode = StringUtil.toLowerCase(barcode);
		TypedQuery<ProductPojo> query = getQuery(select_product, ProductPojo.class);
		query.setParameter("barcode", barcode);
		return getSingle(query);
	}

	public List<ProductPojo> selectAll() {
		TypedQuery<ProductPojo> query = getQuery(select_all, ProductPojo.class);
		return query.getResultList();
	}

}
