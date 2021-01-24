package com.SEproject.guiderapp.service;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.SEproject.guiderapp.R;
import com.SEproject.guiderapp.data.AppUsage;
import com.SEproject.guiderapp.data.DbHandler;
import com.SEproject.guiderapp.ui.DialogActivity;
import com.SEproject.guiderapp.ui.ForegroundServiceActivity;
import com.SEproject.guiderapp.utils.Utils;
import com.rvalerio.fgchecker.AppChecker;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;



/**
 * Foreground service that handles background tasks of counting time,launching stop dialog etc
 */


public class AppTimeDialogService extends Service {

    
	private static final String TIME_KEY = "time";
	private static final String TARGET_PACKAGE_KEY = "target_package";
	private static final String APP_COLOR_KEY = "app_color";
	private static final String TEXT_COLOR_KEY = "text_color";
	private static final int APP_STOPPED_NOTIF_ID = 77;
	private static final String CALLING_CLASS_KEY = "calling_class";
	private static final String ACTION_STOP_SERVICE = "action_stop_service";
	private SharedPreferences preferences;
	private static final int FOREGROUND_NOTIF_ID = 104;
	private static final String DISPLAY_1_MIN = "display_1_min";
	private DbHandler dbHandler;
	private Context context;
	private int appTime;
	private boolean hasUsageAccess;
	private String targetPackage;
	private String mAppName;
	private Bitmap mAppIcon;
	private int mAppColor;
	private int mTextColor;
	int millisUntilFinished;
	CountDownTimer cdt = null;

@Override
public void onCreate() {
	super.onCreate();
}

@Override
public void onDestroy() {
	if(cdt!=null){
		cdt.cancel();
	}
	super.onDestroy();
}

@Override
public int onStartCommand(Intent intent, int flags, int startId) {
    
    if (intent == null) {
            stopSelf();
    } else {
        if (intent.getAction() != null && ACTION_STOP_SERVICE.equals(intent.getAction())) {
            if (cdt!=null){
                    cdt.cancel();
            }
            stopForeground(true);
            stopSelf();

            appTime = intent.getIntExtra(TIME_KEY, 0);
//
//                Timber.i("App Time = %s",appTime);
////
//                Timber.i("AppName = %s",targetPackage);
////
//                Timber.i("AppName = %s",targetPackage);
////
//                context = this;
////
////
//                dbHandler = new DbHandler(context);
////
//                List<AppUsage> lst =  dbHandler.getTop();
////
//                for (AppUsage time : lst){
////
//                    //Timber.i("Total Usage  of %s",time.getDate());
////
//                    Timber.i("App Name of %s",time.getApp_name());
////
//                    Timber.i("Total Usage  of %s",time.getUsage());
////
//                }
//                context = this;
//
//
//                dbHandler = new DbHandler(context);
//
//                List<AppUsage> lst1 =  dbHandler.getWeek();
//
//                for (AppUsage time : lst1){
//
//                    //Timber.i("Total Usage  of %s",time.getDate());
//
//                    Timber.i("App Date of %s",time.getDate());
//
//                    Timber.i("Total Usage  of %s",time.getUsage());
//
//                }
////
//                List<AppUsage> lst2 =  dbHandler.getMonthData();
////
//              /*  for (AppUsage time2 : lst2){
////
//                    Timber.i("Total Usage  of %s",time2.getUsage());
////
//                    Timber.i("Total Usage  of %s",time2.getMonth());
////
//                }*/

            } else {


                initialiseVariables(intent);


                checkIfPermissionGrantedManually();


                fetchAppData();


                runForegroundService();


                setupAndStartCDT();

            }

        }


        return super.onStartCommand(intent, flags, startId);

    }


