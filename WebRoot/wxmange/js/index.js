$(".bottom-list").first().addClass("bottom-list1-act");
lode();
function lode(){
	var right = $("#right").val();
	$(".time-select-box").hide();
	$(".earnings-box").hide();
	$(".in-out-box").hide();
	if(right==7 || right==1 || right==3 || right==5){
		$(".operation-analysis-box").show();	
		$(".none-box").hide();	
	}else{
		$(".operation-analysis-box").hide();	
		$(".none-box").show();	
	}
}
$(".bottom-list").click(function(){
	var right = $("#right").val();
	$(".bottom-list").removeClass("bottom-list1-act").removeClass("bottom-list2-act").removeClass("bottom-list3-act");
	var index=$(this).index();
	if (index==0) {
		$(this).addClass("bottom-list1-act");
		$(".time-select-box").hide();
		$(".earnings-box").hide();
		$(".in-out-box").hide();
		if(right==7 || right==1 || right==3 || right==5){
			$(".operation-analysis-box").show();	
			$(".none-box").hide();	
		}else{
			$(".operation-analysis-box").hide();	
			$(".none-box").show();	
		}
	}else if(index==1){
		$(this).addClass("bottom-list2-act");
		$(".time-select-box").hide();
		$(".operation-analysis-box").hide();
		$(".in-out-box").hide();
		if(right==2 || right==3 || right==6 || right==7){
			$(".earnings-box").show();
			$(".none-box").hide();
		}else{
			$(".earnings-box").hide();
			$(".none-box").show();
		}
	}else{
		$(this).addClass("bottom-list3-act");
		$(".time-select-box").hide();
		$(".operation-analysis-box").hide();
		$(".earnings-box").hide();
		if(right==4 || right==5 || right==6 || right==7){
			$(".in-out-box").show();
			$(".none-box").hide();	
		}else{
			$(".in-out-box").hide();
			$(".none-box").show();	
		}
	}
})

// 月日切换
$(".day-month").click(function(){
	$(this).addClass("day-month-act").siblings().removeClass("day-month-act");
})

// 刷新页面
$(".operation-refresh-btn").click(function(){
	window.location.reload();
})

$(".content-refresh-btn").click(function(){
	window.location.reload();
})

// 选择时间
$(".time-btn").click(function(){
	$(".operation-analysis-box").hide();
	$(".earnings-box").hide();
	$(".in-out-box").hide();
	$(".time-select-box").show();
})

//var calendardatetime = new LCalendar();
//    calendardatetime.init({
//        'trigger': '#demo2',
//        'type': 'datetime'
//    });
//    var calendardatetime = new LCalendar();
//    calendardatetime.init({
//        'trigger': '#demo3',
//        'type': 'datetime'
//    });


var calendardatetime = new LCalendar();
    calendardatetime.init({
        'trigger': '#picktime-s1',
        'type': 'datetime'
    });
    var calendardatetime = new LCalendar();
    calendardatetime.init({
        'trigger': '#picktime-e1',
        'type': 'datetime'
    });

var calendardatetime = new LCalendar();
    calendardatetime.init({
        'trigger': '#picktime-s2',
        'type': 'datetime'
    });
    var calendardatetime = new LCalendar();
    calendardatetime.init({
        'trigger': '#picktime-e2',
        'type': 'datetime'
    });

var calendardatetime = new LCalendar();
    calendardatetime.init({
        'trigger': '#picktime-s3',
        'type': 'datetime'
    });
    var calendardatetime = new LCalendar();
    calendardatetime.init({
        'trigger': '#picktime-e3',
        'type': 'datetime'
    });


