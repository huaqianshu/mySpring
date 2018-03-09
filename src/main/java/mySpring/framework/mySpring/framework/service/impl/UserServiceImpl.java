package mySpring.framework.mySpring.framework.service.impl;

import javax.servlet.http.HttpServletRequest;

import mySpring.framework.mySpring.framework.annotation.MyAgent;
import mySpring.framework.mySpring.framework.annotation.Service;
import mySpring.framework.mySpring.framework.dao.UserDao;
import mySpring.framework.mySpring.framework.model.User;
import mySpring.framework.mySpring.framework.service.UserService;
@Service("serServiceImpl")
public class UserServiceImpl implements UserService {
	@MyAgent("userDao")
	private UserDao userDao;
	
	@Override
	public int saveUser(HttpServletRequest req) {
		User user = new User();
		user.setIdcard(req.getParameter("username"));
		user.setIdType(req.getParameter("username"));
		user.setMobile(req.getParameter("username"));
		user.setName(req.getParameter("username"));
		//return userDao.saveUser(user);
		return 0;
	}

	@Override
	public int checkUser(HttpServletRequest req) {
		String mobile = (String) req.getAttribute("username");
		//return userDao.checkUser(mobile);
		return 1;
	}

}
