package in.pyv;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {
    
	Menu menu;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	this.menu = menu;
    	
        getMenuInflater().inflate(R.menu.main, menu);
        
        if(isMyServiceRunning(UpdateService.class))
        {
        	MenuItem item = (MenuItem) menu.findItem(R.id.start_action);
        	item.setVisible(false);
        }
        else
        {
        	MenuItem item = (MenuItem) menu.findItem(R.id.cancel_action);
        	item.setVisible(false);
        }
        return true;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    
    // Menu options to set and cancel the alarm.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	MenuItem mi;
        switch (item.getItemId()) {
            case R.id.start_action:
            	Log.d("MainActivity","Starting UpdateService" );
            	startService(new Intent(MainActivity.this, UpdateService.class));
            	item.setVisible(false);
            	mi = (MenuItem) menu.findItem(R.id.cancel_action);
            	mi.setVisible(true);
            	Toast.makeText(this, "Protect Your Vision alarm activated", Toast.LENGTH_LONG).show();
                return true;
            case R.id.cancel_action:
            	Log.d("MainActivity","Stopping UpdateService" );
            	stopService(new Intent(MainActivity.this, UpdateService.class));
            	item.setVisible(false);
            	mi = (MenuItem) menu.findItem(R.id.start_action);
            	mi.setVisible(true);
            	Toast.makeText(this, "Protect Your Vision Alarm cancelled", Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }
}