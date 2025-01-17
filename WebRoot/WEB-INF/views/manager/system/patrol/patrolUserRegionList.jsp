<%@ page language="java" import="java.text.DateFormat,java.text.SimpleDateFormat" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Locale" %>
<%@include file="../../taglibs.jsp" %>
<%
String path = request.getContextPath();
DateFormat df = new SimpleDateFormat("yyyy年MM月dd日，EEE", Locale.CHINA);
String date ="今天是："+ df.format(new Date());
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8"/>
	<title>安防巡更|巡更记录管理</title>
	<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<link href="<%=path %>/page/css/select.css" rel="stylesheet" type="text/css" />
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
	<style type="text/css">
		/*.select3{*/
			/*width: 80px;*/
			/*height: 32px;*/
			/*border-top: solid 1px #a7b5bc;*/
			/*border-left: solid 1px #a7b5bc;*/
			/*border-right: solid 1px #ced9df;*/
			/*border-bottom: solid 1px #ced9df;*/
		/*}*/
	</style>
</head>
<body>
	<div class="place">
	    <span>位置：</span>
		    <ul class="placeul">
			    <li><a href="#">安防巡更</a></li>
			    <li><a href="<%=path %>/patrolUserRegionList">巡更记录管理</a></li>
		    </ul>
	    </div>
	<div class="rightinfo">
				<%--
					分页表单				
				--%>
				<form action="<%=path %>/patrolUserRegionList" method="post" id="searchForm">
				    <ul class="seachform">
				    	<li><label>巡更人员姓名</label><input name="username" value="${name}" type="text" class="scinput" style="width: 90px;"/></li>
				    	<li><label>巡更区域</label>
							<div class="vocation">
								<select class="select3" name="regionId" >
						        	<option value="-1">-请选择-</option>
						        	<c:forEach items="${patrolRegions}" var="p">
										<option value="${p.id}">${p.regionName}</option>
						        	</c:forEach>
						        </select>
							</div>
						</li>
				    	<li><label>是否异常</label>
					    	<div class="vocation">
								<select class="select3" name="exceptionType" >
						        	<option value="-1" <c:if test="${exceptionType!=0&&exceptionType!=1}">selected</c:if>>-请选择-</option>
									<option value="0" <c:if test="${exceptionType==0}">selected</c:if>>否</option>
									<option value="1" <c:if test="${exceptionType==1}">selected</c:if>>是</option>
						        </select>
							</div>
				    	</li>
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
							<input type="hidden" name="exceptionType" value="${exceptionType}"/>
							<input type="hidden" name="regionId" value="${regionId}"/>
						</li>
					</ul>
				</form>
				<div class="tools">
			    	<ul class="toolbar">
				    	<sec:authorize ifAnyGranted="patUserReg_delete">
				        <li onclick="bulkDelete('<%=path %>/patUserReg_delete','0');"><span><img src="<%=path %>/page/images/t03.png" /></span>批量删除</li>
				        </sec:authorize>
				    	<sec:authorize ifAnyGranted="patrolUserRegionList">
				        <%--<li onclick="ExOut('<%=path %>/paUserRegExOut','0');"><span><img src="<%=path %>/page/images/t07.png" /></span>导出</li>--%>
				        <li onclick="forWardUrl_param('<%=path %>/paUserRegExOut','0');"><span><img src="<%=path %>/page/images/t07.png" /></span>导出</li>
				        </sec:authorize>
			        </ul>
			    </div>
				<table width="100%" class="tablelist">
		            <tbody width="100%">
		            	<thead>
					    	<tr>
					        	<th class="tbt1"><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>
			            		<th>巡更人员姓名</th>
			            		<th>巡更人员账号</th>
			            		<th>开始巡更时间</th>
			            		<th>结束巡更时间</th>
			            		<th>巡更时长</th>
			            		<th>巡更区域</th>
			            		<%--<th>是否异常</th>
			            		<th>异常原因</th>--%>
								<th>异常次数</th>
			            		<th>操作</th>
					        </tr>
				        </thead>
		            	<c:forEach items="${patrolUserRegions.list}" var="m">
					        <tr>
					        <td><input type="checkbox" name="checkbox" value="${m.id}" id="checkbox_${m.id}" class="checkItem"/></td>
					        <td>${m.username}</td>
					        <td>${m.jobNum}</td>
					        <td>${m.formatStartTime}</td>
					        <td>${m.formatEndTime}</td>
					        <td>${m.checkDuration}</td>
					        <td>${m.regionName}</td>
					      <%--  <td>
					        	<c:if test="${empty m.patrolException}">否</c:if>
					        	<c:if test="${!empty m.patrolException}">是</c:if>
					        </td>
					        <td>
					        	<c:if test="${empty m.patrolException}">无</c:if>
					        	<c:if test="${!empty m.patrolException}">${m.patrolException.exceptionName}</c:if>
					        </td>--%>
