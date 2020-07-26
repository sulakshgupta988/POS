var orderItemsData={};
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

//BUTTON ACTIONS
function addOrderItem(){
	var $form = $("#order-item-form");
	var barcode = $("#order-item-form input[name=barcode]").val();
	var quantity=$("#order-item-form input[name=quantity]").val();
	quantity=Number(quantity);
	if(!isInt(quantity)){
		alert("Quantity should be an integer");
		return false;
	}
	if(barcode==""){
		alert("Barcode cannot be empty");
		return false;
	}
	if( quantity <  1){
		alert("Quantity should be greater than 1");
		return false;
	}
	
	var json = toJson($form);
	//console.log(json);
	$.ajax({
	   url: getOrderUrl() + "/"+ barcode,
	   type: 'GET',   
	   success: function(response) {
	   		if(response.productQuantity<1)
	   			alert("Sorry item not available");
	   		else{
	   			if(!orderItemsData.hasOwnProperty(barcode))
	   			orderItemsData[barcode]=[response.productName,quantity,response.mrp,response.productQuantity];
	   		else{
	   			orderItemsData[barcode][1]=orderItemsData[barcode][1]+quantity;
	   		}
	   		if(response.productQuantity<orderItemsData[barcode][1]){
	   			alert("Cannot buy more than "+ response.productQuantity + response.productName);
	   			orderItemsData[barcode][1]=response.productQuantity;
	   		}
	   		displayOrderItemsList();
	   }},
	   error: handleAjaxError
	});
	$('#order-item-form')[0].reset();
	return false;

}

function isInt(value) {
  return !isNaN(value) && 
         parseInt(Number(value)) == value && 
         !isNaN(parseInt(value, 10));
}

function addOrder(event){
	//Set the values to update
	if (Object.keys(orderItemsData).length === 0){
		alert("At least 1 item should be there in an order");
		return false;
	}
	var $tbody = $('#order-items-table').find('tbody');
	$tbody.empty();
	var item={};
	var json=[];
	for(var barcode in orderItemsData){
		item = new Object;
		item['barcode']=barcode;
		item['quantity']=orderItemsData[barcode][1];
		json.push(item);
	}
	console.log(json);
	$.ajax({
	   url: getOrderUrl(),
	   type: 'POST',
	   data: JSON.stringify(json),
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   	orderItemsData= {};
	   	displayOrderItemsList();
	   },
	   error: function(response){
	   		displayOrderItemsList();
	   		handleAjaxError(response);
	   }
	   
	});
	displayOrderItemsList();
	return false;
}

function updateOrderItem(event){
	$('#edit-order-item-modal').modal('toggle');
	var quantity=$('#order-item-edit-form input[name=quantity]').val();
	var barcode=$('#order-item-edit-form input[name=barcode]').val();
	if(!isInt(quantity)){
		alert("Quantity should be an integer");
		return false;
	}
	if(orderItemsData[barcode][3]<quantity)
	{
		alert("Maximum quantity can be:"+ orderItemsData[barcode][3]);
		quantity=orderItemsData[barcode][3];
	}
	orderItemsData[barcode][1]=quantity;
	displayOrderItemsList();
}



//UI DISPLAY METHODS
function displayOrderItemsList(){
	var $tbody = $('#order-items-table').find('tbody');
	$tbody.empty();
	var id=1;
	var total=0;
	for(var i in orderItemsData){
		var buttonHtml = ' <button class="btn btn-info" onclick="displayOrderItem(\'' + i + '\')">Edit</button>'
		buttonHtml +=	' || <button class="btn btn-danger"onclick="deleteOrderItem(\''+ i + '\')">Delete</button>'
		var row = '<tr>'
		+ '<td>' + id++ + '</td>'
		+ '<td>' + orderItemsData[i][0] + '</td>'
		+ '<td>' + orderItemsData[i][1] + '</td>'
		+ '<td>' + orderItemsData[i][2] + '</td>'
		+ '<td>' + orderItemsData[i][1]*orderItemsData[i][2]+ '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		total+=orderItemsData[i][1]*orderItemsData[i][2];
        $tbody.append(row);
    }
    var row = '<tr>'
		+ '<td></td>'
		+ '<td></td>'
		+ '<td></td>'
		+ '<td>Total Amount:</td>'
		+ '<td>' + total + '</td>'
		+ '<td></td>'
		+ '</tr>';
		$tbody.append(row);
}

function deleteOrderItem(barcode){
	delete orderItemsData[barcode];
	displayOrderItemsList();
}

function displayOrderItem(barcode){
	$("#order-item-edit-form input[name=productName]").val(orderItemsData[barcode][0]);	
	$("#order-item-edit-form input[name=quantity]").val(orderItemsData[barcode][1]);	
	$("#order-item-edit-form input[name=barcode]").val(barcode);	
	$('#edit-order-item-modal').modal('toggle');
}


//INITIALIZATION CODE
function init(){
	$('#add-order-item').click(addOrderItem);
	$('#update-order-item').click(updateOrderItem);
	$('#confirm-order').click(addOrder);
	$('#refresh-data').click(displayOrderItemsList);
}

$(document).ready(init);
//$(document).ready(getBrandList);
