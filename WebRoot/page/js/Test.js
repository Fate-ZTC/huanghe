$(function(){
	//APPVersioningAdd.html  and APPVersioningEdit.html
	$('#app-form').html5Validate(function() {
		this.submit();	
	},{
		validate:function(){
			if($('#pic-url').val() == '' || $('#pic-url').val() == '未选择任何文件'){
				$("#pic-url").testRemind("您尚未选择app版本文件");
            	return false;  
			}
			return true;
		}
	});
	//BackMusicAdd.html
	$('#music-form').html5Validate(function() {
		this.submit();	
	},{
		validate:function(){
			if($('#pic-urlb').val() == '' || $('#pic-urlb').val() == '未选择任何文件'){
				$("#pic-urlb").testRemind("您尚未选择音乐文件");
            	return false;  
			}
			return true;
		}
	});
	//ImportantnewsAdd.html and ImportantnewsEdit.html
	$('#impot-form').html5Validate(function() {
		this.submit();	
	},{
		validate:function(){
			if($('#pic-url').val() == '' || $('#pic-url').val() == '未选择任何文件'){
				$("#pic-url").testRemind("您尚未选择图片");
            	return false;  
			}
			return true;
		}
	});
	//JBuidingManagementAdd.html and JBuidingManagementEdit.html
	$('#building-form').html5Validate(function() {
		this.submit();	
	});
	//JBuidingManagementUpload.html
	$('#upload-form').html5Validate(function() {
		this.submit();	
	},{
		validate:function(){
			if($('#pic-urla').val() == '' || $('#pic-urla').val() == '未选择任何文件'){
				$("#pic-urla").testRemind("您尚未选择任何文件");
            	return false;  
			}
			return true;
		}
	});
	//JInstitutonSettingAdd.html and 
	$('#institution-form').html5Validate(function() {
		this.submit();	
	})
	//JPannormicPositionManagementAdd.html  and  JPannormicPositionManagementEdit.html
	$('#pannormic-form').html5Validate(function() {
		this.submit();	
	})
	//JSmartPublicServiceAdd.html  and  JSmartPublicServiceEdit.html
	$('#public-form').html5Validate(function() {
		this.submit();	
	})
	//XWelcomeManagementAdd.html  and  XWelcomeManagementEdit.html
	$('#welcome-form').html5Validate(function() {
		this.submit();	
	})
	//YCinteracitonAdd.html  and YCinteracitonEdit.html
	$('#cinteraction-form').html5Validate(function() {
		this.submit();	
	},{
		validate:function(){
			if($('#pic-url').val() == '' || $('#pic-url').val() == '未选择任何文件'){
				$("#pic-url").testRemind("您尚未选择任何文件");
            	return false;  
			}
			return true;
		}
	});
})