
function getBrandReport(){
	var url = "/pos/api/brand";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandReport(data);  
	   },
	   error: handleAjaxError
	});
}

function displayBrandReport(data){
	var $thead= $('#report-table').find('thead');
	$thead.empty();
	var $tbody = $('#report-table').find('tbody');
	var row='<tr>'
	+ '<th scope="col">Brand</th>'
	+ '<th scope="col">Category</th>'
	+ '</tr>'
	$thead.append(row);
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}
function init(){
	getBrandReport();
}

$(document).ready(init);