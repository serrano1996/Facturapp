$(document).ready(function() {
	let enterpriseid = $("#enterpriseid").val();
	$("#search_product").autocomplete({
		source : function(request, response) {
			$.ajax({
				url : "/enterprise/load_products/" + request.term + "/" + enterpriseid,
				dataType : "json",
				data : {
					term : request.term
				},
				success : function(data) {
					response($.map(data, function(item) {
						//item.enterprise = null;
						return {
							value : item.id,
							label : item.longName,
							price : item.price,
						};
					}));
				},
			});
		},
		select : function(event, ui) {
			if(itemsHelper.hasProduct(ui.item.value)){
				itemsHelper.incrementQuantity(ui.item.value, ui.item.price);
				return false;
			}					
			let line = $("#items_template").html();
			line = line.replace(/{ID}/g, ui.item.value);
			line = line.replace(/{NAME}/g, ui.item.label);
			line = line.replace(/{PRICE}/g, ui.item.price);
			$("#load_items tbody").append(line);
			itemsHelper.calculateAmount(ui.item.value, ui.item.price, 1);
			return false;
		}
	});

	$("form").submit(function(){
		$("#items_template").remove();
		return;
	});

});
		
var itemsHelper = {
	calculateAmount: function(id, price, quantity){
		$("#total_amount_" + id).html(parseFloat(price) * parseInt(quantity));
		this.calculateTotal();
	},
	hasProduct: function(id){					
		var result = false;			
		$('input[name="item_id[]"]').each(function(){
			if(parseInt(id) == parseInt($(this).val()) ){
				result = true;
			}
		});			
		return result;
	},
	incrementQuantity: function(id, price){
		var quantity = $("#quantity_" + id).val() ? parseInt($("#quantity_" + id).val()) : 0;
		$("#quantity_" + id).val(++quantity);
		this.calculateAmount(id, price, quantity);
	},
	deleteLine: function(id){
		$("#row_" + id).remove();
		this.calculateTotal();
	},
	calculateTotal: function(){
		var total = 0;	
		var iva = 0;
		$('span[id^="total_amount_"]').each(function(){
			total += parseFloat($(this).html());
		});		
		iva = total * 0.21;
		total += iva;
		$('#iva').html(Number(iva.toFixed(2)) + " €");
		$('#total').html(Number(total.toFixed(2)) + " €");
	}
}
