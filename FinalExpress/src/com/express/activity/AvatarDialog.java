package com.express.activity;


import com.example.finalexpress.R;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AvatarDialog extends Dialog implements OnClickListener{
	private Button btnCamera, btnPhoto;
	private LeaveAvatarDialogListener listener;
	private Uri imageUri;
	
	public AvatarDialog(Context context, LeaveAvatarDialogListener listener) {
		super(context);
		this.listener = listener;
	}
	
	public interface LeaveAvatarDialogListener{   
        public void onClick(View view);
        public void leaveAvatarDialog(int value);
    }   

	
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.avatar_dialog);
        btnCamera = (Button)findViewById(R.id.camera_btn);
        btnPhoto = (Button)findViewById(R.id.photo_btn);
        btnCamera.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.leaveAvatarDialog(0);
				dismiss();
			}
		});
        btnPhoto.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.leaveAvatarDialog(1);
				dismiss();
			}
		});
    }
    

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		listener.onClick(v);
	}
}

