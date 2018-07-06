<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="taglibs.jsp" %>
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

<script type="text/javascript">
$(function(){	
	//导航切换
	$(".menuson .header").click(function(){
		var $parent = $(this).parent();
		$(".menuson>li.active").not($parent).removeClass("active open").find('.sub-menus').hide();
		
		$parent.addClass("active");
		if(!!$(this).next('.sub-menus').size()){
			if($parent.hasClass("open")){
				$parent.removeClass("open").find('.sub-menus').hide();
			}else{
				$parent.addClass("open").find('.sub-menus').show();	
			}
			
			
		}
	});
	//二级菜单点击
	$('.menuson li').click(function(e) {
        $(".menuson li.active").removeClass("active")
		$(this).addClass("active");
    });
	
	
	$('.title').click(function(){
		var $ul = $(this).next('ul');
		$('dd').find('.menuson').slideUp();
		if($ul.is(':visible')){
			$(this).next('.menuson').slideUp();
		}else{
			$(this).next('.menuson').slideDown();
		}
	});
})	
</script>


</head>

<body style="background:#f0f9fd">
	<div class="lefttop"><span></span>系统菜单</div>
    <dl class="leftmenu">
	    <sec:authorize ifAnyGranted="userAuthManager_list,userAuthManager_auth,userAuthManager_delete">
	    <!-- 应用管理START -->
	    <%--<dd>--%>
	    <%--<div class="title">--%>
	    <%--<span><img src="<%=path %>/page/images/leftico01.png" /></span>应用管理--%>
	    <%--</div>--%>
	    	<%--<ul class="menuson">--%>
	    	<%--<sec:authorize ifAnyGranted="memberManager_list,userAuthManager_auth,userAuthManager_delete">--%>
	        <%--<li><cite></cite><a href="<%=path %>/memberManager_list" target="rightFrame">会员用户管理</a><i></i></li>--%>
	        <%--</sec:authorize>--%>
	    	<%--<sec:authorize ifAnyGranted="userAuthManager_list,userAuthManager_auth,userAuthManager_delete">--%>
	        <%--<li><cite></cite><a href="<%=path %>/userAuthManager_list" target="rightFrame">信息认证审核</a><i></i></li>--%>
	        <%--</sec:authorize>--%>
	        <%--</ul>    --%>
	    <%--</dd>--%>
	    <!-- 应用管理END -->
	    </sec:authorize>
	    <sec:authorize ifAnyGranted="manager_list,manager_add,manager_edit,manager_delete,manager_resetPassword
	    					,department_list,department_add,department_edit,department_delete
	    					,role_list,role_add,role_edit,role_delete
	    					,resources_list,resources_add,resources_edit,resources_delete
	    					,enumerateDetail_list,enumerateDetai_add,enumerateDetai_edit,enumerateDetai_delete
	    					,optLogs_list,optLogs_delete,
	    					sysconfig_edit">
	    <!-- 系统管理START -->
	    <dd>
	    <div class="title">
	    <span><img src="<%=path %>/page/images/leftico01.png" /></span>系统管理
	    </div>
	    	<ul class="menuson">
	    	<sec:authorize ifAnyGranted="manager_list,manager_add,manager_edit,manager_delete,manager_resetPassword">
	        <li><cite></cite><a href="<%=path %>/manager_list" target="rightFrame">用户管理</a><i></i></li>
	        </sec:authorize>
	    	<sec:authorize ifAnyGranted="department_list,department_add,department_edit,department_delete">
	        <li><cite></cite><a href="<%=path %>/department_list" target="rightFrame">部门管理</a><i></i></li>
	        </sec:authorize>
	        <sec:authorize ifAnyGranted="role_list,role_add,role_edit,role_delete">
			<li><cite></cite><a href="<%=path %>/role_list" target="rightFrame">角色管理</a><i></i></li>
			</sec:authorize>
			<sec:authorize ifAnyGranted="resources_list,resources_add,resources_edit,resources_delete">
	        <li><cite></cite><a href="<%=path %>/resources_list" target="rightFrame">资源权限管理</a><i></i></li>
	        </sec:authorize>
	        <sec:authorize ifAnyGranted="optLogs_list,optLogs_delete">
	        <li><cite></cite><a href="<%=path %>/optLogs_list" target="rightFrame">系统日志</a><i></i></li>
	        </sec:authorize>
	        <sec:authorize ifAnyGranted="sysconfig_edit">
	        <li><cite></cite><a href="<%=path %>/sysconfig_edit" target="rightFrame">系统配置</a><i></i></li>
	        </sec:authorize>
	        </ul>    
	    </dd>
	    <!-- 系统管理END -->
	    </sec:authorize>
	    <sec:authorize ifAnyGranted="managerInfo_edit,managerInfo_password">
	    <!-- 个人中心START -->
	    <dd>
	    <div class="title">
	    <span><img src="<%=path %>/page/images/leftico01.png" /></span>个人中心
	    </div>
	    	<ul class="menuson">
	    	<sec:authorize ifAnyGranted="managerInfo_edit">
	        <li><cite></cite><a href="<%=path %>/managerInfo_edit" target="rightFrame">个人资料</a><i></i></li>
	    	</sec:authorize>
	    	<sec:authorize ifAnyGranted="managerInfo_password">
	        <li><cite></cite><a href="<%=path %>/managerInfo_password" target="rightFrame">修改密码</a><i></i></li>
	    	</sec:authorize>
	    	
	        </ul>    
	    </dd>
	    <!-- 个人中心END -->
	    </sec:authorize>
	    <!-- 安防巡查管理START -->
	    <sec:authorize ifAnyGranted="patrolUserRegionList">
	    <dd>
	    <div class="title">
	    <span><img src="<%=path %>/page/images/leftico01.png" /></span>安防巡更
	    </div>
	    	<ul class="menuson">
	    	<sec:authorize ifAnyGranted="patrolUserRegionList">
	        <li><cite></cite><a href="<%=path %>/patrolUserRegionList" target="rightFrame">巡更记录管理</a><i></i></li>
	    	</sec:authorize>
	    	<sec:authorize ifAnyGranted="patrolRegList">
	        <li><cite></cite><a href="<%=path %>/patrolRegList" target="rightFrame">巡更区域管理</a><i></i></li>
	    	</sec:authorize>
	    	<sec:authorize ifAnyGranted="patrolEmergency_list,patrolEmergency_delete">
	        <li><cite></cite><a href="<%=path %>/patrolEmergency_list" target="rightFrame">突发事件管理</a><i></i></li>
	    	</sec:authorize>
	    	<sec:authorize ifAnyGranted="patrolUser_list,patrolUser_edit,patrolUser_add,patrolUser_delete">
	        <li><cite></cite><a href="<%=path %>/patrolUser_list" target="rightFrame">巡更人员管理</a><i></i></li>
	    	</sec:authorize>
	    	<sec:authorize ifAnyGranted="patrolConfig_list,patrolConfig_edit">
	        <li><cite></cite><a href="<%=path %>/patrolConfig_list" target="rightFrame">巡查参数配置</a><i></i></li>
	    	</sec:authorize>
				<li><cite></cite><a href="<%=path %>/patrolBeaconInfo_list" target="rightFrame">蓝牙标签管理</a><i></i></li>
	        </ul>    
	    </dd>
