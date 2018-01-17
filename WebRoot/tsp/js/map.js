$("#search").focus(function(){
	$(".search_del").show();
})
$(".search_del").click(function(){
	$("#search").val('');
	$("#search").blur();
	$(".search_del").hide();
})
//$("#search").blur(function(){
//	$(".search_del").hide();
//})
$(".search-btn").click(function(){
	if($("#search").val()!=''){
		$(".search-result").show();
	}
})
$(".result-list li").click(function(){
	$(".search-result").hide();
})

$(".slideTopBot").click(function(){
	$(".route-list").slideToggle('normal');
	$(".slideTopBot").toggleClass("slideTopBot-act");
})

var holdPosition = 0;
  var mySwiper = new Swiper('.map-container',{
    slidesPerView:'auto',
    mode:'vertical',
    watchActiveIndex: true,
    nextButton: '.button-next',
    prevButton: '.button-prev',
    onTouchStart: function() {
      holdPosition = 0;
    },
    onResistanceBefore: function(s, pos){
      holdPosition = pos;
    },
    onTouchEnd: function(){
      if (holdPosition>100) {
        // Hold Swiper in required position
        mySwiper.setWrapperTranslate(0,100,0)

        //Dissalow futher interactions
        mySwiper.params.onlyExternal=true

        //Show loader
        $('.preloader').addClass('visible');
		loadNewSlides();
      }
      var now=mySwiper.activeLoopIndex;
	  $(".swiper-slide").eq(now).addClass("floor-now").siblings().removeClass("floor-now");
    },
    onSlideClick: function(){
      
    }
  })
	$('.button-prev').click(function(){
		mySwiper.swipePrev(); 
		var now=mySwiper.activeLoopIndex;
		$(".swiper-slide").eq(now).addClass("floor-now").siblings().removeClass("floor-now");
	})
	$('.button-next').click(function(){
	mySwiper.swipeNext(); 
	var now=mySwiper.activeLoopIndex;
	$(".swiper-slide").eq(now).addClass("floor-now").siblings().removeClass("floor-now");
	})
	$(".swiper-slide").click(function(){
      var index=$(this).index();
      $(".swiper-wrapper").css({transform:'translate3d(0px, -'+25*index+'px, 0px)'});
      $(".swiper-slide").eq(index).addClass("floor-now").siblings().removeClass("floor-now");
	})
  var slideNumber = 0;
  function loadNewSlides() {
    setTimeout(function(){
      mySwiper.setWrapperTranslate(0,0,0)
      mySwiper.params.onlyExternal=false;

      //Update active slide
      mySwiper.updateActiveSlide(0)

      //Hide loader
      $('.preloader').removeClass('visible')
    },1000)

    slideNumber++;
  }