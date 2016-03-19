// 依赖于jQuery
xzc.setSecondNav = function( opts ) {
	var $breadcrumb = $( '#breadcrumb' );
	$( opts ).each( function( ) {
		$( '<a>' ).attr( 'href' , this.url ).text( this.name ).appendTo( $( '<li>' ).appendTo( $breadcrumb ) );
	} );
	var $li = $breadcrumb.find( '> li' ).last( );
	$li.text( $li.find( '> a' ).text( ) );
};

$( function( ) {
	
	/**
	 * 初始化totop
	 */
	//一开始先隐藏
	$( ".totop" ).hide( );
	//相应屏幕滚动事件 在适当的时候进行显示和隐藏
	$( window ).scroll( function( ) {
		if ( $( this ).scrollTop( ) > 500 ) {
			$( '.totop' ).slideDown( );
		} else {
			$( '.totop' ).slideUp( );
		}
	} );

	//按钮被点击后 500毫秒滚动到顶部
	$( '.totop' ).click( function( e ) {
		//别忘了这句
		e.preventDefault( );
		$( 'body,html' ).animate( {
			scrollTop : 0
		} , 500 );
	} );

} );

