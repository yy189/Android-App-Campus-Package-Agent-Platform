package com.server.dao;

import java.util.List;

import com.server.db.DbHelper;

import entity.CMessage;
import entity.Order;
import entity.User;

public class UserDAO {
	public static User getUser(User user) {
		// TODO Auto-generated method stub
		String sql = "select * from userinfo where studentid=? and password=?";
		String[] paras = new String[2];
		paras[0] = user.getStudentid();
		paras[1] = user.getPassword();
		String messageType = "LOGIN";
		
		//执行查询操作
		List<User> users = new DbHelper().sqlQuery(sql, paras,messageType);
		if (users != null && users.size() > 0) {
			return users.get(0);
		} else
			return null;
	}
	
	public static List<String> getFriends(User user)
	{
		String sql = "select * from friend where studentid =?";
		String[] paras = new String[1];
		paras[0] = user.getStudentid();
		List<String> friends = new DbHelper().sqlQueryFriends(sql, paras);
		if (friends != null && friends.size() > 0) 
		{
			return friends;
		}else
			return null;	
	}
	
	public static List<Order> getOrders(String studentid)
	{
		String sql = "select * from expressorder where publisher =?";
		String[] paras = new String[1];
		paras[0] = studentid;
		List<Order> orders = new DbHelper().sqlQueryOrders(sql, paras);
		if (orders != null && orders.size() > 0) 
		{
			return orders;
			
		}else
		{
			return null;
		}
	}
	
	public static List<User> getNewFriends(User user)
	{
		String studentId = user.getStudentid();
		System.out.println(studentId);
		studentId = studentId.substring(0, 5);//比如09013404搜studentid为09013开头的同学
		System.out.println(studentId);
		String sql = "select * from userinfo where studentid like '%"+studentId+"%'";
		String[] paras = new String[0];
        String messageType = "LOGIN";
		
		//执行查询操作
		List<User> newFriends = new DbHelper().sqlQuery(sql, paras,messageType);
		if (newFriends != null && newFriends.size() > 0) 
		{
			return newFriends;
			
		}else
		{
			return null;
		}
	}
	
	public static List<CMessage> loadChatRecords(User sender, User receiver) {
		// TODO Auto-generated method stub
		String sql = "select * from chatrecords where (senderid=? AND receiverid=?)OR (senderid=? AND receiverid=?)";
		String[] paras = new String[4];
		paras[0] = sender.getStudentid();
		paras[1] = receiver.getStudentid();
		paras[2] = receiver.getStudentid();
		paras[3] = sender.getStudentid();
		List<CMessage> listmsgs = new DbHelper().sqlQueryChatRecords(sql, paras);
				
		if (listmsgs != null && listmsgs.size() > 0) 
		{
			return listmsgs;
			
		}else
		{
			return null;
		}
	}

	public static Boolean insertChatRecord(CMessage obj) {
		// TODO Auto-generated method stub
		String sql = "Insert into chatrecords (senderid,content,time,receiverid) values(?,?,?,?)";
		String[] paras2 = new String[4];
		paras2[0] = obj.getSender().getStudentid();
		paras2[1] = (String) obj.getObj();
		paras2[2] = obj.getTime();	
		paras2[3] = obj.getReceiver().getStudentid();
		//paras2[4] = user.getDormitory();
		new DbHelper();
		Boolean flag = false; 
		int row = DbHelper.executeUpdate(sql, paras2);
		if(row>0)
			flag = true;	
		
		return flag;
	}
	public static User getFindPWUser(User user) {
		// TODO Auto-generated method stub
		String sql = "select * from userinfo where studentid=? and dormitory=?";
		String[] paras = new String[2];
		paras[0] = user.getStudentid();
		paras[1] = user.getDormitory();
		String messageType = "LOGIN";
		
		//执行查询操作
		List<User> users = new DbHelper().sqlQuery(sql, paras, messageType);
		//List<User> users = new DbHelper().sqlQuery(sql, paras);
		if (users != null && users.size() > 0) {
			return users.get(0);
		} else
			return null;
	}
	public static List<CMessage> loadChaterList(User obj) {
		// TODO Auto-generated method stub
		String sql = "select * from chatrecords where senderid=? OR receiverid=?";
		String[] paras = new String[2];
		paras[0] = obj.getStudentid();
		paras[1] = obj.getStudentid();
		List<CMessage> listmsgs = new DbHelper().sqlQueryChatRecords(sql, paras);
				
		if (listmsgs != null && listmsgs.size() > 0) 
		{
			return listmsgs;
			
		}else
		{
			return null;
		}
	}
	
	public static User getTheUser(User obj) {
		// TODO Auto-generated method stub
		String sql = "select * from userinfo where studentid=?";
		String[] paras = new String[1];
		paras[0] = obj.getStudentid();
	
		//执行查询操作
		User user = new DbHelper().sqlQuery(sql, paras);
		//List<User> users = new DbHelper().sqlQuery(sql, paras);
		if (user != null ) {
			return user;
		} else
			return null;
	}
	
}

