<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="msapplication-tap-highlight" content="no" />
    <link rel="stylesheet" href="<%=basePath%>tsp/page/css/common.css">
    <link rel="stylesheet" href="<%=basePath%>tsp/page/css/newAdd.css">
    <link rel="stylesheet" type="text/css" href="<%=path %>/layer3.1.1/mobile/need/layer.css" />
    <script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
	<script type="text/javascript" src="<%=path %>/layer3.1.1/layer.js"></script>
	<style>
	.fail{
		font-size: .1rem;
	    color: #e99856;
	    height: .1rem;
	    line-height: .1rem;
	    float: right;
	    margin-left: 0.1rem;
	}
	</style>
    <title>消费明细</title>
</head>
<body>
    <div class="classify f14 color-6 posit-AB">
        <div class="classifyTitle ">
            <span>全部分类</span>
            <i class="jiantu"></i>
        </div>
        <!--分类对数据库进行查询   ajax加载数据  -->
        <ul class="classifyList">
        	<li  class="" style="width: 25%;" chooseType="all">全部分类</li>
            <li  class="" style="width: 25%;" chooseType="tem">停车缴费</li>
            <li  class="" style="width: 25%;" chooseType="rent">月租办理</li>
            <li  class="" style="width: 25%;" chooseType="renew">月租续费</li>
        </ul>
    </div>
    <div id="wrapper">
		<ul class="detailList"></ul>
	</div>
    <div class="zhezhao posit-AB"></div>
    <input type="hidden" id="choose"/>
    <input type="hidden" id="mobile" value=""/>
</body>

