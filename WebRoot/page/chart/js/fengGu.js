	//开始时间
	var mydate = new Date();
	var year=mydate.getFullYear();
	var month=mydate.getMonth()+1;
	month=(month<10?'0':'')+month;
	var day=mydate.getDate();
	day=(day<10?'0':'')+day;
	var str=year+'-'+month+'-'+day;
	$("#start-time").val(str);
	$("#end-time").val(str);


	var start_time = {
			  elem: '#start-time',
			  format: 'YYYY-MM-DD',
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
			  format: 'YYYY-MM-DD',
			  max: '2099-06-16',
			  istime: true,
			  istoday: false,
			  choose: function(datas){
			    start_time.max = datas;
			    $("#end-time").val(datas);
			  }
			};
			laydate(end_time);
			
			
			function ajax(){
				var path = $("#path").val();
				var customType=$(".carAct").attr("value");
				var startTime = $("#start-time").val();
				var endTime = $("#end-time").val();
				var gateId=$("#gateId option:selected").val();
				if(startTime==""){
					layer.msg('请选择开始时间', {icon: 1});
					return;
				}else{
					startTime = startTime+" 00:00:00"
				}
				if(endTime==""){
					layer.msg('请选择结束时间', {icon: 1});
					return;
				}else{
					endTime = endTime+" 23:59:59"
				}
				var index = layer.load(0, {shade: [0.5,'#333']})
				$.ajax({
				  type:'post',
                  url:path+'/park/carStatistics_sta',
                  data:{'startTime':startTime,"endTime":endTime,'gateId':gateId,'customType':customType},
                  dataType:'json',
				  success: function(msg){
				  	if("success" == msg.status){
				  		var arr=[],arrName=[];
				  		$.each(msg.fengGu.series, function() {
				  			var obj={
				  				name:this.name,
						        type:'line',
						        areaStyle: {normal: {}},
						        data:this.data
				  			};
				  			arr.push(obj);
				  			arrName.push(this.name);
				  		});
				  		//峰谷统计图
						
			
						var fengGuChart = echarts.init(document.getElementById('fengGu'));
						
						        // 指定图表的配置项和数据
						        fengGu = {
						    title: {
						        textStyle:{
						        		fontSize:12,
						        		color:'#7A7979',
						        		fontWeight:'normal'
						        	},	
						        	text: '辆',
						        	x:'16',
						        	top:'20'
						    },
						    tooltip : {
						        trigger: 'axis',
						        axisPointer: {
						            type: 'cross',
						            label: {
						                backgroundColor: '#6a7985'
						            }
						        }
						    },
						    legend: {
						    	top:15,
						    	textStyle:{
						    		fontSize:16
						    	},
						        data:arrName
						    },
						    toolbox: {
						        feature: {
						            saveAsImage: {}
						        }
						    },
						    grid: {
						        left: '3%',
						        right: '4%',
						        bottom: '3%',
						        containLabel: true
						    },
						    xAxis : [
						        {
						            type : 'category',
						            boundaryGap : false,
						            data : msg.fengGu.xAxis
						        }
						    ],
						    yAxis : [
						        {
						            type : 'value'
						        }
						    ],
						    series : arr
						};
						
						        // 使用刚指定的配置项和数据显示图表。
						        fengGuChart.setOption(fengGu);
						        
						        layer.closeAll(); 
				  	}
				  },
				fail:function(){
					alert("123")  
				}
				});
			}
		ajax();	
		//车辆切换
		$(".carClass").click(function(){
			$(this).addClass("carAct").siblings().removeClass("carAct");
			ajax();
		}) 
