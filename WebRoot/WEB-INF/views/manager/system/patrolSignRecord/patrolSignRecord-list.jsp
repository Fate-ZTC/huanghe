<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    DateFormat df = new SimpleDateFormat("yyyy年MM月dd日，EEE", Locale.CHINA);
    String date ="今天是："+ df.format(new Date());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>巡更异常记录-列表</title>
<link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
<script type="text/javascript" src="<%=path %>/page/js/common.js"></script>
<script type="text/javascript" src="<%=path %>/page/layer/layer.js"></script>
<script src="<%=path %>/page/chart/laydate/laydate.js"></script>
<link href="<%=path %>/page/css/select.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path %>/page/js/select-ui.min.js"></script>
<script type="text/javascript">
	var method = '${method }';
	if(method=='addSuccess'){
		layer.msg('添加成功', {icon: 1});
	}else if(method=='editSuccess'){
		layer.msg('编辑成功', {icon: 1});
	}else if(method=='deleteSuccess'){
		layer.msg('删除成功', {icon: 1});
	}else{
	}
</script>
<style>
.excul li{
	font-size: 1.5rem;
    margin-left: 4rem;
    margin-top: 3rem;
}
.scbtn {
    width: 60px;
}
.scinput {
    width: 80px;
    }
.select3{
    width: 80px;
    height: 32px;
    border-top: solid 1px #a7b5bc;
    border-left: solid 1px #a7b5bc;
    border-right: solid 1px #ced9df;
    border-bottom: solid 1px #ced9df;
}
</style>
</head>
<body>

	<div class="place">
    <span>位置：</span>
    <ul class="placeul">
    <li><a href="#">安防巡更</a></li>
    <li><a href="<%=path %>/patrolSignRecord_list">巡更异常记录</a></li>
    </ul>
    </div>
    
    <div class="rightinfo">
    <form action="<%=path %>/patrolSignRecord_list" method="post" id="searchForm">
    <ul class="seachform">
    	<li><label>姓名:</label><input name="name" value="${name}" type="text" class="scinput" /></li>
		<li><label>账号:</label><input name="jobNum" value="${jobNum}" type="text" class="scinput" /></li>
		<li><label>巡更结果:</label>
    		<div class="vocation">
				<select class="select3" id="select" name="patrol" style="opacity: 1;" >
		        	<option value="-1" <c:if test="${patrol!=2&&patrol!=1}">selected = "selected"</c:if>>-请选择-</option>
                    <option value="2" <c:if test="${patrol==2}">selected</c:if>>异常</option>
                    <option value="1" <c:if test="${patrol==1}">selected</c:if>>正常</option>
                    <%--<c:if test="${patrolStatus == 0}">--%>
                        <%--<option value="0">设备异常</option>--%>
                    <%--</c:if>--%>
                    <%--<c:if test="${patrolStatus == 1}">--%>
                        <%--<option value="1">设备正常</option>--%>
                    <%--</c:if>--%>
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
		</li>
	</ul>
	</form>
    <div class="tools">
    
    	<ul class="toolbar">
    	<sec:authorize ifAnyGranted="patrolSignRecord_delete">
        <li onclick="bulkDelete('<%=path %>/patrolSignRecord_delete','0');"><span><img src="<%=path %>/page/images/t03.png" /></span>批量删除</li>
        </sec:authorize>
        <%--<sec:authorize ifAnyGranted="firePatrolInfo_excelOut">--%>
        <%--<li onclick="forWardUrl('<%=path %>/firePatrolInfo_excelOut','0');"><span><img src="<%=path %>/page/images/t04.png" /></span>导出</li>--%>
        <%--</sec:authorize>--%>
        <sec:authorize ifAnyGranted="patrolSignRecord_excelOut">
        <li onclick="forWardUrlParam('<%=path %>/patrolSignRecord_excelOut','0');"><span><img src="<%=path %>/page/images/t07.png" /></span>导出</li>
        </sec:authorize>
        </ul>
    
    </div>
    
    
    <table class="tablelist">
    	<thead>
    	<tr>
        <th width="40px"><input type="checkbox" name="checkAll" value="checkbox" id="checkAll" class="checkAll"/></th>
        <th width="100px">姓名</th>
        <th width="100px">工号</th>
        <th width="100px">巡更时间</th>
        <th width="100px">巡更结果</th>
        <th width="50px">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${patrolSignRecordPage.list}" var="d">
        <tr>
        <td><input type="checkbox" name="checkbox" value="${d.recordId}" id="checkbox_${d.recordId}" class="checkItem"/></td>
        <td>${d.username}</td>
        <td>${d.jobNum}</td>
        <td><fmt:formatDate value="${d.signTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        <td>${d.signType==1?"正常":"异常"}</td>
        <td>
        	<sec:authorize ifAnyGranted="patrolSignRecord_delete">
        	<a href="javascript:void(0);" onclick="bulkDelete('<%=path %>/patrolSignRecord_delete','${d.recordId}');" class="tablelink"> 删除</a>
        	</sec:authorize>
        </td>
        </tr>
        </c:forEach>
        </tbody>
    </table>

   	<!-- 分页开始 -->
    <div class="pagin">
    	<div class="message">共<i class="blue">${patrolSignRecordPage.allRow}</i>条记录，当前显示第&nbsp;<i class="blue"> ${patrolSignRecordPage.currentPage}/${patrolSignRecordPage.totalPage }&nbsp;</i>页</div>
        <ul class="paginList">
			<c:forEach items="${patrolSignRecordPage.fristPage}" var="f">
				<li class="paginItem"><a href="javascript:void(0);" rel="${f}">${f}</a></li>
			</c:forEach>
			<li class="paginItem current"><a href="javascript:void(0);">${patrolSignRecordPage.currentPage}</a></li>
			<c:forEach items="${patrolSignRecordPage.laPage}" var="l">
				<li class="paginItem"><a href="javascript:void(0);" rel="${l}">${l}</a></li>
			</c:forEach>
        </ul>
    </div>
    <!-- 分页结束 -->
    </div>
    <script type="text/javascript">
	$('.tablelist tbody tr:odd').addClass('odd');
	var start_time = {
			  elem: '#start-time',
			  format: 'YYYY-MM-DD hh:mm:ss',
			  max: '2099-06-16',
			  istime: true,
			  istoday: false,
			  choose: function(datas) {
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
			  choose: function(datas) {
			    start_time.max = datas;
			    $("#end-time").val(datas);
			  }
			};
			laydate(end_time);
	var the_host = "<%=path%>/";


	function excepDeta(name,exceptionTypes,id) {
	$.get(the_host+'showExcptions',{'exceptionTypes':exceptionTypes,'id':id},function(data){
 		console.log(data);
        var content = '';
 		if(data.exceptions === data.description) {
            content = '<ul class="excul"><li>'+data.exceptions+'</li>';
        }else {
            content = '<ul class="excul"><li>'+data.exceptions+'</li>'+
                '<li>'+data.description+'</li></ul>';
        }

   		if(data.status=='true') {
			$(".layui-layer-title").text(name + "-异常描述");
			layer.open({
			  type: 1,
			  skin: 'layui-layer-rim', //加上边框
			  area: ['420px', '240px'], //宽高
			  content: content
			});
   		}
   	},'json');
}
	/**
		 * 删除
		 */
		function bulkDelete(url,ids) {
			if(ids == 0){
				var ids = "";
				$("[name='checkbox']:checked").each(function() {
					ids += $(this).val()+",";
				});
				if(ids.length>0){
					ids=ids.substr(0,ids.length-1);
					layer.confirm('确定要删除吗？',function(index) {
						window.location.href=the_host+"patrolSignRecord_delete?ids="+ids;
					});
				}else{
					layer.alert('请至少选择一条数据！', 8, !1);
				}
			}else{
				layer.confirm('确定要删除吗？',function(index) {
					window.location.href=the_host+"patrolSignRecord_delete?ids="+ids;
				});
			}
		}

		//这里进行
        var forWardUrlParam = function (url) {
            //进行组装参数
            var param = '';
            var name = $("input[name='name']").val();//设备名称
            var jobNum = $("input[name='jobNum']").val();//姓名
            var startTime =  $("input[name='startTime']").val();//开始时间
            var endTime =  $("input[name='endTime']").val();//结束时间
            var ids = "";
            $("[name='checkbox']:not(:disabled):checked").each(function(){
                ids += $(this).val()+",";
            });
            if(ids.length>0){
                ids=ids.substr(0,ids.length-1);
            }
            //获取选中的值
            var patrol = $("#select option:selected").val();
            var now = new Date().getTime();
            param += "?now=" + now;
            if(name != undefined && name != "") {
                param += "&name=" + name;
            }
            if(jobNum != undefined && jobNum != "") {
                param += "&jobNum=" + jobNum;
            }
            if(startTime != undefined && startTime != "") {
                param += "&startTime=" + startTime;
            }
            if(endTime != undefined && endTime != "") {
                param += "&endTime=" + endTime;
            }
            if(patrol != undefined && patrol != "") {
                param += "&patrol=" + patrol;
            }
            param += "&ids=" + ids;
            window.location.href = url + param;



        };

    /**
     * 设置选中
     */
    // var selectStatus = function () {
    //         //进行默认选中
    //         var status = $("input[name='patrolStatus']").val();
    //         if(status != undefined) {
    //             if(status == 1) {
    //                 $(".select3").find("option[value='1']").prop("selected","selected");
    //             }
    //             if(status == 0) {
    //                 $(".select3").find("option[value='0']").prop("selected","selected");
    //             }
    //         }
    //     }
	</script>

</body>

</html>
