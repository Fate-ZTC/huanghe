package com.parkbobo.utils.http;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request.Builder;


/**
 * http工具类
 */
public class HttpUtils {
    private static String baseUrl = "";

    private static OkHttpClient client;

    private static CacheControl cacheControl;

    private static HttpUtils httpUtils;


    public static void init(String baseUrl) {
        HttpUtils.baseUrl = baseUrl;
    }

    public synchronized static HttpUtils getInstance() {
        if (client == null) {

            //设置缓存
            Cache cache = new Cache(new File(""), 1024 * 1024 * 50);

            //设置超时时间
            client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
                    .cache(cache)
                    .connectTimeout(10,TimeUnit.SECONDS)
                    .build();
        }
        if (httpUtils == null) {
            httpUtils = new HttpUtils();

            /**
             * 缓存设置
             */
            cacheControl = new CacheControl.Builder()
                    .maxAge(30, TimeUnit.DAYS)
                    .build();
        }
        
        return httpUtils;
    }

   

    public OkHttpClient getClient() {
        return client;
    }

    public void post(String tag, String url, RequestBody requestBody, final HttpCallBack httpCallBack, HttpFeature... features) {
        int cache_time = -1;
        int out_time = 60;

        for (HttpFeature feature : features) {
            if (feature instanceof HttpCacheFeature) {
                HttpCacheFeature cacheFeature = (HttpCacheFeature) feature;
                cache_time = cacheFeature.getCache_time();
            }
            if (feature instanceof HttpTimeOutFeature) {
                HttpTimeOutFeature timeOutFeature = (HttpTimeOutFeature) feature;
                out_time = timeOutFeature.getTime_out();
            }
        }
        post(tag,url, requestBody, httpCallBack, out_time, cache_time);
    }

    /**
     * @param url          请求链接
     * @param requestBody  请求体
     * @param httpCallBack 请求回调
     * @param cache_time   缓存时间
     * @param out_time     超时时间
     */
    private void post(String tag,String url, RequestBody requestBody, final HttpCallBack httpCallBack, int cache_time, int out_time) {
        

        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("cache", cache_time + "")
                .tag(tag)
                .cacheControl(cacheControl);

        Callback callback = new Callback() {

            @Override
            public void onFailure(final Call call, final IOException e) {
            	httpCallBack.onError(call, e);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                final String body = response.body().string();
                if (response.isSuccessful()) {
                    httpCallBack.onSuccess(call, body);
                } else {
                    httpCallBack.onError(call, new IOException("服务器数据读取错误"));
                }
                response.close();
            }
        };

        if (out_time != -1) {
            OkHttpClient new_client = client.newBuilder().connectTimeout(out_time, TimeUnit.SECONDS).build();

            new_client.newCall(builder.build()).enqueue(callback);
        } else {
            client.newCall(builder.build()).enqueue(callback);
        }
    }

    /**
     * @param url         请求连接
     * @param requestBody 请求体
     * @param httpCallBack 回调
     * @return
     */
    public void post(String url, RequestBody requestBody, final HttpCallBack httpCallBack) {
        post("",url, requestBody, httpCallBack, 60, -1);
    }
    /**
     * @param url         请求连接
     * @param requestBody 请求体
     * @param httpCallBack 回调
     * @return
     */
    public void post(String tag,String url, RequestBody requestBody, final HttpCallBack httpCallBack) {
        post(tag,url, requestBody, httpCallBack, 60, -1);
    }

    public void get(String url, final HttpCallBack httpCallBack) {
        get("",url, httpCallBack, 60, -1);
    }
    public void get(String tag,String url, final HttpCallBack httpCallBack) {
        get(tag,url, httpCallBack, 60, -1);
    }
    public void get(String tag,String url,final HttpCallBack httpCallBack, HttpFeature... features) {
        int cache_time = -1;
        int out_time = 60;

        for (HttpFeature feature : features) {
            if (feature instanceof HttpCacheFeature) {
                HttpCacheFeature cacheFeature = (HttpCacheFeature) feature;
                cache_time = cacheFeature.getCache_time();
            }
            if (feature instanceof HttpTimeOutFeature) {
                HttpTimeOutFeature timeOutFeature = (HttpTimeOutFeature) feature;
                out_time = timeOutFeature.getTime_out();
            }
        }
        get(tag,url, httpCallBack, out_time, cache_time);
    }

    /**
     *
     * @param url
     * @param httpCallBack
     * @param cache_time   缓存时间
     * @param out_time     超时时间
     */
    private void get(String tag,String url, final HttpCallBack httpCallBack, int cache_time, int out_time) {
        

        Request.Builder builder = new Request.Builder()
                .url(url)
                .get()
                .addHeader("cache", cache_time + "")
                .tag(tag)
                .cacheControl(cacheControl);

        Callback callback = new Callback() {

            @Override
            public void onFailure(final Call call, final IOException e) {
            	httpCallBack.onError(call, e);httpCallBack.onError(call, e);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                final String body = response.body().string();
                if (response.isSuccessful()) {
                    httpCallBack.onSuccess(call, body);
                } else {
                    httpCallBack.onError(call, new IOException("服务器数据读取错误"));
                }
                response.close();
            }
        };

        if (out_time != -1) {
            OkHttpClient new_client = client.newBuilder().build();

            new_client.newCall(builder.build()).enqueue(callback);
        } else {
            client.newCall(builder.build()).enqueue(callback);
        }
    }

