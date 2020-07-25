package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.dto.SalesReportDto;
import com.increff.employee.model.ApiException;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.model.SalesReportForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class SalesReportController {

	@Autowired
	private SalesReportDto dto;
	
	@ApiOperation(value = "Get Sales Report")
	@RequestMapping(path = "/api/salesreport", method = RequestMethod.POST)
	public List<SalesReportData> getSalesReport(@RequestBody SalesReportForm form ) throws ApiException {
		return dto.getSalesReport(form);
		
	}
}
