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
</head>
<body>
    <input type="hidden" name="id" value="${id}" id="regionId">
    <input type="hidden" name="color" value="${color}" id="colorDraw">
    <div class="box f14 xungeng-map-box">
        <div class="xungeng-map-top">
            <!--绘制前操作-->
            <div class="xungeng-map-operation-box">
                <%--<button class="xungeng-start-drawing float-L" onclick="startDrawPolygon()">开始绘制</button>--%>
                <%--<div class="xungeng-start-drawing-prompt float-L">--%>
                    <%--点击绘制巡更区域--%>
                <%--</div>--%>
                <%--<button class="drawing-demo-btn float-R">绘制演示</button>--%>
            </div>

            <!--绘制过程中提示结束绘制-->
            <div class="xungeng-map-drawing-prompt-box" style="display: none;">
                双击地图结束绘制巡更区域
                <button class="end-drawing-btn float-R" onclick="stopDrawPolygon()">结束绘制</button>
            </div>

            <!--绘制成功后操作-->
            <div class="xungeng-map-drawing-end-box" style="display: none;">
                <button class="xungeng-drawing-result-del float-L" id="delete">删除</button>
                <button class="xungeng-drawing-submit float-L" id="submit">提交</button>
            </div>
        </div>
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

        <div id="mask"></div>
    </div>


    <script>

        <%--地图页面相关内容--%>
        //视频播放
        $(".drawing-demo-btn").click(function(){
            $(".demo-video-box").fadeIn(300,function(){
                $(".demo-video-close-video").addClass("close-show-video");
            });
            $("#mask").show();
            $("body").addClass("ovhide");
        });

        $(".demo-video-close-video").click(function(){
            $(".demo-video-box").fadeOut(300);
            $(".demo-video-close-video").removeClass("close-show-video");
            $("#mask").hide();
            $("body").removeClass("ovhide");

            var myVideo = document.getElementsByTagName('video')[0];
            if (!myVideo.paused){
                myVideo.pause();
            }
        });

        //开始绘制
        $(".xungeng-start-drawing").click(function(){
            $(".xungeng-map-operation-box").hide();
            $(".xungeng-map-drawing-prompt-box").fadeIn(300);
        });
        <%--地图页面相关内容--%>


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


        /**
         * 测距结果回调
         */
        map.addMeasureDistanceControl(function(e) {
            LqUtil.log(e);
        });


        /**
         * 侧面结果回调
         */
        map.addMeasureAreaControl(function (e) {
            LqUtil.log(e);
        });

        var styles = new LMap.StyleMap({
            "default": new LMap.Style(null, {
                rules: [
                    new LMap.Rule({
                        symbolizer: {
                            "Point": {
                                pointRadius: 6,
                                graphicName: "#02a6cf",
                                fillColor: "#02a6cf",
                                fillOpacity: 0.1,
                                strokeWidth: 2,
                                strokeOpacity: 1,
                                strokeColor: "#02a6cf"
                            },
                            "Line": {
                                strokeWidth: 4,
                                strokeOpacity: 0.1,
                                strokeColor: "#00ccff"
                            },
                            "Polygon":{
                                strokeWidth: 2,
                                strokeOpacity: 0.1,
                                fillColor: "#02a6cf",
                                strokeColor: "#02a6cf"
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
                                strokeColor: "#02a6cf"
                            },
                            "Polygon":{
                                strokeWidth: 2,
                                strokeOpacity: 1,
                                strokeColor: "#02a6cf"
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
                                strokeColor: "#02a6cf"
                            },
                            "Polygon":{
                                strokeWidth: 2,
                                strokeOpacity: 1,
                                fillColor: "#02a6cf" ,
                                strokeColor: "#02a6cf"
                            }
                        }
                    })
                ]
            })
        });


        var layer = new LMap.Layer.Vector("showDrawRegion",{
            styleMap:new LMap.StyleMap({
                "default":new LMap.Style({
                    cursor:'pointer',
//                    fillColor: "\${color}" ,
                    fillColor:"#ee9900",
                    text:"没用",
                    fillOpacity: 0.1,
//                    strokeColor: "\${color}" ,
                    strokeColor: "#ee9900" ,
                    strokeWidth: 1
                }),
                "select":new LMap.Style({
                    fillColor: "\${color}"
                }),
                "temporary":new LMap.Style({
//                    fillColor: "\${color}",
                    fillOpacity: 0.1,
                    text:"没用",
//                    strokeColor: "\${color}" ,
                    strokeWidth: 1
                })
            })
        });
        map.addLayers([layer]);







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
            snapping: new LMap.Control.Snapping({layer: drawLayer}),
            modify: new LMap.Control.ModifyFeature(drawLayer)
        };
        //square
        //circle
        //ellipse
        map.addControl(drawControls['box']);
        drawControls['box'].featureAdded = function(e) {
            LqUtil.log("box：" + e.geometry.toString());
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

        drawControls['polygon'].featureAdded = function(e) {
            polygon = e.geometry.transform(map.getProjectionObject(),new LMap.Projection("EPSG:4326")).toString();
            stopDrawLine();
            startEdit();
            drawControls['modify'].selectFeature(e);
        };*/




        //下面进行数据回显操作
        var geoJson =  {
            "type": "FeatureCollection",
            "features": []
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
            layer.addFeatures(geojsonFormat.read(geoJson));
        }






        //这里是进行初始化区域相关内容
        var patrolRegions = '';
        <c:if test="${not empty patrolRegions}">
        patrolRegions = '${patrolRegions}';
        </c:if>
        var polygon = "";
        $(function () {
            var patrols = showData(patrolRegions);
            //下面进行数据展示
            showRegion(patrols);

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
        var showRegion = function (patrols) {
            if(patrols != undefined && patrols != "") {
                //这里进行输出
                console.log(patrols);
                for(var i = 0; i < patrols.length; i++) {
                    var tempPatrol = patrols[i];
                    if(tempPatrol.coordinates != undefined && tempPatrol.coordinates != '') {
                        //这里进行数据格式化
                        console.log(tempPatrol.color);
//                        features.properties.color = tempPatrol.color;
//                        features.properties.name = tempPatrol.regionName;
//                        features.properties.gid = tempPatrol.id;
                        geomentrie.type = 'Polygon';
                        //设置相关坐标
                        geomentrie.coordinates.push(tempPatrol.coordinates);
                        var features = {
                            "geometry":{
                                "type": "GeometryCollection",
                                "geometries": []
                            },
                            "type": "Feature",
                            "properties": {
                                "gid":tempPatrol.id,
                                "name":tempPatrol.regionName,
                                "color":tempPatrol.color
                            }
                        };


                        //设置绘制颜色
                        features.geometry.geometries.push(geomentrie);
                        geoJson.features.push(features);
                        //设置完成进行加载数据

                    }
                }
                console.log(geoJson);
                loadPolygon();

            }
        }
    </script>

</body>
</html>