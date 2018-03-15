<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>月租办理-列表</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/page/js/common.js"></script>
<script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>
	<script src="<%=path %>/page/chart/laydate/laydate.js"></script>
<script type="text/javascript">
	var method = '${method }';
	if(method=='addSuccess'){
		layer.msg('添加成功', {icon: 1});
	}else if(method=='editSuccess'){
		layer.msg('编辑成功', {icon: 1});
	}else if(method=='deleteSuccess'){
		layer.msg('删除成功', {icon: 1});
	}else if(method=='drivingAuthSuccess'){
		layer.msg('提交成功', {icon: 1});
	}else if(method=='driverAuthSuccess'){
		layer.msg('提交成功', {icon: 1});
	}else{
	
	}
</script>
<style>
.tablelist td:nth-child(odd){
	background:#d4e4f1;
}
 table,table tr th, table tr td {
	border:1px solid #cbcbcb;
}
.scbtn{
    margin: 0px auto;
    width: 80%;
    height:100px;
    }
.tablelist{
    width:80%;
    margin-left: 30px;
    margin-top: 30px;
    }
</style>
</head>
<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="<%=path %>/monthlyHandle_list?isRenew=0">月租办理</a></li>
    <li><a href="#">详情</a></li>
    </ul>
    </div>
      <table class="tablelist">
    	<thead>
    	<tr>
        <td>车牌号</td>
        <td>${handleMonthlyVehicle.carPlate}</td>
        <td>用户账号</td>
        <td>${handleMonthlyVehicle.users.mobile}</td>
        </tr>
    	<tr>
        <td>停车场</td>
        <td>${handleMonthlyVehicle.carpark.name}</td>
        <td>包期类型</td>
        <td>${handleMonthlyVehicle.ruleName}</td>
        </tr>
    	<tr>
        <td>办理时长</td>
        <td><c:if test="${handleMonthlyVehicle.ruleType==1}">${handleMonthlyVehicle.timeLong-1}个月</c:if>
        <c:if test="${handleMonthlyVehicle.ruleType==2}">${handleMonthlyVehicle.timeLong-1}年</c:if></td>
        <td>到期时间</td>
        <td><fmt:formatDate value="${handleMonthlyVehicle.endTime}" pattern="yyyy-MM-dd"/></td>
        </tr>
    	<tr>
        <td>费用</td>
        <td>${handleMonthlyVehicle.payFee/100}元</td>
        <td>办理时间</td>
        <td><fmt:formatDate value="${handleMonthlyVehicle.handleTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        </tr>
    	<tr>
        <td>车辆类型</td>
        <td> <c:if test="${handleMonthlyVehicle.vehicleType==0}">其他</c:if>
        <c:if test="${handleMonthlyVehicle.vehicleType==1}">小型汽车</c:if>
        <c:if test="${handleMonthlyVehicle.vehicleType==2}">大型汽车</c:if></td>
        <td>车牌颜色</td>
        <td>
        <c:if test="${handleMonthlyVehicle.plateColor==0}">其他</c:if>
        <c:if test="${handleMonthlyVehicle.plateColor==1}">蓝色</c:if>
        <c:if test="${handleMonthlyVehicle.plateColor==2}">黄色</c:if>
        <c:if test="${handleMonthlyVehicle.plateColor==3}">黑色</c:if>
        <c:if test="${handleMonthlyVehicle.plateColor==4}">白色</c:if>
        </td>
        </tr>
    	<tr>
        <td>车主姓名</td>
        <td>${handleMonthlyVehicle.vehicleHost}</td>
        <td>使用人姓名</td>
        <td></td>
        </tr>
        </thead>
        </table>
    <input name="" type="button" class="scbtn" value="返回" onclick="window.history.back()"/>

</body>

</html>
