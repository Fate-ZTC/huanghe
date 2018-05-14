<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	DateFormat df = new SimpleDateFormat("yyyy年MM月dd日，EEE", Locale.CHINA);
	String date ="今天是："+ df.format(new Date());
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>消防巡查记录</title>
		<link rel="stylesheet" href="<%=basePath%>page/css/common-PC.css" />
		<link rel="stylesheet" href="<%=basePath%>page/css/gallery.css" />
		<link rel="stylesheet" href="<%=basePath%>page/css/Qstyle.css" />
		<script src="<%=basePath%>page/js/jquery.js" type="text/javascript"></script>
		<script type="text/javascript" src="<%=basePath%>page/js/common.js"></script>
		<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />

	</head>
	<body>


	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="#">消防巡查</a></li>
			<li><a href="<%=path %>/firePatrolInfo_list">巡查记录</a></li>
		</ul>
	</div>



		<div class="box f14 inspection-record-box">
			<ul class="inspection-record-list">
				<li>
					<label class="inspection-record-tit float-L">现场照片</label>
					<div class="scene-picture-box float-L">
						<div class="mygallery clearfix">
							<div class="tn3 album">
							    <ol>
									<c:forEach items="${firePatrolImgs}" var="d">
										<li>
											<a href="${d.imgUrl}">
												<img src="${d.imgUrl}" />
											</a>
										</li>
									</c:forEach>
									<%--<li>--%>
									    <%--<a href="http://www.jq22.com/demo/jquery-lbt-150305232001/images/bp1.jpg">--%>
										<%--<img src="http://www.jq22.com/demo/jquery-lbt-150305232001/images/bp1.jpg" />--%>
									    <%--</a>--%>
									<%--</li>--%>
									<%--<li>--%>
									    <%--<a href="http://www.jq22.com/demo/jquery-lbt-150305232001/images/bp2.jpg">--%>
										<%--<img src="http://www.jq22.com/demo/jquery-lbt-150305232001/images/bp2.jpg" />--%>
									    <%--</a>--%>
									<%--</li>--%>
									<%--<li>--%>
									    <%--<a href="http://www.jq22.com/demo/jquery-lbt-150305232001/images/bp14.jpg">--%>
										<%--<img src="http://www.jq22.com/demo/jquery-lbt-150305232001/images/bp14.jpg" />--%>
									    <%--</a>--%>
									<%--</li>--%>
									<%--<li>--%>
									    <%--<a href="http://www.jq22.com/demo/jquery-lbt-150305232001/images/bp26.jpg">--%>
											<%--<img src="http://www.jq22.com/demo/jquery-lbt-150305232001/images/bp26.jpg" />--%>
									    <%--</a>--%>
									<%--</li>--%>
							    </ol>
							</div>
					    </div>
					    
				    </div>
				</li>
				
				<li>
					<label class="inspection-record-tit float-L">异常描述</label>
					<div class="inspection-record-result-input-box float-L">
						<ul class="inspection-record-input-top">
							<c:forEach var="exception" items="${exceptions}">
								<li>${exception.exceptionName}</li>
							</c:forEach>
							<%--<li>机器磨损</li>--%>
							<%--<li>机器磨损</li>--%>
							<%--<li>机器磨损</li>--%>
						</ul>
						<textarea class="inspection-record-textarea" name="" rows="" cols="" readonly>${description}</textarea>
					</div>
				</li>
				<li>
					<label class="inspection-record-tit float-L"></label>
					<button class="inspection-record-btn" onclick="backPreviousPage()">返回</button>
				</li>
			</ul>
		</div>
		
		<script src="<%=basePath%>page/js/jquery.gallery.min.js"></script>
		<script>
			//初始化图片查看器
			$(document).ready(function() {
				var tn1 = $('.mygallery').tn3({
					skinDir:"skins",
					imageClick:"fullscreen",
					image:{
					maxZoom:1.5,
					crop:true,
					clickEvent:"dblclick",
					transitions:[{
					type:"blinds"
					},{
					type:"grid"
					},{
					type:"grid",
					duration:460,
					easing:"easeInQuad",
					gridX:1,
					gridY:8,
					// flat, diagonal, circle, random
					sort:"random",
					sortReverse:false,
					diagonalStart:"bl",
					// fade, scale
					method:"scale",
					partDuration:360,
					partEasing:"easeOutSine",
					partDirection:"left"
					}]
					}
				});
			});

			//点击标签能添加文字
			// $(".inspection-record-input-top li").click(function(){
			// 	var t=$(this).text();
			// 	var val=$(".inspection-record-textarea").val();
			// 	$(".inspection-record-textarea").val(val+t);
			// })



			//这里进行处理返回按钮
			function backPreviousPage() {
				history.go(-1);
            }
		</script>
	</body>
</html>
