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
<link href="<%=path %>/page/css/select.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/page/js/select-ui.min.js"></script>
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
    $(function() {
        //选择框
        $(".select3").uedSelect({
            width : 150
        });
        /**
         * 弹出页面
         */
    });
</script>
</head>
<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">安防巡更</a></li>
    <li><a href="<%=path %>/patrolUserList">巡更人员管理</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    <form action="<%=path %>/patrolUser_list" method="post" id="searchForm">
    <ul class="seachform">
    	<li><label>巡更人员姓名:</label><input name="username" value="${patrolUser.username}" type="text" class="scinput" /></li>
		<li><label>巡更人员账号:</label><input name="jobNum" value="${patrolUser.jobNum}" type="text" class="scinput" /></li>
        <li><label>校区</label>
            <div class="vocation">
                <select class="select3" name="campusNum" >
                    <option value="-1">-请选择-</option>
                    <option value="0" <c:if test="${patrolUser.campusNum==0}"> selected</c:if>>主校区</option>
<%--
                    <option value="1" <c:if test="${patrolUser.campusNum==1}"> selected</c:if>>缙云校区</option>
--%>
                </select>
            </div>
        </li>
		<li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="搜索"/>
		<input type="hidden" name="pageSize" value="${pageSize}" />
		<input type="hidden" name="page" id="page" value="${page}"/>
		</li>
	</ul>
	</form>
    <div class="tools">
    
    	<ul class="toolbar">
    	<sec:authorize ifAnyGranted="patrolUser_add">
    	<li onclick="forWardUrl('<%=path %>/patrolUser_add');"><span><img src="<%=path %>/page/images/t01.png" /></span>添加</li>
    	</sec:authorize>
    	<sec:authorize ifAnyGranted="patrolUser_delete">
        <li onclick="bulkDelete('<%=path %>/patrolUser_delete','0');"><span><img src="<%=path %>/page/images/t03.png" /></span>批量删除</li>
        </sec:authorize>
        <sec:authorize ifAnyGranted="patrolUser_list">
        <li onclick="forWardUrl_param('<%=path %>/patrolUser_excelOut','0');"><span><img src="<%=path %>/page/images/t07.png" /></span>导出</li>
        </sec:authorize>
        </ul>
    
    </div>
    
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <th width="40px"><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>
        <th width="150px">巡更人员姓名</th>
        <th width="150px">巡更人员账号</th>
        <th width="150px">所属校区</th>
        <th width="150px">更新时间</th>
        <th width="150px">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${patrolUserPage.list}" var="d">
        <tr>
        <td><input type="checkbox" name="checkbox" value="${d.id}" id="checkbox_${d.id}" class="checkItem"/></td>
        <td>${d.username}</td>
        <td>${d.jobNum}</td>
        <td><c:choose>
            <c:when test="${d.campusNum == 0}">
                主校区
            </c:when>
            <%--<c:when test="${d.campusNum == 1}">
                缙云校区
            </c:when>--%>
        </c:choose></td>
        <td><fmt:formatDate value="${d.lastUpdateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
        <td>
        	<sec:authorize ifAnyGranted="patrolUser_edit">
        	<a href="<%=path %>/patrolUser_edit?id=${d.id}" class="tablelink">编辑人员信息</a> | 
        	</sec:authorize>
        	<sec:authorize ifAnyGranted="patrolUser_delete">
        	<a href="javascript:void(0);" onclick="bulkDelete('<%=path %>/patrolUser_delete','${d.id}');" class="tablelink"> 删除</a> 
        	</sec:authorize>
        </td>
        </tr> 
        </c:forEach>
        </tbody>
    </table>
    
   	<!-- 分页开始 -->
    <div class="pagin">
    	<div class="message">共<i class="blue">${patrolUserPage.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${patrolUserPage.currentPage}/${patrolUserPage.totalPage }&nbsp;</i>页</div>
        <ul class="paginList">
			<c:forEach items="${patrolUserPage.fristPage}" var="f">
				<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
			</c:forEach>
			<li class="paginItem current"><a href="javascript:void(0);">${patrolUserPage.currentPage}</a></li>
			<c:forEach items="${patrolUserPage.laPage}" var="l">
				<li class="paginItem"><a href="javascript:void(0);" rel="${l}">${l}</a></li>
			</c:forEach>
        </ul>
    </div>
    <!-- 分页结束 -->
    </div>
    <script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
    function forWardUrl_param(url){
        var ids = "";
        $("[name='checkbox']:not(:disabled):checked").each(function(){
            ids += $(this).val()+",";
        });
        if(ids.length>0){
            ids=ids.substr(0,ids.length-1);
        }
        $("#ids").val(ids);
        var username = $("input[name='username']").val();//姓名
        var jobNum = $("input[name='jobNum']").val();//姓名
        var param = "?now=" + new Date().getTime();
        if(username != undefined && username != null && username != "") {
            param += "&username=" + username;
        }
        if(jobNum != undefined && jobNum != null && jobNum != "") {
            param += "&jobNum=" + jobNum;
        }
        param += "&ids=" + ids;
        window.location.href= url+param;
    }

	</script>

</body>

</html>
