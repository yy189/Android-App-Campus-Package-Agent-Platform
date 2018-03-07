package com.server.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.CMessage;
import entity.Order;
import entity.User;

public class UserDAOUtil {
	public static List<User> ResultSet2List(ResultSet rs) throws SQLException {
		List<User> users = new ArrayList<User>();
		while(rs.next())
		{				
			User u = new User();
			u.setStudentid(rs.getString("studentid"));
			u.setUsername(rs.getString("username"));
			u.setPassword(rs.getString("password"));
			u.setDepartment(rs.getString("department"));
			u.setDormitory(rs.getString("dormitory"));
			u.setPoint(rs.getInt("points"));
			users.add(u);
		}	
		return users;
	}
	
	public static List<String> ResultSet2FriendIds(ResultSet rs) throws SQLException {
		List<String> friends = new ArrayList<String>();
		while(rs.next())
		{
			String friendId = new String();
			friendId = rs.getString(3);
			friends.add(friendId);
		}
		return friends;
	}
	
	public static List<Order> ResultSet2Orders(ResultSet rs) throws SQLException {
		List<Order> orders = new ArrayList<Order>();
		while(rs.next())
		{
			Order order = new Order();
			order.setOrderId(rs.getInt(1));
			order.setPublishUserId(rs.getString(2));
			order.setReceiveUserId(rs.getString(3));
			order.setExpressDescribe(rs.getString(4));
			if(rs.getString(5).equals("F"))
			{
				order.setAccepted(false);
			}
			else if(rs.getString(5).equals("T"))
			{
				order.setAccepted(true);
			}
			if(rs.getString(6).equals("F"))
			{
				order.setReceived(false);
			}
			else if(rs.getString(6).equals("T"))
			{
				order.setReceived(true);
			}
			order.setPublishDate(rs.getString(7));
			order.setPublishName(rs.getString(8));
			orders.add(order);
		}
		return orders;
	}
	
	public static List<CMessage> ResultSetChatRecords(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<CMessage> listmsg = new ArrayList<CMessage>();
		while(rs.next())
		{		
			User u1 = new User();
			User u2 = new User();
			CMessage message = new CMessage();
			u1.setStudentid(rs.getString(1));
			message.setSender(u1);
			message.setObj((Object)rs.getString(2));
			message.setTime(rs.getString(3));
			u2.setStudentid(rs.getString(4));
			message.setReceiver(u2);	
			listmsg.add(message);
		}
		return listmsg;
	}
	
	public static User ResultUser(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		User u = new User();
		while(rs.next())
		{	
			u.setStudentid(rs.getString(1));
			u.setUsername(rs.getString(2));
			u.setDepartment(rs.getString(4));
			u.setDormitory(rs.getString(5));
		}
		return u;
	}
}
