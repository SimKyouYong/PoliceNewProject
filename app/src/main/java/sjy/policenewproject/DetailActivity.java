package sjy.policenewproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fsn.cauly.CaulyAdView;
import com.fsn.cauly.CaulyAdViewListener;

import sjy.policenewproject.common.Check_Preferences;


public class DetailActivity extends Activity implements CaulyAdViewListener {
	String Tag = "";
	private LinearLayout adWrapper = null;
	TextView title;
	private CaulyAdView xmlAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		initCauly();
		Tag = getIntent().getStringExtra("tag");
		adWrapper = (LinearLayout) findViewById(R.id.adWrapper);
		title = (TextView)findViewById(R.id.title_dt);

		title.setText("" + Tag);

		findViewById(R.id.btn1).setOnClickListener(btnListener); 
		findViewById(R.id.btn2).setOnClickListener(btnListener); 
		findViewById(R.id.btn3).setOnClickListener(btnListener);
		findViewById(R.id.btn4).setOnClickListener(btnListener);
		findViewById(R.id.btn5).setOnClickListener(btnListener);

	}
	private void initCauly(){
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
		public void onClick(View v) {
			Intent it = new Intent(DetailActivity.this, ResultActivity.class);
			it.putExtra("tag",Tag);
			switch (v.getId()) {
			case R.id.btn1:
				it.putExtra("type","익히기");
				startActivity(it);
				break;
			case R.id.btn2:
				it.putExtra("type","오엑스");
				startActivity(it);
				break;
			case R.id.btn3:
				it.putExtra("type","오답노트");
				startActivity(it);
				break;
			case R.id.btn4:
				it.putExtra("type","휴지통");
				startActivity(it);
				break;
			case R.id.btn5:
				//초기화
				Check_Preferences.setAppPreferences(DetailActivity.this, "position1", ""+0);
				Check_Preferences.setAppPreferences(DetailActivity.this, "position2", ""+0);
				Check_Preferences.setAppPreferences(DetailActivity.this, "position3", ""+0);
				Check_Preferences.setAppPreferences(DetailActivity.this, "position4", ""+0);
				
				Toast.makeText(getApplicationContext(), "모두 초기화 되었습니다.", 0).show();

				break;
			}
			

		}
	};
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