<script src="<%=basePath%>tsp/page/js/iscroll.js"></script>
<script src="<%=basePath%>tsp/page/js/pullToRefresh.js"></script>
<script src="<%=basePath%>tsp/page/js/newAdd.js"></script>
<script type="text/javascript">
	document.body.addEventListener('touchstart', function () { 
		//...空函数即可
	});
	var path = '<%=basePath%>';
	var currentPage = 1;
	var totalPage = 0;
	var mobile = $("#mobile").val();
	$(function(){
		refresher.init({
			id:"wrapper",
			pullDownAction:Refresh,
			pullUpAction:Load
		});
		$.post(path+'expenseDetail',{'mobile':mobile,'choose':'all','page':currentPage,'pageSize':9},function(data){
		 	$.ajaxSetup({
				async: false
			});
	 		console.log(data);
	 		var tem='';
	 		var rent='';
	 		var renew='';
	 		var refund='';
		   		if(data.status=='true'){
		   			$.each(data.list, function(index,term) {
		   				if (term.parktype==1 && term.payStatus != -1) {
			   					tem += "<li class='detailLi reduce' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'>临停</i><span class='address'>"+term.carparkname+"</span></div>"
		   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div></li>";
						}else if(term.parktype==1 && term.payStatus == -1){
		   					tem += "<li class='detailLi reduce' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'>临停</i><span class='address'>"+term.carparkname+"</span></div>"
		   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div><div class='fail'>交易失败</div></li>";
						}
		   				if (term.parktype != 1 && term.isRenew==0 && term.payStatus != -1) {
		   					rent += "<li class='detailLi add' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'>月租</i><span class='address'>"+term.carparkname+"</span></div>"
		   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div></li>";
						}else if(term.parktype != 1 && term.isRenew==0 && term.payStatus == -1){
		   					rent += "<li class='detailLi add' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'>月租</i><span class='address'>"+term.carparkname+"</span></div>"
		   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div><div class='fail'>交易失败</div></li>";
						}
		   				if (term.parktype != 1 && term.isRenew==1 && term.payStatus != -1) {
		   					renew += "<li class='detailLi add' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'></i><span class='address'>月租续费</span></div>"
		   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div></li>";
						}else if(term.parktype != 1 && term.isRenew==1 && term.payStatus == -1){
		   					renew += "<li class='detailLi add' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'></i><span class='address'>月租续费</span></div>"
		   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div><div class='fail'>交易失败</div></li>";
						}
		   				if (term.payStatus == -1) {
		   					refund += "<li class='detailLi add' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'></i><span class='address'>退款</span></div>"
		   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>+"+term.payMoney/100+"</div></li>";
						}
		   			});
		   			currentPage ++;
					myScroll.refresh();
					totalPage= data.totalPage;
					$(".detailList").html(tem + rent + renew + refund);
					$(".detailList li").click(function(){
						var kid = $(this).attr("kid");
						window.location.href = path+'expenseDetailView?kid='+kid;
					}); 
		   		}else{
		   			layer.closeAll();
	    			layer.open({
	    				content: "请稍后刷新重试"
	    					,skin: 'msg'
	    						,time: 3 //3秒后自动关闭
	    			});
	    			return;
		   		}
			},'json');
		});
	
	function Refresh() {
		setTimeout(function () {
			window.location.reload();
		}, 1000);
	}
	
	function Load() {
		if(currentPage < totalPage){
			setTimeout(function () {
				el =document.querySelector("#wrapper ul");
				var choose = $("#choose").val();
				if (choose == '' || typeof choose == 'undefined') {
					choose='all';
				}			
				$.post(path+'expenseDetail',{'mobile':mobile,'choose':choose,'page':currentPage,'pageSize':9},function(data){
				 	$.ajaxSetup({
						async: false
					});
			 		console.log(data);
			 		var tem='';
			 		var rent='';
			 		var renew='';
			 		var refund='';
			   		if(data.status=='true'){
			   			if ((data.list).length <= 0) {
			   				$("#pullUp").show();
			   				$(".pullUpLabel").text("暂无该分类数据...")
						}else if ((data.list).length < 9) {
							$("#pullUp").hide();
						}
			   			$.each(data.list, function(index,term) {
			   				if (term.parktype==1 && term.payStatus != -1) {
			   					tem += "<li class='detailLi reduce' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'>临停</i><span class='address'>"+term.carparkname+"</span></div>"
			   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div></li>";
							}else if(term.parktype==1 && term.payStatus == -1){
			   					tem += "<li class='detailLi reduce' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'>临停</i><span class='address'>"+term.carparkname+"</span></div>"
			   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div><div class='fail'>交易失败</div></li>";
							}
			   				if (term.parktype != 1 && term.isRenew==0 && term.payStatus != -1) {
			   					rent += "<li class='detailLi add' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'>月租</i><span class='address'>"+term.carparkname+"</span></div>"
			   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div></li>";
							}else if(term.parktype != 1 && term.isRenew==0 && term.payStatus == -1){
			   					rent += "<li class='detailLi add' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'>月租</i><span class='address'>"+term.carparkname+"</span></div>"
			   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div><div class='fail'>交易失败</div></li>";
							}
			   				if (term.parktype != 1 && term.isRenew==1 && term.payStatus != -1) {
			   					renew += "<li class='detailLi add' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'></i><span class='address'>月租续费</span></div>"
			   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div></li>";
							}else if(term.parktype != 1 && term.isRenew==1 && term.payStatus == -1){
			   					renew += "<li class='detailLi add' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'></i><span class='address'>月租续费</span></div>"
			   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div><div class='fail'>交易失败</div></li>";
							}
			   				if (term.payStatus == -1) {
			   					refund += "<li class='detailLi add' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'></i><span class='address'>退款</span></div>"
			   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>+"+term.payMoney/100+"</div></li>";
							}
			   			});
			   			currentPage ++;
						myScroll.refresh();
						$(".detailList").append(tem + rent + renew + refund);
						$(".detailList li").click(function(){
							var kid = $(this).attr("kid");
							window.location.href = path+'expenseDetailView?kid='+kid;
						}); 
			   		}else{
			   			layer.closeAll();
		    			layer.open({
		    				content: "请稍后刷新重试"
		    					,skin: 'msg'
		    						,time: 3 //3秒后自动关闭
		    			});
		    			return;
			   		}
				},'json');
			}, 1000);	
		}else{
			$(".pullUpLabel").text("没有更多内容了...")
		}
	}	
	
	$(".classifyList li").click(function(){
		currentPage=1;
		var choose = $(this).attr("chooseType");
		$("#choose").val(choose);
		if (choose=='tem') {
			$(".classifyTitle span").text('停车缴费');
		}else if (choose=='rent') {
			$(".classifyTitle span").text('月租办理');
		}else if (choose=='renew') {
			$(".classifyTitle span").text('月租续费');
		}else{
			$(".classifyTitle span").text('全部分类');
		}
	 	$.post(path+'expenseDetail',{'mobile':mobile,'choose':choose,'page':currentPage,'pageSize':9},function(data){
	 		console.log(data);
	 		var tem='';
	 		var rent='';
	 		var renew='';
	 		var refund='';
	   		if(data.status=='true'){
	   			if ((data.list).length <= 0) {
	   				$("#pullUp").show();
	   				$(".pullUpLabel").text("暂无该分类数据...")
				}else if ((data.list).length < 9) {
					$("#pullUp").hide();
				}
	   			$.each(data.list, function(index,term) {
	   				if (term.parktype==1 && term.payStatus != -1) {
	   					tem += "<li class='detailLi reduce' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'>临停</i><span class='address'>"+term.carparkname+"</span></div>"
	   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div></li>";
					}else if(term.parktype==1 && term.payStatus == -1){
	   					tem += "<li class='detailLi reduce' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'>临停</i><span class='address'>"+term.carparkname+"</span></div>"
	   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div><div class='fail'>交易失败</div></li>";
					}
	   				if (term.parktype != 1 && term.isRenew==0 && term.payStatus != -1) {
	   					rent += "<li class='detailLi add' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'>月租</i><span class='address'>"+term.carparkname+"</span></div>"
	   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div></li>";
					}else if(term.parktype != 1 && term.isRenew==0 && term.payStatus == -1){
	   					rent += "<li class='detailLi add' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'>月租</i><span class='address'>"+term.carparkname+"</span></div>"
	   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div><div class='fail'>交易失败</div></li>";
					}
	   				if (term.parktype != 1 && term.isRenew==1 && term.payStatus != -1) {
	   					renew += "<li class='detailLi add' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'></i><span class='address'>月租续费</span></div>"
	   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div></li>";
					}else if(term.parktype != 1 && term.isRenew==1 && term.payStatus == -1){
	   					renew += "<li class='detailLi add' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'></i><span class='address'>月租续费</span></div>"
	   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>-"+term.payMoney/100+"</div><div class='fail'>交易失败</div></li>";
					}
	   				if (term.payStatus == -1) {
	   					refund += "<li class='detailLi add' kid="+term.kid+"><div class='left'><div class='leftTop'><i class='class'></i><span class='address'>退款</span></div>"
	   						+"<div class='date'>"+term.payTime+"</div></div><div class='right'>+"+term.payMoney/100+"</div></li>";
					}
	   			});
				$(".detailList").html(tem + rent + renew + refund);
				$(".detailList li").click(function(){
					var kid = $(this).attr("kid");
					window.location.href = path+'expenseDetailView?kid='+kid;
				}); 
	   		}else{
	   			layer.closeAll();
    			layer.open({
    				content: "请稍后刷新重试"
    					,skin: 'msg'
    						,time: 3 //3秒后自动关闭
    			});
    			return;
	   		}
   		},'json');
	});
</script>
</html>