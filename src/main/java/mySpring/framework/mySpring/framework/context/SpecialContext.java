package mySpring.framework.mySpring.framework.context;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import mySpring.framework.mySpring.framework.beanfactory.BeanFactory;

public class SpecialContext implements BeanFactory{
	private final static Map<String,Object> specialContext=new HashMap<>();
	@Override
	public Object getBean(String className)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return specialContext.get(className);
	}
	public static void manageClass(HttpServletRequest req){
		specialContext.put(HttpServletRequest.class.getName(), req);
	}
}
