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
		<title>停车记录</title>
		<link rel="stylesheet" href="<%=basePath %>tsp/css/common.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath %>swiper/swiper-3.4.2.min.css"/>
		<link rel="stylesheet" type="text/css" href="<%=basePath %>tsp/css/jiucheng-common.css"/>
		<link rel="stylesheet" href="<%=basePath %>tsp/css/jiucheng.css">
		<script src="<%=basePath %>tsp/js/jquery.min.js"></script>
		<script src="<%=basePath %>/layer_mobile/layer.js"></script>
	    <link rel="stylesheet" href="<%=basePath%>tsp/page/css/newAdd.css">
	    <link rel="stylesheet" type="text/css" href="<%=basePath %>/layer3.1.1/mobile/need/layer.css" />
		<script type="text/javascript" src="<%=basePath %>/layer3.1.1/layer.js"></script>
		<script src="<%=basePath%>tsp/page/js/iscroll.js"></script>
		<script src="<%=basePath%>tsp/page/js/pullToRefresh.js"></script>
		<script src="<%=basePath%>tsp/page/js/newAdd.js"></script>
		<style>
		body{background-color: #F2F2F2;}
		.box{background-color: #F2F2F2;}
		.swiper-container {
		    margin-top: .1rem;
		    margin-bottom: .1rem;
		    border-radius: 5px;
		    width: 3.1rem;
		    height: 1.5rem;
		}
		.car-status img {
		    width: 0.35rem;
		    margin: 0.18rem 0.15rem 0.17rem 0.1rem;
		    float: left;
		}
		.date-right {
		    float: right;
		    font-size: .1rem;
		    color: #999999;
		    height: .1rem;
		    line-height: .1rem;
		}
		</style>
	</head>
	<body>
		<div id="wrapper">
			<ul class="detailList"></ul>
		</div>
<!-- 		<div class="myTerritory-box box posit-RE f14"> -->
<%-- 			<a class="car-status" href="javascript:void(0);"><img src="<%=basePath %>tsp/img/car1.png" alt="" /></a> --%>
<!-- 		</div> -->
		<input type="hidden" id="mobile" value="${mobile }">
		<script src="<%=basePath %>swiper/swiper-3.4.2.jquery.min.js"></script>
		<script>
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
		$.post(path+'parkingRecord',{'mobile':mobile,'page':currentPage,'pageSize':9},function(data){
		 	$.ajaxSetup({
				async: false
			});
	 		console.log(data);
// 	 		var a='<a class="car-status" href="<%=basePath %>"></a>';
	 		var a ='';
	 		var renew='';
	 		var refund='';
		   		if(data.status=='true'){
		   			$.each(data.list, function(index,term) {
				 		a += "<li class='detailLi reduce' kid="+term.kid+"><div class='left'><div class='leftTop'><span class='address'>"+term.carparkname+"</span></div>"
					   		+"<div class='date'>进："+term.inTime+"</div></div><div class='date-right'>出："+term.outTime+"</div></li>";
		   			});
		   			currentPage ++;
					myScroll.refresh();
					totalPage= data.totalPage;
					$(".detailList").html(a);
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
				$.post(path+'parkingRecord',{'mobile':mobile,'page':currentPage,'pageSize':9},function(data){
				 	$.ajaxSetup({
						async: false
					});
			 		console.log(data);
			 		var a='';
			 		var rent='';
			 		var renew='';
			 		var refund='';
			   		if(data.status=='true'){
			   			$.each(data.list, function(index,term) {
					 		a += "<li class='detailLi reduce' kid="+term.kid+"><div class='left'><div class='leftTop'><span class='address'>"+term.carparkname+"</span></div>"
						   		+"</div><div class='date'>进："+term.inTime+"</div><div class='date-right'>出："+term.outTime+"</div></li>";
				   		});
			   			currentPage ++;
						myScroll.refresh();
						totalPage= data.totalPage;
						$(".detailList").html(a);
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
	  </script>
	</body>
</html>
