package com.express.adapter;

import java.util.List;

import com.example.finalexpress.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import entity.Order;
import entity.User;

public class PublishAdapter extends ArrayAdapter<Order>{
	private int resourceId;
	public PublishAdapter(Context context, int resource, List<Order> objects) {
		super(context, resource, objects);
		resourceId=resource;
	}
	
	@Override
	public View getView(int position,View convertView,ViewGroup parent)
	{
		Order order = getItem(position);//获取当前实例
		View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
		
		//ImageView userImg = (ImageView)view.findViewById(R.id.find_item_userImage);
		TextView username = (TextView)view.findViewById(R.id.publish_item_username);
		TextView date = (TextView)view.findViewById(R.id.publish_item_date);
		TextView description = (TextView)view.findViewById(R.id.publish_item_description);
		
		System.out.println(order.getAccepted());
		if(order.getAccepted()==false)
		{
			username.setText("未领取");
		}
		else 
		{
			username.setText("已领取："+order.getReceiveUserId());
		}
		
		//username.setText(order.getPublishName());
		date.setText(order.getPublishDate());
		description.setText(order.getExpressDescribe());
		
//		byte[] temp = order.getAvatar();
//		Bitmap bitmap = null;
//		if(temp != null)
//		{
//			System.out.println("1");
//			bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
//			System.out.println("2");
//			userImg.setImageBitmap(bitmap);
//		}
//		else
//		{
//			userImg.setBackgroundResource(R.drawable.default_avatar);
//		}
		return view;
		
	}
}
