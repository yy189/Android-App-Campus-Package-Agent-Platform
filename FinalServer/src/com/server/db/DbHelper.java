package com.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.server.util.UserDAOUtil;
import com.server.util.UserFriendUtil;

import entity.CMessage;
import entity.MessageType;
import entity.Order;
import entity.User;

public class DbHelper {
	static String drivername = "com.mysql.jdbc.Driver";
	static String url = "jdbc:mysql://localhost:3306/expressdb";
	static String username = "root";
	static String password = "";
	static
	{
		try
		{
			Class.forName(drivername);//创建驱动
			System.out.println("创建驱动成功！");
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection()
	{
		Connection conn = null;
		try
		{
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("连接数据库成功！");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void free(ResultSet rs,Connection conn, Statement stmt)
	{
		try
		{
			if(rs != null)
				rs.close();//关闭结果集
		}
		catch(SQLException e)
		{
			System.out.println("关闭ResultSet失败！");
			e.printStackTrace();
		}
		finally{
			try
			{
				if (conn != null)
					conn.close();//关闭连接
			}
			catch(SQLException e)
			{
				System.out.println("关闭Connection失败！");
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(stmt != null)
						stmt.close();
				}
				catch(SQLException e)
				{
					System.out.println("关闭Statement失败！");
					e.printStackTrace();
				}
			}
		}
	}
		
	//执行查询语句
	public List<User> sqlQuery(String sql,String []paras,String messageType)
	{
		List<User> users = null;
		User user = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbHelper.getConnection();
			pstmt=conn.prepareStatement(sql);
			//给pstmt的问号赋值
			for(int i=0;i<paras.length;i++){
				pstmt.setString(i+1, paras[i]);
			}
			//执行操作
			rs=pstmt.executeQuery();
			//users = UserDAOUtil.ResultSet2List(rs);
			
			if(messageType == MessageType.LOGIN || messageType == MessageType.REGISTER 
					||messageType == MessageType.SECONDQUERY ||messageType==MessageType.CHANGEPW){
				users = UserDAOUtil.ResultSet2List(rs);	
			}else if(messageType == MessageType.MYFRIEND){
				users = UserFriendUtil.getMyFriends(rs);
			}
					
		} catch (Exception e) {		
			e.printStackTrace();
		}finally{
			//关闭资源
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return users;
	}
	
	public List<Order> sqlQueryRecord(String sql,String []paras)
	{
		List<Order> publish = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbHelper.getConnection();
			pstmt=conn.prepareStatement(sql);
			for(int i=0;i<paras.length;i++){
				pstmt.setString(i+1, paras[i]);
			}
			rs=pstmt.executeQuery();
			
		    publish = UserFriendUtil.getUserRecord(rs);
					
		} catch (Exception e) {		
			e.printStackTrace();
		}finally{
			//关闭资源
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return publish;
	}
	
	public List<String> sqlQueryFriends(String sql,String []paras)
	{
		List<String> friends = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbHelper.getConnection();
			pstmt=conn.prepareStatement(sql);
			//给pstmt的问号赋值
			for(int i=0;i<paras.length;i++){
				pstmt.setString(i+1, paras[i]);
			}
			//执行操作
			rs=pstmt.executeQuery();
			friends = UserDAOUtil.ResultSet2FriendIds(rs);			
		} catch (Exception e) {		
			e.printStackTrace();
		}finally{
			//关闭资源
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return friends;
	}
	
	public List<Order> sqlQueryOrders(String sql,String []paras)
	{
		List<Order> orders = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbHelper.getConnection();
			pstmt=conn.prepareStatement(sql);
			//给pstmt的问号赋值
			for(int i=0;i<paras.length;i++){
				pstmt.setString(i+1, paras[i]);
			}
			//执行操作
			rs=pstmt.executeQuery();
			orders = UserDAOUtil.ResultSet2Orders(rs);			
		} catch (Exception e) {		
			e.printStackTrace();
		}finally{
			//关闭资源
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return orders;
	}
	
	public List<CMessage> sqlQueryChatRecords(String sql, String[] paras) {
		// TODO Auto-generated method stub
		List<CMessage> listmsg = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbHelper.getConnection();
			pstmt=conn.prepareStatement(sql);
			//给pstmt的问号赋值
			for(int i=0;i<paras.length;i++){
				pstmt.setString(i+1, paras[i]);
			}
			//执行操作
			rs=pstmt.executeQuery();
			listmsg = UserDAOUtil.ResultSetChatRecords(rs);	
		} catch (Exception e) {		
			e.printStackTrace();
		}finally{
			//关闭资源
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return listmsg;
	}
	
	public static int executeUpdate(String sql,Object[]params)
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int rows=-1;
		try
		{
			conn = DbHelper.getConnection();
			pstmt = conn.prepareStatement(sql);
			//给pstmt的问号赋值
			for(int i=0;i<params.length;i++)
			{
				pstmt.setObject(i+1, params[i]);
			}
			rows = pstmt.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			System.out.println("使用预编译语句更新数据操作发生连接异常");
		}finally{
			//关闭资源
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return rows;
	}
	

	public User sqlQuery(String sql, String[]params) {
		// TODO Auto-generated method stub
		User user = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbHelper.getConnection();
			pstmt=conn.prepareStatement(sql);
			//给pstmt的问号赋值
			for(int i=0;i<params.length;i++){
				pstmt.setString(i+1, params[i]);
			}
			//执行操作
			rs=pstmt.executeQuery();
			user = UserDAOUtil.ResultUser(rs);	
		} catch (Exception e) {		
			e.printStackTrace();
		}finally{
			//关闭资源
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return user;
	}
}
