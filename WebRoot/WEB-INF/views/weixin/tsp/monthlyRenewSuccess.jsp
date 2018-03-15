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
		<title>月租续费</title>
		<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta name="msapplication-tap-highlight" content="no" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>tsp/css/common.css"/>
		<link rel="stylesheet" type="text/css" href="<%=basePath%>tsp/css/monthly.css"/>
		<link rel="stylesheet" type="text/css" href="<%=basePath%>tsp/css/select-bottom.css"/>
		<script src="<%=basePath%>tsp/js/jquery.min.js"></script>
		<script src="<%=basePath%>layer_mobile/layer.js"></script>
	</head>
	<body>
		<div class="jiucheng-box monthlyRenewSuccess-box f14">
			<div class="monthlyRenewSuccess-top">
				<p class="f16">月租续费成功！</p>
			</div>
			<ul class="renewSuccess-mesg">
				<li><span class="float-L">车牌号：</span><span class="float-R">${hmv.carPlate}</span></li>
				<li><span class="float-L">停车场：</span><span class="float-R">${hmv.carpark.name}</span></li>
				<li><span class="float-L">到期时间：</span><span class="float-R">${hmv.formatEndTime}</span></li>
			</ul>
		</div>
		<script type="text/javascript">
		history.pushState(null, null, document.URL);
        window.addEventListener('popstate', function () {
            history.pushState(null, null, document.URL);
        });
		</script>
	</body>
</html>