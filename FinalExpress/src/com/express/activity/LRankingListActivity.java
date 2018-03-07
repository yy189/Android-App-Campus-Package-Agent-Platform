package com.express.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.example.finalexpress.R;
import com.express.adapter.FriendAdapter;
import com.express.adapter.PublishAdapter;
import com.express.adapter.RankingAdapter;
import com.express.service.MyCallable;

import entity.MessageType;
import entity.Order;
import entity.User;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class LRankingListActivity extends Activity{
	List<User> rankingList = new ArrayList<User>();
	ListView listView;
	private RankingAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rankinglist_layout);
		
		ImageButton backbtn = (ImageButton)findViewById(R.id.return_ranking);
		backbtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
		
		MyCallable call = new MyCallable(MessageType.RANKING, LoginActivity.getUser());
		FutureTask<Object> task = new FutureTask<Object>(call);
		Thread thread = new Thread(task);
		thread.start();
		
		try {
			rankingList = (List<User>)task.get();
			for(int i=0;i<rankingList.size();i++){
				Log.v("我的代领", rankingList.get(i).getStudentid());
			}
			
			adapter = new RankingAdapter(LRankingListActivity.this, R.layout.listview_ranking_item, rankingList);
			ListView listView=(ListView) findViewById(R.id.ranking_list_view);
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