// 单列滚动选择
$('#showPicker1').on('click', function () {
	    layer.open({type: 2});
		var path = $('#path').val();
		var token = $('#token').val();
		$.post(path+'carparkAllQuery',{'token':token},function(data){
			if(data.errorCode==300000){
				var arrayObj = new Array();
				var str1 = {"label": '全部', "value":0,"id":'1'};
			    arrayObj.push(str1);
				for(var o in data.data){  
			       var str1 = {"label": data.data[o].parkingName, "value":parseInt(o)+1,"id":data.data[o].parkingCode};
			       arrayObj.push(str1);
				}
				weui.picker(arrayObj, {
		            onConfirm: function (result) {
		                $("#carpark1").html($(".weui-picker__item").eq(result).text());
		                $("#carpark1").attr('carparkcode',$(".weui-picker__item").eq(result).attr("id"));
		                $(".day-month-act").each(function(){
		                	if($(this).hasClass('dayioc')){
		                		dayIncomeQuery();
		                	}
		                	if($(this).hasClass('monthioc')){
		                		if($("#picktime-s2").val()=='' || $("#picktime-e2").val()==''){
									return;
								}
		                		var parkingName = $("#carpark1").html();
								var parkingCode = $("#carpark1").attr('carparkcode');
								var strTime = $("#picktime-s2").val();
								var endTime = $("#picktime-e2").val();
								$(".start-time2").text(strTime);
								$(".end-time2").text('~'+endTime);
								parkIncomeQuery(strTime,endTime,parkingCode,parkingName);
		                	}
		                });
		            }
		        });
			}else{
				window.location.href=path+'managetoLogin';
			}
			layer.closeAll();
		},'json');
        
    });
    $('#showPicker2').on('click', function () {
       layer.open({type: 2});
       var path = $('#path').val();
	   var token = $('#token').val();
       $.post(path+'carparkAllQuery',{'token':token},function(data){
			if(data.errorCode==300000){
				var arrayObj = new Array();
				var str1 = {"label": '全部', "value":0,"id":'1'};
			    arrayObj.push(str1);
				for(var o in data.data){  
			       var str1 = {"label": data.data[o].parkingName, "value":parseInt(o)+1,"id":data.data[o].parkingCode};
			       arrayObj.push(str1);
				}
				weui.picker(arrayObj, {
		            onConfirm: function (result) {
		                $("#carpark2").html($(".weui-picker__item").eq(result).text());
		                $("#carpark2").attr('carparkcode',$(".weui-picker__item").eq(result).attr("id"));
		                $(".day-month-act").each(function(){
		                	if($(this).hasClass('dayflow')){
		                		dayFlowQuery();
		                	}
		                	if($(this).hasClass('monthflow')){
		                		if($("#picktime-s3").val()=='' || $("#picktime-e3").val()==''){
									return;
								}
		                		var parkingName = $("#carpark2").html();
								var parkingCode = $("#carpark2").attr('carparkcode');
								var strTime = $("#picktime-s3").val();
								var endTime = $("#picktime-e3").val();
								$(".start-time3").text(strTime);
								$(".end-time3").text('~'+endTime);
								flowQuery(strTime,endTime,parkingCode,parkingName);
		                	}
		                });
		            }
		        });
			}else{
				 window.location.href=path+'managetoLogin';
			}
			layer.closeAll();
		},'json');
    });


//车位使用率分析 统计图
function space_occupancy(beginTime,endTime){
	layer.open({type: 2});
	var path = $('#path').val();
	var token = $('#token').val();
	$.post(path+'occupyPreQuery',{'token':token,'queryType':2,'beginTime':beginTime,'endTime':endTime},function(data){
		if(data.errorCode==300000){
			$('.occupancy-right').html("");
			$('.occupancy-left').html("");
			$.each(data.data,function(i,item){
				var address = "";
				var flog=true;
				var parkingName=item.parkingName+"";
				if (parkingName.length>8) {
					address=parkingName.slice(0,7)+"...";
					flog=false;
				}else{
					address=parkingName;
				}
				if (flog) {
					var app_address='<li style="line-height:0.3rem;">'+address+''+'</li>';
				}else{
					var app_address='<li style="line-height:0.15rem;">'+address+''+'</li>';
				}
				
				$(app_address).appendTo($('.occupancy-left'));
				var n1=(item.usage*100).toFixed(2);
				var n2=70;
				if (flog) {
					if (n1*n2/100>70) {
						var app_num='<div><li style="width:70%"></li><span>'+n1+'%'+'</span></div>';
					}else{
						var app_num='<div><li style="width:'+n1*n2/100+'%"></li><span>'+n1+'%'+'</span></div>';
					}
					//var app_num='<div><li style="width:'+n1*n2/100+'%"></li><span>'+n1+'%'+'</span></div>';
				}else{
					var app_num='<div><li style="width:'+n1*n2/100+'%"></li><span>'+n1+'%'+'</span></div>';
				}
				
				$(app_num).appendTo($('.occupancy-right'));
			});
		}else{
			window.location.href=path+'managetoLogin';
		}
		$('.occupancy-right').find('li').last().css({'margin-bottom':0});
		layer.closeAll(); 
	},'json');
}

