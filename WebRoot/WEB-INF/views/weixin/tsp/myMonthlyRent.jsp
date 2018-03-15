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
		<link rel="stylesheet" href="<%=basePath%>tsp/page/css/common.css">
		<link rel="stylesheet" href="<%=basePath%>tsp/page/css/supply-style.css">
		<title>我的月租</title>
	</head>
	<style>
	.renewBtn{
		border: 1px solid #3BC0F3;
	    color: #3BC0F3;
	    background-color: #ffffff;
	    width: 1rem;
	    height: .3rem;
	    border-radius: 3px;
    }
	</style>
	<body>
		<script src="<%=basePath %>/tsp/js/jquery.min.js"></script>
		<div class="wrap">
		<ul class="title">
			<li class="title-item active">当前月租</li>
			<li class="title-item">历史月租</li>
		</ul>
		<ul class="content">
			<li class="content-item content-active">
				<c:forEach items="${handleMonthly}" var="hm">
					<c:if test="${!hm.isEnd}">
						<ul class="parking-infor posit-RE">
							<li class="time" style="font-size:.2rem">${hm.carPlate}</li>
							<li class="parking-lot">停车场：${hm.carpark.name}</li>
							<li class="week">到期时间：${hm.formatEndTime}</li>
							<li class="admission-type">月租类型：<p>${hm.ruleName}</p></li>
							<li class="state"><button class="renewBtn" onclick="renew(${hm.kid},${hm.ruleType},${hm.carpark.carparkid},'${hm.carPlate}',${hm.ruleId},'${hm.ruleName}',${hm.payFee},'${hm.endTime}')" >续费</button></li>
						</ul>
					</c:if>
				</c:forEach>
			</li>
			<li class="content-item">
				<c:forEach items="${handleMonthly}" var="hm">
					<c:if test="${hm.isEnd}">
						<ul class="parking-infor posit-RE">
							<li class="time" style="font-size:.2rem">${hm.carPlate}</li>
							<li class="parking-lot">停车场：${hm.carpark.name}</li>
							<li class="week">到期时间：${hm.formatEndTime}</li>
							<li class="admission-type">月租类型：<p>${hm.ruleName}</p></li>
							<li class="state"><button class="renewBtn" onclick="renew(${hm.kid},${hm.ruleType},${hm.carpark.carparkid},'${hm.carPlate}',${hm.ruleId},'${hm.ruleName}',${hm.payFee},'${hm.endTime}')">续费</button></li>
						</ul>
					</c:if>
				</c:forEach>
			</li>
		</ul>
	</div>
	</body>
	<script type="text/javascript">
	var path = '<%=basePath%>';
	$(".title-item").click(function(){
		$(".title-item").removeClass("active");
		$(this).addClass("active");
		var index=$(this).index();
		$(".content-item").removeClass("content-active");
		$(".content-item").eq(index).addClass("content-active");
	})
	function renew(kid,ruleType,carparkid,plateNo,ruleId,ruleName,payPee,endTime){
		window.location.href=path+"monthlyRenew?kid="+kid+"&ruleType="+ruleType+"&carparkid="+carparkid+"&plateNo="+plateNo+
		"&ruleId="+ruleId+"&ruleName="+ruleName+"&payPee="+payPee+"&endTime="+endTime;
	}
	</script>
</html>

