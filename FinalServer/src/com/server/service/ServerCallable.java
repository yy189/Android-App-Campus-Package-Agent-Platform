package com.server.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import com.mysql.jdbc.log.Log;
import com.server.dao.UserDAO;
import com.server.dao.UserFriends;

import entity.CMessage;
import entity.MessageType;
import entity.Order;
import entity.User;

public class ServerCallable implements Callable<Object> {
	private String operation;//操作
	private Object obj;     //sql参数
	private Object obj2; //sql参数2
	
	public ServerCallable(String orderRefresh, Object sender, Object receiver) {
		// TODO Auto-generated constructor stub
		this.operation = orderRefresh;
		this.obj = sender;
		this.obj2 = receiver;
	}
	
	
	public ServerCallable(String operation, Object obj)
	{
		this.operation = operation;
		this.obj = obj;
	}

	
	public void setOperation(String operation)//除了注册和登录外别的操作都要用
	{
		this.operation = operation;
	}
	
	public void setObj(Object obj)//除了注册和登录外别的操作都要用
	{
		this.obj = obj;
	}
	
	@Override
	public Object call() throws Exception {
		Object result = null;
		if(operation.equals(MessageType.LOGIN))
		{
			User user = UserDAO.getUser((User)obj);//调用数据库查询操作
			user.setAvatar(loadAvatar(user.getStudentid()));
			result =  user;
		}
		else if(operation.equals(MessageType.REGISTER))
		{
			 result = UserFriends.Register((User)obj);
		}else if(operation.equals(MessageType.MYFRIEND))
		{
			List<User> myfriends = UserFriends.MyFriends((User)obj);
			for(int i=0; i < myfriends.size(); i++)
			{
				myfriends.get(i).setAvatar(loadAvatar(myfriends.get(i).getStudentid()));
			}
			result = myfriends;
		}else if(operation.equals(MessageType.ORDER_REFRESH))
		{
			List<String> friends = UserDAO.getFriends((User)obj);
			List<Order> finalResult = new ArrayList<Order>();
			if (friends != null)
			{
				List<Order> orders = null;
				Order order = null;
				int i, j;
				for(i = 0; i < friends.size(); i++)
				{
					orders = UserDAO.getOrders(friends.get(i));
					if(orders == null)
					{
						continue;
					}
					else
					{
						for(j = 0; j < orders.size(); j++)
						{
							order = orders.get(j);
							if(order.getAccepted() == false && order.getReceived() == false)
							{
								order.setAvatar(loadAvatar(friends.get(i)));
								finalResult.add(order);
							}
						}
					}
				}
				Collections.sort(finalResult);
			}
			result = finalResult;
		}
		else if(operation.equals(MessageType.NEW_FRIENDS))
		{
			List<User> newFriends = UserDAO.getNewFriends((User)obj); 
			int i, j;
			for(i = 0; i<newFriends.size();i++)//删掉自己
			{
				if(newFriends.get(i).getStudentid().equals(((User)obj).getStudentid()))
				{
					newFriends.remove(i);
				}
			}
			List<String> friends = UserDAO.getFriends((User)obj);
			
			for(i = 0; i<newFriends.size(); i++)//删掉好友
			{
				for(j = 0; j<friends.size(); j++)
				{
					if(newFriends.get(i).getStudentid().equals(friends.get(j)))
					{
						newFriends.remove(i);
					}
				}
			}
			for(i = 0; i < newFriends.size(); i++)
			{
				newFriends.get(i).setAvatar(loadAvatar(newFriends.get(i).getStudentid()));
			}
			Collections.shuffle(newFriends);//随机排序
			result =  newFriends;
		}
		else if(operation.equals(MessageType.PUBLISH))
		{
			List<Order> mypublish = UserFriends.getPublish((User)obj);
			for(int i=0;i<mypublish.size();i++){
				mypublish.get(i).setAvatar(loadAvatar(mypublish.get(i).getPublishUserId()));
			}
			result = mypublish;
		}else if(operation.equals(MessageType.RECEIVE))
		{
			List<Order> myreceiver = UserFriends.getReceive((User)obj);
			for(int i=0;i<myreceiver.size();i++){
				myreceiver.get(i).setAvatar(loadAvatar(myreceiver.get(i).getPublishUserId()));
			}
			result = myreceiver;
		}else if(operation.equals(MessageType.ADDFRIEND))
		{
			List<User> searchfriends  = UserFriends.getFriend((CMessage)obj);
			for(int i=0; i < searchfriends.size(); i++)
			{
				searchfriends.get(i).setAvatar(loadAvatar(searchfriends.get(i).getStudentid()));
			}
			result = searchfriends;
		}
		else if(operation.equals(MessageType.ADDFRIENDIF))
		{
			int insertRow  = UserFriends.AddFriend((CMessage)obj);
			result = insertRow;
		}
		else if(operation.equals(MessageType.ORDERPUBLISH))
		{
			result=UserFriends.insertOrder((Order)obj);
		}
		else if(operation.equals(MessageType.CHANGEPW))
		{
			result=UserFriends.changePw((User)obj);
		}else if(operation.equals(MessageType.CONFIRM))
		{
			int updateRow = UserFriends.ConfirmExpress((CMessage)obj);
			result = updateRow;
		}else if(operation.equals(MessageType.LOAD_CHAT_RECORDS)){
			//List<String> friends = UserDAO.getFriends((User)obj);
			List<CMessage> chatrecords = new ArrayList<CMessage>();
			//List<CMessage> finalResult = new ArrayList<CMessage>();			
			chatrecords = UserDAO.loadChatRecords((User)obj,(User)obj2);
			for(int k = 0;k<chatrecords.size();k++){
				chatrecords.get(k).getSender().setAvatar(loadAvatar(chatrecords.get(k).getSender().getStudentid()));
				chatrecords.get(k).getReceiver().setAvatar(loadAvatar(chatrecords.get(k).getReceiver().getStudentid()));
			}
			result = chatrecords;
		}else if(operation.equals(MessageType.INSERT_CHAT_RECORDS)){
			Boolean flag = UserDAO.insertChatRecord((CMessage)obj);
			result = flag;
		}else if(operation.equals(MessageType.CHATER_REFRESH)){
			List<CMessage> chaterList = new ArrayList<CMessage>();
			List<User> charter = new ArrayList<User>();
			List<CMessage> finalResult = new ArrayList<CMessage>();
			chaterList = UserDAO.loadChaterList((User)obj);
			User owner =(User)obj;
			boolean flag;			
			for(int i = chaterList.size()-1; i>=0;--i){
				flag = false;  //不存在在已保存的记录中
				User touser = new User();
				if(chaterList.get(i).getReceiver().getStudentid().equals(owner.getStudentid())){
					touser.setStudentid(chaterList.get(i).getSender().getStudentid());	
				}else{
					touser.setStudentid(chaterList.get(i).getReceiver().getStudentid());	
				}
				
				for (int j = 0; j<charter.size(); j++ ) {
					if (touser.getStudentid().equals(charter.get(j).getStudentid())) {
						flag = true;
						break;
					}
				}
				
				if(!flag){
					finalResult.add(chaterList.get(i));
					charter.add(touser);
				}
			}
			
			for(int k = 0;k<finalResult.size();k++){
				finalResult.get(k).setAvatar(loadAvatar(charter.get(k).getStudentid()));
			}
			
			result = finalResult;
		}else if(operation.equals(MessageType.FIND_USER)){
			result =  UserDAO.getTheUser((User)obj);//调用数据库查询操作
		}
		else if(operation.equals(MessageType.FINDPASSWORD))
		{
			 result =  UserDAO.getFindPWUser((User)obj);//调用数据库查询操作
		}
		else if(operation.equals(MessageType.RANKING))
		{
			List<User> ranking = UserFriends.Ranking((User)obj);
			for(int i=0;i<ranking.size();i++){
				ranking.get(i).setAvatar(loadAvatar(ranking.get(i).getStudentid()));
			}
			result = ranking;
		}
		else if(operation.equals(MessageType.ADDPOINT))
		{
			int updateRow  = UserFriends.AddPoint((CMessage)obj);
			result = updateRow;
		}
		return result;
	}
	
	private byte[] loadAvatar(String studentId)
	{
		byte[] data = null;
		File myFile = new File("C://avatar//"+studentId+".jpg");
		if(myFile.exists())
		{
			FileInputStream fis;
			try {
				fis = new FileInputStream(myFile);
				int size = fis.available();
				data = new byte[size];
				fis.read(data);
				fis.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return data;
	}

}
