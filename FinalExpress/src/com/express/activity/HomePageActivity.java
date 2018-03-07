package com.express.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import entity.CMessage;
import entity.MessageType;
import entity.Order;
import entity.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import com.example.finalexpress.R;
import com.express.adapter.FindChaterAdapter;
import com.express.adapter.FindOrderAdapter;
//import com.express.adapter.FindOrderAdapter;
import com.express.adapter.FriendAdapter;
import com.express.service.MyCallable;
import com.nineoldandroids.view.ViewHelper;


public class HomePageActivity extends FragmentActivity
{

	Button setAvatar_btn;
    List<Order>findOrderList=new ArrayList<Order>();
    List<CMessage>chatList=new ArrayList<CMessage>();
    List<User>friendList=new ArrayList<User>();
	private FindOrderAdapter findOrderAdapter;
	private FindChaterAdapter findChaterAdapter;
	private FriendAdapter friendAdapter;
	private int flagTag = 1; //表示点击哪个底部菜单
	
	private ListView listView;
	
	private DrawerLayout mDrawerLayout;
	
	private Bitmap avatar;
	private Uri imageUri;
	public static final int TAKE_PHOTO = 1;
	public static final int CROP_PHOTO =2;
	
	private static User receiver = new User(); //接收聊天消息的用户
	
	public static User getReceiver()
	{
		return receiver;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.homepage_layout);

		initView();
		initEvents();
		
	    TextView point=(TextView) findViewById(R.id.myreword_point);
		point.setText("我的积分       "+LoginActivity.getUser().getPoint());
	    
		setAvatar_btn = (Button)findViewById(R.id.setAvatar_btn);
		avatar = getDiskBitmap("mnt/sdcard/" + LoginActivity.getUser().getStudentid() + ".jpg");//初始化时从SD卡读取头像
		if(avatar != null)//设置头像
		{
			Drawable drawable = new BitmapDrawable(avatar);
			setAvatar_btn.setBackgroundDrawable(drawable);
		}
		
