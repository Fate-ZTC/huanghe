<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.parkbobo.utils.Configuration" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

    final String API = Configuration.getInstance().getValue("map_api_url");
    final String LMap_APIURL = Configuration.getInstance().getValue("map_LMap_APIURL");
    final String LMap_MAPSERVERURL = Configuration.getInstance().getValue("map_LMap_MAPSERVERURL");

%>
<html>
<head>
    <meta charset="UTF-8">
    <title>查看位置</title>
    <script src="<%=basePath%>v2/lmap.2.1.min.js"></script>
    <script src="<%=basePath%>v2/lqutil.js"></script>
    <script src="<%=API%>"></script>
    <link rel="stylesheet" href="<%=basePath%>page/css/common-PC.css" />
    <link rel="stylesheet" href="<%=basePath%>page/css/Qstyle.css" />
    <script src="<%=basePath%>page/js/jquery.js" type="text/javascript"></script>
    <script src="https://cdn.bootcss.com/html5media/1.1.8/html5media.js"></script>
</head>
<style>
  #campus-name{
    position: absolute;
    top: 20px;
    left: 20px;
    padding: 6px 18px;
    color: #fff;
    background: #3692c8;
    z-index: 999;
  }
</style>
<body>
  <div id="map" style="width: 100%;height: 100%;"></div>
  <!--地图放大、缩小功能-->
  <div class="right-tool-box">
      <div class="index-zoom">
          <!--放大-->
          <span class="zoom-in" onclick="zoomIn()"></span>
          <!--缩小-->
          <span class="zoom-out" onclick="zoomOut()"></span>
      </div>
  </div>
  <div id="campus-name">

  </div>
</body>
<script>
  var query = parseUrlQuery()
  LMap.APIURL = "<%=LMap_APIURL%>";
  LMap.MAPSERVERURL = "<%=LMap_MAPSERVERURL%>";
  var zoreid = parseInt(query.zoreid);
  var map = new LMap.Map2D("map",zoreid);
  /**
    * 放大地图比例
    */
  var zoomIn = function(){
      map.zoomIn();
  };
  /**
    * 缩小地图比例
    */
  var zoomOut = function(){
      map.zoomOut();
  };
  var sosMarkerLayer = new LMap.Layer.Vector('sosMarkerLayer',{
    styleMap:map.iconStyle({'src':'\${icon}','width':40,'height':40})
  });
  var markerFeaure = new LMap.Feature.Vector(
    new LMap.Geometry.Point(query.lon,query.lat).transform(proj, map.getProjectionObject()),{
      icon:'page/images/sos.png'
    }
  );
  sosMarkerLayer.addFeatures(markerFeaure);
  map.addLayer(sosMarkerLayer);

  map.setCenter([query.lon,query.lat])

  $('#campus-name').html(mapConfig.name)

  function parseUrlQuery(){
    let query = window.location.search || null
    if(!query) return
    let params = {}
    query = query.replace('?','')
    query.split('&').forEach(element=>{
      params[element.split('=')[0]] = element.split('=')[1]
    })
    return params
  }
</script>
</html>
