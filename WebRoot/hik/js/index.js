$(function(){
	$('.depot-btn').on('touchstart',function(){
		$(this).addClass('depot-btn-bg1');
	})
	$('.depot-btn').on('touchend',function(){
		$(this).removeClass('depot-btn-bg1');
	})
})
