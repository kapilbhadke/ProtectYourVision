package in.pyv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {
	 
    private boolean screenOff;
 
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
        	Log.d("via Receiver","Normal ScreenOFF" );
            screenOff = true;
        } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
        	Log.d("via Receiver","Normal ScreenON" );
            screenOff = false;
        }
        Intent i = new Intent(context, UpdateService.class);
        i.putExtra("screen_state", screenOff);
        context.startService(i);
    }
 
}