package com.server.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.FutureTask;

import javax.imageio.ImageIO;

import com.server.dao.UserDAO;

import entity.CMessage;
import entity.MessageType;
import entity.Order;
import entity.User;

public class SServer {
	public SServer()
	{
		ServerSocket ss = null;
		try
		{
			 ss = new ServerSocket(8890);
			 System.out.println("服务器已启动，正在监听8888端口...");
			
			 while(true)
			 {
				//接受客户端的socket连接
				 Socket s = ss.accept();
				//接受客户发来的信息
				 ObjectInputStream ois = new ObjectInputStream(s.getInputStream());		 
				 CMessage msg = (CMessage)ois.readObject();
				 User sender = msg.getSender();     //几乎所有功能都会用到sender来查数据库
				 
				 ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				 
				 //判断客户端的操作类型是否为：登陆操作
				 if(msg.getMsgType().equals(MessageType.LOGIN))
				 {
					 ServerCallable call = new ServerCallable(MessageType.LOGIN, msg.getSender());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 //将线程放入线程池中,登录操作执行结束后保持线程，对同一个用户而言除注册外其余操作都用此线程
					 thread.start();
					 
					 User u = (User)task.get(); //执行登录操作返回的结果，UserDao中的返回结果
					 if(u != null)
					 {
						 System.out.println("["+u.getUsername()+"]上线了!");
						 msg.setMsgType(MessageType.LOGIN_SUCCESS);
						 msg.setReceiver(u);
						 oos.writeObject(msg);
						 oos.flush();
					 }
					 else
					 {
						 msg.setMsgType(MessageType.LOGIN_FAILURE);
						 msg.setReceiver(u);
						 oos.writeObject(msg);
					 }
					 thread.interrupt();
					
				 }
				 else if(msg.getMsgType().equals(MessageType.REGISTER))
				 {
					 ServerCallable call = new ServerCallable(MessageType.REGISTER,msg.getSender());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 
					 thread.start();
					 
					 msg.setReceiver(msg.getSender());
					 String str = (String)task.get();
					 
					 if(str=="1")
						 msg.setMsgType(MessageType.REGISTER_1); //数据库已存在此人
				     if(str=="2")
						 msg.setMsgType(MessageType.REGISTER_2);  //未插入成功
					 if(str=="3")
						 msg.setMsgType(MessageType.REGISTER_3);  //注册成功
					 
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt();  //注册结束杀掉线程
					 
				 }else if(msg.getMsgType().equals(MessageType.MYFRIEND)){
					 ServerCallable call = new ServerCallable(MessageType.MYFRIEND,sender);
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();
					 

					 msg.setReceiver(sender);
					 msg.setObj((Object)task.get());
					 					 
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt(); 
				 } else if(msg.getMsgType().equals(MessageType.ADDFRIENDIF)){
					 ServerCallable call = new ServerCallable(MessageType.ADDFRIENDIF,msg);
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();

					 msg.setReceiver(sender);
					 msg.setObj((Object)task.get());
					 					 
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt(); 
				 } else if(msg.getMsgType().equals(MessageType.ORDER_REFRESH)){
					 ServerCallable call = new ServerCallable(MessageType.ORDER_REFRESH, msg.getSender());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 thread.start();
					 
					 List<Order> orders = (List<Order>)task.get();
					 thread.interrupt();
					 msg.setReceiver(sender);
					 if(orders != null)
					 {
						 System.out.println("发现快递成功！");
						 msg.setMsgType(MessageType.ORDER_REFRESH_SUCCESS);
						 msg.setObj(orders);
						 oos.writeObject(msg);
						 oos.flush();
					 }
					 else
					 {
						 System.out.println("发现快递失败！");
						 msg.setMsgType(MessageType.ORDER_REFRESH_FAILURE);
						 msg.setObj(orders);
						 oos.writeObject(msg);
						 oos.flush();
					 }
				 }
				 else if(msg.getMsgType().equals(MessageType.NEW_FRIENDS))
				 {
					 ServerCallable call = new ServerCallable(MessageType.NEW_FRIENDS, msg.getSender());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 thread.start();
					 List<User> newFriends = (List<User>)task.get();
					 thread.interrupt();
					 msg.setReceiver(sender);
					 if(newFriends != null)
					 {
						 System.out.println("可能认识的人成功！");
						 msg.setObj(newFriends);
						 oos.writeObject(msg);
						 oos.flush();
					 }
					 else
					 {
						 System.out.println("可能认识的人失败！");
						 oos.writeObject(msg);
						 oos.flush();
					 }
				 }
				 else if(msg.getMsgType().equals(MessageType.SET_AVATAR))
				 {
					 ByteArrayInputStream in = new ByteArrayInputStream((byte[])msg.getObj());
					 BufferedImage image = ImageIO.read(in);
					 File newFile = new File("C://avatar//"+sender.getStudentid()+".jpg");
					 boolean b = ImageIO.write(image, "JPEG", newFile);
					 msg.setReceiver(sender);
					 if (b == true)//头像保存成功
					 {
						 System.out.println("设置头像成功！");
						 msg.setObj(true);
						 oos.writeObject(msg);
						 oos.flush();
					 }
					 else
					 {
						 System.out.println("设置头像失败！");
						 msg.setObj(false);
						 oos.writeObject(msg);
						 oos.flush();
					 }
				 }else if(msg.getMsgType().equals(MessageType.PUBLISH)){
					 ServerCallable call = new ServerCallable(MessageType.PUBLISH,sender);
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();
					 
					 msg.setReceiver(sender);
					 msg.setObj((Object)task.get());
					 					 
					 oos.writeObject(msg);
					 oos.flush();
				 }else if(msg.getMsgType().equals(MessageType.RECEIVE)){
					 ServerCallable call = new ServerCallable(MessageType.RECEIVE,sender);
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();
					 
					 msg.setReceiver(sender);
					 msg.setObj((Object)task.get());
					 					 
					 oos.writeObject(msg);
					 oos.flush();
				 }else if(msg.getMsgType().equals(MessageType.ADDFRIEND)){
					 System.out.println("msg1"+msg.getObj().toString());
					 ServerCallable call = new ServerCallable(MessageType.ADDFRIEND,msg);
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();
					 
					 msg.setReceiver(sender);
					 msg.setObj((Object)task.get());
					 					 
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt();
				 } else if(msg.getMsgType().equals(MessageType.ORDERPUBLISH))
				 {
					 ServerCallable call = new ServerCallable(MessageType.ORDERPUBLISH,msg.getObj());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 
					 thread.start();
					 
					 msg.setReceiver(msg.getSender());
					 String str = (String)task.get();
					 
					 if(str=="1")
						 msg.setMsgType(MessageType.ORDERPUBLISH_SUCCESS); //发布成功
				     if(str=="0")
						 msg.setMsgType(MessageType.ORDERPUBLISH_FAILURE);  //发布失败
				     
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt();  //注册结束杀掉线程
				 }
				 else if(msg.getMsgType().equals(MessageType.CHANGEPW))
				 {
					 System.out.println("3");
					 ServerCallable call = new ServerCallable(MessageType.CHANGEPW,msg.getSender());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 
					 thread.start();
					 
					 msg.setReceiver(msg.getSender());
					 String str = (String)task.get();
					 
					 if(str=="1-1")
						 msg.setMsgType(MessageType.CHANGEPW_SUCCESS); //修改成功
				     if(str=="1-0")
						 msg.setMsgType(MessageType.CHANGEPW_FAILURE);  //密码失败
				     if(str=="0")
				    	 msg.setMsgType(MessageType.PW_WRONG);//密码不对
				     
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt();  //注册结束杀掉线程
				 } 
				 else if(msg.getMsgType().equals(MessageType.SENDMESSAGE))
				 {
					 ServerCallable call = new ServerCallable(MessageType.SENDMESSAGE,msg.getSender());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 
					 thread.start();
					 
					 msg.setReceiver(msg.getSender());
					 String str = (String)task.get();
					 
					 if(str=="1-1")
						 msg.setMsgType(MessageType.CHANGEPW_SUCCESS); //修改成功
				     if(str=="1-0")
						 msg.setMsgType(MessageType.CHANGEPW_FAILURE);  //密码失败
				     if(str=="0")
				    	 msg.setMsgType(MessageType.PW_WRONG);//密码不对
				     
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt();  //注册结束杀掉线程
				 }else if(msg.getMsgType().equals(MessageType.CONFIRM)){
					 ServerCallable call = new ServerCallable(MessageType.CONFIRM,msg);
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();
					 
					 //获取用户登录的线程
					 //ServerThreadMgr.get(sender).start();
					 msg.setReceiver(sender);
					 msg.setObj((Integer)task.get());
					 					 
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt();
				 }else if(msg.getMsgType().equals(MessageType.FINDPASSWORD))
				 {
					 ServerCallable call = new ServerCallable(MessageType.FINDPASSWORD, msg.getSender());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 //将线程放入线程池中,登录操作执行结束后保持线程，对同一个用户而言除注册外其余操作都用此线程
					 thread.start();
					 
					 User u = (User)task.get(); //执行登录操作返回的结果，UserDao中的返回结果
					 if(u != null)
					 {
						 System.out.println("["+u.getUsername()+"]找到啦!");
						 msg.setMsgType(MessageType.FINDPASSWORD_SUCCESS);
						 msg.setReceiver(u);
						 oos.writeObject(msg);
						 oos.flush();
					 }
					 else
					 {
						 msg.setMsgType(MessageType.FINDPASSWORD_FAILURE);
						 msg.setReceiver(u);
						 oos.writeObject(msg);
					 }
					 thread.interrupt();
					
				 }
				 else if(msg.getMsgType().equals(MessageType.RANKING)){
					 ServerCallable call = new ServerCallable(MessageType.RANKING,sender);
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();
					 
					 msg.setReceiver(sender);
					 msg.setObj((Object)task.get());
					 					 
					 oos.writeObject(msg);
					 oos.flush();
				 }
				 else if(msg.getMsgType().equals(MessageType.LOAD_CHAT_RECORDS)){					 
					 ServerCallable call = new ServerCallable(MessageType.LOAD_CHAT_RECORDS, msg.getSender(),msg.getReceiver());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 thread.start();
					 List<CMessage> chatrecords = (List<CMessage>)task.get();
					 thread.interrupt();
					 msg.setReceiver(sender);
					 
					 if(chatrecords != null)
					 {
						 System.out.println("加载聊天记录成功！");
						 msg.setMsgType(MessageType.LOAD_CHAT_RECORDS_SUCCESS);
						 for(int i = 0; i<chatrecords.size(); i++)
						 {
							System.out.println("sender: "+chatrecords.get(i).getSender().getStudentid()+ "  receiver: "+ chatrecords.get(i).getReceiver().getStudentid()
									 + "  content: " +(String)chatrecords.get(i).getObj());
							 //System.out.println("content: " +(String)chatrecords.get(i).getObj());
						 }
						 msg.setObj(chatrecords);
					 }
					 else
					 {
						 System.out.println("加载聊天记录失败！");
						 msg.setMsgType(MessageType.LOAD_CHAT_RECORDS_FAILURE);
					 }
					 oos.writeObject(msg);
					 oos.flush();
				 }else if(msg.getMsgType().equals(MessageType.INSERT_CHAT_RECORDS)){
					 ServerCallable call = new ServerCallable(MessageType.INSERT_CHAT_RECORDS,msg.getObj());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();
					 
					 //System.out.println("msg2"+task.get().toString());
					 
					 Boolean flag =  (Boolean)task.get();
					 thread.interrupt();
					 msg.setReceiver(sender);
					 
					 if(flag)
					 {
						 System.out.println("插入聊天记录成功！");
						 msg.setMsgType(MessageType.INSERT_CHAT_RECORDS_SUCCESS);
					 }
					 else
					 {
						 System.out.println("插入聊天记录失败！");
						 msg.setMsgType(MessageType.INSERT_CHAT_RECORDS_FAILURE);

					 }
					 msg.setObj(flag);
					 oos.writeObject(msg);
					 oos.flush();
				 }else if(msg.getMsgType().equals(MessageType.CHATER_REFRESH)){
					 
					 ServerCallable call = new ServerCallable(MessageType.CHATER_REFRESH, msg.getSender());
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);
					 thread.start();
					 List<CMessage> chaterList= (List<CMessage>)task.get();
					 thread.interrupt();
					 msg.setReceiver(sender);
					 User temp = new User();
					 
					 for(int i = 0;i<chaterList.size();i++){
						 if(chaterList.get(i).getReceiver().getStudentid().equals(msg.getSender().getStudentid())){
							 ServerCallable call2 = new ServerCallable(MessageType.FIND_USER, chaterList.get(i).getSender());
							 FutureTask<Object> task2 = new FutureTask<Object>(call2);
							 Thread thread2 = new Thread(task2);
							 thread2.start();
							 temp = (User)task2.get();
							 chaterList.get(i).setSender(temp);
							 thread2.interrupt();
						 }else{
							 ServerCallable call2 = new ServerCallable(MessageType.FIND_USER, chaterList.get(i).getReceiver());
							 FutureTask<Object> task2 = new FutureTask<Object>(call2);
							 Thread thread2 = new Thread(task2);
							 thread2.start();
							 temp = (User)task2.get();
							 chaterList.get(i).setReceiver(temp);
							 thread2.interrupt();
						 }
					 }
					 msg.setMsgType(MessageType.LOAD_CHAT_RECORDS_SUCCESS);
					 /*for(int i = 0; i<chaterList.size(); i++)
					 {
						 System.out.println("~~~~~~~");
						 
						System.out.println("sender: "+chaterList.get(i).getSender().getUsername() + "  receiver: "+ chaterList.get(i).getReceiver().getUsername()
								 + "  content: " +(String)chaterList.get(i).getObj());
						 //System.out.println("content: " +(String)chatrecords.get(i).getObj());
					 }*/
					 msg.setObj(chaterList);
					 oos.writeObject(msg);
					 oos.flush();
				 }else if(msg.getMsgType().equals(MessageType.ADDPOINT)){
					 ServerCallable call = new ServerCallable(MessageType.ADDPOINT,msg);
					 FutureTask<Object> task = new FutureTask<Object>(call);
					 Thread thread = new Thread(task);					 
					 thread.start();

					 msg.setReceiver(sender);
					 msg.setObj((Object)task.get());
					 					 
					 oos.writeObject(msg);
					 oos.flush();
					 thread.interrupt(); 
				 } 
			 }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
