package mySpring.framework.mySpring.framework.context;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mySpring.framework.mySpring.framework.annotation.Component;
import mySpring.framework.mySpring.framework.annotation.Controller;
import mySpring.framework.mySpring.framework.annotation.Dao;
import mySpring.framework.mySpring.framework.annotation.MyAgent;
import mySpring.framework.mySpring.framework.annotation.RequestMapping;
import mySpring.framework.mySpring.framework.annotation.Service;
import mySpring.framework.mySpring.framework.beanfactory.BeanFactory;

public class MyContext implements BeanFactory {
	private final static Map<String, String> classNameContext = new HashMap<>();//key=路径，value=类名
	private static Map<String, Object> spaceNameContext = new HashMap<>();//命名的类
	private final Map<String, Object> beanContext = new HashMap<>();//bean的map
	private final static Map<String, String> methodNameContext = new HashMap<>();//key=路径，value=方法名
	private final String requestObj = "javax.servlet.http.HttpServletRequest";//特殊的类
	// public void init(){
	// try {
	// File f = new File("spring.xml");
	// List<Element> list = new ArrayList<>();
	// SAXReader reader = new SAXReader();
	// Document doc = reader.read(f);
	// Element root = doc.getRootElement();
	// list = root.elements("service");
	// list.addAll(root.elements("dao"));
	// Iterator<Element> it =list.iterator();
	// while(it.hasNext()){
	// Element service = it.next();
	// String packageName = service.attributeValue("package");
	// List<Class<?>> classes = getAllClasses(packageName);
	// for(Class clazz:classes){
	// if(clazz.isInterface())
	// continue;
	// Object obj = clazz.newInstance();
	// classNameContext.put(clazz.getName(),obj );
	// Annotation annotation = clazz.getAnnotation(Service.class);
	// String spaceName="";
	// if(annotation!=null){
	// Service serviceAnnotation = (Service) annotation;
	// spaceName = serviceAnnotation.value();
	// if(spaceName.isEmpty()){
	// spaceName = clazz.getName();
	// }
	// }
	// annotation = clazz.getAnnotation(Dao.class);
	// if(annotation!=null){
	// Dao serviceAnnotation = (Dao) annotation;
	// spaceName = serviceAnnotation.value();
	// if(spaceName.isEmpty()){
	// spaceName = clazz.getName();
	// }
	// }
	// spaceNameContext.put(spaceName, obj);
	// }
	// }
	// } catch (DocumentException | ClassNotFoundException | IOException |
	// InstantiationException | IllegalAccessException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	//
	// }

	// private List<Class<?>> getAllClasses(String packageName) throws
	// IOException, ClassNotFoundException{
	// List<Class<?>> classes = new ArrayList<Class<?>>();
	// String packageDirName = packageName.replace('.', '/');
	// Enumeration<URL> dirs =
	// Thread.currentThread().getContextClassLoader().getResources(packageDirName);
	// while(dirs.hasMoreElements()){
	// URL url = dirs.nextElement();
	// String protocol = url.getProtocol();
	// if("file".equals(protocol)){
	// String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
	// getClassFromPackage(packageName,classes,filePath);
	// }
	// }
	//
	// return classes;
	// }
	// private void getClassFromPackage(String packageName,List<Class<?>>
	// classes,String filePath) throws UnsupportedEncodingException,
	// ClassNotFoundException{
	//
	// File dir = new File(filePath);
	// File[] dirfiles = dir.listFiles(new FileFilter() {
	// //自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
	// public boolean accept(File file) {
	// return (file.isDirectory()) || (file.getName().endsWith(".class"));
	// }
	// });
	// for(File file:dirfiles){
	// if(file.isDirectory()){
	//
	// getClassFromPackage(packageName + "." +
	// file.getName(),classes,file.getAbsolutePath());
	// }else{
	// String className = file.getName().substring(0, file.getName().length() -
	// 6);
	// classes.add(Class.forName(packageName + '.' + className));
	// }
	// }
	// }
	public static void manageClass(String classname)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class clazz = Class.forName(classname);
		if (clazz.isInterface())
			return;
		Annotation[] annotations = clazz.getAnnotations();
		Object obj = null;
		// 不管类中属性是否已经完整，在扫描后的第一步，先将类创建出来
		for (Annotation annotation : annotations) {
			String className = "";
			if (annotation instanceof Component)
				className = ((Component) annotation).value();
			else if (annotation instanceof Controller)
				className = "";
			else if (annotation instanceof Dao)
				className = ((Dao) annotation).value();
			else if (annotation instanceof Service)
				className = ((Service) annotation).value();
			obj = clazz.newInstance();//创建类
			RequestMapping requestMapping = obj.getClass().getAnnotation(RequestMapping.class);//地址
			if (requestMapping != null) {
				//整理地址
				String classvalue = requestMapping.value();
				if (!classvalue.endsWith("/"))
					classvalue += "/";
				if (!classvalue.startsWith("/"))
					classvalue = "/" + classvalue;
				Method[] methods = obj.getClass().getDeclaredMethods();
				for (Method method : methods) {
					RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
					if (methodRequestMapping != null) {
						String methodvalue = methodRequestMapping.value();
						if (methodvalue.startsWith("/"))
							methodvalue = methodvalue.substring(1, methodvalue.length());
						classNameContext.put(classvalue + methodvalue, classname);
						methodNameContext.put(classvalue + methodvalue, method.getName());
					}
				}

			}
			if (className.isEmpty())
				className = clazz.getName();
			spaceNameContext.put(className, obj);//写入类，如果没有命名，采用类名，此处可加入类所实现的所有接口及类所继承的所有类名
		}
	}
	//循环查找，从最底层开始注入
	public Object getBean(String beanName)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Object obj = beanContext.get(beanName);
		if (obj == null)
			obj = spaceNameContext.get(beanName);
		else//已经注入完成
			return obj;
		if (obj != null) {
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields) {
				MyAgent annotation = field.getAnnotation(MyAgent.class);
				String classsspaceName = "";
				if (annotation != null) {
					field.setAccessible(true);
					classsspaceName = annotation.value();
					if (classsspaceName.isEmpty()) {
						classsspaceName = field.getType().getName();
					}
					if (classsspaceName.equals(requestObj)) {
						field.set(obj, new SpecialContext().getBean(classsspaceName));
						continue;
					} else if (spaceNameContext.get(classsspaceName) == null) {//未曾写入spaceNameContext
						manageClass(field.getType().getName());
					}
					field.set(obj, getBean(classsspaceName));
				}
			}
			spaceNameContext.put(beanName, obj);
			return obj;
		}
		throw new RuntimeException("no such bean" + beanName);
	}

	public String getBeanName(String uri) {
		return classNameContext.get(uri);
	}

	public String getMethodName(String uri) {
		return methodNameContext.get(uri);
	}
}
