window.onload = getLocation;

    function getLocation()
    {
    if (navigator.geolocation)
    {
    navigator.geolocation.getCurrentPosition(showPosition, showError);
    }
    }

    function showPosition(position)
    {
    var latlon=position.coords.latitude+","+position.coords.longitude;
    var url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+latlon+"&sensor=false";
    var posting = $.post( url);

    posting.done(function( myJSONResult ) {
    if(myJSONResult){
    var address = myJSONResult.results[0].address_components;
    var zipcode = address[address.length - 1].long_name;
    $('#zip').val(zipcode);
    }
    });
    }

    function showError(error)
    {
    switch(error.code)
    {
    case error.PERMISSION_DENIED:
    x.innerHTML="User denied the request for Geolocation."
    break;
    case error.POSITION_UNAVAILABLE:
    x.innerHTML="Location information is unavailable."
    break;
    case error.TIMEOUT:
    x.innerHTML="The request to get user location timed out."
    break;
    case error.UNKNOWN_ERROR:
    x.innerHTML="An unknown error occurred."
    break;
    }
    }