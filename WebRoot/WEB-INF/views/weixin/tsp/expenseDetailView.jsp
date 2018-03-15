<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="msapplication-tap-highlight" content="no" />
    <link rel="stylesheet" href="<%=basePath%>tsp/page/css/common.css">
    <link rel="stylesheet" href="<%=basePath%>tsp/page/css/details.css">
    <link rel="stylesheet" type="text/css" href="<%=path %>/layer3.1.1/mobile/need/layer.css" />
    <script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
	<script type="text/javascript" src="<%=path %>/layer3.1.1/layer.js"></script>
    <title>消费详情</title>
</head>
<body>
    <section class="details">
        <div class="details_info">
            <div class="parkingName space"><span>${expenseDetail.carparkname}</span></div>
            <c:if test="${expenseDetail.status == 1}">
	            <div class="parkingCost space">-${expenseDetail.payMoney/100}</div>
	            <div class="parkingState space">交易成功</div>
            </c:if>
            <c:if test="${expenseDetail.status == -1}">
	            <div class="parkingCost space">-${expenseDetail.payMoney/100}</div>
	            <div class="parkingState fail" style="color:#e99856;">交易失败</div>
            </c:if>
        </div>
        <ul class="parkingInfo">
            <li class="parkingInfo_item">
                <span class="item_title">订单金额</span>
                <span class="item_info item">${expenseDetail.payMoney/100}</span>
            </li>
            <li class="parkingInfo_item">
                <span class="item_title">付款方式</span>
                <span class="item_info item">微信支付</span>
            </li>
            <li class="parkingInfo_item">
                <span class="item_title">收款方</span>
                <span class="item_info item">酒城停车</span>
            </li>
        </ul>
        <div id="line"></div>
        <ul>
            <li class="time">
               <div class="time_created">创建时间</div>
                <div class="time_details">
                    <span class="f15">${expenseDetail.fmPayTime}</span>
                </div>
            </li>
        </ul>
    </section>
</body>
</html>