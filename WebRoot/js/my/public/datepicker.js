//依赖于 jQuery 和 underscore
// 对jQuery UI的datepicker进行一点封装
xzc.datepicker = function(target, opts) {
	var defaultOptions = {
		dateFormat : 'yy-mm-dd',
		showAnim : 'slideDown',
		showOtherMonths : true,// 显示别的月份的那几天
		selectOtherMonths : true,
		showButtonPanel : true,// 显示一个按钮面板
		changeMonth : true,// 让你可以选择年,否则你要一个月一个月的跳才能找到你想要的年份
		changeYear : true,
		yearRange : "1900:c",// 表示显示年份的可选范围,默认是 "c-10:c+10" 意思是 当前选中的年的前后10年是可选的
		// 一开始你在2015年,假设你要切换到1980年,那么你需要先跳到2005年(因为那个页面就只显示了10条记录),然后再跳...
		monthNamesShort : _.map(_.range(1, 12 + 1), function(value) {
					return value + "月";
				}),
		minDate : new Date(1900, 1 - 1, 1),
		maxDate : "+0D" // 表示不能超过今天
		// numberOfMonths : 3,//同时显示3个月的范围
	}
	var opts = $.extend(defaultOptions, opts);
	$(target).datepicker(opts);
};