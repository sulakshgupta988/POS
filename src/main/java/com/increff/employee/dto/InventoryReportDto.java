package com.increff.employee.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.employee.model.InventoryReportData;

import com.increff.employee.service.InventoryReportService;

@Service
public class InventoryReportDto {

	@Autowired
	private InventoryReportService service;

	@Transactional(readOnly = true)
	public List<InventoryReportData> getAll() {
		List<InventoryReportData> data = new ArrayList<InventoryReportData>();
		List<Object[]> list = service.getAll();
		for (Object[] ob : list) {
			data.add(convert(ob));
		}
		return data;
	}

	private InventoryReportData convert(Object[] ob) {
		InventoryReportData data = new InventoryReportData();
		data.setBrand(ob[0].toString());
		data.setCategory(ob[1].toString());
		Number quantity = (Number) ob[2];
		data.setQuantity(quantity.intValue());
		return data;
	}

}