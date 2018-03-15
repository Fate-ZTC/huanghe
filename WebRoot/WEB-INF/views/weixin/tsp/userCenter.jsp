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
		<title>个人中心</title>
		<link rel="stylesheet" href="<%=basePath %>tsp/css/common.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath %>swiper/swiper-3.4.2.min.css"/>
		<link rel="stylesheet" type="text/css" href="<%=basePath %>tsp/css/jiucheng-common.css"/>
		<link rel="stylesheet" href="<%=basePath %>tsp/css/jiucheng.css">
		<script src="<%=basePath %>tsp/js/jquery.min.js"></script>
		<script src="<%=basePath %>/layer_mobile/layer.js"></script>
		<style>
		body{background-color: #F2F2F2;}
		.box{background-color: #F2F2F2;}
		.center-list {
		    background-size: 0.07rem 0.14rem;
		    background-color: #FFFFFF;
		}
		</style>
	</head>
	<body>
		<div class="myTerritory-box box posit-RE f14">
		<div class="center-head"><img src="<%=basePath %>tsp/img/headcar.png" alt="" /><span style="margin-left: .2rem;">${user.mobile}</span></div>
		<a class="center-list" href="<%=basePath %>expenseList?mobile=${user.mobile}"><img src="<%=basePath %>tsp/img/xfmx.png" alt="" />消费明细</a>
		<a class="center-list" href="<%=basePath %>expenseList?mobile=${user.openid}"><img src="<%=basePath %>tsp/img/wdyz.png" alt="" />我的月租</a>
		<a class="center-list" href="javascript:void(0);" onclick="tips()"><img src="<%=basePath %>tsp/img/wdyy-new.png" alt="" />我的预约</a>
		<a class="center-list" href="javascript:void(0);" onclick="tips()"><img src="<%=basePath %>tsp/img/clrz-new.png" alt="" />车辆认证</a>
		<a class="center-list" href="javascript:void(0);" onclick="tips()"><img src="<%=basePath %>tsp/img/tcjl.png" alt="" />停车记录</a>
		</div>
		<script src="<%=basePath %>swiper/swiper-3.4.2.jquery.min.js"></script>
		<script>
			var path = '<%=basePath %>';
			function tips(){
				window.alert('敬请期待...');
			}
	  </script>
	</body>
</html>
