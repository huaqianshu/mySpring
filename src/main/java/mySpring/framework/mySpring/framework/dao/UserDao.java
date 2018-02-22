package mySpring.framework.mySpring.framework.dao;

import mySpring.framework.mySpring.framework.model.User;

public interface UserDao {
	public int saveUser(User user);
	public int checkUser(String mobile);
}
