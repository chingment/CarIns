package com.uplink.carins.ui.dialog;

import com.uplink.carins.R;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomDialogLoading extends Dialog {

	public CustomDialogLoading(Context context) {
		super(context,R.style.WeixinLoadingDialog);
		this.context = context;
		initDialog(context);
	}
	
	private ImageView ivProgress;
	private TextView tvInfo;
	private Context context;
	
	
	private void initDialog(Context context) {
		setContentView(R.layout.dialog_loading);
		ivProgress = (ImageView)findViewById(R.id.img);
		tvInfo = (TextView)findViewById(R.id.tipTextView);
		// 显示文本
		tvInfo.setText("正在加载...");
	}
	
	public void showDialog(){
		Animation animation = AnimationUtils.loadAnimation(context,
				R.anim.dialog_load_animation);
		// 显示动画
		ivProgress.startAnimation(animation);
		show();
	}
	
	public void cancelDialog(){
		ivProgress.clearAnimation();
		this.cancel();
	}
	
	public void setProgressText(String text){
		tvInfo.setText(text);
	}

}
