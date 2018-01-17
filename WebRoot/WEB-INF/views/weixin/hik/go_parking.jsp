<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>停车场引导</title>
    <link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>
    <style type="text/css">
        #panel {
            position: fixed;
            background-color: white;
            max-height: 90%;
            overflow-y: auto;
            top: 10px;
            right: 10px;
            width: 280px;
        }
    </style>
    <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=ded817adc103999ae6f17df90ddf74a9&plugin=AMap.Driving"></script>
    <script type="text/javascript" src="http://cache.amap.com/lbs/static/addToolbar.js"></script>
    <script type="text/javascript" src="<%=basePath %>hik/js/jquery-1.9.1.min.js" ></script>
</head>
<body>
<div id="container"></div>
<div id="panel" style="display: none;"></div>
<input type="hidden" id="latitude" value="${latitude}">
<input type="hidden" id="longitude" value="${longitude}">
<input type="hidden" id="strLat" value="${strLat}">
<input type="hidden" id="strLon" value="${strLon}">
<script type="text/javascript">




    //基本地图加载
	var latitude = $("#latitude").val();
	var longitude = $("#longitude").val();
	var strLon = $("#strLon").val();
	var strLat = $("#strLat").val();
	var lng = new AMap.LngLat(strLon,strLat);
	var newLng;
	AMap.convertFrom(lng,'gps',function(status,result){
		newLng = result.locations[0];
		  // 根据起终点经纬度规划驾车导航路线
		driving.search(newLng, new AMap.LngLat(longitude, latitude));
	});
	
    var map = new AMap.Map("container", {
        resizeEnable: true,
        center: [strLon, strLat],//地图中心点
        zoom: 13 //地图显示的缩放级别
    });
    //构造路线导航类
    var driving = new AMap.Driving({
        map: map,
        panel: "panel"
    }); 
   
</script>
</body>
</html>
