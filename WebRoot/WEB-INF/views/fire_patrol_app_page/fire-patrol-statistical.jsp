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
		<title>巡查记录</title>
		<link rel="stylesheet" href="<%=basePath%>fire_patrol_app_page/css/common.css" />
		<link rel="stylesheet" href="<%=basePath%>fire_patrol_app_page/css/pullToRefresh.css" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>fire_patrol_app_page/swiper/swiper-3.4.2.min.css"/>
		<link rel="stylesheet" href="<%=basePath%>fire_patrol_app_page/css/chuanshi.css" />
		<script src="<%=basePath%>fire_patrol_app_page/js/jquery.min.js"></script>
		<script src="<%=basePath%>fire_patrol_app_page/js/datePicker.js"></script>
	</head>
	<body>
		<div class="swiper-container swiper-container-horizontal swiper-container-free-mode listPage-swiper">
		    <div class="swiper-wrapper" style="">
		      <a class="swiper-slide swiper-slide-active active">最新动态</a>
		      <a class="swiper-slide swiper-slide-next">新闻动态</a>
		      <a class="swiper-slide swiper-slide-next">新闻动态</a>
		      <a class="swiper-slide swiper-slide-next">新闻动态</a>
		      <a class="swiper-slide swiper-slide-next">新闻动态</a>
		      <a class="swiper-slide swiper-slide-next">新闻动态</a>
		    </div>
		</div>
		<div class="box f14 inspection-record-box">
			<div class="inspection-record-top">
				<div class="month-select-box">
					<span>本月</span>
					<div class="date-select-btn" id="dateBtn"><input type="hidden" name="month" id="month" value="" /></div>
				</div>
				<ul class="inspection-record-sum">
					<li>巡查次数：600</li>
					<li>巡查设备数：600</li>
					<li>正常设备数：600</li>
					<li>异常设备数：600</li>
				</ul>
			</div>
			
			<div class="inspection-record-list-box" id="inspection-record-wrapper-box">
				<ul class="inspection-record-list">
					<li>
						<div class="inspection-record-basic-infor">
							<span class="inspection-record-name">设备001</span>
							<span class="inspection-record-status">正常</span>
						</div>
						<p>设备位置：一号教学楼2层</p>
						<p>巡查时间：2018.04.28 </p>
					</li>
					<li>
						<div class="inspection-record-basic-infor">
							<span class="inspection-record-name">设备001</span>
							<span class="inspection-record-status">正常</span>
						</div>
						<p>设备位置：一号教学楼2层</p>
						<p>巡查时间：2018.04.28 </p>
					</li>
					<li>
						<div class="inspection-record-basic-infor">
							<span class="inspection-record-name">设备001</span>
							<span class="inspection-record-status">正常</span>
						</div>
						<p>设备位置：一号教学楼2层</p>
						<p>巡查时间：2018.04.28 </p>
					</li>
					<li>
						<div class="inspection-record-basic-infor">
							<span class="inspection-record-name">设备001</span>
							<span class="inspection-record-status">正常</span>
						</div>
						<p>设备位置：一号教学楼2层</p>
						<p>巡查时间：2018.04.28 </p>
					</li>
				</ul>
			</div>
		</div>
		<script src="<%=basePath%>fire_patrol_app_page/js/iscroll.js"></script>
		<script src="<%=basePath%>fire_patrol_app_page/js/pullToRefresh.js"></script>
		<script src="<%=basePath%>fire_patrol_app_page/swiper/swiper-3.4.2.jquery.min.js"></script>
		<script>


			$(function () {
            });



			//进行请求数据
			var showTitle = function () {
                $.ajax({
                    url: '<%=basePath%>getBuildingType?',
                    type: 'get',
//					contentType:'application/json',
//					dataType:'json',
                    success:function(data) {
						console.log(data);
                    },
                    error:function() {}

                });
            };

            showTitle();




			//顶部导航功能
			var length=$(".swiper-slide").length;
			if (length<5) {
				$(".swiper-slide").addClass("flex");
			    swiperClick();
			}else{
			    var swiper = new Swiper('.swiper-container', {
			      slidesPerView: 'auto',
			      freeMode: true,
			      onInit: function(){
			      	swiperClick();
			      }
			    });
			}
			
			function swiperClick(){
				$(".swiper-slide").click(function(){
		      		$(this).addClass("list-tab-active").siblings(".swiper-slide").removeClass("list-tab-active");
		      	})
			}
			
			//时间选择
			var calendar = new datePicker();
			calendar.init({
				'trigger': '#dateBtn', 		/*按钮选择器，用于触发弹出插件*/
				'type': 'ym',				//模式：date日期；datetime日期时间；time时间；ym年月；year 年；年月日+时分秒：dateTimeSecond； month 月；dateHour 
				'minDate':'1900-1-1',		/*最小日期*/
				'maxDate':'2100-12-31',		/*最大日期*/
				'onSubmit':function(){		/*确认时触发事件*/
					var theSelectData=calendar.value;
					$("#month").val(calendar.value);
				},
				'onClose':function(){
				}
			});
		
			//上拉加载，下拉刷新
			$(function(){
				refresher.init({
					id:"inspection-record-wrapper-box",
					pullDownAction:Refresh,
					pullUpAction:Load
				});
						
				function Refresh() {
					setTimeout(function () {
						window.location.reload();
					}, 1000);
				}
				
				var paga=0,pagaTotal=3;
				//首次执行
				Load();
				
				//加载内容
				function Load() {
					if (paga<pagaTotal) {
						setTimeout(function () {
	//						$.ajax({
	//							type:"get",
	//							url:"json/distribution.json",
	//							async:false,
	//							success:function(Data){
	//								if (Data.status=="success") {
		//								$.each(data.number,function(j,j_item){
		//									
		//								})
										for (i=0; i<1; i++) {
											$(".inspection-record-list").append('<li>'+
													'<div class="inspection-record-basic-infor">'+
													'<span class="inspection-record-name">设备001</span>'+
													'<span class="inspection-record-status">正常</span>'+
													'</div>'+
													'<p>设备位置：一号教学楼2层</p>'+
													'<p>巡查时间：2018.04.28 </p>'+
													'</li>');
										}
										myScroll.refresh();
										
										paga++;
										pagaTotal=3;
										
										if(paga==pagaTotal){
											$(".pullUpLabel").text("没有更多内容了...")
										}
	//								}
	//							}
	//						});
						}, 1000);
					} else{
						$(".pullUpLabel").text("没有更多内容了...")
					}
				}
			});
			
		</script>
	</body>
</html>