		FindFunc(findViewById(R.id.id_drawerLayout));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,View view,int position,long id){
				if(flagTag == 1){
					final Order order = findOrderList.get(position);
					User user = LoginActivity.getUser();
					final CMessage msg = new CMessage(); 
					msg.setObj(order);
					msg.setSender(user);
					new AlertDialog.Builder(HomePageActivity.this).setTitle("确认").setMessage("确认帮忙代领？")
							.setNegativeButton("取消", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									//finish();
								}
							}).setPositiveButton("确认", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									MyCallable call = new MyCallable(MessageType.CONFIRM, msg);
									FutureTask<Object> task = new FutureTask<Object>(call);
									Thread thread = new Thread(task);
									thread.start();
								}
							}).show();					
				}else if(flagTag == 2){
					if(chatList.get(position).getReceiver().getStudentid().equals(LoginActivity.getUser().getStudentid())){
						receiver = chatList.get(position).getSender();
					}else{
						receiver = chatList.get(position).getReceiver();
					}
					
					
					Intent intent = new Intent(HomePageActivity.this,ChatActivity.class);
					//intent.putExtra("user", user);//这里要改一下！！！！！！！！！！！
					startActivity(intent);
					
				}else if(flagTag == 3){
					receiver = friendList.get(position);
					
					Intent intent = new Intent(HomePageActivity.this,ChatActivity.class);
					startActivity(intent);
				}
				
			}
		});
		//listview的点击事件，根据flagTag选择对应的响应
		
		
		ImageView findImg = (ImageView)findViewById(R.id.find);
		ImageView messageImg = (ImageView)findViewById(R.id.message);
		ImageView friendImg = (ImageView)findViewById(R.id.friends);
		//ImageButton queryImg = (ImageButton)findViewById(R.id.query);
		
		findImg.setOnTouchListener(new OnTouchListener() {            
	        @Override  
	        public boolean onTouch(View v, MotionEvent event) {  
	            if(event.getAction()==MotionEvent.ACTION_DOWN){  
	                v.setBackgroundResource(R.drawable.icon_find_pressed);  
	            }else if(event.getAction()==MotionEvent.ACTION_UP){  
	                v.setBackgroundResource(R.drawable.icon_find);  
	            }  
	            return false;  
	        }  
	    });  
		messageImg.setOnTouchListener(new OnTouchListener() {            
	        @Override  
	        public boolean onTouch(View v, MotionEvent event) {  
	            // TODO Auto-generated method stub  
	            if(event.getAction()==MotionEvent.ACTION_DOWN){  
	                v.setBackgroundResource(R.drawable.icon_message);  
	            }else if(event.getAction()==MotionEvent.ACTION_UP){  
	                v.setBackgroundResource(R.drawable.icon_message_pressed);  
	            }  
	            return false;  
	        }  
	    });  
		friendImg.setOnTouchListener(new OnTouchListener() {            
	        @Override  
	        public boolean onTouch(View v, MotionEvent event) {  
	            // TODO Auto-generated method stub  
	            if(event.getAction()==MotionEvent.ACTION_DOWN){  
	                v.setBackgroundResource(R.drawable.icon_friend_pressed);  
	            }else if(event.getAction()==MotionEvent.ACTION_UP){  
	                v.setBackgroundResource(R.drawable.icon_friend);  
	            }  
	            return false;  
	        }  
	    });  
		
		setAvatar_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AvatarDialog avatarDialog = new AvatarDialog(HomePageActivity.this, new AvatarDialog.LeaveAvatarDialogListener() {
					
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						switch(view.getId()){
						case R.id.camera_btn:
							break;
						case R.id.photo_btn:
							break;
						default:break;
						}
					}

					@Override
					public void leaveAvatarDialog(int value) {
						// TODO Auto-generated method stub
						if(value == 0)
						{
							File outputImage = new File(Environment.getExternalStorageDirectory(), LoginActivity.getUser().getStudentid()+".jpg");
							try{
								if(outputImage.exists())
								{
									outputImage.delete();
								}
								outputImage.createNewFile();
							}catch(IOException e)
							{
								e.printStackTrace();
							}
							imageUri = Uri.fromFile(outputImage);
							Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
							intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
							startActivityForResult(intent, TAKE_PHOTO);//启动相机程序
						}
						else if(value == 1)
						{
							File outputImage=new File(Environment.getExternalStorageDirectory(), LoginActivity.getUser().getStudentid()+".jpg");
							try{
								if(outputImage.exists())
								{
									outputImage.delete();
								}
								outputImage.createNewFile();
							}catch(IOException e)
							{
								e.printStackTrace();
							}
							imageUri = Uri.fromFile(outputImage);
							Intent intent = new Intent("android.intent.action.GET_CONTENT");
							intent.setType("image/*");
							intent.putExtra("crop", true);
							intent.putExtra("scale", true);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
							startActivityForResult(intent, CROP_PHOTO);
						}
					}
				});
				avatarDialog.show();
			}
		});
		
	}
	
	public void MenuRight(View view)
	{
		mDrawerLayout.openDrawer(Gravity.RIGHT);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
				Gravity.RIGHT);
	}
	
	
	//初始化界面
	private void initView()
	{
		mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
				Gravity.RIGHT);
	}
	
	//实现侧滑
	@SuppressWarnings("deprecation")
	private void initEvents()
	{
		//setDrawerListener
		mDrawerLayout.setDrawerListener(new DrawerListener()
		{
			@Override
			public void onDrawerStateChanged(int newState)
			{
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset)
			{
				View mContent = mDrawerLayout.getChildAt(0);
				View mMenu = drawerView;
				float scale = 1 - slideOffset;
				float rightScale = 0.8f + scale * 0.2f;

				if (drawerView.getTag().equals("LEFT"))
				{

					float leftScale = 1 - 0.3f * scale;

					ViewHelper.setScaleX(mMenu, leftScale);
					ViewHelper.setScaleY(mMenu, leftScale);
					ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
					ViewHelper.setTranslationX(mContent,
							mMenu.getMeasuredWidth() * (1 - scale));
					ViewHelper.setPivotX(mContent, 0);
					ViewHelper.setPivotY(mContent,
							mContent.getMeasuredHeight() / 2);
					mContent.invalidate();
					ViewHelper.setScaleX(mContent, rightScale);
					ViewHelper.setScaleY(mContent, rightScale);
				} else
				{
					ViewHelper.setTranslationX(mContent,
							-mMenu.getMeasuredWidth() * slideOffset);
					ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
					ViewHelper.setPivotY(mContent,
							mContent.getMeasuredHeight() / 2);
					mContent.invalidate();
					ViewHelper.setScaleX(mContent, rightScale);
					ViewHelper.setScaleY(mContent, rightScale);
				}

			}

			@Override
			public void onDrawerOpened(View drawerView)
			{
				
			}

			@Override
			public void onDrawerClosed(View drawerView)
			{
				mDrawerLayout.setDrawerLockMode(
						DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
			}
		});
	}
	
    ///////////////////左侧菜单响应函数///////////////////////////
	
	//我发布的代领信息
	public void myPublish(View v){
		Intent intent = new Intent(HomePageActivity.this,LPublishActivity.class);
	    startActivity(intent);
	}
	
	//我帮忙代领的快递记录
	public void myReceive(View v){
		Intent intent = new Intent(HomePageActivity.this,LReceiveActivity.class);
	    startActivity(intent);
	}
	
	//天使榜
	public void rankinglist(View v){
		Intent intent = new Intent(HomePageActivity.this,LRankingListActivity.class);
	    startActivity(intent);
	}
	
	//个人设置
	public void setting(View v){
		Intent intent = new Intent(HomePageActivity.this,LSettingActivity.class);
	    startActivity(intent);
	}
		
	////////////////////右侧菜单栏响应////////////////////////////
	public void SendMessage(View v){
		Intent intent = new Intent(HomePageActivity.this,RSendMessageActivity.class);
	    startActivity(intent);
	}
	
	public void AddFriends(View v){
		Intent intent = new Intent(HomePageActivity.this,RAddFriendsActivity.class);
	    startActivity(intent);
	}
	
	public void MaybeFriends(View v){
	   Intent intent = new Intent(HomePageActivity.this,RMaybeFriendsActivity.class);
	   startActivity(intent);
		
	}	
	
	/////////////////////homepage页面下方功能响应///////////////////////
	
	//发现
	public void FindFunc(View v){
		findOrderList.clear();
		flagTag = 1;
		MyCallable call = new MyCallable(MessageType.ORDER_REFRESH, null);
		FutureTask<Object> task = new FutureTask<Object>(call);
		Thread thread = new Thread(task);
		thread.start();
		List<Order> orders = null;
		try {
			orders = (List<Order>)task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread.interrupt();
		findOrderList.addAll(orders);
		findOrderAdapter = new FindOrderAdapter(HomePageActivity.this, R.layout.listview_find_item, findOrderList);
		listView=(ListView) findViewById(R.id.list_view);
		listView.setAdapter(findOrderAdapter);
			
	}
	
	//聊天
	public void MessageFunc(View v){
		chatList.clear();
		flagTag = 2;
		MyCallable call = new MyCallable(MessageType.CHATER_REFRESH, null);
		FutureTask<Object> task = new FutureTask<Object>(call);
		Thread thread = new Thread(task);
		thread.start();
		List<CMessage> msgs = null;
		try {
			msgs = (List<CMessage>)task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread.interrupt();
		chatList.addAll(msgs);
		findChaterAdapter = new FindChaterAdapter(HomePageActivity.this, R.layout.listview_find_item, chatList);
		listView=(ListView) findViewById(R.id.list_view);
		listView.setAdapter(findChaterAdapter);
	}
	
	//通讯录
	public void FriendsFunc(View v){
		friendList.clear();
		flagTag = 3;
		MyCallable call = new MyCallable(MessageType.MYFRIEND, LoginActivity.getUser());
		FutureTask<Object> task = new FutureTask<Object>(call);
		Thread thread = new Thread(task);
		thread.start();
		
		try {
			List<User> myfriends = (List<User>)task.get();
			
			friendList.addAll(myfriends);
			
			friendAdapter=new FriendAdapter(HomePageActivity.this, R.layout.listview_find_item, friendList);
			listView=(ListView) findViewById(R.id.list_view);
			listView.setAdapter(friendAdapter);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	//查询快递
	public void SearchFunc(View v){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("http://www.kuaidi100.com/"));
		startActivity(intent);		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch(requestCode)
		{
		case TAKE_PHOTO:
			if(resultCode == RESULT_OK)
			{
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);//启动裁剪程序
			}
			break;
		case CROP_PHOTO:
			if(resultCode == RESULT_OK)
			{
				try{
					avatar = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));//将剪裁后的照片显示出来
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					//读取图片到ByteArrayOutputStream
					avatar.compress(Bitmap.CompressFormat.PNG, 100, baos);
					byte[] bytes = baos.toByteArray();
					MyCallable call = new MyCallable(MessageType.SET_AVATAR, bytes);
					FutureTask<Object> task = new FutureTask<Object>(call);
					Thread thread = new Thread(task);
					thread.start();
					boolean b = false;
					try {
						b = ((Boolean)task.get()).booleanValue();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					thread.interrupt();
					if(b == true)
					{
						Drawable drawable = new BitmapDrawable(avatar);
						setAvatar_btn.setBackgroundDrawable(drawable);
					}
					else
					{
						Toast.makeText(HomePageActivity.this, "上传头像失败！", Toast.LENGTH_LONG).show();
					}
					
				}catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
			break;
			default:
				break;
		}
	}
	
	private Bitmap getDiskBitmap(String pathString)
	{
		Bitmap bitmap = null;
		try{
			File file = new File(pathString);
			if(file.exists())
			{
				bitmap = BitmapFactory.decodeFile(pathString);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return bitmap;
	}
}

