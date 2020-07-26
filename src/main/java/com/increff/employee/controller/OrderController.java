package com.increff.employee.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.dto.OrderDto;
import com.increff.employee.model.ApiException;
import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderItemsData;
import com.increff.employee.model.OrderItemsForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderController {

	private static final String PATH_XSL = "./templateInvoice.xsl";
	@Autowired
	private OrderDto dto;

	@ApiOperation(value = "Get all orders")
	@RequestMapping(path = "/api/order", method = RequestMethod.GET)
	public List<OrderData> getAll() {
		return dto.getAll();
	}

	@ApiOperation(value = "Get a product by barcode")
	@RequestMapping(path = "/api/order/{barcode}", method = RequestMethod.GET)
	public OrderItemsData getProduct(@PathVariable String barcode) throws ApiException {
		return dto.getProduct(barcode);
	}

	@ApiOperation(value = "Creates an order")
	@RequestMapping(path = "/api/order", method = RequestMethod.POST)
	public void addOrder(@RequestBody List<OrderItemsForm> orderItemsForm)
			throws ApiException, ParserConfigurationException, TransformerException {
		dto.add(orderItemsForm);
	}

	@ApiOperation(value = "Generate Invoice")
	@RequestMapping(path = "/api/order/invoice/{id}", method = RequestMethod.GET)
	public void get(@PathVariable String id, HttpServletResponse response)
			throws ApiException, ParserConfigurationException, TransformerException {
		int idInt = Integer.parseInt(id);
		dto.createInvoice(idInt);
		try {

			FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(PATH_XSL));
			// Make sure the XSL transformation's result is piped through to FOP
			Result res = new SAXResult(fop.getDefaultHandler());
			// Setup input
			Source src = new StreamSource(new File("./src/main/resources/com/increff/employee/invoice.xml"));

			transformer.transform(src, res);
			response.setContentType("application/pdf");
			response.setContentLength(out.size());

			response.getOutputStream().write(out.toByteArray());
			response.getOutputStream().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
