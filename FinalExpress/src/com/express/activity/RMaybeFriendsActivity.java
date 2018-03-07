package com.express.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.example.finalexpress.R;
import com.express.adapter.FriendAdapter;
import com.express.service.MyCallable;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import entity.MessageType;
import entity.User;

public class RMaybeFriendsActivity extends Activity{
	private FriendAdapter friendAdapter;
	private ListView listView;
	List<User> newFriends = new ArrayList<User>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maybefriends_layout);
		
		ImageButton backbtn = (ImageButton)findViewById(R.id.return_maybefriend);
		backbtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
		
		
		MyCallable call = new MyCallable(MessageType.NEW_FRIENDS, null);
		FutureTask<Object> task = new FutureTask<Object>(call);
		Thread thread = new Thread(task);
		thread.start();

		try {
			newFriends = (List<User>)task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread.interrupt();
		
		friendAdapter=new FriendAdapter(RMaybeFriendsActivity.this, R.layout.listview_find_item, newFriends);
		listView=(ListView) findViewById(R.id.list_view_maybefriend);
		listView.setAdapter(friendAdapter);
		
	}
}
