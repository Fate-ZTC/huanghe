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
		<link rel="stylesheet" href="<%=basePath %>hik/css/pullToRefresh.css" />
		<link rel="stylesheet" href="<%=basePath %>hik/css/style.css" />
	</head>
	<body>
		<div class="wrap" id="wrapper">
			<ul class="wrap1">
				<c:forEach items="${hikParking}" var="p">
<!--					<li class="depot-show-list radius-4">-->
<!--						<div>-->
<!--							<p class="list-left">-->
<!--								<span class="depot-name over"><i class="green-circle"></i>${p.name}</span>-->
<!--								<span class="depot-info">${p.feeRates}</span>-->
<!--							</p>-->
<!--							<p class="list-right">-->
<!--								<span class="depot-num">${p.enableBerth}个</span>-->
<!--								<span class="surplus-depot">剩余车位</span>-->
<!--							</p>-->
<!--						</div>-->
<!--						<a href="<%=basePath%>wxGoParking_to?longitude=${p.longitude}&latitude=${p.latitude}" class="depot-btn">去这儿</a>-->
<!--					</li>-->
					
					<li class="depot-show-list radius-4">
					<div>
						<p class="list-left">
							<span class="depot-name over"><i class="green-circle"></i>${p.name}</span>
							<span class="depot-info">${p.feeRates}</span>
						</p>
						<p class="list-right">
							<span class="depot-num">${p.enableBerth}个</span>
							<span class="surplus-depot">剩余车位</span>
						</p>
					</div>
					<p class="depot-address">
						<i class="address-pic"></i>
						<span>${p.province}${p.city}${p.county}${address}</span>
					</p>
					<a href="<%=basePath%>wxGoParking_to?longitude=${p.longitude}&latitude=${p.latitude}" class="depot-btn">去这儿</a>
				</li>
				</c:forEach>
			</ul>
		</div>
	</body>
	<script type="text/javascript" src="<%=basePath %>hik/js/jquery-1.9.1.min.js" ></script>
	<script src="<%=basePath %>hik/js/fastclick.js"></script>
	<script type="text/javascript" src="<%=basePath %>hik/js/iscroll.js" ></script>
	<script type="text/javascript" src="<%=basePath %>hik/js/pullToRefresh.js" ></script>
	<script type="text/javascript" src="<%=basePath %>hik/js/base.js" ></script>
	<script type="text/javascript" src="<%=basePath %>hik/js/index.js" ></script>
</html>
