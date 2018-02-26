package mySpring.framework.mySpring.framework.rest;

import javax.servlet.http.HttpServletRequest;


import mySpring.framework.mySpring.framework.annotation.Controller;
import mySpring.framework.mySpring.framework.annotation.MyAgent;
import mySpring.framework.mySpring.framework.annotation.RequestMapping;
import mySpring.framework.mySpring.framework.service.UserService;

@Controller
@RequestMapping("login")
public class LoginController {
	@MyAgent("serServiceImpl")
	private UserService userService;
	
	@MyAgent
	private HttpServletRequest request;
	@RequestMapping("/signin")
	public String signin(){
		return userService.saveUser(request)==0?"true":"false";
	}
	@RequestMapping("/checkin")
	public String checkin(){
		return userService.checkUser(request)==0?"true":"false";
	}
}
