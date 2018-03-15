package com.parkbobo.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.sf.json.JSONObject;


public class ResendHttpPost {
	private static ResendHttpPost resendHttpPost;
	private ResendHttpPost(){}
	public synchronized static ResendHttpPost getInstance(){
		if(resendHttpPost==null){
			resendHttpPost = new ResendHttpPost();
		}
		return resendHttpPost;
	}
	public String resend(String url,String data,int resendNum, int sleepTime){
//		MyThread myThread = new MyThread(url, data, resendNum, sleepTime);
//		Thread thread = new Thread(myThread);
//		thread.start();
		String result = "";
		ExecutorService pool = Executors.newCachedThreadPool();
		Callable<String> c1 = new MyCallable(url, data, resendNum, sleepTime); 
		Future<String> f1 = pool.submit(c1); 
		try {
			result =  f1.get().toString();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		//关闭线程池 
         pool.shutdown();
         System.out.println(result);
         return result;
         
	}
	protected class MyCallable implements Callable<String>{
		private String url;//接口地址
		private String data;//传递参数
		private int resendNum;//重发次数
		private int sleepTime;//重发间隔时间
		public MyCallable(String url,String data,int resendNum, int sleepTime){
			this.url = url;
			this.data = data;
			this.resendNum = resendNum;
			this.sleepTime = sleepTime;
		}
		@Override
		public String call() {
			int i = 0;
			String state = "";
			while(i < resendNum && !state.equals("0")){
				String sendPost = HttpPostGzip.sendPost(url, data);
				if(sendPost != null && !sendPost.equals("")){
					state = parseState(sendPost);
				}
				i++;
				System.out.println(DateUtils.getDefaultInstance().formatString(System.currentTimeMillis()) +"第" + i + "次重新请求E泊服务器地址："+url);
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return state;
		}
		
	}
	private String parseState(String json){
		JSONObject fromObject = JSONObject.fromObject(json);
		Object object = fromObject.get("state");
		return String.valueOf(object);
	}
	public static void main(String[] args) {
		ResendHttpPost.getInstance().resend("http://182.150.28.182:8087/subscribe/paysync?partnerid=bobopartner&sign=","",5,5000);
	}
}

