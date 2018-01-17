function selectBottom(parameter){
	var title=parameter.title?parameter.title:'选择操作';
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
		parameter.callFunction($(this).text());
		$(".select-bottom").animate({bottom:-height},300);
		$("#selectMask").fadeOut(300);
	})
	
	$(".cancel-select,#selectMask").unbind("click");
	$(".cancel-select,#selectMask").click(function(){
		$(".select-bottom").animate({bottom:-height},300);
		$("#selectMask").fadeOut(300);
	})
	$(".select-title").text(title);
}

//选择时长
$(".longTimeSelect").click(function(){
	var longTimeSelect=new selectBottom({
	  title: '选择办理时长',
	  showCont: '#longTimeSelectResult',
	  hideCont: '#longTimeSelectInput',
	  callFunction: function(p){
		pay(p)
	  },
	  actions: [{
	    text: "1",
	    typeId: "id",
	    id: '1'
	  },{
	    text: "2",
	    typeId: "id",
	    id: '2'
	  },{
	    text: "3",
	    typeId: "id",
	    id: '3'
	  },{
	    text: "4",
	    typeId: "id",
	    id: '4'
	  },{
	    text: "5",
	    typeId: "id",
	    id: '5'
	  },{
	    text: "6",
	    typeId: "id",
	    id: '6'
	  },{
	    text: "7",
	    typeId: "id",
	    id: '7'
	  },{
	    text: "8",
	    typeId: "id",
	    id: '8'
	  },{
	    text: "9",
	    typeId: "id",
	    id: '9'
	  },{
	    text: "10",
	    typeId: "id",
	    id: '10'
	  },{
	    text: "11",
	    typeId: "id",
	    id: '11'
	  },{
	    text: "12",
	    typeId: "id",
	    id: '12'
	  }]
	})
})
