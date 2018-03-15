<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>部门-配置</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<link rel="stylesheet" href="<%=path %>/page/validator/jquery.validator.css" />
<script type="text/javascript" src="<%=path %>/page/validator/jquery.validator.js"></script>
<script type="text/javascript" src="<%=path %>/page/validator/local/zh-CN.js"></script>
<script type="text/javascript" src="<%=path %>/page/js/upload.js"></script>
<script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>
<style>
.uploadLogo-box{display: block;padding: 0;margin: 0;overflow: hidden; }
.upload-btn{position: relative;display: inline-block;border: 1px solid #cccccc;padding: 1px;}
.not-found-img{font-size: 48px;font-weight: bold;text-align: center;color: #ccc;}
.forminfo li label{width: 120px}
</style>
<script type="text/javascript">
var method = '${method}';
if(method=='editSuccess'){
	layer.msg('设置成功', {icon: 1});
}
</script>
</head>

<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">系统管理</a></li>
    <li><a href="<%=path %>/system/sysconfig_edit">系统配置</a></li>
    </ul>
    </div>
    <div class="formbody">
     
    
    <form action="<%=path %>/system/sysconfig_edit" method="post" id="addForm" data-validator-option="{stopOnError:true, timely:1 ,theme:'yellow_right'}">
    <input type="hidden" name="method" value="edit" />
    <!-- 管理系统Start -->
    <div class="formtitle"><span>管理系统配置</span></div>
    <ul class="forminfo">
	    <li><label>管理系统Logo<b>*</b></label>
	    <div class="uploadLogo-box">
	    	<div class="img-list" style="margin-bottom: 10px">
	    		<div class="upload-btn uploadAdminLogo" style="width: 310px;height: 80px;">
	    			<s:if test="%{sysconfigMap['adminLogo'] != null && sysconfigMap['adminLogo'] != ''}">
	    			<img  class="logo-img" src="${sysconfigMap['adminLogo']}"  />
	    			</s:if>
	    			<s:else>
	    			<div class="not-found-img" style="height: 80px;line-height: 70px;width: 310px;">+</div>
	    			</s:else>
		    	</div>
	    	</div>
	    	<i>图片尺寸：310px * 80px</i>
	    </div>
	    <input name="adminLogo" value="${sysconfigMap['adminLogo']}" type="hidden" class="adminLogo"/>
	    </li>
	    <li><label>管理系统名称<b>*</b></label><input name="adminTitle" value="${sysconfigMap['adminTitle']}" type="text" class="dfinput" data-rule="管理系统名称:required;"/></li>
	    <li><label>日志保留天数<b>*</b></label><input name="clearLogsTime" value="${sysconfigMap['clearLogsTime']}" type="text" class="dfinput" data-rule="日志保留天数:required;integer;"/></li>
    </ul>
    <!-- 管理系统END -->
    
    
    <ul  class="forminfo">
    <li><label>&nbsp;</label><input  type="submit" class="btn" value="确认保存"/></li>
    </ul>
    </form>
    </div>
	<script type="text/javascript">
	var host = '<%=path %>';
	var loadding = null;
	function showLoadding(){
		loadding = layer.load(1, {
		  shade: [0.1,'#fff']
		});
	}
	function closeLoadding(){
		layer.close(loadding);
	}
	$(".uploadAdminLogo").upload({
        action: host + "/system/upload",
        fileName: "attached",
        params: {"dir":"image","w":"310","h":"80"},
        dataType:'JSON',
        accept: ".jpg,.png,.gif,.jpeg,.bmp",
        complete: function(res){
        	if(res.status){
	        	var img = "<img class=\"logo-img\" src=\"" + res.path +"\" />";
				$(".adminLogo").val(res.path);
	        	$(".uploadAdminLogo .not-found-img").remove();
				$(".uploadAdminLogo .logo-img").remove();
				$(".uploadAdminLogo").prepend(img);
        	}else{
        		layer.msg(res.errorContent, function(){});
        	}
        	closeLoadding();
        },
        submit: function () {
            showLoadding();
        }
    });
	</script>
</body>

</html>
