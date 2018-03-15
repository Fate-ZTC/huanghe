package com.system.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import sun.misc.BASE64Decoder;

import com.opensymphony.xwork2.ActionContext;
import com.system.service.SysconfigService;
import com.system.utils.DateUtil;
import com.system.utils.ImageUtil;
import com.system.utils.StringUtil;

@Controller
public class UploadController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5153120182801019612L;
	@Resource 
	private SysconfigService sysconfigService;
	private static Map<String, String> extMap = null;
	private static Map<String, String> configMap = null;
	@RequestMapping("upload_execute")
	public void execute(@RequestParam("attached") MultipartFile attached,String dir,Integer w,Integer h,HttpServletRequest request,String system,HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String attachedFileName = attached.getOriginalFilename();
		try {
			String fileExt = attachedFileName.substring(attachedFileName.lastIndexOf(".") + 1).toLowerCase();
			if(!Arrays.<String>asList(getExtMap().get(dir).split(",")).contains(fileExt)){
				out.print("{\"status\":false,\"errorContent\":\"上传文件类型错误。只允许" + extMap.get(dir) + "格式。\"}");
			}else{
				String nowDate = DateUtil.formatDateNoSplit(System.currentTimeMillis());
				String realPath=request.getSession().getServletContext().getRealPath("/");
				String contextPath = request.getContextPath() + "/";
				if(StringUtil.isNotEmpty(system)){
					realPath = sysconfigService.getValue(system + "Path");
					contextPath = "";
				}
				String savePath = realPath
						+ "attached" + System.getProperty("file.separator") + dir + System.getProperty("file.separator")  
						+ nowDate + System.getProperty("file.separator");
				String saveUrl = contextPath + "attached/" + dir + "/" + nowDate + "/";
				File uploadDir = new File(savePath);
				if(!uploadDir.exists()){
					uploadDir.mkdirs();
				}
				//检查目录写权限
				if(!uploadDir.canWrite()){
					uploadDir.setWritable(true);
					uploadDir.setReadable(true);
				}
				String newName = StringUtil.createUUID() + "." + fileExt;
				File file = new File(savePath,newName);
				CommonsMultipartFile cf= (CommonsMultipartFile)attached; 
			    DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 

			    File f = fi.getStoreLocation();
				
				FileUtils.copyFile(f, file);
				if("image".equals(dir) && w != null && h != null){
					String  prevfix = w + "x" +h +"-";
					ImageUtil.thumbnailImage(file, w, h, prevfix , true);
					out.print("{\"status\":true,\"path\":\"" + saveUrl + prevfix + newName + "\",\"name\":\"" + attachedFileName + "\"}");
				}else{
					out.print("{\"status\":true,\"path\":\"" + saveUrl + newName + "\",\"name\":\"" + attachedFileName + "\"}");
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
			out.print("{\"status\":false,\"errorContent\":\"文件上传失败\"}");
		}finally{
			out.flush();
			out.close();
		}
	}
	@RequestMapping("upload_base64Img")
	public String base64Img(HttpSession session,HttpServletRequest request,HttpServletResponse response,String base64,String type) throws IOException{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter pw = response.getWriter();
		String nowDate = DateUtil.formatDateNoSplit(System.currentTimeMillis());
		String savePath = session.getServletContext().getRealPath("/") 
						+ "attached" + System.getProperty("file.separator") + "img" + System.getProperty("file.separator")  
						+ nowDate + System.getProperty("file.separator");
		String saveUrl = request.getContextPath() + "/attached/img/" + nowDate + "/";
		File uploadDir = new File(savePath);
		if(!uploadDir.exists()){
			uploadDir.mkdirs();
		}
		String fileExt = type.substring(6).replace("jpeg", "jpg");
		String newName = StringUtil.createUUID() + "." + fileExt;
		String code = base64.replace("jpeg", "jpg").substring(22);
		FileOutputStream out = null;
		try {
			byte[] buffer = new BASE64Decoder().decodeBuffer(code);
			out = new FileOutputStream(savePath + newName);
			out.write(buffer);
			out.close();
			pw.print("{\"status\":true,\"path\":\"" + saveUrl + newName + "\"}");
		} catch (IOException e) {
			e.printStackTrace();
			if(out != null){
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			pw.print("{\"status\":false,\"errorCode\":" + 203 + ",\"errorContent\":\"" + "接口未知错误" + "\"}");
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			pw.flush();
			pw.close();
			pw.print("{\"status\":false,\"errorCode\":" + 203 + ",\"errorContent\":\"" + "接口未知错误" + "\"}");
		}
		
		return null;
	}
	private Map<String, String> getExtMap(){
		if(extMap == null){
			extMap = new HashMap<String, String>();
			extMap.put("image", "gif,jpg,jpeg,png,bmp");
			extMap.put("flash", "swf,flv");
			extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
			extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
		}
		return extMap;
	}
	



	public static Map<String, String> getConfigMap() {
		return configMap;
	}

	public static void setConfigMap(Map<String, String> configMap) {
		UploadController.configMap = configMap;
	}
	
}
