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
	   			if(!orderItemsData.hasOwnProperty(barcode)){
	   				orderItemsData[barcode]=[response.productName,quantity,response.mrp,response.productQuantity];
	   				if(response.productQuantity < quantity){
	   				alert("Only "+ response.productQuantity + " " + response.productName + " left.");
	   				orderItemsData[barcode][1]=response.productQuantity;
	   				}
	   			}
	   			else if(response.productQuantity < orderItemsData[barcode][1] + quantity){
	   				var moreCanBeBought  = response.productQuantity - orderItemsData[barcode][1];
	   				alert("Cannot add more than "+ moreCanBeBought + " " + response.productName);
	   				orderItemsData[barcode][1]=response.productQuantity;
	   				}
	   			else{
	   				orderItemsData[barcode][1]=orderItemsData[barcode][1]+quantity;
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

function addOrder(){
	//Set the values to update
	if (Object.keys(orderItemsData).length === 0){
		$.toaster('Error!! ', {
       		 text: 'At least one item is required to place an order.'
		})
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
	   	getOrders();
	   },
	   error: function(response){
	   		displayOrderItemsList();
	   		getOrders();
	   		handleAjaxError(response);
	   }
	   
	});
	displayOrderItemsList();
	$('#add-order-item-modal').modal('toggle');
	return false;
}

function getOrders(){
	var url = getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrders(data);  
	   },
	   error: handleAjaxError
	});
}



function updateOrderItem(event){
	var quantity=$('#order-item-edit-form input[name=quantity]').val();
	var barcode=$('#order-item-edit-form input[name=barcode]').val();
	if(!isInt(quantity)){
		alert("Quantity should be an integer");
		return false;
	}
	if(quantity < 1){
		$.toaster('Error!!' , {
			text: 'Quantity should be greater than zero'
		})
		return false;
	}
	if(orderItemsData[barcode][3] < quantity)
	{
		alert("Maximum quantity can be:"+ orderItemsData[barcode][3]);
		quantity=orderItemsData[barcode][3];
	}
	orderItemsData[barcode][1]=quantity;
	displayOrderItemsList();
	closeForm();
}

function genInv(id){
	var url = "/pos/api/order/invoice/" + id;
	console.log(fss);
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
 	
	   },
	   error: handleAjaxError
	});	

}

//UI DISPLAY METHODS

function displayOrders(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<a href="/pos/api/order/invoice/' + e.id + '" target="_blank" class="btn btn-primary float-right">Generate Invoice</a>';
		
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td class="text-center">' + new Date(e.date).toLocaleString() + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
    }
}


function displayOrderItemsList(){
	var $tbody = $('#order-items-table').find('tbody');
	$tbody.empty();
	var id=1;
	var total=0;
	for(var i in orderItemsData){
		var buttonHtml = ' <button class="btn btn-info" onclick="displayOrderItem(\'' + i + '\')">Edit</button>'
		buttonHtml +=	' || <button class="btn btn-dark"onclick="deleteOrderItem(\''+ i + '\')">Delete</button>'
		var row = '<tr>'
		+ '<td>' + id++ + '</td>'
		+ '<td>' + orderItemsData[i][0] + '</td>'
		+ '<td>' + orderItemsData[i][1] + '</td>'
		+ '<td>' + orderItemsData[i][2] + '</td>'
		+ '<td>' + (orderItemsData[i][1]*orderItemsData[i][2]).toFixed(2)+ '</td>'
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
		+ '<td>' + total.toFixed(2) + '</td>'
		+ '<td></td>'
		+ '</tr>';
		$tbody.append(row);
}

function closeForm(){
	$(document).find('#edit-div').hide();
	$(document).find('#buttons').hide();
}

function deleteOrderItem(barcode){
	delete orderItemsData[barcode];
	displayOrderItemsList();
}

function displayOrderItem(barcode){
	$("#order-item-edit-form input[name=productName]").val(orderItemsData[barcode][0]);	
	$("#order-item-edit-form input[name=quantity]").val(orderItemsData[barcode][1]);	
	$("#order-item-edit-form input[name=barcode]").val(barcode);	
	$(document).find('#edit-div').show();
	$(document).find('#buttons').show();
}

function displayOrderModal(){
	$('#add-order-item-modal').modal('toggle');
}

//INITIALIZATION CODE
function init(){
	$('#add-order-item').click(addOrderItem);
	$('#update-order-item').click(updateOrderItem);
	$('#confirm-order').click(addOrder);
	$('#refresh-data').click(displayOrderItemsList);
	$('#add-order').click(displayOrderModal);
	$('#cancel-order-item').click(closeForm);
	$(document).find('#edit-div').hide();
	$(document).find('#buttons').hide();
	getOrders();
}

$(document).ready(init);
//$(document).ready(getBrandList);
