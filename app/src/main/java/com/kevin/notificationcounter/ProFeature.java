package com.kevin.notificationcounter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;

public class ProFeature extends AppCompatActivity {

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    boolean premium = false;

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
    }

    public void twelveMonthSub(View view) {
    }

    public void lifeTimeSub(View view) {
    }
}