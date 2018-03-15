<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="msapplication-tap-highlight" content="no" />
	<meta name="format-detection" content="telephone=no" />
	<title>智慧停车云平台·微信端</title>
	<link rel="stylesheet" href="<%=basePath%>wxmange/css/common.css">
	<link rel="stylesheet" href="<%=basePath%>wxmange/css/style.css">
	<script src="<%=basePath%>wxmange/js/jquery.min.js"></script>
	<script src="<%=basePath%>wxmange/js/echarts.simple.min.js"></script>
	<script src="<%=basePath%>wxmange/layer_mobile/layer.js"></script>
	<link rel="stylesheet" href="<%=basePath%>wxmange/css/LCalendar.css">
	<link rel="stylesheet" href="<%=basePath%>wxmange/weui/weui.css">
</head>
<body>
    <div class="box none-box">
		没有权限查看
    </div>
	<div class="box operation-analysis-box">
		<div class="top posit-RE">
<!--			<span class="top-return posit-AB"><img src="<%=basePath%>wxmange/images/hh.png" alt=""></span>-->
			<p class="top-title f16 text-CENT">全部停车场</p>
<!--			<span class="top-fun posit-AB time-btn"><img src="<%=basePath%>wxmange/images/time-select.png" alt=""></span>-->
		</div>
		<div class="operation-content f14">
<!--			<p class="operation-title pad-r-12"><span class="operation-refresh-btn"><img src="<%=basePath%>wxmange/images/sx.png" alt=""></span><span id="dateChartMome"></span><span class="day-month month-btn float-R" onclick="monthloadChart()">当月</span><span class="obstruct float-R">|</span><span class="day-month day-btn float-R day-month-act" onclick="dayloadChart()">当日</span></p>-->
			<div class="operation-title pad-r-12"><span class="operation-refresh-btn"><img src="<%=basePath%>wxmange/images/sx.png" alt=""></span><span class="day-month query-btn query-btn1 float-R">查询</span><span class="obstruct float-R">|</span><span class="day-month day-btn float-R day-month-act" onclick="dayloadChart()">当日</span><p class="query-result-box"><span class="query-result-mesg-box query-result-mesg-box1"><span class="result-mesg1-1"><i class="start-time1">2017年11月21日</i><i class="end-time1">20170316</i>运营分析结果</span><span class="result-mesg1-2"></span></span></p></div>
			<div class="operation-data-box">
				<p class="data-title f16"><span class="data-title-logo"><img src="<%=basePath%>wxmange/images/sy.png" alt=""></span><span class="">经营收益对比</span></p>
				<div id="bing"></div>
			</div>
			<div class="operation-data-box">
				<p class="data-title f16"><span class="data-title-logo"><img src="<%=basePath%>wxmange/images/qxt.png" alt=""></span><span>车位使用率分析</span><!-- <a class="space-occupancy-more-btn" href="javascript:;">更多</a> --></p>
				<div class="zhu space-occupancy">
					<ul class="zhu-left occupancy-left float-L f12"></ul>
					<ul class="zhu-right occupancy-right float-R f12"></ul>
				</div>
			</div>
			<div class="operation-data-box">
				<p class="data-title f16"><span class="data-title-logo"><img src="<%=basePath%>wxmange/images/qxt-1.png" alt=""></span><span>车位周转率分析</span><!-- <a class="space-turnover-more-btn" href="javascript:;">更多</a>--></p>
				<div class="zhu turnover">
					<ul class="zhu-left turnover-left float-L f12"></ul>
					<ul class="zhu-right turnover-right float-R f12"></ul>
				</div>
			</div>
		</div>
	</div>
	<!-- earning页面 -->
	<div class="box earnings-box">
		<div class="top posit-RE">
<!--			<span class="top-return posit-AB"><img src="<%=basePath%>wxmange/images/hh.png" alt=""></span>-->
			<p class="top-title f16 text-CENT" carparkcode="" id="carpark1"></p>
			<span class="top-fun posit-AB" id="showPicker1"><img src="<%=basePath%>wxmange/images/qh.png" alt=""></span>
		</div>
		<div class="earnings-content f14">
