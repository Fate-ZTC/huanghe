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
		<title>信息认证</title>
		<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta name="msapplication-tap-highlight" content="no" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>tsp/css/common.css"/>
		<link rel="stylesheet" type="text/css" href="<%=basePath%>tsp/css/driving.css"/>
		<script src="<%=basePath%>tsp/js/jquery.min.js"></script>
		<script src="<%=basePath%>layer_mobile/layer.js"></script>
	</head>
	<body>
		<div class="jiucheng-box informationAuthentication-box f14">
			<div class="driver-license-box">
				<div class="driver-license-upload">
					<p class="license-title">驾驶证</p>
					<c:if test="${user.driverUrl==null || user.driverUrl==''}">
						<a href="<%=basePath%>addDriver?mobile=${user.mobile}" class="license-a"><p class="driverLicense">添加驾驶证</p></a>
					</c:if>
					<c:if test="${user.driverUrl!=null && user.driverUrl!=''}">
						<div class="read-picture-box show">
							<img src="<%=basePath%>${user.driverUrl}" class="read-picture" alt="" id="ImgPr" />
						</div>
					</c:if>
				</div>
				<c:if test="${user.authStatus==0}">
					<p class="auditStatus inAudit pd-lr14 show">审核中...</p>
				</c:if>
				<c:if test="${user.authStatus==1}">
					<div class="auditStatus auditAdopt pd-lr14 show"><span class="float-L">审核通过</span><a class="modifyBtn float-R" href="<%=basePath%>addDriver?mobile=${user.mobile}">修改</a></div>
				</c:if>
				<c:if test="${user.authStatus==-1}">
					<div class="auditStatus notPass show">
						<p class="ovell pd-lr14"><span class="float-L">审核未通过</span><a class="modifyBtn float-R" href="<%=basePath%>addDriver?mobile=${user.mobile}">修改</a></p>
						<p class="notPassReason">未通过原因：${user.authReason}</p>
					</div>
				</c:if>
			</div>
			<div class="driving-license-box">
				<p class="license-title pd-lr14">行驶证及相关信息</p>
				<c:forEach items="${carList}" var="c">
					<div class="driving-license">
						<div class="driving-license-ipload pd-lr14">
							<h2 class="upload-car-num f16">${c.carPlate}</h2>
							<c:if test="${c.drivingUrl==null || c.drivingUrl==''}">
								<a href="<%=basePath%>addDriving?kid=${c.kid}&mobile=${user.mobile}" class="license-a"><p class="driverLicense">添加行驶证</p></a>
							</c:if>
							<c:if test="${c.drivingUrl!=null && c.drivingUrl!=''}">
								<div class="read-picture-box show">
									<img src="<%=basePath%>${c.drivingUrl}" class="read-picture" alt="" id="ImgPr" />
								</div>
							</c:if>
						</div>
						<c:if test="${c.authStatus==0}">
							<p class="auditStatus inAudit pd-lr14 show">审核中...</p>
						</c:if>
						<c:if test="${c.authStatus==1}">
							<div class="auditStatus auditAdopt pd-lr14 show"><span class="float-L">审核通过</span><a class="modifyBtn float-R" href="<%=basePath%>addDriving?kid=${c.kid}&mobile=${user.mobile}">修改</a></div>
						</c:if>
						<c:if test="${c.authStatus==-1}">
							<div class="auditStatus notPass show">
								<p class="ovell pd-lr14"><span class="float-L">审核未通过</span><a class="modifyBtn float-R" href="<%=basePath%>addDriving?kid=${c.kid}&mobile=${user.mobile}">修改</a></p>
								<p class="notPassReason">未通过原因：${c.authReason}</p>
							</div>
						</c:if>
					</div>
				</c:forEach>
				<c:if test="${carNum==0}">
					<div class="driving-license">
						<div class="driving-license-ipload pd-lr14">
							<h2 class="upload-car-num f16">${c.carPlate}</h2>
							<a href="<%=basePath%>addDriving?&mobile=${user.mobile}" class="license-a"><p class="driverLicense">添加行驶证</p></a>			
						</div>
					</div>
				</c:if>
				<c:if test="${carNum>0 && carNum<3}">
					<p class="driverLicense-upload f12 show">最多可添加三个行驶证</p>
					<a href="<%=basePath%>addDriving?&mobile=${user.mobile}"><button class="continueAddBtn">继续添加</button></a>
				</c:if>
			</div>
			<p class="picture-reamind text-CENT f12">为避免重复提交，请上传清晰图片</p>
		</div>
	</body>
</html>