<!-- 	    月租管理END -->
 		</sec:authorize>
	    <sec:authorize ifAnyGranted="monthlyHandleManage,monthlyRenewManage">
	    <!-- 消防巡查管理START -->
	    <dd>
	    <div class="title">
	    <span><img src="<%=path %>/page/images/leftico01.png" /></span>消防巡查
	    </div>
	    	<ul class="menuson">
	    	<sec:authorize ifAnyGranted="firePatrolUserList,firePatrolUser_add,firePatrolUser_edit,firePatrolUser_delete">
	        <li><cite></cite><a href="<%=path %>/firePatrolUserList" target="rightFrame">消防人员管理</a><i></i></li>
	    	</sec:authorize>
	    	<sec:authorize ifAnyGranted="firePatrolInfo_list">
	        <li><cite></cite><a href="<%=path %>/firePatrolInfo_list" target="rightFrame">巡查记录</a><i></i></li>
	    	</sec:authorize>
	    	<sec:authorize ifAnyGranted="firePatrolConfig_list,firePatrolConfig_edit">
	        <li><cite></cite><a href="<%=path %>/firePatrolConfig_list" target="rightFrame">巡查参数配置</a><i></i></li>
	    	</sec:authorize>
	    	<%--<sec:authorize ifAnyGranted="firePatrolExc_list,firePatrolExc_add,firePatrolExc_edit,firePatrolExc_delete">--%>
	        <%--<li><cite></cite><a href="<%=path %>/firePatrolExc_list" target="rightFrame">异常描述</a><i></i></li>--%>
	    	<%--</sec:authorize>--%>
			<sec:authorize ifAnyGranted="firePatrolExc_list,firePatrolExc_add,firePatrolExc_edit,firePatrolExc_delete">
				<li><cite></cite><a href="<%=path %>/firePatrolExc_list" target="rightFrame">术语管理</a><i></i></li>
			</sec:authorize>
	    	
	        </ul>    
	    </dd>
	    <!-- 消防巡查管理END -->
	    </sec:authorize>
    </dl>
    
</body>
</html>
