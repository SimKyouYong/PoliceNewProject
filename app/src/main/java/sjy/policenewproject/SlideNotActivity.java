package sjy.policenewproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyAdView;
import com.fsn.cauly.CaulyAdViewListener;
import com.fsn.cauly.CaulyCloseAd;
import com.fsn.cauly.CaulyCloseAdListener;

import sjy.policenewproject.common.Check_Preferences;



public class SlideNotActivity extends Activity implements CaulyAdViewListener , CaulyCloseAdListener {
	private LinearLayout adWrapper = null;
	Button screenlock;
	private static final String LOGTAG = "SKY";
	private CaulyAdView xmlAdView;
	private ScreenReceiver restartService;
	private Intent intent = null;
	CaulyCloseAd mCloseAd;
	@Override
	protected void onResume() {
		if (mCloseAd != null)
			mCloseAd.resume(this);
		super.onResume();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slidenot);


		initCauly();
		screenlock = (Button) findViewById(R.id.switch_on_off);

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
		findViewById(R.id.btn5).setOnClickListener(btnListener);
	}

	private void initCauly(){
		// CloseAd 초기화
		CaulyAdInfo closeAdInfo = new CaulyAdInfoBuilder("U6yUgNCr").build();
		mCloseAd = new CaulyCloseAd();
		/*
		 * Optional //원하는 버튼의 문구를 설젇 할 수 있다. mCloseAd.setButtonText("취소", "종료");
		 * //원하는 텍스트의 문구를 설젇 할 수 있다. mCloseAd.setDescriptionText("종료하시겠습니까?");
		 */
		mCloseAd.setAdInfo(closeAdInfo);
		mCloseAd.setCloseAdListener(this);
		// 선택사항: XML의 AdView 항목을 찾아 Listener 설정
		xmlAdView = (CaulyAdView) findViewById(R.id.xmladview);
		xmlAdView.setAdViewListener(this);

		adWrapper = (LinearLayout) findViewById(R.id.adWrapper);
	}

	@Override
	public void onReceiveAd(CaulyAdView adView, boolean isChargeableAd) {
		// 광고 수신 성공 & 노출된 경우 호출됨.
		// 수신된 광고가 무료 광고인 경우 isChargeableAd 값이 false 임.
		if (isChargeableAd == false) {
			Log.e("SKY", "free banner AD received.");
		}
		else {
			Log.e("SKY", "normal banner AD received.");
		}
	}

	@Override
	public void onFailedToReceiveAd(CaulyAdView adView, int errorCode, String errorMsg) {
		// 배너 광고 수신 실패할 경우 호출됨.
		Log.e("SKY", "failed to receive banner AD.");
		adWrapper.setVisibility(View.GONE);
	}

	@Override
	public void onShowLandingScreen(CaulyAdView adView) {
		// 광고 배너를 클릭하여 랜딩 페이지가 열린 경우 호출됨.
		Log.e("SKY", "banner AD landing screen opened.");
	}

	@Override
	public void onCloseLandingScreen(CaulyAdView adView) {
		// 광고 배너를 클릭하여 열린 랜딩 페이지가 닫힌 경우 호출됨.
		Log.e("SKY", "banner AD landing screen closed.");
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

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showCloseAd(null);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void showCloseAd(View button)
	{
		// 앱을 처음 설치하여 실행할 때, 필요한 리소스를 다운받았는지 여부.
		if (mCloseAd != null && mCloseAd.isModuleLoaded()) {
			mCloseAd.show(this);
			//광고의 수신여부를 체크한 후 노출시키고 싶은 경우, show(this) 대신 request(this)를 호출.
			//onReceiveCloseAd에서 광고를 정상적으로 수신한 경우 , show(this)를 통해 광고 노출

		} else {
			// 광고에 필요한 리소스를 한번만 다운받는데 실패했을 때 앱의 종료팝업 구현
			showDefaultClosePopup();
		}
	}
	private void showDefaultClosePopup() {
		new AlertDialog.Builder(this).setTitle("").setMessage("종료 하시겠습니까?")
				.setPositiveButton("예", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).setNegativeButton("아니요", null).show();
	}
	@Override
	public void onFailedToReceiveCloseAd(CaulyCloseAd ad, int errCode,
										 String errMsg) {
	}
	// CloseAd의 광고를 클릭하여 앱을 벗어났을 경우 호출되는 함수이다.
	@Override
	public void onLeaveCloseAd(CaulyCloseAd ad) {
	}
	// CloseAd의 request()를 호출했을 때, 광고의 여부를 알려주는 함수이다.
	@Override
	public void onReceiveCloseAd(CaulyCloseAd ad, boolean isChargable) {
	}
	// 왼쪽 버튼을 클릭 하였을 때, 원하는 작업을 수행하면 된다.
	@Override
	public void onLeftClicked(CaulyCloseAd ad) {
	}
	// 오른쪽 버튼을 클릭 하였을 때, 원하는 작업을 수행하면 된다.
	// Default로는 오른쪽 버튼이 종료로 설정되어있다.
	@Override
	public void onRightClicked(CaulyCloseAd ad) {
		finish();
	}
	@Override
	public void onShowedCloseAd(CaulyCloseAd ad, boolean isChargable) {
	}
}
