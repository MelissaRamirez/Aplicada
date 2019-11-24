function changePassword() {
	if (document.getElementById("chec").checked)
		document.getElementById('password').readOnly = false;

	else
		document.getElementById('password').readOnly = true;
}

function quantityValidation(){
	let quantity = document.getElementById("quantity");
	let stock = document.getElementById("unitsInput");
	let button = document.getElementById("btnBuy");
	let lblError = document.getElementById("lblError");
	if(quantity > stock){
		button.disabled = true;
		lblError.innerHTML = "";

	} else{
		button.disabled = false;
		lblError.innerHTML = "No puede comprar más productos de lo que hay en bodegas";
	}
}