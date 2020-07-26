package com.increff.employee.dto;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.increff.employee.model.OrderItemsForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemsPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.model.ApiException;
import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderItemsData;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.OrderItemsService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;

@Service
public class OrderDto {
	@Autowired
	private ProductService productService;

	@Autowired
	private OrderItemsService orderItemsService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private InventoryService inventoryService;

	@Transactional(rollbackOn = ApiException.class)
	public void add(List<OrderItemsForm> orderItemsForm)
			throws ApiException, ParserConfigurationException, TransformerException {
		OrderPojo order = new OrderPojo();
		order = orderService.add(order);
		for (OrderItemsForm form : orderItemsForm) {
			orderItemsService.add(convert(form, order));
		}
		make(orderItemsForm, order.getId());
	}
	
	
	public List<OrderData> getAll(){
		List<OrderPojo> orderList = orderService.getAll();
		List<OrderData> orderDataList = new ArrayList<OrderData>();
		for(OrderPojo pojo : orderList) {
			OrderData data = new OrderData();
			data.setDate(pojo.getDate());
			data.setId(pojo.getId());
			orderDataList.add(data);
		}
		return orderDataList;
	}

	@Transactional
	public void createInvoice(int id) throws ApiException, ParserConfigurationException, TransformerException {
		List<OrderItemsPojo> orderItems = orderItemsService.getOrder(id);
		List<OrderItemsForm> orderItemsForm = new ArrayList<OrderItemsForm>();
		for (OrderItemsPojo pojo : orderItems) {
			OrderItemsForm form = new OrderItemsForm();
			form.setQuantity(pojo.getQuantity());
			ProductPojo productPojo = productService.select(pojo.getProductId());
			form.setBarcode(productPojo.getBarcode());
			orderItemsForm.add(form);
		}
		make(orderItemsForm, id);
	}

	@Transactional
	public OrderItemsData getProduct(String barcode) throws ApiException {
		ProductPojo product = productService.select(barcode);
		return convert(product, barcode);

	}

	private OrderItemsData convert(ProductPojo product, String barcode) throws ApiException {
		OrderItemsData orderItemsData = new OrderItemsData();
		InventoryPojo inventoryPojo = inventoryService.get(product.getId());
		orderItemsData.setBarcode(barcode);
		orderItemsData.setMrp(product.getMrp());
		orderItemsData.setProductName(product.getName());
		orderItemsData.setProductQuantity(inventoryPojo.getQuantity());
		return orderItemsData;

	}

	private OrderItemsPojo convert(OrderItemsForm orderItemsForm, OrderPojo orderPojo) throws ApiException {
		OrderItemsPojo orderItemsPojo = new OrderItemsPojo();
		ProductPojo productPojo = productService.select(orderItemsForm.getBarcode());
		InventoryPojo inventoryPojo = new InventoryPojo();
		inventoryPojo.setQuantity(0 - orderItemsForm.getQuantity());
		inventoryService.updatePlus(productPojo.getId(), inventoryPojo);

		orderItemsPojo.setOrderId(orderPojo.getId());
		orderItemsPojo.setProductId(productPojo.getId());
		orderItemsPojo.setQuantity(orderItemsForm.getQuantity());
		orderItemsPojo.setSellingPrice(productPojo.getMrp());

		return orderItemsPojo;
	}

	public final String xmlFilePath = "./src/main/resources/com/increff/employee/invoice.xml";

	public void make(List<OrderItemsForm> formList, int id)
			throws ParserConfigurationException, ApiException, TransformerException {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

		Document document = documentBuilder.newDocument();

		// root element
		Element root = document.createElement("InvoiceData");
		document.appendChild(root);
		Double sum = 0.0;
		int sno = 1;
		for (OrderItemsForm form : formList) {
			ProductPojo p = productService.select(form.getBarcode());
			Element product = document.createElement("invoice");
			root.appendChild(product);
			Element count = document.createElement("sno");
			count.appendChild(document.createTextNode(Integer.toString(sno)));
			product.appendChild(count);
			Element product_name = document.createElement("name");
			product_name.appendChild(document.createTextNode(p.getName()));
			product.appendChild(product_name);
			Element barcode = document.createElement("barcode");
			barcode.appendChild(document.createTextNode(p.getBarcode()));
			product.appendChild(barcode);
			Element qty = document.createElement("qty");
			qty.appendChild(document.createTextNode(Integer.toString(form.getQuantity())));
			product.appendChild(qty);
			Element mrp = document.createElement("mrp");
			mrp.appendChild(document.createTextNode(Double.toString(p.getMrp())));
			product.appendChild(mrp);
			Element totalPrice = document.createElement("totalPrice");
			totalPrice.appendChild(document.createTextNode(Double.toString(form.getQuantity() * p.getMrp())));
			product.appendChild(totalPrice);
			sum += (p.getMrp() * form.getQuantity());
			sno += 1;
		}
		Element totalPrice = document.createElement("totalAmount");
		totalPrice.appendChild(document.createTextNode(Double.toString(sum)));
		root.appendChild(totalPrice);

		Element orderId = document.createElement("ID");
		orderId.appendChild(document.createTextNode(Integer.toString(id)));
		root.appendChild(orderId);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(new File(xmlFilePath));

		// If you use
		// StreamResult result = new StreamResult(System.out);
		// the output will be pushed to the standard output ...
		// You can use that for debugging

		transformer.transform(domSource, streamResult);

	}

}
