package com.example.dell.a20hour;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Forest extends AppCompatActivity {


    private Date p1, p2;
    TextView tvDateWeekRange;
    DateFormat dateFormat1 = new SimpleDateFormat("yyyy MMM dd");
    DateFormat dateFormat2 = new SimpleDateFormat("MMM dd");
    ImageButton ibtnLeft, ibtnRight;
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forest);

        //get reference to views
        tvDateWeekRange = findViewById(R.id.tvDateWeekRange);
        ibtnLeft = findViewById(R.id.ibtnLeft);
        ibtnRight = findViewById(R.id.ibtnRight);
        barChart = findViewById(R.id.barchart);

        //set initial date
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int offset = calendar.get(Calendar.DAY_OF_WEEK)-1; //current Day
        //go to sunday
        calendar.add(Calendar.DATE, -offset);
        p1 = calendar.getTime();
        //go to saturday
        calendar.add(Calendar.DATE, 6);
        p2 = calendar.getTime();

        displayWeekRange();

        //left
        ibtnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set calendar to p1
                calendar.setTime(p1);
                calendar.add(Calendar.DATE, -1); // p1 - 1 day
                p2 = calendar.getTime();

                //Again set calendar to p1
                calendar.setTime(p1);
                calendar.add(calendar.DATE, -7); // p1 - 7days
                p1 = calendar.getTime();

                displayWeekRange();
            }
        });

        //right
        ibtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set calendar to p2
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(p2);
                calendar.add(Calendar.DATE, 1); // p1 + 1 day
                p1 = calendar.getTime();

                //Again set calendar to p2
                calendar.setTime(p2);
                calendar.add(calendar.DATE, 7); // p2 + 7 days
                p2 = calendar.getTime();

                displayWeekRange();
            }
        });

        setupBarchart();

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 40f));
        barEntries.add(new BarEntry(2, 20f));
        barEntries.add(new BarEntry(3, 30f));
        barEntries.add(new BarEntry(4, 10f));
        barEntries.add(new BarEntry(5, 10f));
        barEntries.add(new BarEntry(6, 10f));
        barEntries.add(new BarEntry(7, 10f));

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setDrawValues(false);
        barDataSet.setColor(getResources().getColor(R.color.cpb_front));

        BarData data = new BarData(barDataSet);
        barChart.setData(data);




    }

    private void displayWeekRange(){
        tvDateWeekRange.setText(dateFormat1.format(p1)+ " - " + dateFormat2.format(p2));
    }

    private void renderForest(){

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

        //gradient fill
        Paint paint = barChart.getRenderer().getPaintRender();
        int height = barChart.getHeight();
        LinearGradient linGrad = new LinearGradient(0, 0, 0, height,
                getResources().getColor(R.color.cpb_back),
                getResources().getColor(R.color.cpb_front),
                Shader.TileMode.REPEAT);
        paint.setShader(linGrad);




    }


}
