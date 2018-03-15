package com.parkbobo.utils;
import java.io.File;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
/**
 * 压缩ZIP
 * @author LH
 *
 */
public class ZipUtils {
	private static ZipUtils zipUtils;
	public synchronized static ZipUtils getDefaultInstance(){
		if(zipUtils==null){
			zipUtils = new ZipUtils();
		}
		return zipUtils;
	}
	/**
	 * 将单个文件压缩成zip
	 * @param file
	 * @param zipFile
	 */
	public void compress(File file, File zipFile) {
		Project prj = new Project();
		Zip zip = new Zip();
		zip.setProject(prj);
		zip.setDestFile(zipFile);
		FileSet fileSet = new FileSet();
		fileSet.setProject(prj);
		fileSet.setFile(file);
		zip.addFileset(fileSet);
		zip.execute();
	}
}