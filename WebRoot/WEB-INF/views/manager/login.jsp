<%@ page language="java" import="java.util.*,com.system.utils.DESHelper" pageEncoding="UTF-8"%>
<%@page import="com.alibaba.fastjson.JSONObject"%>
<%@page import="com.alibaba.fastjson.JSON"%>
<%
String path = request.getContextPath();
Cookie[] cookies = request.getCookies();

String rememberUserInfo = null,username="",password="";
if(cookies != null){
	for(Cookie cookie : cookies){
		if(cookie.getName().equals("xygisadminrememberUserInfo")){
			try{
				String value = DESHelper.decryptDES(cookie.getValue(),"lqkj*gis");
				JSONObject json = JSON.parseObject(value);
				username = json.getString("username");
				password = json.getString("password");
			}catch(Exception e){
				
			}
		}
	}
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>欢迎登录${sysconfigMap['adminTitle'] }</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<script src="<%=path %>/page/js/cloud.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>
<script type="text/javascript">
$(function(){
    $('.loginbox').css({'position':'absolute','left':($(window).width()-692)/2});
	$(window).resize(function(){  
    	$('.loginbox').css({'position':'absolute','left':($(window).width()-692)/2});
    });
	 $("#captcha").bind("click", function() {
		var timenow = new Date().getTime();
		$("#captcha").attr("src","captcha?w=112&h=44&f=36&d=" + timenow);
	});
}); 
var message = '${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message }';
if(message != '' && message != 'null'){
	layer.msg(message, function(){});
}
</script> 

</head>
<body style="background-color:#1c77ac; background-image:url(page/images/light.png); background-repeat:no-repeat; background-position:center top; overflow:hidden;">
    <div id="mainBody">
      <div id="cloud1" class="cloud"></div>
      <div id="cloud2" class="cloud"></div>
    </div>  
    <div class="loginbody">
    <span class="systemlogo" style="background:url(page/images/logo.png) no-repeat center;"></span> 
    <form action="<%=path %>/j_spring_security_check" method="post" autocomplete="off"> 
    <div class="loginbox loginbox3">
    <ul>
    	<li>
	    	<input name="username" type="text" class="loginuser" title="请输入用户名" value="<%=username %>" placeholder="请输入用户名" autocomplete="off"/></li>
	    <li><input name="password" type="password" class="loginpwd" title="请输入密码" placeholder="请输入密码" value="<%=password %>" autocomplete="off"/></li>
	    <li class="yzm">
	    	<span><input name="validateCode" type="text" title="请输入验证码" placeholder="请输入验证码"  autocomplete="off"/></span>
	    	<img src="captcha?w=112&h=44&f=36" style="float:right;border: 1px solid #A6CAFF;cursor: pointer;" title="看不清?换一张" alt="看不清?，换一张" id="captcha"/>
	    </li>
    	<li><input type="submit" class="loginbtn" value="登录" /><label><input name="remember" type="checkbox" checked="checked" />记住密码</label></li>
    </ul>
    </div>
    </form>
    
    </div>
    
</body>

</html>
