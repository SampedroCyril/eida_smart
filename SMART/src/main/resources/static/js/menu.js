
function openNav() {
    document.getElementById("sideNavigation").style.width = "250px";
    document.getElementById("main").style.marginLeft = "250px";
}
 
function closeNav() {
    document.getElementById("sideNavigation").style.width = "0";
    document.getElementById("main").style.marginLeft = "0";
}

var check = function() {
    if (document.getElementById('password').value ==
      document.getElementById('confirm_password').value) {
      document.getElementById('message').style.color = 'green';
      document.getElementById('message').innerHTML = ' Mot de passe valide';
    } else {
      document.getElementById('message').style.color = 'red';
      document.getElementById('message').innerHTML = ' Mot de passe invalide';
    }
  }

var check2 = function() {
    if (document.getElementById('password2').value ==
        document.getElementById('confirm_password2').value) {
        document.getElementById('message2').style.color = 'green';
        document.getElementById('message2').innerHTML = ' Mot de passe valide';
    } else {
        document.getElementById('message2').style.color = 'red';
        document.getElementById('message2').innerHTML = ' Mot de passe invalide';
    }
}

var check3 = function() {
    if (document.getElementById('password3').value ==
        document.getElementById('confirm_password3').value) {
        document.getElementById('message3').style.color = 'green';
        document.getElementById('message3').innerHTML = ' Mot de passe valide';
    } else {
        document.getElementById('message3').style.color = 'red';
        document.getElementById('message3').innerHTML = ' Mot de passe invalide';
    }
}

var check4 = function() {
    if (document.getElementById('password4').value ==
        document.getElementById('confirm_password4').value) {
        document.getElementById('message4').style.color = 'green';
        document.getElementById('message4').innerHTML = ' Mot de passe valide';
    } else {
        document.getElementById('message4').style.color = 'red';
        document.getElementById('message4').innerHTML = ' Mot de passe invalide';
    }
}

var check5 = function() {
    if (document.getElementById('password5').value ==
        document.getElementById('confirm_password5').value) {
        document.getElementById('message5').style.color = 'green';
        document.getElementById('message5').innerHTML = ' Mot de passe valide';
    } else {
        document.getElementById('message5').style.color = 'red';
        document.getElementById('message5').innerHTML = ' Mot de passe invalide';
    }
}

var check6 = function() {
    if (document.getElementById('password6').value ==
        document.getElementById('confirm_password6').value) {
        document.getElementById('message6').style.color = 'green';
        document.getElementById('message6').innerHTML = ' Mot de passe valide';
    } else {
        document.getElementById('message6').style.color = 'red';
        document.getElementById('message6').innerHTML = ' Mot de passe invalide';
    }
}

$(".sidebar-dropdown > a").click(function() {
    $(".sidebar-submenu").slideUp(200);
    if (
      $(this)
        .parent()
        .hasClass("active")
    ) {
      $(".sidebar-dropdown").removeClass("active");
      $(this)
        .parent()
        .removeClass("active");
    } else {
      $(".sidebar-dropdown").removeClass("active");
      $(this)
        .next(".sidebar-submenu")
        .slideDown(200);
      $(this)
        .parent()
        .addClass("active");
    }
  });
  
  $("#close-sidebar").click(function() {
    $(".page-wrapper").removeClass("toggled");
  });
  $("#show-sidebar").click(function() {
    $(".page-wrapper").addClass("toggled");
  });
  
  