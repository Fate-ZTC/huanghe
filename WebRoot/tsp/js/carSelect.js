$(function(){
	function operaCar(){
		var n=3-$(".vehicle-li").length;
		$(".vehicle-num").text(n);
		// 删除车牌
		$(".vehicle-li").unbind("click");
		$(".vehicle-li").click(function(){
			$(this).find(".carSelect").prop("checked","checked");
		})

		// 确认
		$(".car-sure").unbind("click");
		$(".car-sure").click(function(){
			if ($(".carSelect:checked").length!=1) {
				layer.open({
					content: '最少选一辆车'
					,skin: 'msg'
					,time: 2 //2秒后自动关闭
				});
				return;
			};
			var carNum=$(".carSelect:checked").prev().text();
			$(".mesg-num").text(carNum);
			car_plate = carNum;
			
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
			
			$(".box-start").show();
			$(".box-car-select").hide();
		})

		$(".revamp-num").unbind("click");
		$(".revamp-num").click(function(){
			$(".box-start").hide();
			$(".box-car-select").show();
		})
	}
operaCar();	

	// 添加车牌
	$(".vehicle-add-btn").click(function(){
		if ($(".vehicle-li").length>=3) {
			layer.open({
				content: '最多只允许添加3辆车'
				,skin: 'msg'
				,time: 2 //2秒后自动关闭
			});
			return;
		};
		$(".vehicle-list").append('<li class="vehicle-li"><span class="car-num"></span><input type="radio" name="car" class="carSelect"></li>')
		$(".box-car-select").hide();
		$(".box-num-select").show();
		var obj=$(".car-num").last();
		carNumselect(obj);
		operaCar();
	})
})