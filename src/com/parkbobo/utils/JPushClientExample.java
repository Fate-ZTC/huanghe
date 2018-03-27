package com.parkbobo.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
/**
 * 极光推送方法封装
 * @version 1.0 基本的消息通知  By RY 2014
 * @version 1.1 使用新版本的jar包，添加个推机制 By RY 2015
 * @version 1.2 补充注释 By RY 2016-7-19 10:32:38
 * 
 *
 */
public class JPushClientExample {
	private String appKey;
	private String masterSecret;

	private static JPushClient jpush = null;

	/**
	 * 保存离线的时长。秒为单位。最多支持10天（864000秒）。
	 * 0 表示该消息不保存离线。即：用户在线马上发出，当前不在线用户将不会收到此消息。
	 * 此参数不设置则表示默认，默认为保存1天的离线消息（86400秒)。
	 */
	private static int timeToLive = 60 * 60 * 24;  

	public JPushClientExample(String appKey, String masterSecret) {
		// TODO Auto-generated constructor stub
		this.appKey = appKey;
		this.masterSecret = masterSecret;
	}

	public static void main(String[] args) {
		JPushClientExample push = new JPushClientExample("7137044983fe02dfd5836e78", "fed0e42674361b2aa7ef160f");
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "5");
		map.put("content","测试");
		push.aliasSendMsg("黄淮图书馆", "测试", map, "980120");

	}

	private static void testSendNotification() {
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("type", "3");
		map.put("id", "");
		map.put("content", "http://211.149.164.113:8081/qbjandroid/info!sydetails?id=358");
		
		PushPayload payload = buildPushObject_Notification("新闻", "新闻推送测试", map);
		 try {
			PushResult result = jpush.sendPush(payload);
			System.out.println(result.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	
	private static void testSendMsg() {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("type", "4");
		map.put("id", "ddd");
		map.put("content", "http://211.149.164.113:8081/qbjandroid/info!sydetails?id=358");
		
		PushPayload payload = buildPushObject_Message("新闻", "lsdafwe 玩儿玩儿", map);
		 try {
			PushResult result = jpush.sendPush(payload);
			System.out.println(result.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	
	/**
	 * 系统通知
	 * @param msgTitle，APP接收通知，显示的标题
	 * @param msgContent，APP接收通知，显示的内容
	 * @param map，封装APP接收消息后动作需要的参数，主要包括：
	 * 1.type,消息类型，目前有： 1.系统通知；2.版本升级；3.个推类型消息; 4.座位分配成功通知；5.签到通知提示；6、座位续约通知；7.预约自动结束通知；8.排队预约失效通知
	 * 2.id,根据不同类型
	 */
	public void systemNotice(String msgTitle, String msgContent, Map<String, String> map){
		
		try {
			jpush = new JPushClient(masterSecret, appKey, timeToLive);
			
			PushPayload payload = buildPushObject_Notification(msgTitle, msgContent, map);
			
			PushResult pushResult = jpush.sendPush(payload);
			
			System.out.println(pushResult.toString());
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 个推消息
	 * @param msgTitle
	 * @param msgContent
	 * @param map
	 */
	public void sendMsg(String msgTitle, String msgContent, Map<String, String> map){
		
		try {
			jpush = new JPushClient(masterSecret, appKey, timeToLive);
			
			PushPayload payload = buildPushObject_Message(msgTitle, msgContent, map);
			
			PushResult pushResult = jpush.sendPush(payload);
			
			System.out.println(pushResult.toString());
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 个推消息
	 * @param msgTitle
	 * @param msgContent
	 * @param map
	 */
	public void aliasSendMsg(String msgTitle, String msgContent, Map<String, String> map,String alias){
		
		try {
			jpush = new JPushClient(masterSecret, appKey, timeToLive);
			
			PushPayload payload = buildPushObject_all_alias_alert(msgTitle, msgContent, map, alias);
			
			PushResult pushResult = jpush.sendPush(payload);
			
			System.out.println(pushResult.toString());
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据tag批量推送消息
	 * @param msgTitle
	 * @param msgContent
	 * @param map
	 * @param tag
	 */
	public void tagSendMsg(String msgTitle, String msgContent, Map<String, String> map, List<String> tag){
		
		try {
			jpush = new JPushClient(masterSecret, appKey, timeToLive);
			
			PushPayload payload = buildPushObject_tag_alert(msgTitle, msgContent, map, tag);
			
			PushResult pushResult = jpush.sendPush(payload);
			
			System.out.println(pushResult.toString());
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 构建通知公告推送的PushPayload
	 * @param msgTitle
	 * @param msgContent
	 * @param map
	 * @return PushPayload
	 */
	public static PushPayload buildPushObject_Notification(String msgTitle, 
			String msgContent, Map<String, String> map) {		
		
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(msgTitle)
                                .setBadge(1)
                                .setSound("Windows_Logon_Sound.wav")
                                .addExtra("type", (String) map.get("type"))
                                .addExtra("id", (String) map.get("id"))
                                .addExtra("content", (String) map.get("content"))
                                .build())
                        .addPlatformNotification(AndroidNotification.newBuilder()
                        		.setAlert(msgTitle)
                        		.setTitle(msgContent)
                        		.addExtra("type", (String) map.get("type"))
                                .addExtra("id", (String) map.get("id"))
                                .addExtra("content", (String) map.get("content"))
                                .build())
                        .build())
                 .setOptions(Options.newBuilder()
                         .setApnsProduction(false)
                         .build())
                 .build();
    }
	
	/**
	 * 构建个推 PushPayload
	 * @param msgTitle
	 * @param msgContent
	 * @param map
	 * @return PushPayload
	 */
	public static PushPayload buildPushObject_Message(String msgTitle, 
			String msgContent, Map<String, String> map) {		
		
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                .setMessage(Message.newBuilder()
                		.setTitle(msgTitle)
                		.setMsgContent(msgContent)
                		.addExtras(map)
                		.build())
                .setOptions(Options.newBuilder()
                         .setApnsProduction(true)//IOS推送环境，true生产环境，false开发环境
                         .build())
                .build();
    }
	
	
	/**
	 * 构建tag批量推送 PushPayload
	 * @param msgTitle //头
	 * @param msgContent //内容
	 * @param map 
	 * @param tag //tag
	 * @return PushPayload
	 */
	public static PushPayload buildPushObject_tag_alert(String msgTitle,
			String msgContent, Map<String, String> map, List<String> tag) {
		return PushPayload.newBuilder()
				.setAudience(Audience.tag(tag))
				.setPlatform(Platform.android_ios())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(msgContent)
                                .setBadge(1)
                                .setSound("Windows_Logon_Sound.wav")
                                .addExtra("type", (String) map.get("type"))
                                .addExtra("id", (String) map.get("id"))
                                .addExtra("content", (String) map.get("content"))
                                .build())
                        .addPlatformNotification(AndroidNotification.newBuilder()
                        		.setAlert(msgContent)
                        		.setTitle(msgTitle)
                        		.addExtra("type", (String) map.get("type"))
                                .addExtra("id", (String) map.get("id"))
                                .addExtra("content", (String) map.get("content"))
                                .build())
                        .build())
                 .setOptions(Options.newBuilder()
                         .setApnsProduction(true)//IOS推送环境，true生产环境，false开发环境
                         .build())
                 .build();
	}
	
	/**
	 * 构建别名个推 PushPayload
	 * @param msgTitle //头
	 * @param msgContent //内容
	 * @param map 
	 * @param alias //别名
	 * @return PushPayload
	 */
	public static PushPayload buildPushObject_all_alias_alert(String msgTitle,
			String msgContent, Map<String, String> map, String alias) {
		return PushPayload.newBuilder()
				.setAudience(Audience.alias(alias))
				.setPlatform(Platform.android_ios())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(msgContent)
                                .setBadge(1)
                                .setSound("Windows_Logon_Sound.wav")
                                .addExtra("type", (String) map.get("type"))
                                .addExtra("id", (String) map.get("id"))
                                .addExtra("content", (String) map.get("content"))
                                .build())
                        .addPlatformNotification(AndroidNotification.newBuilder()
                        		.setAlert(msgContent)
                        		.setTitle(msgTitle)
                        		.addExtra("type", (String) map.get("type"))
                                .addExtra("id", (String) map.get("id"))
                                .addExtra("content", (String) map.get("content"))
                                .build())
                        .build())
                 .setOptions(Options.newBuilder()
                         .setApnsProduction(true)//IOS推送环境，true生产环境，false开发环境
                         .build())
                 .build();
	}
	

}
