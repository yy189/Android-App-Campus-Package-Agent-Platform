package com.express.activity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.example.finalexpress.R;
import com.express.service.MyCallable;

import entity.MessageType;
import entity.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LSettingActivity extends Activity{
	private EditText oldPassword;
	private EditText newPassword;
	private EditText new2Password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_layout);
		
		ImageButton backbtn = (ImageButton)findViewById(R.id.return_setting);
		backbtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
		
		Button settingOK=(Button) findViewById(R.id.button_setting_ok);
		settingOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				oldPassword=(EditText) findViewById(R.id.oddpassword);
				newPassword=(EditText) findViewById(R.id.newpassword);
				new2Password=(EditText) findViewById(R.id.new2password);
				if(oldPassword==null||newPassword==null||new2Password==null)
				{
					Toast.makeText(LSettingActivity.this, "信息不完整！", Toast.LENGTH_LONG).show();
				}
				else if(newPassword.toString().equals(new2Password.toString()))
				{
					Toast.makeText(LSettingActivity.this, "两次密码输入不一致！", Toast.LENGTH_LONG).show();
					newPassword.setText("");
					new2Password.setText("");
				}
				else {
					User user=LoginActivity.getUser();
					String password=user.getPassword();
					password+=",";
					user.setPassword(password+newPassword.getText().toString());
					System.out.println(user.getPassword());
					MyCallable call = new MyCallable(MessageType.CHANGEPW, user);
					FutureTask<Object> task = new FutureTask<Object>(call);
					Thread thread = new Thread(task);
					thread.start();
					String ret= "0";
					try {
						System.out.println("1");
						ret = (String)task.get();						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					thread.interrupt();
					if (ret=="1-1")//密码修改成功
					{
						Toast.makeText(LSettingActivity.this, "修改密码成功", Toast.LENGTH_LONG).show();
						Intent intent = new Intent(LSettingActivity.this,HomePageActivity.class);
						startActivity(intent);
					}
					else if(ret=="1-0")//密码修改失败
					{
						Toast.makeText(LSettingActivity.this, "服务器打盹了！", Toast.LENGTH_LONG).show();
					}
					else //if(ret=="0")//原密码错误
					{
						Toast.makeText(LSettingActivity.this, "原密码错误！", Toast.LENGTH_LONG).show();
						oldPassword.setText("");
						new2Password.setText("");
					}
				}
			}
		});
	}
}
