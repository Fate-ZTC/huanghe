<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.parkbobo.utils.Configuration" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    //这里进行获取地图地址
    final String API = Configuration.getInstance().getValue("map_api_url");
    final String LMap_APIURL = Configuration.getInstance().getValue("map_LMap_APIURL");
    final String LMap_MAPSERVERURL = Configuration.getInstance().getValue("map_LMap_MAPSERVERURL");

%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>地图控件</title>
    <script src="<%=basePath%>v2/lmap.2.1.min.js"></script>
    <script src="<%=basePath%>v2/lqutil.js"></script>
    <script src="<%=API%>"></script>
    <link rel="stylesheet" href="<%=basePath%>page/css/common-PC.css" />
    <link rel="stylesheet" href="<%=basePath%>page/css/Qstyle.css" />
    <script src="<%=basePath%>page/js/jquery.js" type="text/javascript"></script>
    <script src="https://cdn.bootcss.com/html5media/1.1.8/html5media.js"></script>
    <script type="text/javascript" src="<%=basePath %>/page/layer/layer.js"></script>
    <style>
        .patrol-bomb-box{width: 758px;height: 570px;background: #fff;position: absolute;top: 50%;left: 50%;margin-top: -285px;margin-left: -379px;z-index: 999}
        .patrol-bomb-top{height: 42px;position: relative;line-height: 40px;padding: 0 14px;border-bottom: 1px solid #E8E8E8;}
        .patrol-bomb-close{width: 42px;height: 42px;background: url(<%=basePath%>page/images/patrol-bomb-close.png) no-repeat center;position: absolute;right: 0;top: 0;cursor: pointer;}

        .patrol-bomb-filter-box{padding: 14px 45px;}
        .patrol-bomb-mesg-tit{color: #4097CA;line-height: 36px;margin-bottom: 8px;}
        .patrol-bomb-mesg-tit .number-box{float: left;border-radius: 50%;width: 14px;height: 14px;border: 1px solid #4097CA;line-height: 12px;font-size: 12px;text-align: center;margin-top: 11px;margin-right: 4px;}
        .patrol-name-input{width: 510px;height: 28px;border: 1px solid #D8D8D8;}

        .segmenting-line{height: 5px;background: #f4f4f4;}

        .patrol-mesg-list-box{background: #F6F6F6;padding:10px 20px;}

        .patrol-mesg-list-filter{overflow: hidden;}
        .patrol-mesg-list-filter>li{width: 28%;float: left;}
        .patrol-mesg-list-filter>li:last-of-type{width: 16%;}
        .patrol-mesg-list-filter>li input{width: 115px;height: 28px;border: 1px solid #D8D8D8;}

        .patrol-mesg-list-table-box{height: 241px;overflow-y: auto;}
        .patrol-mesg-list-table{width: 100%;border-left: 1px solid #D8D8D8;border-top: 1px solid #D8D8D8;text-align: center;margin-top: 15px;}
        .patrol-mesg-list-table td,.patrol-mesg-list-table th{border-right: 1px solid #D8D8D8;border-bottom: 1px solid #D8D8D8;height: 30px;font-weight: 500;}

        .patrol-bomb-bottom{padding: 0 50px 10px;}
        .patrol-bomb-reamind{color: #EA573F;line-height: 28px;font-size: 12px;}
        .patrol-bomb-cancel-btn{margin-right: 15px;}

        .common-button{width: 86px;height: 28px;background: #4097CA;color: #fff;text-align: center;padding: 0;cursor: pointer;}
        .common-cancel-button{width: 86px;height: 28px;background: #fff;color: #CFCFCF;text-align: center;padding: 0;cursor: pointer;border: 1px solid #CFCFCF;}

    </style>
</head>
<body>
    <input type="hidden" name="patrolRegion.regionid" value="${id}" id="regionId">
    <div class="box f14 xungeng-map-box">
        <div class="xungeng-map" id="xungeng-map">
            <div id="map" style="width: 100%;height: 100%;"></div>
        </div>

        <!--地图放大、缩小功能-->
        <div class="right-tool-box">
            <div class="index-zoom">
                <!--放大-->
                <span class="zoom-in" onclick="zoomIn()"></span>
                <!--缩小-->
                <span class="zoom-out" onclick="zoomOut()"></span>
            </div>
        </div>

        <div class="demo-video-box">
            <div class="demo-video-close-box">
                <div class="demo-video-close-video"></div>
            </div>
            <video controls loop="true" preload="auto">
                <source src="<%=basePath%>page/video/movie.mp4" type="video/mp4">
                <p style="padding: 50px;color:#fff;text-align: center">您的浏览器不支持 HTML5 video 标签。</p>
            </video>
        </div>

        <div class="patrol-bomb-box" style="display: none;">
            <div class="patrol-bomb-top">
                <span>配置</span>
                <div class="patrol-bomb-close"></div>
            </div>

            <div class="patrol-bomb-filter-box">
                <div class="patrol-bomb-mesg-tit"><div class="number-box">1</div><span>编辑点位名称</span></div>
                <div class="">
                    <label for="">巡更点位名称：</label>
                    <input type="text" class="patrol-name-input" id="point-name">
                </div>
            </div>

            <div class="segmenting-line"></div>

            <div class="patrol-bomb-filter-box">
                <div class="patrol-bomb-mesg-tit"><div class="number-box">2</div><span>选择蓝牙标签</span></div>
                <div class="patrol-mesg-list-box">
                    <ul class="patrol-mesg-list-filter">
                        <li><label for="">UUID：</label><input type="text" id="search-uuid"></li>
                        <li><label for="">major：</label><input type="text" id="search-major"></li>
                        <li><label for="">minor：</label><input type="text" id="search-minor"></li>
                        <li><button class="common-button float-R" onclick="loadBeacon()">搜索</button></li>
                    </ul>
                    <div class="patrol-mesg-list-table-box">
                        <table class="patrol-mesg-list-table" cellspacing="0" cellpadding="0">
                            <tr>
                                <th>UUID</th>
                                <th>major</th>
                                <th>minor</th>
                                <th>选择</th>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>2</td>
                                <td>3</td>
                                <td><input type="radio"></td>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>2</td>
                                <td>3</td>
                                <td><input type="radio"></td>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>2</td>
                                <td>3</td>
                                <td><input type="radio"></td>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>2</td>
                                <td>3</td>
                                <td><input type="radio"></td>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>2</td>
                                <td>3</td>
                                <td><input type="radio"></td>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>2</td>
                                <td>3</td>
                                <td><input type="radio"></td>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>2</td>
                                <td>3</td>
                                <td><input type="radio"></td>
                            </tr>
                            <tr>
                                <td>1</td>
                                <td>2</td>
                                <td>3</td>
                                <td><input type="radio"></td>
                            </tr>

                        </table>
                    </div>

                </div>


            </div>

            <div class="patrol-bomb-bottom">
                <span class="patrol-bomb-reamind">提示：请先编辑点位名称，然后选择该点位配置的蓝牙标</span>
                <button class="common-button float-R" onclick="saveSignPoint()">提交</button>
                <button class="common-cancel-button patrol-bomb-cancel-btn float-R">取消</button>
            </div>

        </div>

        <div id="mask"></div>
    </div>

    <script>

        LMap.APIURL = "<%=LMap_APIURL%>";
        LMap.MAPSERVERURL = "<%=LMap_MAPSERVERURL%>";

        var map = new LMap.Map2D("map", 1001);
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

        var styles = new LMap.StyleMap({
            "default": new LMap.Style(null, {
                rules: [
                    new LMap.Rule({
                        symbolizer: {
                            "Point": {
                                pointRadius: 6,
                                graphicName: "#02a6cf",
                                fillColor: "#02a6cf",
                                fillOpacity: 1,
                                strokeWidth: 2,
                                strokeOpacity: 1,
                                strokeColor: "#02a6cf"
                            },
                            "Line": {
                                strokeWidth: 4,
                                strokeOpacity: 1,
                                strokeColor: "#00ccff"
                            },
                            "Polygon":{
                                strokeWidth: 2,
                                strokeOpacity: 1,
                                fillColor: "#02a6c0",
                                strokeColor: "#02a6c0"
                            }
                        }
                    })
                ]
            }),
            "select": new LMap.Style(null,{
                rules: [
                    new LMap.Rule({
                        symbolizer: {
                            "Point": {
                                pointRadius: 6,
                                graphicName: "circle",
                                fillColor: "white",
                                fillOpacity: 0.5,
                                strokeWidth: 1,
                                strokeOpacity: 1,
                                strokeColor: "#02a6cf"
                            },
                            "Line": {
                                strokeWidth: 4,
                                strokeOpacity: 1,
                                strokeColor: "#00ccff"
                            },
                            "Polygon":{
                                strokeWidth: 2,
                                strokeOpacity: 1,
                                strokeColor: "#00ccff"
                            }
                        }
                    })
                ]
            }),
            "temporary": new LMap.Style(null, {
                rules: [
                    new LMap.Rule({
                        symbolizer: {
                            "Point": {
                                pointRadius: 6,
                                graphicName: "circle",
                                fillColor: "white",
                                fillOpacity: 0.5,
                                strokeWidth: 1,
                                strokeOpacity: 1,
                                strokeColor: "#02a6cf"
                            },
                            "Line": {
                                strokeWidth: 4,
                                strokeOpacity: 1,
                                strokeColor: "#00ccff"
                            },
                            "Polygon":{
                                strokeWidth: 2,
                                strokeOpacity: 1,
                                strokeColor: "#02a6cf"
                            }
                        }
                    })
                ]
            })
        });




        var drawLayer = new LMap.Layer.Vector("Draw Layer",{styleMap: styles});
        map.addLayers([drawLayer]);
        var drawControls = {
            point: new LMap.Control.DrawFeature(drawLayer,
                    LMap.Handler.Point
            ),
            line: new LMap.Control.DrawFeature(drawLayer,
                    LMap.Handler.Path
            ),
            polygon: new LMap.Control.DrawFeature(drawLayer,
                    LMap.Handler.Polygon
            ),
            box: new LMap.Control.DrawFeature(drawLayer,
                    LMap.Handler.RegularPolygon, {
                        handlerOptions: {
                            sides: 4,
                            irregular: true
                        }
                    }
            ),
            snapping: new LMap.Control.Snapping({layer1: drawLayer}),
            modify: new LMap.Control.ModifyFeature(drawLayer)
        };
        //square
        //circle
        //ellipse
        map.addControl(drawControls['box']);
        drawControls['box'].featureAdded = function(e) {
            LqUtil.log("box：" + e.geometry.toString());
        };
        var startDrawBox = function(){
            drawControls['box'].activate();
        };
        var stopDrawBox = function(){
            drawControls['box'].deactivate();
        };

        map.addControl(drawControls['line']);
        drawControls['line'].featureAdded = function(e) {
            LqUtil.log("line：" + e.geometry.toString());
            stopDrawLine();
            startEdit();
            //alert("长度：" + map.distanceLine(e) +"米");

            drawControls['modify'].selectFeature(e);
            //drawControls['modify'].setFeatureState();
        };


        var startDrawLine = function(){
            var control = drawControls['line'];
            drawControls['line'].activate();
            var t = control;
        };
        var stopDrawLine = function(){
            drawControls['line'].deactivate();
        };




        map.addControl(drawControls['modify']);
        var startEdit = function(){
            drawControls['modify'].activate();
        };
        var stopEdit = function(){
            drawControls['modify'].deactivate();
        };
        drawControls['modify'].onModificationEnd = function(e){
            alert(drawLayer.getFeature().geometry.toString());
        };

        map.addControl(drawControls['polygon']);
        var startDrawPolygon = function(){
            drawControls['polygon'].activate();
        };

        /**
         * 结束绘制
         */
        var stopDrawPolygon = function(){
            drawControls['polygon'].deactivate();
            //显示提交相关内容
            $(".xungeng-map-drawing-end-box").css('display','block');
            $(".xungeng-map-drawing-prompt-box").hide();
        };


        LMap.Event.observe(document, "keydown", function(evt) {
            var handled = false;
            switch (evt.keyCode) {
                case 90: // z
                    if (evt.metaKey || evt.ctrlKey) {
                        //drawControls['polygon'].undo();
                        // drawControls['box'].undo();

                        drawControls['line'].undo();
                        handled = true;
                    }
                    break;
                case 89: // y
                    if (evt.metaKey || evt.ctrlKey) {
                        //drawControls['polygon'].redo();
                        //drawControls['box'].redo();
                        drawControls['line'].redo();
                        handled = true;
                    }
                    break;
                case 27: // esc
                    //drawControls['polygon'].cancel();
                    //drawControls['box'].cancel();
                    drawControls['line'].cancel();
                    handled = true;
                    break;
            }
            if (handled) {
                LMap.Event.stop(evt);
            }
        });


//        "geometry":{
//            "type": "GeometryCollection",
//                    "geometries": [
//                {"type":"Polygon","coordinates":[[[104.119335337292,30.61427111586],[104.120869560857,30.6141972486362],[104.120869560857,30.6127937606711],[104.119174404755,30.6127014252239],[104.119335337292,30.61427111586]]]}
//            ]
//        },
//        "type": "Feature",
//                "properties": {
//            "gid":4,
//                    "name":'绘制测试',
//                    "color":'#ffff00'
//        }


        /**
         *
         * 画面
         *
         */
        drawControls['polygon'].featureAdded = function(e) {
//            LqUtil.log("line：" + e.geometry.transform(map.getProjectionObject(), new LMap.Projection("EPSG:4326")).toString());
            //进行保存数据
//            console.log(e.geometry.transform(map.getProjectionObject(), new LMap.Projection("EPSG:4326")).toString());
            polygon = e.geometry.transform(map.getProjectionObject(),new LMap.Projection("EPSG:4326")).toString();

            stopDrawLine();
            startEdit();
            //alert("长度：" + map.distanceLine(e) +"米");
            drawControls['modify'].selectFeature(e);
            //drawControls['modify'].setFeatureState();


        };


        //数据回显所需要的样式
        var color = $("#colorDraw").val();
        var layer1 = new LMap.Layer.Vector("showDrawRegion",{
            styleMap:new LMap.StyleMap({
                "default":new LMap.Style({
                    cursor:'pointer',
                    fillColor: (color == "" || color == undefined) ? "#02a6cf" : color ,
                    fillOpacity: 0.4,
                    strokeColor: (color == "" || color == undefined) ? "#02a6cf" : color ,
                    strokeWidth: 1
                }),
                "select":new LMap.Style({
                }),
                "temporary":new LMap.Style({
                    fillColor: (color == "" || color == undefined) ? "#02a6cf" : color ,
                    fillOpacity: 0.4,
                    strokeColor: (color == "" || color == undefined) ? "#02a6cf" : color ,
                    strokeWidth: 1
                })
            })

        });
        map.addLayers([layer1]);

        //下面进行数据回显操作
        var geoJson =  {
            "type": "FeatureCollection",
            "features": []
        };

        var features = {
            "geometry":{
                "type": "GeometryCollection",
                "geometries": []
            },
            "type": "Feature",
            "properties": {
                "gid":4,
                "name":'绘制测试',
                "color":'#ffff00'
            }
        };
        var geomentrie = {
            "type":"Polygon",
            "coordinates":[]
        };
        var geojsonFormat = new LMap.Format.GeoJSON(
                {
                    'externalProjection': new LMap.Projection("EPSG:4326"),
                    'internalProjection': map.getProjectionObject()
                }
        );

        /**
         * 重回数据
         */
        function loadPolygon(){
            layer1.addFeatures(geojsonFormat.read(geoJson));
        }




        //这里是进行初始化区域相关内容
        var patrolRegion = '';
        <c:if test="${not empty patrolRegion}">
        patrolRegion = '${patrolRegion}';
        </c:if>
        var polygon = "";
        $(function () {
            var patrol = showData(patrolRegion);
            //下面进行数据展示
            showRegion(patrol);

        });


        /**
         * 将json字符串转化成对象
         * @param data
         */
        var showData = function (data) {
            if(data != undefined && data != "") {
                var showData = JSON.parse(data);
                return showData;
            }
        };


        /**
         * 进行区域坐标设置并且进行绘制
         * @param patrol
         */
        var showRegion = function (patrol) {
            if(patrol != undefined && patrol != "") {
                if(patrol.coordinates != undefined && patrol.coordinates != '') {
                    //这里进行数据格式化
                    geomentrie.type = 'Polygon';
                    //设置相关坐标
                    geomentrie.coordinates.push(patrol.coordinates);
                    //设置绘制颜色
                    features.geometry.geometries.push(geomentrie);
                    geoJson.features.push(features);
                    //设置完成进行加载数据
                    loadPolygon();
                }
            }
        };

        //进行数据提交
        var deleteRegion = function () {
            var id = $("#regionId").val();
            polygon = "";
        };


        /**
         * 删除数据
         */
        $("#delete").click(function () {
            var id = $("#regionId").val();
            polygon = "";
            //删除已经绘制的内容
            drawLayer.removeAllFeatures();
            //删除区域的同时删除已经存在的数据
            window.location.href = '<%=basePath%>deleteRegionLocation?id=' + id;
            <%--$.ajax({--%>
                <%--url: '<%=basePath%>deleteRegionLocation?id=' + id,--%>
                <%--type: 'get',--%>
<%--//					contentType:'application/json',--%>
<%--//					dataType:'json',--%>
                <%--success:function(data) {},--%>
                <%--error:function() {}--%>
            <%--});--%>
        });

        /**
         * 提交区域数据
         */
        $("#submit").click(function () {
            var id = $("#regionId").val();
            var value = polygon;
            window.location.href = '<%=basePath%>addDrawRegion?id=' + id + "&polygon=" + value;
            <%--$.ajax({--%>
                <%--url: '<%=basePath%>addDrawRegion?id=' + id + "&polygon=" + value,--%>
                <%--type: 'get',--%>
<%--//					contentType:'application/json',--%>
<%--//					dataType:'json',--%>
                <%--success:function(data) {},--%>
                <%--error:function() {}--%>
            <%--});--%>
        });

        /***-------------------一、Geoserver图层点击回调 START -------------------***/
        map.getFeatureInfo(function(data){
            if(data != null){
                LqUtil.log("当前大楼ID:"+data.id);
            }

        });

        /**
         * 定义签到点位点标注样式
         */
        var signPointMarkerStyle = new LMap.StyleMap({
            "default":new LMap.Style({
                externalGraphic:'\${src}',
                cursor:'pointer',
                graphicWidth:61,
                graphicHeight:65,
                graphicYOffset:-65,
                title:'\${name}'
            }),
            "select":new LMap.Style({}),
            "temporary":new LMap.Style({
                fillOpacity:'0.9',
                graphicWidth:66,
                graphicHeight:70,
                graphicYOffset:-65
            })

        });

        /**
         * 定义签到点位点标注图层
         */
        var signPointMarkerLayer = new LMap.Layer.Vector('signPintMarkerLayer',{
            styleMap:signPointMarkerStyle,
            visibility:true
        });
        map.addLayers([signPointMarkerLayer]);

        /**
         * 获取签到点位数据，创建点，添加到图层
         * @type {string}
         */
        var signPointList = '${pointInfoList}';
        if(signPointList != ''){
            $.each(JSON.parse(signPointList), function(p, p_item){
                var signPointMarkerFeature = new LMap.Feature.Vector(
                    new LMap.Geometry.Point( p_item.lng, p_item.lat)
                    .transform(new LMap.Projection("EPSG:4326"), map.getProjectionObject()),
                    {
                        gid:p_item.pointId,
                        src: '<%=basePath%>page/images/red.png',
                        name: p_item.pointName
                    }
                );
                signPointMarkerLayer.addFeatures(signPointMarkerFeature);
            })
        }

        //
        var point_name = '';
        var point_id = null;
        /**
         * 定义点击
         * @param e
         */
        function signPointMarkerFeatureSelected(e){
            LqUtil.log("点击点图元：" + e.feature.attributes.gid);
            point_name = e.feature.attributes.name;
            if(point_name != '新增签到点位'){
                point_id = e.feature.attributes.gid;
                removeClickMarkerLayer();
                var lonlat = new LMap.LonLat(e.feature.geometry.x, e.feature.geometry.y);
                var markerPopup = createPopup('point-' + e.feature.attributes.gid, lonlat, -57,-70);
                markerPopup.setContentHTML("<div class=\"headImg\" style='background: #fff;width: 114px;height: 42px;padding: 8px;border: 1px solid #e5e5e5;'>" +
                    "<button style=\"background: #169BD5;width: 45px;height: 26px;cursor: pointer;float: left;color: #fff;border: 1px solid #e5e5e5;border-radius: 3px;\" onclick='mapIconSure()'>配置</button>" +
                    "<button style=\"background: #f0471d;width: 45px;height: 26px;cursor: pointer;float: right;color: #fff;border: 1px solid #e5e5e5;border-radius: 3px;\" onclick='deletePoint()'>删除</button></div>");
                map.addPopup(markerPopup);
            } else{
                point_name = '';
            }
        }

        /**
         * 定义点图元取消点击事件
         * @param e
         */
        function signPointMarkerFeatureUnSelected(e){
            LqUtil.log("取消点击点图元：" + e.feature.attributes.gid);
        }

        /**
         * 注册点图元点击事件
         */
        function signPointMarkerLayerRegister(){
            signPointMarkerLayer.events.un({
                featureselected:signPointMarkerFeatureSelected,
                featureunselected:signPointMarkerFeatureUnSelected
            });
            signPointMarkerLayer.events.on({
                featureselected:signPointMarkerFeatureSelected,
                featureunselected:signPointMarkerFeatureUnSelected
            });
        }


        var highlightControl = null;
        var selectControl = null;

        if(highlightControl != null){
            highlightControl.unselectAll();
            highlightControl.deactivate();
            selectControl.unselectAll();
            selectControl.deactivate();
            map.removeControl(highlightControl);
            map.removeControl(selectControl);
            highlightControl = null;
            selectControl = null;
        }
        var report = function(e){
            LMap.Console.log(e.type, e.feature.id);
        };
        /**
         * 定义高亮
         */
        highlightControl = new LMap.Control.SelectFeature(signPointMarkerLayer,{
            hover:true,
            highlightOnly:true,
            renderIntent:"temporary",
            eventListeners:{
                beforefeaturehighlighted:report,
                featurehightlighted:report,
                featureunhightlighted:report
            }
        });

        /**
         * 定义选中
         */
        selectControl = new LMap.Control.SelectFeature(signPointMarkerLayer,{clickout:true});
        function onPopupClose(evt) {
            selectControl.unselectAll();
        }

        map.addControl(highlightControl);
        map.addControl(selectControl);
        highlightControl.activate();
        selectControl.activate();
        signPointMarkerLayerRegister();

        /**
         * 地图点击事件
        **/
        var pointLng = 0;
        var pointLat = 0;
        var isAdded = false;
        function mapClick(e){
            removeClickMarkerLayer();
            var lonlat = map.getLonLatFromPixel(e.xy);

            var gpsPoint = new LMap.Geometry.Point(lonlat.lon,lonlat.lat)
                .transform(map.getProjectionObject(), new LMap.Projection("EPSG:4326"));
            pointLng = gpsPoint.x;
            pointLat = gpsPoint.y;
            var markerFeaure = new LMap.Feature.Vector(
                new LMap.Geometry.Point(lonlat.lon,lonlat.lat),{
                    gid:'new-1',
                    src:'<%=basePath%>page/images/orange.png',
                    name:'新增签到点位'
                }
            );
            signPointMarkerLayer.addFeatures(markerFeaure);

            var markerPopup = createPopup('new-1', lonlat, 33,-51);
            markerPopup.setContentHTML("<div class=\"headImg\"><button style=\"background: #fff;width: 45px;height: 32px;cursor: pointer;border: 1px solid #e5e5e5;\n" +
                "border-radius: 3px;\" onclick='mapIconSure()'>确定</button></div>");
            map.addPopup(markerPopup);

            isAdded = true;
            point_id = null;
            point_name = '';
        };

        function removeClickMarkerLayer(){
            if(isAdded){
                signPointMarkerLayer.removeFeatures(signPointMarkerLayer.features[signPointMarkerLayer.features.length - 1]);
                isAdded = false;
            }
        }

        map.registerEvent('click',mapClick);


        function clearPopup(){
            map.removePopups();
        }

        function createPopup(gid,lonlat,x,y){
            clearPopup();
            return new LMap.Popup.BlankFramedCloud(
                'g_'+gid,
                lonlat,
                null,
                null,
                null,
                false,
                null,
                'tr',
                null,
                x,
                y
            );
        }
        var load_beacon_status = false;
        function mapIconSure() {
            clearInput();
            $("#point-name").val(point_name);
            loadBeacon()
            if(load_beacon_status){
                $(".patrol-bomb-box").fadeIn(300);
                $("#mask").show();
            }
        }

        $(".patrol-bomb-close,.patrol-bomb-cancel-btn").click(function() {
            $(".patrol-bomb-box").fadeOut(300);
            $("#mask").hide();
            $(".patrol-bomb-box input[type='text']").val('');
            $(".patrol-bomb-box input[type='radio']").removeProp("checked");
        })

        function clearInput(){
            $("#search-uuid").val('')
            $("#search-major").val('')
            $("#search-minor").val('')
            $("#point-name").val('')
        }

        function loadBeacon(){
            var uuid = $("#search-uuid").val();
            var major = $("#search-major").val();
            var minor = $("#search-minor").val();
            $.ajax({
                type:"get",
                url:"<%=basePath%>patrolBeaconInfo_loadUnbindBeacons",
                data:{
                    pointId : point_id,
                    uuid : uuid,
                    major : major,
                    minor :minor
                },
                async:false,
                dataType:"json",
                success:function(Data){
                    if (Data.code==200) {
                        $(".patrol-mesg-list-table tr").first().siblings().remove();
                        $.each(Data.data, function(k, k_item){
                            $("#point-name").val(point_name);
                            if(point_id != null && k == 0){
                                var _html = '<tr>'
                                        + '<td>' + k_item.uuid + '</td>'
                                        + '<td>' + k_item.major + '</td>'
                                        + '<td>' + k_item.minor + '</td>'
                                        + '<td><input type="radio" name="beaconId" value="' + k_item.beaconId + '" checked></td>'
                                        + '</tr>';
                                $(".patrol-mesg-list-table tbody").append(_html);
                            } else{
                                var _html = '<tr>'
                                    + '<td>' + k_item.uuid + '</td>'
                                    + '<td>' + k_item.major + '</td>'
                                    + '<td>' + k_item.minor + '</td>'
                                    + '<td><input type="radio" name="beaconId"  value="' + k_item.beaconId + '"></td>'
                                    + '</tr>';
                                $(".patrol-mesg-list-table tbody").append(_html);
                            }
                        })
                        load_beacon_status = true;
                    } else{
                        layer.msg(Data.message, {icon: 2});
                        load_beacon_status = false;
                    }
                }
            });
        }

        var regionInfo = '${patrolRegion}';
        var regionId = JSON.parse(regionInfo).id;
        function saveSignPoint(){
            point_name = $("#point-name").val();
            var beacon_id = $("input[name='beaconId']:checked").val();
            if(point_name == ''){
                layer.msg('点位名称不能为空', {icon: 2});
            } else if(beacon_id == ''){
                layer.msg('请配置绑定标签', {icon: 2});
            } else{
                $.ajax({
                    type:"post",
                    url:"<%=basePath%>patrolSignPortInfo_save",
                    data:{
                        pointId : point_id,
                        pointName : point_name,
                        lng : pointLng,
                        lat : pointLat,
                        regionId : regionId,
                        beaconId : beacon_id
                    },
                    async:false,
                    dataType:"json",
                    success:function(Data){
                        if (Data.code==200) {
                            console.log(Data);
                            if(isAdded){
                                removeClickMarkerLayer();
                            } else{
                                deletePointWithGid(point_id);
                            }
                            map.removePopups();
                            var signPointMarkerFeature = new LMap.Feature.Vector(
                                new LMap.Geometry.Point( Data.data.lng, Data.data.lat)
                                    .transform(new LMap.Projection("EPSG:4326"), map.getProjectionObject()),
                                {
                                    gid:Data.data.pointId,
                                    src: '<%=basePath%>page/images/red.png',
                                    name: Data.data.pointName
                                }
                            );
                            signPointMarkerLayer.addFeatures(signPointMarkerFeature);

                            $(".patrol-bomb-box").fadeOut(300);
                            $("#mask").hide();
                            $(".patrol-bomb-box input[type='text']").val('');
                            $(".patrol-bomb-box input[type='radio']").removeProp("checked");
                            layer.msg(Data.message, {icon: 1});
                        } else{
                            layer.msg(Data.message, {icon: 2});
                        }
                    }
                });
            }
        }

        function deletePoint(){
            $.ajax({
                type:"get",
                url:"<%=basePath%>patrolSignPortInfo_delete",
                data:{
                    pointId : point_id
                },
                async:false,
                dataType:"json",
                success:function(Data){
                    if (Data.code==200) {
                        deletePointWithGid(point_id);
                        map.removePopups();
                        layer.msg('删除成功', {icon: 1});
                    } else{
                        layer.msg(Data.message, {icon: 2});
                    }
                }
            });
        }

        function deletePointWithGid(gid){
            $.each(signPointMarkerLayer.features, function(sp, sp_item){
                if(sp_item.attributes.gid == gid){
                    signPointMarkerLayer.removeFeatures(sp_item);
                    return false;
                }
            })
        }
    </script>

</body>
</html>