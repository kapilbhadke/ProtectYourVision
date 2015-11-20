package in.pyv;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;

public class NotificationView extends Activity {

	public static final String NOTIFICATION_ID_EXTRA = "NOTIFICATION_ID";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Create Notification Manager
		NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// Dismiss Notification
		notificationmanager.cancel(getIntent().getIntExtra(NOTIFICATION_ID_EXTRA, -1));
		
		finish();
	}
}