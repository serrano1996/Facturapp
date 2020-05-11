$(document).ready(function () {

	// Show sidebar.
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');   
    });
    
    // Enterprise.
    $("button[name='info']").click(function() {
 		let id = $(this).val();
 		//console.log(id);
 		$.ajax({
 			type: 'GET',
 			url: '/admin/load_user/' + id,
 			success: function(res) {
 				console.log(res);
 				$("#name").val(res.name + " " + res.lastname);
 				$("#email").val(res.email);
 				$("#username").val(res.username);
 				$("#date").val(res.createAt);
 			}
 		});
 	})
    
 	// Costumer.
    $("button[name='editCostumer']").click(function() {
 		let id = $(this).val();
 		//console.log(id);
 		$.ajax({
 			type: 'GET',
 			url: '/enterprise/load_client/' + id,
 			success: function(res) {
 				//console.log(res);
 				$("#client").val(res.id);
 				$("#nif").val(res.nif);
 				$("#name").val(res.name);
 				$("#email").val(res.email);
 				$("#address").val(res.address);
 				$("#phone").val(res.phone);
 			}
 		});
 	})
 	
 	$("button[name='deleteCostumer']").click(function() {
 		let id = $(this).val();
 		$("#remCId").val(id);
 	})
 	
 	$("button[name='deleteInvoice']").click(function() {
 		let id = $(this).val();
 		$("#remIId").val(id);
 	})
 	
 	$("button[name='deleteEnterprise']").click(function() {
 		let id = $(this).val();
 		$("#remEId").val(id);
 	})
 	
 	$("button[name='deleteCancel']").click(function() {
 		$('#confirmDelete').modal('hide')
 	})
 	
 	$("button[name='cancel']").click(function() {
 		$('#changePassword').modal('hide')
 	})
 	
});