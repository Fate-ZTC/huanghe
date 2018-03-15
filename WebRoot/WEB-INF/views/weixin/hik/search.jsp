<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta name="msapplication-tap-highlight" content="no" />
		<title>酒城停车</title>
		<link rel="stylesheet" href="<%=basePath %>hik/css/common.css" />
		<link rel="stylesheet" href="<%=basePath %>hik/css/style.css" />
	</head>
	<body>
		<!--<div class="wrap">-->
			<div class="search-box">
				<div id="search-form" class="search-form radius-4">
					<input id="search-text" type="text" name="search" required placeholder="输入举例：川EN6N07" />
					<i id="clear-txt" class="search-close clear"></i>
					<span id="search-btn" class="search-btn"></span>
				</div>
			</div>
			<div class="search-reminder">
				<img src="<%=basePath %>hik/img/remind.png" />
				<p class="reminder-title">温馨提示：</p>
				<p>请确保您已进入停车场后再查询！</p>
			</div>
			<!--车牌查询结果-->
			<ul class="search-result">
				<!--<li>
					<span>川E•N6N01</span>
					<span>白招牌停车场</span>
				</li>-->
			</ul>
			<!--查询失败-->
			<div class="search-fail">
				<img class="search-fail-pic" src="<%=basePath %>hik/img/fail.png" />
				<p class="fail-title">查询失败</p>
				<p class="fail-reminder margin-one">请检查您的车牌号输入是否完整，</p>
				<p class="fail-reminder"> 或请确保您的车辆已进入停车场！</p>
			</div>
			<!--查询完成-->
			<div class="search-success">
				
			</div>
<!--		</div>-->
		<div class="test-hint">
			<img src="<%=basePath %>hik/img/white.png" />
			<p class="test-hint-word"></p>
		</div>
		<input type="hidden" id="path" value="<%=basePath %>">
	</body>
	<script src="<%=basePath %>hik/js/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>hik/js/fastclick.js" ></script>
	<script type="text/javascript" src="<%=basePath %>hik/js/base.js" ></script>
	<script src="<%=basePath %>hik/js/search.js"></script>
</html>
