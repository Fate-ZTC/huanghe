<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="<%=path %>/page/js/jquery.js"></script>


</head>

<body style="background:url(<%=path %>/page/images/topbg.gif) repeat-x;">

    <div class="topleft">
    <a href="<%=path %>/main_index" target="_parent"><img src="<%=path %>/page/images/logo.png" title="系统首页" /></a>
    </div>
        
            
    <div class="topright">    
    <ul>
    <li><a href="<%=path %>/j_spring_security_logout" target="_parent">退出</a></li>
    </ul>
     
    <div class="user">
    <span>欢迎您,${sessionScope.loginUser.username}</span>
    </div>      
    
    </div>

</body>
</html>
