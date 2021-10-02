package com.kevin.notificationcounter.Service;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.kevin.notificationcounter.models.Application;
import com.kevin.notificationcounter.models.ApplicationDao;
import com.kevin.notificationcounter.models.DatabaseHelper;
import com.kevin.notificationcounter.models.NotificationItem;

import java.sql.SQLException;
import java.util.Date;

public class NotificationListener extends NotificationListenerService {
    public static boolean isNotificationAccessEnabled = false;

    private DatabaseHelper databaseHelper = null;

    public NotificationListener() {
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (!sbn.isOngoing()) {
            storeNotification(sbn);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (sbn.isOngoing()) {
            storeNotification(sbn);
        }
    }

    private void storeNotification(StatusBarNotification sbn) {
        if ((sbn.getNotification().flags & Notification.FLAG_GROUP_SUMMARY) != 0) {
            //Ignore duplicate notifications which we might receive in apps like whatsapp, gmail, etc...
        } else {
            try {
                String packageName = sbn.getPackageName();
                ApplicationDao applicationDao = getDatabaseHelper().getApplicationDao();
                if (!applicationDao.idExists(packageName)) {
                    Application application = new Application(packageName, false);
                    applicationDao.create(application);
                }

                String appName = packageName;
                PackageManager packageManager = getApplicationContext().getPackageManager();
                try {
                    //ApplicationInfo info = getPackageManager().getApplicationInfo(packageName, 0);
                    //appName = getPackageManager().getApplicationLabel(info).toString();
                    appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                String message = null;
                if (sbn.getNotification().extras != null) {
                    message = sbn.getNotification().extras.get(
                            Notification.EXTRA_TITLE).toString();
                    if (message == null || "".equals(message)) {
                        message = sbn.getNotification().extras.get(
                                Notification.EXTRA_TEXT).toString();
                    } else if (message.equals(appName)) {
                        String otherMsg = sbn.getNotification().extras.get(
                                Notification.EXTRA_TEXT).toString();
                        if (otherMsg != null && !"".equals(otherMsg)) {
                            message = otherMsg;
                        }
                    }
                }

                if (message == null || "".equals(message)) {
                    message = sbn.getNotification().tickerText.toString();
                } else if (message.equals(appName)) {
                    String otherMsg = sbn.getNotification().tickerText.toString();
                    if (otherMsg != null && !"".equals(otherMsg)) {
                        message = otherMsg;
                    }
                }

                Dao<NotificationItem, Integer> dao = getDatabaseHelper().getNotificationDao();
                NotificationItem newItem = new NotificationItem(packageName, new Date(sbn.getPostTime()), message);
                dao.create(newItem);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        IBinder binder = super.onBind(intent);
        isNotificationAccessEnabled = true;
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        boolean onUnbind = super.onUnbind(intent);
        isNotificationAccessEnabled = false;
        return onUnbind;
    }
}
