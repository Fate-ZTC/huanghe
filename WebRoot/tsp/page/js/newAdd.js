$(".classifyTitle").click(function(){
    $(".classifyList").toggleClass("active-cl");
    $(".classifyTitle i").toggleClass("jiantu2");
    $(".zhezhao").toggleClass("zhezhao-bl");
})

classifyListli();
function classifyListli(){
    $(".classifyList li").click(function(){
        $(".classifyList").removeClass("active-cl");
        $(".classifyTitle i").removeClass("jiantu2");
        $(".zhezhao").removeClass("zhezhao-bl");
        $(this).addClass("active");
        $(this).siblings().removeClass("active");
    })
}
remove()
function remove() {
    $(".detailList li").each(function (index, domEle) {
        if($(this).hasClass("chongZhi")){
            $(this).find(".class").remove();
        }
    });
}
//充值金额
chongZhi();
function chongZhi(){
    var str=$(".optionalAmount").children(".active").text().replace(/[^0-9]/ig,"");
    $(".moneyBox").children("input").val(str);
    var inp =  $(".moneyBox").children("input").val();
    var span = $(".optionalAmount").children("li");
    $(".moneyBox").children("input").focus(function(){
    	$(".optionalAmount").children("li").removeClass("active");
    	$.each(span,function(k,v){
    		$(this).removeClass("active");
    	})
    })
    
    $(".optionalAmount li").click(function(){
        $(this).addClass("active");
        $(this).siblings().removeClass("active");
        str=$(this).text().replace(/[^0-9]/ig,"");
        $(".moneyBox").children("input").val(str);
    })
}
