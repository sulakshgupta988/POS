package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.dto.BrandDto;
import com.increff.employee.model.ApiException;
import com.increff.employee.model.BrandData;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class BrandReportController {

	@Autowired
	private BrandDto dto;
	
	@ApiOperation(value="Get Brand Report")
	@RequestMapping(path = "/api/brandreport", method = RequestMethod.GET)
	public List<BrandData> getAll() throws ApiException {
		return dto.getAll();
	}
}
