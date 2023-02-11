package com.kevin.notificationcounter;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import java.text.DateFormat;
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
    boolean premium;

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
        setDateView(main,  listView);

        return main;
    }
    private void setDateView(View view, ListView listView) {
        TextView dateSun = view.findViewById(R.id.date_sun);
        TextView dateMon = view.findViewById(R.id.date_mon);
        TextView dateTue = view.findViewById(R.id.date_tue);
        TextView dateWed = view.findViewById(R.id.date_wed);
        TextView dateThu = view.findViewById(R.id.date_thu);
        TextView datefri = view.findViewById(R.id.date_fri);
        TextView datesat = view.findViewById(R.id.date_sat);
        LinearLayout layoutsun = view.findViewById(R.id.layout_sunday);
        LinearLayout layoutmon = view.findViewById(R.id.layout_monday);
        LinearLayout layouttue = view.findViewById(R.id.layout_tuesday);
        LinearLayout layoutwed = view.findViewById(R.id.layout_wednesday);
        LinearLayout layoutthu = view.findViewById(R.id.layout_thursday);
        LinearLayout layoutfri = view.findViewById(R.id.layout_friday);
        LinearLayout layoutsat = view.findViewById(R.id.layout_saturday);

        DateFormat format = new SimpleDateFormat("dd");
        //DateFormat format2 = new SimpleDateFormat("dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        Calendar calendar1 = Calendar.getInstance();
        int day = calendar1.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                layoutsun.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.datebg));
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
                layoutmon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.datebg));
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
                layouttue.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.datebg));
                layoutwed.setBackgroundResource(0);
                layoutthu.setBackgroundResource(0);
                layoutfri.setBackgroundResource(0);
                layoutsat.setBackgroundResource(0);
                break;
            case Calendar.WEDNESDAY:
                layoutsun.setBackgroundResource(0);
                layoutmon.setBackgroundResource(0);
                layouttue.setBackgroundResource(0);
                layoutwed.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.datebg));
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
                layoutthu.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.datebg));
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
                layoutfri.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.datebg));
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
                layoutsat.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.datebg));
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

        //DateFormat dateFormat = new SimpleDateFormat("dd");
        //Date date = new Date();
        //int datetoday =  Integer.parseInt(dateFormat.format(date));

        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date currentsun = c.getTime();

        Calendar ctoday = Calendar.getInstance();
        //ctoday.setFirstDayOfWeek(Calendar.SUNDAY);
        ctoday.set(Calendar.HOUR_OF_DAY,0);
        Date today = ctoday.getTime();
        long diff = today.getTime() - currentsun.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long daydiff = hours / 24;
        Log.i(TAG, "day difference is "+daydiff);
        if (daydiff>2 && premium == false){
            Log.i(TAG, "day difference is "+daydiff);
            layoutsun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),ProFeature.class));
                }
            });
        }
        else {
            layoutsun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Calendar c = Calendar.getInstance();
                    c.setFirstDayOfWeek(Calendar.SUNDAY);
                    c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    Date currentTime = c.getTime();
                    Log.i(TAG, "Current Sunday date: "+currentTime);
                    List<NotificationAppView> list = new LinkedList<NotificationAppView>();
                    try {
                        NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
                        list = dao.getOverviewDay(currentTime);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    NotificationAppViewAdapter adapter = new NotificationAppViewAdapter(getActivity(), list);

                    ListView listView = (ListView) getActivity().findViewById(R.id.list_view);
                    listView.setAdapter(adapter);
                }
            });
        }

        Calendar cmon = Calendar.getInstance();
        cmon.setFirstDayOfWeek(Calendar.SUNDAY);
        cmon.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date currentmon = c.getTime();

        //Calendar ctoday = Calendar.getInstance();
        //ctoday.setFirstDayOfWeek(Calendar.SUNDAY);
        //ctoday.set(Calendar.HOUR_OF_DAY,0);
        //Date today = ctoday.getTime();
        long diffmon = today.getTime() - currentsun.getTime();
        long secondmon = diffmon / 1000;
        long minutesmon = secondmon / 60;
        long hoursmon = minutesmon / 60;
        long daydiffmon = hoursmon / 24;
        Log.i(TAG, "day difference is "+daydiffmon);
        if (daydiffmon>2 && premium == false){
            Log.i(TAG, "day difference is "+daydiffmon);
            layoutmon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),ProFeature.class));
                }
            });
        }
        else {

            layoutmon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Calendar c = Calendar.getInstance();
                    c.setFirstDayOfWeek(Calendar.SUNDAY);
                    c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    Date currentTime = c.getTime();
                    Log.i(TAG, "Current Monday date: "+currentTime);
                    List<NotificationAppView> list = new LinkedList<NotificationAppView>();
                    try {
                        NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
                        list = dao.getOverviewDay(currentTime);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    NotificationAppViewAdapter adapter = new NotificationAppViewAdapter(getActivity(), list);

                    ListView listView = (ListView) getActivity().findViewById(R.id.list_view);
                    listView.setAdapter(adapter);
                }
            });

        }


        layouttue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                c.setFirstDayOfWeek(Calendar.SUNDAY);
                c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                Date currentTime = c.getTime();
                Log.i(TAG, "Current Tuesday date: "+currentTime);
                List<NotificationAppView> list = new LinkedList<NotificationAppView>();
                try {
                    NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
                    list = dao.getOverviewDay(currentTime);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                NotificationAppViewAdapter adapter = new NotificationAppViewAdapter(getActivity(), list);

                ListView listView = (ListView) getActivity().findViewById(R.id.list_view);
                listView.setAdapter(adapter);
            }
        });
        layoutwed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                c.setFirstDayOfWeek(Calendar.SUNDAY);
                c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                Date currentTime = c.getTime();
                Log.i(TAG, "Current Wednesday date: "+currentTime);
                List<NotificationAppView> list = new LinkedList<NotificationAppView>();
                try {
                    NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
                    list = dao.getOverviewDay(currentTime);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                NotificationAppViewAdapter adapter = new NotificationAppViewAdapter(getActivity(), list);

                ListView listView = (ListView) getActivity().findViewById(R.id.list_view);
                listView.setAdapter(adapter);
            }
        });
        layoutthu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                c.setFirstDayOfWeek(Calendar.SUNDAY);
                c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                Date currentTime = c.getTime();
                Log.i(TAG, "Current Thursday date: "+currentTime);
                List<NotificationAppView> list = new LinkedList<NotificationAppView>();
                try {
                    NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
                    list = dao.getOverviewDay(currentTime);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                NotificationAppViewAdapter adapter = new NotificationAppViewAdapter(getActivity(), list);

                ListView listView = (ListView) getActivity().findViewById(R.id.list_view);
                listView.setAdapter(adapter);
            }
        });
        layoutfri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                c.setFirstDayOfWeek(Calendar.SUNDAY);
                c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                Date currentTime = c.getTime();
                Log.i(TAG, "Current Friday date: "+currentTime);
                List<NotificationAppView> list = new LinkedList<NotificationAppView>();
                try {
                    NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
                    list = dao.getOverviewDay(currentTime);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                NotificationAppViewAdapter adapter = new NotificationAppViewAdapter(getActivity(), list);

                ListView listView = (ListView) getActivity().findViewById(R.id.list_view);
                listView.setAdapter(adapter);
            }
        });
        layoutsat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                c.setFirstDayOfWeek(Calendar.SUNDAY);
                c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                Date currentTime = c.getTime();
                Log.i(TAG, "Current Saturday date: "+currentTime);
                List<NotificationAppView> list = new LinkedList<NotificationAppView>();
                try {
                    NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
                    list = dao.getOverviewDay(currentTime);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                NotificationAppViewAdapter adapter = new NotificationAppViewAdapter(getActivity(), list);

                ListView listView = (ListView) getActivity().findViewById(R.id.list_view);
                listView.setAdapter(adapter);
            }
        });

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

        SharedPreferences prefs = this.getActivity().getSharedPreferences("ProVersion", MODE_PRIVATE);
        premium = prefs.getBoolean("premium", false);
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

