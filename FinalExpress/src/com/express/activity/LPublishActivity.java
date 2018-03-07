package com.express.activity;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.example.finalexpress.R;
import com.express.adapter.FriendAdapter;
import com.express.adapter.PublishAdapter;
import com.express.service.MyCallable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import entity.CMessage;
import entity.MessageType;
import entity.Order;
import entity.User;

public class LPublishActivity extends Activity{
	List<Order> publishList = new ArrayList<Order>();
    ListView listView;
	private PublishAdapter adapter;
	ImageButton backbtn = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mypublish_layout);
		listView=(ListView)findViewById(R.id.publish_list_view);
		backbtn = (ImageButton)findViewById(R.id.return_publish);
		backbtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
		
		//publishList.clear();
		MyCallable call = new MyCallable(MessageType.PUBLISH, LoginActivity.getUser());
		FutureTask<Object> task = new FutureTask<Object>(call);
		Thread thread = new Thread(task);
		thread.start();
		
		try {
			publishList = (List<Order>)task.get();			
			adapter = new PublishAdapter(LPublishActivity.this, R.layout.listview_publish_item, publishList);
			listView.setAdapter(adapter);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent,View view,int position,long id){
					final Order orderChoose = publishList.get(position);
					if(orderChoose.getAccepted()==true)
					{
						User user = LoginActivity.getUser();
						
						final CMessage msg = new CMessage(); 
						msg.setObj(orderChoose);
						msg.setSender(user);
						msg.setMsgType("ADDPOINT");
						
						new AlertDialog.Builder(LPublishActivity.this)
						.setTitle("收货")
						.setMessage("确认收货？")
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//finish();
							}
						})
						.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								MyCallable call = new MyCallable(MessageType.ADDPOINT, msg);
								FutureTask<Object> task = new FutureTask<Object>(call);
								Thread thread = new Thread(task);
								thread.start();
								
								int point = LoginActivity.getUser().getPoint()+1;
								LoginActivity.getUser().setPoint(point);
								try {
									int insertRow = (Integer)task.get();
									if(insertRow != -1){
										Toast.makeText(LPublishActivity.this, "收货成功", Toast.LENGTH_SHORT).show();
									}else{
										Toast.makeText(LPublishActivity.this, "收货失败", Toast.LENGTH_SHORT).show();
									}
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (ExecutionException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}).show();
					}else{
						Toast.makeText(LPublishActivity.this, "还未被代领，不能确认收货！", Toast.LENGTH_SHORT).show();
					}
			}
		});
	}
}
