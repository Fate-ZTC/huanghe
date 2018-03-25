<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>异常描述-列表</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/page/js/common.js"></script>
<script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>
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
    <li><a href="#">消防巡查</a></li>
    <li><a href="<%=path %>/firePatrolExc_list">术语管理</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    <form action="<%=path %>/firePatrolExc_list" method="post" id="searchForm">
    <ul class="seachform">
    	<li><label>关键词:</label><input name="exceptionName" value="${firePatrolException.username}" type="text" class="scinput" /></li>
		<li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="搜索"/>
		<input type="hidden" name="pageSize" value="${pageSize}" />
		<input type="hidden" name="page" id="page" value="${page}"/>
		</li>
	</ul>
	</form>
    <div class="tools">
    
    	<ul class="toolbar">
    	<sec:authorize ifAnyGranted="firePatrolExc_add">
    	<li onclick="forWardUrl('<%=path %>/firePatrolExc_add');"><span><img src="<%=path %>/page/images/t01.png" /></span>添加</li>
    	</sec:authorize>
    	<sec:authorize ifAnyGranted="firePatrolExc_delete">
        <li onclick="bulkDelete('<%=path %>/firePatrolExc_delete','0');"><span><img src="<%=path %>/page/images/t03.png" /></span>删除</li>
        </sec:authorize>
        <li><span>${msg }</span></li>
        </ul>
    
    </div>
    
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <th width="40px"><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>
        <th width="150px">术语内容</th>
        <th width="150px">排序</th>
        <th width="150px">更新时间</th>
        <th width="150px">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${firePatrolExceptionPage.list}" var="d">
        <tr>
        <td><input type="checkbox" name="checkbox" value="${d.id}" id="checkbox_${d.id}" class="checkItem"/></td>
        <td>${d.exceptionName}</td>
        <td>${d.sort}</td>
        <td><fmt:formatDate value="${d.lastUpdateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
        <td>
        	<sec:authorize ifAnyGranted="firePatrolExc_edit">
        	<a href="<%=path %>/firePatrolExc_edit?id=${d.id}" class="tablelink">编辑人员信息</a> | 
        	</sec:authorize>
        	<sec:authorize ifAnyGranted="firePatrolExc_delete">
        	<a href="javascript:void(0);" onclick="bulkDelete('<%=path %>/firePatrolExc_delete','${d.id}');" class="tablelink"> 删除</a> 
        	</sec:authorize>
        </td>
        </tr> 
        </c:forEach>
        </tbody>
    </table>
    
   	<!-- 分页开始 -->
    <div class="pagin">
    	<div class="message">共<i class="blue">${firePatrolExceptionPage.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${firePatrolExceptionPage.currentPage}/${firePatrolExceptionPage.totalPage }&nbsp;</i>页</div>
        <ul class="paginList">
			<c:forEach items="${firePatrolExceptionPage.fristPage}" var="f">
				<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
			</c:forEach>
			<li class="paginItem current"><a href="javascript:void(0);">${firePatrolExceptionPage.currentPage}</a></li>
			<c:forEach items="${firePatrolExceptionPage.laPage}" var="l">
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