//        List<NotificationAppView> listyesterday = new LinkedList<NotificationAppView>();
//        Date yesterday = new Date(System.currentTimeMillis()-24*60*60*1000);
//        DateFormat format2=new SimpleDateFormat("EEEE");
//        String yesterdayDay=format2.format(yesterday);
//        try {
//            NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
//            listyesterday = dao.getOverviewDay(yesterday);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        int totalCountyesterday = 0;
//        for (NotificationAppView aList : listyesterday) {
//            totalCountyesterday += aList.Notifications;
//        }
//        Log.i(TAG, "Total Count Yesterday: "+totalCountyesterday);

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
        creategraph();

//        //Check if premium or not
//        if (premium == false){
//            switch (day) {
//                case Calendar.SUNDAY:
//                    for (int i = 0; i < WeekNotCountnotPremium.length; i++) {
//                        WeekNotCountnotPremium[i] = 0;
//                    }
//                    WeekNotCountnotPremium[Calendar.SUNDAY-1] = totalCount;
//                    break;
//                case Calendar.MONDAY:
//                    // Current day is Monday
//                    WeekNotCountnotPremium[Calendar.MONDAY-1] = totalCount;
//                    break;
//                case Calendar.TUESDAY:
//                    // etc.
//                    WeekNotCountnotPremium[Calendar.TUESDAY-1] = totalCount;
//                    break;
//                case Calendar.WEDNESDAY:
//                    WeekNotCountnotPremium[Calendar.WEDNESDAY-1] = totalCount;
//                    // etc.
//                    break;
//                case Calendar.THURSDAY:
//                    WeekNotCountnotPremium[Calendar.SUNDAY-1] = 0;
//                    WeekNotCountnotPremium[Calendar.THURSDAY-1] = totalCount;
//                    // etc.
//                    break;
//                case Calendar.FRIDAY:
//                    WeekNotCountnotPremium[Calendar.MONDAY-1] = 0;
//                    WeekNotCountnotPremium[Calendar.FRIDAY-1] = totalCount;
//                    // etc.
//                    break;
//                case Calendar.SATURDAY:
//                    WeekNotCountnotPremium[Calendar.TUESDAY-1] = 0;
//                    WeekNotCountnotPremium[Calendar.SATURDAY-1] = totalCount;
//                    // etc.
//                    break;
//            }
//
//            for (int i = 0; i < WeekNotCountnotPremium.length; ++i) {
//                Log.i(TAG, "WeekNotCountNotPremium: " + i + " count" + WeekNotCountnotPremium[i]);
//            }
//
//            ArrayList<BarEntry> values = new ArrayList<>();
//            for (int i = 0; i < WeekNotCountnotPremium.length; i++) {
//                float x = i;
//                float y = WeekNotCountnotPremium[i];
//                values.add(new BarEntry(x, y));
//            }
//            BarDataSet set1 = new BarDataSet(values, SET_LABEL);
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//            dataSets.add(set1);
//            set1.setDrawValues(false);
//
//            BarData data = new BarData(dataSets);
//            data.setBarWidth(0.7f);
//            chart.getDescription().setEnabled(false);
//            chart.setDrawValueAboveBar(false);
//            chart.setDrawGridBackground(false);
//            chart.setDrawBarShadow(false);
//            chart.setPinchZoom(false);
//            chart.getLegend().setEnabled(false);
//            chart.setFitBars(true);
//            chart.setElevation(1f);
//            chart.setScaleEnabled(false);
//            chart.setVisibleXRangeMinimum(values.size());
//            IMarker marker = new MyMarkerView(chart.getContext(), R.layout.custom_marker_view);
//            chart.setMarker(marker);
//
//
//            XAxis xAxis = chart.getXAxis();
//            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//            xAxis.setDrawGridLines(false);
//            xAxis.setAxisMinimum(0f);
//            xAxis.setSpaceMin(data.getBarWidth() / 2f);
//            xAxis.setSpaceMax(data.getBarWidth() / 2f);
//            xAxis.setAxisMinimum(-0.9f);
//            xAxis.setValueFormatter(new IAxisValueFormatter() {
//                @Override
//                public String getFormattedValue(float v, AxisBase axisBase) {
//                    return DaysForChart[(int) v];
//                }
//            });
//
//            YAxis axisLeft = chart.getAxisLeft();
//            axisLeft.setEnabled(true);
//            axisLeft.setDrawGridLines(true);
//
//            YAxis axisRight = chart.getAxisRight();
//            axisRight.setEnabled(false);
//            axisRight.setAxisMinimum(0f);
//            axisLeft.setAxisMinimum(0f);
//
//            data.setValueTextSize(12f);
//            chart.setData(data);
//            //chart.invalidate();
//        }
//        else {
//            switch (day) {
//                case Calendar.SUNDAY:
//                    for (int i = 0; i < WeekNotCount.length; i++) {
//                        WeekNotCount[i] = 0;
//                    }
//                    WeekNotCount[Calendar.SUNDAY-1] = totalCount;
//                    break;
//                case Calendar.MONDAY:
//                    // Current day is Monday
//                    WeekNotCount[Calendar.MONDAY-1] = totalCount;
//                    break;
//                case Calendar.TUESDAY:
//                    // etc.
//                    WeekNotCount[Calendar.TUESDAY-1] = totalCount;
//                    break;
//                case Calendar.WEDNESDAY:
//                    WeekNotCount[Calendar.WEDNESDAY-1] = totalCount;
//                    // etc.
//                    break;
//                case Calendar.THURSDAY:
//                    WeekNotCount[Calendar.THURSDAY-1] = totalCount;
//                    // etc.
//                    break;
//                case Calendar.FRIDAY:
//                    WeekNotCount[Calendar.FRIDAY-1] = totalCount;
//                    // etc.
//                    break;
//                case Calendar.SATURDAY:
//                    WeekNotCount[Calendar.SATURDAY-1] = totalCount;
//                    // etc.
//                    break;
//            }
//            for (int i = 0; i < WeekNotCount.length; ++i) {
//                Log.i(TAG, "WeekNotCount: " + i + " count" + WeekNotCount[i]);
//            }
//
//            ArrayList<BarEntry> values = new ArrayList<>();
//            for (int i = 0; i < WeekNotCount.length; i++) {
//                float x = i;
//                float y = WeekNotCount[i];
//                values.add(new BarEntry(x, y));
//            }
//            BarDataSet set1 = new BarDataSet(values, SET_LABEL);
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//            dataSets.add(set1);
//            set1.setDrawValues(false);
//
//            BarData data = new BarData(dataSets);
//            data.setBarWidth(0.7f);
//            chart.getDescription().setEnabled(false);
//            chart.setDrawValueAboveBar(false);
//            chart.setDrawGridBackground(false);
//            chart.setDrawBarShadow(false);
//            chart.setPinchZoom(false);
//            chart.getLegend().setEnabled(false);
//            chart.setFitBars(true);
//            chart.setElevation(1f);
//            chart.setScaleEnabled(false);
//            chart.setVisibleXRangeMinimum(values.size());
//            IMarker marker = new MyMarkerView(chart.getContext(), R.layout.custom_marker_view);
//            chart.setMarker(marker);
//
//
//            XAxis xAxis = chart.getXAxis();
//            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//            xAxis.setDrawGridLines(false);
//            xAxis.setAxisMinimum(0f);
//            xAxis.setSpaceMin(data.getBarWidth() / 2f);
//            xAxis.setSpaceMax(data.getBarWidth() / 2f);
//            xAxis.setAxisMinimum(-0.9f);
//            xAxis.setValueFormatter(new IAxisValueFormatter() {
//                @Override
//                public String getFormattedValue(float v, AxisBase axisBase) {
//                    return DaysForChart[(int) v];
//                }
//            });
//
//            YAxis axisLeft = chart.getAxisLeft();
//            axisLeft.setEnabled(true);
//            axisLeft.setDrawGridLines(true);
//
//            YAxis axisRight = chart.getAxisRight();
//            axisRight.setEnabled(false);
//            axisRight.setAxisMinimum(0f);
//            axisLeft.setAxisMinimum(0f);
//
//            data.setValueTextSize(12f);
//            chart.setData(data);
//            //chart.invalidate();
//        }

        NotificationAppViewAdapter adapter = new NotificationAppViewAdapter(getActivity(), list);

        ListView listView = (ListView) getActivity().findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }
    public void creategraph(){
        // for sunday
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date sunday = c.getTime();
        Log.i(TAG, "Current Sunday date: "+sunday);
        List<NotificationAppView> listsun = new LinkedList<NotificationAppView>();
        try {
            NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
            listsun = dao.getOverviewDay(sunday);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int totalCountsun = 0;
        for (NotificationAppView aList : listsun) {
            totalCountsun += aList.Notifications;
        }
        // for monday
        Calendar c2 = Calendar.getInstance();
        c2.setFirstDayOfWeek(Calendar.SUNDAY);
        c2.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date monday = c2.getTime();
        Log.i(TAG, "Current Monday date: "+monday);
        List<NotificationAppView> listmon = new LinkedList<NotificationAppView>();
        try {
            NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
            listmon = dao.getOverviewDay(monday);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int totalCountmon = 0;
        for (NotificationAppView aList : listmon) {
            totalCountmon += aList.Notifications;
        }
        //for tuesday
        Calendar c3 = Calendar.getInstance();
        c3.setFirstDayOfWeek(Calendar.SUNDAY);
        c3.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        Date tuesday = c3.getTime();
        Log.i(TAG, "Current Tuesday date: "+tuesday);
        List<NotificationAppView> listtue = new LinkedList<NotificationAppView>();
        try {
            NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
            listtue = dao.getOverviewDay(tuesday);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int totalCounttue = 0;
        for (NotificationAppView aList : listtue) {
            totalCounttue += aList.Notifications;
        }


        //for wednesday
        Calendar c4 = Calendar.getInstance();
        c4.setFirstDayOfWeek(Calendar.SUNDAY);
        c4.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        Date wednesday = c4.getTime();
        Log.i(TAG, "Current Wednesday date: "+wednesday);
        List<NotificationAppView> listwed = new LinkedList<NotificationAppView>();
        try {
            NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
            listwed = dao.getOverviewDay(wednesday);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int totalCountwed = 0;
        for (NotificationAppView aList : listwed) {
            totalCountwed += aList.Notifications;
        }

        //for thursday
        Calendar c5 = Calendar.getInstance();
        c5.setFirstDayOfWeek(Calendar.SUNDAY);
        c5.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        Date thursday = c5.getTime();
        Log.i(TAG, "Current Saturday date: "+thursday);
        List<NotificationAppView> listthu = new LinkedList<NotificationAppView>();
        try {
            NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
            listthu = dao.getOverviewDay(thursday);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int totalCountthu = 0;
        for (NotificationAppView aList : listthu) {
            totalCountthu += aList.Notifications;
        }

        //for friday
        Calendar c6 = Calendar.getInstance();
        c6.setFirstDayOfWeek(Calendar.SUNDAY);
        c6.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        Date friday = c6.getTime();
        Log.i(TAG, "Current Saturday date: "+friday);
        List<NotificationAppView> listfri = new LinkedList<NotificationAppView>();
        try {
            NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
            listfri = dao.getOverviewDay(friday);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int totalCountfri = 0;
        for (NotificationAppView aList : listfri) {
            totalCountfri += aList.Notifications;
        }
        //for saturday
        Calendar c7 = Calendar.getInstance();
        c7.setFirstDayOfWeek(Calendar.SUNDAY);
        c7.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        Date saturday = c7.getTime();
        Log.i(TAG, "Current Saturday date: "+saturday);
        List<NotificationAppView> listsat = new LinkedList<NotificationAppView>();
        try {
            NotificationItemDao dao = getDatabaseHelper().getNotificationDao();
            listsat = dao.getOverviewDay(saturday);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int totalCountsat = 0;
        for (NotificationAppView aList : listsat) {
            totalCountsat += aList.Notifications;
        }

        int[] WeekNotCount = {totalCountsun, totalCountmon, totalCounttue, totalCountwed, totalCountthu, totalCountfri, totalCountsat};


        Calendar calendar1 = Calendar.getInstance();
        int day = calendar1.get(Calendar.DAY_OF_WEEK);
        if (premium == true){

        }
        else {


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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}