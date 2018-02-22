package mySpring.framework.mySpring.framework.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mySpring.framework.mySpring.framework.annotation.Dao;
import mySpring.framework.mySpring.framework.dao.UserDao;
import mySpring.framework.mySpring.framework.model.User;
@Dao("userDao")
public class UserDaoImpl implements UserDao{

	private static final String URL = "jdbc:mysql://127.0.0.1:3306/imooc?useUnicode=true&amp;characterEncoding=utf-8";
	private static final String USER = "root";
	private static final String PASSWORD = "tiger";
	private static Connection conn = null;
	static {
		try {
			// 1.加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			// 2.获得数据库的连接
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int saveUser(User user){
		String s=""+"insert into user(id,user_name,user_idType,user_idcard,id_mobile) values(@@IDENTITY,?,?,?,?)";
		try {
		PreparedStatement pst=conn.prepareStatement(s);
		pst.setString(1, user.getName());
		pst.setString(2, user.getIdType());
			pst.setString(3, user.getIdcard());
		pst.setString(4, user.getMobile());
		pst.execute();   
		ResultSet rs = pst.getGeneratedKeys(); 
		pst.close();
		conn.close();
		int id = 0; 
        if (rs.next()) 
            id = rs.getInt(1); 
        return id; 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int checkUser(String mobile) {
		String s=""+"select id from user where user.mobile="+mobile;
		try {
			PreparedStatement pst=conn.prepareStatement(s);
			ResultSet rs = pst.executeQuery();
			pst.close();
			conn.close();
			int id = 0; 
	        if (rs.next()) 
	            id =rs.getInt("id");
	        return id; 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
	}
}
