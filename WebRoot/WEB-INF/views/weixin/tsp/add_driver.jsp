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
		<title>添加驾驶证</title>
		<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta name="msapplication-tap-highlight" content="no" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>tsp/css/common.css"/>
		<link rel="stylesheet" type="text/css" href="<%=basePath%>tsp/css/driving.css"/>
		<script src="<%=basePath%>tsp/js/jquery.min.js"></script>
		<script src="<%=basePath%>layer_mobile/layer.js"></script>
	</head>
	<body>
		<div class="jiucheng-box add-driving-box f14">
			<form action="<%=basePath%>uploadDriver" method="post" enctype="multipart/form-data" id="fileForm">
			<div class="driver-license-box license-submit-box">
				<div class="driver-license-upload">
					<input type="hidden" name="mobile" value="${mobile}"/>
					<div class="reupload-box">
						<span>重新上传</span>
						<input type="file" id="reuploadBtn" />
					</div>
					<div class="license-upload">
						<p class="driverLicense">添加驾驶证</p>
						<input type="file" class="licenseUploadBtn" id="licenseUploadBtn"/>
					</div>
					<div class="read-picture-box">
						<img src="" class="read-picture" alt="" id="ImgPr" />
					</div>
				</div>
				
				<button class="submit-license" type="button" onclick="sub()" style="margin-top:1.2rem;">提交</button>
			</div>
			</form>
		</div>
		
	<script>
		$(".licenseUploadBtn,#reuploadBtn").change(function(){
			if($(this).attr("id")=="licenseUploadBtn"){
				$(this).attr("name","fileDriver");
				$('#reuploadBtn').attr("name","");
			}else{
				$(this).attr("name","fileDriver");
				$('#licenseUploadBtn').attr("name","");
			}
			var $file = $(this);
	        var fileObj = $file[0];
	        var windowURL = window.URL || window.webkitURL;
	        var dataURL;
	        $(".read-picture-box").show();
	        var $img = $("#ImgPr");
	        if(fileObj && fileObj.files && fileObj.files[0]){
		        dataURL = windowURL.createObjectURL(fileObj.files[0]);
		        $img.attr('src',dataURL);
		        
		        //图片上传成功后执行操作
		        $(".license-upload").hide();
		        $(".reupload-box").show();
	        }else{
		        dataURL = $file.val();
		        var imgObj = document.getElementById("ImgPr");
		        imgObj.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
		        imgObj.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = dataURL;
          }
		});
		function sub(){
			if($('input[name="fileDriver"]').val()!=undefined){
			 	 layer.open({
				    type: 2,
				    content: '加载中',
				    shadeClose: false,
				  });
				$('#fileForm').submit();		
			}else{
				  layer.open({
				    content: '请选择添加驾驶证'
				    ,skin: 'msg'
				    ,time: 2 //2秒后自动关闭
				  });
			}
		}
	</script>
	</body>
</html>