// 车位周转率分析 统计图
function turnover(beginTime,endTime){
	layer.open({type: 2});
	var path = $('#path').val();
	var token = $('#token').val();
	$.post(path+'usagePreQuery',{'token':token,'queryType':2,'beginTime':beginTime,'endTime':endTime},function(data){
		if(data.errorCode==300000){
			console.debug(data);
			$('.turnover-left').html("");
			$('.turnover-right').html("");
			$.each(data.data,function(i,item){
				var address = "";
				var flog=true;
				var parkingName=item.parkingName+"";
				if (parkingName.length>8) {
					address=parkingName.slice(0,7)+"...";
					flog=false;
				}else{
					address=parkingName;
				}
				if (flog) {
					var app_address='<li style="line-height:0.3rem;">'+address+''+'</li>';
				}else{
					var app_address='<li style="line-height:0.15rem;">'+address+''+'</li>';
				}
				
				$(app_address).appendTo($('.turnover-left'));
				var n1=(item.usage*100).toFixed(2);
				var n2=70;
				if (flog) {
					if (n1*n2/100>70) {
						var app_num='<div><li style="width:70%"></li><span>'+n1+'%'+'</span></div>';
					}else{
						var app_num='<div><li style="width:'+n1*n2/100+'%"></li><span>'+n1+'%'+'</span></div>';
					}
				}else{
					var app_num='<div><li style="width:'+n1*n2/100+'%"></li><span>'+n1+'%'+'</span></div>';
				}
				
				$(app_num).appendTo($('.turnover-right'));
			});
		}else{
			window.location.href=path+'managetoLogin';s
		}
		$('.turnover-right').find('li').last().css({'margin-bottom':0});
		layer.closeAll(); 
	},'json');
}
function dayFlowQuery(){
	var parkingName = $("#carpark2").html();
	var parkingCode = $("#carpark2").attr('carparkcode');
	var mydate = new Date();
	var year = mydate.getFullYear();
	var month = (mydate.getMonth()+1);
	var day = mydate.getDate();
	if(day<10){
		day = "0"+day;
	}
	if(month<10){
		month = "0"+month;
	}
	var beginTime = year+"-"+month+"-"+day+" "+ "00:00:00";
	var endTime = year+"-"+month+"-"+day+" "+ "23:59:59";
	flowQuery(beginTime,endTime,parkingCode,parkingName);
	$(".start-time3").text(month+"月"+day+"日");
	$(".end-time3").text("");
}

