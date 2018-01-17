<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<title>临停缴费</title>
	<link rel="stylesheet" href="<%=basePath %>/tsp/css/common.css">
	<link rel="stylesheet" href="<%=basePath %>/tsp/css/jiucheng.css">
	<script src="<%=basePath %>/tsp/js/jquery.min.js"></script>
	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.1.js"></script>
	<script type="text/javascript" src="<%=basePath%>/tsp/js/pingpp_pay.js"></script>
	<script src="<%=basePath %>/layer_mobile/layer.js"></script>
	<script type="text/javascript">
		var the_host = '<%=basePath %>';
		var park_id='${parkId}';
		var car_plate='${vcw.carPlate}';
		var mobile = '${users.mobile}';
		var park_name = '${parkName}';
		var openid = '${users.openid}';
		var errMsg = '${errMsg}';
		var kid = '${vcw.kid}';
	</script>
</head>
<body>
	<div class="box posit-RE box-start" style="padding-bottom:.42rem;">
		<p class="title f14">基本信息</p>
		<ul class="mesg-box">
			<li class="mesg">
				<span class="mesg-name float-L">停车场：</span>
				<span class="message float-L mesg-place">${parkName}</span>
				<span class="revamp float-R revamp-place">修改</span>
			</li>
			<li class="mesg">
				<span class="mesg-name float-L">车牌号码：</span>
				<span class="message float-L mesg-num">${vcw == null ? ppv.carplate : vcw.carPlate}</span>
				<span class="revamp float-R revamp-num">修改</span>
			</li>
			<li class="mesg">
				<span class="mesg-name float-L">入场时间：</span>
				<span class="message float-L" id="in-time">${vcw.formatInTime()}</span>
			</li>
			<li class="mesg">
				<span class="mesg-name float-L">已停车：</span>
				<span class="message float-L" id="park-time">${vcw.formatParkTime()}</span>
			</li>
		</ul>
		<p class="title f14">停车费用</p>
		<ul class="mesg-box">
			<li class="mesg">
				<span class="mesg-name float-L">停车费：</span>
				<span class="message float-L" id="s-pay">${shouldPay}</span>
				<span class="yuan float-R">元</span>
			</li>
			<li class="mesg">
				<span class="mesg-name float-L">已支付：</span>
				<span class="message float-L" id="h-pay">${hasPayed}</span>
				<span class="yuan float-R">元</span>
			</li>
			<li class="mesg">
				<span class="mesg-name float-L">本次实际缴费：</span>
				<span class="message float-L" id="n-pay">${needPay}</span>
				<span class="yuan float-R">元</span>
			</li>
		</ul>
		<p class="title f14">支付方式</p>
		<ul class="pay-box">
			<li class="pay">
				<span class="wiexin-logo float-L"><img src="<%=basePath %>/tsp/img/wx.png" alt=""></span>
				<div class="prompt float-L">
					<p class="prompt1 float-L f16">微信支付</p>
					<p class="prompt2 float-L f12">推荐安装微信5.0及以上版本的使用</p>
				</div>
				
				<span class="xuan float-R pay-checked"></span>
			</li>
		</ul>
		<p class="warm f12">温馨提示：支付成功后，请在指定时间内驶离车场，超过指定时间未离场将重新计费！</p>
		<div class="pay-btn text-CENT f16 posit-AB" onclick="subPay()">支付</div>
		
		
		<div class="shodow posit-AB"></div>
		
		<!--支付超时提示-->
		<div class="pay-lose-cash posit-AB text-CENT">
			<P class="pay-kuang-title">支付提示</P>
			<p class="cash f16">支付超时，请重新获取停车费用。</p>
			<div class="know">知道了</div>
		</div>
		
		<!--支付失败提示-->
		<div class="get-lose posit-AB text-CENT">
			<P class="pay-kuang-title">支付提示</P>
			<p class="lose-weixin-tishi f16">请在收费处使用现金完成</p>
			<p class="lose-get-tishi f16">停车缴费</p>
			<div class="btn-box">
				<div class="detail-btn float-L text-CENT">取消</div>
				<div class="chongshi-btn float-L text-CENT">确认</div>
			</div>
		</div>
		
		<!--缴费提示-->
		<div class="get-pay posit-AB text-CENT">
			<P class="pay-kuang-title">支付提示</P>
			<p class="lose-weixin-tishi f16">停车：<span id="park-time-1">${vcw.formatParkTime()}</span></p>
			<p class="lose-get-tishi f16">应支付：<span id="n-pay-1">${needPay}</span>元</p>
			<div class="btn-box">
				<div class="detail-btn float-L text-CENT">取消</div>
				<div class="sure-btn float-L text-CENT">确认支付</div>
			</div>
		</div>
	</div>
	<div class="box box-num-select posit-RE">
		<div class="num-select">
			<p class="select-title f12">请绑定真实有效车牌，否则无法进行电子支付。</p>
			<div class="input-box f14"><div class="province select" index="1"></div><div class="select" index="2"></div><div class="black float-L"></div><div class="select" index="3"></div><div class="select" index="4"></div><div class="select" index="5"></div><div class="select" index="6"></div><div class="select" index="7"></div></div>
			<input class="complet" type="button" value="完成">
		</div>	
		<div class="value-sel-bg posit-AB f14">
			<div class="option-box">
				<div class="option" style="margin-left:0.04rem;">京</div><div class="option">津</div><div class="option">冀</div><div class="option">鲁</div><div class="option">晋</div><div class="option">蒙</div><div class="option">辽</div><div class="option">吉</div><div class="option">黑</div><div class="option" style="margin-right:0.03rem;">沪</div>
			</div>
			<div class="option-box">
				<div class="option" style="margin-left:0.04rem;">苏</div><div class="option">浙</div><div class="option">皖</div><div class="option">闽</div><div class="option">赣</div><div class="option">豫</div><div class="option">鄂</div><div class="option">湘</div><div class="option">粤</div><div class="option" style="margin-right:0.03rem;">桂</div>
			</div>
			<div class="option-box" style="width:2.63rem;">
				<div class="option" style="margin-left:0.07rem;">渝</div><div class="option">川</div><div class="option">贵</div><div class="option">云</div><div class="option">藏</div><div class="option">陕</div><div class="option">甘</div><div class="option">青</div>
			</div>
			<div class="option-box" style="width:1.98rem;">
				<div class="option" style="margin-left:0.06rem;">琼</div><div class="option">新</div><div class="option">港</div><div class="option">澳</div><div class="option">台</div><div class="option">宁</div>
			</div>
		</div>
		<div class="value-letter-bg posit-AB f14">
			<div class="option-box">
				<div class="option" style="margin-left:0.04rem;">1</div><div class="option">2</div><div class="option">3</div><div class="option">4</div><div class="option">5</div><div class="option">6</div><div class="option">7</div><div class="option">8</div><div class="option">9</div><div class="option" style="margin-right:0.03rem;">0</div>
			</div>
			<div class="option-box">
				<div class="option" style="margin-left:0.04rem;">港</div><div class="option">Q</div><div class="option">W</div><div class="option">E</div><div class="option">R</div><div class="option">T</div><div class="option">Y</div><div class="option">U</div><div class="option">P</div><div class="option" style="margin-right:0.03rem;">澳</div>
			</div>
			<div class="option-box" style="width:2.95rem;">
				<div class="option" style="margin-left:0.07rem;">A</div><div class="option">S</div><div class="option">D</div><div class="option">F</div><div class="option">G</div><div class="option">H</div><div class="option">J</div><div class="option">K</div><div class="option">L</div>
			</div>
			<div class="option-box float-R" style="width:2.73rem;">
				<div class="option" style="margin-left:0.06rem;">Z</div><div class="option">X</div><div class="option">C</div><div class="option">V</div><div class="option">B</div><div class="option">N</div><div class="option">M</div><div class="del"></div>
			</div>
		</div>
	</div>
	<div class="box posit-RE box-place-select">
		<div class="select-place">
			<ul class="place-list f14">
				<c:forEach items="${hikParking}" var="cp">
					<li>
						<span class="place" rel="${cp.carparkid}">${cp.name}</span><span class="click_select ${cp.carparkid==parkId?'click_select-act':''}"></span>
					</li>
				</c:forEach>
			</ul>
			<input class="place-btn" type="button" value="确定">
		</div>
	</div>

	<!-- 选择车辆 -->
	<div class="box box-car-select f14">
		<div class="vehicle-top">
			<div class="vehicle-add-btn"></div>
			<p>您当前还可以添加：<span class="vehicle-num">3</span>辆</p>
		</div>
		<ul class="vehicle-list">
			<c:forEach items="${carList}" var="cr">
				<li class="vehicle-li">
					<span class="car-num">${cr.carPlate}</span>
					<input type="radio" name="car" class="carSelect" ${cr.carPlate==vcw.carPlate? 'checked' : ''}>
				</li>
			</c:forEach>
		</ul>
		<button class="car-sure">确定</button>
	</div>

	<script>
	$(".revamp-place").click(function(){
		$(".box-start").hide();
		$(".box-place-select").show();
	})
	
	$('body,html').animate({scrollTop:100},500);
	
	// 添加车牌
	/*$(".vehicle-add-btn").click(function(){
		if ($(".vehicle-li").length>=3) {
			layer.open({
				content: '最多只允许添加3辆车'
				,skin: 'msg'
				,time: 2 //2秒后自动关闭
			});
			return;
		};
		$(".vehicle-list").append('<li class="vehicle-li"><span class="car-num"></span><input type="radio" name="car" class="carSelect"></li>')
		$(".box-vehicle").hide();
		$(".box-num-select").show();
		var obj=$(".car-num").last();
		console.log($(obj).text());
		carNumselect(obj);
	})*/
	var amount = '${needPay}';
	function subPay(){
			
		if($(".xuan").hasClass("pay-checked")){
			layer.open({type: 2});
			var leaveTime = '${vcw.formatPlanyTime()}';
			var parkTime =  '${vcw.formatParkTime()}';
			var enterTime = '${vcw.formatInTime()}';
			var inunid = '${vcw.inUnid}';
			amount = amount*100;
			if(amount==0){
				layer.closeAll();
				alert("费用为0，无需支付");
				return;
			}
			 $.post(the_host+'wxCost_temPay',{'openid':openid,'amount':amount,'carNumber':car_plate,'inunid':inunid,'kid':kid},
				function(data){
					if(data.status=="true"){
						var charge = data.charge;
						var ch_id = charge.id;
						pingpp.createPayment(charge, function(result, err){
							if (result == "success") {
								$.post(the_host+'wxCost_pushCost',{'kid':kid,'chargeid':ch_id},function(data){
									if(data.status=="success"){
										layer.closeAll();
										layer.open({
										    content: '支付成功'
										    ,skin: 'msg'
										    ,time: 2 //2秒后自动关闭
										 });
										window.alert(kid);
										window.location.href=the_host+'wxCost_paySuccess?kid='+kid;
									}
									else{
										layer.closeAll();
										layer.open({
										    content: '支付失败'
										    ,skin: 'msg'
										    ,time: 2 //2秒后自动关闭
										 });
								        window.location.href=the_host+'wxCost_payFailed';
									}
								},'json');
						        // 只有微信公众账号 wx_pub 支付成功的结果会在这里返回，其他的 wap 支付结果都是在 extra 中对应的 URL 跳转。
						        
						    } else if (result == "fail") {
						        // charge 不正确或者微信公众账号支付失败时会在此处返回
						    	layer.closeAll();
						        console.log(err);
						        layer.open({
								    content: '支付失败，请刷新重试'
								    ,skin: 'msg'
								    ,time: 2 //2秒后自动关闭
								 });
						    } else if (result == "cancel") {
						    	layer.closeAll();
						    	layer.open({
								    content: '取消支付'
								    ,skin: 'msg'
								    ,time: 2 //2秒后自动关闭
								 });
						    }
				 		});
					}else{
						layer.closeAll();
						layer.open({
						    content: data.errorMsg
						    ,skin: 'msg'
						    ,time: 2 //2秒后自动关闭
						 });
					}
			 },'json');
		}else{
			layer.closeAll();
			layer.open({
				content: '请选择支付方式'
				,skin: 'msg'
				,time: 2 //2秒后自动关闭
			});
		}
		
		
	}
	
	if(errMsg != ''){
		layer.closeAll();
		layer.open({
		    content: errMsg
		    ,skin: 'msg'
		    ,time: 2 //2秒后自动关闭
		 });
	}
	</script>
	<script src="<%=basePath %>/tsp/js/carNumSelect.js"></script>
	<script src="<%=basePath %>/tsp/js/carSelect.js"></script>
	<script src="<%=basePath %>/tsp/js/index.js"></script>
</body>
</html>
