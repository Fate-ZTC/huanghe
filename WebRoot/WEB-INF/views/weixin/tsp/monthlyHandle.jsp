<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>月租办理</title>
		<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta name="msapplication-tap-highlight" content="no" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>tsp/css/common.css"/>
		<link rel="stylesheet" type="text/css" href="<%=basePath%>tsp/css/monthly.css"/>
		<link rel="stylesheet" type="text/css" href="<%=basePath%>tsp/css/select-bottom.css"/>
		<script src="<%=basePath%>tsp/js/jquery.min.js"></script>
		<script src="<%=basePath%>layer_mobile/layer.js"></script>
		<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
	</head>
	<body>
		<div class="jiucheng-box monthlyRenew-box f14">
			<div class="monthlyRenew-top">
				<img class="top-img" src="<%=basePath%>tsp/img/top1.png" alt="" />
				<!--<p class="monthlyRenew-cost"><span>包全天：<i>660/月</i></span><span>包白天：<i>660/月</i></span><span>包夜晚：<i>660/月</i></span></p>-->
				<input type="hidden" id="monthlypaymentRule" value=""/>
				<input type="hidden" id="basePath" value="<%=basePath%>"/>
				<p class="monthlyRenew-cost" id="monthlyRenew-cost">
				<c:forEach items="${monthlypaymentRule}" var="m">
					<span>${m.ruleName}：
					<i>${m.payPee/100}元/
					<c:if test="${m.ruleType==1}">月</c:if>
					<c:if test="${m.ruleType==2}">年</c:if>
					</i></span>
				</c:forEach>
				</p>
				<div class="top-bottom-img"></div>
			</div>
			<ul class="renewOperationList">
				<li class="parkingSelect">
					<span class="car-optipn-tit">停车场</span>
					<div class="jianYou"></div>
					<span class="car-option-right" id="parkingSelectResult">${name}</span>
					<input type="hidden" id="parkingSelectInput" name="carparkid" value="${carparkid}"/>
				</li>
				<li class="carNumSelect">
					<span class="car-optipn-tit">车牌号</span>
					<div class="jianYou"></div>
					<span class="car-option-right" id="carNumSelectResult">${plateNo}</span>
					<input type="hidden" id="carNumSelectInput" name="plateNo" value="${plateNo}"/>
				</li>
				<li class="carTypeSelect">
					<span class="car-optipn-tit">包月类型</span>
					<div class="jianYou"></div>
					<span class="car-option-right" id="carTypeSelectResult">${ruleName}</span>
					<input type="hidden" id="carTypeSelectInput" name="ruleId" value="${ruleId}" />
					<input type="hidden" id="ruleName" name="ruleName" value="${ruleName}" />
				</li>
				<li class="longTimeSelect">
					<span class="car-optipn-tit">办理时长</span>
					<div class="jianYou"></div>
					<span class="float-R renewTime">
					</span>
					<span class="car-option-right" id="longTimeSelectResult">点击选择</span>
					<input type="hidden" id="longTimeSelectInput" name="hTime" value=""/>
					<input type="hidden" name="ruleType" id="ruleType" value="${ruleType}"> 
				</li>
				<li class="carCost">
					<span class="car-optipn-tit">费用</span>
					<span class="car-option-right-text" id="carCostResult">0.00元</span>
					<input type="hidden" id="totalPay" value=""/>
					<input type="hidden" id="carCostInput" name="singlePayPee" value=""/>
				</li>
			</ul>
			<input type="hidden" value="${kid}" name="kid">
			<button class="submit-btn" onclick="sub()">提交</button>
		</div>
		<div class="select-bottom">
			<p class="select-title">选择操作</p>
			<ul class="select-ul">
				<li>发布</li>
				<li>编辑</li>
				<li>删除</li>
			</ul>
			<div class="cancel-select">取消</div>
		</div>
		<div id="selectMask"></div>
		<script src="<%=basePath%>tsp/js/monthlyHandle.js"></script>
		<script type="text/javascript">
		var path = '<%=basePath%>';
		//选择停车场
		$(".parkingSelect").click(function(){
			var carparks = ${carparks};
			var parkingSelect=new selectCarpark({
			  title: '选择停车场',
			  showCont: '#parkingSelectResult',
			  hideCont: '#parkingSelectInput',
			  actions:carparks
			})
		})
		//选择包月类型
		$(".carTypeSelect").click(function(){
			var carparkid =  $("input[name='carparkid']").val();
			
			if(carparkid==''){
				layer.open({
				    content: '请先选择停车场'
				    ,skin: 'msg'
				    ,time: 2 //2秒后自动关闭
				  });
				return;
			}
			
			$.post(path+'monthlyStyle',{'carparkId':carparkid},function(data){
			console.log(data);
			
			var jobj = JSON.parse(data);
				if(jobj.status=="true"){
					var payPee = $("#carCostInput").val();
					var carNumSelect=new selectBottom({
				  	title: '选择包月类型',
				  	showCont: '#carTypeSelectResult',
				  	hideCont: '#carTypeSelectInput',
				  	calfunction:function(payPee){
					var time = $("#longTimeSelectInput").val();
					console.log("payPee="+payPee);
						if(time!=''){
							$("#carCostResult").html((time*payPee/100)+'元');
							$("#totalPay").val(time*payPee);
						}
						},
				  	actions:jobj.monthlypaymentJson
					})
					
				}
			})
			
		})
		
		
		
		function sub(){
			var ruleId =  $("input[name='ruleId']").val();
			var ruleType =  $("input[name='ruleType']").val();
			var ruleName =  $("input[name='ruleName']").val();
			var plateNo =  $("input[name='plateNo']").val();
			var carparkid =  $("input[name='carparkid']").val();
			var totalPay = $("#totalPay").val();
			var kid =  $("input[name='kid']").val();
			var hTime =  $("input[name='hTime']").val();
			
			console.log("ruleId="+ruleId+"-ruleType="+ruleType+"-ruleName="+ruleName+"-plateNo="+plateNo+
			"-carparkid="+carparkid+"-totalPay="+totalPay+"-kid="+kid+"-hTime="+hTime);
			
			if(hTime==''){
				layer.open({
				    content: '请选择办理时长'
				    ,skin:'msg'
				    ,time: 2 //2秒后自动关闭
				  });
				return;
			}
			 layer.open({
			    type: 2,
			    content: '加载中',
			    shadeClose: false,
			  });
			$.post(path+'monthly_temPay',{'kid':kid,'handleSubFlag':1,'hTime':hTime,'ruleId':ruleId,'ruleType':ruleType,'ruleName':ruleName,'plateNo':plateNo,'carparkid':carparkid,'totalPay':totalPay},function(data){
				if(data.status=="true"){
						layer.closeAll();
						if (typeof WeixinJSBridge == "undefined"){
						   if( document.addEventListener ){
						       document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
						   }else if (document.attachEvent){
						       document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
						       document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
						   }
						}else{
						   onBridgeReady(data);
						}
					}else{
						layer.closeAll();
						layer.open({
						    content: data.errorMsg
						    ,skin: 'msg'
						    ,time: 2 //2秒后自动关闭
						 });
				}
			},'json');
		}
	function onBridgeReady(data,kid){
	   WeixinJSBridge.invoke(
	       'getBrandWCPayRequest', {
	           "appId":data.appId,     //公众号名称，由商户传入     
	           "timeStamp":data.timeStamp,         //时间戳，自1970年以来的秒数     
	           "nonceStr":data.nonceStr, //随机串     
	           "package":data.package,
	           "signType":data.signType,         //微信签名方式：     
	           "paySign":data.paySign //微信签名 
	       },
	       function(res){     
	           if(res.err_msg == "get_brand_wcpay_request:ok" ) {
	           		window.location.href=path+'monthly_paySuccess?mid='+data.mid;
	           }else{
	           		layer.closeAll();
					layer.open({
					    content: '支付失败'
					    ,skin: 'msg'
					    ,time: 2 //2秒后自动关闭
					 });
	           }  
	       }
	   ); 
	}
	var time = $("#longTimeSelectInput").val();
	function month(time){
		var payPee = $("#carCostInput").val();
		$("#carCostResult").html(time*payPee/100+'元');
		$("#totalPay").val(time*payPee);
	}
		</script>
	</body>
</html>

