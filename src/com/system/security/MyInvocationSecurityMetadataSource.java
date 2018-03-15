package com.system.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.system.model.Resources;
import com.system.security.tool.AntUrlPathMatcher;
import com.system.security.tool.UrlMatcher;
import com.system.service.ResourcesService;



/**
 * 
 * 此类在初始化时，应该取到所有资源及其对应角色的定义
 * 
 * 对于资源的访问权限的定义，我们通过实现FilterInvocationSecurityMetadataSource这个接口来初始化数据。
 * 看看loadResourceDefine方法，我在这里，假定index.jsp这个资源，需要ROLE_USER角色的用户才能访问,other.jsp这个资源，需要ROLE_NO角色的用户才能访问。
 * 这个类中，还有一个最核心的地方，就是提供某个资源对应的权限定义，即getAttributes方法返回的结果。
 * 注意，我例子中使用的是AntUrlPathMatcher这个path matcher来检查URL是否与资源定义匹配，事实上你还要用正则的方式来匹配，或者自己实现一个matcher。
 * 这里的角色和资源都可以从数据库中获取，建议通过我们封装的平台级持久层管理类获取和管理。
 * 
 */
public class MyInvocationSecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource  {
	private static final Logger logger = Logger.getLogger(MyInvocationSecurityMetadataSource.class);
	private UrlMatcher urlMatcher = new AntUrlPathMatcher();
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;
	private ResourcesService resourcesService;
	public ResourcesService getResourcesService() {
		return resourcesService;
	}
	public void setResourcesService(ResourcesService resourcesService) {
		this.resourcesService = resourcesService;
	}
	public MyInvocationSecurityMetadataSource(ResourcesService resourcesService) throws Exception{
		this.resourcesService = resourcesService;
		loadResourceDefine();
		
	}
	
	
	
	//加载所有资源与权限的关系
	private void loadResourceDefine(){
		if(resourceMap == null) {
			resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
			//查询数据库Resources所有资源遍历
			List<Resources> resList = resourcesService.getByProperty("enable", 1);
			for (Resources resources : resList) {
				Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
				ConfigAttribute ca = new SecurityConfig(resources.getEnname());
				atts.add(ca);
				resourceMap.put(resources.getTarget(), atts);
			}
			//默认所有用户权限
			Map<String, String> defaultCa = new HashMap<String,String> ();
			defaultCa.put("main_index", "/main*");
			defaultCa.put("upload_image", "/upload_image*");
			defaultCa.put("upload_base64Img", "/upload_base64Img*");
			defaultCa.put("mapPoint_map", "/mapPoint_map*");
			defaultCa.put("mapPolygon_map", "/mapPolygon_map*");
			defaultCa.put("mapPoint_getMapPointCategory", "/mapPoint_getMapPointCategory*");
			defaultCa.put("editor_upload", "/editor_upload*");
			defaultCa.put("appVersion_upload", "/appVersion_upload*");
			defaultCa.put("thematicLocation_getParentThematicLocation", "/thematicLocation_getParentThematicLocation*");
			defaultCa.put("thematicPoint_map", "/thematicPoint_map*");
			defaultCa.put("thematicPolyline_map", "/thematicPolyline_map*");
			defaultCa.put("thematicMapPermission_getZtreeData", "/thematicMapPermission_getZtreeData_map*");
			for (Map.Entry<String, String> map : defaultCa.entrySet()) {  
				Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
				ConfigAttribute ca = new SecurityConfig(map.getKey());
				atts.add(ca);
				resourceMap.put(map.getValue(), atts);
			}
			
			
		}
	}
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		if (logger.isDebugEnabled()) {
			logger.debug("getAttributes(Object) - start"); //$NON-NLS-1$
		}
		String url = ((FilterInvocation)object).getRequestUrl();
		int firstQuestionMarkIndex = url.indexOf("?");  
        if (firstQuestionMarkIndex != -1) {  
            url = url.substring(0, firstQuestionMarkIndex);  
        }  
        if (resourceMap == null) {  
            loadResourceDefine();  
        }
		Iterator<String> ite = resourceMap.keySet().iterator();
		while (ite.hasNext()) {
            String resURL = ite.next();
            if (urlMatcher.pathMatchesUrl(resURL, url)) {
            	if (logger.isDebugEnabled()) {
					logger.debug("getAttributes(Object) - end"); //$NON-NLS-1$
				}
            	return resourceMap.get(resURL);
            }
        }
		if (logger.isDebugEnabled()) {
			logger.debug("getAttributes(Object) - end"); //$NON-NLS-1$
		}
		return	null;
	}
	public boolean supports(Class<?> clazz) {
		return true;
	}
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return new ArrayList<ConfigAttribute>();
	}
	
}
