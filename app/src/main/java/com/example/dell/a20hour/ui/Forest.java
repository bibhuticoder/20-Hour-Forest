package com.example.dell.a20hour.ui;

import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dell.a20hour.R;
import com.example.dell.a20hour.db.AppDatabase;
import com.example.dell.a20hour.db.entity.Task;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Forest extends AppCompatActivity {

    private Date p1, p2;
    TextView tvDateWeekRange, tvTotalTimeOfWeek, tvNumAliveTrees, tvNumDeadTrees, tvTotalTimeDay;
    DateFormat dateFormat1 = new SimpleDateFormat("yyyy MMM dd");
    DateFormat dateFormat2 = new SimpleDateFormat("MMM dd");
    ImageButton ibtnLeft, ibtnRight;
    BarChart barChart;
    Calendar calendar, tempCal;
    ArrayList<BarEntry> barEntries;
    AppDatabase db;
    long totalTimeWorked = 0, totalTimeDay = 0;
    List<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forest);

        //get reference to views
        tvDateWeekRange = findViewById(R.id.tvDateWeekRange);
        tvTotalTimeOfWeek = findViewById(R.id.tvTotalTimeOfWeek);
        tvNumDeadTrees = findViewById(R.id.tvNumDeadTrees);
        tvNumAliveTrees = findViewById(R.id.tvNumTrees);
        tvTotalTimeDay = findViewById(R.id.tvTotalTimeDay);
        ibtnLeft = findViewById(R.id.ibtnLeft);
        ibtnRight = findViewById(R.id.ibtnRight);
        barChart = findViewById(R.id.barchart);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "20hours")
                .allowMainThreadQueries()
                .build();
        tempCal = Calendar.getInstance();

        setupInitialDate();
        displayWeekRange();
        setupBarchart();
        populateBarChart();
        renderForest(1); //sunday

        //left
        ibtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClick(1);
            }
        });

        //right
        ibtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClick(0);
            }
        });

        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                renderForest((int)e.getX());
            }

            @Override
            public void onNothingSelected() {}
        });

    }

    private void displayWeekRange(){
        tvDateWeekRange.setText(dateFormat1.format(p1)+ " - " + dateFormat2.format(p2));
    }

    private void renderForest(int day){
        List<Task> tasksToday = new ArrayList<>();
        totalTimeDay = 0;
        int dead = 0;
        int alive = 0;
        for(int i=0; i<tasks.size(); i++){
            Task t = tasks.get(i);
            if(new Date(t.getDateCreated()).getDay()+1 == day){
                tasksToday.add(t);
                totalTimeDay += t.getTimeSpent();

                if(t.getTimeSpent() == t.getTargetTime()) alive++;
                else dead++;
            }
        }

        TextView tvDead = findViewById(R.id.tvDeadDay);
        TextView tvAlive = findViewById(R.id.tvAliveDay);

        tvAlive.setText("Alive : " + Integer.toString(alive));
        tvDead.setText("Dead : " + Integer.toString(dead));

        tvTotalTimeDay.setText((formatTime(totalTimeDay, false)));


    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter{

        private String[] mValues;

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int)value-1];
        }

        public MyXAxisValueFormatter(String[] mValues) {
            this.mValues = mValues;
        }
    }

    private  void setupBarchart(){
        //barchart
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawValueAboveBar(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);

        //days label
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setTextColor(Color.WHITE);

        //hide unnecessary stuffs
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawAxisLine(false);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);

        //set bottom labels
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        barChart.getXAxis().setValueFormatter(new MyXAxisValueFormatter(days));


        //barChart.getAxisLeft().setAxisMinimum(0);
//        barChart.getAxisLeft().setAxisMaximum(10*60*60);



    }

    private void setupInitialDate(){
        //set initial date
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int offset = calendar.get(Calendar.DAY_OF_WEEK)-1; //current Day
        //go to sunday
        calendar.add(Calendar.DATE, -offset);
        p1 = calendar.getTime();
        //go to saturday
        calendar.add(Calendar.DATE, 6);
        p2 = calendar.getTime();
    }

