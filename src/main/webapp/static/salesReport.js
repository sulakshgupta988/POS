var brandData={}
var categoryData={}
function getReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/salesreport";
}

function getReport(event){
	$('#report-form input[name=brand]').val(brandTemp);
	$('#report-form input[name=category]').val(categoryTemp);
	brandTemp="";
	categoryTemp="";
	if($('#report-form input[name=startDate]').val()==""){
		alert("Start date cannot be empty");
		return false
	}

	var $form = $("#report-form");
	var json = toJson($form);
	console.log(json);
	var url = getReportUrl();
	//console.log(json.productName+ ' ' + json.category);
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   			displaySalesReport(response)
	   },
	   error: handleAjaxError
	});
$('#report-form')[0].reset();
fillBrandDrop($('#selectBrand'));
fillCategoryDropInit($('#selectCategory'));
	return false;
}

function displaySalesReport(data){
	var $thead= $('#report-table').find('thead');
	$thead.empty();
	var $tbody = $('#report-table').find('tbody');
	var row='<tr>'
	+ '<th scope="col">Category</th>'
	+ '<th scope="col">Quantity</th>'
	+ '<th scope="col">Revenue</th>'
	+ '</tr>'
	$thead.append(row);
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + e.category + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>'  + e.revenue + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
	

}

function getBrandList(){

	var url = "/pos/api/brand";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		createBrandData(data);
	   },
	   error: handleAjaxError
	});
}

function createBrandData(data)
{
	var brandTemp="";
	for(var i in data){
		var e=data[i];
		brandTemp=e.brand;
		//console.log(brandTemp);
		if(!brandData.hasOwnProperty(brandTemp))
			brandData[brandTemp]={};
		brandData[brandTemp][e.category]=e.id;
		if(!categoryData.hasOwnProperty(e.category))
			categoryData[e.category]=1;
	}
	//console.log(brandData);
	fillBrandDrop($('#selectBrand'));
	fillCategoryDropInit($('#selectCategory'))
}

function fillCategoryDropInit(selectbody){
	var $selectbody=selectbody;
	$selectbody.empty();
	$selectbody.append('<option value="" disabled selected>Select Category</option>');
	for(var i in categoryData)
	{
		var row="<option>"+i+"</option>";
		$selectbody.append(row);
	}
}

function fillBrandDrop(selectbody){
	var $selectbody=selectbody;
	$selectbody.empty();
	//console.log(brandData);
	$selectbody.append('<option value="" disabled selected>Select Brand</option>');
	for(var i in brandData){
		var row="<option>"+i+"</option>";
		$selectbody.append(row);
	}
}

function fillCategoryDrop(selectbody,brandTemp){
	var $selectbody=selectbody;
	$selectbody.empty();
	$selectbody.append('<option value="" disabled selected>Select Category</option>');
	for(var i in brandData[brandTemp])
	{
		var row="<option>"+i+"</option>";
		$selectbody.append(row);
	}
	}

var brandTemp="";
$('#selectBrand').on('change',function(){
	brandTemp = $("#selectBrand option:selected").text();
	fillCategoryDrop($('#selectCategory'),brandTemp);
    });

var categoryTemp="";
$('#selectCategory').on('change',function(){
	categoryTemp = $("#selectCategory option:selected").text();
    });

function init(){
$('#generate-report').click(getReport);
getBrandList();

}

$(document).ready(init);


