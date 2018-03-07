package com.express.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.example.finalexpress.R;
import com.express.service.MyCallable;

import entity.MessageType;
import entity.Order;
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

public class RSendMessageActivity extends Activity{
	private EditText orderDescription;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sendmessage_layout);
		

		//返回响应
		ImageButton orderReturn=(ImageButton) findViewById(R.id.return_send);
		Button orderPublish=(Button) findViewById(R.id.send_btn);
		
		orderReturn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		//发布响应
		orderPublish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				orderDescription=(EditText) findViewById(R.id.send_message);
				if(orderDescription==null)
					Toast.makeText(RSendMessageActivity.this, "还没写快递信息！", Toast.LENGTH_LONG).show();
				else
				{
					Order order = new Order();
					order.setPublishUserId(LoginActivity.getUser().getStudentid());
					order.setPublishName(LoginActivity.getUser().getUsername());
					order.setExpressDescribe(orderDescription.getText().toString());
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
					order.setPublishDate(df.format(new Date()));
					
					MyCallable call = new MyCallable(MessageType.ORDERPUBLISH, order);
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
					if (ret=="1")//如果发布成功
					{
						Toast.makeText(RSendMessageActivity.this, "发布成功", Toast.LENGTH_LONG).show();
						Intent intent = new Intent(RSendMessageActivity.this,HomePageActivity.class);
						startActivity(intent);
					}
					else//发布失败，通过Toast向用户提示发布失败
					{
						Toast.makeText(RSendMessageActivity.this, "发布失败，不告诉你为什么...", Toast.LENGTH_LONG).show();
					}
				}
			}

		});
		//响应结束					
	}
}