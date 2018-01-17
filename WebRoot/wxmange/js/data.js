dayloadChart();
function dayloadChart(){
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
	turnover(beginTime,endTime);
	loadChart(beginTime,endTime);
	space_occupancy(beginTime,endTime);
	$(".start-time1").text(month+"月"+day+"日");
	$(".end-time1").text('');
}
function monthloadChart(){
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
	turnover(month_first,month_last);
	loadChart(month_first,month_last);
	space_occupancy(month_first,month_last);
	$('#dateChartMome').html(year+"年"+month+"月运营分析结果");
}
function loadChart(beginTime,endTime){
	layer.open({type: 2
    	,content: '加载中'
 	});
	var path = $('#path').val();
	var token = $('#token').val();
	$.post(path+'parkIncomePreQuery',{'beginTime':beginTime,'endTime':endTime,'token':token,'queryType':2},function(msg){
		if(msg.errorCode=="0"){
			var arrayObj = new Array();
			for(var o in msg.data){
				 var num = msg.data[o].pre;
				 var numpre = (num*100).toFixed(2)
				 var parkname =  msg.data[o].parkingName;
				 parkname = parkname.replace("停车场","")
				 var str1 = {"name": parkname+numpre+"%", "value":msg.data[o].pre};
				 arrayObj.push(str1);
			}
			// 基于准备好的dom，初始化echarts实例
			var myChart = echarts.init(document.getElementById('bing'));
		
		        // 指定图表的配置项和数据
		        option = {
		    title : {
		        text: '某站点用户访问来源',
		        subtext: '纯属虚构',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    legend: {
		        orient : 'vertical',
		        x : 'left',
		        data:['白招牌停车','龙马广场停','龙驰路停车','陶然路停车','中心广场停']
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            magicType : {
		                show: true, 
		                type: ['pie', 'funnel'],
		                option: {
		                    funnel: {
		                        x: '50%',
		                        width: '50%',
		                        funnelAlign: 'left',
		                        max: 1
		                    }
		                }
		            },
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		    series : [
		        {
		            name:'访问来源',
		            type:'pie',
		            radius : '60%',
		            center: ['50%', '60%'],
		            data:arrayObj
		        }
		    ]
		};
		
		        // 使用刚指定的配置项和数据显示图表。
		layer.closeAll(); 
		myChart.setOption(option);
		 
		}else{
			layer.closeAll(); 
			layer.open({
			    content: data.errorMsg()
			    ,skin: 'msg'
			    ,time: 2 //2秒后自动关闭
			 });
			 window.location.href=path+'managetoLogin';
		}
	},'json');
	
	
	
	
	
	
}
    

        