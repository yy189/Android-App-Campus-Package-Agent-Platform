package entity;

import java.io.Serializable;

public class Order implements Serializable, Comparable<Order>{
	private int orderId;
	private String publishUserId;
	private String publishName;
	private String receiveUserId;
	private String expressDescribe;
	private boolean accepted = false;//是否被人领走
	private boolean received = false;//是否收到货
	private String publishDate;
	private byte[] avatar;
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int id) {
		this.orderId = id;
	}
	public String getPublishUserId() {
		return publishUserId;
	}
	public void setPublishUserId(String id) {
		this.publishUserId = id;
	}
	public String getReceiveUserId() {
		return receiveUserId;
	}
	public void setReceiveUserId(String receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	public String getExpressDescribe() {
		return expressDescribe;
	}
	public void setExpressDescribe(String expressDescribe) {
		this.expressDescribe = expressDescribe;
	}
	public boolean getAccepted()
	{
		return accepted;
	}
	public void setAccepted(boolean accepted)
	{
		this.accepted = accepted;
	}
	public boolean getReceived()
	{
		return received;
	}
	public void setReceived(boolean received)
	{
		this.received = received;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String date) {
		this.publishDate = date;
	}
	public byte[] getAvatar() {
		return avatar;
	}
	public void setAvatar(byte[] avatar){
		this.avatar = avatar;
	}
	public String getPublishName() {
		return publishName;
	}
	public void setPublishName(String publishName) {
		this.publishName = publishName;
	}
	
	@Override
	public int compareTo(Order another) {
		if(orderId >= another.getOrderId())
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}
	

}