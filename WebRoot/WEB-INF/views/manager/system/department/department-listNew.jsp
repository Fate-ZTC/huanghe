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
    <li><a href="#">系统管理</a></li>
    <li><a href="<%=path %>/department_list">部门管理</a></li>
    </ul>
    </div>

    <div class="rightinfo">
    <form action="<%=path %>/department_list" method="post" id="searchForm">
    <ul class="seachform">
    	<li><label>名称</label><input name="name" value="${department.name}" type="text" class="scinput" /></li>
		<li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="查询"/>
		<input type="hidden" name="pageSize" value="${pageSize}" />
		<input type="hidden" name="page" id="page" value="${page}"/>
		</li>
	</ul>
	</form>
    <div class="tools">

    	<ul class="toolbar">
    	<sec:authorize ifAnyGranted="department_add">
    	<li onclick="forWardUrl('<%=path %>/department_add');"><span><img src="<%=path %>/page/images/t01.png" /></span>添加</li>
    	</sec:authorize>
    	<sec:authorize ifAnyGranted="department_delete">
        <li onclick="bulkDelete('<%=path %>/department_delete','0');"><span><img src="<%=path %>/page/images/t03.png" /></span>删除</li>
        </sec:authorize>
        </ul>

    </div>


    <table class="tablelist">
    	<thead>
    	<tr>
        <th width="40px"><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>
        <th width="150px">名称</th>
        <th width="">描述</th>
        <th width="150px">创建时间</th>
        <th width="90px">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${departmentPage.list}" var="d">
        <tr>
        <td><input type="checkbox" name="checkbox" value="${d.departmentid}" id="checkbox_${d.departmentid}" class="checkItem"/></td>
        <td>${d.name}</td>
        <td>${d.description}</td>
        <td><fmt:formatDate value="${d.createTime}" pattern="yyyy-MM-dd HH:mm"/></td>
        <td>
        	<sec:authorize ifAnyGranted="department_edit">
        	<a href="<%=path %>/department_edit?id=${d.departmentid}" class="tablelink">编辑</a> |
        	</sec:authorize>
        	<sec:authorize ifAnyGranted="department_delete">
        	<a href="javascript:void(0);" onclick="bulkDelete('<%=path %>/department_delete','${d.departmentid}');" class="tablelink"> 删除</a>
        	</sec:authorize>
        </td>
        </tr>
        </c:forEach>
        </tbody>
    </table>

   	<!-- 分页开始 -->
    <div class="pagin">
    	<div class="message">共<i class="blue">${departmentPage.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${departmentPage.currentPage}/${departmentPage.totalPage }&nbsp;</i>页</div>
        <ul class="paginList">
			<c:forEach items="${departmentPage.fristPage}" var="f">
				<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
			</c:forEach>
			<li class="paginItem current"><a href="javascript:void(0);">${departmentPage.currentPage}</a></li>
			<c:forEach items="${departmentPage.laPage}" var="l">
				<li class="paginItem"><a href="javascript:void(0);" rel="${l}">${l}</a></li>
			</c:forEach>
        </ul>
    </div>
    <!-- 分页结束 -->
    </div>
    <script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
        $(function(){
            alert("你选择的部门下还有用户，请先删除相关用户再进行操作！");
        });
    </script>

</body>

</html>
