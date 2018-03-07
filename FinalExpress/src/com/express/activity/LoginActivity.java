package com.express.activity;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.example.finalexpress.R;
import com.express.service.MyCallable;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import entity.MessageType;
import entity.User;

public class LoginActivity extends Activity{
	private EditText studentid;
	private EditText password;
    private static User loginuser = null;
	
    public static User getUser(){
    	return loginuser;
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_layout);
		
		studentid = (EditText)findViewById(R.id.studentid);
		password = (EditText)findViewById(R.id.password);
		
		Button login = (Button)findViewById(R.id.login);
		Button register = (Button)findViewById(R.id.register);
		Button forgetPassword = (Button)findViewById(R.id.forgetPassword);
		final ImageView avatar = (ImageView)findViewById(R.id.userImag_layout);
		
		studentid.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String temp = (s.toString());
				if(temp.length() == 8)
				{
					Bitmap bitmap = null;
					bitmap = getDiskBitmap("mnt/sdcard/" + temp + ".jpg");
					if(bitmap != null)//设置头像
					{
						avatar.setImageBitmap(bitmap);
					}
				}
				else{
					Resources resources = getResources();
					Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.default_avatar);
					avatar.setImageBitmap(bitmap);
				}
			}
		});

	    //登陆响应
		login.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				User user = new User();
				user.setStudentid(studentid.getText().toString());
				user.setPassword(password.getText().toString());
				
				MyCallable call = new MyCallable(MessageType.LOGIN, user);
				FutureTask<Object> task = new FutureTask<Object>(call);
				Thread thread = new Thread(task);
				thread.start();
				
				try {
					loginuser = (User)task.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (loginuser != null)//如果登陆成功
				{
					Intent intent = new Intent(LoginActivity.this,LoadingActivity.class);
					startActivity(intent);
				}
				else//登录失败，通过Toast向用户提示登录失败
				{
					Toast.makeText(LoginActivity.this, "登录失败，不告诉你为什么...", Toast.LENGTH_LONG).show();
				}
				thread.interrupt();
			}
		});
		
		 //登陆响应
		register.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
			    startActivity(intent);
			}
		});
		  
		
		//忘记密码响应
		forgetPassword.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(LoginActivity.this,FindPassWordActivity.class);
			    startActivity(intent);
			}
		});
	}
	
	private Bitmap getDiskBitmap(String pathString)
	{
		Bitmap bitmap = null;
		try{
			File file = new File(pathString);
			if(file.exists())
			{
				bitmap = BitmapFactory.decodeFile(pathString);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return bitmap;
	}
	
}
