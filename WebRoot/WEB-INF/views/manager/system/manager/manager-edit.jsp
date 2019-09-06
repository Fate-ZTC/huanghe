<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="../../taglibs.jsp" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>系统用户-编辑</title>
    <link href="<%=path %>/page/css/style.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="<%=path %>/page/js/jquery.js"></script>
    <link rel="stylesheet" href="<%=path %>/page/validator/jquery.validator.css" />
    <script type="text/javascript" src="<%=path %>/page/validator/jquery.validator.js"></script>
    <script type="text/javascript" src="<%=path %>/page/validator/local/zh-CN.js"></script>
    <script type="text/javascript" src="<%=path %>/page/laydate/laydate.js"></script>
    <link rel="stylesheet" href="<%=path %>/page/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css"/>
    <script type="text/javascript" src="<%=path %>/page/ztree/jquery.ztree.all-3.5.min.js"></script>
    <style type="text/css">
        .menu{
            z-index: 2;
            _top: 6px;
            cursor: pointer;
            height: 32px;
            border: 1px solid #cccccc;
        }
        .level2 span,.level1 span,.level0 span{
            display: inline;
        }
    </style>
    <script type="text/javascript">
        $(function(){
            //选择框
            laydate.skin('molv');
        });
    </script>
</head>

<body>

<div class="place">
    <span>位置：</span>
    <ul class="placeul">
        <li><a href="#">系统管理</a></li>
        <li><a href="<%=path %>/manager_list">系统用户</a></li>
        <li><a href="#">编辑</a></li>
    </ul>
</div>
<div class="formbody">
    <div class="formtitle"><span>编辑系统用户</span></div>
    <form action="<%=path %>/manager_edit" method="post" id="addForm" data-validator-option="{stopOnError:true, timely:1 ,theme:'yellow_right'}">
        <input type="hidden" name="method" value="edit" />
        <input type="hidden" name="userId" value="${manager.userId}" />
        <input type="hidden" name="enablRegionIds" id="enablRegionIds"/>
        <ul class="forminfo">
            <li><label>用户名</label><input name="username" type="text" value="${manager.username}" class="dfinput" disabled="disabled"/></li>
            <li><label>密码</label><input name="password" type="text" class="dfinput"/></li>
            <li><label>姓名</label><input name="realname" type="text" class="dfinput" value="${manager.realname}" data-rule="姓名:length[~30]"/></li>
            <li><label>QQ</label><input name="qq" type="text" class="dfinput" value="${manager.qq}" data-rule="QQ:qq;length[~20]"/></li>
            <li><label>部门<b>*</b></label>
                <select class="menu" name="department.departmentid" data-rule="部门:required;">
                    <option value="">-请选择-</option>
                    <c:forEach items="${departmentList}" var="d">
                        <c:if test="${manager.department.departmentid == d.departmentid}">
                            <option value="${d.departmentid}" selected="selected">${d.name}</option>
                        </c:if>
                        <c:if test="${manager.department.departmentid != d.departmentid}">
                            <option value="${d.departmentid }" >${d.name}</option>
                        </c:if>
                    </c:forEach>
                </select>
            </li>
            <li><label>角色<b>*</b></label>
                <c:forEach items="${roleList}" var="r">

                    <input name="roles" data-rule="角色:required;" style="width: 15px;height:13px" type="checkbox" <c:forEach items="${manager.managerRoles}" var="mr"><c:if test="${mr.role.roleId==r.roleId}">checked="checked"</c:if></c:forEach> class="dfinput" value="${r.roleId}"/>${r.name}
                </c:forEach>
                <%--    	<select class="menu" name="role.roleId" data-rule="角色:required;">--%>
                <%--        	<option value="">-请选择-</option>--%>
                <%--        	<c:forEach items="${roleList}" var="r">--%>
                <%--        		<c:if test="${manager.role.roleId == r.roleId}">--%>
                <%--					<option value="${r.roleId }" selected="selected">${r.name}</option>--%>
                <%--				</c:if>--%>
                <%--				<c:if test="${manager.role.roleId != r.roleId}">--%>
                <%--					<option value="${r.roleId }" >${r.name}</option>--%>
                <%--				</c:if>--%>
                <%--				--%>
                <%--        	</c:forEach>--%>
                <%--        </select>--%>
            </li>
            <li><label>添加时间<b>*</b></label><input name="" type="text" style="height: 34px;"
                                                  value="<fmt:formatDate value="${manager.registerTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
                                                  data-rule="添加时间:required;" class="laydate-icon" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})"/></li>
            <li><label>&nbsp;</label><input name="" type="button" class="btn" value="确认编辑" onclick="_submit()"/></li>
        </ul>
    </form>

</div>
<script type="text/javascript">
    var host ='<%=path%>';
    var _submit = function(){
        var checkOne = false;                    //判断是否被选择条件
        var chboxVal = [];                       //存入被选中项的值
        var checkBox = $('input[name = roles]'); //获得得到所的复选框

        for (var i = 0; i < checkBox.length; i++) {

            //如果有1个被选中时（jquery1.6以上还可以用if(checkBox[i].prop('checked')) 去判断checkbox是否被选中）
            if (checkBox[i].checked) {
                checkOne = true;
                chboxVal.push(checkBox[i].value)//将被选择的值追加到
            };
        };

        if (checkOne) {
            $('#addForm').submit();
        } else {
            alert("对不起：至少要选择一项角色哦!");
        };
    }
</script>
</body>

</html>
