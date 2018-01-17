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
		<title>添加驾驶证</title>
		<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta name="msapplication-tap-highlight" content="no" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>tsp/css/common.css"/>
		<link rel="stylesheet" type="text/css" href="<%=basePath%>tsp/css/driving.css"/>
		<script src="<%=basePath%>tsp/js/jquery.min.js"></script>
		<script src="<%=basePath%>layer_mobile/layer.js"></script>
	</head>
	<body>
		<div class="jiucheng-box add-driving-box f14">
			<form action="<%=basePath%>uploadDriving" method="post" enctype="multipart/form-data" id="fileForm">
			<input type="hidden" name="kid" value="${kid}"/>
			<input type="hidden" name="mobile" value="${mobile}"/>
			<div class="driver-license-box license-submit-box">
				<div class="driver-license-upload">
					<!--<a href="" class="license-a"><p class="driverLicense">添加驾驶证</p></a>-->
					<div class="reupload-box">
						<span>重新上传</span>
						<input type="file" id="reuploadBtn" />
					</div>
					<div class="license-upload">
						<p class="driverLicense">添加行驶证</p>
						<input type="file" class="licenseUploadBtn" />
					</div>
					<div class="read-picture-box">
						<img src="img/balance.png" class="read-picture" alt="" id="ImgPr" />
					</div>
				</div>
				<ul class="car-mesg-box">
					<li class="carNumSelect">
						<span class="car-optipn-tit">车牌号码</span>
						<div class="jianYou"></div>
						<span class="car-option-right" id="carNumSelectResult" kid="${kid}">
						<c:if test="${kid==null}">点击输入</c:if>
						<c:if test="${kid!=null}">${cars.carPlate}</c:if>
						</span>
						
						<input type="hidden" id="carNumSelectInput" name="carPlate" value="${cars.carPlate}"/>
					</li>
					<li class="carTypeSelect">
						<span class="car-optipn-tit">车辆类型</span>
						<div class="jianYou"></div>
						<span class="car-option-right" id="carTypeSelectResult">点击选择</span>
						<input type="hidden" id="carTypeSelectInput" name="vehicleType" value=""/>
					</li>
					<li class="carNumColorSelect">
						<span class="car-optipn-tit">车牌颜色</span>
						<div class="jianYou"></div>
						<span class="car-option-right" id="carNumColorSelectResult">点击选择</span>
						<input type="hidden" id="carNumColorSelectInput" name="plateColor" value=""/>
					</li>
				</ul>
				<ul class="owner-mesg-box">
					<li>
						<span class="owner-optipn-tit">车主姓名</span>
						<input type="text" placeholder="请输入姓名" class="owner-option-input" id="vehicleHost" name="vehicleHost" value=""/>
					</li>
					<li>
						<span class="owner-optipn-tit">手机号码</span>
						<input type="text" placeholder="请输入手机号码" class="owner-option-input" id="contactNum" name="contactNum" value="" />
					</li>
					<li>
						<span class="owner-optipn-tit">使用人姓名</span>
						<input type="text" placeholder="请输入使用人姓名" class="owner-option-input" id="userHost" name="userHost" value="" />
					</li>
				</ul>
				
				<button class="submit-license" type="button" onclick="sub()">提交</button>
			</div>
			</form>
		</div>
		
		<div class="box box-num-select posit-RE">
		<div class="num-select">
			<p class="select-title f12">请绑定真实有效车牌，否则无法进行电子支付。</p>
			<div class="input-box f14"><div class="province select" index="1"></div><div class="select" index="2"></div><div class="black float-L"></div><div class="select" index="3"></div><div class="select" index="4"></div><div class="select" index="5"></div><div class="select" index="6"></div><div class="select" index="7"></div></div>
			<input class="complet" type="button" value="完成">
		</div>	
		<div class="value-sel-bg posit-AB f14" style="display: block;">
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
		<script src="<%=basePath%>tsp/js/add-driving.js"></script>
		<script type="text/javascript">
		function sub(){
			if($('input[name="fileDriving"]').val()==undefined){
				 layer.open({
				    content: '请选择添加行驶证'
				    ,skin: 'msg'
				    ,time: 2 //2秒后自动关闭
				  });
			}else if($('#carNumSelectInput').val()==''){
				layer.open({
				    content: '请输入车牌号'
				    ,skin: 'msg'
				    ,time: 2 //2秒后自动关闭
				  });
			}else if($('#carTypeSelectInput').val()==''){
				layer.open({
				    content: '请选择车辆类型'
				    ,skin: 'msg'
				    ,time: 2 //2秒后自动关闭
				  });
			}else if($('#carNumColorSelectInput').val()==''){
				layer.open({
				    content: '请选择车车牌颜色'
				    ,skin: 'msg'
				    ,time: 2 //2秒后自动关闭
				  });
			}else if($('#vehicleHost').val()==''){
				layer.open({
				    content: '请输入车主姓名'
				    ,skin: 'msg'
				    ,time: 2 //2秒后自动关闭
				  });
			}else if($('#contactNum').val()==''){
				layer.open({
				    content: '请输入手机号码'
				    ,skin: 'msg'
				    ,time: 2 //2秒后自动关闭
				  });
			}else if($('#userHost').val()==''){
				layer.open({
				    content: '请输入使用人姓名'
				    ,skin: 'msg'
				    ,time: 2 //2秒后自动关闭
				  });
			}else{
				 layer.open({
				    type: 2,
				    content: '加载中',
				    shadeClose: false,
				  });
				 $('#fileForm').submit();
			}
		}
		</script>
	</body>
</html>
