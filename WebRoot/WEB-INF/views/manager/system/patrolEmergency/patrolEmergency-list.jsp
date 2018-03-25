<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>突发事件管理-列表</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<link href="<%=path %>/page/css/select.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/select-ui.min.js"></script>
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
	}else{
	}
	
</script>
</head>
<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">安防巡查</a></li>
    <li><a href="<%=path %>/patrolEmergency_list">突发事件管理</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    <form action="<%=path %>/patrolEmergency_list" method="post" id="searchForm">
    <ul class="seachform">
   		<li>
			<label>开始时间：</label>
			<input type="text" class="laydate-icon scinput" id="start-time" name="startTime" value="${startTime}" style="width: 137px;height:32px;" />
		</li>
		<li>
			<label>结束时间：</label>
			<input type="text" class="laydate-icon scinput" id="end-time" name="endTime" value="${endTime}" style="width: 137px;height:32px;" />
		</li>
		<li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="搜索"/>
		<input type="hidden" name="pageSize" value="${pageSize}" />
		<input type="hidden" name="page" id="page" value="${page}"/>
		</li>
	</ul>
	</form>
    <div class="tools">
    
    	<ul class="toolbar">
    	<sec:authorize ifAnyGranted="patrolEmergency_delete">
        <li onclick="bulkDelete('<%=path %>/patrolEmergency_delete','0');"><span><img src="<%=path %>/page/images/t03.png" /></span>批量删除</li>
        </sec:authorize>
        <sec:authorize ifAnyGranted="patrolEmergency_delete">
        <li onclick="forWardUrl('<%=path %>/patrolEmergency_excelOut','0');"><span><img src="<%=path %>/page/images/t04.png" /></span>导出</li>
        </sec:authorize>
        </ul>
    
    </div>
    
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <th width="40px"><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>
        <th width="150px">突发事件发生时间</th>
        <th width="150px">突发事件结束时间</th>
        <th width="150px">事件时长</th>
        <th width="150px">突发事件发起人姓名</th>
        <th width="150px">突发事件发起人账号</th>
        <th width="150px">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${patrolEmergencyPage.list}" var="d">
        <tr>
        <td><input type="checkbox" name="checkbox" value="${d.id}" id="checkbox_${d.id}" class="checkItem"/></td>
          <td><fmt:formatDate value="${d.startTime}" pattern="yyyy-MM-dd HH:mm"/></td>
          <td><fmt:formatDate value="${d.endTime}" pattern="yyyy-MM-dd HH:mm"/></td>
        <td>${d.checkDuration }</td>
        <td>${d.username}</td>
        <td>${d.jobNum}</td>
        <td>
        	<sec:authorize ifAnyGranted="patrolEmergency_delete">
        	<a href="javascript:void(0);" onclick="bulkDelete('<%=path %>/patrolEmergency_delete','${d.id}');" class="tablelink"> 删除</a> 
        	</sec:authorize>
        </td>
        </tr> 
        </c:forEach>
        </tbody>
    </table>
    
   	<!-- 分页开始 -->
    <div class="pagin">
    	<div class="message">共<i class="blue">${patrolEmergencyPage.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${patrolEmergencyPage.currentPage}/${patrolEmergencyPage.totalPage }&nbsp;</i>页</div>
        <ul class="paginList">
			<c:forEach items="${patrolEmergencyPage.fristPage}" var="f">
				<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
			</c:forEach>
			<li class="paginItem current"><a href="javascript:void(0);">${patrolEmergencyPage.currentPage}</a></li>
			<c:forEach items="${patrolEmergencyPage.laPage}" var="l">
				<li class="paginItem"><a href="javascript:void(0);" rel="${l}">${l}</a></li>
			</c:forEach>
        </ul>
    </div>
    <!-- 分页结束 -->
    </div>
    <script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
	</script>

</body>

</html>
