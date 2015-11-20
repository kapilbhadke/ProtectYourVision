package in.pyv;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

public class SchedulingService extends IntentService {

	public static final String TAG = "PYV";
	// An ID used to post the notification.
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public SchedulingService() {
		super("SchedulingService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		sendNotification(getString(R.string.doodle_found));

		// Release the wake lock provided by the BroadcastReceiver.
		AlarmReceiver.completeWakefulIntent(intent);
	}

	// Post a notification indicating to look at some object 20 feet away for 20 seconds
	private void sendNotification(String msg) {
		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, NotificationView.class), 0);

		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle(getString(R.string.doodle_alert))
		.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
		.setContentText(msg);
		
		mBuilder.setContentIntent(contentIntent);
		mBuilder.setSound(alarmSound);
		//mBuilder.setVibrate(new long[] { 1000, 1000});
		
		RemoteViews remoteViews = new RemoteViews(getPackageName(),	R.layout.customnotification);
		remoteViews.setImageViewResource(R.id.imagenotileft,R.drawable.ic_launcher);
		remoteViews.setImageViewResource(R.id.imagenotiright,R.drawable.ic_action_accept);
		remoteViews.setTextViewText(R.id.title, getString(R.string.doodle_alert));
		remoteViews.setTextViewText(R.id.text, msg);
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setTicker(getString(R.string.doodle_alert))
		.setAutoCancel(true)
		.setContentIntent(contentIntent)
		.setSound(alarmSound)
		.setContent(remoteViews);
		
		mNotificationManager.notify(NOTIFICATION_ID, builder.build());

		Log.d(TAG, "Sending notification");
	}
}
