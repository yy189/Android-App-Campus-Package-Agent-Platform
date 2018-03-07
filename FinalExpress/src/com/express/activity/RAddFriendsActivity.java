package com.express.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.example.finalexpress.R;
import com.express.adapter.FriendAdapter;
import com.express.adapter.MsgAdapter;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import entity.CMessage;
import entity.MessageType;
import entity.Order;
import entity.User;

public class RAddFriendsActivity extends Activity{
	
	List<User> searchfriend = new ArrayList<User>();
	ListView listView;
	private FriendAdapter adapter;
	private EditText searchName;
	String searchFriendName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.addfriends_layout);		
		
		listView=(ListView) findViewById(R.id.searchfriend_list_view);
		ImageButton backbtn = (ImageButton)findViewById(R.id.return_addfriend);
		
		backbtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent,View view,int position,long id){
					final User friend = searchfriend.get(position);
					User user = LoginActivity.getUser();
					
					final CMessage msg = new CMessage(); 
					msg.setObj(friend);
					msg.setSender(user);
					msg.setMsgType("ADDFRIENDIF");
					
					new AlertDialog.Builder(RAddFriendsActivity.this)
					.setTitle("确认")
					.setMessage("确认添加好友？")
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//finish();
						}
					})
					.setPositiveButton("确认", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							MyCallable call = new MyCallable(MessageType.ADDFRIENDIF, msg);
							FutureTask<Object> task = new FutureTask<Object>(call);
							Thread thread = new Thread(task);
							thread.start();
							
							try {
								int insertRow = (Integer)task.get();
								if(insertRow != -1){
									Toast.makeText(RAddFriendsActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
								}else{
									Toast.makeText(RAddFriendsActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
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
			}
		});
			
	}

	public void SearchFriends(View v){
		searchName = (EditText)findViewById(R.id.searchname);
		searchFriendName = searchName.getText().toString();

		CMessage message = new CMessage();
		message.setSender(LoginActivity.getUser());
		message.setObj(searchFriendName);
		
		if(searchName.length()==0){
			Toast.makeText(getApplicationContext(), "请输入好友姓名！", Toast.LENGTH_SHORT).show();	
		}
		else{
			MyCallable call = new MyCallable(MessageType.ADDFRIEND, message);
			FutureTask<Object> task = new FutureTask<Object>(call);
			Thread thread = new Thread(task);
			thread.start();
			
			try {
				searchfriend = (List<User>)task.get();		
				if(searchfriend.size()==0){
					Toast.makeText(getApplicationContext(), "没有此用户！", Toast.LENGTH_SHORT).show();	
				}
				else{
					adapter = new FriendAdapter(RAddFriendsActivity.this, R.layout.listview_find_item, searchfriend);
					listView.setAdapter(adapter);
				}
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
}

