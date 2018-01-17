$(function(){
	function operaCar(){
		var n=3-$(".vehicle-li").length;
		$(".vehicle-num").text(n);
		// 删除车牌
		$(".del-btn").unbind("click");
		$(".del-btn").click(function(){
			kid = $(this).attr("rel");
			var _this=$(this).parent().parent();
			layer.open({
		    content: '确定要删除当前车辆？'
		    ,btn: ['删除', '取消']
		    ,skin: 'footer'
		    ,yes: function(index){
				$.ajax({
		            type:'post',
		            url:the_host + 'wxCarManage_delete',
		            data:{
		    			kid:kid
		            },
		            dataType:'json',
		            success:function(msg){
		            	if(msg.status == 'true'){
		            		$(_this).remove();
					    	var n=3-$(".vehicle-li").length;
							$(".vehicle-num").text(n);
							layer.open({
							    content: '删除成功'
							    ,skin: 'msg'
							    ,time: 2 //2秒后自动关闭
							  });
		            	}
		            	else{
		            		layer.open({
								content: msg.errMsg
								,skin: 'msg'
								,time: 2 //2秒后自动关闭
							});
		            	}
		            }
		        })
		    }
		  });
		})
	// 编辑车牌
	$(".edit-btn").unbind("click");
		$(".edit-btn").click(function(){
			kid = $(this).attr("rel");
			$(".box-vehicle").hide();
			$(".box-num-select").show();
			var obj=$(this).parent().prev();
			carNumselect(obj);
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
		$(".vehicle-list").append('<li class="vehicle-li"><span class="car-num"></span><div class="operation-box float-R"><button class="edit-btn" rel="0">编辑</button><button class="del-btn" rel="0">删除</button></div></li>')
		$(".box-vehicle").hide();
		$(".box-num-select").show();
		var obj=$(".car-num").last();
		carNumselect(obj);
		operaCar();
	})
})