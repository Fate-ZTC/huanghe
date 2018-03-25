<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>进出记录</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<link href="<%=path %>/layer3.1.1/mobile/need/layer.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/page/js/common.js"></script>
<script type="text/javascript" src="<%=path %>/layer3.1.1/layer.js"></script>
<script src="<%=path %>/page/chart/laydate/laydate.js"></script>
<script type="text/javascript">
	var method = '${method }';
	if(method=='addSuccess'){
		layer.msg('添加成功', {icon: 1});
	}else if(method=='editSuccess'){
		layer.msg('编辑成功', {icon: 1});
	}else if(method=='deleteSuccess'){
		layer.msg('删除成功', {icon: 1});
	}else if(method=='drivingAuthSuccess'){
		layer.msg('提交成功', {icon: 1});
	}else if(method=='driverAuthSuccess'){
		layer.msg('提交成功', {icon: 1});
	}else{
	
	}
</script>
<style>
	.excelbar li {
	    line-height: 33px;
	    float: left;
	    padding-right: 10px;
	    margin-right: 5px;
	}
</style>
</head>
<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">月租管理</a></li>
    <li><a href="<%=path %>/inOutRecord">进出记录</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    <form action="<%=path %>/inOutRecord" method="post" id="searchForm">
    <ul class="seachform">
		<li>
			<label>开始时间：</label>
			<input type="text" class="laydate-icon scinput" id="start-time" name="startTime" value="${startTime}" style="width: 137px;height:32px;" />
		</li>
		<li>
			<label>结束时间：</label>
			<input type="text" class="laydate-icon scinput" id="end-time" name="endTime" value="${endTime}" style="width: 137px;height:32px;" />
		</li>
    	<li><label>停车场</label><input name="carparkName" id="carparkName" value="${carparkName}" type="text" class="scinput" style="width: 110px;"/></li>
    	<li><label>用户账号</label><input name="mobile" id="mobile" value="${mobile}" type="text" class="scinput" style="width: 110px;"/></li>
    	<li><label>车牌号</label><input name="plateNo" id="plateNo" value="${plateNo}" type="text" class="scinput" style="width: 110px;"/></li>
		<li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="搜索"/>
		<input type="hidden" name="isRenew" value="0"/>
		<input type="hidden" name="pageSize" value="${pageSize}" />
		<input type="hidden" name="page" id="page" value="${page}"/>
		</li>
	</ul>
	</form>
    <div class="tools">
    
    	<ul class="excelbar">
	    	<li><input id="excelOut" type="button" class="scbtn" value="导出" onclick="excelOut();"/></li>
	    	<li>当日收益：${sumTotalMoney/100}元</li>
<!--    	<sec:authorize ifAnyGranted="userAuthManager_delete">-->
<!--        <li onclick="bulkDelete('<%=path %>/userAuthManager_delete','0');"><span><img src="<%=path %>/page/images/t03.png" /></span>删除</li>-->
<!--        </sec:authorize>-->
        </ul>
    
    </div>
    
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <!--<th><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>-->
        <th>停车场</th>
        <th>用户账号</th>
        <th>车位号</th>
        <th>进场时间</th>
        <th>出场时间</th>
        <th>进场照片</th>
        <th>出场照片</th>
        <th>费用</th>
        <th>用户类型</th>
        <th>付款状态</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${entryOutRecordViewPage.list}" var="m">
        <tr>
        <td>${m.carparkName}</td>
        <td>${m.mobile}</td>
        <td>${m.spotId}</td>
        <td><fmt:formatDate value="${m.inTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        <td><fmt:formatDate value="${m.outTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        <td>
	        <c:if test="${empty m.inImg}">无图片</c:if>
	        <c:if test="${!empty m.inImg}"><a id="inImg-${m.kid}"><img layer-pid="" layer-src="${m.inImg}" src="${m.inImg}" alt="点击查看进场图" style="width: 150px;"></a></c:if>
        </td>
        <td>
	        <c:if test="${empty m.outImg}">无图片</c:if>
	        <c:if test="${!empty m.inImg}"><a id="outImg-${m.kid}"><img layer-pid="" layer-src="${m.outImg}" src="${m.outImg}" alt="点击查看出场图" style="width: 150px;"></a></c:if>
        </td>
        <td>${m.totalMoney/100}</td>
        <td>
	        <c:if test="${m.parkType==1}">临停</c:if>
	        <c:if test="${m.parkType!=1}">月租</c:if>
        </td>
        <td>已付款</td>
        </tr> 
        </c:forEach>
        </tbody>
    </table>
    
   	<!-- 分页开始 -->
    <div class="pagin">
    	<div class="message">共<i class="blue">${entryOutRecordViewPage.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${entryOutRecordViewPage.currentPage}/${entryOutRecordViewPage.totalPage }&nbsp;</i>页</div>
        <ul class="paginList">
			<c:forEach items="${entryOutRecordViewPage.fristPage}" var="f">
				<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
			</c:forEach>
			<li class="paginItem current"><a href="javascript:void(0);">${entryOutRecordViewPage.currentPage}</a></li>
			<c:forEach items="${entryOutRecordViewPage.laPage}" var="l">
				<li class="paginItem"><a href="javascript:void(0);" rel="${l}">${l}</a></li>
			</c:forEach>
        </ul>
    </div>
    <!-- 分页结束 -->
    </div>
    <script type="text/javascript">
    function excelOut(){
		var path = '<%=path %>/';
		var startTime = $("#start-time").val();
		var endTime = $("#end-time").val();
		var carparkName = $("#carparkName").val();
		var mobile = $("#mobile").val();
		var plateNo = $("#plateNo").val();
		var params = {
			    'startTime':startTime,
		    	'endTime':endTime,
		    	'carparkName':carparkName,
		    	'mobile':mobile,
		    	'plateNo':plateNo
			    };
		var url = path+'excelOut';
        var body = $(document.body),
            form = $("<form method='post'></form>"),
            input;
	        form.attr({"action":url});
	        $.each(params,function(key,value){
	            input = $("<input type='hidden'>");
	            input.attr({"name":key});
	            input.val(value);
	            form.append(input);
	        });

	        form.appendTo(document.body);
	        form.submit();
	        document.body.removeChild(form[0]);
    }
    
    
	$('.tablelist tbody tr:odd').addClass('odd');
	
	var start_time = {
			  elem: '#start-time',
			  format: 'YYYY-MM-DD hh:mm:ss',
			  max: '2099-06-16',
			  istime: true,
			  istoday: false,
			  choose: function(datas){
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
			  choose: function(datas){
			    start_time.max = datas;
			    $("#end-time").val(datas);
			  }
			};
			laydate(end_time);
			$(".tablelist td a").click(function(){
				var id = $(this).attr('id');
				layer.photos({
				  photos: '#'+id
				  ,anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
				}); 
			})
			
	</script>

</body>

</html>
