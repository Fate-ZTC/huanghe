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
	<div class="box posit-RE pay-failure-box">
		<div class="failure-img">
			<img src="<%=basePath %>tsp/img/failure.png" alt="">
		</div>
		<div class="failure-remind">
			<h4>退款提示：</h4>
			<p>如无异常，退款会于2-5个工作日内执行完成，商家退款完成后，原路退回到用户支付账户。零钱即时到账；银行卡（储蓄卡及信用卡）1-3个工作日。</p>
		</div>
	</div>
	<script type="text/javascript">
		history.pushState(null, null, document.URL);
	       window.addEventListener('popstate', function () {
	            history.pushState(null, null, document.URL);
	    });
	</script>
</body>
</html>