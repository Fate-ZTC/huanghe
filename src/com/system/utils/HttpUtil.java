package com.system.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * @author ztc
 * @date 2019年09月23日 下午2:49
 * HttpClient工具类
 */
public class HttpUtil {

    private static Logger logger = Logger.getLogger(HttpUtil.class);

    /**
     * get请求
     * @return
     */
    public static String doGet(String url) {
        try {
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet(url);
            request.addHeader("authorization",  "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiY21jY3Itc2VydmVyIl0sInVzZXJfbmFtZSI6ImFkbWluIiwic2NvcGUiOlsianMiXSwicnVsZXMiOlsiYWRtaW4iXSwiZXhwIjoxNTcwMDc0NzI2LCJhdXRob3JpdGllcyI6WyJzdHVkeS13b3JrZXIiLCJ1c2VyX21hbmFnZXIiLCJhZGQtcGFub3JhbWEiLCJhZXJpYWwtcGhvdG8iLCJhY2hpZXZlbWVudCIsInNlbnNpdGl2ZV9tYW5hZ2VyIiwidmVyc2lvbl9hbmRyb2lkIiwiQ01EQkUiLCJlZGl0LXN0dWRlbnRDb3Vyc2VTY2hlZHVsZSIsImNhdGVnb3J5LW9yZ2FuaXphdGlvbiIsIm1hcC1jb3JyZWN0aW9uIiwibW9kZWwtbWFuYWdlci1pbmRleCIsImNhdGVnb3J5LW90aGVyIiwiZWRpdC1hY2hpZXZlbWVudCIsIm1ham9yIiwiY2F0ZWdvcnktbWFuYWdlciIsIkNNQ0NSIiwiYXBwX21hbmFnZXIiLCJjcmVhdGUtYnVpbGRpbmciLCJNVEFwcCIsImVkaXQtdGVhY2hpbmctc3RhZmYiLCJlZHVjYXRpb25hbC1hZG1pbmlzdHJhdGlvbiIsIm1lbnVfaW5kZXgiLCJlZGl0LWNsYXNzIiwiUENBcHAiLCJjbGFzc3Jvb20iLCJlZGl0LWN1cnJpY3VsdW0iLCJ0ZWFjaC1jb25maWciLCJ2ZXJzaW9uX2lvcyIsInJlc2VydmF0aW9uU2V0dGluZyIsImdyb3VuZC1wYW5vcmFtYSIsImluZm9ybWF0aW9uLW90aGVyIiwiQ01JUFMiLCJlZGl0LWNsYXNzQ291cnNlU2NoZWR1bGUiLCJjcmVhdGUtb3JnYW5pemF0aW9uIiwibWV0YV9pbmRleCIsImNvbmZpZ19tYW5hZ2VyIiwiY2F0ZWdvcnktbGFiZWwiLCJlZGl0LXJvb20iLCJjYXRlZ29yeS1yb29tIiwiY29ycmVjdGlvbiIsImluZm9ybWF0aW9uLW1hbmFnZXIiLCJDTS1NMzgyMCIsIm1haW50ZW5hbmNlX2NlbnRlciIsInN0dWRlbnQiLCJjb25maWd1cmF0aW9uIiwibWVudV9tYW5hZ2VyIiwic3R1ZGVudENvdXJzZVNjaGVkdWxlIiwiaW5mb3JtYXRpb24tYnVpbGRpbmciLCJvdXRsaWVycyIsInN1Z2dlc3Rpb25zLW1hbmFnZXIiLCJzZXJ2ZXJfbWFpbnRlbmFuY2UiLCJpbmZvcm1hdGlvbi1vcmdhbml6YXRpb24iLCJwZXJzb25hbF9pbmRleCIsIkNNR0lTIiwiY2F0ZWdvcnktYnVpbGRpbmciLCJyb2xlX2NvbmZpZ3VyYXRpb24iLCJ2ZXJzaW9uX21pY3JvX2Rvd25sb2FkIiwibW9kZWwtbWFuYWdlciIsImdhdGV3YXlfc3RhdHMiLCJlZGl0LXN0dWRlbnQiLCJwZXJzb25hbF9jZW50ZXIiLCJjcmVhdGUtbGFiZWwiLCJjbGFzcyIsImFjYWRlbXkiLCJlZGl0LWNsYXNzcm9vbSIsImdlb3N0YXRpc3RpY3MiLCJzeXN0ZW1fbG9nIiwicGFub3JhbWljLXJvYW1pbmciLCJ2ZXJzaW9uX21hbmFnZXIiLCJjdXJyaWN1bHVtIiwiY2xhc3NDb3Vyc2VTY2hlZHVsZSIsImVkaXQtbWFqb3IiLCJvcmdhbml6YXRpb25UeXBlIiwidXJvc3ciLCJ0ZWFjaGluZy1zdGFmZiIsImdhdGV3YXlfZGF0YSIsImVkaXQtb3RoZXIiLCJpbmZvcm1hdGlvbi1yb29tIiwiY3JlYXRlLXJvbGUiLCJpbmZvcm1hdGlvbi1sYWJlbCIsImFwcF9pbmRleCIsIm9yZ2FuaXphdGlvbiIsIm9wZW5mdW5jIiwic3VnZ2VzdGlvbnMiLCJnYXRld2F5X2Zsb3ciLCJtZXRhX2FuYWx5c2lzIiwic2VuZE1lc3NhZ2VDb25maWciXSwianRpIjoiOGJlZTM3ODMtOGFiOS00ODIyLTk5NmEtNTI2NDNjMmI2ZGZiIiwiY2xpZW50X2lkIjoiY21jY3ItaDUifQ.jMHTzKiFqLD2wWNdHU2TgoXEWOR3zm-BbzJ-xKGtUGsOjBXFEfJibLg8D19-89iASXQz3PhhlllMjyCWv0CtwAOf1FowJuF_GOvOSyPL0Pxt-XpTQkkxoHaTqK4gMuYZY2zL-UDYk0zRd3oVrQZqcvPHwDk7hisBX6UNYGXIeXlOsn-KOQfz-eKsJTxISbiaE5GtGseqZ9kmP6uZvo0FjN3Ro5l6W9hd9zEqG0qs7B0pKOmYXBUgCfclGKn1gIm3CWPHoY2iaa_6lQEyW4OALX-TNegLedy2dDdrbjPhvWP2C1MgIHeb_sGW086CP1ODSPCTcx4JBxjJoeuAaKD9Pw");
            HttpResponse response = client.execute(request);

            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());

                return strResult;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * post请求(用于key-value格式的参数)
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, Map params){

        BufferedReader in = null;
        try {
            // 定义HttpClient
            HttpClient client = new DefaultHttpClient();
            // 实例化HTTP方法
            HttpPost request = new HttpPost();
            //request.addHeader("Content-type","application/json; charset=utf-8");
            //request.setHeader("Accept", "application/json");
            request.setURI(new URI(url));

            //设置参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String value = String.valueOf(params.get(name));
                nvps.add(new BasicNameValuePair(name, value));
            }
            request.setEntity(new UrlEncodedFormEntity(nvps,HTTP.UTF_8));

            HttpResponse response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            if(code == 200){	//请求成功
                in = new BufferedReader(new InputStreamReader(response.getEntity()
                        .getContent(),"utf-8"));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();

                return sb.toString();
            }
            else{	//
                System.out.println("状态码：" + code);
                return null;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getInfoByToken(String url, String token) {
        try {
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet(url);
            request.addHeader("authorization",  "Bearer" + " " + token);
            HttpResponse response = client.execute(request);

            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());

                return strResult;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}