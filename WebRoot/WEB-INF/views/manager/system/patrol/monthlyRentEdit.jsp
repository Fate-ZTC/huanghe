<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>车场月租信息管理-编辑</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/page/js/common.js"></script>
<script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>
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
.tablelist td:nth-child(odd){
	background:#d4e4f1;
}
 table,table tr th, table tr td {
	border:1px solid #cbcbcb;
}
.scbtn{
    margin: 0px auto;
    width: 40%;
    height:100px;
    }
.tablelist{
    width:80%;
    margin-left: 30px;
    margin-top: 30px;
    text-align: center;
    }
.inp{
    border: 1px solid #d4e4f1;
    height: 25px;
    width: 66px;
    margin-right: 100px;
    }
</style>
</head>
<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="<%=path %>/monthlyRentEdit">车场月租信息管理</a></li>
    <li><a href="#">编辑</a></li>
    </ul>
    </div>
    <form action="<%=path %>/monthlyRentEdit" method="post">
      <table class="tablelist">
    	<thead>
	    	<tr>
		        <td>停车场</td><td id="carparkName">${carpark.name}</td>
	        </tr>
	    	<tr id="after">
		        <td>月租数</td><td><input class="inp" name="totalMonRentNum" type="number" value="${carpark.totalMonRentNum}"/ style="margin-right: 112px;" min="0">个</td>
	        </tr>
<%-- 	        <c:if test="${!empty carpark.monthlypaymentRule}"> --%>
<%-- 		        <c:forEach items="${carpark.monthlypaymentRule}" var="r"> --%>
<!-- 			        <tr> -->
<%-- 				        <td>${r.ruleName}</td> --%>
<%-- 				        <td>${r.payPee/100}元/<c:if test="${r.ruleType==1}">月</c:if><c:if test="${r.ruleType==2}">年</c:if></td> --%>
<!-- 			        </tr> -->
<%-- 		        </c:forEach> --%>
<%-- 	        </c:if> --%>
<%-- 	        <c:if test="${empty carpark.monthlypaymentRule}"> --%>
<!-- 	       		<tr><td>包月类型</td><td>暂无数据，请刷新后重试</td></tr> -->
<%-- 	        </c:if> --%>
	    	<tr>
		        <td>起租时长</td><td><input class="inp" name="limitRentMon" type="number" value="${carpark.limitRentMon}"  min="1"/>个月</td>
	       	</tr>
	    	<tr>
	        	<td>当前月租数</td><td>${carpark.nowMonRentNum}个<a style="margin-left: 124px;cursor: pointer;" onclick="mon()">查看记录</a></td>
	        </tr>
        </thead>
      </table>
    <input type="hidden" name="carparkid" value="${carpark.carparkid}" />
    <input type="hidden" id="parkingCode" value="${carpark.thirdId}" />
    <input type="hidden" name="type" value="edit" />
    <input name="" type="button" class="scbtn" value="返回" onclick="window.history.back()"/>
    <input type="submit" class="scbtn" value="提交"/>
    </form>

</body>
<script type="text/javascript">
	$(function(){
		var path = '<%=path %>/';
		var parkingCode = $("#parkingCode").val();
		$.post(path+'getMonthlypayRule',{'parkingCode':parkingCode},function(data){
			console.log(data);
			if(data.status=="true"){
				$.each(data.payRules, function(index,term) {
					var tr = $("<tr></tr>");
					if(term.ruleType==1){
						var tdm = "<td>"+term.ruleName+"</td><td>"+(term.payFee/100)+"元/月</td>";
						tdm.appendTo(tr);
					}else{
						var tdy = "<td>"+term.ruleName+"</td><td>"+(term.payFee/100)+"元/年</td>";
						tdy.appendTo(tr);
					}
					$("#after").after(tr);
				});
			}else{
				$("#after").after("<tr><td>包月类型</td><td>暂无数据，请刷新后重试</td></tr>");
			}
		},'json')
	})
    function mon(){
		var path = '<%=path %>/';
		var carparkName = $("#carparkName").text();
		var params = {'carparkName':carparkName,'isRenew':0};
		var url = path+'monthlyHandle_list';
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
</script>
</html>