function monthFlowQuery(){
	var parkingName = $("#carpark2").html();
	var parkingCode = $("#carpark2").attr('carparkcode');
	var date_ = new Date();  
	var year = date_.getFullYear();
	var month = date_.getMonth() + 1;  
	if(month<10){
		month = "0"+month;
	}
	var firstdate = year + '-' + month + '-01'  
	var month_first = firstdate+" 00:00:00";  
	  
	var day = new Date(year,month,0);      
	var lastdate = year + '-' + month + '-' + day.getDate();  
	var month_last = lastdate+" 23:59:59";  
	flowQuery(month_first,month_last,parkingCode,parkingName);
	$('#inOutMemo').html(year+"年"+month+"月进出统计");
}
function flowQuery(beginTime,endTime,parkingCode,parkingName){
	layer.open({type: 2});
	var path = $('#path').val();
	var token = $('#token').val();
	$('.in-right').html("");
	$('.out-right').html("");
	var totalIn = 0;
	var totalOut = 0;
	var monthIn = 0;
	var monthOut = 0;
	var temIn = 0;
	var temOut = 0;
	$.post(path+'flowQuery',{'token':token,'queryType':2,'beginTime':beginTime,'endTime':endTime,'parkingCode':parkingCode,'parkingName':parkingName},function(data){
		if(data.errorCode==300000){
			for(var o in data.data){ 
				totalIn += data.data[o].inCars;
				totalOut += data.data[o].outCars;				
			}
			$.post(path+'flowQuery',{'token':token,'queryType':0,'beginTime':beginTime,'endTime':endTime,'parkingCode':parkingCode,'parkingName':parkingName},function(temData){
				for(var i in temData.data){ 
					temIn += temData.data[i].inCars;
					temOut += temData.data[i].outCars;
				}
				$.post(path+'flowQuery',{'token':token,'queryType':1,'beginTime':beginTime,'endTime':endTime,'parkingCode':parkingCode,'parkingName':parkingName},function(monthData){
					for(var j in monthData.data){ 
						monthIn += monthData.data[j].inCars;
						monthOut += monthData.data[j].outCars;
					}
					if(totalIn==0){
						var w=0;
					}else{
						var w=1.3;
					}
					var scale_in=monthIn/totalIn;
					var scale_out=temIn/totalIn;
					var app_num='<div><li style="width:'+w+'rem;"></li><span>'+totalIn+'辆</span></div><div><li style="width:'+scale_in*w+'rem;"></li><span>'+monthIn+'辆</span></div><div><li style="width:'+scale_out*w+'rem;"></li><span>'+temIn+'辆</span></div>';
					$(app_num).appendTo($('.in-right'));
					if(totalOut==0){
						var w=0;
					}else{
						var w=1.3;
					}
					var scale_in=monthOut/totalOut;
					var scale_out=temOut/totalOut;
					var app_num='<div><li style="width:'+w+'rem;"></li><span>'+totalOut+'辆</span></div><div><li style="width:'+scale_in*w+'rem;"></li><span>'+monthOut+'辆</span></div><div><li style="width:'+scale_out*w+'rem;"></li><span>'+temOut+'辆</span></div>';
					$(app_num).appendTo($('.out-right'));
				},'json');
			},'json');
		}else{
			window.location.href=path+'managetoLogin';
		}
		$('.in-right').find('li').last().css({'margin-bottom':0});
		$('.out-right').find('li').last().css({'margin-bottom':0});
		layer.closeAll(); 
	},'json');
						

}


