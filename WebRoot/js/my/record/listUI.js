// 依赖jQuery
$( function( ) {

	xzc.pagination( {
		url : '${_absurl}',
		target : $( '#listUI-wrapper' ),
		pager : $( '#listUI-pager' ),
		page : ${page},
		total : ${total},
		rows : ${rows},
		ajax : 'records',
		state : ${state},
	} );
	
} );