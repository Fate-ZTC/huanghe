Array.prototype.forEach || (Array.prototype.forEach = function(a, b) {
    var c, d, e, f, g;
    if (null == this) throw new TypeError("this is null or not defined");
    if (e = Object(this), f = e.length >>> 0, "function" != typeof a) throw new TypeError(a + " is not a function");
    for (arguments.length > 1 && (c = b), d = 0; f > d;) d in e && (g = e[d], a.call(c, g, d, e)),
        d++
});
Array.prototype.remove = function(s) {
    for (var i = 0; i < this.length; i++) {
        if (s == this[i]) {
            this.splice(i, 1);
        }
    }
};
/**
 * 自定义键值对类型HashMap
 * @constructor
 */
function HashMap() {
    this.keys = new Array();
    this.data = new Object();
    this.put = function(key, value) {
        if(this.data[key] == null){
            this.keys.push(key);
        }
        this.data[key] = value;
    };
    this.get = function(key) {
        return this.data[key];
    };
    this.remove = function(key) {
        this.keys.remove(key);
        this.data[key] = null;
    };
    this.each = function(fn){
        if(typeof fn != 'function'){
            return;
        }
        var len = this.keys.length;
        for(var i=0;i<len;i++){
            var k = this.keys[i];
            fn(k,this.data[k],i);
        }
    };
    this.entrys = function() {
        var len = this.keys.length;
        var entrys = new Array(len);
        for (var i = 0; i < len; i++) {
            entrys[i] = {
                key : this.keys[i],
                value : this.data[i]
            };
        }
        return entrys;
    };
    this.isEmpty = function() {
        return this.keys.length == 0;
    };
    this.size = function(){
        return this.keys.length;
    };
    this.contains = function(key){
        if(this.data[key] == undefined){
            return false;
        }else{
            return true;
        }
    };
    this.toString = function(){
        var s = "{";
        for(var i=0;i<this.keys.length;i++,s+=','){
            var k = this.keys[i];
            s += k+"="+this.data[k];
        }
        s+="}";
        return s;
    };
}
/**
 * ajax请求
 * @param options
 * @constructor
 */
function LqAjax(options) {
    options = options || {};
    options.type = (options.type || "GET").toUpperCase();
    options.dataType = options.dataType || "json";
    var params = formatParams(options.data);
    if(typeof window.XDomainRequest != 'undefined'){
        var xdr = new XDomainRequest();
        xdr.timeout = 10000;

        xdr.onload = function(){
            if(options.dataType == 'json'){
                options.success && options.success(eval("("+xdr.responseText+")"));
            }else{
                options.success && options.success(xdr.responseText);
            }
        };
        xdr.onerror = function(){
            options.fail && options.fail(500);
        };

        if (options.type == "GET") {
            if(options.url.indexOf("?") >=0 ){
                xdr.open("GET", options.url + "&" + params, options.async);
            }else{
                xdr.open("GET", options.url + "?" + params, options.async);
            }

            xdr.send(null);
        } else if (options.type == "POST") {
            xdr.open("POST", options.url, options.async);

            xdr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            xdr.send(params);
        }
    }else{
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4) {
                var status = xhr.status;
                if (status >= 200 && status < 300) {
                    if(options.dataType == 'json'){
                        options.success && options.success(eval("("+xhr.responseText+")"));
                    }else{
                        options.success && options.success(xhr.responseText);
                    }

                } else {
                    options.fail && options.fail(status);
                }
            }
        };
        if (options.type == "GET") {
            if(options.url.indexOf("?") >=0 ){
                xhr.open("GET", options.url + "&" + params, options.async);
            }else{
                xhr.open("GET", options.url + "?" + params, options.async);
            }
            xhr.send(null);
        } else if (options.type == "POST") {
            xhr.open("POST", options.url, options.async);
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            xhr.send(params);
        }
    }
}
function formatParams(data) {
    var arr = [];
    for (var name in data) {
        arr.push(encodeURIComponent(name) + "=" + encodeURIComponent(data[name]));
    }
    arr.push(("t=" + Math.random()).replace(".",""));
    return arr.join("&");
}
/**
 * 工具方法
 * @type {{isNotEmpty: Function, deleteLastStr: Function, log: Function, get: Function, post: Function, fowardUrl: Function}}
 */
var LqUtil = {
    isNotEmpty:function(str){
        if(str != undefined && str.length > 0){
            return true;
        }else{
            return false;
        }
    },

    deleteLastStr:function(str){
        if(this.isNotEmpty(str)){
            return str.substring(0, str.length-1);
        }else{
            return "";
        }
    },
    log:function(info){
        console.log(info);
    },
    get:function(url,async,data,callback){
        LqAjax({
            type:'GET',
            url:url,
            data:data,
            async: async,
            success:callback,
            error:function(response){
                console.log("error:" + response.responseText);
            }
        })

    },
    post:function(url,async,data,callback){
        LqAjax({
            type:'POST',
            url:url,
            data:data,
            async: async,
            success:callback,
            error:function(response){
                console.log("error:" + response.responseText);
            }
        })
    },
    fowardUrl:function(url){
        window.location=url;
    }
};

