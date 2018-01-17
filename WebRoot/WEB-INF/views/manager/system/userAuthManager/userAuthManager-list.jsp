<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>部门管理-列表</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/page/js/common.js"></script>
<script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>
	<script src="<%=path %>/page/chart/laydate/laydate.js"></script>
<script type="text/javascript">
	var method = '${method }';
	if(method=='addSuccess'){
		layer.msg('添加成功', {icon: 1});
	}else if(method=='editSuccess'){
		layer.msg('编辑成功', {icon: 1});
	}else if(method=='deleteSuccess'){
		layer.msg('删除成功', {icon: 1});
	}else if(method=='drivingAuthSuccess'){
		layer.msg('提交成功', {icon: 1});
	}else if(method=='driverAuthSuccess'){
		layer.msg('提交成功', {icon: 1});
	}else{
	
	}
</script>
</head>
<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">应用管理</a></li>
    <li><a href="<%=path %>/userAuthManager_list">信息认证审核</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    <form action="<%=path %>/userAuthManager_list" method="post" id="searchForm">
    <ul class="seachform">
    	<li><label>用户账号</label><input name="mobile" value="${mobile}" type="text" class="scinput" /></li>
		<li>
			<label>开始时间：</label>
			<input type="text" class="laydate-icon scinput" id="start-time" name="startTime" value="${startTime}" style="width: 137px;height:32px;" />
		</li>
		<li>
			<label>结束时间：</label>
			<input type="text" class="laydate-icon scinput" id="end-time" name="endTime" value="${endTime}" style="width: 137px;height:32px;" />
		</li>
		<li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="查询"/>
		<input type="hidden" name="pageSize" value="${pageSize}" />
		<input type="hidden" name="page" id="page" value="${page}"/>
		</li>
	</ul>
	</form>
    <div class="tools">
    
    	<ul class="toolbar">
<!--    	<sec:authorize ifAnyGranted="userAuthManager_delete">-->
<!--        <li onclick="bulkDelete('<%=path %>/userAuthManager_delete','0');"><span><img src="<%=path %>/page/images/t03.png" /></span>删除</li>-->
<!--        </sec:authorize>-->
        </ul>
    
    </div>
    
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <th width="40px"><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>
        <th width="150px">用户账号</th>
        <th width="">更新时间</th>
        <th width="150px">驾驶证审核状态</th>
        <th width="150px">行驶证审核状态</th>
        <th width="200px">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${userAuthPage.list}" var="u">
        <tr>
        <td><input type="checkbox" name="checkbox" value="${d.departmentid}" id="checkbox_${d.departmentid}" class="checkItem"/></td>
        <td>${u.mobile}</td>
        <td><fmt:formatDate value="${u.posttime}" pattern="yyyy-MM-dd HH:mm"/></td> 
        <td>
        
        <c:if test="${u.drivingStauts==0}"><font color="red">待审核</font></c:if>
        <c:if test="${u.drivingStauts==1}">通过</c:if>
        <c:if test="${u.drivingStauts==-1}">未通过</c:if>
        <c:if test="${u.drivingStauts==null}">未上传</c:if>
        </td>
        <td>${u.driverStatus}</td>
        <td>
        	<sec:authorize ifAnyGranted="userAuthManager_auth">
        	<a href="<%=path %>/userAuthManager_auth?id=${u.mobile}" class="tablelink">驾驶证审核</a> | 
        	<a href="<%=path %>/userAuthManager_drivingAuth?id=${u.mobile}" class="tablelink">行驶证审核</a>
        	</sec:authorize>
<!--        	<sec:authorize ifAnyGranted="userAuthManager_delete">-->
<!--        	<a href="javascript:void(0);" onclick="bulkDelete('<%=path %>/userAuthManager_delete','${u.mobile}');" class="tablelink"> 删除</a> -->
<!--        	</sec:authorize>-->
        </td>
        </tr> 
        </c:forEach>
        </tbody>
    </table>
    
   	<!-- 分页开始 -->
    <div class="pagin">
    	<div class="message">共<i class="blue">${userAuthPage.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${userAuthPage.currentPage}/${userAuthPage.totalPage }&nbsp;</i>页</div>
        <ul class="paginList">
			<c:forEach items="${userAuthPage.fristPage}" var="f">
				<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
			</c:forEach>
			<li class="paginItem current"><a href="javascript:void(0);">${userAuthPage.currentPage}</a></li>
			<c:forEach items="${userAuthPage.laPage}" var="l">
				<li class="paginItem"><a href="javascript:void(0);" rel="${l}">${l}</a></li>
			</c:forEach>
        </ul>
    </div>
    <!-- 分页结束 -->
    </div>
    <script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
	
	var start_time = {
			  elem: '#start-time',
			  format: 'YYYY-MM-DD hh:mm:ss',
			  max: '2099-06-16',
			  istime: true,
			  istoday: false,
			  choose: function(datas){
			     end_time.min = datas;
			     $("#start-time").val(datas);
			  }
			};
			laydate(start_time);
			
			//结束时间
			var end_time = {
			  elem: '#end-time',
			  format: 'YYYY-MM-DD hh:mm:ss',
			  max: '2099-06-16',
			  istime: true,
			  istoday: false,
			  choose: function(datas){
			    start_time.max = datas;
			    $("#end-time").val(datas);
			  }
			};
			laydate(end_time);
	</script>

</body>

</html>
