package com.mobile.port;
/**
 * 版本更新接口
 * @author RY
 * @version 1.0
 * @since 2016-7-15 19:28:30
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.parkbobo.utils.Configuration;
import com.parkbobo.utils.PageBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mobile.model.AppVersion;
import com.mobile.service.AppVersionService;
import com.opensymphony.xwork2.ActionSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppVersionInterface extends ActionSupport implements ServletResponseAware,ServletRequestAware{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1391979226417015277L;
	protected HttpServletResponse response;
	protected HttpServletRequest request;
	
	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response = response;
		this.response.setContentType("text/html;charset=utf-8");
	}
	
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
	}
	@Resource(name="appVersionService")
	private AppVersionService appVersionService;
	private AppVersion appVersion;
	private PageBean<AppVersion> appVersionPage;
	private String versioncode;
	private String savepath;
	private String fileName;
	private Long contentLength;
	private static String ACCESSPATH = Configuration.getInstance().getValue("basepath");
	/**
	 *  检查更新
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("update")
	public String update() throws IOException
	{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		//app类型
		String type = request.getParameter("type");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		StringBuffer s = new StringBuffer();
		try {
			List<AppVersion> appVersions = this.appVersionService.getByHql("From AppVersion as a where a.isDel = 0 and a.type ="+type+" order by a.posttime desc");
			AppVersion version = appVersions.get(0);
			s.append("{");
			s.append("\"versionCode\":\"" + version.getVersioncode() + "\",");
			s.append("\"downUrl\":\"" + ACCESSPATH + "service/appVersion!downloadApp?versioncode=" + version.getVersioncode() +"\",");
			s.append("\"versionName\":\"" + version.getName() + "\",");
			s.append("\"postTime\":\"" + version.getPosttime().getTime() + "\",");
			s.append("\"content\":\"" + version.getContent() + "\",");
			s.append("\"needUpdate\":\"" + version.getNeedUpdate() + "\"");
			s.append("}");
			out.print(s);
		} catch (Exception e) {
			s = new StringBuffer();
			s.append("{");
			s.append("\"versionCode\":\"\",");
			s.append("\"downUrl\":\"\",");
			s.append("\"versionName\":\"\",");
			s.append("\"postTime\":\"\",");
			s.append("\"content\":\"\",");
			s.append("\"needUpdate\":\"\"");
			s.append("}");
			out.print(s);
		}
		out.flush();    
    	out.close();
		return NONE;
	}
	/**
	 * 下载最新版本APP
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("download")
	public String download() throws UnsupportedEncodingException
	{
		HttpServletRequest request = ServletActionContext.getRequest();
		//app类型
		String type = request.getParameter("type");
		List<AppVersion> appVersions = this.appVersionService.getByHql("from AppVersion as a where a.isDel = 0 and a.type ="+type+" order by a.posttime desc");
		if(appVersions.size() > 0)
		{
			this.appVersion = appVersions.get(0);
			if(appVersion == null){
				return INPUT;
			}
			this.fileName = java.net.URLEncoder.encode("huanghuai.apk","UTF-8");
			this.savepath = appVersion.getAttached();
			File f = new File(this.savepath);
			this.contentLength = f.length();
			this.appVersion.setDownloadcount(this.appVersion.getDownloadcount() + 1);
			this.appVersionService.save(appVersion);
			return "downloadApp";
		}else
		{
			return INPUT;
		}
	}
	/**
	 * 
	 * 根据versioncode下载对应版本app
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String downloadApp() throws UnsupportedEncodingException
	{
		this.appVersion = this.appVersionService.getById(versioncode);
		if(appVersion == null){
			return INPUT;
		}
		this.fileName = java.net.URLEncoder.encode("huanghuai.apk","UTF-8");
		this.savepath = appVersion.getAttached();
		File f = new File(this.savepath);
		this.contentLength = f.length();
		this.appVersion.setDownloadcount(this.appVersion.getDownloadcount() + 1);
		this.appVersionService.save(appVersion);
		return "downloadApp";
	}
	
	public InputStream getInputStream() throws FileNotFoundException 
	{
		return new FileInputStream(this.savepath);
	}
	public static String transFromGBKToISO(String str) 
	{
		String newstr = "";
		try {
			newstr = new String(str.getBytes("ISO-8859-1"), "UTF-8");
		} catch (Exception e) {
			System.out.println("error in transFromGBKToISO");
		}
		return newstr;
	}
	public String dispatcherAdd()
	{
		return "dispatcherAdd";
	}
	public AppVersion getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(AppVersion appVersion) {
		this.appVersion = appVersion;
	}

	public PageBean<AppVersion> getAppVersionPage() {
		return appVersionPage;
	}

	public void setAppVersionPage(PageBean<AppVersion> appVersionPage) {
		this.appVersionPage = appVersionPage;
	}

	public String getSavepath() {
		return savepath;
	}
	
	public void setSavepath(String savepath) {
		this.savepath = savepath;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getVersioncode() {
		return versioncode;
	}
	
	public void setVersioncode(String versioncode) {
		this.versioncode = versioncode;
	}
	
	public Long getContentLength() {
		return contentLength;
	}
	
	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}
	
}