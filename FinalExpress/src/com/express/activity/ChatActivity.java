package com.express.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.example.finalexpress.R;
import com.express.adapter.MsgAdapter;
import com.express.service.MyCallable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import entity.CMessage;
import entity.MessageType;
import entity.Order;
import entity.User;

public class ChatActivity extends Activity {
	private ListView msgListView;
	private EditText inputText;
	private Button send;
	private MsgAdapter adapter;
	private TextView titleText;	
	private ImageButton back;
	private List<CMessage> msgList = new ArrayList<CMessage>();
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private int i = 0;
	boolean stopThread=false;
	private final static int updateRecords_ID = 1;
    public Timer mTimer = new Timer();// 定时器
	public Handler mHandler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case 1:  

            	// Handler处于UI线程中,更新界面的操作在此处执行  */
            	//System.out.println("handler" + i);
            	adapter.notifyDataSetChanged(); // 当有新消息时，刷新ListView中的显示
				msgListView.setSelection(msgList.size()); // 将ListView定位到最后一行
                break; 
            case 2:
            	break; 
            case 3:  
                mTimer.cancel();//  
                mTimer=null;  
            }  
            super.handleMessage(msg);  
        }  
    };  
      
    //public Timer mTimer = new Timer();// 定时器
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		back = (ImageButton)findViewById(R.id.chat_return_publish);
		titleText = (TextView)findViewById(R.id.chat_title_text);
		msgList = initMsgs(); // 初始化消息数据
		adapter = new MsgAdapter(ChatActivity.this, msgList);
		inputText = (EditText) findViewById(R.id.input_text);
		send = (Button) findViewById(R.id.send);
		msgListView = (ListView) findViewById(R.id.msg_list_view);
		msgListView.setAdapter(adapter);
		msgListView.setSelection(msgList.size()); // 将ListView定位到最后一行	
		
		send.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				String content = inputText.getText().toString();				
				Date curDate = new Date(System.currentTimeMillis()); //获取当前时间
				String time = formatter.format(curDate);
				if (!"".equals(content)) {
					CMessage msg = new CMessage(LoginActivity.getUser(),HomePageActivity.getReceiver(),MessageType.CHAT_SENT,(Object)content,time);
					insertMsgList(msg);	
					//chatClient.sendMsg(msg);
					msgList.add(msg);
					adapter.notifyDataSetChanged(); // 当有新消息时，刷新ListView中的显示
					msgListView.setSelection(msgList.size()); // 将ListView定位到最后一行			
					inputText.setText(""); // 清空输入框中的内容
				}
			}		
		});	
		
		titleText.setText(HomePageActivity.getReceiver().getUsername());
		back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
		timerTask(); // 定时执行 		
	}
	
    public void timerTask() {  
        //创建定时线程执行更新任务  
        mTimer.schedule(new TimerTask() {  
            @Override  
            public void run() {  
            	Message msgTemp =new Message(); 
                if(!stopThread){  
                	List<CMessage> temp = initMsgs();
                	if(temp.size()==msgList.size()){
                		msgTemp.what = 2;
                	}else{
                		for(int j = msgList.size();j<temp.size();j++){
                			msgList.add(temp.get(j));
                		}
                		
                		msgTemp.what = updateRecords_ID;
                	}
					
					System.out.println("task" + i++);
					System.out.println(msgList.size());
					mHandler.sendMessage(msgTemp); 
                }else{  
                	msgTemp.what = 3;
                	mHandler.sendMessage(msgTemp);// 向Handler发送消息停止继续执行  
                }   
            }  
        }, 3000, 3000);// 定时任务  
    } 

	protected void onDestroy() {
		System.out.println("-----------onDestroy------");
		stopThread = true;
		super.onDestroy();
	};
		
	protected void insertMsgList(CMessage msg) {
		// TODO Auto-generated method stub
		MyCallable call = new MyCallable(MessageType.INSERT_CHAT_RECORDS, msg);
		FutureTask<Object> task = new FutureTask<Object>(call);
		Thread thread = new Thread(task);
		thread.start();
		try {
			msg = (CMessage)task.get();
			//System.out.println("!r: "+msg.getReceiver().getStudentid()+"!s: "+msg.getSender().getStudentid()+"!t: "+msg.getMsgType()+"!c: "+(String)msg.getObj());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread.interrupt();
	}

	//有新消息，更新消息列表
	private void updateMsgList(CMessage msg) {
		MyCallable call = new MyCallable(MessageType.UPDATE_CHAT_RECORDS, msg);
		FutureTask<Object> task = new FutureTask<Object>(call);
		Thread thread = new Thread(task);
		thread.start();
		try {
			msg = (CMessage)task.get();
			System.out.println("!r: "+msg.getReceiver().getStudentid()+"!s: "+msg.getSender().getStudentid()+"!t: "+msg.getMsgType()+"!c: "+(String)msg.getObj());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread.interrupt();
	}
	
	private List<CMessage> initMsgs() {
		List<CMessage> msgListTemp = null;
		MyCallable call = new MyCallable(MessageType.LOAD_CHAT_RECORDS, null);
		FutureTask<Object> task = new FutureTask<Object>(call);
		Thread thread = new Thread(task);
		thread.start();

		try {
			msgListTemp = (List<CMessage>)task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread.interrupt();
		return msgListTemp;
	}
	
    @Override  
    protected void onStop() {  
        mTimer.cancel();// 程序退出时cancel timer  
        super.onStop();  
    } 
}
