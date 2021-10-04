package com.kevin.notificationcounter;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.kevin.notificationcounter.Adapters.NotificationAppView;
import com.kevin.notificationcounter.Adapters.NotificationAppViewAdapter;
import com.kevin.notificationcounter.customizers.MyMarkerView;
import com.kevin.notificationcounter.models.DatabaseHelper;
import com.kevin.notificationcounter.models.NotificationItemDao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class TodayFragment extends Fragment {

    private DatabaseHelper databaseHelper = null;
    private static final String SET_LABEL = "";
    private BarChart chart;
    int[] WeekNotCount = {0, 0, 0, 0, 0, 0, 0};
    int[] WeekNotCountnotPremium = {0, 0, 0, 0, 0, 0, 0};
    private static final String[] DaysForChart = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    boolean premium = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ActionBar actionBar = getActivity().getActionBar();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View main = inflater.inflate(R.layout.fragment_today, container, false);
//        View viewHeader = inflater.inflate(R.layout.list_header_day_count, null);

        ListView listView = (ListView) main.findViewById(R.id.list_view);
        chart = (BarChart) main.findViewById(R.id.chart1);
        SharedPreferences prefs = this.getActivity().getSharedPreferences("ProVersion", MODE_PRIVATE);
        premium = prefs.getBoolean("premium", false);

//        listView.addHeaderView(viewHeader, null, false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), AppDetail.class);
                NotificationAppView clickedApp = (NotificationAppView) adapterView.getAdapter().getItem(i);
                intent.putExtra(AppDetail.EXTRA_PACKAGENAME, clickedApp.AppName);
                intent.putExtra(AppDetail.EXTRA_INTERVALTYPE, AppDetail.FLAG_VIEW_DAILY);
                intent.putExtra(AppDetail.EXTRA_DATESTRING, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                startActivity(intent);
            }
        });

        return main;
    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<NotificationAppView> list = new LinkedList<NotificationAppView>();
        try {
            NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
            list = dao.getOverviewToday();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int totalCount = 0;
        for (NotificationAppView aList : list) {
            totalCount += aList.Notifications;
        }

        TextView titleCounter = (TextView) getActivity().findViewById(R.id.title_counter);
        titleCounter.setText(Integer.toString(totalCount));
        TextView titleCounterSuffix = (TextView) getActivity().findViewById(R.id.title_counter_suffix);
        if (totalCount == 1) {
            titleCounterSuffix.setText(R.string.title_counter_suffix_single);
        } else {
            titleCounterSuffix.setText(R.string.title_counter_suffix_plural);
        }

        Calendar calendar1 = Calendar.getInstance();
        int day = calendar1.get(Calendar.DAY_OF_WEEK);

        //Check if premium or not
        if (premium == false){
            switch (day) {
                case Calendar.SUNDAY:
                    for (int i = 0; i < WeekNotCountnotPremium.length; i++) {
                        WeekNotCountnotPremium[i] = 0;
                    }
                    WeekNotCountnotPremium[Calendar.SUNDAY-1] = totalCount;
                    break;
                case Calendar.MONDAY:
                    // Current day is Monday
                    WeekNotCountnotPremium[Calendar.MONDAY-1] = totalCount;
                    break;
                case Calendar.TUESDAY:
                    // etc.
                    WeekNotCountnotPremium[Calendar.TUESDAY-1] = totalCount;
                    break;
                case Calendar.WEDNESDAY:
                    WeekNotCountnotPremium[Calendar.WEDNESDAY-1] = totalCount;
                    // etc.
                    break;
                case Calendar.THURSDAY:
                    WeekNotCountnotPremium[Calendar.SUNDAY-1] = 0;
                    WeekNotCountnotPremium[Calendar.THURSDAY-1] = totalCount;
                    // etc.
                    break;
                case Calendar.FRIDAY:
                    WeekNotCountnotPremium[Calendar.MONDAY-1] = 0;
                    WeekNotCountnotPremium[Calendar.FRIDAY-1] = totalCount;
                    // etc.
                    break;
                case Calendar.SATURDAY:
                    WeekNotCountnotPremium[Calendar.TUESDAY-1] = 0;
                    WeekNotCountnotPremium[Calendar.SATURDAY-1] = totalCount;
                    // etc.
                    break;
            }

            for (int i = 0; i < WeekNotCountnotPremium.length; ++i) {
                Log.i(TAG, "WeekNotCountNotPremium: " + i + " count" + WeekNotCountnotPremium[i]);
            }

            ArrayList<BarEntry> values = new ArrayList<>();
            for (int i = 0; i < WeekNotCountnotPremium.length; i++) {
                float x = i;
                float y = WeekNotCountnotPremium[i];
                values.add(new BarEntry(x, y));
            }
            BarDataSet set1 = new BarDataSet(values, SET_LABEL);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            set1.setDrawValues(false);

            BarData data = new BarData(dataSets);
            data.setBarWidth(0.7f);
            chart.getDescription().setEnabled(false);
            chart.setDrawValueAboveBar(false);
            chart.setDrawGridBackground(false);
            chart.setDrawBarShadow(false);
            chart.setPinchZoom(false);
            chart.getLegend().setEnabled(false);
            chart.setFitBars(true);
            chart.setElevation(1f);
            chart.setScaleEnabled(false);
            chart.setVisibleXRangeMinimum(values.size());
            IMarker marker = new MyMarkerView(chart.getContext(), R.layout.custom_marker_view);
            chart.setMarker(marker);


            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisMinimum(0f);
            xAxis.setSpaceMin(data.getBarWidth() / 2f);
            xAxis.setSpaceMax(data.getBarWidth() / 2f);
            xAxis.setAxisMinimum(-0.9f);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float v, AxisBase axisBase) {
                    return DaysForChart[(int) v];
                }
            });

            YAxis axisLeft = chart.getAxisLeft();
            axisLeft.setEnabled(true);
            axisLeft.setDrawGridLines(true);

            YAxis axisRight = chart.getAxisRight();
            axisRight.setEnabled(false);
            axisRight.setAxisMinimum(0f);
            axisLeft.setAxisMinimum(0f);

            data.setValueTextSize(12f);
            chart.setData(data);
            //chart.invalidate();
        }
        else {
            switch (day) {
                case Calendar.SUNDAY:
                    for (int i = 0; i < WeekNotCount.length; i++) {
                        WeekNotCount[i] = 0;
                    }
                    WeekNotCount[Calendar.SUNDAY-1] = totalCount;
                    break;
                case Calendar.MONDAY:
                    // Current day is Monday
                    WeekNotCount[Calendar.MONDAY-1] = totalCount;
                    break;
                case Calendar.TUESDAY:
                    // etc.
                    WeekNotCount[Calendar.TUESDAY-1] = totalCount;
                    break;
                case Calendar.WEDNESDAY:
                    WeekNotCount[Calendar.WEDNESDAY-1] = totalCount;
                    // etc.
                    break;
                case Calendar.THURSDAY:
                    WeekNotCount[Calendar.THURSDAY-1] = totalCount;
                    // etc.
                    break;
                case Calendar.FRIDAY:
                    WeekNotCount[Calendar.FRIDAY-1] = totalCount;
                    // etc.
                    break;
                case Calendar.SATURDAY:
                    WeekNotCount[Calendar.SATURDAY-1] = totalCount;
                    // etc.
                    break;
            }
            for (int i = 0; i < WeekNotCount.length; ++i) {
                Log.i(TAG, "WeekNotCount: " + i + " count" + WeekNotCount[i]);
            }

            ArrayList<BarEntry> values = new ArrayList<>();
            for (int i = 0; i < WeekNotCount.length; i++) {
                float x = i;
                float y = WeekNotCount[i];
                values.add(new BarEntry(x, y));
            }
            BarDataSet set1 = new BarDataSet(values, SET_LABEL);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            set1.setDrawValues(false);

            BarData data = new BarData(dataSets);
            data.setBarWidth(0.7f);
            chart.getDescription().setEnabled(false);
            chart.setDrawValueAboveBar(false);
            chart.setDrawGridBackground(false);
            chart.setDrawBarShadow(false);
            chart.setPinchZoom(false);
            chart.getLegend().setEnabled(false);
            chart.setFitBars(true);
            chart.setElevation(1f);
            chart.setScaleEnabled(false);
            chart.setVisibleXRangeMinimum(values.size());
            IMarker marker = new MyMarkerView(chart.getContext(), R.layout.custom_marker_view);
            chart.setMarker(marker);


            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setAxisMinimum(0f);
            xAxis.setSpaceMin(data.getBarWidth() / 2f);
            xAxis.setSpaceMax(data.getBarWidth() / 2f);
            xAxis.setAxisMinimum(-0.9f);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float v, AxisBase axisBase) {
                    return DaysForChart[(int) v];
                }
            });

            YAxis axisLeft = chart.getAxisLeft();
            axisLeft.setEnabled(true);
            axisLeft.setDrawGridLines(true);

            YAxis axisRight = chart.getAxisRight();
            axisRight.setEnabled(false);
            axisRight.setAxisMinimum(0f);
            axisLeft.setAxisMinimum(0f);

            data.setValueTextSize(12f);
            chart.setData(data);
            //chart.invalidate();
        }

        NotificationAppViewAdapter adapter = new NotificationAppViewAdapter(getActivity(), list);

        ListView listView = (ListView) getActivity().findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}