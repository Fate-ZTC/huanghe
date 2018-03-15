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
</head>
<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">月租管理</a></li>
    <li><a href="<%=path %>/monthlyHandle_list">月租办理</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    <form action="<%=path %>/monthlyHandle_list" method="post" id="searchForm">
    <ul class="seachform">
		<li>
			<label>开始时间：</label>
			<input type="text" class="laydate-icon scinput" id="start-time" name="startTime" value="${startTime}" style="width: 137px;height:32px;" />
		</li>
		<li>
			<label>结束时间：</label>
			<input type="text" class="laydate-icon scinput" id="end-time" name="endTime" value="${endTime}" style="width: 137px;height:32px;" />
		</li>
    	<li><label>停车场</label><input name="carparkName" value="${carparkName}" type="text" class="scinput" style="width: 110px;"/></li>
    	<li><label>用户账号</label><input name="mobile" value="${mobile}" type="text" class="scinput" style="width: 110px;"/></li>
    	<li><label>车牌号</label><input name="plateNo" value="${plateNo}" type="text" class="scinput" style="width: 110px;"/></li>
		<li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="搜索"/>
		<input type="hidden" name="isRenew" value="0"/>
		<input type="hidden" name="pageSize" value="${pageSize}" />
		<input type="hidden" name="page" id="page" value="${page}"/>
		</li>
	</ul>
	</form>
    <div class="tools">
    
    	<ul class="toolbar">
<!--    	<sec:authorize ifAnyGranted="userAuthManager_delete">-->
<!--        <li onclick="bulkDelete('<%=path %>/userAuthManager_delete','0');"><span><img src="<%=path %>/page/images/t03.png" /></span>删除</li>-->
<!--        </sec:authorize>-->
        </ul>
    
    </div>
    
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <!--<th><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>-->
        <th>停车场</th>
        <th>用户账号</th>
        <th>车牌号</th>
        <th>车辆类型</th>
        <th>包期类型</th>
        <th>办理时长</th>
        <th>到期时间</th>
        <th>缴费金额(元)</th>
        <th>办理时间</th>
        <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${handleMonthlyVehiclePage.list}" var="m">
        <c:if test="${m.isRenew==0}">
        <tr>
        <td>${m.carpark.name}</td>
        <td>${m.users.mobile}</td>
        <td>${m.carPlate}</td>
        <td>
        <c:if test="${m.vehicleType==0}">其他</c:if>
        <c:if test="${m.vehicleType==1}">小型汽车</c:if>
        <c:if test="${m.vehicleType==2}">大型汽车</c:if>
        </td>
        <td>${m.ruleName}</td>
        
        <td>
        <c:if test="${m.ruleType==1}">${m.timeLong-1}个月</c:if>
        <c:if test="${m.ruleType==2}">${m.timeLong-1}年</c:if>
        </td>
        
        <td><fmt:formatDate value="${m.endTime}" pattern="yyyy-MM-dd"/></td>
        <td>${m.payFee/100}</td>
        <td><fmt:formatDate value="${m.handleTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        <td><a href="<%=path %>/monthlyManage_detail?mid=${m.mid}&isRenew=0">详情</a></td>
        </tr> 
        </c:if>
        </c:forEach>
        </tbody>
    </table>
    
   	<!-- 分页开始 -->
    <div class="pagin">
    	<div class="message">共<i class="blue">${handleMonthlyVehiclePage.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${handleMonthlyVehiclePage.currentPage}/${handleMonthlyVehiclePage.totalPage }&nbsp;</i>页</div>
        <ul class="paginList">
			<c:forEach items="${handleMonthlyVehiclePage.fristPage}" var="f">
				<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
			</c:forEach>
			<li class="paginItem current"><a href="javascript:void(0);">${handleMonthlyVehiclePage.currentPage}</a></li>
			<c:forEach items="${handleMonthlyVehiclePage.laPage}" var="l">
				<li class="paginItem"><a href="javascript:void(0);" rel="${l}">${l}</a></li>
			</c:forEach>
        </ul>
    </div>
    <!-- 分页结束 -->
    </div>
    <script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
	
	var start_time = {
			  elem: '#start-time',
			  format: 'YYYY-MM-DD hh:mm:ss',
			  max: '2099-06-16',
			  istime: true,
			  istoday: false,
			  choose: function(datas){
			     end_time.min = datas;
			     $("#start-time").val(datas);
			  }
			};
			laydate(start_time);
			
			//结束时间
			var end_time = {
			  elem: '#end-time',
			  format: 'YYYY-MM-DD hh:mm:ss',
			  max: '2099-06-16',
			  istime: true,
			  istoday: false,
			  choose: function(datas){
			    start_time.max = datas;
			    $("#end-time").val(datas);
			  }
			};
			laydate(end_time);
	</script>

</body>

</html>
