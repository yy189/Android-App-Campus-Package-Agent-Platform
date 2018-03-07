package com.express.adapter;

import java.util.List;

import com.example.finalexpress.R;
import com.express.activity.LoginActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import entity.CMessage;
import entity.User;

public class FindChaterAdapter extends ArrayAdapter<CMessage>{
	private int resourceId;
	
	public FindChaterAdapter(Context context, int resource,List<CMessage> chatList) {
	// TODO Auto-generated constructor stub
		super(context, resource, chatList);
		resourceId=resource;
    }


	@Override
	public View getView( int position,View convertView,ViewGroup parent)
	{
		CMessage currentMsg = getItem(position);//获取当前实例
		User user = new User();
		View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
		ImageView orderImage=(ImageView) view.findViewById(R.id.find_item_userImage);
		TextView orderPubName=(TextView) view.findViewById(R.id.find_item_username);
		TextView orderPubDate=(TextView) view.findViewById(R.id.find_item_date);
		TextView orderPubDsecription=(TextView) view.findViewById(R.id.find_item_description);
		if(currentMsg.getReceiver().getStudentid().equals(LoginActivity.getUser().getStudentid())){
			user = currentMsg.getSender();
		}else{
			user = currentMsg.getReceiver();
		}
		
		orderPubName.setText(user.getUsername());
		orderPubDate.setText(currentMsg.getTime());
		orderPubDsecription.setText((String)currentMsg.getObj());
		
		byte[] temp = currentMsg                                                                                                                         .getAvatar();
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
