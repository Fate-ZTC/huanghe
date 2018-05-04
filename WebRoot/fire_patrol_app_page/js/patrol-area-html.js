//日期切换
$(".date-tab-list li").click(function(){
	$(this).addClass("active").siblings("li").removeClass("active");
})

//状态切换
$(".situation-tab-list li").click(function(){
	$(this).addClass("active").siblings("li").removeClass("active");
})

//更多内容展开、收起
abnormalMore();
function abnormalMore(){
	$(".patrol-situation-abnormal-more").unbind("click");
	$(".patrol-situation-abnormal-more").click(function(){
		if ($(this).prev("ul").hasClass("hidden-latter")) {
			$(this).prev("ul").removeClass("hidden-latter");
			$(this).text("收起");
		} else{
			$(this).prev("ul").addClass("hidden-latter");
			$(this).text("更多");
		}
	})
}

//日历选择
var calendar = new datePicker();
calendar.init({
	'trigger': '#dateBtn', /*按钮选择器，用于触发弹出插件*/
	'type': 'date',//模式：date日期；datetime日期时间；time时间；ym年月；year 年；年月日+时分秒：dateTimeSecond； month 月；dateHour 
	'minDate':'1900-1-1',/*最小日期*/
	'maxDate':'2100-12-31',/*最大日期*/
	'onSubmit':function(){/*确认时触发事件*/
		var theSelectData=calendar.value;
		console.log(calendar.value);
		$(".date-select-result span").text(theSelectData);
		$(".date-tab-list").hide();
		$(".date-select-result").show();
	},
	'onClose':function(){/*取消时触发事件*/
	}
});

//删除选中的时间
$(".delete-date-result").click(function(){
	$(".date-tab-list").show();
	$(".date-select-result").hide();
	$(".date-select-result span").text("");
})