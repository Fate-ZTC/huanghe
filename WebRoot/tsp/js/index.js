$(".xuan").click(function(){
	$(this).toggleClass("pay-checked");
})

// 选择停车场
//$(".click_select").first().addClass("click_select-act");
//submit();
var flog=true;
$(".place-list li").click(function(){
	if (flog) {
	$(".place-list li").find(".click_select").removeClass("click_select-act");
	$(this).find(".click_select").addClass("click_select-act")
	submit();
	};
})
// 提交选中的停车场
function submit(){
	$(".place-btn").click(function(){
		$(".mesg-place").text($(".click_select-act").prev(".place").text());
		park_id = $(".click_select-act").prev(".place").attr("rel");
		if(park_id != '' && car_plate != ''){
			$.ajax({
	            type:'post',
	            url:the_host + 'wxCost_ajaxCountPay',
	            data:{
	    			carparkid:park_id,
	    			carPlate:car_plate
	            },
	            dataType:'json',
	            success:function(msg){
	            	console.log(msg)
	            	if(msg.status == 'success'){
	            		$("#in-time").html(msg.inTime);
	            		$("#park-time").html(msg.parkTime);
	            		$("#s-pay").html(msg.shouldPay);
	            		$("#h-pay").html(msg.hasPay);
	            		$("#n-pay").html(msg.needPay);
	            		$("#park-time-1").html(msg.parkTime);
	            		$("#n-pay-1").html(msg.needPay);
	            		kid=msg.kid;
	            		amount = msg.needPay;
	            	}
	            	else{
	            		$("#in-time").html('');
	            		$("#park-time").html('');
	            		$("#s-pay").html('');
	            		$("#h-pay").html('');
	            		$("#n-pay").html('');
	            		$("#park-time-1").html('');
	            		$("#n-pay-1").html('');
	            		layer.open({
							content: msg.errMsg
							,skin: 'msg'
							,time: 2 //2秒后自动关闭
						});
	            	}
	            }
	        })
		}
		else{
			layer.open({
				content: '请选择停车场或车牌号码'
				,skin: 'msg'
				,time: 2 //2秒后自动关闭
			});
		}
		
		$(".place-btn").unbind("click");
		$(".box-place-select").hide();
		$(".box-start").show();
	})
	
}

//确认支付
$(".sure-btn").click(function(){
	$(".get-pay").fadeOut(300);
	$(".shodow").hide();
})

$(".chongshi-btn").click(function(){
	$(".get-lose").fadeOut(300);
	$(".shodow").hide();
})

$(".know").click(function(){
	$(".pay-lose-cash").fadeOut(300);
	$(".shodow").hide();
})

$(".detail-btn").click(function(){
	$(".get-lose").fadeOut(300);
	$(".pay-lose-cash").fadeOut(300);
	$(".shodow").hide();
})