//停车场收入统计
getPark();
function getPark(){
	var path = $('#path').val();
		var token = $('#token').val();
		$.post(path+'carparkAllQuery',{'token':token},function(data){
			if(data.errorCode==300000){
//				var parkingName = data.data[0].parkingName;
//				var parkingCode = data.data[0].parkingCode;
//                $("#carpark1").html(parkingName);
//                $("#carpark1").attr('carparkcode',parkingCode);
//                 $("#carpark2").html(parkingName);
//                $("#carpark2").attr('carparkcode',parkingCode);
				var parkingName = '全部';
				var parkingCode = '1';
                $("#carpark1").html(parkingName);
                $("#carpark1").attr('carparkcode',parkingCode);
                $("#carpark2").html(parkingName);
                $("#carpark2").attr('carparkcode',parkingCode);
                var mydate = new Date();
				var year = mydate.getFullYear();
				var month = (mydate.getMonth()+1);
				var day = mydate.getDate();
				if(day<10){
					day = "0"+day;
				}
				if(month<10){
					month = "0"+month;
				}
				var beginTime = year+"-"+month+"-"+day+" "+ "00:00:00";
				var endTime = year+"-"+month+"-"+day+" "+ "23:59:59";
                parkIncomeQuery(beginTime,endTime,parkingCode,parkingName);
                flowQuery(beginTime,endTime,parkingCode,parkingName);
                $(".start-time2").text(month+"月"+day+"日");
				$(".end-time2").text("");
                //$('#dateMemoIco').html(month+"月"+day+"日收费统计");
                $('#inOutMemo').html(month+"月"+day+"日进出统计");
			}else{
				window.location.href=path+'managetoLogin';
			}
		},'json');
}
function dayIncomeQuery(){
	var parkingName = $("#carpark1").html();
	var parkingCode = $("#carpark1").attr('carparkcode');
	var mydate = new Date();
	var year = mydate.getFullYear();
	var month = (mydate.getMonth()+1);
	var day = mydate.getDate();
	if(day<10){
		day = "0"+day;
	}
	if(month<10){
		month = "0"+month;
	}
	var beginTime = year+"-"+month+"-"+day+" "+ "00:00:00";
	var endTime = year+"-"+month+"-"+day+" "+ "23:59:59";
	$(".start-time2").text(month+"月"+day+"日");
	$(".end-time2").text("");
	parkIncomeQuery(beginTime,endTime,parkingCode,parkingName);
}

function monthIncomeQuery(){
	var parkingName = $("#carpark1").html();
	var parkingCode = $("#carpark1").attr('carparkcode');
	var date_ = new Date();  
	var year = date_.getFullYear();
	var month = date_.getMonth() + 1;  
	if(month<10){
		month = "0"+month;
	}
	var firstdate = year + '-' + month + '-01'  
	var month_first = firstdate+" 00:00:00";  
	
	var day = new Date(year,month,0);      
	var lastdate = year + '-' + month + '-' + day.getDate();  
	var month_last = lastdate+" 23:59:59";  
	parkIncomeQuery(month_first,month_last,parkingCode,parkingName);
	$('#dateMemoIco').html(year+"年"+month+"月收费统计");
}


function parkIncomeQuery(beginTime,endTime,parkingCode,parkingName){
	layer.open({type: 2});
	var path = $('#path').val();
	var token = $('#token').val();
	var tem = 0;
	var total = 0;
	var monthly = 0;
	var fee = 0;
	$.post(path+'parkIncomeQuery',{'beginTime':beginTime,'endTime':endTime,'parkingCode':parkingCode,'parkingName':parkingName,'queryType':0,'token':token},function(data){
		if(data.errorCode==300000){
			console.debug(data);
			for(var o in data.data){  
				tem+=data.data[o].tempPay/100;
				total+=data.data[o].allPay/100;
				monthly+=data.data[o].monthlyPay/100;
				fee+=data.data[o].fee/100;
			}
			$('#tem').html(tem);
			$('#total').html(total);
			$('#monthly').html(monthly);
			$('#fee').html(fee);
			layer.closeAll(); 
		}else{
			window.location.href=path+'managetoLogin';	
		}
	},'json')
	
	
	
	
}


$(".query-btn1").click(function(){
	$(".time-select-box1").show();
	$(".time-select-box1").animate({bottom:0},1000,function(){
		$(".operation-analysis-box").hide();
	})
})

$(".query-btn2").click(function(){
	$(".time-select-box2").show()
	$(".time-select-box2").animate({bottom:0},1000,function(){
		$(".earnings-box").hide();
	})
})

$(".query-btn3").click(function(){
	$(".time-select-box3").show();
	$(".time-select-box3").animate({bottom:0},1000,function(){
		$(".in-out-box").hide();
	})
})

