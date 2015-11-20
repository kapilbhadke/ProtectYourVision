package in.pyv;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class SchedulingService extends IntentService {

    public static final String TAG = "PYV";
    // An ID used to post the notification.
    public static final int NOTIFICATION_ID = 1;
    public static final String NOTIFICATION_ID_EXTRA = "NOTIFICATION_ID";

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

        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, NotificationView.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent = new Intent(this, NotificationView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID_EXTRA, NOTIFICATION_ID);
        PendingIntent dismissIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setPriority(NotificationCompat.PRIORITY_MAX) //HIGH, MAX, FULL_SCREEN and setDefaults(Notification.DEFAULT_ALL) will make it a Heads Up Display Style
                .setDefaults(Notification.DEFAULT_ALL) // also requires VIBRATE permission
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(getString(R.string.doodle_alert))
                .setContentTitle(getString(R.string.doodle_alert))
                .setContentText(msg)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .addAction(R.drawable.ic_action_cancel, "Dismiss Now", dismissIntent)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentText(msg);

        mBuilder.setSound(alarmSound);
        mBuilder.setVibrate(new long[]{1000, 1000});

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        Log.d(TAG, "Sending notification");
    }
}
