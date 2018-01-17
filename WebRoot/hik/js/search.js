$(function(){
	var path = $("#path").val();
	window.addEventListener('load', function() {
	   FastClick.attach(document.body);
	}, false);
	$('#search-btn').click(function(){
		var oval = $('#search-text').val();
		if(oval == ''){
			$('.test-hint-word').text('请输入要查询的车牌号');
			$('.test-hint').fadeIn(300).delay(1000).fadeOut(300);
			var oh = -$('.test-hint').height()/2;
			var ow = -$('.test-hint').width()/2;
			$('.test-hint').css({
				'left':'50%',
				'top':'50%',
				'margin-top':oh,
				'margin-left':ow
			})
		}
		else{
			$.ajax({
				type:'get',
				url:path+'carNumberSer',
				data:{
					"carNumber":oval
				},
				dataType:'json',
				success:function(msg){
					console.debug(msg.status);
					if(msg.status=="true"){
						$('.search-reminder').hide();
						$('.search-result').hide();
						$('.search-reminder').hide();
						$('.search-result').children().remove();
						var op = "<p>车牌号：<span>"+msg.para.carNumber+"</span></p>" +
						"<p>停车场：<span>"+msg.para.carparkName+"</span></p>" +
						"<p>车位号：<span>"+msg.para.berthNum+"</span></p>"+
						"<p>停车类型：<span>"+msg.para.parktype+"</span></p>"+
						"<p>入场时间：<span>"+msg.para.enterTime+"</span></p>"+
						"<p>停车时长：<span>"+msg.para.timed+"</span></p>"+
						"<p>停车费用：<span>"+msg.para.shouldPayMoney+"</span></p>"+
						"<p>图片：</p>"+
						"<img class='search-success-pic radius-10' src='"+msg.para.carNumImgPath+"' />";
						$('.search-success').html(op);
						$('.search-success').show();
						$('.search-fail').hide();
						$('.search-result').hide();
						$('.search-reminder').hide();
					}else{
						$('.search-fail').show();
						$('.search-reminder').hide();
						$('.search-result').hide();
						$('.search-success').hide();
					}
				},
				error:function(){
					$('.test-hint-word').text('网络请求超时，请检查您的网络设置');
					$('.test-hint').fadeIn(300).delay(1000).fadeOut(300);
					var oh = -$('.test-hint').height()/2;
					var ow = -$('.test-hint').width()/2;
					$('.test-hint').css({
						'left':'50%',
						'top':'50%',
						'margin-top':oh,
						'margin-left':ow
					})
				}
			});
		}
	})
	//点击检索出来的结果，将其写入搜索框，并显示该车牌对应车的信息
	$(document).on('click','.search-result li',function(){
		$('#search-text').val($(this).find('span').first().text());
		$('.search-reminder').hide();
		$('.search-result').hide();
		$('.search-fail').hide();
		$('.search-success').show();
	})
	$('#search-text').keyup(function(){
		if($('#search-text').val() == ''){
			$('.search-reminder').show();
			$('.search-result').hide();
			$('.search-fail').hide();
			$('.search-success').hide();
		}
	})
	
	//清除搜索框文字
	$('#clear-txt').click(function(){
		$('#search-text').val('');
		$('#search-text').focus();
		$('.search-reminder').show();
		$('.search-result').hide();
		$('.search-fail').hide();
		$('.search-success').hide();
	})
})
