package com.kevin.notificationcounter.Adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.palette.graphics.Palette;

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
            holder.shadowview = (View) view.findViewById(R.id.shadowcolor);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        PackageManager packageManager = mContext.getPackageManager();
        NotificationAppView nv = this.getItem(position);
        String str_appName = null;
        Drawable icon = null;
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(nv.AppName, PackageManager.GET_META_DATA);
            str_appName = packageManager.getApplicationLabel(appInfo).toString();
            icon = packageManager.getApplicationIcon(appInfo);
            holder.imageView.setImageDrawable(icon);

            Bitmap bitmap = drawableToBitmap(icon);
            int color = getDominantColor(bitmap);
            holder.shadowview.setBackgroundColor(color);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            holder.imageView.setImageResource(R.drawable.android);
        }
        if (str_appName != null) holder.appName.setText(str_appName);
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
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    private static class ViewHolder {
        TextView appName;
        TextView appCount;
        ProgressBar progressBar;
        ImageView imageView;
        View shadowview;
    }
}
