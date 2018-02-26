package mySpring.framework.mySpring.framework.scan;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import mySpring.framework.mySpring.framework.context.MyContext;

public class Scanner {

	public void scanner(){
		try {
			scannerFile(System.getProperty("user.dir"));
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void scannerFile(String path) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		File file = new File(path);
		if(file.isDirectory()){
			String[] filelist = file.list();
			for(String filename:filelist)
				scannerFile(path+"\\"+filename);
		}else if(file.getName().endsWith(".java")){
			String absfilename = file.getAbsolutePath();
			String filename = absfilename.substring(absfilename.indexOf(System.getProperty("user.dir"))+System.getProperty("user.dir").length()+1,absfilename.length());
			filename = filename.replaceAll("\\\\", ".");
			String classname =filename.substring(0,filename.length()-5)+".class"; 
			MyContext.manageClass(classname);
		}
	}
}
