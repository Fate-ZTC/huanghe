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
    <li><a href="#">系统管理</a></li>
    <li><a href="<%=path %>/userAuthManager_list">信息认证审核</a></li>
    <li><a href="#">行驶证审核</a></li>
    </ul>
    </div>
    
    <div class="box signBG-box">
		<!-- 签到、人员统计切换 -->
		<div class="signBgTabbox">
			<ul class="signBgTabList">
				<a href="<%=path %>/userAuthManager_auth?id=${id}">驾驶证审核</a>
				<a href="#" class="tabNow">行驶证审核</a>
			</ul>
		</div>

		<!-- 表 -->
		<form action="<%=path %>/userAuthManager_drivingAuth" method="post" id="myForm">
		<input type="hidden" name="method" value="edit"/> 
		<input type="hidden" name="authStatus" value="" id="authStatus"/> 
		<c:forEach items="${usersCars}" var="c" varStatus="L">
		<div class="signFun-box driving-box">
			<div id="usual1" class="usual">
				<li class="picture-li">
					<label for="" class="title-label">行驶证一</label>
					<div class="picture-box">
						<img class="driving-picture" src="<%=path %>/${c.drivingUrl}"/>
						<img 
						<c:if test="${c.authStatus==null}">src=""</c:if>
						<c:if test="${c.authStatus==0}">src="<%=path %>/page/images/dengdai.png"</c:if>
						<c:if test="${c.authStatus==1}">src="<%=path %>/page/images/pass.png"</c:if>
						<c:if test="${c.authStatus==-1}">src="<%=path %>/page/images/noPass.png"</c:if> 
						class="statusLogo" alt="" />
					</div>
				</li>
				<c:if test="${c.authStatus==0}">
				<input type="hidden" name="kid" value="${c.kid}"/>
				<li class="radio-li"><input type="radio" class="passInput pass" name="authStauts${L.index+1}" id="pass" checked="checked" value="1"/>通过<input type="radio" class="passInput noPass" name="authStauts${L.index+1}" id="noPass" value="-1" />不通过</li>
				</c:if> 
			</div>
			<div class="driving-mesg-box">
				<table id="drivingMesgTable" border="" cellspacing="0" cellpadding="0">
					<tr><td class="tableTitle">车牌号</td><td>${c.carPlate}</td></tr>
					<tr><td class="tableTitle">车辆类型</td><td>
						<c:if test="${c.vehicleType==1}">小型车</c:if>
						<c:if test="${c.vehicleType==2}">大型车</c:if>
						<c:if test="${c.vehicleType==0}">其他</c:if>
					</td></tr>
					<tr><td class="tableTitle">车牌颜色</td><td>
						<c:if test="${c.plateColor==1}">蓝色</c:if>
						<c:if test="${c.plateColor==2}">黄色</c:if>
						<c:if test="${c.plateColor==3}">黑色</c:if>
						<c:if test="${c.plateColor==4}">白色</c:if>
						<c:if test="${c.plateColor==0}">其他</c:if>
					</td></tr>
					<tr><td class="tableTitle">车主姓名</td><td>${c.vehicleHost}</td></tr>
					<tr><td class="tableTitle">手机号码</td><td>${c.contactNum}</td></tr>
					<tr><td class="tableTitle">使用人姓名</td><td>${c.userHost}</td></tr>
				</table>
			</div>
			<c:if test="${c.authStatus==-1}">
			<li class="textarea-li" style="display: block;">
				<label for="" class="title-label">原因</label>
				<textarea name="" rows="" cols="" readonly="readonly">${c.authReason}</textarea>
			</li>
			</c:if>
			<c:if test="${c.authStatus==0}">
			<li class="textarea-li">
				<label for="" class="title-label">原因</label>
				<textarea name="authReason" rows="" cols=""></textarea>
			</li>
			</c:if>
		</div>
		</c:forEach>
		<li class="submit-li"><button class="submit-btn" type="button" onclick="sub()">提交</button></li>
		</form>
	<script>
		$(".passInput").click(function(){
			var textareaBox=$(this).parents(".driving-box").find(".textarea-li");
			if ($(this).hasClass("pass")) {
				$(textareaBox).hide();
			} else{
				$(textareaBox).show();
			}
		})
		
		function sub(){
			var radioEl = "";
			$('input[type="radio"]:checked').each(function(){
				radioEl+=$(this).val()+',';
			});
			$('#authStatus').val(radioEl);
			$('#myForm').submit();
		}
	</script>

</body>

</html>
