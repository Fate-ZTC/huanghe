package com.parkbobo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.HttpRequest;
import com.parkbobo.utils.JPushClientExample;

/**
 * Created by lijunhong on 18/4/19.
 */
@Service("exceptionPushService")
public class ExceptionPushService {


    final String PATROL_ADMIN_CONFIG_PARAM = Configuration.getInstance().getValue("patrolAdminConfigParam");
    final String FIRE_PATROL_ADMIN_CONFIG_PARAM = Configuration.getInstance().getValue("firePatrolAdminConfigParam");
    final String patrolAdminConfigURL = Configuration.getInstance().getValue("patrolAdminConfigURL");

    //app管理段消防和安防推送相关key
    final String APP_KEY = Configuration.getInstance().getValue("AppKey");
    final String SECRET = Configuration.getInstance().getValue("Secret");

    //app安防巡更使用端推送相关key
    final String USE_PATROL_APP_KEY = Configuration.getInstance().getValue("UsePatrolAppKey");
    final String USE_PATROL_SECRET = Configuration.getInstance().getValue("UsePatrolSecret");


    /**
     * 进行消息推送(消防安防管理端)
     * @param title		标题
     * @param content	内容
     * @param alias		推送人员
     * @param type 		1 消防 2 安防
     */
    public void pushSend(String type,String title,String content,String alias) {
        if(alias != null && !"".equals(alias)) {
            JPushClientExample push = new JPushClientExample(APP_KEY, SECRET);
            Map<String, String> map = new HashMap<String, String>();
            map.put("type", type);
            map.put("title", title);
            map.put("content", content);


            //TODO 这里进行测试推送
//            String test_al = "ydtest";
            push.aliasSendMsg(title, content, map, alias);
        }
    }



    /**
     * 进行消息推送(消防管理端)
     * @param title		标题
     * @param content	内容
     * @param alias		推送人员
     */
    public void pushSendHelp(Integer id,String title,String content,String userCode,String userName,Double lon, Double lat,String type,String alias) {
        if(alias != null && !"".equals(alias)) {
            /*因为暂定移动救援只需要推使用端的，所以把key改成了使用端的key，如果后面需要不同推送，再做修改*/
            JPushClientExample push = new JPushClientExample(USE_PATROL_APP_KEY, USE_PATROL_SECRET);
            Map<String, String> map = new HashMap<String, String>();
            map.put("id",id.toString());
            map.put("title", title);
            map.put("userName",userName);
            map.put("content", content);
            map.put("userCode",userCode);
            map.put("lon",lon.toString());
            map.put("lat",lat.toString());
            map.put("type",type);
            //TODO 这里进行测试推送
//            String test_al = "ydtest";
            push.aliasSendMsg(title, content, map, alias);
        }
    }




    /**
     * 消息推送(安防巡更使用端)
     * @param type      类型(使用端推送：3)
     * @param title     标题
     * @param content   内容
     * @param alias     推送人员
     */
    public void pushUsePatrolSend(String type,String title,String content,String alias) {
        if(alias != null && !"".equals(alias)) {
            JPushClientExample push = new JPushClientExample(USE_PATROL_APP_KEY, USE_PATROL_SECRET);
            Map<String, String> map = new HashMap<String, String>();
            map.put("type", type);
            map.put("title", title);
            map.put("content", content);
            push.aliasSendMsg(title, content, map, alias);
        }
    }



    /**
     *
     * @param type 1 为消防端管理员用户 ,2位安防端管理员用户
     * @return
     */
    public String getPartrolAdminUserId(String type) {
        HttpRequest httpRequest = new HttpRequest();
        String param = "";
        if("1".equals(type)) {
            param = "moduleId=" + PATROL_ADMIN_CONFIG_PARAM;
        }
        if("2".equals(type)) {
            param = "moduleId=" + FIRE_PATROL_ADMIN_CONFIG_PARAM;
        }
        String result = httpRequest.sendGet(patrolAdminConfigURL,param);
        if(result != null && !"".equals(result)) {
            JSONObject resultObject = JSONObject.parseObject(result);
            if(resultObject != null) {
                int code = resultObject.getIntValue("code");
                boolean status = resultObject.getBoolean("status");
                if(code == 200 && status) {
                    //获取data
                    JSONObject dataObject = resultObject.getJSONObject("data");
                    if(dataObject != null) {
                        System.out.println(dataObject.getString("privateUsers"));
                        return dataObject.getString("privateUsers");
                    }
                }
            }
        }
        return null;
    }
}
