package com.kevin.notificationcounter.Adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kevin.notificationcounter.R;

import java.util.Comparator;
import java.util.List;

public class NotificationAppViewAdapter extends ArrayAdapter<NotificationAppView> {

    private Context mContext;
    public NotificationAppViewAdapter(Context context, List<NotificationAppView> objects) {
        super(context, 0, objects);
        mContext= context;
        this.sort(new Comparator<NotificationAppView>() {
            @Override
            public int compare(NotificationAppView notificationAppView, NotificationAppView notificationAppView2) {
                return notificationAppView2.Notifications.compareTo(notificationAppView.Notifications);
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_element, null);
            holder = new ViewHolder();
            holder.appName = (TextView) view.findViewById(R.id.app_name);
            holder.appCount = (TextView) view.findViewById(R.id.app_count);
            holder.progressBar = (ProgressBar) view.findViewById(R.id.app_progress_bar);
            holder.imageView = (ImageView) view.findViewById(R.id.app_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        PackageManager packageManager = mContext.getPackageManager();
        NotificationAppView nv = this.getItem(position);
        String str_appName = null;
        Drawable icon = null;
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(nv.AppName, 0);
            str_appName = packageManager.getApplicationLabel(appInfo).toString();
            icon = packageManager.getApplicationIcon(appInfo);

            String packageName = getPackNameByAppName(nv.AppName);

            Drawable icon1 = mContext.getPackageManager().getApplicationIcon(nv.AppName);
            holder.imageView.setImageDrawable(icon1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            holder.imageView.setImageResource(R.drawable.android);
        }
        if (nv.AppName.equals("com.whatsapp")){
            holder.imageView.setImageResource(R.drawable.whatsapp_logo);
            holder.appName.setText("Whatsapp");
        }
        else if (nv.AppName.equals("com.facebook.orca")) {
            holder.imageView.setImageResource(R.drawable.facebook_logo);
            holder.appName.setText("Facebook");
        }
        else if (nv.AppName.equals("com.facebook.katana")) {
            holder.imageView.setImageResource(R.drawable.facebook_logo);
            holder.appName.setText("Facebook");
        }
        else if (nv.AppName.equals("com.instagram.android")) {
            holder.imageView.setImageResource(R.drawable.instagram);
            holder.appName.setText("Instagram");
        }
        else if (nv.AppName.equals("com.twitter.android")){
            holder.imageView.setImageResource(R.drawable.twitter);
            holder.appName.setText("Twitter");
        }
        else if (nv.AppName.equals("com.snapchat.android")){
            holder.imageView.setImageResource(R.drawable.snapchat);
            holder.appName.setText("Snapchat");
        }
        else if (nv.AppName.equals("org.telegram.messenger")){
            holder.imageView.setImageResource(R.drawable.telegram);
            holder.appName.setText("Telegram");
        }
        else if (nv.AppName.equals("com.linkedin.android")) {
            holder.imageView.setImageResource(R.drawable.linkedin);
            holder.appName.setText("Linkedin");
        }

        else if (str_appName != null) holder.appName.setText(str_appName);
        else holder.appName.setText(nv.AppName);
        holder.appCount.setText(Integer.toString(nv.Notifications));
        holder.progressBar.setProgress((int) ((double) nv.Notifications / (double) nv.MaxNotifications * 100));
        if (icon != null) holder.imageView.setImageDrawable(icon);

        return view;
    }
    public String getPackNameByAppName(String name) {
        PackageManager pm = mContext.getPackageManager();
        List<ApplicationInfo> l = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        String packName = "";
        for (ApplicationInfo ai : l) {
            String n = (String)pm.getApplicationLabel(ai);
            if (n.contains(name) || name.contains(n)){
                packName = ai.packageName;
            }
        }

        return packName;
    }

    private static class ViewHolder {
        TextView appName;
        TextView appCount;
        ProgressBar progressBar;
        ImageView imageView;
    }
}
