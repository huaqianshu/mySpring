package mySpring.framework.mySpring.framework.scan;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import mySpring.framework.mySpring.framework.annotation.MySpringApplication;
import mySpring.framework.mySpring.framework.context.MyContext;

public class Scanner {
	private ClassLoader classLoader = getClass().getClassLoader();
	
	
	public void scanner(){
		try {
			System.out.println(this.getClass().getResource("/").getPath());
			System.out.println(this.getClass().getResource("").getPath());
			String thispath = this.getClass().getResource("").getPath().replace(this.getClass().getResource("/").getPath(), "");
			System.out.println(thispath.substring(0,thispath.indexOf("/")));
			scannerFile(thispath.substring(0,thispath.indexOf("/")));
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void scannerFile(String path) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException{
		path = path.replace(".", "/"); 
		Enumeration<URL> urls = classLoader.getResources(path);
		
		while(urls.hasMoreElements()){
			URL url = urls.nextElement();
			String protocol = url.getProtocol();
			if("file".equals(protocol)){
				String file = URLDecoder.decode(url.getFile(), "UTF-8");
                File dir = new File(file);
                if(dir.isDirectory()){
                	scanner(dir);
                }
			}else{
				System.err.println(protocol);
			}
		}
	}
	private void scanner(File file) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(File everyfile:files)
				scanner(everyfile);
		}else if(file.getName().endsWith(".class")){
			String classname = file.getPath();
			classname = classname.substring(classname.indexOf("classes")+8).replace("\\", ".");
			classname = classname.substring(0, classname.length()-6);
			MyContext.manageClass(classname);
		}
	}
}
