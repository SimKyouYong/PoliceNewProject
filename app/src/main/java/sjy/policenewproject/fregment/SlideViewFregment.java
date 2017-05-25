package sjy.policenewproject.fregment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import sjy.policenewproject.DetailActivity;
import sjy.policenewproject.R;
import sjy.policenewproject.ScreenReceiver;
import sjy.policenewproject.ScreenService;
import sjy.policenewproject.common.Check_Preferences;
import sjy.policenewproject.common.FragmentEx;


@SuppressLint("ValidFragment")
public class SlideViewFregment extends FragmentEx implements OnTouchListener{
	private static Context mContext;
	Activity av_;    //  액티비티 av_로 지정
	private static final String LOGTAG = "SKY";

	boolean popup = false;
	public static  RelativeLayout mainBody;
	WebView pWebView;
	ProgressDialog dialog;
	private ValueCallback<Uri> mUploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 1;
	public View vi;
	String openURL="";

	ImageButton btn1,btn2,btn3,btn4,btn5,btn6;
	public LinearLayout bottomMenu;
	private boolean clearHistory = false;
	private LinearLayout adWrapper = null;

	private ScreenReceiver restartService;

	Button screenlock;
	@SuppressLint("ValidFragment")
	public SlideViewFregment(Context context , Activity av , int tag) {  //  액티비티 지정, 프래그먼트
		mContext = context;  
		av_ = av;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


		Log.e("SKY" , "--MAIN START--");
		View view = inflater.inflate(R.layout.activity_slidefregment, null);
		adWrapper = (LinearLayout) view.findViewById(R.id.adWrapper);
		screenlock = (Button) view.findViewById(R.id.btn5);
		
		Log.e("SKY", "VAL :: " + Check_Preferences.getAppPreferencesboolean(av_, "ScreenLock"));
		if (Check_Preferences.getAppPreferencesboolean(av_, "ScreenLock")) {
			//on
			screenlock.setBackgroundResource(R.mipmap.policedream_mainbtn_06_0n);
			/*
			//리스타트 서비스 생성
	        restartService = new ScreenReceiver();
	        intent = new Intent(av_, ScreenReceiver.class);
	 
	 
	        IntentFilter intentFilter = new IntentFilter("co.kr.newpolice.ScreenService");
	        //브로드 캐스트에 등록
	        av_.registerReceiver(restartService,intentFilter);
	        // 서비스 시작
	        av_.startService(intent);
	        */
		}else{
			//off
			screenlock.setBackgroundResource(R.mipmap.policedream_mainbtn_06_0ff);
			//av_.stopService(intent);
		}
		view.findViewById(R.id.btn1).setOnClickListener(btnListener); 
		view.findViewById(R.id.btn2).setOnClickListener(btnListener); 
		view.findViewById(R.id.btn3).setOnClickListener(btnListener); 
		view.findViewById(R.id.btn4).setOnClickListener(btnListener); 
		view.findViewById(R.id.btn5).setOnClickListener(btnListener); 

		
		return view;

	}
	//버튼 리스너 구현 부분 
	View.OnClickListener btnListener = new View.OnClickListener() {
		@SuppressWarnings("deprecation")
		public void onClick(View v) {
			Intent it = new Intent(mContext, DetailActivity.class);
			switch (v.getId()) {
			case R.id.btn1:
				it.putExtra("tag", "형법");
				startActivity(it);
				break;
			case R.id.btn2:
				it.putExtra("tag", "형소법");
				startActivity(it);
				break;
			case R.id.btn3:
				it.putExtra("tag", "경찰학개론");
				startActivity(it);
				break;
			case R.id.btn4:
				break;
			case R.id.btn5:
				//스크린락 
				Intent intent = new Intent(av_, ScreenService.class);

				if (!Check_Preferences.getAppPreferencesboolean(av_, "ScreenLock")) {
					//on
					screenlock.setBackgroundResource(R.mipmap.policedream_mainbtn_06_0n);
					av_.startService(intent);	
					//리스타트 서비스 생성
			        restartService = new ScreenReceiver();
			        intent = new Intent(av_, ScreenReceiver.class);
			 
			 
			        IntentFilter intentFilter = new IntentFilter("co.kr.newpolice.ScreenService");
			        //브로드 캐스트에 등록
			        av_.registerReceiver(restartService,intentFilter);
			        // 서비스 시작
			        av_.startService(intent);
			        Check_Preferences.setAppPreferences(av_, "ScreenLock", true);
				}else{
					//off
					screenlock.setBackgroundResource(R.mipmap.policedream_mainbtn_06_0ff);
					av_.stopService(intent);
					Check_Preferences.setAppPreferences(av_, "ScreenLock", false);
				}
				break;
			}
			

		}
	};
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}