<!--			<p class="content-title pad-r-12"><span class="content-refresh-btn"><img src="<%=basePath%>wxmange/images/sx.png" alt=""></span><span id="dateMemoIco"></span><span class="day-month month-btn float-R monthioc" onclick="monthIncomeQuery()">当月</span><span class="obstruct float-R">|</span><span class="day-month day-btn float-R dayioc day-month-act " onclick="dayIncomeQuery()">当日</span></p>-->
			<div class="operation-title pad-r-12"><span class="operation-refresh-btn"><img src="<%=basePath%>wxmange/images/sx.png" alt=""></span><span class="day-month query-btn query-btn2 float-R monthioc">查询</span><span class="obstruct float-R">|</span><span class="day-month day-btn float-R dayioc day-month-act" onclick="dayIncomeQuery()">当日</span><p class="query-result-box"><span class="query-result-mesg-box query-result-mesg-box2"><span class="result-mesg2-1"><i class="start-time2">2017年11月21日</i><i class="end-time2">~20170316</i>收费统计</span><span class="result-mesg2-2"></span></span></p></div>
			<div class="all-num-box">
				<p class="all-title text-CENT f22">全部应收</p>
				<p class="all-num text-CENT f20"><span><img src="<%=basePath%>wxmange/images/money13.png" alt=""></span><span id="total">0</span></p>
			</div>
			<div class="num-list-box">
				<div class="temporary-num float-L text-CENT f14">
					<p style="margin-top:.3rem;">临停收费</p>
					<p id="tem">0</p>
				</div>
				<div class="payment-mon float-L">
					<p class="payment-mon-p f14"><span><img src="<%=basePath%>wxmange/images/by.png" alt=""></span><span class="f14" id="monthly">0</span><span class="rmb f12" style="margin:0;">￥</span><span class="serve-mon">包月</span></p>
				</div>
				<div class="payment-serve float-L">
					<p class="payment-serve-p f14"><span><img src="<%=basePath%>wxmange/images/fw.png" alt=""></span><span class="f14" id="fee">0</span><span class="rmb f12" style="margin:0;">￥</span><span class="serve-mon">服务</span></p>
				</div>
			</div>
		</div>

	</div>
	<div class="box in-out-box">
		<div class="top posit-RE">
<!--			<span class="top-return posit-AB"><img src="<%=basePath%>wxmange/images/hh.png" alt=""></span>-->
			<p class="top-title f16 text-CENT" carparkcode="" id="carpark2"></p>
			<span class="top-fun posit-AB" id="showPicker2"><img src="<%=basePath%>wxmange/images/qh.png" alt=""></span>
		</div>
		<div class="earnings-content f14">
