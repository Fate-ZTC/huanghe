<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<title>正在加载</title>
	</head>
	<body>
		<script src="<%=basePath %>/tsp/js/jquery.min.js"></script>
		<script src="<%=basePath %>/layer_mobile/layer.js"></script>
		<script type="text/javascript">
			//loading
			  layer.open({
			    type: 2,
			    content: '加载中',
			    shadeClose: false,
			    shade: 'background-color: rgba(0,0,0,.6)'
			  });
			  var the_host = '<%=basePath %>';
			  var kid = '${kid}';
			  function sub(){
				  $.post(the_host+'wxCost_payStatus',{'kid':kid},function(data){
			  		  if(data.status==true && data.errorCode==0){
					  	  window.location.href=the_host+'wxCost_paySuccess?kid='+kid;
					  }else if(data.status==false){
					  	  window.location.href=the_host+'wxCost_paySuccess?kid='+kid;
					  }else{
					  	 	sub();
					  }
				  },'json');
			  }
			  sub();
		</script>
	</body>
</html>

