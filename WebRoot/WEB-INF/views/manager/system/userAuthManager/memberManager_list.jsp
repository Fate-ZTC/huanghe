<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>部门管理-列表</title>
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
    <li><a href="#">应用管理</a></li>
    <li><a href="<%=path %>/memberManager_list">会员用户管理</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    <form action="<%=path %>/memberManager_list" method="post" id="searchForm">
    <ul class="seachform">
    	<li><label>用户账号</label><input name="mobile" value="${mobile}" type="text" class="scinput" /></li>
		<li>
			<label>开始时间：</label>
			<input type="text" class="laydate-icon scinput" id="start-time" name="startTime" value="${startTime}" style="width: 137px;height:32px;" />
		</li>
		<li>
			<label>结束时间：</label>
			<input type="text" class="laydate-icon scinput" id="end-time" name="endTime" value="${endTime}" style="width: 137px;height:32px;" />
		</li>
		<li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="查询"/>
		<input type="hidden" name="pageSize" value="${pageSize}" />
		<input type="hidden" name="page" id="page" value="${page}"/>
		</li>
	</ul>
	</form>
    <div class="tools">
    	<ul class="toolbar">
    	<sec:authorize ifAnyGranted="memberManager_delete">
        <li onclick="bulkDelete('<%=path %>/memberManager_delete','0');"><span><img src="<%=path %>/page/images/t03.png" /></span>批量删除</li>
        </sec:authorize>
        </ul>
    </div>
    
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <th width="40px"><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>
        <th width="">头像</th>
        <th width="">账号</th>
        <th width="">昵称</th>
        <th width="">性别</th>
        <th width="">所在城市</th>
        <th width="">省份</th>
        <th width="">国家</th>
        <th width="">添加时间</th>
        <th width="">最后活跃</th>
        <th width="">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${usersPage.list}" var="u">
        <tr>
        <td><input type="checkbox" name="checkbox" value="${u.mobile}" id="checkbox_${u.mobile}" class="checkItem"/></td>
        <td><img src="${u.head}"/></td>
        <td>${u.mobile}</td>
        <td>${u.nickname}</td>
        <td><c:if test="${u.sex==1}">男</c:if>
        	<c:if test="${u.sex==2}">女</c:if></td>
        <td>${u.city}</td>
        <td>${u.province}</td>
        <td>${u.country}</td>
        <td><fmt:formatDate value="${u.posttime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        <td><fmt:formatDate value="${u.lastAcces}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        <td>
        	<sec:authorize ifAnyGranted="memberManager_edit">
        	<a href="<%=path %>/memberManager_edit?mobile=${u.mobile}" class="tablelink">编辑</a> | 
        	</sec:authorize>
        	<sec:authorize ifAnyGranted="memberManager_delete">
        	<a href="javascript:void(0);" onclick="bulkDelete('<%=path %>/memberManager_delete','${u.mobile}');" class="tablelink"> 删除</a> 
        	</sec:authorize>
        </td>
        </tr> 
        </c:forEach>
        </tbody>
    </table>
    
   	<!-- 分页开始 -->
    <div class="pagin">
    	<div class="message">共<i class="blue">${usersPage.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${usersPage.currentPage}/${usersPage.totalPage }&nbsp;</i>页</div>
        <ul class="paginList">
			<c:forEach items="${usersPage.fristPage}" var="f">
				<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
			</c:forEach>
			<li class="paginItem current"><a href="javascript:void(0);">${usersPage.currentPage}</a></li>
			<c:forEach items="${usersPage.laPage}" var="l">
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
