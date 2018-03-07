package entity;

import java.io.Serializable;

public class User implements Serializable {
	private String studentid;  //学号
	private String username;   //姓名
	private String password;   //密码
	private String department; //院系
	private String dormitory;  //宿舍区
	private int point;
	private byte[] avatar = null;//头像

	//学号
	public String getStudentid()
	{
		return studentid;
	}
	public void setStudentid(String studentid)
	{
		this.studentid = studentid;
	}
	
	//姓名
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	
	//密码
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	//院系
	public String getDepartment(){
		return department;
	}
	public void setDepartment(String department){
		this.department = department;
	}
	
	//宿舍区
	public String getDormitory(){
		return dormitory;
	}
	public void setDormitory(String dormitory){
		this.dormitory = dormitory;
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
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
}