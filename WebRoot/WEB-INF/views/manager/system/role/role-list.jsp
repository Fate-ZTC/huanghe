<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>角色管理-列表</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<link href="<%=path %>/page/css/select.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/select-ui.min.js"></script>
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
    <li><a href="<%=path %>/role_list">角色管理</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    <form action="<%=path %>/role_list" method="post" id="searchForm">
    <ul class="seachform">
    	<li><label>名称</label><input name="name" value="${role.name}" type="text" class="scinput" /></li>
    	<li><label>可用</label>
    		<div class="vocation">
				<select class="select3" name="enable" >
		        	<option value="-1">-请选择-</option>
					<option value="0" ${role.enable eq 0?"selected":"" }>否</option>
					<option value="1" ${role.enable eq 1?"selected":"" }>是</option>
		        </select>
			</div>
   		</li>
		<li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="查询"/>
		<input type="hidden" name="pageSize" value="${pageSize}" />
		<input type="hidden" name="page" id="page" value="${page}"/>
		</li>
	</ul>
	</form>
    <div class="tools">
    
    	<ul class="toolbar">
    	<sec:authorize ifAnyGranted="role_add">
    	<li onclick="forWardUrl('<%=path %>/role_add');"><span><img src="<%=path %>/page/images/t01.png" /></span>添加</li>
    	</sec:authorize>
    	<sec:authorize ifAnyGranted="role_delete">
        <li onclick="bulkDelete('<%=path %>/role_delete','0');"><span><img src="<%=path %>/page/images/t03.png" /></span>删除</li>
        </sec:authorize>
        </ul>
    
    </div>
    
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <th width="40px"><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>
        <th width="100px">类型</th>
        <th width="">名称</th>
        <th width="">编码</th>
        <th width="">是否可用</th>
        <th width="">核心模块</th>
        <th width="">创建时间</th>
        <th width="90px">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${rolePage.list}" var="r">
        <tr>
        <td><input type="checkbox" name="checkbox" value="${r.roleId}" id="checkbox_${r.roleId}" class="checkItem"/></td>
       	<td><c:if test="${r.roleType==1}">系统角色</c:if>
       	<c:if test="${r.roleType==2}">区\县角色</c:if>
       	<c:if test="${r.roleType==3}">企业角色</c:if>
        <s:else></s:else>
        </td>
        <td>${r.name}</td>
        <td>${r.enname}</td>
        <td><c:if test="${r.enable==0}">否</c:if>
       <c:if test="${r.enable==1}">是</c:if></td>
        <td><c:if test="${r.iscore==0}">否</c:if>
       <c:if test="${r.iscore==1}">是</c:if></td>
        <td><fmt:formatDate value="${r.createTime}" pattern="yyyy-MM-dd HH:mm"/></td> 
        <td>
        	<sec:authorize ifAnyGranted="role_edit">
        	<a href="<%=path %>/role_edit?id=${r.roleId}" class="tablelink">编辑</a> | 
        	</sec:authorize>
        	<sec:authorize ifAnyGranted="role_delete">
        	<a href="javascript:void(0);" onclick="bulkDelete('<%=path %>/role_delete','${r.roleId}');" class="tablelink"> 删除</a> 
        	</sec:authorize>
        </td>
        </tr> 
        </c:forEach>
        </tbody>
    </table>
    
   <!-- 分页开始 -->
    <div class="pagin">
    	<div class="message">共<i class="blue">${rolePage.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${rolePage.currentPage}/${rolePage.totalPage }&nbsp;</i>页</div>
        <ul class="paginList">
			<c:forEach items="${rolePage.fristPage}" var="f">
				<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
			</c:forEach>
			<li class="paginItem current"><a href="javascript:void(0);">${rolePage.currentPage}</a></li>
			<c:forEach items="${rolePage.laPage}" var="l">
				<li class="paginItem"><a href="javascript:void(0);" rel="${l}">${l}</a></li>
			</c:forEach>
        </ul>
    </div>
    <!-- 分页结束 -->
    </div>
    <script type="text/javascript">
    $(function(){
    	//选择框
	    $(".select3").uedSelect({
			width : 152
		});
    });
	$('.tablelist tbody tr:odd').addClass('odd');
	</script>

</body>

</html>
