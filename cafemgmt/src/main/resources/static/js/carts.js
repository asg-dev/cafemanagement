

jQuery(document).ready(function(){

jQuery('.qtyplus').each(function(e) {
var respPar = jQuery(this).prev();
if(respPar.attr('class') != "qty") {
    jQuery(respPar).replaceWith(`<i class="fa fa-minus-square qtyminus fa-xl" field='quantity' style="font-size: 26px;"></i><input type='number' name='quantity' value='0' min="0" max="999"  class='qty' disabled />`);
}
});

    $('.qtyplus').click(function(e){
        e.preventDefault();
        var action = 'plus';
        fieldName = $(this).attr('field');
        var quantity = parseInt($(this).prev().val());
        var itemDetails = (jQuery(this).parent().attr('value')).split("M");
        var menu_id = itemDetails[0];
        var id = itemDetails[1];
        if (!isNaN(quantity)) {
            $(this).prev().val(quantity+=1);
        } else {
            $(this).prev().val(0);
        }
        updateCart(quantity, id, action, menu_id);
    });
    // This button will decrement the value till 0
    $(".qtyminus").click(function(e) {
        e.preventDefault();
        var action = 'minus';
        fieldName = $(this).attr('field');
        var quantity = parseInt($(this).next().val());
        var itemDetails = (jQuery(this).parent().attr('value')).split("M");
        var menu_id = itemDetails[0];
        var id = itemDetails[1];
        if (!isNaN(quantity)) {
            if (quantity-1 < 0) {
                return;
            }
            $(this).next().val(quantity-=1);
        } else {
            $(this).next().val(0);
        }
        updateCart(quantity, id, action, menu_id);
    });

});

    function updateCart(quantity, id, action, menu_id) {
        $.ajax({
            type: 'POST',
            url: '/api/carts',
            dataType: 'json',
            xhrFields: { 'withCredentials': true },
            data: { 'item_id': id,
                    'quantity': quantity,
                    'action': action,
                    'menu_id': menu_id,
            }, success: function(msg) {
                 alert("Added item, please check the cart to checkout");
            }
        });
    }