    private void checkIfPermissionGrantedManually() {

        // Check if permission has been granted manually

        if (!preferences.getBoolean(getString(R.string.usage_permission_pref), false)) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                    && hasUsageStatsPermission(this)) {

                preferences.edit().putBoolean(getString(R.string.usage_permission_pref), true).apply();


            }

        }

        hasUsageAccess = preferences.getBoolean(getString(R.string.usage_permission_pref), false);

    }


    /**
     * Initialises variables to be used
     *
     * @param intent starting intent
     */


    private void initialiseVariables(Intent intent) {
        if (cdt != null) {
            cdt.cancel();
        }


        appTime = intent.getIntExtra(TIME_KEY, 0);

        targetPackage = intent.getStringExtra(TARGET_PACKAGE_KEY);

        context = this;


        dbHandler = new DbHandler(context);


        fetchAppData();


        Timber.i("AppTime = %s",appTime);

        Timber.i("AppName = %s",targetPackage);

        Timber.i("AppName = %s",mAppName);

        String date = "2021.01.10";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

        String currentDateandTime = sdf.format(new Date());

        AppUsage appUsage = new AppUsage(mAppName,currentDateandTime ,appTime);


        dbHandler.addUsage(appUsage);


        mAppColor = intent.getIntExtra(APP_COLOR_KEY, getResources().getColor(R.color.black));

        mTextColor = intent.getIntExtra(TEXT_COLOR_KEY, getResources().getColor(R.color.white));


        preferences = PreferenceManager.getDefaultSharedPreferences(this);


    }

    

/**
     * Starts countdown timer for required time as specified by the class starting this service
     */
    void setupAndStartCDT() {

        long count =0;

        cdt = new CountDownTimer(appTime, 1000) {

            private long millisUntilFinished;



            @Override

            public void onTick(long millisUntilFinished) {

                this.millisUntilFinished = millisUntilFinished;

                Timber.i("Countdown seconds remaining in ATDService: %s", millisUntilFinished / 1000);

            }



            @Override

            public void onFinish() {


                Timber.i("Timer finished.Starting activity");


                showStopDialog();


                stopForeground(true);


                stopSelf();

            }

        };

        cdt.start();



    }


    /**
     * Displays stop dialog on top of the current activity ( has a transparent background due to DialogActivity)
     */
    

private void showStopDialog() {

        Intent dialogIntent = new Intent(AppTimeDialogService.this, DialogActivity.class);

        dialogIntent.putExtra(TARGET_PACKAGE_KEY, targetPackage);

        dialogIntent.putExtra(APP_COLOR_KEY, mAppColor);

        dialogIntent.putExtra(TEXT_COLOR_KEY, mTextColor);

        dialogIntent.putExtra(CALLING_CLASS_KEY, getClass().getSimpleName());

        dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


        // Duration equal to 1 minute

        if (appTime == 60000)

            dialogIntent.putExtra(DISPLAY_1_MIN, false);


        if (hasUsageAccess) {


            // Checks which app is in foreground

            AppChecker appChecker = new AppChecker();

            String packageName = appChecker.getForegroundApp(AppTimeDialogService.this);


            // Creates intent to display

            if (packageName.equals(targetPackage)) {

                Timber.d("App is in use");

                startActivity(dialogIntent);

            } else {

                issueAppStoppedNotification();

                Timber.i("Countdown seconds remaining in Sandaruwan: %s", 0);

            }

        }

        // No usage permission, show dialog without checking foreground app

        else {

            startActivity(dialogIntent);

        }

    }


    /**
     * Checks if usage permission has been granted
     *
     * @param context
     * @return
     */


    @TargetApi(Build.VERSION_CODES.KITKAT)
    boolean hasUsageStatsPermission(Context context) {

        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());

        boolean granted = mode == AppOpsManager.MODE_ALLOWED;

        preferences.edit().putBoolean(getString(R.string.usage_permission_pref), granted).apply();


        return granted;

    }


    /**
     * Fetches data of target app i.e. application name and icon
     */


    private void fetchAppData() {


        ApplicationInfo appInfo;

        PackageManager pm = getPackageManager();


        try {

            Drawable iconDrawable = pm.getApplicationIcon(targetPackage);


            mAppIcon = Utils.getBitmapFromDrawable(iconDrawable);

            appInfo = pm.getApplicationInfo(targetPackage, 0);

        } catch (final PackageManager.NameNotFoundException e) {

            appInfo = null;

            mAppIcon = null;

        }

        mAppName = (String) (appInfo != null ? pm.getApplicationLabel(appInfo) : "(unknown)");


    }


    /**
     * App is no longer running. Display notification instead of dialog
     */


    private void issueAppStoppedNotification() {
 
       NotificationManager notificationManager =
 (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);

 
       String channelId = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String CHANNEL_ID = "Guider_app_stopped";
		// The id of the channel.
            
		String channelName = getString(R.string.notif_app_stopped_channel_name);
		// The user-visible name of the channel.

            	int importance = NotificationManager.IMPORTANCE_LOW;

            channelId = createNotificationChannel(CHANNEL_ID, channelName, importance);

        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppTimeDialogService.this, channelId);


        String title = mAppName + " " + getString(R.string.app_closed_notification_title);

        builder.setContentTitle(title)
                .setSmallIcon(R.drawable.app_notification_icon)
