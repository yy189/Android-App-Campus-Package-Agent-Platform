package com.express.activity;


import com.example.finalexpress.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

public class LoadingActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState){ 
		super.onCreate(savedInstanceState);	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loading_layout);
		
		//ÑÓ³Ù2ÃëÌø×ª
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run(){
				Intent intent = new Intent(LoadingActivity.this,HomePageActivity.class);
				startActivity(intent);
				LoadingActivity.this.finish();
				Toast.makeText(getApplicationContext(), "µÇÂ½³É¹¦", Toast.LENGTH_SHORT).show();				
			}
		},2000);
	}
}
