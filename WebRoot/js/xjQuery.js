+(function($) {

	$.fn.xtable = function(options2) {
		var options = {
			page : 1,
			rows : 10,
			minPage : 1,
			maxPage : 10,
			prevAndNext : false,
			url : '',
			type : 'get',
			paginationPlacement : ''
		};
		$.extend(options, options2);

		var url2 = options.url + '?' + 'rows=' + options.rows + '&page=';

		var $r1 = $(this);
		var $r2 = $(options.paginationPlacement);

		var $ul = $('<ul>').addClass('pagination').appendTo($r2);
		var doMark = function() {
			$(this).addClass('active').siblings().removeClass('active');
		}
		var updatePages = function(page) {
			options.page = page;
		};
		var doMark2 = function() {
			doMark.apply(this);
			$r1.load($(this).find('a').data('url'));
			updatePages($(this).data('a').data(page));
		}
		if (options.type === 'get') {
			for (var i = options.minPage; i <= options.maxPage; ++i) {
				$('<a>').attr('href', url2 + i).text(i).appendTo(
						$('<li>').addClass(options.page == i ? 'active' : '')
								.appendTo($ul).click(doMark));
			}
			if (options.prevAndNext) {
				var b = options.page == 1;
				$('<a>').attr('href',
						b ? 'javascript:void(0)' : (url2 + (options.page - 1)))
						.text('<').appendTo(
								$('<li>').addClass(b ? 'disabled' : '')
										.prependTo($ul));
				b = options.page == options.maxPage;
				$('<a>').attr('href',
						b ? 'javascript:void(0)' : (url2 + (options.page + 1)))
						.text('>').appendTo(
								$('<li>').addClass(b ? 'disabled' : '')
										.appendTo($ul));
			}
		} else if (options.type === 'load') {
			for (var i = options.minPage; i <= options.maxPage; ++i) {
				$('<a>').attr('href', 'javascript:void(0)').data('url',
						url2 + i).data('page', i).text(i).appendTo(
						$('<li>').addClass(options.page == i ? 'active' : '')
								.appendTo($ul).click(doMark2));
			}
			if (options.prevAndNext) {

				var b = options.page == 1;
				var p = url2 + (b ? 1 : options.page - 1);
				$('<a>').attr('href', 'javascript:void(0)').data('url', p)
						.text('<').appendTo(
								$('<li>').addClass(b ? 'disabled' : '')
										.prependTo($ul).click(doMark2));

				b = options.page == options.maxPage;
				p = url2 + (b ? options.maxPage : options.page + 1);
				$('<a>').attr('href', 'javascript:void(0)').data('url', p)
						.text('>').appendTo(
								$('<li>').addClass(b ? 'disabled' : '')
										.appendTo($ul).click(doMark2));
			}
		}
	}

})(jQuery);

$.validator
		.addMethod(
				"xremote",
				function(value, element, param) {

					if (this.optional(element)) {
						return "dependency-mismatch";
					}

					var previous = this.previousValue(element), validator, data;

					if (!this.settings.messages[element.name]) {
						this.settings.messages[element.name] = {};
					}
					previous.originalMessage = this.settings.messages[element.name].xremote;
					this.settings.messages[element.name].xremote = previous.message;

					param = typeof param === "string" && {
						url : param
					} || param;

					if (previous.old === value) {
						return previous.valid;
					}

					previous.old = value;
					validator = this;
					this.startRequest(element);
					data = {
						randomNumber : Math.random()
					};
					data[element.name] = value;
					var obj = $
							.extend(
									true,
									{
										url : param,
										mode : "abort",
										port : "validate" + element.name,
										dataType : "json",
										data : data,
										context : validator.currentForm,
										success : function(response) {
											var valid = (response.success === true || response.success === "true"), errors, message, submitted;
											if (valid) {
												submitted = validator.formSubmitted;
												validator
														.prepareElement(element);
												validator.formSubmitted = submitted;
												validator.successList
														.push(element);
												delete validator.invalid[element.name];
												validator.showErrors();
											} else {
												errors = {};
												message = response.msg
														|| validator
																.defaultMessage(
																		element,
																		"xremote");
												errors[element.name] = previous.message = $
														.isFunction(message) ? message(value)
														: message;
												validator.invalid[element.name] = true;
												validator.showErrors(errors);
											}
											previous.valid = valid;
											validator.stopRequest(element,
													valid);
										}
									}, param);
					$.ajax(obj);
					return "pending";

				}, '字段远程验证失败');

$.validator.addMethod("regex", function(value, element, param) {
	var re = new RegExp(param);
	return this.optional(element) || re.test(value);
}, '不满足正则表达式{0}');

$.xserialize = function(options) {
	var $form = $.xserialize.form.attr('action', '/sdf/sdf').html('');
	for ( var k in options)
		$('<input/>').attr('name', k).val(options[k]).appendTo($form);
	return $form.serialize();
};
$.xserialize.form = $('<form>');





xzc = xzc||{};

xzc.url = xzc.url || {};

xzc.getJSON = function(url, arg1, arg2) {
	var rdata = {
		randomNumber : Math.random()
	};
	if (arguments.length == 1)
		return jQuery.getJSON(url, rdata);
	else if (arguments.length == 2)
		return jQuery.getJSON(url, jQuery.extend(rdata, arg1));
	else
		return jQuery.getJSON(url, jQuery.extend(rdata, arg1), arg2);
};