    /**
     * 下载文件
     *
     * @param url          下载链接
     * @param path         下载的文件路径
     * @param fileCallBack 回调
     */
    public void downloadFile(String url, final File path,final HttpFileCallBack fileCallBack) {
        

        final Request request = new Request.Builder()
                .url(url)
                .get()
                .cacheControl(cacheControl)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(final Call call, final IOException e) {
            	fileCallBack.onError(call, e);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                InputStream is = response.body().byteStream();
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(path);
                }catch (final Exception e) {
                    e.printStackTrace();

                    fileCallBack.onError(call, e);

                    is.close();

                    return;
                }
                final long length = response.body().contentLength();
                long getleng = 0;

                byte[] bytes = new byte[1024];

                int index = -1;

                do {
                    index = is.read(bytes);
                    if (index != -1) {
                        fos.write(bytes, 0, index);
                        getleng = getleng + index;

                        final long finalGetleng = getleng;
                        fileCallBack.onProgress(finalGetleng, length);
                    }
                } while (index != -1);

                is.close();

                fos.flush();
                fos.close();

                fileCallBack.onSuccess(call, path);

            }
        });
    }

    /**
     * 通过tag来结束掉链接
     * @param tag 标致
     */
    public void cancelConnect(String tag){
        if (client!=null){
            for(Call call:client.dispatcher().runningCalls()){
                if (call.request().tag().equals(tag)) {
                    call.cancel();
                }
            }
            for(Call call:client.dispatcher().queuedCalls()){
                if (call.request().tag().equals(tag)) {
                    call.cancel();
                }
            }
        }
    }

    /**
     * 结束所有链接
     */
    public void cancelAll(){
        if (client!=null){
            client.dispatcher().cancelAll();
        }
    }

    private static final int TIMEOUT_CONNECT = 5; //5秒
    private static final int TIMEOUT_DISCONNECT = 60 * 60 * 24 * 7; //7天

    public static final Interceptor REWRITE_RESPONSE_INTERCEPTOR = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            //获取retrofit @headers里面的参数，参数可以自己定义，在本例我自己定义的是cache，跟@headers里面对应就可以了
            String cache = chain.request().header("cache");
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            String cacheControl = originalResponse.header("Cache-Control");
            //如果cacheControl为空，就让他TIMEOUT_CONNECT秒的缓存，本例是5秒，方便观察。注意这里的cacheControl是服务器返回的
            if (cacheControl == null) {
                //如果cache没值，缓存时间为TIMEOUT_CONNECT，有的话就为cache的值
                if (cache == null || "".equals(cache)) {
                    cache = TIMEOUT_CONNECT + "";
                }
                if (!cache.equals("0")) {
                    originalResponse = originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + cache)
                            .build();
                } else {
                    originalResponse = originalResponse.newBuilder()
                            .header("Cache-Control", "no-cache")
                            .build();
                }
                return originalResponse;
            } else {
                return originalResponse;
            }
        }
    };

 
    public String uploadFile(String url,File file,String fileName,Map<String,String> para){
    	MultipartBody.Builder build = new MultipartBody.Builder()
    	.setType(MultipartBody.FORM)
    	.addFormDataPart("upload", fileName, RequestBody.create(MediaType.parse("application/octet-stream"),file));
    	for (String key : para.keySet()) {
			String val=para.get(key);
			build.addFormDataPart(key, val);
		}
    	
    	Request.Builder builder = new Request.Builder()
        .url(url)
        .post(build.build())
        .cacheControl(cacheControl);
    	
    	try {
			return client.newCall(builder.build()).execute().body().string();
		} catch (Exception e) {
			System.out.println("请求异常");
			e.printStackTrace();
		}
		return null;
    }
    
    public String postRequest(String url,Map<String,String> para){
    	MultipartBody.Builder build = new MultipartBody.Builder()
    	.setType(MultipartBody.FORM);
    	for (String key : para.keySet()) {
			String val=para.get(key);
			build.addFormDataPart(key, val);
		}
    	
    	
    	Request.Builder builder = new Request.Builder()
        .url(url)
        .post(build.build())
        .cacheControl(cacheControl);
    	
    	try {
			return client.newCall(builder.build()).execute().body().string();
		} catch (Exception e) {
			System.out.println("请求异常");
			e.printStackTrace();
		}
		return null;
    }
    
	public static void inputstreamtofile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String requestPostJson(String url,String json,int out_time){
		OkHttpClient new_client = client.newBuilder().connectTimeout(out_time, TimeUnit.SECONDS).build();
		RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
		Request request = new Request.Builder()
		.url(url)
		.post(body)
		.build();
		
		try {
			return new_client.newCall(request).execute().body().string();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	} 
}
