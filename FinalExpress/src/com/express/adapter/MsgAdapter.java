package com.express.adapter;

import java.util.ArrayList;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import entity.CMessage;
import entity.MessageType;

public class MsgAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private List<CMessage> msgs = new ArrayList<CMessage>();;
	private Context context;
	//private int resourceId;
	
	public MsgAdapter(Context context,List<CMessage> msg) {
		this.context = context;
		mInflater = LayoutInflater.from(this.context);
		this.msgs = msg;
	}
	
	public void addMessage(CMessage msg){
		msgs.add(msg);
		notifyDataSetChanged();
	}
	
	public void addMessages(List<CMessage> msgList){
		msgs.addAll(msgList);
		notifyDataSetChanged();
	}
	
	@Override
	public CMessage getItem(int position) {
		return msgs.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CMessage msg = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.msg_item, null);
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		
		if (msg.getReceiver().getStudentid().equals(LoginActivity.getUser().getStudentid())) {
			// 如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
			viewHolder.leftLayout.setVisibility(View.VISIBLE);
			viewHolder.rightLayout.setVisibility(View.GONE);
			byte[] temp = msg.getSender().getAvatar();
			Bitmap bitmap = null;
			if(temp != null)
			{
				bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
				viewHolder.lefticon.setImageBitmap(bitmap);
			}
			else
			{
				viewHolder.lefticon.setBackgroundResource(R.drawable.default_avatar);
			}
			//viewHolder.leftMsg.setText( msg.getSender()+" to " +msg.getReceiver()+" time: "+msg.getTime()+" content: "+(String) msg.getObj());
			viewHolder.leftMsg.setText((String) msg.getObj());
		}else{
			// 如果是发出的消息，则显示右边的消息布局，将左边的消息布局隐藏
			viewHolder.rightLayout.setVisibility(View.VISIBLE);
			viewHolder.leftLayout.setVisibility(View.GONE);
			byte[] temp1 = msg.getSender().getAvatar();
			Bitmap bitmap1 = null;
			if(temp1 != null)
			{
				bitmap1 = BitmapFactory.decodeByteArray(temp1, 0, temp1.length);
				viewHolder.righticon.setImageBitmap(bitmap1);
			}
			else
			{
				viewHolder.righticon.setBackgroundResource(R.drawable.default_avatar);
			}
			viewHolder.rightMsg.setText((String) msg.getObj());
			//viewHolder.rightMsg.setText(viewHolder.setData(msg));
			//viewHolder.rightMsg.setText(msg.getSender()+" from " +msg.getReceiver()+" time: "+msg.getTime()+" content: "+(String) msg.getObj());
		}
		return view;
	}



	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return msgs.size();
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}	
	
	class ViewHolder {
		RelativeLayout leftLayout;
		RelativeLayout rightLayout;
		TextView leftMsg;
		TextView rightMsg;
		ImageView lefticon;
		ImageView righticon;
		
		public ViewHolder(View view){
			leftLayout = (RelativeLayout) view.findViewById(R.id.left_layout);
			rightLayout = (RelativeLayout) view.findViewById(R.id.right_layout);
			leftMsg = (TextView) view.findViewById(R.id.left_msg);
			rightMsg = (TextView) view.findViewById(R.id.right_msg);
			lefticon = (ImageView) view.findViewById(R.id.left_headicon);
			righticon = (ImageView) view.findViewById(R.id.right_headicon);
		}
		/*
		public CharSequence setData(CMessage msg) {
			// TODO Auto-generated method stub
			return null;
		}*/
	}
}
