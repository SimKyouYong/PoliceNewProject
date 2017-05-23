package sjy.policenewproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.kakao.adfit.publisher.AdView.*;
import com.kakao.adfit.publisher.AdView;
import com.kakao.adfit.publisher.impl.AdError;

import sjy.policenewproject.common.Check_Preferences;


public class SlideNotActivity extends Activity {
	private LinearLayout adWrapper = null;
	private AdView adView = null;
	Button screenlock;
	private static final String LOGTAG = "SKY";

	private ScreenReceiver restartService;
	private Intent intent = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slidenot);

		adWrapper = (LinearLayout) findViewById(R.id.adWrapper);
		adView = (AdView) findViewById(R.id.adview);
		screenlock = (Button) findViewById(R.id.btn5);

		intent = new Intent(SlideNotActivity.this, ScreenService.class);
		if (Check_Preferences.getAppPreferencesboolean(SlideNotActivity.this, "ScreenLock")) {
			//on
			screenlock.setBackgroundResource(R.mipmap.policedream_mainbtn_06_0n);
			startService(intent);
		}else{
			//off
			screenlock.setBackgroundResource(R.mipmap.policedream_mainbtn_06_0ff);
		}
		findViewById(R.id.btn1).setOnClickListener(btnListener); 
		findViewById(R.id.btn2).setOnClickListener(btnListener); 
		findViewById(R.id.btn3).setOnClickListener(btnListener); 
		findViewById(R.id.btn4).setOnClickListener(btnListener); 
		findViewById(R.id.btn5).setOnClickListener(btnListener);
		initAdam();
	}
	//버튼 리스너 구현 부분 
	View.OnClickListener btnListener = new View.OnClickListener() {
		@SuppressWarnings("deprecation")
		public void onClick(View v) {
			Intent it = new Intent(SlideNotActivity.this, DetailActivity.class);
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
				Intent it3 = new Intent(Intent.ACTION_SEND);
				it.setType("plain/text");
				// 수신인 주소 - tos배열의 값을 늘릴 경우 다수의 수신자에게 발송됨
				String[] tos = { "pongjjun@gmail.com" };
				it3.putExtra(Intent.EXTRA_EMAIL, tos);
				it3.putExtra(Intent.EXTRA_SUBJECT, "[문제추가 요청]" + "문제 추가 요청 드립니다.");
				it3.putExtra(Intent.EXTRA_TEXT, "파일을 첨부해주세요.(Execel 형식으로 정리해서 주시면 감사합니다.");
				startActivity(it3);
				break;
			case R.id.btn5:
				Log.e("SKY", "--btn5--");
				//스크린락 
				if (!Check_Preferences.getAppPreferencesboolean(SlideNotActivity.this, "ScreenLock")) {
					//on
					screenlock.setBackgroundResource(R.mipmap.policedream_mainbtn_06_0n);
					intent = new Intent(SlideNotActivity.this, ScreenService.class);
					startService(intent);
					Check_Preferences.setAppPreferences(SlideNotActivity.this, "ScreenLock", true);
				}else{
					//off
					screenlock.setBackgroundResource(R.mipmap.policedream_mainbtn_06_0ff);
					intent = new Intent(SlideNotActivity.this, ScreenService.class);
					stopService(intent);
					Check_Preferences.setAppPreferences(SlideNotActivity.this, "ScreenLock", false);
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

	private void initAdam() {
		// Ad@m sdk 초기화 시작
		adView.setRequestInterval(5);

		// 광고 클릭시 실행할 리스너
		adView.setOnAdClickedListener(new OnAdClickedListener() {
			public void OnAdClicked() {
				Log.i("SKY", "광고를 클릭했습니다.");
			}
		});

		// 광고 내려받기 실패했을 경우에 실행할 리스너
		adView.setOnAdFailedListener(new OnAdFailedListener() {
			public void OnAdFailed(AdError arg0, String arg1) {
				adWrapper.setVisibility(View.INVISIBLE);
				Log.e("SKY", "setOnAdFailedListener :: " + arg0.toString());
				Log.e("SKY", "setOnAdFailedListener :: " + arg1);
			}
		});

		// 광고를 정상적으로 내려받았을 경우에 실행할 리스너
		adView.setOnAdLoadedListener(new OnAdLoadedListener() {
			public void OnAdLoaded() {
				// 광고 제거
				adWrapper.setVisibility(View.VISIBLE);
				Log.e("SKY", "광고가 정상적으로 로딩되었습니다.");
			}
		});

		// 광고를 불러올때 실행할 리스너
		adView.setOnAdWillLoadListener(new OnAdWillLoadListener() {
			public void OnAdWillLoad(String arg1) {
				Log.e("SKY", "광고를 불러옵니다. : " + arg1);
			}
		});

		// 광고를 닫았을때 실행할 리스너
		adView.setOnAdClosedListener(new OnAdClosedListener() {
			public void OnAdClosed() {
				Log.e("SKY", "광고를 닫았습니다.");
			}
		});

		// 할당 받은 clientId 설정
		adView.setClientId("DAN-t4cbz4yyy0xl");

		adView.setRequestInterval(12);

		// Animation 효과 : 기본 값은 AnimationType.NONE
		adView.setAnimationType(AdView.AnimationType.FLIP_HORIZONTAL);

		adView.setVisibility(View.VISIBLE);
	}
}
