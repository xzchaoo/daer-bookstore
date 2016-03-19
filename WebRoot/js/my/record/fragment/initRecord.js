$( function( ) {

	// ajax删除订单
	$( '.deleteRecord' ).click( function( ) {
		if ( confirm( "你真的要删除这个订单吗?" ) ) {
			var $this = $( this );
			// 发ajax删除请求
			xzc.getJSON( xzc.ctxPath + "/record/deleteRecordAjax" , {
				'record.id' : $this.data( 'id' )
			} , function( res ) {
				if ( res.success ) {
					$this.closest( 'div.panel' ).remove( );
					$( '#total' ).text( parseInt( $( '#total' ).text( ) ) - 1 );
					location.reload();
				}else{
					alert(res.msg);
				}
			} );
		}
	} );

	/**
	 * 用于询问用户是否确认收货
	 */
	$( '.receive-record' ).click( function( e ) {
		if ( !confirm( "真的要确认收货吗?" ) ) {
			e.preventDefault( );
		}
	} );

} );
