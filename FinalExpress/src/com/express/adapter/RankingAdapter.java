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

public class RankingAdapter extends ArrayAdapter<User>{

	private int resourceId;
	public RankingAdapter(Context context, int resource, List<User> objects) {
		super(context, resource, objects);
		resourceId=resource;
	}
	
	@Override
	public View getView( int position,View convertView,ViewGroup parent)
	{
		User user=getItem(position);//获取当前实例
		View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
		
		ImageView friendImg=(ImageView) view.findViewById(R.id.ranking_item_userImage);
		TextView friendName=(TextView) view.findViewById(R.id.ranking_item_username);
		TextView friendPoint=(TextView) view.findViewById(R.id.ranking_item_description);
		ImageView rankImg=(ImageView) view.findViewById(R.id.ranking_item_image);
		if(position==0)
			rankImg.setBackgroundResource(R.drawable.rank_first);
		else if(position==1)
			rankImg.setBackgroundResource(R.drawable.rank_second);
		else if(position==2)
			rankImg.setBackgroundResource(R.drawable.rank_third);
		
		friendName.setText(user.getUsername());
		String point="积分   "+user.getPoint();
		friendPoint.setText(point);
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

