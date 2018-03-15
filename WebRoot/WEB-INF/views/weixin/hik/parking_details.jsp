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
		<div class="wrap">
			<div class="depot-pic-box">
				<img src="<%=basePath %>hik/img/tcc.jpg" />
				<p>参考收费：0.0元/0小时</p>
			</div>
			<div class="wrap1">
				<table class="tablelist">
					<tr>
						<td>停车时间</td>
						<td>室外</td>
					</tr>
					<tr>
						<td>是否对外</td>
						<td>对外开放停车场</td>
					</tr>
					<tr>
						<td>总车位</td>
						<td>126个</td>
					</tr>
				</table>
				<a href="" class="go-here">去这儿</a>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="<%=basePath %>hik/js/base.js" ></script>
</html>
