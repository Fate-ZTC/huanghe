package com.parkbobo.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.parkbobo.utils.httpmodel.PassCarPort;

public class HttpReqMapThread extends Thread{
	private PassCarPort passCarPort;
	
	public HttpReqMapThread(PassCarPort passCarPort) {
		super();
		this.passCarPort = passCarPort;
	}

	public void run() {
		 HttpClient httpClient = new HttpClient();
			httpClient.getHostConfiguration().setHost("www.jiuchengpark.cn", 80);
			PostMethod method = new PostMethod("/jcmap/map_updateParkLotStatus");
			try {
				method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
				NameValuePair floorid = new NameValuePair("floorid", passCarPort.getFlourCode());
				NameValuePair name = new NameValuePair("name", passCarPort.getSpotId());
				NameValuePair portStatus = new NameValuePair("status", passCarPort.getDirect() + "");
				NameValuePair plateNum = new NameValuePair("plateNum", passCarPort.getCarPlate());
				method.addParameters(new NameValuePair[] {floorid, name, portStatus, plateNum});
				int status = httpClient.executeMethod(method);
				System.out.println(status);
				System.out.println(method.getResponseBodyAsString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				if(method != null){
					method.releaseConnection();
				}
			}
	 }
}
