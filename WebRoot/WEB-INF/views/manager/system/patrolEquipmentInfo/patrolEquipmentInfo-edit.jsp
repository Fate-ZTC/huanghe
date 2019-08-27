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
<title>蓝牙标签-编辑</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<link rel="stylesheet" href="<%=path %>/page/validator/jquery.validator.css" />
<script type="text/javascript" src="<%=path %>/page/validator/jquery.validator.js"></script>
<script type="text/javascript" src="<%=path %>/page/validator/local/zh-CN.js"></script>
<script type="text/javascript" src="<%=path %>/page/laydate/laydate.js"></script>
<script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>

<script type="text/javascript">
$(function(){
   	//选择框
   	laydate.skin('molv');
 });

var msg = '${msg }';
if(msg != ''){
    layer.msg(msg, {icon: 1});
}
</script>
</head>

<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">安防巡更</a></li>
    <li><a href="<%=path %>/patrolBeaconInfo_list">蓝牙标签管理</a></li>
    <li><a href="#">编辑</a></li>
    </ul>
    </div>
    <div class="formbody">
    <div class="formtitle"><span>编辑蓝牙标签</span></div>
        <form action="<%=path %>/patrolBeaconInfo_edit" method="post" id="addForm" data-validator-option="{stopOnError:true, timely:1 ,theme:'yellow_right'}">
            <input type="hidden" name="method" value="edit" />
            <ul class="forminfo">
                <li><label>UUID<b>*</b></label><input name="uuid" type="text" value="${patrolBeaconInfo.uuid}" class="dfinput" data-rule="UUID:required;length[~100];"/></li>
                <li><label>major<b>*</b></label><input name="major" type="text" class="dfinput" value="${patrolBeaconInfo.major}" data-rule="UUID:required;digits;"/></li>
                <li><label>minor<b>*</b></label><input name="minor" type="text" class="dfinput" value="${patrolBeaconInfo.minor}" data-rule="UUID:required;digits;"/></li>
                <input type="hidden" name="id" value="${patrolBeaconInfo.beaconId}"/>
                <input type="hidden" name="beaconId" value="${patrolBeaconInfo.beaconId}"/>
                <li><label>&nbsp;</label><input name="" type="submit" class="btn" value="确认添加"/></li>
            </ul>
        </form>
    
    </div>

</body>

</html>
