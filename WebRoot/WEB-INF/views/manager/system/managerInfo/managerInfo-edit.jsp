<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>个人资料</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<link rel="stylesheet" href="<%=path %>/page/validator/jquery.validator.css" />
<script type="text/javascript" src="<%=path %>/page/validator/jquery.validator.js"></script>
<script type="text/javascript" src="<%=path %>/page/validator/local/zh-CN.js"></script>
<script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>
<script type="text/javascript">
	var method = '${method }';
	if(method=='editSuccess'){
		layer.msg('更新成功', {icon: 1});
	}else{
	}
</script>
</head>

<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">系统管理</a></li>
	<li><a href="<%=path %>/system/managerInfo_edit">个人资料</a></li>
    </ul>
    </div>
    
    <div class="formbody">
    
    <div class="formtitle"><span>个人资料</span></div>
    <form action="<%=path %>/managerInfo_edit" method="post" id="addForm" data-validator-option="{stopOnError:true, timely:1 ,theme:'yellow_right'}">
    <input type="hidden" name="method" value="edit" />
    <input type="hidden" name="id" value="${manager.userId}"/>
    <ul class="forminfo">
    <li><label>姓名<b>*</b></label><input name="realname" value="${manager.realname }" type="text" class="dfinput" data-rule="姓名:required;length[~16]"/></li>
    <li><label>QQ<b>*</b></label><input name="qq" value="${manager.qq }" type="text" class="dfinput" data-rule="QQ:required;qq;length[~20]"/></li>
    <li><label>手机<b>*</b></label><input name="mobile" value="${manager.mobile }" type="text" class="dfinput" data-rule="手机:required;mobile;manager.mobile;remote[<%=path %>/managerInfo_checkMobile, id]"/></li>

    <li><label>&nbsp;</label><input name="" type="submit" class="btn" value="确认保存"/></li>
    </ul>
    </form>
    
    </div>


</body>

</html>

