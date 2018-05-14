$(function(){
	/**
	 * 分页
	 */
	$(".paginList a").each(function(){  
        $(this).click(function(){ 
        	if(typeof($(this).attr("rel")) != "undefined"){
        		$('#page').val($(this).attr("rel"));
        		$('#searchForm').submit();
        	}
        });  
    });
	/**
	 * 全选操作
	 */
	$("#checkAll").click(function() {
		$('input[name="checkbox"]:not(:disabled)').prop("checked",this.checked); 
	});
    $("input[name='checkbox']").click(function(){
		$("#checkAll").prop("checked",$("input[name='checkbox']:not(:disabled)").length == $("input[name='checkbox']:not(:disabled):checked").length ? true : false);
    });
});
/**
 * 删除
 */
function bulkDelete(url,ids){
	if(ids == 0){
		ids = "";
		$("[name='checkbox']:not(:disabled):checked").each(function(){
			ids += $(this).val()+",";
		});
		if(ids.length>0){
			ids=ids.substr(0,ids.length-1);
			layer.confirm('确定要删除吗？',function(index){
				if(url.indexOf("?") > 0){
					window.location.href=url+"&ids="+ids;
				}else{
					window.location.href=url+"?ids="+ids;
				}
			});
		}else{
			layer.alert('请至少选择一条数据！', 8, !1);
		}
	}else{
		layer.confirm('确定要删除吗？',function(index){
			if(url.indexOf("?") > 0){
				window.location.href=url+"&ids="+ids;
			}else{
				window.location.href=url+"?ids="+ids;
			}
			
		});
	}
}
/**
 * 跳转
 * @param {Object} url
 */
function forWardUrl(url) {
	window.location.href=url;
}
/**
 * 排序
 */
var sortTable = function(column){
	var sort = $("#sort").val();
	var sortColumn = $("#sortColumn").val();
	if(column == sortColumn){
		if(sort == 'desc'){
			$("#sort").val("asc");
		}else{
			$("#sort").val("");
			$("#sortColumn").val("");
		}
	}else{
		$("#sortColumn").val(column);
		$("#sort").val("desc");
	}
	$("#searchForm").submit();
}