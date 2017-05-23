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

import com.kakao.adfit.publisher.AdInterstitial;
import com.kakao.adfit.publisher.AdView;
import com.kakao.adfit.publisher.AdView.AnimationType;
import com.kakao.adfit.publisher.AdView.OnAdClickedListener;
import com.kakao.adfit.publisher.AdView.OnAdClosedListener;
import com.kakao.adfit.publisher.AdView.OnAdFailedListener;
import com.kakao.adfit.publisher.AdView.OnAdLoadedListener;
import com.kakao.adfit.publisher.AdView.OnAdWillLoadListener;
import com.kakao.adfit.publisher.impl.AdError;

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
	private AdView adView = null;

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
		adView = (AdView) view.findViewById(R.id.adview);
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

		
		initAdam();
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

		if (adView != null) {
			adView.destroy();
			adView = null;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	private void initAdam() {
		AdInterstitial mAdInterstitial = null;
		// 1. 전면형 광고 객체 생성
		mAdInterstitial = new AdInterstitial(av_);
		// 2. 전면형 광고 광고단위ID를 설정한다.
		//mAdInterstitial.setClientId(ADVIEW_FULL);
		// 3. (선택)전면형 광고 다운로드시에 실행할 리스너
		mAdInterstitial.setOnAdLoadedListener(new OnAdLoadedListener() {
			@Override
			public void OnAdLoaded() {
				Log.i("InterstitialTab", "광고가 로딩되었습니다.");
			}
		});
		// 4. (선택)전면형 광고 다운로드 실패시에 실행할 리스너
		mAdInterstitial.setOnAdFailedListener(new OnAdFailedListener() {
			@Override
			public void OnAdFailed(AdError error, String errorMessage) {
				// Toast.makeText(MainActivity.this,errorMessage,
				// Toast.LENGTH_LONG).show();
				// 광고 표시실패시 배너광고
				Log.i("InterstitialTab", "전면형 광고 표시 실패/" + errorMessage);
				initAdam2();
			}
		});
		// 5. (선택)전면형 광고를 닫을 시에 실행할 리스너
		mAdInterstitial.setOnAdClosedListener(new OnAdClosedListener() {
			@Override
			public void OnAdClosed() {
				Log.i("InterstitialTab", "광고를 닫았습니다. ");
			}
		});
		// 6. 전면형 광고를 불러온다.
		mAdInterstitial.loadAd();
	}
	private void initAdam2() {
		// Ad@m sdk 초기화 시작
		adView.setRequestInterval(5);

		// 광고 클릭시 실행할 리스너
		adView.setOnAdClickedListener(new OnAdClickedListener() {
			public void OnAdClicked() {
				Log.i(LOGTAG, "광고를 클릭했습니다.");
			}
		});

		// 광고 내려받기 실패했을 경우에 실행할 리스너
		adView.setOnAdFailedListener(new OnAdFailedListener() {
			public void OnAdFailed(AdError arg0, String arg1) {
				adWrapper.setVisibility(View.INVISIBLE);
				Log.e(LOGTAG, arg1);
			}
		});

		// 광고를 정상적으로 내려받았을 경우에 실행할 리스너
		adView.setOnAdLoadedListener(new OnAdLoadedListener() {
			public void OnAdLoaded() {
				// 광고 제거
					adWrapper.setVisibility(View.VISIBLE);
					Log.e(LOGTAG, "광고가 정상적으로 로딩되었습니다.");
			}
		});

		// 광고를 불러올때 실행할 리스너
		adView.setOnAdWillLoadListener(new OnAdWillLoadListener() {
			public void OnAdWillLoad(String arg1) {
				Log.e(LOGTAG, "광고를 불러옵니다. : " + arg1);
			}
		});

		// 광고를 닫았을때 실행할 리스너
		adView.setOnAdClosedListener(new OnAdClosedListener() {
			public void OnAdClosed() {
				Log.e(LOGTAG, "광고를 닫았습니다.");
			}
		});

		// 할당 받은 clientId 설정
		adView.setClientId("DAN-1ib0yvnhuimur");

		adView.setRequestInterval(12);

		// Animation 효과 : 기본 값은 AnimationType.NONE
		adView.setAnimationType(AnimationType.FLIP_HORIZONTAL);

		adView.setVisibility(View.VISIBLE);
	}
}