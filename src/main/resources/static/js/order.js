document.addEventListener('DOMContentLoaded', function() {
	console.log("DOM content loading")

	var customerId = document.getElementById("customerId").value;
    console.log("customerId: " + customerId)

    var addOrderButton = document.getElementById('addOrderButton');
    addOrderButton.addEventListener('click',  function() {
        console.log("clicked add order button")
        window.location.href = '/order/showFormForAdd?customerId=' + customerId;
    });

    var backToCustomerListButton = document.getElementById('backToCustomerListButton');
    backToCustomerListButton.addEventListener('click',  function() {
        console.log("clicked backToCustomerListButton")
        window.location.href = '/customer/list';
    });



});
