package com.increff.employee.controller;

import com.increff.employee.dto.InventoryReportDto;
import com.increff.employee.model.InventoryReportData;
import com.increff.employee.model.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class InventoryReportController {

	@Autowired
	private InventoryReportDto dto;

	@ApiOperation(value = "Inventory Report")
	@RequestMapping(path = "/api/inventoryreport", method = RequestMethod.GET)
	public List<InventoryReportData> getAll() throws ApiException {
		return dto.getAll();
	}
}