<!-- 					        <td>删除</td> -->
								<td>
									<a  style="color:#16c2ea;" href="javascript:void(0);" onclick="upHtm(${m.id},'${m.jobNum}','${m.formatStartTime}','${m.formatEndTime}');">${m.abnormalCount}</a>
								</td>
								<td>
									<a   href="<%=path %>/patUserReg_delete?ids=${m.id}">删除</a>
								</td>

					        </tr> 
				        </c:forEach>

			        </tbody>
			    </table>
			   	<!-- 分页开始 -->
			    <div class="pagin">
			    	<div class="message">共<i class="blue">${patrolUserRegions.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${patrolUserRegions.currentPage}/${patrolUserRegions.totalPage }&nbsp;</i>页</div>
			        <ul class="paginList">
						<c:forEach items="${patrolUserRegions.fristPage}" var="f">
							<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
						</c:forEach>
						<li class="paginItem current"><a href="javascript:void(0);">${patrolUserRegions.currentPage}</a></li>
						<c:forEach items="${patrolUserRegions.laPage}" var="l">
							<li class="paginItem"><a href="javascript:void(0);" rel="${l}">${l}</a></li>
						</c:forEach>
			        </ul>
			    </div>
			    <!-- 分页结束 -->
</div>
<script type="text/javascript">
	$(function(){

        showData();
			//选择框
			$(".select3").uedSelect({
				width : 80
			});

		});
	var the_host = "<%=path%>/";
	function upHtm(id,jobNum,formatStartTime,formatEndTime) {
        layer.open({
            type:2,
            title:'异常详情',
            area: ['772px', '470px'],
            content:"<%=basePath%>toSelectPatrolExceptionInfosByid?id=" + id+"&jobNum=" + jobNum +"&formatStartTime="+formatStartTime+"&formatEndTime="+formatEndTime
   	 	})
		<%--window.location.href = "<%=basePath%>toSelectPatrolExceptionInfosByid?id=" + id+"&jobNum=" + jobNum +"&formatStartTime="+formatStartTime+"&formatEndTime="+formatEndTime;--%>
	}



	var the_host = "<%=path%>/";
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
		$(document).ready(function(){
			$(".m_berthOrder_l").attr("class","lhover");
			/**
			 * 分页
			 */
			$(".sabrosus a").each(function(){  
		        $(this).click(function(){ 
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

		/**
		 * 删除
		 */
		function bulkDelete(url,ids){
			if(ids == 0){
				var ids = "";
				$("[name='checkbox']:checked").each(function(){
					ids += $(this).val()+",";
				});
				if(ids.length>0){
					ids=ids.substr(0,ids.length-1);
					layer.confirm('确定要删除吗？',function(index){
						window.location.href=the_host+"patUserReg_delete?ids="+ids;
					});
				}else{
					layer.alert('请至少选择一条数据！', 8, !1);
				}
			}else{
				layer.confirm('确定要删除吗？',function(index){
					window.location.href=the_host+"patUserReg_delete?ids="+ids;
				});
			}
		}

		//这里进行数据回显操作
		var showData = function () {
		    //选中异常
			var exceptionType = $("input[name='exceptionType']").val();
            if(exceptionType != undefined) {
                if(exceptionType == 1) {
                    $(".select3").find("option[value='1']").prop("selected","selected");
                }
                if(exceptionType == 0) {
                    $(".select3").find("option[value='0']").prop("selected","selected");
                }
            }

            var regionId = $("input[name='regionId']").val();
            if(regionId != undefined) {
                $(".select3").find("option[value='"+regionId+"']").prop("selected","selected");
			}

        };

		/**
		 * 导出
		 */
		function forWardUrl_param(url){
			var param = "";
            var username = $("input[name='username']").val();//姓名
            var startTime =  $("input[name='startTime']").val();//开始时间
            var endTime =  $("input[name='endTime']").val();//结束时间
			var regionId = $("input[name='regionId']").val();
            var exceptionType = $("input[name='exceptionType']").val();
            var ids = "";
            $("[name='checkbox']:not(:disabled):checked").each(function(){
                ids += $(this).val()+",";
            });
            if(ids.length>0){
                ids=ids.substr(0,ids.length-1);
            }

            param += "?now=" + new Date().getTime();
            if(username != undefined && username != null && username != "") {
                param += "&username=" + username;
			}
            if(regionId != undefined && regionId != null && regionId != "") {
                param += "&regionId=" + regionId;
            }
            if(exceptionType != undefined && exceptionType != null && exceptionType != "") {
                param += "&exceptionType=" + exceptionType;
            }
            if(startTime != undefined && startTime != null && startTime != "") {
                param += "&startTime=" + startTime;
            }
            if(endTime != undefined && endTime != null && endTime != "") {
                param += "&endTime=" + endTime;
            }
            param += "&ids=" + ids;

			window.location.href= url + param;
		}


</script>
</body>
</html>