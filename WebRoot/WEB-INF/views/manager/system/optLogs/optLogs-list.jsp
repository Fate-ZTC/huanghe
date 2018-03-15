<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>系统日志-列表</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<link href="<%=path %>/page/css/select.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/select-ui.min.js"></script>
<script type="text/javascript" src="<%=path %>/page/js/common.js"></script>
<script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>
<script type="text/javascript">
   	var method = '${method }';
	if(method=='deleteSuccess'){
		layer.msg('删除成功', {icon: 1});
	}else{
	}
</script>
</head>
<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">系统管理</a></li>
    <li><a href="<%=path %>/optLogs_list">系统日志</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    <form action="<%=path %>/optLogs_list" method="post" id="searchForm">
    <ul class="seachform">
		<li><input name="keywords" value="${keywords}" type="text" class="scinput" /></li>
		<li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="查询"/>
		<input type="hidden" name="pageSize" value="${pageSize}" />
		<input type="hidden" name="page" id="page" value="${page}"/>
		</li>
	</ul>
	</form>
    <div class="tools">
    
    	<ul class="toolbar">
    	<sec:authorize ifAnyGranted="optLogs_delete">
        <li onclick="bulkDelete('<%=path %>/optLogs_delete','0');"><span><img src="<%=path %>/page/images/t03.png" /></span>删除</li>
    	</sec:authorize>	
    		
        </ul>
    
    </div>
    
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <th width="40px"><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>
        <th width="150px">用户</th>
        <th width="150px">模块</th>
        <th>描述</th>
        <th width="150px">操作时间</th>
        <th width="70px">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${optLogsPage.list}" var="log">
        <tr>
        <td><input type="checkbox" name="checkbox" value="${log.logId}" id="checkbox_${log.logId}" class="checkItem"/></td>
        <td>${log.username}</td>
        <td>${log.fromModel}</td>
        <td>${log.description}</td>
        <td><fmt:formatDate value="${log.createTime}" pattern="yyyy-MM-dd HH:mm"/></td> 
        <td>
        <sec:authorize ifAnyGranted="optLogs_delete">
        <a href="javascript:void(0);" onclick="bulkDelete('<%=path %>/optLogs_delete','${log.logId}');" class="tablelink"> 删除</a>
        </sec:authorize>
        </td>
        </tr> 
        </c:forEach>
        </tbody>
    </table>
    
      
   <!-- 分页开始 -->
    <div class="pagin">
    	<div class="message">共<i class="blue">${optLogsPage.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${optLogsPage.currentPage}/${optLogsPage.totalPage }&nbsp;</i>页</div>
        <ul class="paginList">
			<c:forEach items="${optLogsPage.fristPage}" var="f">
				<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
			</c:forEach>
			<li class="paginItem current"><a href="javascript:void(0);">${optLogsPage.currentPage}</a></li>
			<c:forEach items="${optLogsPage.laPage}" var="l">
				<li class="paginItem"><a href="javascript:void(0);" rel="${l}">${l}</a></li>
			</c:forEach>
        </ul>
    </div>
    <!-- 分页结束 -->
   
    </div>
    
    <script type="text/javascript">
    $(function(){
    	//选择框
	    $(".select3").uedSelect({
			width : 152
		});
    });
	$('.tablelist tbody tr:odd').addClass('odd');
	</script>

</body>

</html>
