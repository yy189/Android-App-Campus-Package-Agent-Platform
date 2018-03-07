/*package com.express.adapter;

import java.util.List;

import com.example.finalexpress.R;

import entity.CMessage;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends ArrayAdapter<CMessage> {

	private int resourceId;
	public MessageAdapter(Context context, int resource, List<CMessage> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		resourceId=resource;
	}

	@Override
	public View getView( int position,View convertView,ViewGroup parent)
	{
		CMessage cmessage=getItem(position);//获取当前实例
		View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
		ImageView orderImage=(ImageView) view.findViewById(R.id.find_item_userImage);
		TextView orderPubName=(TextView) view.findViewById(R.id.find_item_username);
		//TextView orderPubDate=(TextView) view.findViewById(R.id.find_item_date);
		TextView orderPubDsecription=(TextView) view.findViewById(R.id.find_item_description);
		
		orderImage.setImageResource(cmessage);
		orderPubName.setText(cmessage.getReceiver().getUsername());
		//orderPubDate.setText(cmessage.getOrderDate());
		orderPubDsecription.setText(cmessage.getObj().toString());
		
		return view;
	}
}*/
