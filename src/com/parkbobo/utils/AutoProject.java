package com.parkbobo.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class AutoProject {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		traversal("F:\\Workspaces\\parkbobo2.2\\src\\com\\parkbobo\\model");
	}
	/**
	 * 遍历文件夹下的文件，并打印文件夹下文件的路径
	 * @param directoryPath 文件夹目录
	 * @throws IOException 
	 */
	public static void traversal(String directoryPath) throws IOException {
		File dir = new File(directoryPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		} else {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					traversal(files[i].getAbsolutePath());
				} else {
					create(files[i].getName());
				}
			}
		}
	}
	public static void create(String modelname) throws IOException
	{
		String model = modelname.substring(0,modelname.length()-5);
		String root = "F:\\test\\com\\parkbobo\\";
		String rootpackage = "com.parkbobo";
		if(!model.substring(model.length()-2,model.length()).equals("Id"))
		{
			//建dao
			File daoFile = new File(root +"dao\\"+model+"Dao.java");
			if (!daoFile.getParentFile().exists()) {
				daoFile.getParentFile().mkdirs();
			}
			daoFile.createNewFile();
			BufferedWriter daobw = new BufferedWriter(new OutputStreamWriter(  
					new FileOutputStream(daoFile), "UTF-8"));  
			daobw.write("package "+rootpackage+".dao;");
			daobw.write("\r\n");
			daobw.write("import "+rootpackage+".model."+model+";");
			daobw.write("\r\n");
			daobw.write("public interface "+model+"Dao extends BaseDao<"+model+">{");
			daobw.write("\r\n");
			daobw.write("}");
			
			
			daobw.flush();
			daobw.close();
			
			//建dao实现
			File daoImplFile = new File(root +"dao\\impl\\"+model+"DaoImpl.java");
			if (!daoImplFile.getParentFile().exists()) {
				daoImplFile.getParentFile().mkdirs();
			}
			daoImplFile.createNewFile();
			BufferedWriter daoimplbw = new BufferedWriter(new OutputStreamWriter(  
					new FileOutputStream(daoImplFile), "UTF-8"));  
			daoimplbw.write("package "+rootpackage+".dao.impl;");
			daoimplbw.write("\r\n");
			daoimplbw.write("import org.springframework.stereotype.Component;");
			daoimplbw.write("\r\n");
			daoimplbw.write("import "+rootpackage+".model."+model+";");
			daoimplbw.write("\r\n");
			daoimplbw.write("import "+rootpackage+".dao."+model+"Dao;");
			daoimplbw.write("\r\n");
			daoimplbw.write("@Component(\""+model.substring(0,1).toLowerCase()+model.substring(1,model.length())+"DaoImpl\")");
			daoimplbw.write("\r\n");
			daoimplbw.write("public class "+model+"DaoImpl extends BaseDaoSupport<"+model+"> implements "+model+"Dao {");
			daoimplbw.write("\r\n");
			daoimplbw.write("}");
			
			
			daoimplbw.flush();
			daoimplbw.close();
			
			
			//建service
			File serviceFile = new File(root +"service\\"+model+"Service.java");
			if (!serviceFile.getParentFile().exists()) {
				serviceFile.getParentFile().mkdirs();
			}
			serviceFile.createNewFile();
			BufferedWriter servicebw = new BufferedWriter(new OutputStreamWriter(  
					new FileOutputStream(serviceFile), "UTF-8"));  
			servicebw.write("package "+rootpackage+".service;");
			servicebw.write("\r\n");
			servicebw.write("import org.springframework.stereotype.Component;");
			servicebw.write("\r\n");
			servicebw.write("import javax.annotation.Resource;");
			servicebw.write("\r\n");
			servicebw.write("import java.util.List;");
			servicebw.write("\r\n");
			servicebw.write("import "+rootpackage+".model."+model+";");
			servicebw.write("\r\n");
			servicebw.write("import "+rootpackage+".dao."+model+"Dao;");
			servicebw.write("\r\n");
			servicebw.write("@Component(\""+model.substring(0,1).toLowerCase()+model.substring(1,model.length())+"Service\")");
			servicebw.write("\r\n");
			servicebw.write("public class "+model+"Service {");
			servicebw.write("\r\n");
			servicebw.write("	@Resource(name=\""+model.substring(0,1).toLowerCase()+model.substring(1,model.length())+"DaoImpl\")");
			servicebw.write("\r\n");
			servicebw.write("	private "+model+"Dao "+model.substring(0,1).toLowerCase()+model.substring(1,model.length())+"Dao;");
			servicebw.write("\r\n");
			servicebw.write("\r\n");
			servicebw.write("	public List<"+model+"> getByHql(String hql){");
			servicebw.write("\r\n");
			servicebw.write("		return this."+model.substring(0,1).toLowerCase()+model.substring(1,model.length())+"Dao.getByHQL(hql);");
			servicebw.write("\r\n");
			servicebw.write("	}");
			servicebw.write("\r\n");
			servicebw.write("}");
			
			servicebw.flush();
			servicebw.close();
			
			//建action
			File actionFile = new File(root +"action\\"+model+"Action.java");
			if (!actionFile.getParentFile().exists()) {
				actionFile.getParentFile().mkdirs();
			}
			actionFile.createNewFile();
			BufferedWriter actionbw = new BufferedWriter(new OutputStreamWriter(  
					new FileOutputStream(actionFile), "UTF-8"));  
			actionbw.write("package "+rootpackage+".action;");
			actionbw.write("\r\n");
			actionbw.write("import org.springframework.stereotype.Component;");
			actionbw.write("\r\n");
			actionbw.write("import org.springframework.context.annotation.Scope;");
			actionbw.write("\r\n");
			actionbw.write("import javax.annotation.Resource;");
			actionbw.write("\r\n");
			actionbw.write("import com.opensymphony.xwork2.ActionSupport;");
			actionbw.write("\r\n");
			actionbw.write("import "+rootpackage+".service."+model+"Service;");
			actionbw.write("\r\n");
			actionbw.write("@Component(\""+model.substring(0,1).toLowerCase()+model.substring(1,model.length())+"Action\")");
			actionbw.write("\r\n");
			actionbw.write("@Scope(\"prototype\")");
			actionbw.write("\r\n");
			actionbw.write("public class "+model+"Action extends ActionSupport {");
			actionbw.write("\r\n");
			actionbw.write("	@Resource(name=\""+model.substring(0,1).toLowerCase()+model.substring(1,model.length())+"Service\")");
			actionbw.write("\r\n");
			actionbw.write("	private "+model+"Service "+model.substring(0,1).toLowerCase()+model.substring(1,model.length())+"Service;");
			actionbw.write("\r\n");
			actionbw.write("}");
			
			
			actionbw.flush();
			actionbw.close();
			
			//ehcache
			File ehcacheFile = new File(root +"ehcache.xml");
			if (!ehcacheFile.getParentFile().exists()) {
				ehcacheFile.getParentFile().mkdirs();
			}
			if(!ehcacheFile.exists())
			{
				ehcacheFile.createNewFile();
			}
//			BufferedWriter ehcachebw = new BufferedWriter(new OutputStreamWriter(  
//					new FileOutputStream(ehcacheFile), "UTF-8"));  
			  FileWriter ehcachebw = new FileWriter(ehcacheFile, true);
			ehcachebw.write("   	<cache name=\"com.parkbobo.model."+model+"\"");
			ehcachebw.write("\r\n");
			ehcachebw.write("		maxElementsInMemory=\"1000\"");
			ehcachebw.write("\r\n");
			ehcachebw.write("		eternal=\"false\"");
			ehcachebw.write("\r\n");
			ehcachebw.write("		timeToIdleSeconds=\"100\"");
			ehcachebw.write("\r\n");
			ehcachebw.write("		timeToLiveSeconds=\"4200\"");
			ehcachebw.write("\r\n");
			ehcachebw.write("		overflowToDisk=\"true\"");
			ehcachebw.write("\r\n");
			ehcachebw.write("   	/>");
			ehcachebw.write("\r\n");
			ehcachebw.flush();
			ehcachebw.close();
			
		}
	}
}
