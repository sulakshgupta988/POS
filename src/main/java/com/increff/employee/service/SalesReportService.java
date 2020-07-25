package com.increff.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.employee.dao.SalesReportDao;
import com.increff.employee.model.ApiException;
import com.increff.employee.model.SalesReportForm;

@Service
public class SalesReportService {
	
	@Autowired
	private SalesReportDao dao;
	@Transactional(readOnly = true)
	public List<Object[]> getSalesReport(SalesReportForm form) throws ApiException{
		return dao.getSalesReport(form);
	}
	
}
