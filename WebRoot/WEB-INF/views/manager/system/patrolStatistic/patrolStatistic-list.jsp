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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>巡更统计-列表</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/page/js/common.js"></script>
<script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>
<script src="<%=path %>/page/chart/laydate/laydate.js"></script>
<link href="<%=path %>/page/css/select.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/select-ui.min.js"></script>
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
<style>
.excul li{
	font-size: 1.5rem;
    margin-left: 4rem;
    margin-top: 3rem;
}
.scbtn {
    width: 60px;
}
.scinput {
    width: 80px;
    }
.select3{
    width: 80px;
    height: 32px;
    border-top: solid 1px #a7b5bc;
    border-left: solid 1px #a7b5bc;
    border-right: solid 1px #ced9df;
    border-bottom: solid 1px #ced9df;
}
</style>
</head>
<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">安防巡更</a></li>
    <li><a href="<%=path %>/patrolStatistic_list">巡更统计</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    <form action="<%=path %>/patrolStatistic_list" method="post" id="searchForm">
    <ul class="seachform">
    	<li><label>姓名:</label><input name="name" value="${name}" type="text" class="scinput" /></li>
		<li><label>账号:</label><input name="jobNum" value="${jobNum}" type="text" class="scinput" /></li>
   		<li>
			<label>开始时间：</label>
			<input type="text" class="laydate-icon scinput" id="start-time" name="startTime" value="${startTime}" style="width: 137px;height:32px;" />
		</li>
		<li>
			<label>结束时间：</label>
			<input type="text" class="laydate-icon scinput" id="end-time" name="endTime" value="${endTime}" style="width: 137px;height:32px;" />
		</li>
		<li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="搜索"/>
		<input type="hidden" name="pageSize" value="${pageSize}" />
		<input type="hidden" name="page" id="page" value="${page}"/>
		</li>
	</ul>
	</form>
    <div class="tools">
    
    	<ul class="toolbar">
        <sec:authorize ifAnyGranted="patrolStatistic_excelOut">
        <li onclick="forWardUrlParam('<%=path %>/patrolStatistic_excelOut','0');"><span><img src="<%=path %>/page/images/t07.png" /></span>导出</li>
        </sec:authorize>
        </ul>
    
    </div>
    
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <th width="40px"><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>
        <th width="100px">姓名</th>
        <th width="100px">工号</th>
        <%--<th width="100px">应巡查</th>--%>
        <th width="100px">实巡更</th>
        <%--<th width="100px">未巡查</th>--%>
            <th width="100px">异常次数</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${data}" var="d">
        <tr>
        <td><input type="checkbox" name="checkbox" value="${d.pauseId}" id="checkbox_${d.pauseId}" class="checkItem"/></td>
        <td>${d.username}</td>
        <td>${d.jobNum}</td>
        <%--<td>${d.expectedCount}</td>--%>
        <td>${d.signedCount}</td>
        <%--<td>${d.noSignCount}</td>--%>


            <td>

                <a  style="color:red;" href="javascript:void(0);" onclick="upHtm(${d.usregId},'${d.jobNum}','${d.startTime}','${d.endTime}');">${d.abnormalCount}</a>

            </td>
        </tr>
        </c:forEach>
        </tbody>
    </table>
    
   	<!-- 分页开始 -->
    <div class="pagin">
    	<div class="message">共<i class="blue">${userPageBean.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${userPageBean.currentPage}/${userPageBean.totalPage }&nbsp;</i>页</div>
        <ul class="paginList">
			<c:forEach items="${userPageBean.fristPage}" var="f">
				<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
			</c:forEach>
			<li class="paginItem current"><a href="javascript:void(0);">${userPageBean.currentPage}</a></li>
			<c:forEach items="${userPageBean.laPage}" var="l">
				<li class="paginItem"><a href="javascript:void(0);" rel="${l}">${l}</a></li>
			</c:forEach>
        </ul>
    </div>
    <!-- 分页结束 -->
    </div>
    <script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
	var start_time = {
			  elem: '#start-time',
			  format: 'YYYY-MM-DD hh:mm:ss',
			  max: '2099-06-16',
			  istime: true,
			  istoday: false,
			  choose: function(datas) {
			     end_time.min = datas;
			     $("#start-time").val(datas);
			  }
			};
			laydate(start_time);
			
			//结束时间
			var end_time = {
			  elem: '#end-time',
			  format: 'YYYY-MM-DD hh:mm:ss',
			  max: '2099-06-16',
			  istime: true,
			  istoday: false,
			  choose: function(datas) {
			    start_time.max = datas;
			    $("#end-time").val(datas);
			  }
			};
			laydate(end_time);
	var the_host = "<%=path%>/";


		//这里进行
        var forWardUrlParam = function (url) {
            //进行组装参数
            var param = '';
            var name = $("input[name='name']").val();//姓名
            var usercode = $("input[name='usercode']").val();//账号
            var pauseStart =  $("input[name='pauseStart']").val();//开始时间
            var pauseEnd =  $("input[name='pauseEnd']").val();//结束时间
            //获取选中的值
            var patrol = $("#select option:selected").val();
            var now = new Date().getTime();
            param += "?now=" + now;
            if(name != undefined && name != "") {
                param += "&name=" + name;
            }
            if(usercode != undefined && usercode != "") {
                param += "&usercode=" + usercode;
            }
            if(pauseStart != undefined && pauseStart != "") {
                param += "&pauseStart=" + pauseStart;
            }
            if(pauseEnd != undefined && pauseEnd != "") {
                param += "&pauseEnd=" + pauseEnd;
            }
            window.location.href = url + param;



        };

    /**
     * 设置选中
     */
    // var selectStatus = function () {
    //         //进行默认选中
    //         var status = $("input[name='patrolStatus']").val();
    //         if(status != undefined) {
    //             if(status == 1) {
    //                 $(".select3").find("option[value='1']").prop("selected","selected");
    //             }
    //             if(status == 0) {
    //                 $(".select3").find("option[value='0']").prop("selected","selected");
    //             }
    //         }
    //     }

    var the_host = "<%=path%>/";
    function upHtm(usregId,jobNum,startTime,endTime) {
        startTime = $("#start-time").val();
        endTime = $("#end-time").val();
        layer.open({
            type:2,
            title:'异常详情',
            area: ['772px', '470px'],
            content:"<%=basePath%>toSelectPatrolExceptionInfosByjobNum?usregId=" + usregId+"&jobNum=" + jobNum +"&startTime="+startTime+"&endTime="+endTime
        })
        <%--window.location.href = "<%=basePath%>toSelectPatrolExceptionInfosByjobNum?usregId=" + usregId+"&jobNum=" + jobNum +"&startTime="+startTime+"&endTime="+endTime;--%>
    }


	</script>

</body>

</html>
