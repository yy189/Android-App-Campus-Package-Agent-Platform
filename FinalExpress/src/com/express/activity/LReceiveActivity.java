package com.express.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.example.finalexpress.R;
import com.express.adapter.PublishAdapter;
import com.express.adapter.ReceiveAdapter;
import com.express.service.MyCallable;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import entity.MessageType;
import entity.Order;

public class LReceiveActivity extends Activity{
	List<Order> receiveList = new ArrayList<Order>();
	ListView listView;
	private ReceiveAdapter adapter;
	ImageButton backbtn = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myreceive_layout);
		
		backbtn = (ImageButton)findViewById(R.id.return_receive);
		backbtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
		
		MyCallable call = new MyCallable(MessageType.RECEIVE, LoginActivity.getUser());
		FutureTask<Object> task = new FutureTask<Object>(call);
		Thread thread = new Thread(task);
		thread.start();
		
		try {
			receiveList = (List<Order>)task.get();
			for(int i=0;i<receiveList.size();i++){
				Log.v("我的代领", receiveList.get(i).getExpressDescribe());
			}
			adapter = new ReceiveAdapter(LReceiveActivity.this, R.layout.listview_find_item, receiveList);
			ListView listView=(ListView) findViewById(R.id.receive_list_view);
			listView.setAdapter(adapter);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}