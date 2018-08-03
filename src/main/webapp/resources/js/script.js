$(function() {
  $('#progress').hide();
  $('#success').hide();		 
  /*  Submit form using Ajax */
  $('button[type=submit]').click(function(e) {
   //Prevent default submission of form
   e.preventDefault();
   
   //Remove all errors
   $('input').next().remove();
   var downloadUrl = $('#downloadUrl').val();
   var json = { "downloadUrl" : downloadUrl };
   console.log(json);
   $.post({
    url: './download',
    data: JSON.stringify(json),
    beforeSend: function(xhr) {
    	$('#progress').show(500);
    	$('.submitBtn').prop("disabled", true);
        xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Content-Type", "application/json");
    },
    success: function(res) {
    	$('#progress').hide();
    	$('.submitBtn').prop("disabled", false);
     if (res.validated) {
      //Download file
      $('#downloadUrl').val("")
      $('#success').show(100, function() {
    	  $('#success').hide(20000);
	  });
     } else {
      //Set error messages
      console.log(res);
      $.each(res.errorMessages, function(key, value) {
       $('input[name=' + key + ']').after('<span class="alert-danger">' + value + '</span>');
      });
     }
    },
    error: function(xhr) {
    	$('.submitBtn').prop("disabled", false); 
	  $('#progress').hide();			  
    }
   })
  });
 });