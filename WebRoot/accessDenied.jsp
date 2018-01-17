<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>错误</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>

<script language="javascript">
	$(function(){
    $('.access').css({'position':'absolute','left':($(window).width()-490)/2});
	$(window).resize(function(){  
    $('.access').css({'position':'absolute','left':($(window).width()-490)/2});
    })  
});  
</script> 


</head>


<body style="background:#FFF8ED">

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="<%=path %>/main_desktop" >首页</a></li>
    <li><a href="#">403错误提示</a></li>
    </ul>
    </div>
    
    <div class="access">
    
    <h2>访问被拒绝，无权访问该资源！</h2>
    <p>请返回，或联系管理员分配权限!</p>
    <div class="reindex"><a href="<%=path %>/main_desktop" >返回首页</a></div>
    
    </div>


</body>

</html>

