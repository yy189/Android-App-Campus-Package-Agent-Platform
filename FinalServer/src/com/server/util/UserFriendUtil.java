package com.server.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.server.db.DbHelper;

import entity.Order;
import entity.User;

public class UserFriendUtil {
	
	public static List<User> getMyFriends(ResultSet rs) throws SQLException{
		List<User> users = new ArrayList<User>();
		String messageType = "SECONDQUERY";
		
		//rs为user的好友集合，查询每个好友的userinfo表返回信息
		while(rs.next()){
			try {
				String sql = "select * from userinfo where studentid=?";
				String[] paras = new String[1];
				paras[0] = rs.getString("friendid");			
				//将每个好用的基本信息存在users中
				List<User> us = new DbHelper().sqlQuery(sql, paras,messageType);
				users.addAll(us);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return users;
	}
	
	//获取我发布的快递信息
	public static List<Order> getUserRecord(ResultSet rs) throws SQLException{
		List<Order> receive = new ArrayList<Order>();
		
		while(rs.next()){
			try {
				Order order = new Order();
				order.setOrderId(rs.getInt(1));
				order.setPublishUserId(rs.getString(2));
				order.setReceiveUserId(rs.getString(3));
				order.setExpressDescribe(rs.getString(4));
				if(rs.getString(5).equals("F")){
					order.setAccepted(false);
				}
				if(rs.getString(5).equals("T")){
					order.setAccepted(true);
				}
				if(rs.getString(6).equals("F")){
					order.setReceived(false);
				}
				if(rs.getString(6).equals("T")){
					order.setReceived(true);
				}
				order.setPublishDate(rs.getString(7));
				order.setPublishName(rs.getString(8));
				receive.add(order);
				
				System.out.println("receive   "+order.getPublishUserId()+"  "+order.getReceiveUserId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return receive;
	}

}