<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    DateFormat df = new SimpleDateFormat("yyyy年MM月dd日，EEE", Locale.CHINA);
    String date ="今天是："+ df.format(new Date());
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>个人信息弹框</title>
    <script src="<%=basePath%>page/js/jquery.js"></script>
    <script src="<%=basePath%>page/color/jscolor.js"></script>
    <style>
        html,body,ul,li{margin: 0;padding: 0;}
        html,body{width: 100%;height: 100%;}
        ul,li,a,em,i{list-style: none;font-style: normal;}
        .background-bomb-box{width: 100%;height: 100%;position: relative;}
        #mask{width: 100%;height: 100%;background: #333;opacity: 0.5;position: fixed;left: 0;top: 0;}
        .background-bomb{width: 448px;height: 245px;position: absolute;left: 50%;top: 50%;margin-left: -224px;background: #fff;margin-top: -122px;}
        .bomb-top{height: 36px;line-height: 36px;background: #ECECEC;padding-left: 10px;color: #282828;font-weight: bold;}
        .bomb-close{width: 36px;height: 36px;background: url(img/close.png) no-repeat center;float: right;cursor: pointer;}
        .bomb-content{height: 209px;padding: 12px 15px;line-height: 22px;overflow: hidden;}

        .color-selection-list{padding: 10px;}
        .color-selection-list>li{overflow: hidden;padding: 10px 0;line-height: 32px;}
        .color-selection-name{width: 150px;float: left;}
        .color-selection-option,.region-name{width: 225px;height: 30px;border: 1px solid #DADADA;float: left;outline: none;padding: 0 8px;}
        .add-xungeng-btn-box{text-align: center;margin-top: 9px;}
        .add-xungeng-btn-box button{height: 34px;background: #fff;border-radius: 3px;outline: none;border: 1px solid #CFCFCF;cursor: pointer;padding: 0 20px;margin-top: 40px;}
        .add-xungeng-btn-box .submit-btn{background: #157ABE;color: #fff;border: 0;margin-left: 25px;}
    </style>
</head>
<body>
<div class="background-bomb-box">
    <div id="mask"></div>
    <div class="background-bomb">
        <div class="bomb-top"><span>异常详情</span><div class="bomb-close"></div></div>
        <div class="bomb-content">
            <table style="width: 100%;border-top:1px solid #e5e5e5;border-left:1px solid #e5e5e5;" cellspacing="0" cellpadding="0">
                <c:forEach items="${patrolExceptionInfos}" var="p">
                <tr><td style="border-right:1px solid #e5e5e5;border-bottom:1px solid #e5e5e5;">${p.createTime}</td><td style="border-right:1px solid #e5e5e5;border-bottom:1px solid #e5e5e5;">${p.exceptionName}</td></tr>
                <%--<tr><td>sdkuhdsk</td><td>sdkuhdsk</td></tr>
                <tr><td>sdkuhdsk</td><td>sdkuhdsk</td></tr>--%>
                </c:forEach>
            </table>
            <%--<ul class="color-selection-list">--%>
                <%--<form action="<%=basePath%>patrolReg_update" method="POST" id="myForm">--%>
                    <%--<input type="hidden" name="regionId" value="${regionId}"/>--%>
                    <%--<li>--%>
                        <%--<label class="color-selection-name">巡更区域名称：</label>--%>
                        <%--<input name="regionName" class="region-name" type="text" value="${regionName}"/>--%>
                    <%--</li>--%>
                    <%--<li>--%>
                        <%--<label class="color-selection-name">选择区域颜色：</label>--%>
                        <%--<input name="color" class="color-selection-option color" value="${color}" type="text"/>--%>
                    <%--</li>--%>
                <%--</form>--%>
            <%--</ul>--%>

            <%--<div class="add-xungeng-btn-box">--%>
                <%--<button type="reset" onclick="goToRegionList()">取消</button>--%>
                <%--<button onclick="submitRegion()" class="submit-btn">提交并配置巡更范围</button>--%>
            <%--</div>--%>
        </div>
    </div>
</div>

<script>
    $(".bomb-close").click(function() {
        $(".background-bomb").fadeOut(300);
        $("#mask").hide();
    });


    //当点击了提交按钮后进行数据提交
    var submitRegion = function () {
        $("#myForm").submit();
    };

    /**
     * 取消操作之后跳转到之前页面进行显示
     */
    var goToRegionList = function () {
        window.location.href = "<%=basePath%>patrolRegList"
    }
</script>
</body>
</html>