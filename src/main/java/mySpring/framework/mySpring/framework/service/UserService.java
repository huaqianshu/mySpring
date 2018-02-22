package mySpring.framework.mySpring.framework.service;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
	public int saveUser(HttpServletRequest req);
	
	public int checkUser(HttpServletRequest req);
}
