$( function( ) {
	$( 'input[type="text"]' ).on( 'blur' , function( ) {
		calcTotalPrice( );
	} );
	/**
	 * 添加1 减少1 归零
	 */
	$( '.item-add,.item-sub,.item-clear' ).click( function( ) {
		var item_id = $( this ).closest( 'tr' ).data( 'id' );
		var $quantity = $( '#quantity-' + item_id );
		var quantity = parseInt( $quantity.val( ) );
		if ( $( this ).hasClass( 'item-add' ) )
			++quantity;
		else if ( $( this ).hasClass( 'item-sub' ) )
			quantity = quantity > 0 ? quantity - 1 : 0;
		else
			quantity = 0;
		$quantity.val( quantity );
	} );

	/**
	 * 单个保存
	 */
	$( '.item-save' ).click( function( ) {
		var $tr = $( this ).closest( 'tr' );
		var item_id = $tr.data( 'id' );
		var quantity = $( '#quantity-' + item_id ).val( );
		xzc.addToCart( {
			'ids[0]' : item_id,
			'quantities[0]' : quantity
		} , function( ) {
			if ( quantity == 0 )
				$tr.remove( );
		} );
	} );

	$( '.item-del' ).click( function( ) {
		var $tr = $( this ).closest( 'tr' );
		var item_id = $tr.data( 'id' );
		xzc.addToCart( {
			'ids[0]' : item_id,
			'quantities[0]' : 0,
		} , function( result ) {
			if ( result.success )
				$tr.remove( );
			else
				alert( result.msg );
		} );
	} );

	$( '#selectAll' ).on( 'ifClicked' , function( ) {
		// $( ':checkbox' ).prop( 'checked' , $( this ).is( ':checked' ) );
		$( ':checkbox' ).iCheck( $( this ).is( ':checked' ) ? 'uncheck' : 'check' );
		calcTotalPrice( );
	} );
	$( ':checkbox' ).not( '#selectAll' ).on( 'ifChanged' , function( ) {
		if ( !$( this ).is( ':checked' ) )
			$( '#selectAll' ).iCheck( 'uncheck' );
		else {
			var shouldSelectAll = true;
			$( ':checkbox' ).not( '#selectAll' ).each( function( ) {
				if ( !$( this ).is( ':checked' ) )
					shouldSelectAll = false;
			} );
			if ( shouldSelectAll )
				$( '#selectAll' ).iCheck( 'check' );
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

	function _saveAll( callback ) {
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
				params['quantities[' + length + ']'] = $( '#quantity-' + item_id ).val( );
				++length;
			}
		} );
		if ( !length ) {
			alert( "你没有选择任何商品" );
			return;
		}
		xzc.addToCart( params , function( result ) {
			if ( typeof ( callback ) === 'function' )
				callback( result );
		} );
	}
	$( '#item-save-all' ).click( function( ) {
		_saveAll( function( ) {
			location.reload( );
		} );
	} );

	$( '#item-select-all' ).click( function( ) {

		if ( $( this ).text( ) == '全选' ) {
			// $( ':checkbox' ).prop( 'checked' , true );
			$( ':checkbox' ).iCheck( 'check' );
			$( this ).text( '取消' );
		} else {
			// $( ':checkbox' ).prop( 'checked' , false );
			$( ':checkbox' ).iCheck( 'uncheck' );
			$( this ).text( '全选' );
		}

		calcTotalPrice( );
	} );
	
	/**
	 *选中结账 
	 */
	$( '#item-do-checkout' ).click( function( ) {
		//if(!confirm("确定要结账吗?"))
		//	return;
		//先保存所有的购物车数量
		_saveAll( function( result ) {
			if ( !result.success ) {
				alert( result.msg );
				return;
			}
			
			//拼凑数据
			var params = {
				randomNumber : Math.random( )
			};
			var length = 0;
			$( '#cart > tbody > tr:has(:checked)' ).each( function( ) {
				params['ids[' + length + ']'] = $(this).data('id');
				++length;
			} );
			
			if ( length == 0 ) {
				alert( '你没有选中任何商品' );
				return;
			}
			$.getJSON( xzc.ctxPath + '/cart/doCheckoutAjax' , params , function( result ) {
				if ( result.success ) {
					location.href = xzc.ctxPath + '/record/viewDetailUI?record.id=' + result.recordId;
				} else
					alert( result.msg );
			} );

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
