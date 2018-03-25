<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>术语编辑</title>
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
</head>

<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">消防巡查</a></li>
    <li><a href="<%=path %>/firePatrolUser_list">术语管理</a></li>
    <li><a href="#">编辑</a></li>
    </ul>
    </div>
    <div class="formbody">
    <div class="formtitle"><span>编辑术语</span></div>
    <form action="<%=path %>/firePatrolExc_edit" method="post" id="addForm" data-validator-option="{stopOnError:true, timely:1 ,theme:'yellow_right'}">
    <input type="hidden" name="method" value="edit" />
    <input type="hidden" name="id" value="${firePatrolException.id}"/>
    <ul class="forminfo">
    <li><label>术语内容<b>*</b></label><input name="exceptionName" value="${firePatrolException.exceptionName}" type="text" class="dfinput" data-rule="名称:required;length[~100];"/></li>
    <li><label>排序<b>*</b></label><input name="sort" type="text" value="${firePatrolException.sort }" class="dfinput"/></li>
    <li><label>&nbsp;</label><input name="" type="submit" class="btn" value="确认保存"/></li>
    </ul>
    </form>
    
    </div>

</body>

</html>