<!--			<p class="content-title pad-r-12"><span class="content-refresh-btn"><img src="<%=basePath%>wxmange/images/sx.png" alt=""></span><span id="inOutMemo"></span><span class="day-month month-btn float-R monthflow" onclick="monthFlowQuery()">当月</span><span class="obstruct float-R">|</span><span class="day-month day-btn float-R day-month-act dayflow" onclick="dayFlowQuery()">当日</span></p>-->
			<div class="operation-title pad-r-12"><span class="operation-refresh-btn"><img src="<%=basePath%>wxmange/images/sx.png" alt=""></span><span class="day-month query-btn query-btn3 float-R monthflow">查询</span><span class="obstruct float-R">|</span><span class="day-month day-btn float-R day-month-act dayflow" onclick="dayFlowQuery()">当日</span><p class="query-result-box"><span class="query-result-mesg-box query-result-mesg-box3"><span class="result-mesg3-1"><i class="start-time3">2017年11月21日</i><i class="end-time3">~2017-03-16</i>进出统计</span><span class="result-mesg3-2"></span></span></p></div>
			<div class="operation-data-box">
				<p class="data-title f16"><span class="data-title-logo"><img src="<%=basePath%>wxmange/images/jr.png" alt=""></span><span>进入</span></p>
				<div class="zhu in">
					<ul class="zhu-left in-left float-L f12">
						<li>总入场</li>
						<li>月租</li>
						<li>临停</li>
					</ul>
					<ul class="zhu-right in-right float-R f12"></ul>
				</div>
			</div>
			<div class="operation-data-box">
				<p class="data-title f16"><span class="data-title-logo"><img src="<%=basePath%>wxmange/images/lk.png" alt=""></span><span>离开</span></p>
				<div class="zhu out">
					<ul class="zhu-left out-left float-L f12">
						<li>总出场</li>
						<li>月租</li>
						<li>临停</li>
					</ul>
					<ul class="zhu-right out-right float-R f12"></ul>
				</div>
			</div>
		</div>
	</div>
    
    <!-- 时间选择1 -->
	<div class="box time-select-box time-select-box1">
		<div class="time-img-box f14">
			<p class="f14">Please select a time</p>
			<div class="close-select-time close-select1 posit-AB">关闭</div>
		</div>
		<div class="time-select-content">
			<div class="time-start f14 posit-RE">
				<span class="f14">选择开始时间：</span><input id="picktime-s1" class="picktime" type="text" readonly placeholder="日期和时间选择特效" value=""/>
				<div class="sanjiao posit-AB"><img src="<%=basePath%>wxmange/images/sj.png" alt=""></div>
	        </div>
	        <div class="time-end f14 posit-RE">
	            <span class="f14">选择结束时间：</span><input id="picktime-e1" class="picktime" type="text" readonly placeholder="日期和时间选择特效" value="" />
	            <div class="sanjiao posit-AB"><img src="<%=basePath%>wxmange/images/sj.png" alt=""></div>

	        </div>
	        <div class="time-sure-box">
	        	<input class="time-sure time-sure1" type="button" value="确定">
	        </div>
		</div>
		
    </div>
	<!-- 时间选择2 -->
    <div class="box time-select-box time-select-box2">
		<div class="time-img-box f14">
			<p class="f14">Please select a time</p>
			<div class="close-select-time close-select2 posit-AB">关闭</div>
		</div>
		<div class="time-select-content">
			<div class="time-start f14 posit-RE">
				<span class="f14">选择开始时间：</span><input id="picktime-s2" class="picktime" type="text" readonly placeholder="日期和时间选择特效" value=""/>
				<div class="sanjiao posit-AB"><img src="<%=basePath%>wxmange/images/sj.png" alt=""></div>
	        </div>
	        <div class="time-end f14 posit-RE">
	            <span class="f14">选择结束时间：</span><input id="picktime-e2" class="picktime" type="text" readonly placeholder="日期和时间选择特效" value="" />
	            <div class="sanjiao posit-AB"><img src="<%=basePath%>wxmange/images/sj.png" alt=""></div>

	        </div>
	        <div class="time-sure-box">
	        	<input class="time-sure time-sure2" type="button" value="确定">
	        </div>
		</div>
		
    </div>
	<!-- 时间选择3 -->
    <div class="box time-select-box time-select-box3">
		<div class="time-img-box f14">
			<p class="f14">Please select a time</p>
			<div class="close-select-time close-select3 posit-AB">关闭</div>
		</div>
		<div class="time-select-content">
			<div class="time-start f14 posit-RE">
				<span class="f14">选择开始时间：</span><input id="picktime-s3" class="picktime" type="text" readonly placeholder="日期和时间选择特效" value=""/>
				<div class="sanjiao posit-AB"><img src="<%=basePath%>wxmange/images/sj.png" alt=""></div>
	        </div>
	        <div class="time-end f14 posit-RE">
	            <span class="f14">选择结束时间：</span><input id="picktime-e3" class="picktime" type="text" readonly placeholder="日期和时间选择特效" value="" />
	            <div class="sanjiao posit-AB"><img src="<%=basePath%>wxmange/images/sj.png" alt=""></div>

	        </div>
	        <div class="time-sure-box">
	        	<input class="time-sure time-sure3" type="button" value="确定">
	        </div>
		</div>
		
    </div>
	<ul class="bottom">
		<li class="bottom-list1 bottom-list"></li>
		<li class="bottom-list2 bottom-list"></li>
		<li class="bottom-list3 bottom-list"></li>
	</ul>
	<input type="hidden" id="path" value="<%=basePath%>">
	<input type="hidden" id="token" value="${loginToken.token}">
	<input type="hidden" id="right" value="${loginToken.right}">
	<script src="<%=basePath%>wxmange/js/LCalendar.js"></script>
   	<script src="<%=basePath%>wxmange/weui/weui.js"></script>
	<script src="<%=basePath%>wxmange/js/index.js"></script>
	<script src="<%=basePath%>wxmange/js/data.js"></script>
	
</body>
</html>