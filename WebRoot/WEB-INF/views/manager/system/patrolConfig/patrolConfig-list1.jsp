<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>配置信息管理</title>
    <link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
    <%--<script type="text/javascript" src="<%=path %>/page/js/common.js"></script>--%>
    <script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>
    <%--<link rel="stylesheet" href="<%=path %>/page/css/common-PC.css" />--%>
    <link rel="stylesheet" href="<%=path %>/page/css/Tstyle.css" />
    <%--<script src="<%=path %>/page/js/jquery.min.js" type="text/javascript"></script>--%>
    <script src="https://cdn.bootcss.com/html5media/1.1.8/html5media.js"></script>
    <script type="text/javascript">
        var method = '${method }';
        if(method=='addSuccess'){
            layer.msg('添加成功', {icon: 1});
        }else if(method=='editSuccess'){
            layer.msg('编辑成功', {icon: 1});
        }else if(method=='deleteSuccess'){
            layer.msg('删除成功', {icon: 1});
        }else{
        }
    </script>
</head>
<body>

<div class="place">
    <span>位置：</span>
    <ul class="placeul">
        <li><a href="#">安防巡更</a></li>
        <li><a href="<%=path %>/patrolConfig_list">配置信息管理</a></li>
    </ul>
</div>

<%--<div class="rightinfo">--%>
<%--<form action="<%=path %>/patrolConfig_edit" method="post" id="addForm">--%>
<%--<table class="tablelist">--%>
<%--<input type="hidden" name="id" value="${patrolConfig.id }"/>--%>
<%--<ul>--%>
<%--<li>参数配置</li>--%>
<%--</ul>--%>
<%--<ul class="seachform">--%>
<%--<li>--%>
<%--位置数据动态上传周期：每<input name="uploadTime" type="text" value="${patrolConfig.uploadTime }"/>*(秒) 设置后,系统每${patrolConfig.uploadTime }秒获取一次巡更人员的位置信息--%>
<%--</li>--%>
<%--</ul>--%>
<%--<ul class="seachform">--%>
<%--<li>--%>
<%--管理端更新人员位置周期：每<input name="refreshTime" type="text" value="${patrolConfig.refreshTime }"/>*(秒) 设置后,系统每${patrolConfig.refreshTime }秒更新一次巡更人员的位置信息--%>
<%--</li>--%>
<%--</ul>--%>
<%--<ul class="seachform">--%>
<%--<li>--%>
<%--允许到达巡更区域时长:<input name="startPatrolTime" type="text" value="${patrolConfig.startPatrolTime }"/>*(分钟) 设置后，巡更人员在点击开始巡更后，允许在${patrolConfig.startPatrolTime }分钟后到达巡更区域--%>
<%--</li>--%>
<%--</ul>--%>
<%--<ul class="seachform">--%>
<%--<li>--%>
<%--允许巡更人员位置不变时长:<input name="lazyTime" type="text" value="${patrolConfig.lazyTime }"/>*(分钟) 设置后，巡更人员在${patrolConfig.lazyTime }分钟内位置没有变动，系统将给管理员发送预警提醒--%>
<%--</li>--%>
<%--</ul>--%>
<%--<ul class="seachform">--%>
<%--<li>--%>
<%--允许巡更人员离开巡更范围时长:<input name="leaveRegionTime" type="text" value="${patrolConfig.leaveRegionTime }"/>*(分钟) 设置后，巡更人员在离开巡更范围${patrolConfig.leaveRegionTime }分钟后，系统将给管理员发送预警提醒--%>
<%--</li>--%>
<%--</ul>--%>
<%--<ul class="seachform">--%>
<%--<li>--%>
<%--允许巡更人员离开巡更范围:<input name="leaveRegionDistance" type="text" value="${patrolConfig.leaveRegionDistance }"/>*(米) 设置后,系统将允许巡更人员离开巡逻范围${patrolConfig.leaveRegionDistance }米--%>
<%--</li>--%>
<%--</ul>--%>
<%--<ul>--%>
<%--<li><label>&nbsp;</label><input name="" type="submit" class="btn" value="确认修改"/><input type="reset"  value="重置"/> </li>--%>
<%--</ul>--%>
<%--</table>--%>
<%--</form>--%>
<%--</div>--%>

