package mySpring.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mySpring.framework.mySpring.framework.context.MyContext;
import mySpring.framework.mySpring.framework.scan.Scanner;


public class LoginServlet extends HttpServlet{
	private MyContext myContext;
	@Override
	public void init() throws ServletException {
		Scanner scanner = new Scanner();
		scanner.scanner();
		myContext = new MyContext();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();
		if(uri.endsWith(".do")){
			uri = uri.substring(0, uri.length()-3);
			try {
				Object obj = myContext.getBean(myContext.getBeanName(uri));
				String methodString = myContext.getMethodName(uri);
				Method method = obj.getClass().getMethod(methodString);
				Object res = method.invoke(obj, req);
				String result=null;
				if(res instanceof String)
					result = (String)res;
				PrintWriter out = resp.getWriter();
				out.print(result);
				out.flush();
				out.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req,resp);
	}
	
	
	
	
}
