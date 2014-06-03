
    $('#star1').raty({
    cancel : false,
    target : '#usability',
    targetKeep: true,
    targetType : 'score'

    });
    $('#star2').raty({
    cancel : false,
    target : '#price',
    targetKeep: true,
    targetType : 'score'

    });
    $('#star3').raty({
    cancel : false,
    target : '#contract',
    targetKeep: true,
    targetType : 'score'

    });
    $('#star4').raty({
    cancel : false,
    target : '#customer_service',
    targetKeep: true,
    targetType : 'score'

    });


    $(document).on("click", ".open-signUpDialog", function () {
    var usability = document.getElementById('usability').value;
    var price = document.getElementById('price').value;
    var contract = document.getElementById('contract').value;
    var customerService = document.getElementById('customer_service').value;
    var text = document.getElementById('text').value;
    var productId = document.getElementById('productId').value;
    var productName = document.getElementById('productName').value;
    $(".modal-body #modalUsability").val( usability );
    $(".modal-body #modalPrice").val( price );
    $(".modal-body #modalContract").val( contract );
    $(".modal-body #modalCustomerService").val( customerService );
    $(".modal-body #modalText").val( text );
    $(".modal-body #modalProductId").val( productId );
    $(".modal-body #modalProductName").val( productName );
    if(usability == 0 || price == 0 || contract ==0 || customerService == 0 || text== ''){
        $('#signUpDialog').modal('hide');
        $('#errorDialog').modal('show');
    }else{
        $('#signUpDialog').modal('show');
    }

    });



    $(document).on("click", ".open-loginDialog", function () {
    var usability = document.getElementById('usability').value;
    var price = document.getElementById('price').value;
    var contract = document.getElementById('contract').value;
    var customerService = document.getElementById('customer_service').value;
    var text = document.getElementById('text').value;
    var productId = document.getElementById('productId').value;
    var productName = document.getElementById('productName').value;
    $(".modal-body #loginUsability").val( usability );
    $(".modal-body #loginPrice").val( price );
    $(".modal-body #loginContract").val( contract );
    $(".modal-body #loginCustomerService").val( customerService );
    $(".modal-body #loginText").val( text );
    $(".modal-body #loginProductId").val( productId );
    $(".modal-body #loginProductName").val( productName );
    if(usability == 0 || price == 0 || contract ==0 || customerService == 0 || text == ''){
    $('#errorDialog').modal('show');
    $('#loginDialog').modal('hide');
    }else{
    $('#loginDialog').modal('show');
    }

    });



    $(document).ready(function () {

    $("#signUpDialogForm").validate({
    onkeyup: false,
    rules: {
    name: {
    required: true
    }, signUpEmail:{
    required: true,
    email:true
    },
    password: "required",
    confirmPassword: {
    equalTo: "#password"
    },phone:{
    required: true,
    digits: true,
    minlength:10,
    maxlength: 10
    }
    },
    messages: {
    name: {
    required: "Name is required.",
    },
    signUpEmail:{
    required: "Email Required",
    email: "Email is not valid"
    },
    password: {
    required: "Password is required",
    password: "Invalid Password"
    },
    confirmPassword: {
    required: "Password needs confirmation",
    confirmPassword: "Invalid Password"
    },
    phone:{
    digits: "Invalid Phone Number",
    minlength: "Invalid Phone Number",
    maxlength: "Invalid Phone Number"
    }

    }
    });

    });


    $(document).ready(function () {

    $("#loginDialogForm").validate({
    onkeyup: false,
    rules: {
    loginEmail:{
    required: true,
    email:true
    },
    password:{
    required: true,
    }
    },
    messages: {
    loginEmail:{
    required: "Email Required",
    email: "Email is not valid"
    },
    password: {
    required: "Password is required",
    }

    }
    });

    });
