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
<title>系统用户-添加</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<link rel="stylesheet" href="<%=path %>/page/validator/jquery.validator.css" />
<script type="text/javascript" src="<%=path %>/page/validator/jquery.validator.js"></script>
<script type="text/javascript" src="<%=path %>/page/validator/local/zh-CN.js"></script>
<script type="text/javascript" src="<%=path %>/page/laydate/laydate.js"></script>
<link rel="stylesheet" href="<%=path %>/page/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css"/>
<script type="text/javascript" src="<%=path %>/page/ztree/jquery.ztree.all-3.5.min.js"></script>
<style type="text/css">
.menu{
	z-index: 2;
	_top: 6px;
	cursor: pointer;
	height: 32px;
	border: 1px solid #cccccc;
}
.level2 span,.level1 span,.level0 span{
	display: inline;
}
</style>
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
    <li><a href="<%=path %>/manager_list">系统用户</a></li>
    <li><a href="#">添加</a></li>
    </ul>
    </div>
    <div class="formbody">
    <div class="formtitle"><span>添加系统用户</span></div>
    <form action="<%=path %>/manager_add" method="post" id="addForm" data-validator-option="{stopOnError:true, timely:1 ,theme:'yellow_right'}">
    <input type="hidden" name="method" value="add" />
    <input type="hidden" name="enablRegionIds" id="enablRegionIds"/>
    <ul class="forminfo">
    <li><label>用户名<b>*</b></label><input name="username" type="text" class="dfinput" data-rule="用户名:required;manager.username;remote[<%=path %>/managerInfo_checkUsername]"/></li>
    <li><label>密码<b>*</b></label><input name="password" type="text" class="dfinput" data-rule="密码:required;password"/></li>
    <li><label>姓名</label><input name="realname" type="text" class="dfinput" data-rule="姓名:length[~30]"/></li>
    <li><label>QQ</label><input name="qq" type="text" class="dfinput" data-rule="QQ:qq;length[~20]"/></li>
    <li><label>部门<b>*</b></label>
    	<select class="menu" name="department.departmentid" data-rule="部门:required;">
        	<option value="">-请选择-</option>
        	<c:forEach items="${departmentList}" var="d">
				<option value="${d.departmentid}" >${d.name}</option>
        	</c:forEach>
        </select>
    </li>
    <li><label>角色<b>*</b></label>
        <c:forEach items="${roleList}" var="r">
            <input name="roles" data-rule="角色:required;" style="width: 15px;height:13px" type="checkbox" class="dfinput" value="${r.roleId}"/>${r.name}
        </c:forEach>
<%--    	<select class="menu" name="role.roleId" data-rule="角色:required;">--%>
<%--        	<option value="">-请选择-</option>--%>
<%--        	<c:forEach items="${roleList}" var="r">--%>
<%--				<option value="${r.roleId }" >${r.name}</option>--%>
<%--        	</c:forEach>--%>
<%--        </select>--%>
    </li>
    <li><label>添加时间<b>*</b></label><input name="" type="text" style="height: 34px;" value="<fmt:formatDate value="<%=date %>" pattern="yyyy-MM-dd HH:mm:ss"/>"
    	data-rule="添加时间:required;" class="laydate-icon" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})"/></li>
    <li><label>&nbsp;</label><input name="" type="button" class="btn" value="确认添加" onclick="_submit()"/></li>
    </ul>
    </form>
    
    </div>
<script type="text/javascript">
var host ='<%=path%>';
var _submit = function(){
	$('#addForm').submit();
}
</script>
</body>

</html>
