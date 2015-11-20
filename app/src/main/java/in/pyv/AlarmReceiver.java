package in.pyv;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent 
 * and then starts the IntentService {@code SchedulingService} to do some work.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

	public static final String TAG = "PYV";

	// The app's AlarmManager, which provides access to the system alarm services.
	private AlarmManager alarmMgr;
	// The pending intent that is triggered when the alarm fires.
	private PendingIntent alarmIntent;

	private static final long REPEAT_TIME = 20 * 60 * 1000;	// 20 minutes

	@Override
	public void onReceive(Context context, Intent intent) {   

		Intent service = new Intent(context, SchedulingService.class);
		// Start the service, keeping the device awake while it is launching.
		startWakefulService(context, service);
	}

	/**
	 * Sets a repeating alarm that runs continuously. When the
	 * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
	 * @param context
	 */
	public void setAlarm(Context context) {
		
		Log.d("PYV", "Setting Alarm !!!");
		
		alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);
		alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		long interval = REPEAT_TIME;
        long triggerTime = SystemClock.elapsedRealtime() + interval;
        
		alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, interval, alarmIntent);

		// Enable {@code SampleBootReceiver} to automatically restart the alarm when the
		// device is rebooted.
		ComponentName receiver = new ComponentName(context, BootReceiver.class);
		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);           
	}

	/**
	 * Cancels the alarm.
	 * @param context
	 */
	public void cancelAlarm(Context context) {
		// If the alarm has been set, cancel it.

		alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmReceiver.class);
		alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

		if (alarmMgr!= null) {

			Log.d("PYV", "Cancelling Alarm !!!");

			alarmMgr.cancel(alarmIntent);
		}

		// Disable {@code SampleBootReceiver} so that it doesn't automatically restart the 
		// alarm when the device is rebooted.
		ComponentName receiver = new ComponentName(context, BootReceiver.class);
		PackageManager pm = context.getPackageManager();

		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				PackageManager.DONT_KILL_APP);
	}
}
