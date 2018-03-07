package entity;

import java.io.Serializable;

public class CMessage implements Serializable {
	private User sender;//发送者
	private User receiver;//接受者
	private String msgType;//消息类型
	private Object obj;//消息内容
	private String time; //发送消息的时间
	private byte[] avatar= null;//头像
	

	public CMessage() {
		super();
	}
	public CMessage(String content, String type) {
		// TODO Auto-generated constructor stub
		this.obj =content;
		this.msgType = type;
	}
	public CMessage(User sender, User receiver, String msgType, Object obj, String time) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.msgType = msgType;
		this.obj = obj;
		this.time = time;
	}
	
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	public User getReceiver() {
		return receiver;
	}
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	//头像
	public byte[] getAvatar()
	{
		return avatar;
	}
	public void setAvatar(byte[] avatar)
	{
		this.avatar = avatar;
	}
}
