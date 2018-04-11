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
    <script type="text/javascript" src="<%=path %>/page/js/common.js"></script>
    <script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>
    <link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
    <%--<script type="text/javascript" src="<%=path %>/page/js/common.js"></script>--%>
    <%--<script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>--%>
    <%--<link rel="stylesheet" href="<%=path %>/page/css/common-PC.css" />--%>
    <link rel="stylesheet" href="<%=path %>/page/css/Tstyle.css" />
    <%--<script src="<%=path %>/page/js/jquery.min.js" type="text/javascript"></script>--%>
    <script src="https://cdn.bootcss.com/html5media/1.1.8/html5media.js"></script>
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
    <li><a href="#">消防巡查</a></li>
    <li><a href="<%=path %>/firePatrolConfig_list">配置信息管理</a></li>
    </ul>
    </div>
    
    <%--<div class="rightinfo">--%>
    <%--<form action="<%=path %>/firePatrolConfig_edit" method="post" id="addForm">--%>
    <%--<table class="tablelist">--%>
        <%--<ul>--%>
        	<%--<li>参数配置</li>--%>
        <%--</ul>--%>
    <%--<input type="hidden" name="id" value="${firePatrolConfig.id }"/>--%>
        <%--<ul class="seachform">--%>
        	<%--<li>--%>
        	<%--设置巡查有效距离:<input name="distance" type="text" value="${firePatrolConfig.distance }"/>*设置后在设备${firePatrolConfig.distance }米范围内可以进行巡查--%>
        	<%--</li>--%>
        <%--</ul>--%>
        <%--<ul>--%>
        <%--<li><label>&nbsp;</label><input name="" type="submit" class="btn" value="确认修改"/><input type="reset"  value="重置"/> </li>--%>
        <%--</ul>--%>
    <%--</table>--%>
    <%--</form>--%>
    <%--</div>--%>
    <%--<script type="text/javascript">--%>
	<%--$('.tablelist tbody tr:odd').addClass('odd');--%>
	<%--</script>--%>

    <%--开始--%>
    <%--这里进行组装--%>
    <div class="box">
        <form action="<%=path %>/firePatrolConfig_edit" method="post" id="addForm">
            <input type="hidden" name="id" value="${firePatrolConfig.id }"/>
            <div class="setting">
                <div class="top-bar">
                    <div class="left">
                        <span class="top-setting">参数配置</span>
                    </div>
                </div>
                <div class="container">
                    <ul class="patrol-parameter-list">
                        <li>
                            <div class="title-first">
                                <div class="title-content">
                                    <span class="content">有效距离:</span>
                                </div>
                            </div>
                            <div class="control-first">
                                <div class="control-content-one">
                                    <span class="content">每</span>
                                </div>
                                <input type="text" name="distance" class="text1" value="${firePatrolConfig.distance }" onkeyup="value=value.replace(/[^\d]/g,'')">
                                <div class="control-content-two">
                                    <span class="symbol">*</span>
                                    <span class="content"> 米</span>
                                </div>
                                <div class="control-content-three">
								<span class="mark">
									（设置巡查有效距离:<span class="input-result">${firePatrolConfig.distance }</span>米范围内可以进行巡查）
								</span>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <input type="submit" class="submit" value="提交">
            <input type="reset" class="reset" value="重置">
        </form>
    </div>
    <script>
        $(".text1").keyup(function(){
            var t=$(this).val();
            if (t!="") {
                $(this).parent().find(".input-result").text(t);
            } else{
                $(this).parent().find(".input-result").text("x");
            }
        })
    </script>

</body>

</html>
