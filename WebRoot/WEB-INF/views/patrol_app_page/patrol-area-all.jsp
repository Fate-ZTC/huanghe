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
		<title>巡更区域</title>
		<link rel="stylesheet" href="<%=basePath%>patrol_app_page/css/common.css" />
		<link rel="stylesheet" href="<%=basePath%>patrol_app_page/css/pullToRefresh.css" />
		<link rel="stylesheet" href="<%=basePath%>patrol_app_page/css/chuanshi.css" />
		<script src="<%=basePath%>patrol_app_page/js/jquery.min.js"></script>
		<script src="<%=basePath%>patrol_app_page/js/datePicker.js"></script>
		<script src="<%=basePath%>patrol_app_page/js/jquery.cookie.js"></script>
	</head>
	<body>
		<div class="box f14">
			<div class="patrol-query-tab-box">
				<ul class="date-tab-list">
					<li id="longtestTitle" onclick="dateOnclick(0)">过去30天</li>
					<li id="mediumTitle" onclick="dateOnclick(1)">过去7天</li>
					<li id="nowTitle" onclick="dateOnclick(2)">今天</li>
					<div class="date-select-btn" id="dateBtn"></div>
				</ul>
				<div class="date-select-result">
					<span>2018</span>
					<div class="delete-date-result"></div>
				</div>
				<ul class="situation-tab-list">
					<li id="selectAll" class="active" onclick="typeOnclick(0)">全部(<i id="allCount">${countVO.allCount}</i>)</li>
					<li id="selectException" onclick="typeOnclick(1)">异常(<i id="exceptionCount">${countVO.exceptionCount}</i>)</li>
					<li id="selectNormal" onclick="typeOnclick(2)">正常(<i id="normalCount">${countVO.normalCount}</i>)</li>
				</ul>
			</div>
			
			<div class="patrol-situation-list-box" id="wrapperList">
				<ul class="patrol-situation-list" id="patrol-situation-list">
					<%--<li>--%>
						<%--<p class="patrol-situation-date">2018年3月28日</p>--%>
						<%--<div class="patrol-situation-box">--%>
							<%--<div class="patrol-situation-person-mesg ovfl">--%>
								<%--<div class="patrol-situation-person-img region float-L"></div>--%>
								<%--<ul class="float-L">--%>
									<%--<li class="patrol-situation-person-time">09:00:00~10:00:00</li>--%>
									<%--<li class="patrol-situation-person">巡更人员：王胜利</li>--%>
									<%--<li class="patrol-situation-person">账号：2018022</li>--%>
								<%--</ul>--%>
							<%--</div>--%>

							<%--<div class="patrol-situation-mesg-box">--%>
								<%--<!--巡更正常-->--%>
								<%--<div class="patrol-situation-mesg-normal">全程巡更正常</div>--%>
								<%--<!--巡更异常-->--%>
								<%--<div class="patrol-situation-mesg-abnormal">--%>
									<%--<p class="patrol-situation-mesg-abnormal-tit">巡更异常</p>--%>
									<%--<ul class="patrol-situation-abnormal-show hidden-latter">--%>
										<%--<li>19:25:00 离开巡更区域</li>--%>
										<%--<li>19:25:00 信号丢失，检测不到人员位置</li>--%>
										<%--<li>19:25:00 离开巡更区域</li>--%>
										<%--<li>19:25:00 信号丢失，检测不到人员位置</li>--%>
									<%--</ul>--%>

									<%--<div class="patrol-situation-abnormal-more">更多</div>--%>
								<%--</div>--%>
							<%--</div>--%>
						<%--</div>--%>

						<%--<div class="patrol-situation-box">--%>

							<%--<div class="patrol-situation-person-mesg ovfl">--%>
								<%--<div class="patrol-situation-person-img region float-L"></div>--%>
								<%--<ul class="float-L">--%>
									<%--<li class="patrol-situation-person-time">09:00:00~10:00:00</li>--%>
									<%--<li class="patrol-situation-person">巡更人员：王胜利</li>--%>
									<%--<li class="patrol-situation-person">账号：2018022</li>--%>
								<%--</ul>--%>
							<%--</div>--%>

							<%--<div class="patrol-situation-mesg-box">--%>
								<%--<!--巡更正常-->--%>
								<%--<div class="patrol-situation-mesg-normal">全程巡更正常</div>--%>
								<%--<!--巡更异常-->--%>
								<%--<div class="patrol-situation-mesg-abnormal">--%>
									<%--<p class="patrol-situation-mesg-abnormal-tit">巡更异常</p>--%>
									<%--<ul class="patrol-situation-abnormal-show hidden-latter">--%>
										<%--<li>19:25:00 离开巡更区域</li>--%>
										<%--<li>19:25:00 信号丢失，检测不到人员位置</li>--%>
										<%--<li>19:25:00 离开巡更区域</li>--%>
										<%--<li>19:25:00 信号丢失，检测不到人员位置</li>--%>
									<%--</ul>--%>

									<%--<div class="patrol-situation-abnormal-more">更多</div>--%>
								<%--</div>--%>
							<%--</div>--%>
						<%--</div>--%>
					<%--</li>--%>

					<%--<li>--%>
						<%--<p class="patrol-situation-date">2018年3月28日</p>--%>
						<%--<div class="patrol-situation-box">--%>

							<%--<div class="patrol-situation-person-mesg ovfl">--%>
								<%--<div class="patrol-situation-person-img region float-L"></div>--%>
								<%--<ul class="float-L">--%>
									<%--<li class="patrol-situation-person-time">09:00:00~10:00:00</li>--%>
									<%--<li class="patrol-situation-person">巡更人员：王胜利</li>--%>
									<%--<li class="patrol-situation-person">账号：2018022</li>--%>
								<%--</ul>--%>
							<%--</div>--%>

							<%--<div class="patrol-situation-mesg-box">--%>
								<%--<!--巡更正常-->--%>
								<%--<div class="patrol-situation-mesg-normal">全程巡更正常</div>--%>
								<%--<!--巡更异常-->--%>
								<%--<div class="patrol-situation-mesg-abnormal">--%>
									<%--<p class="patrol-situation-mesg-abnormal-tit">巡更异常</p>--%>
									<%--<ul class="patrol-situation-abnormal-show hidden-latter">--%>
										<%--<li>19:25:00 离开巡更区域</li>--%>
										<%--<li>19:25:00 信号丢失，检测不到人员位置</li>--%>
										<%--<li>19:25:00 离开巡更区域</li>--%>
										<%--<li>19:25:00 信号丢失，检测不到人员位置</li>--%>
									<%--</ul>--%>

									<%--<div class="patrol-situation-abnormal-more">更多</div>--%>
								<%--</div>--%>
							<%--</div>--%>
						<%--</div>--%>
					<%--</li>--%>
				</ul>
			</div>
		</div>
		
		<script src="<%=basePath%>patrol_app_page/js/iscroll.js"></script>
		<script src="<%=basePath%>patrol_app_page/js/pullToRefresh.js"></script>
		<script src="<%=basePath%>patrol_app_page/js/patrol-area-html.js"></script>
		<script>



			var requestData = '';//请求的数据
			var page = 1;		//page
			var pageSize = 20;	//pageSize
			var regionId = '';	//区域id
			var type = 0;		//获取类型(全部,异常,正常)
			var dateType = 1;	//时间类型
			var startDate = ''; //开始时间
			var endDate = '';	//结束时间
			var totalCount = '';//数据总条数
			var totalPage = '';	//数据总页数

			$(function () {
			    //获取cookie
                getCookie();
				<%--下面进行加载数据--%>
				<c:if test="${not empty data}">
				requestData = '${data}';
				</c:if>
				<c:if test="${not empty page}">
				page = ${page};
				</c:if>
				<c:if test="${not empty type}">
				type = ${type};
				</c:if>
				<c:if test="${not empty dateType}">
				dateType = ${dateType};
				</c:if>
				initDate(requestData);
			});




			/**
			 * 保存cookie
			 */
			var saveCookie = function (c_dateType,c_type) {
				//这里进行保存cookie
				$.cookie("dateType",c_dateType);
				$.cookie("type",c_type);
            };

			/**
			 * 获取cookie的值
			 */
			var getCookie = function () {
				var cookie_dateType = $.cookie("dateType");
				var cookie_type = $.cookie("type");
				if(cookie_dateType != undefined && cookie_dateType != null) {
				    dateType = cookie_dateType;
				}
				if(cookie_type != undefined && cookie_type != null) {
				    type = cookie_type;
				}
            };

			/**
			 *
			 * 首次加载的时候进行数据请求和数据渲染
			 * @data
			 */
			var initDate = function (data) {
				//这里进行组装数据
				console.log(data);
				//默认显示7天
				$("#mediumTitle").addClass("active");
				var dataJson = JSON.parse(data);

				//进行数据总页数和总条数设置
				totalCount = dataJson.totalCount;
				totalPage = dataJson.totalPage;


				//这里进行添加数据

				var showData = dataJson.list;
				var _this = $("#patrol-situation-list");
				if(showData != undefined && showData.length > 0) {
					for (var i = 0; i < showData.length; i++) {
						var item = showData[i];
						var titleDate = item.date;
						var starttime = item.startTime.split(' ')[1];
						var endtime = "";
						if(item.endTime != undefined && item.endTime != null) {
                            endtime = item.endTime.split(' ')[1];
                        }
						var time = starttime + "~" + endtime;
						var id = item.id;
						var username = item.username;
						var status = item.status;
						var jobNum = item.jobNum;
						var regionId = item.regionId;

						//下面进行保存regionId
						saveRegionId(regionId);
						//进行判断是否存在日期


						var itemAppend = '';
						var this_li_id = $("#" + titleDate).attr("id");
						if (this_li_id === undefined) {
							//不存在
							itemAppend += '<li id="' + titleDate + '">' +
									'<p class="patrol-situation-date" value="' + titleDate + '">' + titleDate + '</p>' +
									'<div class="patrol-situation-box">' +
									'<div class="patrol-situation-person-mesg ovfl">' +
									'<div class="patrol-situation-person-img region float-L"></div>' +
									'<ul class="float-L" style="width:1.5rem;">' +
									'<li class="patrol-situation-person-time">' + time + '</li>' +
									'<li class="patrol-situation-person">巡更人员：' + username + '</li>' +
									'<li class="patrol-situation-person">账号：' + jobNum + '</li>' +
									'</ul></div>';
//								'</li>';
							//判断是否有异常信息
							if (status === 1) {
								//正常
								itemAppend += '<div class="patrol-situation-mesg-box">' +
										'<div class="patrol-situation-mesg-normal">全程巡更正常</div></div>';
							}
							if (status === 2) {
								//异常
								itemAppend += '<div class="patrol-situation-mesg-box">' +
										'<div class="patrol-situation-mesg-abnormal">' +
										'<p class="patrol-situation-mesg-abnormal-tit">巡更异常</p>' +
										'<ul class="patrol-situation-abnormal-show hidden-latter">';
//									'<div class="patrol-situation-abnormal-more">更多</div>';
								//有异常需要进行
								var exceptionVos = showData[i].exceptionVOs;
								for (var j = 0; j < exceptionVos.length; j++) {
									var exceptionObj = exceptionVos[j];
									var createtime = exceptionObj.createtime;
									var exceptionMessage = exceptionObj.exception_name;
									var message = createtime + ":" + " " + exceptionMessage;
									itemAppend += '<li>' + message + '</li>';
								}
								itemAppend += '</ul><div class="patrol-situation-abnormal-more">更多</div>'
							}
							itemAppend += '</div></div></li>';
							_this.append(itemAppend);
                            myScroll.refresh();
						} else {
							//存在
							var liObj = $("#" + titleDate);

							itemAppend += '<div class="patrol-situation-box">' +
									'<div class="patrol-situation-person-mesg ovfl">' +
									'<div class="patrol-situation-person-img region float-L"></div>' +
									'<ul class="float-L" style="width:1.5rem;">' +
									'<li class="patrol-situation-person-time">' + time + '</li>' +
									'<li class="patrol-situation-person">巡更人员：' + username + '</li>' +
									'<li class="patrol-situation-person">账号：' + jobNum + '</li>' +
									'</ul></div>';
							//判断是否有异常信息
							if (status === 1) {
								//正常
								itemAppend += '<div class="patrol-situation-mesg-box">' +
										'<div class="patrol-situation-mesg-normal">全程巡更正常</div></div>';

							}
							if (status === 2) {
								//异常
								itemAppend += '<div class="patrol-situation-mesg-box">' +
										'<div class="patrol-situation-mesg-abnormal">' +
										'<p class="patrol-situation-mesg-abnormal-tit">巡更异常</p>' +
										'<ul class="patrol-situation-abnormal-show hidden-latter">';
								//有异常需要进行
								var exceptionVos = showData[i].exceptionVOs;
								if (exceptionVos !== undefined) {
									for (var j = 0; j < exceptionVos.length; j++) {
										var exceptionObj = exceptionVos[j];
										var createtime = exceptionObj.createtime;
										var exceptionMessage = exceptionObj.exception_name;
										var message = createtime + ":" + " " + exceptionMessage;
										itemAppend += '<li>' + message + '</li>';
									}
								}
								itemAppend += '</ul><div class="patrol-situation-abnormal-more">更多</div>'
							}
							itemAppend += '</div></div>';
							liObj.append(itemAppend);
                            myScroll.refresh();
						}
					}
				}
			};


			/**
			 * 进行保存regionId
			 */
			var saveRegionId = function (data) {
				regionId = data;
			};


			/**
			 *
			 * 当点击关闭自定义时间时默认显示初始页面信息
			 *
			 */
			$(".delete-date-result").click(function () {
				type = 0;
				dateType = 1;
				clearDate();
				ajaxRequestDate(0,1,page,pageSize,regionId,startDate,endDate);
				activeChange();
			});


			/**
			 * 通过选择时间触发事件
			 * @param dateTypeValue	0 30天,1 7天,2 今天,3 自定义时间
             */
			var dateOnclick = function (dateTypeValue) {
				page = 1;
				dateType = dateTypeValue;
				//设置cookie
                saveCookie(dateType,type);
                clearDate();
				ajaxRequestDate(type,dateType,page,pageSize,regionId,null,null);
				//进行选择
				activeChange();
				//进行数据显示
			};

			/**
			 * 通过选择类型进行触发事件
			 * @param typeValue 异常类型 0 全部,1,异常,2正常
             */
			var typeOnclick = function (typeValue) {
				page = 1;
				type = typeValue;
				clearDate();
				//自定义事件
				if(dateType == 3) {
					//进行获取时间
					var timeObj = $(".date-select-result").children("span");
					var obj = $(timeObj[0]);
					var dateValue = obj.text();
					//进行对时间组装
					startDate = dateValue + " " + "00:00:00";
					endDate = dateValue + " " + "23:59:59";
				}

				//设置cookie
                saveCookie(dateType,type);

				ajaxRequestDate(type,dateType,page,pageSize,regionId,startDate,endDate);
				activeChange();
			};

			/**
			 * 当选择时间点击确定
			 */
			var calendar = new datePicker();
			calendar.init({
				'trigger': '#dateBtn', /*按钮选择器，用于触发弹出插件*/
				'type': 'date',//模式：date日期；datetime日期时间；time时间；ym年月；year 年；年月日+时分秒：dateTimeSecond； month 月；dateHour
				'minDate':'1900-1-1',/*最小日期*/
				'maxDate':'2100-12-31',/*最大日期*/
				'onSubmit':function(){/*确认时触发事件*/
					var theSelectData=calendar.value;
					console.log(calendar.value);
					$(".date-select-result span").text(theSelectData);
					$(".date-tab-list").hide();
					$(".date-select-result").show();
					//这里进行请求数据
					dateType = 3;
					//这里进行获取时间
					var start = theSelectData + ' ' + '00:00:00';
					var end = theSelectData + ' ' + '23:59:59';
					//进行清理数据
					page = 1;
					clearDate();
					ajaxRequestDate(type,dateType,page,pageSize,regionId,start,end);
				},
				'onClose':function(){/*取消时触发事件*/
				}
			});
			/**
			 * 通过ajax进行获取数据
             */
			var ajaxRequestDate = function (type,dateType,page,pageSize,regionId,startTime,endTime) {
				//下面进行参数拼装
				var param = '';
				if(dateType == 3) {
					param = 'type=' + type + "&dateType=" + dateType + '&regionId=' + regionId + '&page='
							+ page + '&pageSize=' + pageSize + '&startTime=' + startTime + '&endTime=' + endTime;
				}else {
					param = 'type=' + type + '&regionId='+ regionId + "&dateType=" + dateType + '&page='
							+ page + '&pageSize=' + pageSize;
				}

				$.ajax({
					url: '<%=basePath%>statistics/getPatrolAreaData?' + param,
					type: 'get',
//					contentType:'application/json',
//					dataType:'json',
					success:function(data) {
						//这里进行数据获取
						if(data != undefined && data != "") {
							var ajaxData = JSON.parse(data);
							//进行数据渲染
							showDate(data);
						}
					},
					error:function() {}

				});
			};


			/**
			 * 进行数据渲染
             */
			var showDate = function (data) {
				console.log(data);
				var parseData = JSON.parse(data);

				//进行设置记录条数
				if(parseData.properties != undefined && parseData.properties != "") {
					var allCount = parseData.properties.countVO.allCount;
					var exceptionCount = parseData.properties.countVO.exceptionCount;
					var normalCount = parseData.properties.countVO.normalCount;
					//这里进行设置
					$("#allCount").text(allCount);
					$("#exceptionCount").text(exceptionCount);
					$("#normalCount").text(normalCount);
				}


				//进行设置总页数和总记录条数
				totalCount = parseData.data.totalCount;
				totalPage = parseData.data.totalPage;

				var showData = parseData.data.list;
				var _this = $("#patrol-situation-list");
				if(showData != undefined && showData.length > 0) {
					for (var i = 0; i < showData.length; i++) {
						var item = showData[i];
						var titleDate = item.date;
						var starttime = item.startTime.split(' ')[1];

						var endtime = "";
						if(item.endTime != undefined && item.endTime != null) {
                            endtime = item.endTime.split(' ')[1];
						}
						var time = starttime + "~" + endtime;
						var id = item.id;
						var username = item.username;
						var status = item.status;
						var jobNum = item.jobNum;
						var regionId = item.regionId;

						//下面进行保存regionId
						saveRegionId(regionId);
						//进行判断是否存在日期

						var itemAppend = '';
						var this_li_id = $("#" + titleDate).attr("id");
						if (this_li_id === undefined) {
							//不存在
							itemAppend += '<li id="' + titleDate + '">' +
									'<p class="patrol-situation-date" value="' + titleDate + '">' + titleDate + '</p>' +
									'<div class="patrol-situation-box">' +
									'<div class="patrol-situation-person-mesg ovfl">' +
									'<div class="patrol-situation-person-img region float-L"></div>' +
									'<ul class="float-L" style="width:1.5rem;">' +
									'<li class="patrol-situation-person-time">' + time + '</li>' +
									'<li class="patrol-situation-person">巡更人员：' + username + '</li>' +
									'<li class="patrol-situation-person">账号：' + jobNum + '</li>' +
									'</ul></div>';
//								'</li>';
							//判断是否有异常信息
							if (status === 1) {
								//正常
								itemAppend += '<div class="patrol-situation-mesg-box">' +
										'<div class="patrol-situation-mesg-normal">全程巡更正常</div></div>';
							}
							if (status === 2) {
								//异常
								itemAppend += '<div class="patrol-situation-mesg-box">' +
										'<div class="patrol-situation-mesg-abnormal">' +
										'<p class="patrol-situation-mesg-abnormal-tit">巡更异常</p>' +
										'<ul class="patrol-situation-abnormal-show hidden-latter">';
//									'<div class="patrol-situation-abnormal-more">更多</div>';
								//有异常需要进行
								var exceptionVos = showData[i].exceptionVOs;
								for (var j = 0; j < exceptionVos.length; j++) {
									var exceptionObj = exceptionVos[j];
									var createtime = exceptionObj.createtime;
									var exceptionMessage = exceptionObj.exception_name;
									var message = createtime + ":" + " " + exceptionMessage;
									itemAppend += '<li>' + message + '</li>';
								}
								itemAppend += '</ul><div class="patrol-situation-abnormal-more">更多</div>'
							}
							itemAppend += '</div></div></li>';
							_this.append(itemAppend);
                            myScroll.refresh();
						} else {
							//存在
							var liObj = $("#" + titleDate);

							itemAppend += '<div class="patrol-situation-box">' +
									'<div class="patrol-situation-person-mesg ovfl">' +
									'<div class="patrol-situation-person-img region float-L"></div>' +
									'<ul class="float-L" style="width:1.5rem;">' +
									'<li class="patrol-situation-person-time">' + time + '</li>' +
									'<li class="patrol-situation-person">巡更人员：' + username + '</li>' +
									'<li class="patrol-situation-person">账号：' + jobNum + '</li>' +
									'</ul></div>';
							//判断是否有异常信息
							if (status === 1) {
								//正常
								itemAppend += '<div class="patrol-situation-mesg-box">' +
										'<div class="patrol-situation-mesg-normal">全程巡更正常</div></div>';

							}
							if (status === 2) {
								//异常
								itemAppend += '<div class="patrol-situation-mesg-box">' +
										'<div class="patrol-situation-mesg-abnormal">' +
										'<p class="patrol-situation-mesg-abnormal-tit">巡更异常</p>' +
										'<ul class="patrol-situation-abnormal-show hidden-latter">';
								//有异常需要进行
								var exceptionVos = showData[i].exceptionVOs;
								if (exceptionVos !== undefined) {
									for (var j = 0; j < exceptionVos.length; j++) {
										var exceptionObj = exceptionVos[j];
										var createtime = exceptionObj.createtime;
										var exceptionMessage = exceptionObj.exception_name;
										var message = createtime + ":" + " " + exceptionMessage;
										itemAppend += '<li>' + message + '</li>';
									}
								}
								itemAppend += '</ul><div class="patrol-situation-abnormal-more">更多</div>'
							}
							itemAppend += '</div></div>';
							liObj.append(itemAppend);
                            myScroll.refresh();
						}
					}
				}
			};


			/**
			 * 进行清空渲染出来的数据
             */
			var clearDate = function () {
				$("#patrol-situation-list").html("");
			};


			/**
			 * 清除当前选择内容,并进行重新选择
             */
			var activeChange = function () {


				//进行删除
				var dateLi = $(".date-tab-list").children("li");
				if(dateLi !== undefined && dateLi.length > 0) {
					for(var i = 0;i < dateLi.length;i++) {
						var obj =  dateLi[i];
						obj = $(obj);
						if(obj.hasClass("active")) {
							obj.removeClass("active");
						}
					}
				}
				var typeLi = $(".situation-tab-list").children("li");
				if(typeLi !== undefined && typeLi.length > 0) {
					for(var i = 0;i < typeLi.length;i++) {
						var obj = typeLi[i];
						obj = $(obj);
						if(obj.hasClass("active")) {
							obj.removeClass("active");
						}
					}
				}
				//进行添加
				if(type == 0) {
					//全部
					$("#selectAll").addClass("active");
				}
				if(type == 1) {
					//异常
					$("#selectException").addClass("active");
				}
				if(type == 2) {
					//正常
					$("#selectNormal").addClass("active");
				}
				if(dateType == 0) {
					//30天
					$("#longtestTitle").addClass("active");
				}
				if(dateType == 1) {
					//7天
					$("#mediumTitle").addClass("active");
				}
				if(dateType == 2) {
					//今天
					$("#nowTitle").addClass("active");
				}
				if(dateType == 3) {
					//自定义事件
				}
			};






			//上拉加载，下拉刷新
			refresher.init({
				id:"wrapperList",
				pullDownAction:Refresh,
				pullUpAction:Load
			});

			function Refresh() {
				setTimeout(function () {
					window.location.reload();
				}, 1000);
			}

			//首次执行
			Load();

			//加载内容
			function Load() {
				//TODO 这里需要进行数据条数判断
				if(page == 1) {
					page += 1;
				}
				if (page != 1 && page <= totalPage) {
					setTimeout(function () {
						//这里进行加载数据
						if(dateType == 3) {
							//自定义时间
							var timeObj = $(".date-select-result").children("span");
							var obj = $(timeObj[0]);
							var dateValue = obj.text();

							//进行对时间组装
							startDate = dateValue + " " + "00:00:00";
							endDate = dateValue + " " + "23:59:59";
						}
						ajaxRequestDate(type, dateType, page, pageSize, regionId,startDate,endDate);
						if(page >= 2) {
							page += 1;
						}
						if(page >= totalPage) {
							page = totalPage;
						}
					}, 1000);
				} else{
					$(".pullUpLabel").text("没有更多内容了...")
				}
			};
			
		</script>
	</body>
</html>
