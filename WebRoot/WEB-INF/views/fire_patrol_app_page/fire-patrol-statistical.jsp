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
		<%--保存用户工号--%>
		<input type="hidden" name="jobNum" value="${jobNum}" id="jobNum">
		<div class="swiper-container swiper-container-horizontal swiper-container-free-mode listPage-swiper">
		    <div id="selectBar" class="swiper-wrapper" style="">
		      <%--<a class="swiper-slide swiper-slide-active active" id="allActive" value="0">全部</a>--%>

		    </div>
		</div>
		<div class="box f14 inspection-record-box">
			<div class="inspection-record-top">
				<div class="month-select-box">
					<c:if test="${isThisMonth}">
						<span id="showDateSpan">本月</span>
					</c:if>
					<div class="date-select-btn" id="dateBtn"><input type="hidden" name="month" id="month" value="" /></div>
				</div>
				<ul class="inspection-record-sum" id="statisticalNum">
					<%--<li>巡查次数：600</li>--%>
					<%--<li>巡查设备数：600</li>--%>
					<%--<li>正常设备数：600</li>--%>
					<%--<li>异常设备数：600</li>--%>
				</ul>
			</div>
			
			<div class="inspection-record-list-box" id="inspection-record-wrapper-box">
				<ul class="inspection-record-list" id="pageBeanList">
					<%--<li>--%>
						<%--<div class="inspection-record-mesg-box">--%>
							<%--<img src="<%=basePath%>fire_patrol_app_page/img/inspection-record.png" class="inspection-record-img" alt="">--%>
							<%--<div class="inspection-record-text-mesg">--%>
								<%--<div class="inspection-record-basic-infor">--%>
									<%--<span class="inspection-record-name">设备001</span>--%>

								<%--</div>--%>
								<%--<p>设备位置：一号教学楼2层</p>--%>
								<%--<p>巡查时间：2018.04.28 </p>--%>
							<%--</div>--%>

						<%--</div>--%>
						<%--<!-- abnormal存在为异常状态，不存在则为正常 -->--%>
						<%--<div class="inspection-record-status-box abnormal">--%>
							<%--<span class="inspection-record-status">异常</span>--%>
							<%--<a href="" class="record-abnormal-btn">异常原因</a>--%>
						<%--</div>--%>
					<%--</li>--%>
					<%--<li>--%>
						<%--<div class="inspection-record-basic-infor">--%>
							<%--<span class="inspection-record-name">设备001</span>--%>
							<%--<span class="inspection-record-status">正常</span>--%>
						<%--</div>--%>
						<%--<p>设备位置：一号教学楼2层</p>--%>
						<%--<p>巡查时间：2018.04.28 </p>--%>
					<%--</li>--%>
					<%--<li>--%>
						<%--<div class="inspection-record-basic-infor">--%>
							<%--<span class="inspection-record-name">设备001</span>--%>
							<%--<span class="inspection-record-status">正常</span>--%>
						<%--</div>--%>
						<%--<p>设备位置：一号教学楼2层</p>--%>
						<%--<p>巡查时间：2018.04.28 </p>--%>
					<%--</li>--%>
					<%--<li>--%>
						<%--<div class="inspection-record-basic-infor">--%>
							<%--<span class="inspection-record-name">设备001</span>--%>
							<%--<span class="inspection-record-status">正常</span>--%>
						<%--</div>--%>
						<%--<p>设备位置：一号教学楼2层</p>--%>
						<%--<p>巡查时间：2018.04.28 </p>--%>
					<%--</li>--%>
					<%--<li>--%>
						<%--<div class="inspection-record-basic-infor">--%>
							<%--<span class="inspection-record-name">设备001</span>--%>
							<%--<span class="inspection-record-status">正常</span>--%>
						<%--</div>--%>
						<%--<p>设备位置：一号教学楼2层</p>--%>
						<%--<p>巡查时间：2018.04.28 </p>--%>
					<%--</li>--%>
				</ul>
			</div>
		</div>
	</body>

	<script src="<%=basePath%>fire_patrol_app_page/js/iscroll.js"></script>
	<script src="<%=basePath%>fire_patrol_app_page/js/pullToRefresh.js"></script>
	<script src="<%=basePath%>fire_patrol_app_page/swiper/swiper-3.4.2.jquery.min.js"></script>
	<script>


        //顶部导航功能
        // var length = $(".swiper-slide").length;
        // if (length < 5) {
        //     $(".swiper-slide").addClass("flex");
        //     swiperClick();
        // }else{
        //     var swiper = new Swiper('.swiper-container', {
        //         slidesPerView: 'auto',
        //         freeMode: true,
        //         onInit: function() {
        //             swiperClick();
        //         }
        //     });
        // }
        //
        // function swiperClick() {
        //     $(".swiper-slide").click(function(){
        //         $(this).addClass("list-tab-active").siblings(".swiper-slide").removeClass("list-tab-active");
        //     })
        // }

        //顶部导航功能

        function startSwiper(){
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
                $(".swiper-slide").unbind("click");
                $(".swiper-slide").click(function(){
                    $(this).addClass("active").siblings(".swiper-slide").removeClass("active");
                })
            }
        }
        // 顶部切换数据加载完成后调用此方法
        startSwiper();

		//页面参数
        var pageSize = 2;
        var page = 1;
        var buildingType = 0;
        var isNextPage = false;

        $(function () {
            ajaxRequstData();
        });

		var ajaxRequstData = function () {

		    //这里进行组装参数
			var jobNum = $("#jobNum").val();
			var itemObj = $("#selectBar a");
			// var buildingType = 0;
            var dateStr = '';

            if(itemObj != undefined && itemObj != null) {
				for(var i = 0; i < itemObj.length;i++) {
				    var item = $(itemObj[i]);
				    if(item.hasClass("active")) {
				        //如果有这个
						buildingType = item.attr('value');
					}
				}
			}

            //获取时间内容
			dateStr = $("#showDateSpan").text();


			//这里进行获取元素标签
			var param = '';
			if(jobNum !== '') {
			    param += 'jobNum=' + jobNum;
			}
			if(dateStr !== undefined && dateStr !== '' && dateStr !== '本月') {
                    param += '&date=' + dateStr;
			}
			param += '&buildingType=' + buildingType;
			param += '&page=' + page + '&pageSize=' + pageSize;



            $.ajax({
                url: '<%=basePath%>getFirePatrolUseStatisticsData?' + param,
                type: 'get',
                success:function(data) {
                    //这里进行数据获取
                    if(data != undefined && data != "") {
                        var ajaxData = JSON.parse(data);
                        //TODO 这里进行数据渲染
						if(ajaxData.status && ajaxData !== undefined && ajaxData !== ""
							&& ajaxData !== null && ajaxData.message != '没有巡查设备') {
                            showData(ajaxData);
                        }
                    }
                },
                error:function() {}

            });
        };


		//进行数据渲染
		var showData = function (data) {
			//渲染顶部导航栏
            var firePatrolUseBarListVO = "";
            var firePatrolUseEquNumVO = "";
            var list = '';


            if(data.data.firePatrolUseBarListVO != undefined) {
                firePatrolUseBarListVO = data.data.firePatrolUseBarListVO;
			}
			if(data.data.firePatrolUseEquNumVO != undefined) {
                firePatrolUseEquNumVO = data.data.firePatrolUseEquNumVO;
			}
			if(data.data.list != undefined) {
                list = data.data.list;
			}

			if(data.data.buildingType != undefined) {
				buildingType = data.data.buildingType;
				console.log(buildingType);
			}

			if(data.nextPage != undefined) {
				isNextPage = data.nextPage;
			}



			//渲染顶部导航栏
			if(firePatrolUseBarListVO !== undefined && firePatrolUseBarListVO != "") {
                var item = firePatrolUseBarListVO.firePatrolBuildingTypes;
                var selectBar = $("#selectBar");
                var result = '';
                for(var i = 0; i < item.length;i++) {
					//这里进行设置相关信息
					//获取type值和名称
					result += '<a class="swiper-slide swiper-slide-active active" id="allActive" value="'+item[i].type+'">'+item[i].name+'</a>'
				}
				selectBar.append(result);
                removeActiveClass();
                addActiveClass();
                startSwiper();
			}

			//渲染统计数据
			if(firePatrolUseEquNumVO !== undefined && firePatrolUseEquNumVO != "") {
				var statisticalNum = $("#statisticalNum");
                var result = '<li>巡查次数：'+firePatrolUseEquNumVO.totalcheckcount+'</li>' +
					'<li>巡查设备数：'+firePatrolUseEquNumVO.allCount+'</li>' +
					'<li>正常设备数：'+firePatrolUseEquNumVO.normalCount+'</li>' +
					'<li>异常设备数：'+firePatrolUseEquNumVO.exceptionCount+'</li>';
                //设置时间
				$("#showDateSpan").text(firePatrolUseEquNumVO.month);
				statisticalNum.append(result);
			}else {
                var statisticalNum = $("#statisticalNum");
                var result = '<li>巡查次数：0</li>' +
                    '<li>巡查设备数：0</li>' +
                    '<li>正常设备数：0</li>' +
                    '<li>异常设备数：0</li>';
                $("#showDateSpan").text(firePatrolUseEquNumVO.month);
                statisticalNum.append(result);
			}


			//渲染pageBean
			if(list !== undefined && list != "") {
				var pageBeanList = $("#pageBeanList");
				result = '';
				if(list != undefined && list.length > 0) {
					for(var i = 0;i< list.length;i++) {
						//获取相关数据
                        var name = '';
                        var locationName = '';
                        var status = '';
                        var lastUpdateTime = '';
                        var statusName = '正常';
						if(item[i].name !== undefined) {
                            name = list[i].name;
						}
						if(locationName !== undefined) {
                            locationName == list[i].locationName;
						}
						if(list[i].status !== undefined) {
                            status = list[i].status;
						}
						if(list[i].lastUpdateTime !== undefined) {
							//这里还需要进行对时间转换
                            lastUpdateTime = timestampToTime(list[i].lastUpdateTime);
						}


						var imageUrl =  "<%=basePath%>fire_patrol_app_page/img/inspection-record.png";
                        result += '<li><div class="inspection-record-mesg-box">' +
							'<img src="'+imageUrl+'" class="inspection-record-img" alt="">' +
							'<div class="inspection-record-text-mesg">' +
							'<div class="inspection-record-basic-infor">' +
							'<span class="inspection-record-name">'+name+'</span>' +
							'</div><p>设备位置：'+locationName+'</p><p>巡查时间：'+lastUpdateTime+'</p></div></div>';

                        //进行状态判断
                        if(status == 0) {
                            statusName = '异常';
                            //异常，TODO 进行跳转
                            result += '<div class="inspection-record-status-box abnormal">' +
								'<span class="inspection-record-status">异常</span>' +
								'<a href="" class="record-abnormal-btn">异常原因</a></div>'
                        }else {
                            statusName = '正常';
                            //正常
                            result += '<div class="inspection-record-status-box">' +
								'<span class="inspection-record-status">正常</span>' +
								'<a href="" class="record-abnormal-btn">异常原因</a>' +
								'</div>'
                        }
                        result += '</li>';
					}
                    pageBeanList.append(result);
                    myScroll.refresh();
				}
			}
        };


        /**
		 * 添加选中
         */
		var addActiveClass = function () {
			//这里进行选择导航栏
            var selectBars = $("#selectBar a");
            //这里进行选中操作
			if(selectBars != undefined && selectBars.length > 0) {
				for(var i = 0;i < selectBars.length;i++) {
                    var item = $(selectBars[i]);
				    var value = item.attr("value");
				    if(value == buildingType) {
				        //进行设置
                        item.addClass("active");
					}
				}
			}
        };

        /**
		 * 删除选中
         */
		var removeActiveClass = function () {
            var selectBars = $("#selectBar a");
            //这里进行选中操作
            if(selectBars != undefined && selectBars.length > 0) {
                for(var i = 0;i < selectBars.length;i++) {
                    var item = $(selectBars[i]);
					if(item.hasClass("active")) {
                        item.removeClass("active")
					}
                }
            }
        };


        function timestampToTime(timestamp) {
            var date = new Date(timestamp * 1000);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
            Y = date.getFullYear() + '-';
            M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
            D = date.getDate() + ' ';
            h = date.getHours() + ':';
            m = date.getMinutes() + ':';
            s = date.getSeconds();
            return Y+M+D+h+m+s;
        }


        //时间选择
        var calendar = new datePicker();
        calendar.init({
            'trigger': '#dateBtn', 		/*按钮选择器，用于触发弹出插件*/
            'type': 'ym',				//模式：date日期；datetime日期时间；time时间；ym年月；year 年；年月日+时分秒：dateTimeSecond； month 月；dateHour
            'minDate':'1900-1-1',		/*最小日期*/
            'maxDate':'2100-12-31',		/*最大日期*/
            'onSubmit':function(){		/*确认时触发事件*/
                var theSelectData = calendar.value;
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


                        myScroll.refresh();
                        paga++;
                        pagaTotal=3;

                        if(isNextPage) {

                            $(".pullUpLabel").text("没有更多内容了...")
                        }

                    }, 1000);
                } else{
                    $(".pullUpLabel").text("没有更多内容了...")
                }
            }
        });

	</script>
</html>
