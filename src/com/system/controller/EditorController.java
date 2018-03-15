//package com.system.controller;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.fileupload.disk.DiskFileItem;
//import org.apache.commons.io.FileUtils;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.commons.CommonsMultipartFile;
//
//import com.alibaba.fastjson.JSONObject;
//import com.system.utils.DateUtil;
//import com.system.utils.StringUtil;
//
//@Controller
//public class EditorController implements Serializable{
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -7274815286946646068L;
//	private File[] imgFile;
//	private String[] imgFileFileName;
//	private String dir;
//	private static Map<String, String> extMap = null;
//	
//	public String upload(@RequestParam("imgFile") MultipartFile imgFile,String dir,HttpServletRequest request){
//		最大文件大小
//		long maxSize = 900000000;
//		//文件保存路径
//		String savePath =request.getSession().getServletContext().getRealPath("/") + "attached" + System.getProperty("file.separator");
//		//文件保存目录URL
//		String saveUrl =request.getContextPath() +"/attached/";
//		//定义允许上传的文件扩展名
//		CommonsMultipartFile cf= (CommonsMultipartFile)imgFile; 
//	    DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
//
//	    File f = fi.getStoreLocation();
//		if(imgFile.length == 0){
//			writeJSON(getError("请选择文件。"));
//			return NONE;
//		}
//		//检查目录
//		File uploadDir = new File(savePath);
//		if(!uploadDir.isDirectory()){
//			uploadDir.mkdirs();
//		}
//		//检查目录写权限
//		if(!uploadDir.canWrite()){
//			uploadDir.setWritable(true);
//			uploadDir.setReadable(true);
//		}
//		if(!StringUtil.isNotEmpty(dir)){
//			dir = "image";
//		}
//		if(!getExtMap().containsKey(dir)){
//			writeJSON(getError("目录名 不正确。"));
//			return NONE;
//		}
//		//创建文件夹
//		savePath += dir + System.getProperty("file.separator");
//		saveUrl += dir + "/";
//		File saveDirFile = new File(savePath);
//		if(!saveDirFile.exists()){
//			saveDirFile.mkdirs();
//		}
//		savePath += DateUtil.formatDateNoSplit(System.currentTimeMillis()) + System.getProperty("file.separator");
//		saveUrl += DateUtil.formatDateNoSplit(System.currentTimeMillis()) + "/";
//		File dirFile = new File(savePath);
//		if(!dirFile.exists()){
//			dirFile.mkdirs();
//		}
//		for(int i = 0; i < imgFile.length ; i++){
//			try {
//				if(imgFile[i].length() > maxSize){
//					writeJSON(getError("上传文件大小超过限制。"));
//					return NONE;
//				}
//				String fileExt = imgFileFileName[i].substring(imgFileFileName[i].lastIndexOf(".")+1);
//				if(!Arrays.<String>asList(getExtMap().get(dir).split(",")).contains(fileExt)){
//					writeJSON(getError("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dir) + "格式。"));
//					return NONE;
//				}
//				String newName = StringUtil.createUUID() + "." + fileExt;
//				File file = new File(savePath,newName);
//				FileUtils.copyFile(imgFile[i], file);
//				JSONObject obj = new JSONObject();
//				obj.put("error", 0);
//				obj.put("url", saveUrl + newName);
//				obj.put("title", imgFileFileName[i].substring(0,imgFileFileName[i].lastIndexOf(".")));
//				obj.put("fileName", imgFileFileName[i]);
//				writeJSON(obj.toJSONString());
//			} catch (IOException e) {
//				e.printStackTrace();
//				writeJSON(getError("上传文件失败。"));
//				return NONE;
//			}
//		}
//		return NONE;
//		
//		
//	}
//	private Map<String, String> getExtMap(){
//		if(extMap == null){
//			extMap = new HashMap<String, String>();
//			extMap.put("image", "gif,jpg,jpeg,png,bmp");
//			extMap.put("flash", "swf,flv");
//			extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
//			extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
//		}
//		return extMap;
//	}
//	private String getError(String message) {
//		JSONObject obj = new JSONObject();
//		obj.put("error", 1);
//		obj.put("message", message);
//		return obj.toJSONString();
//	}
//	public File[] getImgFile() {
//		return imgFile;
//	}
//	public void setImgFile(File[] imgFile) {
//		this.imgFile = imgFile;
//	}
//	public String[] getImgFileFileName() {
//		return imgFileFileName;
//	}
//	public void setImgFileFileName(String[] imgFileFileName) {
//		this.imgFileFileName = imgFileFileName;
//	}
//	public String getDir() {
//		return dir;
//	}
//	public void setDir(String dir) {
//		this.dir = dir;
//	}
//}
