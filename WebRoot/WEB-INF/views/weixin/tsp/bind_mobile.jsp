<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>登录验证</title>
		<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta name="msapplication-tap-highlight" content="no" />
		<link rel="stylesheet" type="text/css" href="<%=basePath %>/tsp/css/common.css"/>
		<link rel="stylesheet" type="text/css" href="<%=basePath %>/tsp/css/jiucheng.css"/>
		<script src="<%=basePath %>/tsp/js/jquery.min.js"></script>
		<script src="<%=basePath %>/layer_mobile/layer.js"></script>
		<script type="text/javascript">
			var the_host = '<%=basePath %>';
			var code = '${code}';
			var link = '${link}';
		</script>
	</head>
	<body>
		<div class="jiucheng-box f14">
			<div class="logo-box">
				<img src="<%=basePath %>/tsp/img/jcbj.png" alt="" />
			</div>
			<div class="loginMesg-box">
				<p class="tit-remind">请输入您的联系号码</p>
				<ul>
					<li class="input-li">
						<input type="text" class="input" name="" id="phone" value="" placeholder="输入手机号码" /><div class="code-get f12 text-CENT">获取验证码<!--<img src="img/bg.gif" alt="" />--></div>
					</li>
					<li class="input-li">
						<input type="text" class="input" name="" id="code_input" value="" placeholder="输入验证码" />
					</li>
					<p class="code-remind">如您未收到验证码，可能与运营商有关，请多试几次。</p>
					<li class="submit-li">
						<button class="submit">确定</button>
					</li>
				</ul>
			</div>
			
		</div>
		<script src="<%=basePath %>/tsp/js/login.js"></script>
	</body>
</html>
