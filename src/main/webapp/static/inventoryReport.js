
function getInventoryReport(){
	var url = "/pos/api/inventoryreport";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryReport(data);  
	   },
	   error: handleAjaxError
	});
}
	

function displayInventoryReport(data){
	var $thead= $('#report-table').find('thead');
	$thead.empty();
	var $tbody = $('#report-table').find('tbody');
	var row='<tr>'
	+ '<th scope="col">Brand</th>'
	+ '<th scope="col">Category</th>'
	+ '<th scope="col">Quantity</th>'
	+ '</tr>'
	$thead.append(row);
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}


function init(){
	getInventoryReport();
}

$(document).ready(init);