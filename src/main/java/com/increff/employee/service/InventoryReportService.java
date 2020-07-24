package com.increff.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.InventoryReportDao;

@Service
public class InventoryReportService {

	@Autowired
	private InventoryReportDao dao;
	
	public List<Object[]> getAll(){
		return dao.getAll();
	}
}
