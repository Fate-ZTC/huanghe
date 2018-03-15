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
		<title>停车状态</title>
	</head>
	<body>
	    <link rel="stylesheet" href="<%=basePath%>tsp/page/css/state.css">
		<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
		<script src="<%=basePath %>/layer_mobile/layer.js"></script>
		<style>
		#carleave{
			width: 70%;
	    	margin: 1.5rem .7rem .3rem;
		}
		.noparking{
			margin-left: 1rem;
		    font-size: .15rem;
		    color: #949494;
		}
		</style>
		<section id="stateWrapper">
		    <div id="carNumSelectPanel">
		        <div class="carNumSelect">
		            <span class="carNum" id="carNum">${usersCars[0].carPlate}</span>
		            <div class="arrow" id="arrow"></div>
		            <div class="carNumSelect_opt" id="carNumSelect_opt">
		                <ul class="opt_ul">
		                <c:forEach items="${usersCars}" var="uc">
		                    <li class="opt_item"><span class="carNum">${uc.carPlate}</span></li>
		                </c:forEach>
		                </ul>
		            </div>
		        </div>
		        <div class="UnboundPlate">
		            <span>未绑定车牌？</span>
		        </div>
		    </div>
			<div class="carleave" style="display:none;">
	            <img alt="" src="<%=basePath%>tsp/page/img/car-go.png" id="carleave">
	            <span class="noparking">当前还没有停车状态</span>
	        </div>
		    <div class="carInfoWrapper">
		        <ul class="carInfo">
		            <li class="carInfo_li">
		                <span class="carInfo_title">车牌号</span>
		                <span class="carInfo_details" id="carNumber">粤A·1Z574</span>
		            </li>
		            <li class="carInfo_li">
		                <span class="carInfo_title">停车场</span>
		                <span class="carInfo_details" id="parkName">白招牌停车牌</span>
		            </li>
		            <li class="carInfo_li">
		                <span class="carInfo_title">车位号</span>
		                <span class="carInfo_details" id="berthid">B1层22号车位</span>
		            </li>
		        </ul>
		    </div>
		    <div class="carInfoWrapper">
		        <ul class="carInfo">
		            <li class="carInfo_li">
		                <span class="carInfo_title">入场时间</span>
		                <span class="carInfo_details" id="enterTime">2018-02-02</span>
		            </li>
		            <li class="carInfo_li">
		                <span class="carInfo_title">停车时长</span>
		                <span class="carInfo_details" id="stayTime">2小时22分</span>
		            </li>
		            <li class="carInfo_li">
		                <span class="carInfo_title">应用缴费</span>
		                <span class="carInfo_details" id="shouldPay">8元</span> <span class="costDesc">(首停1元/时 每增1元/时)</span>
		            </li>
		        </ul>
		    </div>
		    <div class="carInfoWrapper carImgWrapper">
		        <div><span>图片</span></div>
		        <img class="carImg" src="" alt="未捕捉到图片">
		    </div>
		</section>
</body>
<script>
	var path = '<%=basePath %>';
    var $arrow = $('#arrow');
    var $carNumSelect_opt = $('#carNumSelect_opt');
    var $opt_item = $('.opt_item');
    var $carNum = $('#carNum');
    var selectedCarNum = $carNum.text();
    var carNumArr = [];
    for(var i=0;i<$opt_item.length;i++){
        carNumArr.push($opt_item[i]);
        if($opt_item[i].innerText == selectedCarNum){
            $opt_item.eq(i).hide();
        }
    }
    $arrow.click(function () {
        if($carNumSelect_opt.is(':hidden')){
            $carNumSelect_opt.show();
            $arrow.css("background-image","url("+path+"'img/up.png')");
        }else{
            $carNumSelect_opt.hide();
            $arrow.css("background-image","url("+path+"'img/down.png')");
        }
    });
    $opt_item.click(function (e) {
        $carNum.html(e.target.innerText);
        $.get(path+'carNumberSer',{'carNumber':e.target.innerText},function(data){
	 		console.log(data);
	   		if(data.status=='true'){
	   			var para = data.para;
	   			$('.carInfoWrapper').show();
	   			$('.carleave').hide();
	   			$('#carNumber').text(para.carNumber);
	   			$('#parkName').text(para.carparkName);
	   			$('#berthid').text(para.berthNum);
	   			$('#enterTime').text(para.enterTime);
	   			$('#stayTime').text(para.timed);
	   			$('#shouldPay').text(para.shouldPayMoney);
	   			$('.carImg').attr("src",para.carNumImgPath);
	   		}else{
	   			$('.carInfoWrapper').hide();
	   			$('.carleave').show();
	   		}
		},'json')
        $carNumSelect_opt.hide();
        for(var i=0;i<$opt_item.length;i++){
            if($opt_item[i].innerText == e.target.innerText){
                $opt_item.eq(i).hide();
            }else{
                $opt_item.eq(i).show();
            }
        }
        $arrow.css("background-image","url("+path+"'img/down.png')");
    });
    
    $(function(){
    	var carNumber = $('#carNum').text();
    	$.get(path+'carNumberSer',{'carNumber':carNumber},function(data){
	 		console.log(data);
	   		if(data.status=='true'){
	   			var para = data.para;
	   			$('.carInfoWrapper').show();
	   			$('.carleave').hide();
	   			$('#carNumber').text(para.carNumber);
	   			$('#parkName').text(para.carparkName);
	   			$('#berthid').text(para.berthNum);
	   			$('#enterTime').text(para.enterTime);
	   			$('#stayTime').text(para.timed);
	   			$('#shouldPay').text(para.shouldPayMoney);
	   			$('.carImg').attr("src",para.carNumImgPath);
	   		}else{
	   			$('.carInfoWrapper').hide();
	   			$('.carleave').show();
	   		}
		},'json')
    })
    
</script>
</html>