$(".time-sure1").click(function(){
	if($("#picktime-s1").val()=='' || $("#picktime-e1").val()==''){
		return;
	}
	$(".start-time1").text($("#picktime-s1").val());
	$(".end-time1").text('~'+$("#picktime-e1").val());
	//$(".result-mesg1-2").html($(".result-mesg1-1").html())
	$(".operation-analysis-box").show();
	var strTime = $("#picktime-s1").val();
	var endTime = $("#picktime-e1").val();
	turnover(strTime,endTime);
	loadChart(strTime,endTime);
	space_occupancy(strTime,endTime);
	$(".time-select-box1").animate({bottom:"-100%"},1000,function(){
		$(".time-select-box1").hide();
	})
})

$(".time-sure2").click(function(){
	if($("#picktime-s2").val()=='' || $("#picktime-e2").val()==''){
		return;
	}
	var parkingName = $("#carpark1").html();
	var parkingCode = $("#carpark1").attr('carparkcode');
	$(".start-time2").text($("#picktime-s2").val());
	$(".end-time2").text('~'+$("#picktime-e2").val());
	//$(".result-mesg2-2").html($(".result-mesg2-1").html())
	$(".earnings-box").show();
	var strTime = $("#picktime-s2").val();
	var endTime = $("#picktime-e2").val();
	parkIncomeQuery(strTime,endTime,parkingCode,parkingName);
	$(".time-select-box2").animate({bottom:"-100%"},1000,function(){
		$(".time-select-box2").hide();
	})
})

$(".time-sure3").click(function(){
	if($("#picktime-s3").val()=='' || $("#picktime-e3").val()==''){
		return;
	}
	var parkingName = $("#carpark2").html();
	var parkingCode = $("#carpark2").attr('carparkcode');
	$(".start-time3").text($("#picktime-s3").val());
	$(".end-time3").text("~"+$("#picktime-e3").val());
	//$(".result-mesg3-2").html($(".result-mesg3-1").html())
	$(".in-out-box").show();
	var strTime = $("#picktime-s3").val();
	var endTime = $("#picktime-e3").val();
	flowQuery(strTime,endTime,parkingCode,parkingName);
	$(".time-select-box3").animate({bottom:"-100%"},1000,function(){
		$(".time-select-box3").hide();
	})
})


$(".close-select1").click(function(){
	$(".operation-analysis-box").show();
	$(".time-select-box1").animate({bottom:"-100%"},1000,function(){
		$(".time-select-box1").hide();
	})
})

$(".close-select2").click(function(){
	$(".earnings-box").show();
	$(".time-select-box2").animate({bottom:"-100%"},1000,function(){
		$(".time-select-box2").hide();
	})
})

$(".close-select3").click(function(){
	$(".in-out-box").show();
	$(".time-select-box3").animate({bottom:"-100%"},1000,function(){
		$(".time-select-box3").hide();
	})
})


// 查询结果左右滚动
var wid1=$(".result-mesg1-1").width();
//$(".result-mesg1-2").html($(".result-mesg1-1").html())
var index1=0;
setInterval(function(){
	index1++;
	if (index1>wid1) {
		index1=-250;
	};
	$(".query-result-mesg-box1").css({marginLeft:-index1});
},50)
var flog2=true;
$(".bottom-list2").click(fun2)
	
function fun2(){
	if (flog2) {
		var wid2=$(".result-mesg2-1").width();
		//$(".result-mesg2-2").html($(".result-mesg2-1").html())
		var index2=0;
		setInterval(function(){
			index2++;
			if (index2>wid2) {
				index2=-250;
			};
			$(".query-result-mesg-box2").css({marginLeft:-index2});
		},50)
		flog2=false;
}
}

var flog3=true;
$(".bottom-list3").click(fun3)

function fun3(){
	if (flog3) {
		var wid3=$(".result-mesg3-1").width();
		//$(".result-mesg3-2").html($(".result-mesg3-1").html())
		var index3=0;
		setInterval(function(){
			index3++;
			if (index3>wid3) {
				index3=-250;
			};
			$(".query-result-mesg-box3").css({marginLeft:-index3});
		},50)
		flog3=false;
	};
	

}


