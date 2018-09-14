<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	DateFormat df = new SimpleDateFormat("yyyy年MM月dd日，EEE", Locale.CHINA);
	String date ="今天是："+ df.format(new Date());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
	<title>安防巡更|巡更区域管理</title>
	<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<link href="<%=path %>/page/css/select.css" rel="stylesheet" type="text/css" />
<link href="<%=path %>/layer3.1.1/mobile/need/layer.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/select-ui.min.js"></script>
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
.updul li{
    font-size: 1.5rem;
    margin-left: 3rem;
    margin-top: 3rem;
    }
.btn{
    font-size: 1.5rem;
    margin-left: 3rem;
    margin-top: 3rem;
    }
</style>
</head>
<body>
	<div class="place">
	    <span>位置：</span>
		    <ul class="placeul">
			    <li><a href="#">安防巡更</a></li>
			    <li><a href="<%=path %>/patrolRegList">巡更区域管理</a></li>
		    </ul>
	    </div>
	<div class="rightinfo">
				<%--
					分页表单				
				--%>
				<form action="<%=path %>/patrolRegList" method="post" id="searchForm">
				    <ul class="seachform">
				    	<li><label>巡更区域名称</label>
							<div class="vocation">
								<select class="select3" name="regionId" >
						        	<option value="-1">-请选择-</option>
						        	<c:forEach items="${patrolRegions}" var="p">
										<option value="${p.id}">${p.regionName}</option>
						        	</c:forEach>
						        </select>
							</div>
						</li>
						<li><label>&nbsp;</label><input name="" type="submit" class="scbtn" value="搜索"/>
						<input type="hidden" name="pageSize" value="${pageSize}"/>
						<input type="hidden" name="page" id="page" value="${page}"/>
						</li>
					</ul>
				</form>
				<div class="tools">
			    	<ul class="toolbar">
				    	<sec:authorize ifAnyGranted="patrolReg_add">
				        <li onclick="addHtm();"><span><img src="<%=path %>/page/images/t01.png" /></span>添加</li>
				        </sec:authorize>
				    	<sec:authorize ifAnyGranted="patrolReg_delete">
				        <li onclick="bulkDelete('<%=path %>/patrolReg_delete','0');"><span><img src="<%=path %>/page/images/t03.png" /></span>删除</li>
				        </sec:authorize>
						<sec:authorize ifAnyGranted="patrolReg_add">
							<li onclick="showAllDraw();"><span><img src="<%=path %>/page/images/t01.png"/></span>显示所有区域</li>
						</sec:authorize>
			        </ul>
			    </div>
				<table width="100%" class="tablelist">
		            <tbody width="100%">
		            	<thead>
					    	<tr>
					        	<th class="tbt1">
									<input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>
			            		<th>巡更区域名称</th>
								<th>绘制颜色</th>
			            		<th>更新时间</th>
			            		<th>操作</th>
					        </tr>
				        </thead>
		            	<c:forEach items="${patrolRegionPage.list}" var="m">
					        <tr>
					        <td>
								<input type="checkbox" name="checkbox" value="${m.id}" id="checkbox_${m.id}" class="checkItem"/>
							</td>
					        <td>${m.regionName}</td>
								<td>${m.color}</td>
					        <td>${m.lastUpdateTime}</td>
					        <td style="color:#16c2ea;">
								<a  style="color:#16c2ea;" href="javascript:void(0);" onclick="upHtm('${m.regionName}',${m.id},'${m.color}');">修改名称</a>
								<a style="color:#16c2ea;" href="javascript:void(0);" onclick="toPatrolMap(${m.id})">配置巡更范围</a>
								<a style="color:#16c2ea;" href="javascript:void(0);" onclick="toPatroSignPortlMap(${m.id})">配置巡更点位</a>
							</td>
					        </tr> 
				        </c:forEach>
			        </tbody>
			    </table>
			   	<!-- 分页开始 -->
			    <div class="pagin">
			    	<div class="message">共<i class="blue">${patrolRegionPage.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${patrolRegionPage.currentPage}/${patrolRegionPage.totalPage }&nbsp;</i>页</div>
			        <ul class="paginList">
						<c:forEach items="${patrolRegionPage.fristPage}" var="f">
							<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
						</c:forEach>
						<li class="paginItem current"><a href="javascript:void(0);">${patrolRegionPage.currentPage}</a></li>
						<c:forEach items="${patrolRegionPage.laPage}" var="l">
							<li class="paginItem"><a href="javascript:void(0);" rel="${l}">${l}</a></li>
						</c:forEach>
			        </ul>
			    </div>
			    <!-- 分页结束 -->
</div>
<script type="text/javascript">
		$(function() {
				//选择框
				$(".select3").uedSelect({
					width : 150
				});
				/**
				 * 弹出页面
				 */
			});
		var the_host = "<%=path%>/";
		function upHtm(regionName,regionId,color) {

			window.location.href = "<%=basePath%>toSelectColorPageUpdate?regionId=" + regionId
				+ "&regionName=" + encodeURIComponent(regionName) + "&color=" + encodeURIComponent(color);

		    //TODO 下面是之前在当前页面显示的弹框（废弃）
			<%--$(".layui-layer-title").text("修改信息");--%>
			<%--layer.open({--%>
			  <%--type: 1,--%>
			  <%--skin: 'layui-layer-rim', //加上边框--%>
			  <%--area: ['420px', '340px'], //宽高--%>
			  <%--content: '<form action="<%=path %>/patrolReg_update" method="post" id="editForm">'+--%>
						<%--'<ul class="updul">' +--%>
			  <%--'<li>巡更区域名称:<input name="regionName" style="font-size:1.5rem" type="text" value="'+ regionName +'"/></li>'+--%>
					  <%--'<li>绘制颜色:<input name="color" style="font-size:1.5rem" type="text" value="'+ color +'"/></li>'+--%>
						<%--'<input name="regionId" type="hidden" value="'+ regionId +'"/>'+--%>
						<%--'<li><input type="submit" class="btn" value="提交"/><label>&nbsp;</label><input type="reset" class="btn" value="重置"/> </li></ul></form>'--%>
			<%--});--%>
		}

		/**
		 * 添加颜色
		 */
		function addHtm() {

		    window.location.href = '<%=basePath%>toSelectColorPage';
            //TODO 下面是之前在当前页面显示的弹框（废弃）
			<%--$(".layui-layer-title").text("增加信息");--%>
			<%--layer.open({--%>
			  <%--type: 1,--%>
			  <%--skin: 'layui-layer-rim', //加上边框--%>
			  <%--area: ['420px', '340px'],//宽高--%>
			  <%--content: '<form action="<%=path %>/patrolReg_add" method="post" id="addForm">'+--%>
						<%--'<ul class="updul">' +--%>
			  <%--'<li>巡更区域名称:<input name="regionName" style="font-size:1.5rem" type="text" value=""/></li>' +--%>
			  <%--'<li>颜色:<input name="color" style="font-size:1.5rem" type="text" value=""/></li>' +--%>
						<%--'<li><input type="submit" class="btn" value="提交"/><label>&nbsp;</label><input type="reset" class="btn" value="重置"/> </li></ul></form>'--%>
			<%--});--%>
		}


		$('.tablelist tbody tr:odd').addClass('odd');
	
		$(document).ready(function() {
			$(".m_berthOrder_l").attr("class","lhover");
			/**
			 * 分页
			 */
			$(".sabrosus a").each(function() {
		        $(this).click(function() {
		        	$('#page').val($(this).attr("rel"));
		        	$('#pageform').submit();
		        });  
		    });
			/**
			 * 全选操作
			 */
			$("#checkAll").click(function() {
				$('input[name="checkbox"]').attr("checked",this.checked); 
			});
		    $("input[name='checkbox']").click(function(){
				$("#checkAll").attr("checked",$("input[name='checkbox']").length == $("input[name='checkbox']:checked").length ? true : false);
		    });
		    
		     $('.son_ul').hide(); //初始ul隐藏
			$('.select_box span').hover(function(){ //鼠标移动函数
				$(this).parent().find('li').hover(function(){$(this).addClass('hover')},function(){$(this).removeClass('hover')}); //li的hover效果
				$(this).parent().hover(function(){},
					function(){
						$(this).parent().find("ul.son_ul").slideUp(); 
					}
				);
				},function(){}
			);
		    $('ul.son_ul li').click(function(){
				$(this).parents('li').find('span').html($(this).html());
				if($(this).attr("rel") == ""){
					$(this).parents('li').find('input').val("");
				}else{
					$(this).parents('li').find('input').val($(this).attr("rel"));
				}
				$(this).parents('li').find('ul').slideUp();
			});
			$('ul.son_ul li').each(function(){
				if($(this).parents('li').find('input').val() == $(this).attr("rel")){
					$(this).parents('li').find('span').html($(this).html());
				}
			});
		});



		//点击进入绘制地图界面
		var toPatrolMap = function (id) {
			//点击进入绘制地图区域
			if(id != undefined) {
				window.location.href = '<%=basePath%>firePatrolMap?id=' + id;
			}
		};

        //点击进入巡更签到点位管理页面
        var toPatroSignPortlMap = function (id) {
            //点击进入绘制地图区域
            if(id != undefined) {
                window.location.href = '<%=basePath%>patrolSignPortInfo_toManage?regionId=' + id;
            }
        };


		/**
		 * 删除
		 */
		function bulkDelete(url,ids) {
			if(ids == 0) {
				var ids = "";
				$("[name='checkbox']:checked").each(function(){
					ids += $(this).val()+",";
				});
				if(ids.length>0){
					ids=ids.substr(0,ids.length-1);
					layer.confirm('确定要删除吗？',function(index){
						window.location.href=the_host+"patrolReg_delete?ids="+ids;
					});
				}else{
					layer.alert('请至少选择一条数据！', 8, !1);
				}
			}else{
				layer.confirm('确定要删除吗？',function(index){
					window.location.href=the_host+"patrolReg_delete?ids="+ids;
				});
			}
		}


		/**
		 * 跳转到显示所有区域页面根据校区id
         */
		function showAllDraw(campusNum) {
			window.location.href = '<%=basePath%>toShowAllDrawPage?campusNum=1'
		}

</script>
</body>
</html>