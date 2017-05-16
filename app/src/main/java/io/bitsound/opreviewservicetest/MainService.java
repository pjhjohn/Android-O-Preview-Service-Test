package io.bitsound.opreviewservicetest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Locale;


public class MainService extends Service {

    private static final String TAG = MainService.class.getSimpleName();
    public static final long MAIN_SERVICE_LOOP_INTERVAL = 10 * 1000; // 10s

    /* Lifecycle */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "[onCreate]");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, String.format(Locale.getDefault(), "[onStartCommand] (intent:%s), (flags:%d), (startId:%d)", intent, flags, startId));

        this.reschedule(this);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "[onDestroy]");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, String.format(Locale.getDefault(),"[onBind] (intent:%s)", intent));
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, String.format(Locale.getDefault(),"[onUnbind] (intent:%s)", intent));
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, String.format(Locale.getDefault(),"[onRebind] (intent:%s)", intent));
        super.onRebind(intent);
    }

    private static final int MAIN_SERVICE_REQUEST_CODE = 0x100;
    private void reschedule(Context context) {
        Log.d(TAG, "reschedule");
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, MainService.class);
        long now = System.currentTimeMillis();
        final PendingIntent pendingIntent = PendingIntent.getService(context, MAIN_SERVICE_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, now + MAIN_SERVICE_LOOP_INTERVAL, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, now + MAIN_SERVICE_LOOP_INTERVAL, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, now + MAIN_SERVICE_LOOP_INTERVAL, pendingIntent);
        }
    }
}
