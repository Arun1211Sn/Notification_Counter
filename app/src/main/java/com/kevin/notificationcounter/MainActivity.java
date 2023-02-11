package com.kevin.notificationcounter;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String STATE_ITEM_SELECT = "selectedItem";
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // If the user did not turn the notification listener service on we prompt him to do so
        if(!isNotificationServiceEnabled()){
            AlertDialog enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setDateView();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new TodayFragment());
        fragmentTransaction.commit();

    }

    private void setDateView() {
        TextView dateSun = findViewById(R.id.date_sun);
        TextView dateMon = findViewById(R.id.date_mon);
        TextView dateTue = findViewById(R.id.date_tue);
        TextView dateWed = findViewById(R.id.date_wed);
        TextView dateThu = findViewById(R.id.date_thu);
        TextView datefri = findViewById(R.id.date_fri);
        TextView datesat = findViewById(R.id.date_sat);
        LinearLayout layoutsun = findViewById(R.id.layout_sunday);
        LinearLayout layoutmon = findViewById(R.id.layout_monday);
        LinearLayout layouttue = findViewById(R.id.layout_tuesday);
        LinearLayout layoutwed = findViewById(R.id.layout_wednesday);
        LinearLayout layoutthu = findViewById(R.id.layout_thursday);
        LinearLayout layoutfri = findViewById(R.id.layout_friday);
        LinearLayout layoutsat = findViewById(R.id.layout_saturday);

        DateFormat format = new SimpleDateFormat("dd");
        //DateFormat format2 = new SimpleDateFormat("dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        Calendar calendar1 = Calendar.getInstance();
        int day = calendar1.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                layoutsun.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.datebg));
                layoutmon.setBackgroundResource(0);
                layouttue.setBackgroundResource(0);
                layoutwed.setBackgroundResource(0);
                layoutthu.setBackgroundResource(0);
                layoutfri.setBackgroundResource(0);
                layoutsat.setBackgroundResource(0);
                break;
            case Calendar.MONDAY:
                // Current day is Monday
                layoutsun.setBackgroundResource(0);
                layoutmon.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.datebg));
                layouttue.setBackgroundResource(0);
                layoutwed.setBackgroundResource(0);
                layoutthu.setBackgroundResource(0);
                layoutfri.setBackgroundResource(0);
                layoutsat.setBackgroundResource(0);
                break;
            case Calendar.TUESDAY:
                // etc.
                layoutsun.setBackgroundResource(0);
                layoutmon.setBackgroundResource(0);
                layouttue.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.datebg));
                layoutwed.setBackgroundResource(0);
                layoutthu.setBackgroundResource(0);
                layoutfri.setBackgroundResource(0);
                layoutsat.setBackgroundResource(0);
                break;
            case Calendar.WEDNESDAY:
                layoutsun.setBackgroundResource(0);
                layoutmon.setBackgroundResource(0);
                layouttue.setBackgroundResource(0);
                layoutwed.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.datebg));
                layoutthu.setBackgroundResource(0);
                layoutfri.setBackgroundResource(0);
                layoutsat.setBackgroundResource(0);
                // etc.
                break;
            case Calendar.THURSDAY:
                layoutsun.setBackgroundResource(0);
                layoutmon.setBackgroundResource(0);
                layouttue.setBackgroundResource(0);
                layoutwed.setBackgroundResource(0);
                layoutthu.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.datebg));
                layoutfri.setBackgroundResource(0);
                layoutsat.setBackgroundResource(0);
                // etc.
                break;
            case Calendar.FRIDAY:
                layoutsun.setBackgroundResource(0);
                layoutmon.setBackgroundResource(0);
                layouttue.setBackgroundResource(0);
                layoutwed.setBackgroundResource(0);
                layoutthu.setBackgroundResource(0);
                layoutfri.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.datebg));
                layoutsat.setBackgroundResource(0);
                // etc.
                break;
            case Calendar.SATURDAY:
                layoutsun.setBackgroundResource(0);
                layoutmon.setBackgroundResource(0);
                layouttue.setBackgroundResource(0);
                layoutwed.setBackgroundResource(0);
                layoutthu.setBackgroundResource(0);
                layoutfri.setBackgroundResource(0);
                layoutsat.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.datebg));
                // etc.
                break;
        }


        String[] days = new String[7];
        for (int i = 0; i < 7; i++)
        {
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Log.i(TAG, "onCreate: "+days[i]);
        }
        dateSun.setText(days[0]);
        dateMon.setText(days[1]);
        dateTue.setText(days[2]);
        dateWed.setText(days[3]);
        dateThu.setText(days[4]);
        datefri.setText(days[5]);
        datesat.setText(days[6]);
    }
    //-------- To Check and Get Notifications Reading Permission ------------

    /**
     * Is Notification Service Enabled.
     * Verifies if the notification listener service is enabled.
     * Got it from: https://github.com/kpbird/NotificationListenerService-Example/blob/master/NLSExample/src/main/java/com/kpbird/nlsexample/NLService.java
     * @return True if enabled, false otherwise.
     */
    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * Build Notification Listener Alert Dialog.
     * Builds the alert dialog that pops up if the user has not turned
     * the Notification Listener Service on yet.
     * @return An alert dialog which leads to the notification enabling screen
     */
    private AlertDialog buildNotificationServiceAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }
                });
        return(alertDialogBuilder.create());
    }

    public void goTopaymentActivity(View view) {
        startActivity(new Intent(MainActivity.this,ProFeature.class));
    }
    public static void getStats(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        int interval = UsageStatsManager.INTERVAL_YEARLY;
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context.getApplicationContext());

        Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        UsageEvents uEvents = usm.queryEvents(startTime,endTime);
        while (uEvents.hasNextEvent()){
            UsageEvents.Event e = new UsageEvents.Event();
            uEvents.getNextEvent(e);

            if (e != null){
                Log.d(TAG, "Event: " + e.getPackageName() + "\t" +  e.getTimeStamp());
            }
        }
    }
}