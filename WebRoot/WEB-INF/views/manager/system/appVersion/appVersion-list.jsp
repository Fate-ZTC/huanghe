<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@include file="../../taglibs.jsp" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>APP版本管理</title>
    <link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
    <link href="<%=path %>/page/css/select.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="<%=path %>/page/js/select-ui.min.js"></script>
    <script type="text/javascript" src="<%=path %>/page/js/common.js"></script>
    <script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>
    <script type="text/javascript">
        var method = '${method }';
        if (method == 'addSuccess') {
            layer.msg('添加成功', {icon: 1});
        } else if (method == 'editSuccess') {
            layer.msg('编辑成功', {icon: 1});
        } else if (method == 'deleteSuccess') {
            layer.msg('删除成功', {icon: 1});
        } else {
        }
    </script>
</head>
<body>

<div class="place">
    <span>位置：</span>
    <ul class="placeul">
        <li><a href="#">APP管理</a></li>
        <li><a href="<%=path %>/firePatrolUserList">APP版本管理</a></li>
    </ul>
</div>

<div class="rightinfo">
    <form action="<%=path %>/appManagerList" method="post" id="searchForm">
        <ul class="seachform">
            <li><label>APP类型</label>
            <div class="vocation">
                <select class="select3" name="type" >
                    <option value="-1">-请选择-</option>
                    <option value="1" ${type eq 1?"selected":""}>巡更巡查管理端</option>
                    <option value="2" ${type eq 2?"selected":""}>安防巡更使用端</option>
                    <option value="3" ${type eq 3?"selected":""}>消防巡查使用端</option>
                </select>
            </div>
            </li>
            <li><label>关键字:</label><input name="name" value="${name}" type="text" class="scinput"/></li>
            <li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="搜索"/>
                <input type="hidden" name="pageSize" value="${pageSize}"/>
                <input type="hidden" name="page" id="page" value="${page}"/>
            </li>
        </ul>
    </form>
    <div class="tools">

        <ul class="toolbar">
            <%--<sec:authorize ifAnyGranted="appVersion_add">--%>
            <li onclick="forWardUrl('<%=path %>/appVersion_add');"><span><img
                    src="<%=path %>/page/images/t01.png"/></span>添加
            </li>
            <%--</sec:authorize>--%>
            <%--<sec:authorize ifAnyGranted="firePatrolUser_delete">--%>
            <li onclick="bulkDelete('<%=path %>/appVersion_delete','0');"><span><img
                    src="<%=path %>/page/images/t03.png"/></span>批量删除
            </li>
            <%--</sec:authorize>--%>
        </ul>
    </div>


    <table class="tablelist">
        <thead>
        <tr>
            <th width="40px"><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/>
            </th>
            <th width="100px">APP类型</th>
            <th width="100px">版本号</th>
            <th width="100px">版本名称</th>
            <th width="350px">版本描述</th>
            <th width="150px">强制更新（Y/N）</th>
            <th width="150px">发布时间</th>
            <th width="100px">下载次数</th>
            <th width="100px">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${appVersionPage.list}" var="d">
            <tr>
                <td><input type="checkbox" name="checkbox" value="${d.versioncode}" id="checkbox_${d.versioncode}"
                           class="checkItem"/></td>
                <td>
                    <c:choose>
                        <c:when test="${d.type==1}">
                            <p>巡更巡查管理端</p>
                        </c:when>
                        <c:when test="${d.type==2}">
                            <p>安防巡更使用端</p>
                        </c:when>
                        <c:when test="${d.type==3}">
                            <p>消防巡查使用端</p>
                        </c:when>
                    </c:choose>
                </td>
                <td>${d.versioncode}</td>
                <td>${d.name}</td>
                <td>${d.content}</td>
                <td>${d.needUpdate}</td>
                <td><fmt:formatDate value="${d.posttime}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td>${d.downloadcount}</td>
                <td>
                    <sec:authorize ifAnyGranted="firePatrolUser_delete">
                        <a href="javascript:void(0);"
                           onclick="bulkDelete('<%=path %>/appVersion_delete','${d.versioncode}');" class="tablelink">
                            删除</a>
                    </sec:authorize>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <!-- 分页开始 -->
    <div class="pagin">
        <div class="message">共<i class="blue">${appVersionPage.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${appVersionPage.currentPage}/${appVersionPage.totalPage }&nbsp;</i>页</div>
        <ul class="paginList">
            <c:forEach items="${appVersionPage.fristPage}" var="f">
                <li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
            </c:forEach>
            <li class="paginItem current"><a href="javascript:void(0);">${appVersionPage.currentPage}</a></li>
            <c:forEach items="${appVersionPage.laPage}" var="l">
                <li class="paginItem"><a href="javascript:void(0);" rel="${l}">${l}</a></li>
            </c:forEach>
        </ul>
    </div>
    <!-- 分页结束 -->
</div>
<script type="text/javascript">
    $('.tablelist tbody tr:odd').addClass('odd');
    $(function(){
        //选择框
        $(".select3").uedSelect({
            width : 152
        });
    });
</script>

</body>
</html>
