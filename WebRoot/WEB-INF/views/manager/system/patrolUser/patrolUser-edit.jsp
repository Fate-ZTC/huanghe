<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>巡更人员编辑</title>
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
    <li><a href="<%=path %>/patrolUser_list">巡更人员管理</a></li>
    <li><a href="#">编辑</a></li>
    </ul>
    </div>
    <div class="formbody">
    <div class="formtitle"><span>编辑人员</span></div>
    <form action="<%=path %>/patrolUser_edit" method="post" id="addForm" data-validator-option="{stopOnError:true, timely:1 ,theme:'yellow_right'}">
    <input type="hidden" name="method" value="edit" />
    <input type="hidden" name="id" value="${patrolUser.id}"/>
    <ul class="forminfo">
    <li><label>姓名<b>*</b></label><input name="username" value="${patrolUser.username}" type="text" class="dfinput" data-rule="名称:required;length[~15];"/></li>
    <li><label>密码<b>*</b></label><input name="password" type="password" value="${patrolUser.password }" class="dfinput" data-rule="length[~20]"/></li>
    <li><label>工号<b>*</b></label><input name="jobNum" value="${patrolUser.jobNum}" type="text" class="dfinput" /></li>
    <li><label>校区<b>*</b></label>
        <input name="campusNum" type="radio" <c:if test="${patrolUser.campusNum==0}"> checked</c:if> value="0" style="margin: auto;"/>&nbsp;&nbsp;袁家岗校区&nbsp;&nbsp;&nbsp;&nbsp;
        <input name="campusNum" type="radio" <c:if test="${patrolUser.campusNum==1}"> checked</c:if>  value="1"/>&nbsp;&nbsp;缙云校区&nbsp;&nbsp;&nbsp;&nbsp;
    </li>
    <li><label>&nbsp;</label><input name="" type="submit" class="btn" value="确认保存"/></li>
    </ul>
    </form>
    
    </div>

</body>

</html>
