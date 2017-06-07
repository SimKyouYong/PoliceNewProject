package sjy.policenewproject.boot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;

public class ScreenService extends Service {
	
	// Screen Broadcast Receiver Setting
    private ScreenReceiver sr = new ScreenReceiver();
    
    public void onCreate()
	{
		super.onCreate();
		IntentFilter filter = new IntentFilter();
    	filter.addAction(Intent.ACTION_SCREEN_ON);
    	registerReceiver(sr, filter);
    	unregisterRestartAlarm();
	}
	
	public void onDestroy()
	{
//		registerRestartAlarm();			//이거 불러오면 어떤짓을 해도 자동으로 켜짐
		unregisterReceiver(sr);
		super.onDestroy();
		
	}
	public int onStartCommand (Intent intent, int flags, int startId)
	{
		super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void registerRestartAlarm()
	{
		Intent intent = new Intent(ScreenService.this,RestartService.class);
		intent.setAction("RestartService.ACTION_RESTART_SCREENSERVICE");
		PendingIntent sender = PendingIntent.getBroadcast(ScreenService.this, 0, intent, 0);
		long firstTime = SystemClock.elapsedRealtime(); 
		firstTime += 1 * 1000; //10���� �˶� �̺�Ʈ �߻�
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 10 * 1000, sender);
	}
	
	public void unregisterRestartAlarm()
	{
		Intent intent = new Intent(ScreenService.this,RestartService.class);
		intent.setAction("RestartService.ACTION_RESTART_SCREENSERVICE");
		PendingIntent sender = PendingIntent.getBroadcast(ScreenService.this, 0, intent, 0);
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.cancel(sender);
	}

}
