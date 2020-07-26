function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

//BUTTON ACTIONS
function addProduct(event){
	//Set the values to update
	if(brandTemp==""){
		alert("Brand cannot be empty.");
		return false;
	}
	if(categoryTemp==""){
		alert("Category field is also required.");
		return false;
	}
	$('#product-form input[name=brandId]').val(Number(brandData[brandTemp][categoryTemp]));
	if($('#product-form input[name=mrp]').val()<=0){
		alert("MRP cannot be negative");
		return false;
	}
	if($('#product-form input[name=productName]').val()==""){
		alert("ProductName cannot be empty");
		return false;
	}
	var mrp = $('#product-form input[name=mrp]').val();
	if(isNaN(parseFloat(mrp)) ){
		alert("MRP should be a decimal value");
		return false;
	}
	
	if($('#product-form input[name=barcode]').val()==""){
		alert("Barcode cannot be empty");
		return false;
	}
	var $form = $("#product-form");
	var json = toJson($form);
		console.log(json);
	var url = getProductUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getProductList();  
	   },
	   error: handleAjaxError
	});
	$('#product-form')[0].reset();
	return false;
}

function updateProduct(event){
	$('#edit-product-modal').modal('toggle');
	//Get the ID
	var id = $("#product-edit-form input[name=id]").val();
	if(brandEditTemp==null || categoryEditTemp==null){
		alert("select brand and category both");
		return false;
	}
	var brandId = $("#product-edit-form input[name=brandId]").val(Number(brandData[brandEditTemp][categoryEditTemp]));
	if($('#product-edit-form input[name=mrp]').val()<=0){
		alert("Enter valid mrp(greater than zero)")
		return false;
	}
	if($('#product-edit-form input[name=productName]').val()=="" || $('#product-edit-form input[name=barcode]').val()==""){
		alert("ProductName and Barcode cannot be empty");
		return false;
	}
	var mrp = $('#product-edit-form input[name=mrp]').val();
	if(isNaN(parseFloat(mrp))){
		alert("MRP should be a decimal value");
		return false;
	}
	var url = getProductUrl() + "/" + id;

	//Set the values to update
	var $form = $("#product-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getProductList();   
	   },
	   error: handleAjaxError
	});
}

function getProductList(){

fillBrandDrop($('#selectBrand'));
	var url = getProductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);  
	   },
	   error: handleAjaxError
	});
}

function deleteProduct(barcode){
	var url = getProductUrl() + "/" + barcode;
	console.log(barcode);
	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getProductList();  
	   },
	   error: handleAjaxError
	});
}
//UI DISPLAY METHODS
function displayProductList(data){
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = ' <button class="btn btn-info" onclick="displayEditProduct(\'' + e.barcode + '\' ,\'' + e.brandName + '\',\'' + e.category + '\')">Edit</button> || <button class= "btn btn-dark" onclick="deleteProduct(\'' + e.barcode + '\')">Delete</button> '
	//	console.log(e.barcode);
		var row = '<tr>'
		+ '<td>' + e.brandName + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>'  + e.name + '</td>'
		+ '<td>'  + e.barcode + '</td>'
		+ '<td>'  + e.mrp + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditProduct(barcode,brandName,category){
	fillBrandDrop($('#selectEditBrand'));
	$('#selectEditBrand').val(brandName);
	brandEditTemp=brandName;
	fillCategoryDrop($('#selectEditCategory'),brandEditTemp);
	$('#selectEditCategory').val(category);
	categoryEditTemp=category;
	var url = getProductUrl() + "/" + barcode;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProduct(data);   
	   },
	   error: handleAjaxError
	});	
}

function displayUploadData(){
 	resetUploadDialog(); 	
	$('#upload-product-modal').modal('toggle');
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateFileName(){
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(fileName);
}

function processData(){
	var file = $('#productFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	//If everything processed then return
	if(processCount==fileData.length){
		return;
	}
	var row = fileData[processCount];
	//console.log(brandData);
	processCount++;
	var json = JSON.stringify(row);
	var url = getProductUrl();
	//console.log(json);
	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   	getProductList();
	   		uploadRows();  
	   },
	   error: function(response){
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});
}

function downloadErrors(){
	writeFileData(errorData);
}
function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}


function displayProduct(data){
	console.log(data);
	$("#product-edit-form input[name=name]").val(data.name);	
	$("#product-edit-form input[name=barcode]").val(data.barcode);	
	$("#product-edit-form input[name=mrp]").val(data.mrp);	
	$("#product-edit-form input[name=id]").val(data.id);	
	$('#edit-product-modal').modal('toggle');
}

//To store Brand table in memory
var brandData={};

function getBrandList(){
	var url = "/pos/api/brand";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   	//console.log(data);
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
	}
	//console.log(brandData);
	fillBrandDrop($('#selectBrand'));
}


function fillBrandDrop(selectbody){
	var $selectbody=selectbody;
	//console.log(brandData);
		$selectbody.empty();
		$selectbody.append("<option disabled selected> Select Brand</option>");
	for(var i in brandData){
		var row="<option>"+i+"</option>";
		$selectbody.append(row);
	}
}

function fillCategoryDrop(selectbody,brandTemp){
	var $selectbody=selectbody;
	$selectbody.empty();
	$selectbody.append("<option disabled selected>Select Category</option>");
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

var brandEditTemp;
$('#selectEditBrand').on('change',function(){
	brandEditTemp = $("#selectEditBrand option:selected").text();
	fillCategoryDrop($('#selectEditCategory'),brandEditTemp);
    });

var categoryTemp="";
$('#selectCategory').on('change',function(){
	categoryTemp = $("#selectCategory option:selected").text();
	//console.log(brandData[brandTemp]);
	//console.log(brandData[brandTemp][categoryTemp]);
    });

var categoryEditTemp;
$('#selectEditCategory').on('change',function(){
	categoryEditTemp = $("#selectEditCategory option:selected").text();
	//console.log(brandData[brandTemp]);
	//console.log(brandData[brandTemp][categoryTemp]);
    });
//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#refresh-data').click(getProductList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
   $('#productFile').on('change', updateFileName)
}

$(document).ready(init);
$(document).ready(getBrandList);
$(document).ready(getProductList);

