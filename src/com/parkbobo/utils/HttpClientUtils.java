package com.parkbobo.utils;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
 
/**
 * Http Client工具类
 */
public final class HttpClientUtils {
     
    private static final String TYPE_STRING = "string";
     
    private static final String TYPE_BYTEARRAY = "byte[]";
     
    private static final String TYPE_STREAM = "stream";
     
    private static HttpClientUtils instance;
     
    private HttpClientUtils(){}
     
    /**
     * 使用默认的页面请求编码：utf-8
     * @return
     */
    public static HttpClientUtils getInstance(){
        return getInstance("UTF-8");
    } 
     
    public static HttpClientUtils getInstance(String urlCharset){
        if(instance == null){
            instance = new HttpClientUtils();
        }
        //设置默认的url编码
        instance.setUrlCharset(urlCharset);
        return instance;
    }
     
    /**
     * 请求编码，默认使用utf-8
     */
    private String urlCharset = "UTF-8";
     
    /**
     * @param urlCharset 要设置的 urlCharset。
     */
    public void setUrlCharset(String urlCharset) {
        this.urlCharset = urlCharset;
    }
    /**
     * 获取字符串型返回结果，通过发起http post请求
     * @param targetUrl
     * @param params
     * @return
     * @throws Exception
     */
    public String getResponseBodyAsString(String targetUrl,List<Param> params)throws Exception{
        return (String)setPostRequest(targetUrl,params,TYPE_STRING,null);
    }
     
    /**
     * 获取字符数组型返回结果，通过发起http post请求
     * @param targetUrl
     * @param params
     * @return
     * @throws Exception
     */
    public byte[] getResponseBodyAsByteArray(String targetUrl,List<Param> params)throws Exception{
        return (byte[])setPostRequest(targetUrl,params,TYPE_BYTEARRAY,null);
    }
     
    /**
     * 将response的返回流写到outputStream中，通过发起http post请求
     * @param targetUrl                 请求地址
     * @param params                    请求参数<paramName,paramValue>
     * @param outputStream              输出流
     * @throws Exception
     */
    public void getResponseBodyAsStream(String targetUrl,List<Param> params,OutputStream outputStream)throws Exception{
        if(outputStream == null){
            throw new IllegalArgumentException("调用HttpClientUtil.setPostRequest方法，targetUrl不能为空!");
        }
        //response 的返回结果写到输出流
        setPostRequest(targetUrl,params,TYPE_STREAM,outputStream);
    }
     
    /**
     * 利用http client模拟发送http post请求
     * @param targetUrl                 请求地址
     * @param params                    请求参数<paramName,paramValue>
     * @return  Object                  返回的类型可能是：String 或者 byte[] 或者 outputStream           
     * @throws Exception
     */
    private Object setPostRequest(String targetUrl,List<Param> params,String responseType,OutputStream outputStream)throws Exception{
        if(StringUtils.isBlank(targetUrl)){
            throw new IllegalArgumentException("调用HttpClientUtil.setPostRequest方法，targetUrl不能为空!");
        }
        Object responseResult = null;
        HttpClient client = null;
        PostMethod postMethod = null;
        SimpleHttpConnectionManager connectionManager = null;
        try{
            connectionManager =  new SimpleHttpConnectionManager(true);
            //连接超时,单位毫秒
            connectionManager.getParams().setConnectionTimeout(40 * 1000);
            //读取超时,单位毫秒
            connectionManager.getParams().setSoTimeout(60 * 1000);
            //
            //connectionManager.getParams().set
            //设置获取内容编码
            connectionManager.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,urlCharset);
            client = new HttpClient(new HttpClientParams(),connectionManager);

            postMethod = new PostMethod(targetUrl);
            //设置请求参数的编码
            postMethod.getParams().setContentCharset(urlCharset);
            //服务端完成返回后，主动关闭链接
            postMethod.setRequestHeader("Connection","close");
            for (Param param : params) {
            	postMethod.addParameter(param.getKey(),param.getValue());
			}
            int sendStatus = client.executeMethod(postMethod);
            if(sendStatus == HttpStatus.SC_OK){
                if(StringUtils.equals(TYPE_STRING,responseType)){
                    responseResult = postMethod.getResponseBodyAsString();
                }else if(StringUtils.equals(TYPE_BYTEARRAY,responseType)){
                    responseResult = postMethod.getResponseBody();
                }else if(StringUtils.equals(TYPE_STREAM,responseType)){
                    InputStream tempStream = postMethod.getResponseBodyAsStream();
                    byte[] temp = new byte[1024];
                    while(tempStream.read(temp) != -1){
                        outputStream.write(temp);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            if(postMethod != null){
            	postMethod.releaseConnection();
            }
            //关闭链接
            if(connectionManager != null){
                connectionManager.shutdown();
            }
        }finally{
            //释放链接
            if(postMethod != null){
            	postMethod.releaseConnection();
            }
            //关闭链接
            if(connectionManager != null){
                connectionManager.shutdown();
            }
        }
         
        return responseResult;
    }
}