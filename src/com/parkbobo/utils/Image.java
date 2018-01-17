package com.parkbobo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Image {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Connection con = null;
		try {
		    Class.forName("org.postgresql.Driver");
		    con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bobopark", "bobopark", "bobopark");
		
			Statement stmt=con.createStatement();
			String sql = "select carparkid,picarr,name,address from lq_carpark  order by carparkid";
			ResultSet rs = stmt.executeQuery(sql);
			List<String> ids = new ArrayList<String>();
			
			while(rs.next()){
				/****/	Long id = rs.getLong("carparkid");
				String img = rs.getString("picarr");
				String n = rs.getString("name");
				String a = rs.getString("address");
//	            if( a != null && (a.contains("\r") || a.contains("\n")|| a.contains("\t")|| a.contains("\\s*")))
//	            {
//	            	System.out.println("存在制表符:" + id + "--" + a);
//	            }
//	            if( n != null && (n.contains("\r") || n.contains("\n")|| n.contains("\t")|| n.contains("\\s*")))
//	            {
//	            	System.out.println("存在制表符:" + id + "--" + n);
//	            }
				if(img != null && !img.equals(""))
				{
					String name = img.substring(img.lastIndexOf("/") + 1, img.length());
					if(!name.substring(name.length() - 4, name.length()).equalsIgnoreCase(".jpg"))
					{
						name +=".jpg";
					}
					File f = new File("F:\\cjImage\\1114\\"+ name);
					
					if(f.exists())
					{
//						System.out.println(id+"---"+n+"------"+name);
						   FileOutputStream fos = null;
					       FileInputStream fis = null;
					       String newname = UUID.randomUUID().toString().replace("-", "") + ".jpg";
					       String t = "";
					       if(id < 20000)
					       {
					    	    t = "3";
					       }
					       else
					       {
					    	    t = "3";
					       }
					       File saveDirFile = new File("F:\\cjImage\\new\\" + t +"\\");
				    		if (!saveDirFile.exists()) 
				    		{
				    			saveDirFile.setWritable(true, false); //设置文件夹权限，避免在Linux下不能写入文件
				    			saveDirFile.mkdirs();
				    		}
				    		
				    		fos = new FileOutputStream("F:\\cjImage\\new\\" +t+"\\"+ newname);
							fis = new FileInputStream(f);
							byte[] buffer = new byte[1024];
				            int len = 0;
				            while ((len = fis.read(buffer)) != -1) {
				                fos.write(buffer, 0, len);
				            }
				            sql = "update lq_carpark set picarr ='images/"+t+"/" + newname + "' where carparkid = " + id;
							
							Statement stmt1=con.createStatement();
							stmt1.executeUpdate(sql);
				            
				            
				            if (fis != null) {
				                try {
				                    fis.close();
				                    fis=null;
				                } catch (IOException e) {
				                    System.out.println();
				                    MyPrintUtil.getDefaultInstance().out("FileInputStream关闭失败");
				                    e.printStackTrace();
				                }
				            }
				            if (fos != null) {
				                try {
				                    fos.close();
				                    fis=null;
				                } catch (IOException e) {
				                    MyPrintUtil.getDefaultInstance().out("FileOutputStream关闭失败");
				                    e.printStackTrace();
				                }
				            }
//						ids.add(id+"---"+n+"------"+name);
					}
				}
			}
			for (String l : ids) {
				//System.out.println(l);
			}
//			File f1 = new File("F:\\cjImage\\1\\2014820富力中心P口.jpg");
//			System.out.println(f1.exists());
			rs.close();
			stmt.close();
			con.close(); 
		}catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}catch (SQLException e) {
		    e.printStackTrace();
		}
	}

}
