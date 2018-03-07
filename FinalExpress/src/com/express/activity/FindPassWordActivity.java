package com.express.activity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.example.finalexpress.R;
import com.express.adapter.TestArrayAdapter;
import com.express.service.MyCallable;

import entity.MessageType;
import entity.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FindPassWordActivity extends Activity {
	
	 private Spinner dSpinner;//，dSpinner为宿舍下拉选项
	 private ArrayAdapter<String> dAdapter ;
	 private String [] dStringArray;
	 private EditText FPW_studentid;
	 private TextView FPW_password;
	 private User findPWuser = null;
	 private User user=new User();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.findpassword);
		
		dSpinner = (Spinner) findViewById(R.id.findPW_dormitory);
		dStringArray = getResources().getStringArray(R.array.dormitorys);
        //将可选内容与ArrayAdapter连接起来
		dAdapter = new TestArrayAdapter(this,dStringArray);
		//mAdapter = ArrayAdapter.createFromResource(this, R.array.majors, android.R.layout.simple_spinner_item);		
        //设置下拉列表的风格 
		dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter2 添加到spinner中
        dSpinner.setAdapter(dAdapter);
        //添加事件Spinner事件监听  
        dSpinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
        //设置默认值
        dSpinner.setVisibility(View.VISIBLE);
        
        FPW_studentid=(EditText) findViewById(R.id.findPW_studentid);
        
        FPW_password=(TextView) findViewById(R.id.findPW_password);
        Button FPW_OK=(Button) findViewById(R.id.findPW_button_find);
        FPW_OK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				user.setStudentid(FPW_studentid.getText().toString());
				
				if(user.getDormitory().equals("宿舍区"))
				{
					Toast.makeText(FindPassWordActivity.this,"未选择宿舍区", Toast.LENGTH_SHORT).show(); 
				}
				else {
					MyCallable call = new MyCallable(MessageType.FINDPASSWORD, user);
					FutureTask<Object> task = new FutureTask<Object>(call);
					Thread thread = new Thread(task);
					thread.start();
					
					try {
						findPWuser = (User)task.get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (findPWuser != null)//如果登陆成功
					{
						FPW_password.setText("密码: "+findPWuser.getPassword());
					}
					else//登录失败，通过Toast向用户提示登录失败
					{
						Toast.makeText(FindPassWordActivity.this, "信息错误", Toast.LENGTH_LONG).show();
					}
					thread.interrupt();
				}
				//监听结束
			}
		});
	}
	
	//使用XML形式操作
    class SpinnerXMLSelectedListener implements OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                long arg3) {
        	user.setDormitory(arg0.getItemAtPosition(arg2).toString());
//        	Toast.makeText(arg0.getContext(), 
//                   "The planet is "+  arg0.getItemAtPosition(arg2).toString(), 
//                     Toast.LENGTH_SHORT).show(); 
//        	System.out.println("选中了:"+dStringArray[position]);
//            view2.setText("你使用什么样的手机："+adapter2.getItem(arg2));
        }
 
        public void onNothingSelected(AdapterView<?> arg0) {
             
        }
         
    }

}
