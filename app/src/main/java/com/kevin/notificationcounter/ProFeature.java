package com.kevin.notificationcounter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;

public class ProFeature extends AppCompatActivity {

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    boolean premium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_feature);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        pref = getSharedPreferences("ProVersion", MODE_PRIVATE);
        editor = pref.edit();
        editor.putBoolean("premium", false);
        editor.commit();
        premium = pref.getBoolean("premium", false);

    }

    public void one_month_sub(View view) {

        if(premium == true){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Already Subscribed . Press Yes to Cancel Subscription");
            builder1.setTitle("Cancel Subscription");
            builder1.setIcon(R.drawable.ic_baseline_warning_24);
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            pref = getSharedPreferences("ProVersion", MODE_PRIVATE);
                            editor = pref.edit();
                            editor.putBoolean("premium", false);
                            editor.commit();
                            premium = pref.getBoolean("premium", false);
                            Toast.makeText(ProFeature.this, "Subscription Cancelled", Toast.LENGTH_SHORT).show();

                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Press Yes to Buy one month plan");
            builder1.setTitle("Buy Subscription");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            pref = getSharedPreferences("ProVersion", MODE_PRIVATE);
                            editor = pref.edit();
                            editor.putBoolean("premium", true);
                            editor.commit();
                            premium = pref.getBoolean("premium", true);
                            Toast.makeText(ProFeature.this, "Subscription Bought", Toast.LENGTH_SHORT).show();

                        }
                    });

            builder1.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

    }

    public void twelveMonthSub(View view) {
        if(premium == true){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Already Subscribed . Press Yes to Cancel Subscription");
            builder1.setTitle("Cancel Subscription");
            builder1.setIcon(R.drawable.ic_baseline_warning_24);
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            pref = getSharedPreferences("ProVersion", MODE_PRIVATE);
                            editor = pref.edit();
                            editor.putBoolean("premium", false);
                            editor.commit();
                            premium = pref.getBoolean("premium", false);
                            Toast.makeText(ProFeature.this, "Subscription Cancelled", Toast.LENGTH_SHORT).show();

                        }
                    });

            builder1.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Press Yes to Buy 12 month plan");
            builder1.setTitle("Buy Subscription");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            pref = getSharedPreferences("ProVersion", MODE_PRIVATE);
                            editor = pref.edit();
                            editor.putBoolean("premium", true);
                            editor.commit();
                            premium = pref.getBoolean("premium", true);
                            Toast.makeText(ProFeature.this, "Subscription Bought", Toast.LENGTH_SHORT).show();

                        }
                    });

            builder1.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    public void lifeTimeSub(View view) {
        if(premium == true){
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Already Subscribed . Press Yes to Cancel Subscription");
            builder1.setTitle("Cancel Subscription");
            builder1.setIcon(R.drawable.ic_baseline_warning_24);
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            pref = getSharedPreferences("ProVersion", MODE_PRIVATE);
                            editor = pref.edit();
                            editor.putBoolean("premium", false);
                            editor.commit();
                            premium = pref.getBoolean("premium", false);
                            Toast.makeText(ProFeature.this, "Subscription Cancelled", Toast.LENGTH_SHORT).show();

                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Press Yes to Buy Lifetime plan");
            builder1.setTitle("Buy Subscription");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            pref = getSharedPreferences("ProVersion", MODE_PRIVATE);
                            editor = pref.edit();
                            editor.putBoolean("premium", true);
                            editor.commit();
                            premium = pref.getBoolean("premium", true);
                            Toast.makeText(ProFeature.this, "Subscription Bought", Toast.LENGTH_SHORT).show();

                        }
                    });

            builder1.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }
}