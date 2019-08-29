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
    <title>版本发布</title>
    <link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
    <link rel="stylesheet" href="<%=path %>/page/validator/jquery.validator.css" />
    <script type="text/javascript" src="<%=path %>/page/validator/jquery.validator.js"></script>
    <script type="text/javascript" src="<%=path %>/page/validator/local/zh-CN.js"></script>
    <script type="text/javascript" src="<%=path %>/page/laydate/laydate.js"></script>


    <%--<link href="<%=path %>/page/css/select.css" rel="stylesheet" type="text/css" />--%>
    <%--<script type="text/javascript" src="<%=path %>/page/js/select-ui.min.js"></script>--%>
    <%--<script type="text/javascript" src="<%=path %>/page/js/common.js"></script>--%>
    <%--<script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>--%>
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
        <li><a href="#">APP管理</a></li>
        <li><a href="<%=path %>/firePatrolUser_list">APP版本管理</a></li>
        <li><a href="#">版本发布</a></li>
    </ul>
</div>
<div class="formbody">
    <div class="formtitle"><span>版本发布</span></div>
    <form action="<%=path %>/appVersion_add" method="post" id="addForm" data-validator-option="{stopOnError:true, timely:1 ,theme:'yellow_right'}" enctype="multipart/form-data">
        <input type="hidden" name="method" value="add" />
        <input type="hidden" name="enablRegionIds" id="enablRegionIds"/>
        <ul class="forminfo">
            <li><label>APP类型：</label>
                <div class="vocation" >
                    <select class="select3" name="type" style="width: 347px;height: 40px;border: solid #ced9df ">
                        <option value="1" ${type eq 1?"selected":""}>巡更巡查管理端</option>
                        <option value="2" ${type eq 2?"selected":""}>安防巡更使用端</option>
                        <option value="3" ${type eq 3?"selected":""}>消防巡查使用端</option>
                    </select>
                </div>
            <li>
                <label class="add-label" for="versioncode">版本号：</label>
                <input name="versioncode" id="versioncode" class="dfinput" type="text" />
            </li>
            <li>
                <label class="add-label">版本名称：</label>
                <input name="name" required class=" dfinput" type="text" />
            </li>
            <li>
                <label class="add-label" for="upload">APP：</label>
                <input id="upload" type="file" required class="PanoramaEdit-input radius-4" name="upload" />
            </li>
            <li><label>强制更新：</label>
                <cite>
                    <input name="needUpdate" type="radio" value="Y" checked="checked"/>是
                    <input name="needUpdate" type="radio" value="N" data-rule="核心模块:checked;" />否&nbsp;&nbsp;&nbsp;&nbsp;
                </cite>
            </li>
            <li>
                <label class="add-label" for="add-app-introduction">更新说明：</label>
                <textarea name="content" required id="add-app-introduction" class="textinput" style="width: 330px;height: 100px"></textarea>
            </li>
            <li>
                <button  id="add-app-submit" class="save-btn btn radius-4" type="submit" onclick="_submit()">提交</button>
            </li>
        </ul>
    </form>

</div>
<script type="text/javascript">
    var host ='<%=path%>';
    var _submit = function(){
        $('#addForm').submit();
    }
</script>
</body>

</html>
