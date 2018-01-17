<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<title>酒城停车</title>
	<link rel="stylesheet" href="<%=basePath %>tsp/css/common.css">
	<link rel="stylesheet" href="<%=basePath %>tsp/css/jiucheng.css">
</head>
<body>
	<div class="box posit-RE" style="padding-top:.2rem;">
		<div class="pay-success">
			<p class="pay-success-title posit-RE"><span class="success-logo float-L"><img src="<%=basePath %>tsp/img/zfcg.png" alt=""></span><span class="f18" style="line-height:0.54rem;">支付成功</span></p>
			<p class="car list success-p"><span class="car-title success-place">车牌号：</span><span class="car-num">${vcw.carPlate }</span></p>
			<p class="success-place list success-p"><span class="success-place-title">所在车场：</span><span class="success-place-val">${parkName }</span></p>
			<p class="success-time list success-p"><span class="success-time-title">入场时间：</span><span class="success-time-val">${vcw.formatInTime() }</span></p>
			<p class="Time list success-p"><span class="Time-title">停车时长：</span><span class="Time-val">${vcw.formatParkTime() }</span></p>
			<p class="money list success-p"><span class="money-title">本次支付：</span><span class="money-val">${vcw.formateShouldPayMoney() }</span><span>元</span></p>
		</div>
		<p class="success-tishi f12">温馨提示：支付成功后请在${vcw.leaveOut }分钟内离开车场，超时未离场将重新开始计费！</p>
	</div>
	<script type="text/javascript">
		history.pushState(null, null, document.URL);
        window.addEventListener('popstate', function () {
            history.pushState(null, null, document.URL);
        });
	</script>
</body>
</html>