//  1: left, 0:right
    private void handleClick(int direction){
        if(direction == 1){
            //set calendar to p1
            calendar.setTime(p1);
            calendar.add(Calendar.DATE, -1); // p1 - 1 day
            p2 = calendar.getTime();

            //Again set calendar to p1
            calendar.setTime(p1);
            calendar.add(calendar.DATE, -7); // p1 - 7days
            p1 = calendar.getTime();
        }else if(direction == 0){
            //set calendar to p2
            calendar.setTime(p2);
            calendar.add(Calendar.DATE, 1); // p1 + 1 day
            p1 = calendar.getTime();

            //Again set calendar to p2
            calendar.setTime(p2);
            calendar.add(calendar.DATE, 7); // p2 + 7 days
            p2 = calendar.getTime();
        }
        displayWeekRange();
        populateBarChart();

        //render sunday and highlight it
        renderForest(1); //sunday
        barChart.highlightValue(1, 0);
    }

    private void populateBarChart(){
        tasks = db.taskDao().getAllBetween(p1.getTime(), p2.getTime());
        float[] values = {1f,1f,1f,1f,1f,1f,1f};
        int deadTrees = 0, aliveTrees = 0;

        totalTimeWorked = 0;
        for(int i=0; i<tasks.size(); i++){
            Task t = tasks.get(i);
            tempCal.setTime(new Date(t.getDateCreated()));
            int dayIndex = tempCal.get(Calendar.DAY_OF_WEEK)-1;
            values[dayIndex]+= (t.getTimeSpent()/(1000));
            totalTimeWorked += t.getTimeSpent();

            if(t.getTargetTime() == t.getTimeSpent()) aliveTrees++;
            else deadTrees++;
        }

        tvTotalTimeOfWeek.setText(formatTime(totalTimeWorked, true));
        tvNumAliveTrees.setText(Integer.toString(aliveTrees));
        tvNumDeadTrees.setText(Integer.toString(deadTrees));

        barEntries = new ArrayList<>();
        for(int i=0; i<7; i++) barEntries.add(new BarEntry(i+1, values[i]));
        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setDrawValues(false);

        BarData data = new BarData(barDataSet);
        barChart.setData(data);
        barChart.invalidate();

        //set gradient color after invalidate
        Paint paint = barChart.getRenderer().getPaintRender();
        int height = barChart.getHeight();
        LinearGradient linGrad = new LinearGradient(0, 0, 0, height,
                getResources().getColor(R.color.cpb_back),
                getResources().getColor(R.color.cpb_front),
                Shader.TileMode.REPEAT);
        paint.setShader(linGrad);

    }

    private String formatTime(long millis, boolean showZerovalues){

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long different = millis;
        long days = different / daysInMilli;
        different = different % daysInMilli;
        long hours = different / hoursInMilli;
        different = different % hoursInMilli;
        long mins = different / minutesInMilli;
        different = different % minutesInMilli;
        long secs = different / secondsInMilli;

        String d, h, m, s;
        if(showZerovalues){
            d = (days == 0)? " 0 days ": Long.toString(days) + " days ";
            h = (hours == 0)? " 0 hrs ": Long.toString(hours) + " hrs ";
            m = (mins == 0)? " 0 mins ": Long.toString(mins) + " mins ";
            s = (secs == 0)? " 0 secs ": Long.toString(secs) + " secs ";
        }else{
            d = (days == 0)? "": Long.toString(days) + " days ";
            h = (hours == 0)? "": Long.toString(hours) + " hrs ";
            m = (mins == 0)? "": Long.toString(mins) + " mins ";
            s = (secs == 0)? "": Long.toString(secs) + " secs ";
        }
        return d  +  h  + m +  s ;
    }
}
