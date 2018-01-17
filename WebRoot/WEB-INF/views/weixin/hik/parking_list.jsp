<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no, minimal-ui" />
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<meta name="msapplication-tap-highlight" content="no" />
		<title>酒城停车</title>
		<link rel="stylesheet" href="<%=basePath %>hik/css/common.css" />
		<link rel="stylesheet" href="<%=basePath %>hik/css/pullToRefresh.css" />
		<link rel="stylesheet" href="<%=basePath %>hik/css/style.css" />
		<script src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
	</head>
	<body>
		<div class="wrap" id="wrapper">
			<ul class="wrap1">
				<c:forEach items="${hikParking}" var="p">
<!--					<li class="depot-show-list radius-4">-->
<!--						<div>-->
<!--							<p class="list-left">-->
<!--								<span class="depot-name over"><i class="green-circle"></i>${p.name}</span>-->
<!--								<span class="depot-info">${p.feeRates}</span>-->
<!--							</p>-->
<!--							<p class="list-right">-->
<!--								<span class="depot-num">${p.enableBerth}个</span>-->
<!--								<span class="surplus-depot">剩余车位</span>-->
<!--							</p>-->
<!--						</div>-->
<!--						<a href="<%=basePath%>wxGoParking_to?longitude=${p.longitude}&latitude=${p.latitude}" class="depot-btn">去这儿</a>-->
<!--					</li>-->
					
					<li class="depot-show-list radius-4">
					<div>
						<p class="list-left">
							<span class="depot-name over"><i class="green-circle"></i>${p.name}</span>
							<span class="depot-info">${p.feeRates}</span>
						</p>
						<p class="list-right">
							<span class="depot-num">${p.enableBerth}个</span>
							<span class="surplus-depot">剩余车位</span>
						</p>
					</div>
					<p class="depot-address">
						<i class="address-pic"></i>
						<span>${p.province}${p.city}${p.county}${address}</span>
					</p>
					<a href="javascript:void(0);" onclick="toLoc(${p.longitude},${p.latitude},'${p.province}${p.city}${p.county}${address}','${p.name}')" class="depot-btn">去这儿</a>
				</li>
				</c:forEach>
			</ul>
		</div>
	</body>
	<script type="text/javascript" src="<%=basePath %>hik/js/jquery-1.9.1.min.js" ></script>
	<script src="<%=basePath %>hik/js/fastclick.js"></script>
	<script type="text/javascript" src="<%=basePath %>hik/js/iscroll.js" ></script>
	<script type="text/javascript" src="<%=basePath %>hik/js/pullToRefresh.js" ></script>
	<script type="text/javascript" src="<%=basePath %>hik/js/base.js" ></script>
	<script type="text/javascript" src="<%=basePath %>hik/js/index.js" ></script>
	<script>
  /*
   * 注意：
   * 1. 所有的JS接口只能在公众号绑定的域名下调用，公众号开发者需要先登录微信公众平台进入“公众号设置”的“功能设置”里填写“JS接口安全域名”。
   * 2. 如果发现在 Android 不能分享自定义内容，请到官网下载最新的包覆盖安装，Android 自定义分享接口需升级至 6.0.2.58 版本及以上。
   * 3. 常见问题及完整 JS-SDK 文档地址：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
   *
   * 开发中遇到问题详见文档“附录5-常见错误及解决办法”解决，如仍未能解决可通过以下渠道反馈：
   * 邮箱地址：weixin-open@qq.com
   * 邮件主题：【微信JS-SDK反馈】具体问题
   * 邮件内容说明：用简明的语言描述问题所在，并交代清楚遇到该问题的场景，可附上截屏图片，微信团队会尽快处理你的反馈。
   */
	  wx.config({
	    debug: false,
	    appId: '${appid}',
	    timestamp: ${timestamp},
	    nonceStr: '${nonceStr}',
	    signature: '${signature}',
	    jsApiList: ['openLocation']
	  });
	  wx.ready(function () {
	      
	  });
	  wx.error(function(res){
	  	alert('失败');
	  });
	  function toLoc(lon,lat,na,addre){
	  		wx.openLocation({
			    latitude: lat, // 纬度，浮点数，范围为90 ~ -90
			    longitude: lon, // 经度，浮点数，范围为180 ~ -180。
			    name: na, // 位置名
			    address: addre, // 地址详情说明
			    scale: 28, // 地图缩放级别,整形值,范围从1~28。默认为最大
			    infoUrl: '' // 在查看位置界面底部显示的超链接,可点击跳转
			});
	  }
	  
	</script>
</html>

