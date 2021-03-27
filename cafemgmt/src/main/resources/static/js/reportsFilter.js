$(function() {
     $('input[name="dateRange"]').daterangepicker({
        opens: 'left'
      }, function(start, end, label) {
        console.log("A new date selection was made: " + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD'));
     });

    $('.csel').change(function() {
        $('.hiddencid').val($('.csel').val());

        console.log("Value set for hiddencid: ", $('.hiddencid').val());
    })

    $('.asel').change(function() {
        $('.hiddenaid').val($('.csel').val());
        console.log("Value set for hiddenaid: ", $('.hiddenaid').val());
    })

    function getCustomers(textArea, callback, delay) {
        var timer = null;
        textArea.onkeypress = function() {
            if (timer) {
                window.clearTimeout(timer);
            }
            timer = window.setTimeout( function() {
                timer = null;
                callback($('#customers-input').val(), true, '#customers-input');
            }, delay );
        };
        textArea = null;
    }

     function getApprovers(textArea, callback, delay) {
        var timer = null;
        textArea.onkeypress = function() {
            if (timer) {
                window.clearTimeout(timer);
            }
            timer = window.setTimeout( function() {
                timer = null;
                callback($('#approvers-input').val(), false, '#approvers-input');
            }, delay );
        };
        textArea = null;
    }

  function makeAjaxCall(url, flag, id) {
    $.ajax({
            type: 'POST',
            url: `http://localhost:8080/search/users?customer=${flag}&q=${url}`,
            dataType: 'json',
            success: function(msg) {
                console.log(typeof(msg))
                setDropdownValues(msg, id, flag);
            }
        });
  }

    function setDropdownValues(msg, id, flag) {
    var htmlString = "";
    var count = 0;
    for (const item in msg) {
        if (count == 0) {
            htmlString = htmlString + "<option selected value=\"" + item + "\">" + msg[item] +"</option>"
            if (flag) {
                $('.hiddencid').val(msg[item]);
            } else {
                $('.hiddenaid').val(msg[item]);
            }

        } else {
            htmlString = htmlString + "<option value=\"" + item + "\">" + msg[item] +"</option>"
        }
        count+=1;
    }
        $(id).next().next().html(htmlString);
        console.log(htmlString);
    }

  getCustomers(document.getElementById("customers-input"), makeAjaxCall, 700)
  getApprovers(document.getElementById("approvers-input"), makeAjaxCall, 700)

});