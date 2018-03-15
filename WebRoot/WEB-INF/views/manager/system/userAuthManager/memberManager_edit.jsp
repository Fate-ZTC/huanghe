<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>会员用户管理-编辑</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<link rel="stylesheet" href="<%=path %>/page/validator/jquery.validator.css" />
<script type="text/javascript" src="<%=path %>/page/validator/jquery.validator.js"></script>
<script type="text/javascript" src="<%=path %>/page/validator/local/zh-CN.js"></script>
<script type="text/javascript" src="<%=path %>/page/laydate/laydate.js"></script>
<link rel="stylesheet" href="<%=path %>/page/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css"/>
<script type="text/javascript" src="<%=path %>/page/ztree/jquery.ztree.all-3.5.min.js"></script>
<style type="text/css">
.menu{
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
<script type="text/javascript">
$(function(){
   	//选择框
   	laydate.skin('molv');
 });
</script>
</head>

<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">应用管理</a></li>
    <li><a href="<%=path %>/memberManager_list">会员用户管理</a></li>
    <li><a href="#">编辑</a></li>
    </ul>
    </div>
    <div class="formbody">
    <div class="formtitle"><span>编辑会员用户</span></div>
    <form action="<%=path %>/memberManager_edit" method="post" id="addForm" data-validator-option="{stopOnError:true, timely:1 ,theme:'yellow_right'}">
    <input type="hidden" name="method" value="edit" />
    <input type="hidden" name="enablRegionIds" id="enablRegionIds"/>
    <ul class="forminfo">
    <li><label>账号</label><input name="mobile" type="text" value="${users.mobile}" class="dfinput" readonly="readonly"/></li>
    <li><label>性别</label>
    <c:if test="${users.sex!=2}">
    <input name="sex" type="radio" value="1" checked="checked"/>男&nbsp;&nbsp;&nbsp;
    <input name="sex" type="radio" value="2"/>女
    </c:if>
    <c:if test="${users.sex==2}">
    <input name="sex" type="radio" value="1"/>男&nbsp;&nbsp;&nbsp;
    <input name="sex" type="radio" value="2" checked="checked"/>女
    </c:if>
    </li>
    <li><label>所在城市</label><input name="city" type="text" class="dfinput" value="${users.city}"/></li>
    <li><label>省份</label><input name="province" type="text" class="dfinput" value="${users.province}" /></li>
    <li><label>国家</label><input name="country" type="text" class="dfinput" value="${users.country}"/></li>
    <li><label>&nbsp;</label><input name="" type="button" class="btn" value="确认编辑" onclick="_submit()"/></li>
    </ul>
    </form>
    
    </div>
<script type="text/javascript">
$(function(){

});
var host ='<%=path%>';
var _submit = function(){
	$('#addForm').submit();
}
</script>
</body>

</html>
