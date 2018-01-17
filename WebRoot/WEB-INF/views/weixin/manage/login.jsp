<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%> 
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="msapplication-tap-highlight" content="no" />
	<title>酒城停车登录页</title>
	<link rel="stylesheet" href="<%=basePath%>wxmange/css/common.css">
	<link rel="stylesheet" href="<%=basePath%>wxmange/css/style.css">
	<script type="text/javascript" src="<%=basePath%>wxmange/layer_mobile/layer.js"></script>
	<script type="text/javascript" src="<%=basePath%>wxmange/js/jquery.min.js"></script>	
</head>
<body>
	<div class="box login-box posit-RE">
		<div class="login-logo"><img src="<%=basePath%>wxmange/images/dl-logo.png" alt=""></div>
		<input type="text" class="login-user-input login-text" id="userName" value="" placeholder="用户名">
		<input type="password" class="login-pass-input login-text" id="password" value="" placeholder="密码">
		<input type="button" value="登录" onclick="subLogin()" class="login-login-input">
	</div>
	<script type="text/javascript">
		var path = '<%=basePath%>';
		function subLogin(){
			 layer.open({type: 2});
			 var userName = $('#userName').val();
			 var password = $('#password').val();
			 if(userName==""){
			 	 layer.closeAll();
			 	  layer.open({
				    content: '用户名不能为空'
				    ,skin: 'msg'
				    ,time: 2 //2秒后自动关闭
				  });
				  return;
			 }
			 if(password==""){
			 	layer.closeAll();
			 	 layer.open({
				    content: '密码不能为空'
				    ,skin: 'msg'
				    ,time: 2 //2秒后自动关闭
				  });
				  return;
			 }
			 $.post(path+'manageLoginIn',{'userName':userName,'password':password},function(data){
			 	  
			 	  if(data.status=='true'){
			 	  	  //登录成功
			 	  	  layer.open({
					    content: data.errorMsg
					    ,skin: 'msg'
					    ,time: 2 //2秒后自动关闭
					  });
					  window.location.href=path+'toOperation';
					  layer.closeAll();
			 	  }else{
			 	  		//登录失败
			 	  		layer.closeAll();
			 	  		layer.open({
						    content: data.errorMsg
						    ,skin: 'msg'
						    ,time: 2 //2秒后自动关闭
						 });
			 	  }
			 },'json');
		}
	</script>
</body>
</html>