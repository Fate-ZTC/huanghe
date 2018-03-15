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
		<script src="<%=basePath%>tsp/js/jquery.min.js"></script>
		<script src="<%=basePath%>layer_mobile/layer.js"></script>
	</head>
	<body>
		<div class="jiucheng-box monthlyRenewList-box f14">
			<!--<div class="monthlyRentTitReamind"><span class="float-L">目前仅支持个人办理月租业务</span><div class="batchOperation f14 float-R">批量操作</div></div>-->
			<ul class="renewList">
				<c:forEach items="${qvMonthly}" var="q">
					<li class="renew-li">
						<p class="renewCarNum f16">${q.carPlate}</p>
						<p class="renewMesg"><span>停车场：</span><span>${q.carparkName}</span></p>
						<p class="renewMesg"><span>到期时间：</span><span>${q.endTime}</span></p>
						<p class="renewMesg"><span>月租类型：</span><span>${q.ruleName}</span></p>
						<div class="renewNowStatus-box"><span class="float-L">月租状态：</span>
						<c:if test="${c.cardStatus==0}">
							<span class="renewNowStatus overdue float-L">过期</span>
						</c:if>
						<c:if test="${c.cardStatus==1}">
							<span class="renewNowStatus effective float-L">有效</span>
						</c:if>
						<a class="operationBtn float-R" href="<%=basePath%>monthlyRenew?carparkid=${q.carparkid}&plateNo=${q.carPlate}&ruleId=${q.ruleId}&ruleName=${q.ruleName}&payPee=${q.payPee}&endTime=${q.endTime}&ruleType=${q.ruleType}&kid=${q.kid}">续期</a></div>
					</li>
				</c:forEach>
				<c:forEach items="${carList}" var="c">
					<li class="rent-li">
						<div class="rentNowStatus-box">
							<span class="float-L rentCarNum">${c.carPlate}</span>
					     	<c:if test="${c.authStatus==1}">
							<a class="operationBtn float-R" href="<%=basePath%>monthlyHandle?plateNo=${c.carPlate}&kid=${c.kid}">月租办理</a>
							</c:if>
						</div>
					</li>
				</c:forEach>
				<c:if test="${carNum<3}">
				<div class="toAddCar-box">
					<a class="toAddCar" href="<%=basePath%>addDriving?&mobile=${user.mobile}">
						<p class="addCar f16">新增车辆</p>
						<p class="addCarReamind f12">您还可以添加<i>1</i>个车牌，点击添加</p>
					</a>
				</div>
				</c:if>
			</ul>
		</div>
	</body>
</html>

