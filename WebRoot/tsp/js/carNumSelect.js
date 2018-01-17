function carNumselect(selectObj){
var flag=false;
var index=0;
select(index);
function select(bb){
	if (bb<=6) {
		$(".select").removeClass("option-active")
		$(".select").eq(bb).addClass("option-active");
		start();
	};
}

// 选择车牌执行函数
function start(){
	var obj=$(".option-active");
	zhixing(obj);
}
function zhixing(aa){
	if ($(aa).hasClass("province")) {
			$(".value-letter-bg").hide();
			$(".value-sel-bg").show();
			$(".option").unbind("click")
			$(".option").click(function(){
				$(".option-active").text($(this).text());
				$(".option").unbind("click");
				pan();
				index++;
				select(index);
		})

		}else{
			$(".value-sel-bg").hide();
			$(".value-letter-bg").show();
			$(".option").unbind("click");
			$(".option").click(function(){
				$(".option-active").text($(this).text());
				$(".option").unbind("click");
				pan();
				index++;
				select(index);
			})
	}
}

$(".select").click(function(){
	$(".select").removeClass("option-active")
	$(this).addClass("option-active");
	index=$(this).index();
	if (index>2) {
		index--;
	};
	start();
	
})
del();
function pan(){
	var num=0;
	$(".select").each(function(i){
		if ($(this).text()!="") {
			num++;
		}
	})
	if (num>=7) {
		$(".complet").addClass("complet_act");
		flag=true;
		select_complet();
	}else{
		$(".complet").removeClass("complet_act");
		flag=false;
		select_complet();
	}
}
function select_complet(){
	$(".complet_act").unbind("click");
	$(".complet_act").click(function(){
		if (flag) {
		var str='';
		for (var i = 0; i < 7; i++) {
			if (i==1) {
				str+=$(".select").eq(i).text();
			}else{
				str+=$(".select").eq(i).text();
			}
		};
		if($(selectObj).text()==""){
			$.ajax({
	            type:'post',
	            url:the_host + 'wxCarManage_add',
	            data:{
	    			mobile:mobile,
	    			carPlate:str
	            },
	            dataType:'json',
	            success:function(msg){
	            	if(msg.status == 'true'){
	            		console.log(msg);
	            		kid = msg.kid;
	            		$(selectObj).parent().find(".edit-btn").attr("rel",kid);
	            		$(selectObj).parent().find(".del-btn").attr("rel",kid);
	            		$(".input-box .select").text("");
						$(".box-num-select").hide();
						$(".box-vehicle").show();
						$(".box-car-select").show();
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
		}else{
			$.ajax({
	            type:'post',
	            url:the_host + 'wxCarManage_update',
	            data:{
	    			kid:kid,
	    			mobile:mobile,
	    			carPlate:str
	            },
	            dataType:'json',
	            success:function(msg){
	            	if(msg.status == 'true'){
	            		$(".input-box .select").text("");
						$(".box-num-select").hide();
						$(".box-vehicle").show();
						$(".box-car-select").show();
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
		$(selectObj).text(str);
		
		
		$(".input-box .select").text("");
		$(".box-num-select").hide();
		$(".box-vehicle").show();
		$(".box-car-select").show();

	};
	})
}
function del(){
	$(".del").click(function(){
		
		if ($(".option-active").text()=="") {
			$(".option-active").removeClass("option-active").prev().addClass("option-active");
			index=$(".option-active").index();
		}else{	
		if($(".option-active").prev().attr("index")==1){
			$(".value-letter-bg").hide();
			$(".value-sel-bg").show();
		}
			$(".option-active").text("").removeClass("option-active").prev().addClass("option-active");
			index=$(".option-active").index();
		}
		pan();
		start();
	})
	
}

}