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
<title>蓝牙标签-导入</title>
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
    <li><a href="#">安防巡更</a></li>
    <li><a href="<%=path %>/patrolBeaconInfo_list">蓝牙标签管理</a></li>
    <li><a href="#">导入</a></li>
    </ul>
    </div>
    <div class="formbody">
    <div class="formtitle"><span>导入蓝牙标签</span></div>
    <form action="<%=path %>/patrolBeaconInfo_import" method="post" id="addForm" enctype=”multipart/form-data”  data-validator-option="{stopOnError:true, timely:1 ,theme:'yellow_right'}">
    <input type="hidden" name="method" value="import" />
    <ul class="forminfo">
    <li><label>EXCEL<b>*</b></label><input name="file" type="file" class="dfinput" data-rule="EXCEL:required"/></li>
    <li><label>&nbsp;</label><input name="" type="submit" class="btn" value="确认导入"/></li>
    </ul>
    </form>
    
    </div>

</body>

</html>
