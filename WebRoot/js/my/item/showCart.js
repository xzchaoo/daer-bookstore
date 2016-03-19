$( function( ) {
	$( 'form' ).validate( {
		rules : {
			'e.quantity' : {
				required : true,
				digits : true,
				min : 0,
				max : 100,
			}
		},
		messages : {
			'e.quantity' : {
				required : "请输入数量",
				digits : "请输入一个正整数",
				min : "必须大于等于{0}",
				max : "必须小于等于{0}",
			}
		},
		success : function( element ) {
			element.hide( );
		},
		errorPlacement : function( error , element ) {
			error.appendTo( element.closest( 'div[class*="row"]' ).next( ).find( 'div' ).html( '' ) );
		}
	} );
	$( '.item-add' ).click( function( ) {
		var item_id = $( this ).closest( 'tr' ).data( 'id' );
		var $quantity = $( '#quantity-' + item_id );
		$quantity.val( parseInt( $quantity.val( ) ) + 1 );
	} );
	$( '.item-sub' ).click( function( ) {
		var item_id = $( this ).closest( 'tr' ).data( 'id' );
		var $quantity = $( '#quantity-' + item_id );
		$quantity.val( parseInt( $quantity.val( ) ) - 1 );
	} );
	$( '.item-clear' ).click( function( ) {
		var item_id = $( this ).closest( 'tr' ).data( 'id' );
		var $quantity = $( '#quantity-' + item_id );
		$quantity.val( 0 );
	} );

	$( '.item-save' ).click( function( ) {
		var $tr = $( this ).closest( 'tr' );
		var item_id = $tr.data( 'id' );
		var quantity = $( '#quantity-' + item_id ).val( );
		addToCart( item_id , quantity , true , function( ) {
			if ( quantity == 0 )
				$tr.remove( );
		} );
	} );
	$( '.item-del' ).click( function( ) {
		var $tr = $( this ).closest( 'tr' );
		var item_id = $tr.data( 'id' );
		$.xzc.addToCart( {
			'item.id' : item_id,
			count : 0,
			edit : true
		} , function( ) {
			$tr.remove( );
		} );
	} );
	$( '#selectAll' ).click( function( ) {
		$( ':checkbox' ).prop( 'checked' , $( this ).is( ':checked' ) );
		calcTotalPrice( );
	} );
	$( ':checkbox' ).not( '#selectAll' ).click( function( ) {
		if ( !$( this ).is( ':checked' ) )
			$( '#selectAll' ).prop( 'checked' , false );
		else {
			var shouldSelectAll = true;
			$( ':checkbox' ).not( '#selectAll' ).each( function( ) {
				if ( !$( this ).is( ':checked' ) )
					shouldSelectAll = false;
			} );
			if ( shouldSelectAll )
				$( '#selectAll' ).prop( 'checked' , true );
		}
		calcTotalPrice( );
	} );

	$( '#item-del-all' ).click( function( ) {
		$( '#cart > tbody > tr' ).each( function( ) {
			var item_id = $( this ).data( 'id' );
			var isSelected = $( '#checkbox-' + item_id ).is( ':checked' );
			if ( isSelected )
				$( '#quantity-' + item_id ).val( 0 );
		} );
	} );
	$( '#item-save-all' ).click( function( ) {
		var params = {
			edit : true,
			batch : true,
		};

		var length = 0;
		$( '#cart > tbody > tr' ).each( function( ) {
			var item_id = $( this ).data( 'id' );
			var isSelected = $( '#checkbox-' + item_id ).is( ':checked' );
			if ( isSelected ) {
				params['ids[' + length + ']'] = item_id;
				params['counts[' + length + ']'] = $( '#quantity-' + item_id ).val( );
				++length;
			}
		} );
		$.xzc.addToCart( params , function( result ) {
			location.reload( r );
		} );
	} );
	$( '#item-select-all' ).click( function( ) {
		if ( $( this ).text( ) == '全选' ) {
			$( ':checkbox' ).prop( 'checked' , true );
			$( this ).text( '取消' );
		} else {
			$( ':checkbox' ).prop( 'checked' , false );
			$( this ).text( '全选' );
		}
		calcTotalPrice( );
	} );
	$( '#item-do-checkout' ).click( function( ) {
		var params = {
			randomNumber : Math.random( )
		};
		var length = 0;
		$( '#cart > tbody > tr' ).each( function( ) {
			var $tr = $( this );
			var item_id = $( this ).data( 'id' );
			var isSelected = $( '#checkbox-' + item_id ).is( ':checked' );
			if ( isSelected ) {
				params['ids[' + length + ']'] = item_id;
				++length;
			}
		} );
		$.getJSON( '${ctxPath}/item/doCheckoutAjax' , params , function( result ) {
			if ( result.success )
				location.reload( );
			else
				alert( result.msg + '请先去充值吧...' );
		} );
	} );
} );

function calcTotalPrice( ) {
	var totalPrice = 0;
	$( '#cart > tbody > tr' ).each( function( ) {
		var item_id = $( this ).data( 'id' );
		var $checkbox = $( '#checkbox-' + item_id );
		if ( $checkbox.is( ':checked' ) ) {
			var price = $( '#price-' + item_id ).text( );
			var quantity = $( '#quantity-' + item_id ).val( );
			totalPrice += price * quantity;
		}
	} );
	$( '#totalPrice' ).text( totalPrice );
}