<%--<script type="text/javascript">--%>
<%--$('.tablelist tbody tr:odd').addClass('odd');--%>
<%--</script>--%>


<%--这里进行组装--%>
<div class="box">
    <form action="<%=path %>/patrolConfig_edit1" method="post" id="addForm">
        <input type="hidden" name="id" value="${patrolConfig.id }"/>
        <div class="setting">
            <div class="top-bar">
                <div class="left">
                    <span class="top-setting">参数配置</span>
                </div>
            </div>
            <div class="container">
                <ul class="patrol-parameter-list">
                    <li>
                        <div class="title">
                            <div class="title-content">
                                <span class="content">管理端默认查看校区</span>
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="title">
                            <div class="title-content">
                                <span class="content">巡更异常推送频率</span>
                            </div>
                        </div>
                        <div class="control">
                            <div class="control-content-one">
                                <span class="content">每</span>
                            </div>
                            <input type="text" name="exceptionPushTime" value="${patrolConfig.exceptionPushTime }" class="text1" onkeyup="value=value.replace(/[^\d]/g,'')">
                            <div class="control-content-two">
                                <span class="symbol">*</span>
                                <span class="content"> 分钟/次</span>
                            </div>
                            <div class="control-content-three">
								<span class="mark">
									（设置后，巡更发现异常，将在<span class="input-result">${patrolConfig.exceptionPushTime }</span>分钟后进行二次推送提醒[首次发现异常，会立即推送告知]）
								</span>
                            </div>
                        </div>
                    </li>

                    <li>
                        <div class="title">
                            <div class="title-content">
                                <span class="content">允许到达巡更区域时长</span>
                            </div>
                        </div>
                        <div class="control">
                            <div class="control-content-one">
                                <span class="content">每</span>
                            </div>
                            <input type="text" name="startPatrolTime" class="text1" value="${patrolConfig.startPatrolTime }" onkeyup="value=value.replace(/[^\d]/g,'')">
                            <div class="control-content-two">
                                <span class="symbol">*</span>
                                <span class="content"> 分钟</span>
                            </div>
                            <div class="control-content-three">
								<span class="mark">
									（设置后，巡更人员在点击开始巡更后，允许在<span class="input-result">${patrolConfig.startPatrolTime }</span>分钟后到达巡更区域）
								</span>
                            </div>
                        </div>
                    </li>




                    <li>==============================================</li>





                    <li>
                        <div class="title-first">
                            <div class="title-content">
                                <span class="content">位置数据动态上传周期</span>
                            </div>
                        </div>
                        <div class="control-first control">
                            <div class="control-content-one">
                                <span class="content">每</span>
                            </div>
                            <input type="text" name="uploadTime" class="text1" value="${patrolConfig.uploadTime }" onkeyup="value=value.replace(/[^\d]/g,'')">
                            <div class="control-content-two">
                                <span class="symbol">*</span>
                                <span class="content"> 秒</span>
                            </div>
                            <div class="control-content-three">
								<span class="mark">
									（设置后,系统每秒<span class="input-result">${patrolConfig.uploadTime }</span>获取一次巡更人员的位置信息）
								</span>
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="title">
                            <div class="title-content">
                                <span class="content">管理端更新人员位置周期</span>
                            </div>
                        </div>
                        <div class="control">
                            <div class="control-content-one">
                                <span class="content">每</span>
                            </div>
                            <input type="text" name="refreshTime" class="text1" value="${patrolConfig.refreshTime }" onkeyup="value=value.replace(/[^\d]/g,'')">
                            <div class="control-content-two">
                                <span class="symbol">*</span>
                                <span class="content"> 秒</span>
                            </div>
                            <div class="control-content-three">
								<span class="mark">
									（设置后,系统每<span class="input-result">${patrolConfig.refreshTime }</span>秒更新一次巡更人员的位置信息）
								</span>
                            </div>
                        </div>
                    </li>

                    <li>
                        <div class="title">
                            <div class="title-content">
                                <span class="content">允许巡更人员位置不变时长</span>
                            </div>
                        </div>
                        <div class="control">
                            <div class="control-content-one">
                                <span class="content">每</span>
                            </div>
                            <input type="text" name="lazyTime" class="text1" value="${patrolConfig.lazyTime }" onkeyup="value=value.replace(/[^\d]/g,'')">
                            <div class="control-content-two">
                                <span class="symbol">*</span>
                                <span class="content"> 分钟</span>
                            </div>
                            <div class="control-content-three">
								<span class="mark">
									（设置后，巡更人员在<span class="input-result">${patrolConfig.lazyTime }</span>分钟内位置没有变动，系统将给管理员发送预警提醒）
								</span>
                            </div>
                        </div>
                    </li>

                    <li>
                        <div class="title">
                            <div class="title-content">
                                <span class="content">允许巡更人员离开巡更区域距离</span>
                            </div>
                        </div>
                        <div class="control">
                            <div class="control-content-one">
                                <span class="content">每</span>
                            </div>
                            <input type="text" name="leaveRegionDistance" class="text1" value="${patrolConfig.leaveRegionDistance }" onkeyup="value=value.replace(/[^\d]/g,'')">
                            <div class="control-content-two">
                                <span class="symbol">*</span>
                                <span class="content"> 米</span>
                            </div>
                            <div class="control-content-three">
								<span class="mark">
									（设置后,系统将允许巡更人员离开巡逻范围<span class="input-result">${patrolConfig.leaveRegionDistance }</span>米，系统讲给管理员发送预警提醒）
								</span>
                            </div>
                        </div>
                    </li>

                    <li>
                        <div class="title">
                            <div class="title-content">
                                <span class="content">允许巡更人员离开巡更范围时长</span>
                            </div>
                        </div>
                        <div class="control">
                            <div class="control-content-one">
                                <span class="content">每</span>
                            </div>
                            <input type="text" name="leaveRegionTime" class="text1" value="${patrolConfig.leaveRegionTime }" onkeyup="value=value.replace(/[^\d]/g,'')">
                            <div class="control-content-two">
                                <span class="symbol">*</span>
                                <span class="content"> 分钟</span>
                            </div>
                            <div class="control-content-three">
								<span class="mark">
									（设置后，巡更人员在离开巡更范围<span class="input-result">${patrolConfig.leaveRegionTime }</span>分钟后，系统将给管理员发送预警提醒）
								</span>
                            </div>
                        </div>
                    </li>
                    <%--<li>
                        <div class="title">
                            <div class="title-content">
                                <span class="content">对接定位设备</span>
                            </div>
                        </div>
                        <div class="control">
                        <div class="title-content">
                            <span class="content">设备厂商</span>
                            <input type="text" name="manufactuName" class="text1" value="${patrolConfig.patrolEquipmentManufacturer.manufactuName }" onkeyup="value=value.replace(/[^\d]/g,'')">
                        </div>
                        <div class="title-content">
                            <span class="content">对接地址</span>
                            <input type="text" name="dockingAddress" class="text1" value="${patrolConfig.patrolEquipmentManufacturer.dockingAddress }" onkeyup="value=value.replace(/[^\d]/g,'')">
                        </div>
                        </div>--%>


                    </li>


                    <%--<li>
                        <div class="title">
                            <div class="title-content">
                                <span class="content">人员位置丢失预警时长</span>
                            </div>
                        </div>
                        <div class="control">
                            <div class="control-content-one">
                                <span class="content">每</span>
                            </div>
                            <input type="text" name="personnelLossTime" value="${patrolConfig.personnelLossTime }" class="text1" onkeyup="value=value.replace(/[^\d]/g,'')">
                            <div class="control-content-two">
                                <span class="symbol">*</span>
                                <span class="content"> 分钟</span>
                            </div>
                            <div class="control-content-three">
                            <span class="mark">
                                （设置后，超过<span class="input-result">${patrolConfig.personnelLossTime }</span>分钟获取不到巡更人员位置，系统讲给管理员发送预警提醒）
                            </span>
                            </div>
                        </div>
                    </li>

                    <li>
                        <div class="title">
                            <div class="title-content">
                                <span class="content">蓝牙签到有效距离</span>
                            </div>
                        </div>
                        <div class="control">
                            <div class="control-content-one">
                                <span class="content"></span>
                            </div>
                            <input type="text" name="beaconSignDistance" value="${patrolConfig.beaconSignDistance }" class="text1" onkeyup="value=value.replace(/[^\d]/g,'')">
                            <div class="control-content-two">
                                <span class="symbol">*</span>
                                <span class="content"> (米)</span>
                            </div>
                            <div class="control-content-three">
                            <span class="mark">
                                （设置后，距离蓝牙标签<span class="input-result">${patrolConfig.beaconSignDistance }</span>米可进行签到）
                            </span>
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="title">
                            <div class="title-content">
                                <span class="content">GPS定位辅助签到有效距离</span>
                            </div>
                        </div>
                        <div class="control">
                            <div class="control-content-one">
                                <span class="content"></span>
                            </div>
                            <input type="text" name="gpsSignDistance" value="${patrolConfig.gpsSignDistance }" class="text1" onkeyup="value=value.replace(/[^\d]/g,'')">
                            <div class="control-content-two">
                                <span class="symbol">*</span>
                                <span class="content"> (米)</span>
                            </div>
                            <div class="control-content-three">
                            <span class="mark">
                                （设置后，距离签到点位<span class="input-result">${patrolConfig.gpsSignDistance }</span>米可进行签到）
                            </span>
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="title">
                            <div class="title-content">
                                <span class="content">巡更签到循环要求</span>
                            </div>
                        </div>
                        <div class="control">
                            <div class="control-content-one">
                                <span class="content">每巡更</span>
                            </div>
                            <input type="text" name="signRange" value="${patrolConfig.signRange }" class="text1" onkeyup="value=value.replace(/[^\d]/g,'')">
                            <div class="control-content-two">
                                <span class="symbol"></span>
                                <span class="content"></span>
                            </div>
                            <div class="control-content-three">
                            <span class="mark">
                                分钟，需要签到1次
                            </span>
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="title">
                            <div class="title-content">
                                <span class="content">巡更超过循环周期</span>
                            </div>
                        </div>
                        <div class="control">
                            <div class="control-content-one">
                                <span class="content"></span>
                            </div>
                            <div class="input-radio-box">
                                <input type="radio" name="overtimeDeal" value="1" class="text1" <s:if test="${patrolConfig.overtimeDeal == 1}">checked</s:if>>
                                记为应签
                            </div>
                            <div class="input-radio-box">
                                <input type="radio" name="overtimeDeal" value="2" class="text1" <s:if test="${patrolConfig.overtimeDeal == 2}">checked</s:if>>
                                多余舍弃
                            </div>
                            <div class="control-content-two">
                                <span class="symbol"></span>
                                <span class="content"></span>
                            </div>
                            <div class="control-content-three">
                            <span class="mark">
                                (举例：要求巡更人员每30分钟巡更1次，巡更人员在第31分钟结束巡更，则应签2次，实签1次)
                            </span>
                            </div>
                        </div>
                    </li>--%>
                </ul>
            </div>
        </div>
        <input type="submit" class="submit" value="提交">
        <input type="reset" class="reset" value="重置">
    </form>
</div>
<script>
    $(".text1").keyup(function(){
        var t=$(this).val();
        if (t!="") {
            $(this).parent().find(".input-result").text(t);
        } else{
            $(this).parent().find(".input-result").text("x");
        }
    })
</script>

</body>

</html>