//判断用户使用的手机是安卓系统还是ios系统
var ua = navigator.userAgent.toLowerCase();	
if (/iphone|ipad|ipod/.test(ua)) {
    document.documentElement.style.fontSize = window.screen.width/3.75 + 'px';
} else if (/android/.test(ua)) {
    document.documentElement.style.fontSize = window.screen.width/3.75 + 'px';

}
