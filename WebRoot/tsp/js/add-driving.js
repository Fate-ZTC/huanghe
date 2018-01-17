$(".licenseUploadBtn,#reuploadBtn").change(function(){
			if($(this).attr("id")=="licenseUploadBtn"){
				$(this).attr("name","fileDriving");
				$('#reuploadBtn').attr("name","");
			}else{
				$(this).attr("name","fileDriving");
				$('#licenseUploadBtn').attr("name","");
			}
			var $file = $(this);
	        var fileObj = $file[0];
	        var windowURL = window.URL || window.webkitURL;
	        var dataURL;
	        $(".read-picture-box").show();
	        var $img = $("#ImgPr");
	        if(fileObj && fileObj.files && fileObj.files[0]){
		        dataURL = windowURL.createObjectURL(fileObj.files[0]);
		        $img.attr('src',dataURL);
		        
		        //图片上传成功后执行操作
		        $(".license-upload").hide();
		        $(".reupload-box").show();
	        }else{
		        dataURL = $file.val();
		        var imgObj = document.getElementById("ImgPr");
		        imgObj.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
		        imgObj.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = dataURL;
          }
	})
	
	$(".carNumSelect").click(function(){
		console.log($(this).find(".car-option-right").attr("kid"));
		if($(this).find(".car-option-right").attr("kid")==''){
			$(".add-driving-box").hide();
			$(".box-num-select").show();
			var obj=$("#carNumSelectResult");
			carNumselect(obj);
		}
	})
	
	$(".carTypeSelect").click(function(){
		var carTypeSelect=new selectBottom({
		  showCont: '#carTypeSelectResult',
		  hideCont: '#carTypeSelectInput',
		  actions: [{
		    text: "小型车",
		    typeId: "id",
		    id: '1'
		  },{
		    text: "大型车",
		    typeId: "id",
		    id: '2'
		  },{
		    text: "其他",
		    typeId: "id",
		    id: '0'
		  }]
		})
	})
	
	$(".carNumColorSelect").click(function(){
		var carNumColorSelect=new selectBottom({
		  showCont: '#carNumColorSelectResult',
		  hideCont: '#carNumColorSelectInput',
		  actions: [{
		    text: "蓝色",
		    typeId: "id",
		    id: '1'
		  },{
		    text: "黄色",
		    typeId: "id",
		    id: '2'
		  },{
		    text: "黑色",
		    typeId: "id",
		    id: '3'
		  },{
		    text: "白色",
		    typeId: "id",
		    id: '4'
		  },{
		    text: "其他",
		    typeId: "id",
		    id: '0'
		  }]
		})
	})
	
	function selectBottom(parameter){
		var showCont=parameter.showCont;
		var hideCont=parameter.hideCont;
		var typeId;
		var li='';
		$.each(parameter.actions,function(i,i_item){
			li+='<li '+ i_item.typeId +'="'+ i_item.id +'">'+ i_item.text +'</li>'
			typeId=i_item.typeId;
		})
		$(".select-bottom .select-ul").html(li);
		var height=$(".select-bottom").height();
		$(".select-bottom").css({bottom:-height}).show();
		$(".select-bottom").animate({bottom:0},300);
		$("#selectMask").fadeIn(300);
		
		$(".select-ul li").unbind("click");
		$(".select-ul li").click(function(){
			$(showCont).text($(this).text());
			$(hideCont).val($(this).attr(typeId));
			$(".select-bottom").animate({bottom:-height},300);
			$("#selectMask").fadeOut(300);
		})
		
		$(".cancel-select").unbind("click");
		$(".cancel-select").click(function(){
			$(".select-bottom").animate({bottom:-height},300);
			$("#selectMask").fadeOut(300);
		})
	}
	
	
//	车牌选择
function carNumselect(selectObj){
var flag=false;
var index=0;
select(index);
function select(bb){
	if (bb<=6) {
		$(".select").removeClass("option-active")
		$(".select").eq(bb).addClass("option-active");
		start();
	};
}

// 选择车牌执行函数
function start(){
	var obj=$(".option-active");
	zhixing(obj);
}
function zhixing(aa){
	if ($(aa).hasClass("province")) {
			$(".value-letter-bg").hide();
			$(".value-sel-bg").show();
			$(".option").unbind("click")
			$(".option").click(function(){
				$(".option-active").text($(this).text());
				$(".option").unbind("click");
				pan();
				index++;
				select(index);
		})

		}else{
			$(".value-sel-bg").hide();
			$(".value-letter-bg").show();
			$(".option").unbind("click");
			$(".option").click(function(){
				$(".option-active").text($(this).text());
				$(".option").unbind("click");
				pan();
				index++;
				select(index);
			})
	}
}

$(".select").click(function(){
	$(".select").removeClass("option-active")
	$(this).addClass("option-active");
	index=$(this).index();
	if (index>2) {
		index--;
	};
	start();
	
})
del();
function pan(){
	var num=0;
	$(".select").each(function(i){
		if ($(this).text()!="") {
			num++;
		}
	})
	if (num>=7) {
		$(".complet").addClass("complet_act");
		flag=true;
		select_complet();
	}else{
		$(".complet").removeClass("complet_act");
		flag=false;
		select_complet();
	}
}
function select_complet(){
	$(".complet_act").unbind("click");
	$(".complet_act").click(function(){
		if (flag) {
		var str='';
		for (var i = 0; i < 7; i++) {
			if (i==1) {
				str+=$(".select").eq(i).text();
			}else{
				str+=$(".select").eq(i).text();
			}
		};

		$(selectObj).text(str);
		$('#carNumSelectInput').val(str);
		$(".input-box .select").text("");
		$(".box-num-select").hide();
		$(".box-vehicle").show();
		$(".box-car-select").show();
		$(".add-driving-box").show();

	};
	})
}
function del(){
	$(".del").click(function(){
		
		if ($(".option-active").text()=="") {
			$(".option-active").removeClass("option-active").prev().addClass("option-active");
			index=$(".option-active").index();
		}else{	
		if($(".option-active").prev().attr("index")==1){
			$(".value-letter-bg").hide();
			$(".value-sel-bg").show();
		}
			$(".option-active").text("").removeClass("option-active").prev().addClass("option-active");
			index=$(".option-active").index();
		}
		pan();
		start();
	})
	
}

}
			