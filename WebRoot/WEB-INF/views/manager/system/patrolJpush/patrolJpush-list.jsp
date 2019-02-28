<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    DateFormat df = new SimpleDateFormat("yyyy年MM月dd日，EEE", Locale.CHINA);
    String date ="今天是："+ df.format(new Date());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <style>
        ul,li{
            padding: 0;
            margin: 0;
            list-style: none;
        }
        html,body{
            margin: 0;
            padding: 0;
        }
        .main{
            padding: 20px;
        }
        .main .person-list{
            width: 600px;
            height: 410px;
            padding: 20px 20px;
            border: 1px solid #D8D8D8;
            background: #f4f5f7;
            border-top: none;
            overflow: auto;
        }
        .main .header{
            width: 600px;
            padding: 10px 20px;
            border: 1px solid #D8D8D8;
        }
        .main .header .tip{
            color: #939393;
            font-size: 14px;
            text-indent: 2em;
        }
        .main .header .check-all{
            float: right;
            margin-top: 4px;
        }
        .main .header .check-all input{
            vertical-align: middle;
        }
        .main .header button{
            padding: 6px 20px;
            outline: none;
            color: #fff;
            border: 1px solid;
            border-radius: 4px;
            background: #46A1FD;
        }
        .main .person-list .item{
            display: flex;
            padding:10px 10px;
            border: 1px solid #e1e1e1;
            margin-bottom: 20px;
            background: #fff;
        }
        .main .person-list .user-info{
            flex: 1;
        }
        .main .person-list .user-info .name{
            display: inline-block;
            width: 100px;
        }
        .main .person-list .user-info .id{
            display: inline-block;
            margin-left: 20px;
            width: 80px;
            background: #efefef;
            text-align: center;
            line-height: 1.2em;
        }
    </style>
    <script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
</head>
<body>

<%--<div class="place">--%>
    <%--<span>位置：</span>--%>
    <%--<ul class="placeul">--%>
        <%--<li><a href="#">移动救援管理</a></li>--%>
        <%--<li><a href="<%=path %>/patrolJpush_list">推送配置</a></li>--%>
    <%--</ul>--%>
<%--</div>--%>

<div class="main">
    <div class="header">
        <button onclick="submit()">提交</button>
        <span class="tip">提示:被选中的人员可自动接收救援上报的推送消息</span>
        <span class="check-all">
        <input type="checkbox" id="checkAll" onchange="checkAll()"/>全选
      </span>
    </div>
    <ul class="person-list" id="person-list">

    </ul>
</div>
<script>
    var personList = []
    $.ajax({
        url:'patrolJpushList',
        method:'get',
        success:function(d){
            var res = JSON.parse(d)
            console.log(res.code)
            if (res.code == 200){
                personList = res.data;
                var htmlStr = ''
                personList.forEach((item,index)=>{
                    htmlStr +=
                        '<li class="item">'+
                            '<div class="user-info">'+
                                '<span class="name">'+item.username+'</span>'+
                                '<span class="id">'+item.jobNum+'</span>'+
                            '</div>'+
                            '<div class="checkbox">'+
                                '<input type="checkbox" value="'+item.jobNum+'" '+(item.isJpush?"checked":"")+' onchange="handlerChange('+index+')"/>'+
                            '</div>'+
                        '</li>'
                })
                $('#person-list').html(htmlStr)
                var checkbox = document.querySelectorAll('.item input[type=checkbox]')
                var checked = document.querySelectorAll('.item input[type=checkbox]:checked')
                if (checkbox.length === checked.length) {
                    document.getElementById('checkAll').checked = true
                }else{
                    document.getElementById('checkAll').checked = false
                }
            }
        }
    })
    function handlerChange(index){
        var checkbox = document.querySelectorAll('.item input[type=checkbox]')
        var checked = document.querySelectorAll('.item input[type=checkbox]:checked')
        if (checkbox.length === checked.length) {
            document.getElementById('checkAll').checked = true
        }else{
            document.getElementById('checkAll').checked = false
        }
        if(personList[index].isJpush){
            personList[index].isJpush = 0
        }else{
            personList[index].isJpush = 1
        }
    }
    function submit() {
        var data = personList.filter(item=>item.isJpush === 1)
        console.log(data)
        $.ajax({
            url:'updateIsJpush?ids='+data.map(item=>item.id).join(','),
            type:'post',
            success:function () {
                window.location.reload()
            }
        })
    }
    // 全选
    function checkAll() {
        if (document.getElementById('checkAll').checked) {
            document.querySelectorAll('.item input[type=checkbox]').forEach(item=>{
                item.checked = true
            })
            personList.forEach(item=>{
                item.isJpush = 1
            })
        }else{
            document.querySelectorAll('.item input[type=checkbox]').forEach(item=>{
                item.checked = false
            })
            personList.forEach(item=>{
                item.isJpush = 0
            })
        }
    }
</script>
</body>
</html>
