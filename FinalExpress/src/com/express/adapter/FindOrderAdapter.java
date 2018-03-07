package com.express.adapter;


import java.util.List;

import com.example.finalexpress.R;

import entity.Order;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FindOrderAdapter extends ArrayAdapter<Order> {

	private int resourceId;
	
	public FindOrderAdapter(Context context, int resource, List<Order> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId=resource;
	}

	@Override
	public View getView( int position,View convertView,ViewGroup parent)
	{
		Order order=getItem(position);//获取当前实例
		View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
		ImageView orderImage=(ImageView) view.findViewById(R.id.find_item_userImage);
		TextView orderPubName=(TextView) view.findViewById(R.id.find_item_username);
		TextView orderPubDate=(TextView) view.findViewById(R.id.find_item_date);
		TextView orderPubDsecription=(TextView) view.findViewById(R.id.find_item_description);
		
		orderPubName.setText(order.getPublishName());
		orderPubDate.setText(order.getPublishDate());
		orderPubDsecription.setText(order.getExpressDescribe());
		
		byte[] temp = order                                                                                                                         .getAvatar();
		Bitmap bitmap = null;
		if(temp != null)
		{
			bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
			orderImage.setImageBitmap(bitmap);
		}
		else
		{
			orderImage.setBackgroundResource(R.drawable.default_avatar);
		}
		
		return view;
	}
}