.setContentIntent(PendingIntent.getActivity(AppTimeDialogService.this,
                        0, new Intent(), 0))
                .setLargeIcon(mAppIcon)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setSubText(getString(R.string.app_closed_notification_subtitle))
                .setAutoCancel(true);

        Notification notification = builder.build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        if (notificationManager != null) {

            notificationManager.notify(APP_STOPPED_NOTIF_ID, notification);

        }

    }



    @Nullable

    @Override

    public IBinder onBind(Intent intent) {

        return null;


    }


    private void runForegroundService() {


        String channelId = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            channelId = createNotificationChannel("timesapp_fg_service", "Background Service Notification", NotificationManager.IMPORTANCE_LOW);

        }

        Intent notificationIntent = new Intent(this, ForegroundServiceActivity.class);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);


        Intent appLaunchIntent = getPackageManager().getLaunchIntentForPackage(targetPackage);


        PendingIntent actionPendingIntent = PendingIntent.getActivity(this, 1, appLaunchIntent, 0);

        NotificationCompat.Action.Builder actionBuilder =
 new NotificationCompat.Action.Builder(R.drawable.ic_exit_to_app_black_24dp,
 "Return to " + mAppName, actionPendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            builder.setCategory(Notification.CATEGORY_SERVICE);

        }

        if (preferences.getBoolean(getString(R.string.pref_notification_done_key), true)) {

            Intent stopSelfIntent = new Intent(this, AppTimeDialogService.class);

            stopSelfIntent.setAction(ACTION_STOP_SERVICE);

            PendingIntent stopSelfPIntent = PendingIntent.getService(this, 0, stopSelfIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Action.Builder stopActionBuilder =
                    new NotificationCompat.Action.Builder(R.drawable.ic_clear_black_24dp,
 "Done", stopSelfPIntent);

            builder.addAction(stopActionBuilder.build());

        }

        Notification notification = builder.setOngoing(true)
.setContentText(getString(R.string.app_running_service_notif_text))
.setSubText(getString(R.string.tap_for_more_info_foreground_notif))
.setColor(getResources().getColor(R.color.colorPrimary))
.addAction(actionBuilder.build())
.setPriority(Notification.PRIORITY_MIN)
.setSmallIcon(R.drawable.app_notification_icon)
.setContentIntent(pendingIntent).build();


        startForeground(FOREGROUND_NOTIF_ID, notification);

    }



    @RequiresApi(Build.VERSION_CODES.O)
    
    private String createNotificationChannel(String channelId, String channelName, int importance) {


        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, importance);

        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);


        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (manager != null) {

            manager.createNotificationChannel(chan);

        }

        return channelId;
    }

}
