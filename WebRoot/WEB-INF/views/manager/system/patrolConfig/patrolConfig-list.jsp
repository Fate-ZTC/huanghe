<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>配置信息管理</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/page/js/common.js"></script>
<script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>
<script type="text/javascript">
	var method = '${method }';
	if(method=='addSuccess'){
		layer.msg('添加成功', {icon: 1});
	}else if(method=='editSuccess'){
		layer.msg('编辑成功', {icon: 1});
	}else if(method=='deleteSuccess'){
		layer.msg('删除成功', {icon: 1});
	}else{
	}
</script>
</head>
<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">安防巡更</a></li>
    <li><a href="<%=path %>/patrolConfig_list">配置信息管理</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    <form action="<%=path %>/patrolConfig_edit" method="post" id="addForm">
    <table class="tablelist">
    	<input type="hidden" name="id" value="${patrolConfig.id }"/>
        <ul>
        	<li>参数配置</li>
        </ul>
        <ul class="seachform">
        	<li>
        	位置数据动态上传周期：每<input name="uploadTime" type="text" value="${patrolConfig.uploadTime }"/>*(秒) 设置后,系统每${patrolConfig.uploadTime }秒获取一次巡更人员的位置信息
        	</li>
        </ul>
         <ul class="seachform">
        	<li>
        	管理端更新人员位置周期：每<input name="refreshTime" type="text" value="${patrolConfig.refreshTime }"/>*(秒) 设置后,系统每${patrolConfig.refreshTime }秒更新一次巡更人员的位置信息
        	</li>
        </ul>
         <ul class="seachform">
        	<li>
        	允许到达巡更区域时长:<input name="startPatrolTime" type="text" value="${patrolConfig.startPatrolTime }"/>*(分钟) 设置后，巡更人员在点击开始巡更后，允许在${patrolConfig.startPatrolTime }分钟后到达巡更区域
        	</li>
        </ul>
         <ul class="seachform">
        	<li>
        	允许巡更人员位置不变时长:<input name="lazyTime" type="text" value="${patrolConfig.lazyTime }"/>*(分钟) 设置后，巡更人员在${patrolConfig.lazyTime }分钟内位置没有变动，系统将给管理员发送预警提醒
        	</li>
        </ul>
         <ul class="seachform">
        	<li>
        	允许巡更人员离开巡更范围时长:<input name="leaveRegionTime" type="text" value="${patrolConfig.leaveRegionTime }"/>*(分钟) 设置后，巡更人员在离开巡更范围${patrolConfig.leaveRegionTime }分钟后，系统将给管理员发送预警提醒
        	</li>
        </ul>
         <ul class="seachform">
        	<li>
        	允许巡更人员离开巡更范围:<input name="leaveRegionDistance" type="text" value="${patrolConfig.leaveRegionDistance }"/>*(米) 设置后,系统将允许巡更人员离开巡逻范围${patrolConfig.leaveRegionDistance }米
        	</li>
        </ul>
        <ul>
        <li><label>&nbsp;</label><input name="" type="submit" class="btn" value="确认修改"/><input type="reset"  value="重置"/> </li>
        </ul>
    </table>
    </form>
    </div>
    <script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
	</script>

</body>

</html>
