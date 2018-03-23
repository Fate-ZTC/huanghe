<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车场月租信息管理</title>
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
    <li><a href="<%=path %>/monthlyRentManage">车场月租信息管理</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    <form action="<%=path %>/monthlyRentManage" method="post" id="searchForm">
    <ul class="seachform">
    	<li><label>停车场</label><input name="carparkName" id="carparkName" value="${carparkName}" type="text" class="scinput" style="width: 110px;"/></li>
		<li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="搜索"/>
		<input type="hidden" name="pageSize" value="${pageSize}" />
		<input type="hidden" name="page" id="page" value="${page}"/>
		</li>
	</ul>
	</form>
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <!--<th><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>-->
        <th>停车场</th>
        <th>月租数量</th>
        <th>当前月租数量</th>
        <th>包月价格</th>
        <th>起租时长</th>
        <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${carparksPage.list}" var="m">
        <input type="hidden" class="parkingCode" carparkid="${m.carparkid}" value="${m.thirdId}"/>
        <tr>
        <td>${m.name}</td>
        <td>${m.totalMonRentNum}</td>
        <td>${m.nowMonRentNum}</td>
        <td id="${m.carparkid}_id"></td>
<!--         <td> -->
<%--         <c:if test="${!empty m.monthlypaymentRule}"> --%>
<%-- 	        <c:forEach items="${m.monthlypaymentRule}" var="r"> --%>
<%-- 	        	<span>${r.ruleName}：<i>${r.payPee/100}/<c:if test="${r.ruleType==1}">月</c:if><c:if test="${r.ruleType==2}">年</c:if></i>|</span> --%>
<%-- 	        </c:forEach> --%>
<%--         </c:if> --%>
<%--         <c:if test="${empty m.monthlypaymentRule}"> --%>
<!-- 	       	 暂无数据，请刷新后重试 -->
<%--         </c:if> --%>
<!--         </td> -->
        <td>${m.limitRentMon}个月</td>
        <td><a href="<%=path %>/monthlyRentEdit?carparkid=${m.carparkid}">编辑</a></td>
        </tr> 
        </c:forEach>
        </tbody>
    </table>
    
   	<!-- 分页开始 -->
    <div class="pagin">
    	<div class="message">共<i class="blue">${carparksPage.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${carparksPage.currentPage}/${carparksPage.totalPage }&nbsp;</i>页</div>
        <ul class="paginList">
			<c:forEach items="${carparksPage.fristPage}" var="f">
				<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
			</c:forEach>
			<li class="paginItem current"><a href="javascript:void(0);">${carparksPage.currentPage}</a></li>
			<c:forEach items="${carparksPage.laPage}" var="l">
				<li class="paginItem"><a href="javascript:void(0);" rel="${l}">${l}</a></li>
			</c:forEach>
        </ul>
    </div>
    <!-- 分页结束 -->
    </div>
</body>
<script>
	$(function(){
		var path = '<%=path %>/';
		$(".parkingCode").each(function(k,v){
			var parkingCode = $(v).val();
			var carparkid = $(v).attr("carparkid");
			$.post(path+'getMonthlypayRule',{'parkingCode':parkingCode},function(data){
				console.log(data);
				if(data.status=="true"){
					var text = '';
					$.each(data.payRules, function(index,term) {
						if(term.ruleType==1){
							text += term.ruleName+":"+(term.payFee/100)+"元/月|"
						}else{
							text += term.ruleName+":"+(term.payFee/100)+"元/年|"
						}
					});
					$("#"+carparkid+"_id").text(text.substr(0,text.length-1));
				}else{
					$("#"+carparkid+"_id").text("暂无数据，请刷新重试");
				}
			},'json')
		});
	})
</script>
</html>
