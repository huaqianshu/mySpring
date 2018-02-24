package mySpring.framework.mySpring.framework.context;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mySpring.framework.mySpring.framework.annotation.Controller;
import mySpring.framework.mySpring.framework.annotation.Dao;
import mySpring.framework.mySpring.framework.annotation.MyAgent;
import mySpring.framework.mySpring.framework.annotation.Service;


public class MyContext {
	private Map<String,Object> classNameContext=new HashMap<>();
	private Map<String,Object> spaceNameContext=new HashMap<>();
	private Map<String,Object> beanContext = new HashMap<>();
//	public void init(){
//		try {
//			File f = new File("spring.xml");
//			List<Element> list = new ArrayList<>();
//			SAXReader reader = new SAXReader(); 
//			Document doc = reader.read(f);
//			Element root = doc.getRootElement();
//			list = root.elements("service");
//			list.addAll(root.elements("dao"));
//			Iterator<Element> it =list.iterator();
//			while(it.hasNext()){
//				Element service = it.next();
//				String packageName = service.attributeValue("package");
//				List<Class<?>> classes = getAllClasses(packageName);
//				for(Class clazz:classes){
//					if(clazz.isInterface())
//						continue;
//					Object obj = clazz.newInstance();
//					classNameContext.put(clazz.getName(),obj );
//					Annotation annotation = clazz.getAnnotation(Service.class);
//					String spaceName="";
//					if(annotation!=null){
//						Service serviceAnnotation = (Service) annotation;
//						spaceName = serviceAnnotation.value();
//						if(spaceName.isEmpty()){
//							spaceName = clazz.getName();
//						}
//					}
//					annotation = clazz.getAnnotation(Dao.class);
//					if(annotation!=null){
//						Dao serviceAnnotation = (Dao) annotation;
//						spaceName = serviceAnnotation.value();
//						if(spaceName.isEmpty()){
//							spaceName = clazz.getName();
//						}
//					}
//					spaceNameContext.put(spaceName, obj);
//				}
//			}
//		} catch (DocumentException | ClassNotFoundException | IOException | InstantiationException | IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}
	
//	private List<Class<?>> getAllClasses(String packageName) throws IOException, ClassNotFoundException{
//		List<Class<?>> classes = new ArrayList<Class<?>>(); 
//        String packageDirName = packageName.replace('.', '/');  
//        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);  
//		while(dirs.hasMoreElements()){
//			URL url = dirs.nextElement();
//			String protocol = url.getProtocol();
//			if("file".equals(protocol)){
//				String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
//				getClassFromPackage(packageName,classes,filePath);
//			}
//		}
//		
//		return classes;
//	}
//	private void getClassFromPackage(String packageName,List<Class<?>> classes,String filePath) throws UnsupportedEncodingException, ClassNotFoundException{
//		
//		File dir = new File(filePath);
//		File[] dirfiles = dir.listFiles(new FileFilter() {  
//	        //自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)  
//	              public boolean accept(File file) {  
//	                return (file.isDirectory()) || (file.getName().endsWith(".class"));  
//	              }  
//	            });
//		for(File file:dirfiles){
//			if(file.isDirectory()){
//				
//				getClassFromPackage(packageName + "." + file.getName(),classes,file.getAbsolutePath());
//			}else{
//				String className = file.getName().substring(0, file.getName().length() - 6); 
//				classes.add(Class.forName(packageName + '.' + className));  
//			}
//		}
//	}
	public void manageClass(Class clazz) throws InstantiationException, IllegalAccessException{
		if(clazz.isInterface())
			return;
		Annotation[] annotations = clazz.getAnnotations();
		Object obj=null;
		for(Annotation annotation:annotations){
			if(annotation instanceof Controller||annotation instanceof Service||annotation instanceof Dao){
				obj = clazz.newInstance();
				spaceNameContext.put(clazz.getName(), obj);
			}
		}
		
	}
	public Object getBean(String beanName) throws InstantiationException, IllegalAccessException{
		Object obj = beanContext.get(beanName);
		if(obj == null)
			obj = spaceNameContext.get(beanName);
		else
			return obj;
		if(obj!=null){
			Field[] fields =  obj.getClass().getDeclaredFields();   
			for(Field field:fields){
				field.setAccessible(true);
				MyAgent annotation = field.getAnnotation(MyAgent.class);
				String classsspaceName =""; 
				if(annotation!=null){
					classsspaceName = annotation.value();
					if(classsspaceName.isEmpty()){
						classsspaceName = field.getType().getName();
					}
					if(spaceNameContext.get(classsspaceName)==null){
						manageClass(field.getType());
					}
					field.set(obj, getBean(classsspaceName));
				}
			}
			spaceNameContext.put(beanName, obj);
			return obj;
		}
		return null;//throw...
	}
}
