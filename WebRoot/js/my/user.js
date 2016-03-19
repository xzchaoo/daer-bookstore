// 数据验证的基本模式
// 可以根据需要进行修改
var opts = {
	'user' : {
		rules : {
			'user.name' : {
				required : true,
				minlength : 4,
				maxlength : 20,
				regex : /^xzc\d+$/,
				xremote : xzc.ctxPath + '/user/checkNameAvailableAjax'
			},
			'user.password' : {
				required : true,
				minlength : 6,
				maxlength : 20
			},
			'user.email' : {
				required : true,
				email : true
			},
			'user.birthday' : {
				required : true,
				date : true
			},
			'user.sex' : {
				required : true,
				min : 0,
				max : 2
				,
			}
		},
		messages : {
			'user.name' : {
				required : '名称必须填写',
				minlength : '名称长度必须大于等于{0}',
				maxlength : '名称长度必须小于等于{0}',
				regex : '名称必须以xzc开头,其余是数字'
			},
			'user.password' : {
				required : '密码必须填写',
				minlength : '密码长度必须大于等于{0}',
				maxlength : '密码长度必须小于等于{0}'
			},
			'user.email' : {
				required : '邮箱必须填写',
				email : '请填写合法的邮箱地址'
			},
			'user.birthday' : {
				required : '出生日期必须填写',
				date : '必须是个合法的日期'
			},
			'user.sex' : '请选择性别'
		},
		success : function(element) {
			// element是那个错误标签
			// 现在是success看你要怎么处置它
			// 我们必须要把它remove掉
			// 这样下次字段验证错误的时候就会因为
			// 没有错误标签而重新生成,然后才会调用errorPlacement,否则errorPlacement只会被调用一次!
			element.parent().removeClass('has-error').addClass('has-success').end().remove(); // 给框框加上颜色
		},
		// errorPlacement : function( error , element ) {
		// error.appendTo( element.parent( ).removeClass( 'has-success' ).addClass( 'has-error' ) );
		// },
		highlight : function(element, errorClass, valid) {
			$(element).addClass(errorClass).parent().removeClass('has-success').addClass('has-error');
		}
	}
};

// 对form表单进行验证
// optCallback是一个回调函数 允许它对参数fo进行修改
xzc.validateForm = function($form, type, optCallback) {
	var fo = {};
	jQuery.extend(fo, xzc.validateForm.opts[type]);
	if (typeof(optCallback) == 'function')
		optCallback(fo);
	var vrf = $form.validate(fo);
	return vrf;
}
xzc.validateForm.opts = opts;
opts = null;
