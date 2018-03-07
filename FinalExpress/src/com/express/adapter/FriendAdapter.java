package com.express.adapter;

import java.util.List;

import com.example.finalexpress.R;

import entity.Order;
import entity.User;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendAdapter extends ArrayAdapter<User>{

	private int resourceId;
	public FriendAdapter(Context context, int resource, List<User> objects) {
		super(context, resource, objects);
		resourceId=resource;
	}
	
	@Override
	public View getView( int position,View convertView,ViewGroup parent)
	{
		User user=getItem(position);//获取当前实例
		View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
		
		ImageView friendImg=(ImageView) view.findViewById(R.id.find_item_userImage);
		TextView friendName=(TextView) view.findViewById(R.id.find_item_username);
		
		friendName.setText(user.getUsername());
		
		byte[] temp = user.getAvatar();
		Bitmap bitmap = null;
		if(temp != null)
		{
			bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
			friendImg.setImageBitmap(bitmap);
		}
		else
		{
			friendImg.setBackgroundResource(R.drawable.default_avatar);
		}
		return view;
	}
}
