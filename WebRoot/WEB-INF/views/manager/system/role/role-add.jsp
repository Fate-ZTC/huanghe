<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
Date date = new Date();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>角色-添加</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<link rel="stylesheet" href="<%=path %>/page/validator/jquery.validator.css" />
<script type="text/javascript" src="<%=path %>/page/validator/jquery.validator.js"></script>
<script type="text/javascript" src="<%=path %>/page/validator/local/zh-CN.js"></script>
<script type="text/javascript" src="<%=path %>/page/laydate/laydate.js"></script>
<link rel="stylesheet" href="<%=path %>/page/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css"/>
<script type="text/javascript" src="<%=path %>/page/ztree/jquery.ztree.all-3.5.min.js"></script>

<script type="text/javascript">
$(function(){
   	//选择框
   	laydate.skin('molv');
 });
</script>
<style type="text/css">
#menuId{
	z-index: 2;
	_top: 6px;
	cursor: pointer;
	height: 32px;
	border: 1px solid #cccccc;
}
.level2 span,.level1 span,.level0 span{
	display: inline;
}
</style>
</head>

<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">系统管理</a></li>
    <li><a href="<%=path %>/role_list">角色管理</a></li>
    <li><a href="#">添加</a></li>
    </ul>
    </div>
    <div class="formbody">
    <div class="formtitle"><span>添加角色</span></div>
    <form action="<%=path %>/role_add" method="post" id="addForm" data-validator-option="{stopOnError:true, timely:1 ,theme:'yellow_right'}">
    <input type="hidden" name="roleType" value="1" />
    <input type="hidden" name="method" value="add" />
    <input type="hidden" name="resourcesIds" id="resourcesIds"/>
    <ul class="forminfo">
    <li><label>名称<b>*</b></label><input name="name" type="text" class="dfinput" data-rule="名称:required;length[~50]"/></li>
    <li><label>编码<b>*</b></label><input name="enname" type="text" class="dfinput" data-rule="编码:required;length[~50]"/></li>
    <li><label>核心模块<b>*</b></label>
    	<cite><input name="iscore" type="radio" value="0" data-rule="核心模块:checked;" />否&nbsp;&nbsp;&nbsp;&nbsp;
    	<input name="iscore" type="radio" value="1" checked="checked"/>是
    	</cite>
    </li>
    <li><label>可用<b>*</b></label>
    	<cite><input name="enable" type="radio" value="0" data-rule="可用:checked;" />否&nbsp;&nbsp;&nbsp;&nbsp;
    	<input name="enable" type="radio" value="1" checked="checked"/>是
    	</cite>
    </li>
    <li><label>角色权限<b>*</b></label>
   		<div style="width:270px;height:300px;text-align:left; float: left; overflow: auto; border: 1px solid #ccc;margin-bottom: 10px">
			<ul id="resourcesTree" class="ztree"></ul>
		</div>
    </li>
    <li><label>序列<b>*</b></label><input name="orderid" type="text" class="dfinput" value="999" data-rule="序列:required;integer[+0];"/></li>
    <li><label>添加时间<b>*</b></label><input name="" type="text" style="height: 34px;" value="<fmt:formatDate value="<%=date %>" pattern="yyyy-MM-dd HH:mm:ss"/>"
    	data-rule="添加时间:required;" class="laydate-icon" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})"/></li>
    <li><label>&nbsp;</label><input name="" type="button" class="btn" value="确认添加" onclick="_submit()"/></li>
    </ul>
    </form>
    
    </div>
<script type="text/javascript">
var host ='<%=path%>';
var setting = {
	check: {
		enable: true
	},
	data: {
		simpleData: {
			enable: true
		}
	}
};
$(function() {
	$(".roleType").change(function() {
		getTreeData($("input[name='role.roleType']:checked").val());
	});
	getTreeData(1);
});
var _submit = function(){
	var resourcesIds = "";
	var zTree = $.fn.zTree.getZTreeObj("resourcesTree");
	var nodes = zTree.getCheckedNodes(true);
	for(var i=0;i<nodes.length;i++){
		if(nodes[i].pId != null && nodes[i].pId != '0' && nodes[i].id.substring(0,2) != 'm_'){
			resourcesIds+=nodes[i].id+",";
		}
	}
	$("#resourcesIds").val(resourcesIds);
	$('#addForm').submit();
}
function getTreeData(id){
	$.ajax({
		type: 'POST',
		url: host + "/role_getRoleResources" ,
		data: {
			'ids': id
		},
		dataType: 'json',
		success: function (data) {
			$("#resourcesTree").empty();
			var zTree = $.fn.zTree.init($("#resourcesTree"), setting, data);
			zTree.setting.check.chkboxType = { "Y" : "ps", "N" : "ps" };
		},
		error: function (XMLResponse) {
		}
	});
}
</script>

</body>

</html>
