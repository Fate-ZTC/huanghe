$(function(){
	var flag=true;
	$(".code-get").click(function(){
		var phone=$("#phone").val();
		if (phone!='') {
			if(!(/^1[34578]\d{9}$/.test(phone))){
				layer.open({
					content: '手机号有误'
					,skin: 'msg'
					,time: 2 //2秒后自动关闭
				});
			}else{
				if (flag) {
					$(".code-get").css({background:"#ccc"}).html('<img src="'+the_host+'tsp/img/bg.gif" alt="" />');
					flag=false;
					
					$.ajax({
			            type:'post',
			            url:the_host + 'wxUsersManage_sendSms',
			            data:{
			    			phone:phone
			            },
			            dataType:'json',
			            success:function(msg){
			            	if(msg.status == 'true'){
			            		//发送验证码成功后执行
								setTimeout(function(){
									var num=10;
									var time=setInterval(code,1000);
									code();
									function code(){
										if (num>=0) {
											$(".code-get").css({background:"#ccc"}).html(num+'秒后重试');
											num--;
										}else{
											clearInterval(time);
											$(".code-get").css({background:"#12B7F5"}).html('获取验证码');
											flag=true;
										}
									}
								},1000)
			            	}
			            	else{
			            		layer.open({
									content: msg.errorcode
									,skin: 'msg'
									,time: 2 //2秒后自动关闭
								});
			            	}
			           },
			           fail:function(){
			        	   $(".code-get").css({background:"#ccc"}).html('获取验证码');
			        	   flag=true;
			           }
			        })
				}
				
			}
		}else{
			layer.open({
				content: '手机号不能为空'
				,skin: 'msg'
				,time: 2 //2秒后自动关闭
			});
		}
		
	})
	
	
	$(".submit").click(function(){
		var phone=$("#phone").val();
		var code_input=$("#code_input").val();
		
		if (phone!='') {
			if (code_input!='') {
				$.ajax({
		            type:'post',
		            url:the_host + 'wxUsersManage_band',
		            data:{
		    			phone:phone,
		    			code:code,
		    			validate:code_input
		            },
		            dataType:'json',
		            success:function(msg){
		            	if(msg.status == 'true'){
		            		//绑定成功后提示
							layer.open({
								content: '绑定成功！'
								,skin: 'msg'
								,time: 2 //2秒后自动关闭
							});
							if(link != ''){
								window.location.href=the_host+link;
							}
							else{
								window.location.href=the_host+'/wxUsersManage_myZone';
							}
		            	}
		            	else{
		            		layer.open({
								content: msg.errMsg
								,skin: 'msg'
								,time: 2 //2秒后自动关闭
							});
		            		window.location.href = the_host + 'wxUsersManage_toBind';
		            	}
		            }
		        })
			} else{
				layer.open({
					content: '验证码不能为空'
					,skin: 'msg'
					,time: 2 //2秒后自动关闭
				});
			}
		} else{
			layer.open({
				content: '手机号不能为空'
				,skin: 'msg'
				,time: 2 //2秒后自动关闭
			});
		}
	})
})
