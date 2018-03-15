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
		<title>月租办理——停车场</title>
		<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta name="msapplication-tap-highlight" content="no" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>tsp/css/common.css"/>
		<link rel="stylesheet" type="text/css" href="<%=basePath%>tsp/css/monthly.css"/>
		<script src="<%=basePath%>tsp/js/jquery.min.js"></script>
		<script src="<%=basePath%>layer_mobile/layer.js"></script>
	</head>
	<style>
	.operation{
		width: 0.6rem;
	    height: 0.22rem;
	    line-height: 0.19rem;
	    color: #b3b3b3;
	    text-align: center;
	    border-radius: 0.03rem;
	    font-size: 0.12rem;
	    margin-top: 0.13rem;
		}
	</style>
	<body>
		<div class="jiucheng-box monthlyRent-box f14">
			<div class="monthlyRentTitReamind"><span class="float-L">目前仅支持个人办理月租业务</span><div class="batchOperation f14 float-R">批量操作</div></div>
			<ul class="parkingList">
			<c:forEach items="${carparkList}" var="c">
				<li>
					<div class="parkingLogo"></div>
					<span>${c.name}</span><span>（剩余${c.totalMonRentNum-c.nowMonRentNum}个）</span>
					<c:if test="${(c.totalMonRentNum-c.nowMonRentNum)>0}">
						<a class="operationBtn float-R" href="<%=basePath%>monthlyHandle?carparkid=${c.carparkid}&name=${c.name}&plateNo=${plateNo}&kid=${kid}">办理</a>
					</c:if>
					<c:if test="${(c.totalMonRentNum-c.nowMonRentNum)<=0}">
						<span class="operation float-R">无车位</span>
					</c:if>
				</li>
			</c:forEach>
			</ul>
		</div>
	</body>
</html>

