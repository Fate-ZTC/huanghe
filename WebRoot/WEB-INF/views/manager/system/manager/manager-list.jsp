<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统用户-列表</title>
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
	}else if(method=='restSuccess'){
		layer.msg('重置成功', {icon: 1});
	}else{
	}
</script>
</head>
<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">系统管理</a></li>
    <li><a href="<%=path %>/manager_list">系统用户</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    <form action="<%=path %>/manager_list" method="post" id="searchForm">
    <ul class="seachform">
    	<li><label>用户名</label><input name="username" value="${manager.username}" type="text" class="scinput" /></li>
    	<li><label>姓名</label><input name="realname" value="${manager.realname}" type="text" class="scinput" /></li>
		<li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="查询"/>
		<input type="hidden" name="pageSize" value="${pageSize}" />
		<input type="hidden" name="page" id="page" value="${page}"/>
		</li>
	</ul>
	</form>
    <div class="tools">
    	<ul class="toolbar">
    	<sec:authorize ifAnyGranted="manager_add">
    	<li onclick="forWardUrl('<%=path %>/manager_add');"><span><img src="<%=path %>/page/images/t01.png" /></span>添加</li>
    	</sec:authorize>
    	<sec:authorize ifAnyGranted="manager_delete">
        <li onclick="bulkDelete('<%=path %>/manager_delete','0');"><span><img src="<%=path %>/page/images/t03.png" /></span>删除</li>
        </sec:authorize>
        </ul>
    
    </div>
    
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <th width="40px"><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>
        <th width="150px">用户名</th>
        <th width="">姓名</th>
        <th width="">QQ</th>
        <th width="">部门</th>
        <th width="">角色</th>
        <th width="150px">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${managerPage.list}" var="u">
        <tr>
        <td><input type="checkbox" name="checkbox" value="${u.userId}" id="checkbox_${u.userId}" class="checkItem"/></td>
        <td>${u.username}</td>
        <td>${u.realname}</td>
        <td>${u.qq}</td>
        <td>${u.department.name}</td>
        <td>
            <c:forEach items="${u.managerRoles}" var="v">
                ${v.role.name}
            </c:forEach>
         </td>
        <td>
        	<sec:authorize ifAnyGranted="manager_resetPassword">
        	<a href="javascript:void(0);" onclick="resetPwd('<%=path %>/manager_resetPassword','${u.userId}');" class="tablelink">重置密码</a> |  
        	</sec:authorize>
        	<sec:authorize ifAnyGranted="manager_edit">
        	<a href="<%=path %>/manager_edit?id=${u.userId}" class="tablelink">编辑</a> | 
        	</sec:authorize>
        	<sec:authorize ifAnyGranted="manager_delete">
        	<a href="javascript:void(0);" onclick="bulkDelete('<%=path %>/manager_delete','${u.userId}');" class="tablelink"> 删除</a> 
        	</sec:authorize>
        </td>
        </tr> 
        </c:forEach>
        </tbody>
    </table>
    
   	<!-- 分页开始 -->
    <div class="pagin">
    	<div class="message">共<i class="blue">${managerPage.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${managerPage.currentPage}/${managerPage.totalPage }&nbsp;</i>页</div>
        <ul class="paginList">
			<c:forEach items="${managerPage.fristPage}" var="f">
				<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
			</c:forEach>
			<li class="paginItem current"><a href="javascript:void(0);">${managerPage.currentPage}</a></li>
			<c:forEach items="${managerPage.laPage}" var="l">
				<li class="paginItem"><a href="javascript:void(0);" rel="${l}">${l}</a></li>
			</c:forEach>
        </ul>
    </div>
    <!-- 分页结束 -->
    </div>
    <script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
	function resetPwd(url,id){
		layer.confirm('确定要重置密码为"123456"吗？',function(index){
				window.location.href=url+"?id="+id;
		});
	}
	</script>

</body>

</html>