/*
 * 对购物车进行修改 opts={ 设置id为1的商品加1件 'item.id':1, quantity:1, edit:false,
 * 
 * 设置id为1的商品为4件 'item.id':1, quantitys:4, edit:false,
 * 
 * 批量更新 设置为相应的件数 'ids[0]':1, 'ids[1]':2, 'quantitys[0]':3, 'quantitys[1]':4,
 * batch:true, edit:true }
 */
xzc.addToCart = function(opts, callback) {
	xzc.getJSON(xzc.url.addToCartUrl, opts, function(result) {
		if (typeof (callback) === 'function')
			callback(result);
	});
};
xzc.url.addToCartUrl = xzc.ctxPath + '/cart/addToCartAjax';

xzc.dump = function(obj) {
	for ( var i in obj) {
		document.write(i + "=" + obj[i] + "<br/>");
	}
};

xzc.updateTooltip = function(element, tooltip) {
	if (element.data('tooltip') != null) {
		element.tooltip('hide');
		element.removeData('tooltip');
	}
	element.tooltip({
		title : tooltip
	});
}

// 先不做成fn的形式好了
xzc.slideToggle = function($toggle, $element, options2) {
	var options = jQuery.extend({
		html : true,
		on : '收起',
		off : '展开',
		init : 'off',
		initDuration : 300,
	}, options2);
	$element[options.init == 'on' ? 'slideDown' : 'slideUp']({
		duration : options.initDuration,
	});
	$toggle[options.html ? 'html' : 'text'](options[options.init]);

	$toggle.on('click', function() {
		if ($element.is(':animated'))
			return;
		var visible = !$element.is(':visible');
		$element.slideToggle();
		$toggle[options.html ? 'html' : 'text'](visible ? options.on
				: options.off);
	});
};

// 自动分页
xzc.pagination = function(options) {
	var opt = jQuery.extend({
		page : 1,
		rows : 3,
		total : 5,
	}, options);

	var $target = options.target;
	var $pager = options.pager;
	var url = opt.url;
	var ajax = options.ajax ? true : false;
	delete opt.url;
	delete opt.target;
	delete opt.pager;

	var $pagination = $('<ul class="pagination">').appendTo($pager);
	function buildPage() {

		var beg = opt.page - 3 > 0 ? opt.page - 3 : 1;
		var _maxPage = parseInt(opt.total / opt.rows)
				+ (opt.total % opt.rows == 0 ? 0 : 1);
		var end = opt.page + 3 < _maxPage ? opt.page + 3 : _maxPage;
		var visiblePage = end - beg + 1;

		while (beg > 1 && visiblePage < 7) {
			--beg;
			++visiblePage;
		}
		while (end < _maxPage && visiblePage < 7) {
			++end;
			++visiblePage;
		}
		if (end == 0) {
			$('<a>').text('没有数据').appendTo($('<li>').appendTo($pagination))
					.attr('href', 'javascript:void(0)');
		} else
			for (var i = beg - 1; i <= end + 1; ++i) {
				var $a = $('<a>');
				if (i == beg - 1)
					$a.data('page', 1).text('<');
				else if (i == end + 1)
					$a.data('page', _maxPage).text('>');
				else
					$a.data('page', i).text(i);
				$a.appendTo(
						$('<li>').appendTo($pagination).addClass(
								i == opt.page ? "active" : "")).attr('href',
						'javascript:void(0)').click(
						function() {

							var $this = $(this);
							var page = $this.data('page');
							if (ajax) {
								$target.load(url, $.extend({}, opt, {
									page : page
								}), function() {
									$this.parent().addClass('active')
											.siblings().removeClass('active');
									opt.page = page;
									$pagination.html('');
									buildPage();
								});
							} else {
								location = url + "?page=" + page;
							}
						});
			}
	}
	buildPage();
}

/* 添加bootstrap错误提示的validate */
xzc.validate = function(target, opt) {
	opt = $.extend({
		success : function(element) {
			element.parent().removeClass('has-error').addClass('has-success')
					.end().remove(); // 给框框加上颜色
		},
		highlight : function(element, errorClass, valid) {
			$(element).addClass(errorClass).parent().removeClass('has-success')
					.addClass('has-error');
		}

	}, opt);
	var $target = $(target);
	$target.validate(opt);
}

/**
 * 一个简单的消息框,依赖于jsviews
 */
xzc.alert = function(opt) {
	opt = $.extend({
		title : "提示",
		panelClass : "panel-primary",
		submitText : "确定",
		showCancel : true,
		showSubmit : true,
		cancelText : "取消",
		body : "",
	}, opt);
	var html = $.templates(xzc.alert.style1).render(opt);
	$('#xzc-dialog').remove();
	$(html).appendTo($('body')).modal({
		keyboard : false,
	// backdrop : false,
	});
}
xzc.alert.style1 = '\
	<div id="xzc-dialog" class="modal fade"> \
		<div class="modal-dialog" > \
			<div class="modal-content panel-default {{:panelClass}}"> \
				<div class="modal-header panel-heading"> \
					<button type="button" class="close" data-dismiss="modal">&times;</button> \
					<h4 class="modal-title">{{:title}}</h4> \
				</div> \
				<div class="modal-body"> \
					{{:body}} \
				</div> \
				<div class="modal-footer"> \
				{{if showCancel}} \
					<button type="button" class="btn btn-default" data-dismiss="modal">{{:cancelText}}</button> \
				{{/if}} \
				{{if showSubmit}} \
					<button type="button" class="btn btn-primary" data-dismiss="modal">{{:submitText}}</button> \
				{{/if}} \
				</div> \
			</div> \
		</div> \
	</div>';