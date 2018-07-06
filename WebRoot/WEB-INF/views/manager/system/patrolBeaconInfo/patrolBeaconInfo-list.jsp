<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>巡更人员管理-列表</title>
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
	}if(method=='importSuccess'){
        layer.msg('导入成功', {icon: 1});
    }else{
	}
</script>
</head>
<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">安防巡更</a></li>
    <li><a href="#">蓝牙标签管理</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    <form action="<%=path %>/patrolBeaconInfo_list" method="post" id="searchForm">
    <ul class="seachform">
    	<li><label>UUID:</label><input name="uuid" value="${patrolBeaconInfo.uuid}" type="text" class="scinput" /></li>
		<li><label>major:</label><input name="major" value="${patrolBeaconInfo.major}" type="text" class="scinput" /></li>
		<li><label>minor:</label><input name="major" value="${patrolBeaconInfo.minor}" type="text" class="scinput" /></li>
		<li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="搜索"/>
		<input type="hidden" name="pageSize" value="${pageSize}" />
		<input type="hidden" name="page" id="page" value="${page}"/>
		</li>
	</ul>
	</form>
    <div class="tools">
    
    	<ul class="toolbar">
    	<li onclick="forWardUrl('<%=path %>/patrolBeaconInfo_add');"><span><img src="<%=path %>/page/images/t01.png" /></span>添加</li>
        <li onclick="bulkDelete('<%=path %>/patrolBeaconInfo_delete','0');"><span><img src="<%=path %>/page/images/t03.png" /></span>批量删除</li>
        <li onclick="forWardUrl('<%=path %>/patrolBeaconInfo_downloadTemplate','0');"><span><img src="<%=path %>/page/images/t04.png" /></span>下载导入模板</li>
        <li onclick="forWardUrl('<%=path %>/patrolBeaconInfo_excelOut','0');"><span><img src="<%=path %>/page/images/t04.png" /></span>批量导入</li>
        <li onclick="forWardUrl('<%=path %>/patrolBeaconInfo_excelOut','0');"><span><img src="<%=path %>/page/images/t04.png" /></span>导出</li>
        </ul>
    
    </div>
    
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <th width="40px"><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>
        <th width="150px">UUID</th>
        <th width="150px">major</th>
        <th width="150px">minor</th>
        <th width="150px">更新时间</th>
        <th width="150px">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${pageBean.list}" var="d">
        <tr>
        <td><input type="checkbox" name="checkbox" value="${d.beaconId}" id="checkbox_${d.beaconId}" class="checkItem"/></td>
        <td>${d.uuid}</td>
        <td>${d.major}</td>
        <td>${d.minor}</td>
        <td><fmt:formatDate value="${d.updateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
        <td>
        	<a href="<%=path %>/patrolBeaconInfo_edit?id=${d.beaconId}" class="tablelink">编辑</a> |
        	<a href="javascript:void(0);" onclick="bulkDelete('<%=path %>/patrolBeaconInfo_delete','${d.beaconId}');" class="tablelink"> 删除</a>
        </td>
        </tr> 
        </c:forEach>
        </tbody>
    </table>
    
   	<!-- 分页开始 -->
    <div class="pagin">
    	<div class="message">共<i class="blue">${pageBean.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${pageBean.currentPage}/${pageBean.totalPage }&nbsp;</i>页</div>
        <ul class="paginList">
			<c:forEach items="${pageBean.fristPage}" var="f">
				<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
			</c:forEach>
			<li class="paginItem current"><a href="javascript:void(0);">${pageBean.currentPage}</a></li>
			<c:forEach items="${pageBean.laPage}" var="l">
				<li class="paginItem"><a href="javascript:void(0);" rel="${l}">${l}</a></li>
			</c:forEach>
        </ul>
    </div>
    <!-- 分页结束 -->
    </div>
    <script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
	</script>

</body>

</html>
