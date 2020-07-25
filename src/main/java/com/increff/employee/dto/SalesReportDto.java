package com.increff.employee.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.increff.employee.model.SalesReportData;
import com.increff.employee.model.SalesReportForm;
import com.increff.employee.service.SalesReportService;
import com.increff.employee.model.ApiException;

@Service
public class SalesReportDto {

	@Autowired
	private SalesReportService service;

	@Transactional(readOnly = true)
	public List<SalesReportData> getSalesReport(SalesReportForm form) throws ApiException {
		if (form.getEndDate() == null) {
			form.setEndDate(new Date());
		}
		if (form.getEndDate().compareTo(form.getStartDate()) < 0)
			throw new ApiException("Start cannot be greater than end date or today's date");
		form.setEndDate(addToDate(form.getEndDate(), 1));
		List<SalesReportData> reportDataList = new ArrayList<SalesReportData>();
		List<Object[]> reportList = service.getSalesReport(form);
		if (reportList == null) {
			throw new ApiException("NU;ll received.");
		}

		for (Object[] reportItem : reportList) {
			reportDataList.add(convert(reportItem));
		}

		return reportDataList;
	}

	private SalesReportData convert(Object[] reportItem) throws ApiException {
		SalesReportData data = new SalesReportData();
		Number revenue = (Number) reportItem[2];
		Number quantity = (Number) reportItem[1];
		data.setRevenue(revenue.doubleValue());
		data.setQuantity(quantity.intValue());
		data.setCategory(reportItem[0].toString());

		return data;
	}

	private static Date addToDate(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}
}
