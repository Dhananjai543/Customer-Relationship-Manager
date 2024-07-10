document.addEventListener('DOMContentLoaded', function() {
	console.log("DOM content loading")
    var connectButton = document.getElementById('addCustomerButton');
    connectButton.addEventListener('click',  function() {
		console.log("clicked add customer button")
        window.location.href = '/customer/showFormForAdd';
    });
});
