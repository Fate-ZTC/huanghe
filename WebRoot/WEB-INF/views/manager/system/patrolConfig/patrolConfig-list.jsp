<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@include file="../../taglibs.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>巡更信息配置</title>
    <script src="<%=basePath%>page/js/jquery.js"></script>
    <script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>
    <style>
        html,body,ul,li{margin: 0;padding: 0;}
        html,body{width: 100%;height: 100%;background: #f7f7f7;}
        ul,li,a,em,i{list-style: none;font-style: normal;}
        input{outline: none;}
        button{outline: none;border: none;}
        .configuration-box{width: 100%;font-size: 14px;}
        .configuration-item{margin-bottom:40px;width: calc(100%-60px);background: #fff;padding: 30px;box-shadow:0 1px 4px rgba(0, 0, 0, .08)}
        .profile-item-box{border: 1px solid #e1e1e1;}
        .profile-item{display: flex;align-items: center;height: 50px;overflow: hidden;}
        .profile-item:not(:last-child){border-bottom: 1px solid #e1e1e1;}
        .profile-field{width: 240px;padding-left: 40px;height:50px; line-height: 50px; border-right: 1px solid #e1e1e1;}
        .profile-value{ display: flex; flex: 1;overflow: hidden; align-items: center;}
        .profile-value:not(.manufacturer-item){padding-left: 40px;}
        .reqired::after{content: "*";display: inline-block;color: red;}
        .input-box{display: inline-block;}
        .input-box>input{border:2px solid #DBDBDB;}
        .input-box.required::after{content: "*";color: red;display: inline-block;vertical-align: middle;margin-right: 6px;}
        .placehodler{color: #B3B3B3;margin-left: 10px;white-space: nowrap;}
        .tab-box{display: flex;align-items: center;}
        .tab-item{display: flex; align-items: center; padding: 0 20px;line-height: 40px;font-size: 16px;cursor: pointer;}
        .tab-item .curr{color: #1DB3F0;border-bottom: 1px solid #1DB3F0;line-height: 1.1em;}
        input[type=checkbox] {
            visibility: hidden;
        }
        .checkbox {
            display: inline-block;
            width: 16px;
            position: relative;
            margin-right: 8px;
            vertical-align: top;
        }
        .checkbox label {
            cursor: pointer;
            position: absolute;
            width: 16px;
            height: 16px;
            top: 2px;
            left: 0;
            background: #fff;
            border:1px solid #B5B5B5;
            box-sizing: border-box;
        }
        .checkbox label:after {
            opacity: 0.2;
            content: '';
            position: absolute;
            width: 7px;
            height: 3px;
            background: transparent;
            top: 3px;
            left: 2px;
            border: 3px solid #666;
            border-top: none;
            border-right: none;
            -webkit-transform: rotate(-45deg);
            -moz-transform: rotate(-45deg);
            -o-transform: rotate(-45deg);
            -ms-transform: rotate(-45deg);
            transform: rotate(-45deg);
        }
        .checkbox label:hover::after {
            opacity: 0.5;
        }
        .checkbox input[type=checkbox]:checked + label:after {
            opacity: 1;
        }
        .tab-box{margin-bottom: 10px;}
        .profile-field .checkbox label {top: 17px;}
        .inner-input{position: absolute;margin-left: 10px;}
        .form-item{margin-right: 20px;}
        .btn-commit{padding:6px 24px;color: #fff;background: #2a9bc9;cursor: pointer;}
        .btn-reset{padding:6px 24px;color: #fff;background: #2a9bc9;margin-left: 20px;cursor: pointer;}
        .btn-box{margin-top: 40px;}
        .manufacturer-item{display: flex;padding-left: 40px;}
        .manufacturer-item:not(:first-child){border-top: 1px solid #e1e1e1;}
        .button-box{padding-top: 11px;margin-left:50px;}
        .btn-add,.btn-mul{font-size: 18px;font-weight: bold;color: #fff;border-radius: 50%;width: 30px;height: 30px;text-align: center;cursor: pointer;}
        .btn-add{background: #0A99F1;margin-right: 20px;}
        .btn-mul{background: #F75C5D}
        @font-face {
            font-family: 'iconfont';  /* project id 954075 */
            src: url('http://at.alicdn.com/t/font_954075_bb05h7rlmew.eot');
            src: url('http://at.alicdn.com/t/font_954075_bb05h7rlmew.eot?#iefix') format('embedded-opentype'),
            url('http://at.alicdn.com/t/font_954075_bb05h7rlmew.woff') format('woff'),
            url('http://at.alicdn.com/t/font_954075_bb05h7rlmew.ttf') format('truetype'),
            url('http://at.alicdn.com/t/font_954075_bb05h7rlmew.svg#iconfont') format('svg');
        }
        .iconfont {
            font-family:"iconfont" !important;
            font-size:18px;
            font-style:normal;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
        }
        .el-icon-ips-jia:before { content: "\e600"; }

        .el-icon-ips-jian:before { content: "\e601"; }
    </style>
</head>
<body>
<div class="configuration-box">

    <form action="<%=path %>/patrolConfig_edit" method="post" id="form" onsubmit="handleSubmit()">
    <div class="configuration-item">
        <div class="profile-item-box">
            <div class="profile-item">
                <div class="profile-field">管理端默认查看校区</div>
                <div class="profile-value">
                    <input type="radio" id="campus1" name="campusNum" <c:if test="${patrolConfig.campusNum==0}">checked="checked"</c:if> value=0 /><label for="campus1">主校区</label>
<%--                        <input type="radio" id="campus2" name="campusNum" <c:if test="${patrolConfig.campusNum==1}">checked="checked"</c:if> value=1 /><label for="campus1">缙云校区</label>--%>
                  <%-- <input type="radio" name="campusNum" id="campus1"  value="0"/><label for="campus1">南校区</label>
                    <input type="radio" name="campusNum" id="campus2" value="1"/><label for="campus2">北校区</label>--%>
                </div>
            </div>
            <div class="profile-item">
                <div class="profile-field">巡更异常推送频率</div>
                <div class="profile-value">
                    <div class="input-box required">
                        <input type="text" name="exceptionPushTime" id="frequency" value="${patrolConfig.exceptionPushTime }" class="text1" onkeyup="value=value.replace(/[^\d]/g,'')">
                    </div>
                    <label for="frequency">分钟/次<span class="placehodler">(&nbsp;设置后,巡更发现异常,将在${patrolConfig.exceptionPushTime}分钟后进行二次推送提醒[首次发现异常,会立即推送告知]&nbsp;)</span></label>
                </div>
            </div>
            <div class="profile-item">
                <div class="profile-field">允许到达巡更区域时长</div>
                <div class="profile-value">
                    <div class="input-box required">
                        <%--<input type="number" name="frequency" id="frequency" />--%>
                            <input type="text" name="startPatrolTime" id="frequency1" value="${patrolConfig.startPatrolTime }" onkeyup="value=value.replace(/[^\d]/g,'')">
                    </div>
                    <label for="frequency1">分钟<span class="placehodler">(&nbsp;设置后,巡更人员在点击开始巡更后,允许在${patrolConfig.startPatrolTime }分钟后到达巡更区域&nbsp;)</span></label>
                </div>
            </div>
        </div>
    </div>
    <div class="configuration-item">
        <div class="tab-box">
            <div class="tab-item">
                <div class="checkbox">
                    <input type="checkbox" id="location" name="isLocation" <c:if test="${patrolConfig.isLocation==0}">checked="checked"</c:if> value=0 />
                    <%--<input type="checkbox" id="location" name="" />--%>
                    <label for="location" onclick="selectAll(this,'location-label')"></label>
                </div>
                <span class="curr" target="location-label">定位巡更</span>
            </div>
            <%--<div class="tab-item">
                <div class="checkbox">
                    <input type="checkbox" id="signIn" name="isLocation" <c:if test="${patrolConfig.isLocation==1}">checked="checked"</c:if> value=1 />
                    &lt;%&ndash;<input type="checkbox" id="signIn" name="signIn" />&ndash;%&gt;
                    <label for="signIn" onclick="selectAll(this,'signin-label')"></label>
                </div>
                </label>
                <span target="signin-label">签到巡更</span>
            </div>--%>
        </div>

                <input type="hidden" name="id" value="${patrolConfig.id }"/>
        <input type="hidden" name="tickId" value="${patrolConfigTick.tickId }"/>
            <div class="profile-item-box tab-content" id="location-label">
                <div class="profile-item">
                    <div class="profile-field">
                        <div class="checkbox">
                            <input type="checkbox" id="cycle" name="isSignRange" <c:if test="${patrolConfigTick.isSignRange==1}">checked="checked"</c:if> value=1 />
                            <label for="cycle"></label>
                        </div>
                        <span>位置数据上传周期</span>
                    </div>
                    <div class="profile-value">
                        <div class="input-box required">
                            <%--<span class="inner-input">每</span>--%>
                           <%-- <input type="number" name="cycleValue" id="cycleValue" style="text-indent: 2em" />--%>
                            <input type="text" name="signRange" class="text1" id="cycleValue" value="${patrolConfig.signRange }" onkeyup="value=value.replace(/[^\d]/g,'')">
                        </div>
                        <label for="cycleValue">秒<span class="placehodler">(&nbsp;设置后,系统每<span class="input-result">${patrolConfig.signRange }</span>秒获取一次巡更人员的位置信息&nbsp;)</span></label>
                    </div>
                </div>
                <div class="profile-item">
                    <div class="profile-field">
                        <div class="checkbox">
                            <input type="checkbox" id="management" name="isRefreshTime" <c:if test="${patrolConfigTick.isRefreshTime==1}">checked="checked"</c:if> value=1 />
                            <label for="management"></label>
                        </div>
                        <span>管理端更新人员位置周期</span>
                    </div>
                    <div class="profile-value">
                        <div class="input-box required">
                            <%--<span class="inner-input">每</span>--%>
                            <%--<input type="number" name="managementValue" id="managementValue" style="text-indent: 2em" />--%>
                            <input type="text" name="refreshTime" class="text1" id="managementValue" value="${patrolConfig.refreshTime }" onkeyup="value=value.replace(/[^\d]/g,'')">
                        </div>
                        <label for="managementValue">秒<span class="placehodler">(&nbsp;设置后,管理端每${patrolConfig.refreshTime }秒更新一次巡更人员的位置信息&nbsp;)</span></label>
                    </div>
                </div>
                <div class="profile-item">
                    <div class="profile-field">
                        <div class="checkbox">
                            <input type="checkbox" id="unchanged" name="isLazyTime" <c:if test="${patrolConfigTick.isLazyTime==1}">checked="checked"</c:if> value=1 />
                            <label for="unchanged"></label>
                        </div>
                        <span>允许巡更人员位置不变时长</span>
                    </div>
                    <div class="profile-value">
                        <div class="input-box required">
                            <%--<input type="number" name="unchangedValue" id="unchangedValue" />--%>
                            <input type="text" name="lazyTime" class="text1" id="unchangedValue"  value="${patrolConfig.lazyTime }" onkeyup="value=value.replace(/[^\d]/g,'')">
                        </div>
                        <label for="unchangedValue">分钟<span class="placehodler">(&nbsp;设置后,巡更人员在${patrolConfig.lazyTime }分钟内位置没有变动,系统将给管理员发送预警提醒&nbsp;)</span></label>
                    </div>
                </div>
                <div class="profile-item">
                    <div class="profile-field">
                        <div class="checkbox">
                            <input type="checkbox" id="region" name="isLeaveRegionDistance" <c:if test="${patrolConfigTick.isLeaveRegionDistance==1}">checked="checked"</c:if> value=1 />
                            <label for="region"></label>
                        </div>
                        <span>允许巡更人员离开巡更区域距离</span>
                    </div>
                    <div class="profile-value">
                        <div class="input-box required">
                            <%--<input type="number" name="regionValue" id="regionValue" />--%>
                                <input type="text" name="leaveRegionDistance" id="regionValue"  class="text1" value="${patrolConfig.leaveRegionDistance }" onkeyup="value=value.replace(/[^\d]/g,'')">
                        </div>
                        <label for="regionValue">米<span class="placehodler">(&nbsp;设置后,巡更人员在离开巡更区域${patrolConfig.leaveRegionDistance }米后,系统将给管理员发送预警提醒&nbsp;)</span></label>
                    </div>
                </div>
                <div class="profile-item">
                    <div class="profile-field">
                        <div class="checkbox">
                            <input type="checkbox" id="duration" name="isLeaveRegionTime" <c:if test="${patrolConfigTick.isLeaveRegionTime==1}">checked="checked"</c:if> value=1 />
                            <label for="duration"></label>
                        </div>
                        <span>允许巡更人员离开巡更范围时长</span>
                    </div>
                    <div class="profile-value">
                        <div class="input-box required">
                            <%--<input type="number" name="durationValue" id="durationValue" />--%>
                            <input type="text" name="leaveRegionTime" id="durationValue"  class="text1" value="${patrolConfig.leaveRegionTime }" onkeyup="value=value.replace(/[^\d]/g,'')">
                        </div>
                        <label for="durationValue">分钟<span class="placehodler">(&nbsp;设置后,巡更人员在离开巡更区域${patrolConfig.leaveRegionTime }分钟后,系统将给管理员发送预警提醒&nbsp;)</span></label>
                    </div>
                </div>
                <div class="profile-item" style="height: auto">
                    <div class="profile-field" style="border-right:none">
                        <div class="checkbox">
                            <input type="checkbox" id="equipment" name="isManufacturerId" <c:if test="${patrolConfigTick.isManufacturerId==1}">checked="checked"</c:if> value=1 />
                            <label for="equipment"></label>
                        </div>
                        <span>对接定位设备</span>
                    </div>
                    <div class="profile-value" id="equipment-profile" style="display: block;line-height: 50px;border-left:1px solid #e1e1e1;padding-left: 0;">
                        <c:forEach items="${pList}" var="pList" varStatus="status">
                        <div class="manufacturer-item">
                               <input type="hidden" name="pList[${status.index}].manufacturerId" value="${pList.manufacturerId }" size="5"/>
                               <div class="form-item">
                                   <label >设备厂商：</label>
                                   <input type="text" name="pList[${status.index}].manufacturerName" class="text1" id="manufacturerName" value="${pList.manufacturerName }" onkeyup="value=value.replace(/[^\d]/g,'')">
                               </div>
                               <div class="form-item">
                                   <label >对接地址：</label>
                                   <input type="text" name="pList[${status.index}].dockingAddress" class="text1" id="dockingAddress" value="${pList.dockingAddress }" onkeyup="value=value.replace(/[^\d]/g,'')" >
                               </div>
                            <c:if test="${status.last}">
                               <div class="button-box box-none${status.index+1}">
                                   <button type="button" class="btn-add" onclick="addItem()"><span class="iconfont el-icon-ips-jia"></span></button>
                                   <c:if test="${status.index!=0}">
                                       <button type="button" class="btn-mul" onclick="delItem(this)"><span class="iconfont el-icon-ips-jian"></span></button>
                                   </c:if>
                                   </div>
                            </c:if>
                            <c:if test="${!status.last}">
                                <div class="button-box box-none${status.index+1}" style="display: none;">
                                    <button type="button" class="btn-add" onclick="addItem()"><span class="iconfont el-icon-ips-jia"></span></button>
                                    <c:if test="${status.index!=0}">
                                        <button type="button" class="btn-mul" onclick="delItem(this)"><span class="iconfont el-icon-ips-jian"></span></button>
                                    </c:if>
                                </div>
                            </c:if>
                        </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="profile-item-box tab-content" id="signin-label" style="display: none;">
                <div class="profile-item">
                    <div class="profile-field">
                        <div class="checkbox">
                            <input type="checkbox" id="bluetooth" name="" />
                            <label for="bluetooth"></label>
                        </div>
                        <span>蓝牙签到有效距离</span>
                    </div>
                    <div class="profile-value">
                        <div class="input-box required">
                            <input type="number" name="bluetoothValue" id="bluetoothValue" />
                        </div>
                        <label for="bluetoothValue">米<span class="placehodler">(&nbsp;设置后,距离蓝牙标签X米可进行签到&nbsp;)</span></label>
                    </div>
                </div>
                <div class="profile-item">
                    <div class="profile-field">
                        <div class="checkbox">
                            <input type="checkbox" id="gps" name="" />
                            <label for="gps"></label>
                        </div>
                        <span>GPS定位辅助签到有效距离</span>
                    </div>
                    <div class="profile-value">
                        <div class="input-box required">
                            <span class="inner-input">每</span>
                            <input type="number" name="gpsValue" id="gpsValue" style="text-indent: 2em" />
                        </div>
                        <label for="gpsValue">米<span class="placehodler"></label>
                    </div>
                </div>
                <div class="profile-item">
                    <div class="profile-field">
                        <div class="checkbox">
                            <input type="checkbox" id="loop" name="" />
                            <label for="loop"></label>
                        </div>
                        <span>巡更签到循环要求</span>
                    </div>
                    <div class="profile-value">
                        <div class="input-box required">
                            <input type="number" name="loopValue" id="loopValue" />
                        </div>
                        <label for="loopValue">分钟,需签到一次</label>
                    </div>
                </div>
                <div class="profile-item">
                    <div class="profile-field">
                        <div class="checkbox">
                            <input type="checkbox" id="outCycle" name="outCycle" />
                            <label for="outCycle"></label>
                        </div>
                        <span>巡更超过循环周期</span>
                    </div>
                    <div class="profile-value">
                        <input type="radio" name="outCycleValue" /><label for="campus1">记为应签</label>
                        <input type="radio" name="outCycleValue" /><label for="campus2">多余舍弃</label>
                        <label><span class="placehodler">(&nbsp;举例:要求巡更人员每30分钟巡更一次,巡更人员在第31分钟结束巡更,则应签2次,实签1次&nbsp;)</span></label>
                    </div>
                </div>
            </div>

        <div class="btn-box">
            <button type="submit" class="submit btn-commit">提交</button>
            <button type="button" class="btn-reset">重置</button>
        </div>
            </form>
    </div>
</div>
<script>
    var selected = []; //选中的数组
    //切换
    document.querySelector('.tab-box').addEventListener('click',()=>{
        if(event.target.tagName !== 'SPAN') return
    document.querySelectorAll('.tab-item>span').forEach(element=>{
        element.classList.remove('curr')
    })
    document.querySelectorAll('.tab-content').forEach(element=>{
        element.style.display = 'none'
    })
    event.target.classList.add('curr')
    document.querySelector('#'+event.target.attributes.target.value).style.display = 'block'
    })
    //重置
    document.querySelector('.btn-reset').addEventListener('click',()=>{
        document.querySelector('#form').reset()
    document.querySelector('#location').checked = false
    document.querySelector('#signIn').checked = false
    })

    function handleSubmit(){
        layer.load(1, {
            shade: [0.8,'#000'] //0.1透明度的白色背景
        });
        return true
    }
    //提交
    document.querySelector('.btn-commit').addEventListener('click',()=>{
        document.querySelectorAll('.tab-content input[type=checkbox]').forEach(element=>{
            if (element.checked) {
                selected.push(element)
            }
        })
    })
    //全选
    function selectAll(_this,params) {
        var state = !document.querySelector('#'+_this.attributes.for.value).checked
        document.querySelectorAll('#'+params+' input[type=checkbox]').forEach(element=>{
            element.checked = state
    })
    }

    //添加一项
    function addItem() {
        var id = document.querySelectorAll('#equipment-profile .manufacturer-item').length;
        document.querySelector('#equipment-profile').insertAdjacentHTML('beforeend',
                '<div class="manufacturer-item">'+
          '<div class="form-item">'+
        '<label for="">设备厂商： </label>'+
        '<input type="text" name="pList['+id+'].manufacturerName"/>'+
        '</div>'+
        '<div class="form-item">'+
        '<label for="">对接地址： </label>'+
        '<input type="text" name="pList['+id+'].dockingAddress" />'+
        '</div>'+
        '<div class="button-box box-none'+(id+1)+'">'+
        '<button type="button" class="btn-add" onclick="addItem()"><span class="iconfont el-icon-ips-jia"></span></button>'+
        '<button type="button" class="btn-mul" onclick="delItem()"><span class="iconfont el-icon-ips-jian"></span></button>'+
        '</div>'+
        '</div>'
        );
        $(".box-none"+id).hide();
    }

    //删除一项
    function delItem(_this) {
        document.querySelector('#equipment-profile').removeChild(event.target.parentNode.parentNode.parentNode);
        var id = document.querySelectorAll('#equipment-profile .manufacturer-item').length;
        $(".box-none"+id).show();
    }
    // 不是全选
    document.querySelector('#location-label').addEventListener('change',()=>{
        if (event.target.type !== 'checkbox') return
    let arr = document.querySelectorAll('#location-label input[type=checkbox]');
    let selected = []
    arr.forEach(element=>{
        if (element.checked) {
        selected.push(element)
    }
    })
    if (selected.length === arr.length) {
        document.querySelector('#location').checked = true
    }else{
        document.querySelector('#location').checked = false
    }
    })
    document.querySelector('#signin-label').addEventListener('change',()=>{
        if (event.target.type !== 'checkbox') return
    let arr = document.querySelectorAll('#signin-label input[type=checkbox]');
    let selected = []
    arr.forEach(element=>{
        if (element.checked) {
        selected.push(element)
    }
    })
    if (selected.length === arr.length) {
        document.querySelector('#signIn').checked = true
    }else{
        document.querySelector('#signIn').checked = false
    }
    })

</script>
</body>

</html>
