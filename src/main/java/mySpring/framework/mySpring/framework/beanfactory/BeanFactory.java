package mySpring.framework.mySpring.framework.beanfactory;

public interface BeanFactory {
	Object getBean(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException;
}
