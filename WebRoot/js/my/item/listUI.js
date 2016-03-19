$(function() {
			$('#searchForm').validate({
						errorPlacement : function(error, element) {
							error.appendTo($('#errorContainer').html(''));
						},
						rules : {
							'keyword' : {
								required : true,
								minlength : 1
							}
						},
						messages : {
							'keyword' : {
								required : '请输入关键字',
								minlength : '关键字不能少于1个字'
							}
						}
					});	
			
			/*$('#keyword').autocomplete({
						source : [
									"c++", "java", "php", "coldfusion", "javascript", "asp", "ruby"
							]
						});
						*/
			xzc.pagination({
						url : xzc.ctxPath+'/item/listUI',
						target : $('#tbContainer'),
						pager : $('#listUI-pager'),
						rows : q.rows,
						page : q.page,
						total : q.total,
						keyword : q.keyword,
						ajax : 'tt',
						category:q.category
					});
		});