<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
Date date = new Date();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>部门-添加</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=path %>/page/css/driving.css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<link rel="stylesheet" href="<%=path %>/page/validator/jquery.validator.css" />
<script type="text/javascript" src="<%=path %>/page/validator/jquery.validator.js"></script>
<script type="text/javascript" src="<%=path %>/page/validator/local/zh-CN.js"></script>
<script type="text/javascript" src="<%=path %>/page/laydate/laydate.js"></script>

<script type="text/javascript">
$(function(){
   	//选择框
   	laydate.skin('molv');
 });
</script>
</head>

<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">应用管理</a></li>
    <li><a href="<%=path %>/userAuthManager_list">信息认证审核</a></li>
    <li><a href="#">驾驶证审核</a></li>
    </ul>
    </div>
    
	<div class="box signBG-box">
		<form action="<%=path %>/userAuthManager_auth" method="post">
		<input type="hidden" name="mobile" value="${users.mobile}"/>
		<input type="hidden" name="method" value="edit" />
		<div class="signBgTabbox">
			<ul class="signBgTabList">
				<a href="#" class="tabNow">驾驶证审核</a>
				<a href="<%=path %>/userAuthManager_drivingAuth?id=${users.mobile}">行驶证审核</a>
			</ul>
		</div>

		<!-- 表 -->
		<div class="signFun-box">
			<div id="usual1" class="usual">
				<li class="picture-li">
					<label for="" class="title-label">驾驶证</label>
					<div class="picture-box">
						<img class="driving-picture" src="<%=path %>/${users.driverUrl}"/>
						<img 
						<c:if test="${users.authStatus==null}">src=""</c:if>
						<c:if test="${users.authStatus==0}">src="<%=path %>/page/images/dengdai.png"</c:if>
						<c:if test="${users.authStatus==1}">src="<%=path %>/page/images/pass.png"</c:if>
						<c:if test="${users.authStatus==-1}">src="<%=path %>/page/images/noPass.png"</c:if> 
						class="statusLogo" alt="" />
					</div>
				</li>
				<c:if test="${users.authStatus==0}">
				<li class="radio-li"><input type="radio" class="passInput pass" name="authStatus" value="1" id="pass" checked="checked"/>通过<input type="radio" class="passInput noPass" name="authStatus" id="noPass" value="-1" />不通过</li>
				</c:if>
			</div>
			<c:if test="${users.authStatus==-1}">
			<li class="textarea-li" style="display: block;">
				<label for="" class="title-label">原因</label>
				<textarea name="" rows="" cols="" readonly="readonly">${users.authReason}</textarea>
			</li>
			</c:if>
			<c:if test="${users.authStatus==0}">
			<li class="textarea-li fill-textarea">
				<label for="" class="title-label">原因</label>
				<textarea name="authReason" rows="" cols=""></textarea>
			</li>
			</c:if>
		</div>
		<c:if test="${users.authStatus==0}">
		<li class="submit-li"><button class="submit-btn">提交</button></li>
		</c:if>
		</form>
	</div>
	<script>
		$(".passInput").click(function(){
			if ($(this).hasClass("pass")) {
				$(".textarea-li").hide();
			} else{
				$(".textarea-li").show();
			}
		})
	</script>

</body>

</html>
