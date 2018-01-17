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
		<title>我的地盘</title>
		<link rel="stylesheet" href="<%=basePath %>tsp/css/common.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath %>swiper/swiper-3.4.2.min.css"/>
		<link rel="stylesheet" type="text/css" href="<%=basePath %>tsp/css/jiucheng-common.css"/>
		<link rel="stylesheet" href="<%=basePath %>tsp/css/jiucheng.css">
		<script src="<%=basePath %>tsp/js/jquery.min.js"></script>
		<script src="<%=basePath %>/layer_mobile/layer.js"></script>
	</head>
	<body>
		<div class="myTerritory-box box posit-RE f14">
			<!--swiper-->
			<div class="swiper-container">
			  <div class="swiper-wrapper">
			    <div class="swiper-slide blue-slide"><img class="swiper-img" src="<%=basePath %>tsp/img/banner.png" alt="" /></div>
			  </div>
			  
			  <!-- 如果需要分页器 -->
    			<div class="swiper-pagination"></div>
			</div>
			
			<ul class="function">
				<a class="function-btn aBackground" href="<%=basePath %>tsp/monthlyRent.html">
					<img src="<%=basePath %>tsp/img/yzbl.png" alt="" />
					<p>月租办理</p>
				</a>
				<a class="function-btn aBackground" href="javascript:void(0);" onclick="checkSub('monthlyRenewList')">
					<img src="<%=basePath %>tsp/img/xufei.png" alt="" />
					<p>月租续费</p>
				</a>
				<a class="function-btn aBackground" href="javascript:void(0);" onclick="tips()">
					<img src="<%=basePath %>tsp/img/yy.png" alt="" />
					<p>我的预约</p>
				</a>
				<a class="function-btn aBackground" href="javascript:void(0);" onclick="tips()">
					<img src="<%=basePath %>tsp/img/lsyy.png" alt="" />
					<p>历史预约</p>
				</a>
				<a class="function-btn aBackground" href="<%=basePath %>wxCost_qrCode">
					<img src="<%=basePath %>tsp/img/tcjf.png" alt="" />
					<p>停车缴费</p>
				</a>
				<a class="function-btn aBackground" href="<%=basePath %>wxParkingList_list">
					<img src="<%=basePath %>tsp/img/kxcc.png" alt="" />
					<p>空闲车场</p>
				</a>
				<a class="function-btn aBackground" href="<%=basePath %>wxCarManage_toBind">
					<img src="<%=basePath %>tsp/img/clbd.png" alt="" />
					<p>车辆绑定</p>
				</a>
				<a class="function-btn aBackground" href="<%=basePath %>authList">
					<img src="<%=basePath %>tsp/img/clrz.png" alt=""/>
					<p>车辆认证</p>
				</a>
			</ul>
			<a class="person-link" href="javascript:void(0);" onclick="tips()"><img src="<%=basePath %>tsp/img/grzx.png" alt="" />个人中心</a>
		</div>
		<script src="<%=basePath %>swiper/swiper-3.4.2.jquery.min.js"></script>
		<script>
			var path = '<%=basePath %>';
			var mobile = '${user.mobile}';
			var obj = document.getElementsByClassName('aBackground');
			for (var i = 0; i < obj.length; i++) {
				obj[i].addEventListener('touchstart', function(event) {
					$(this).addClass("function-btnAct");
				}, false); 
				obj[i].addEventListener('touchend', function(event) {
					$(this).removeClass("function-btnAct");
				}, false);
			}
		
			var mySwiper = new Swiper('.swiper-container',{
				mode: 'vertical',
			    loop: true,
				autoplay: 3000,
				pagination: '.swiper-pagination',
				speed: 1000
		  	});  
			
			function tips(){
				window.alert('敬请期待...');
			}
			
			function checkSub(url){
				 layer.open({
				    type: 2,
				    content: '加载中',
				    shadeClose: false,
				  });
				$.post(path+'wxUsersManage_checkDriver',{'mobile':mobile},function(data){
					if(data.status=='true'){
						window.location.href=path+url;
					}else{
						layer.closeAll();
						layer.open({
						    content: '需上传驾驶证才能使用该功能'
						    ,skin: 'msg'
						    ,time: 2 //2秒后自动关闭
						 });
					}
				},'json')
			}
	  </script>
	</body>
</html>
