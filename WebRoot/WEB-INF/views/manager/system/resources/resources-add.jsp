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
<title>资源权限-添加</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
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
<style type="text/css">
#menuId{
	z-index: 2;
	_top: 6px;
	cursor: pointer;
	height: 32px;
	border: 1px solid #cccccc;
}
</style>
</head>

<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">系统管理</a></li>
    <li><a href="<%=path %>/resources_list">资源权限管理</a></li>
    <li><a href="#">添加</a></li>
    </ul>
    </div>
    <div class="formbody">
    <div class="formtitle"><span>添加资源权限</span></div>
    <form action="<%=path %>/resources_add" method="post" data-validator-option="{stopOnError:true, timely:1 ,theme:'yellow_right'}">
    <input type="hidden" name="method" value="add" />
    <input type="hidden" name="resourcetype" value="1"/>
    <input type="hidden" name="isopen" value="1"/>
    <input type="hidden" name="isleaf" value="1"/>
    <ul class="forminfo">
    <li><label>名称<b>*</b></label><input name="name" type="text" class="dfinput" data-rule="名称:required;length[~100]"/></li>
    <li><label>编码<b>*</b></label><input name="enname" type="text" class="dfinput" data-rule="编码:required;length[~100]"/></li>
    <li><label>URL<b>*</b></label><input name="target" type="text" class="dfinput" data-rule="URL:required;length[~100]"/></li>
    <li><label>所属模块<b>*</b></label>
		<select id="menuId" name="menu.menuId" data-rule="所属模块:required;" >
        	<option value="">-请选择-</option>
        	<c:forEach items="${menuList}" var="m">
        	<option value="${m.menuId}" disabled="disabled">${m.name}</option>
        	<c:forEach items="${m.childrenMenu}" var="cm">
        	<option value="${cm.menuId }">&nbsp;&nbsp;&nbsp;&nbsp;|-${cm.name}</option>
        	</c:forEach>
        	</c:forEach>
        </select>
    </li>
    <li><label>核心模块<b>*</b></label>
    	
    	<cite><input name="iscore" type="radio" value="0" data-rule="核心模块:checked;" />否&nbsp;&nbsp;&nbsp;&nbsp;
    	<input name="iscore" type="radio" value="1" checked="checked"/>是
    	</cite>
    </li>
    <li><label>可用<b>*</b></label>
    	<cite><input name="enable" type="radio" value="0" data-rule="可用:checked;" />否&nbsp;&nbsp;&nbsp;&nbsp;
    	<input name="enable" type="radio" value="1" checked="checked"/>是
    	</cite>
    </li>
    <li><label>序列<b>*</b></label><input name="orderid" type="text" class="dfinput" value="999" data-rule="序列:required;integer[+0];"/></li>
    <li><label>添加时间<b>*</b></label><input name="" type="text" style="height: 34px;" value="<fmt:formatDate value="<%=date %>" pattern="yyyy-MM-dd HH:mm:ss"/>"
    	data-rule="添加时间:required;" class="laydate-icon" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})"/></li>
    <li><label>&nbsp;</label><input name="" type="submit" class="btn" value="确认添加"/></li>
    </ul>
    </form>
    
    </div>


</body>

</html>
