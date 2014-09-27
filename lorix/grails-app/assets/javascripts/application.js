// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better 
// to create separate JavaScript files as needed.
//
//= require jquery
//= require bootstrap
//= require sb-admin-2

if (typeof jQuery !== 'undefined') {
  (function($) {

    $(document).ready(function(){

      $('.popupRefdata').click(function() {
          alert('ho ho ho');
      });

    });

    $('#spinner').ajaxStart(function() {
      $(this).fadeIn();
    }).ajaxStop(function() {
      $(this).fadeOut();
    });

  })(jQuery);
}


