package com.example.dell.a20hour.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devadvance.circularseekbar.CircularSeekBar;
import com.example.dell.a20hour.R;
import com.example.dell.a20hour.db.entity.Skill;
import com.google.gson.Gson;

public class SkillActivity extends AppCompatActivity {

    TextView tvSkillTitle, tvTime, tvProgress;
    Skill skill;
    long selectedTime, MIN_TIME = 10*60*1000, TOTAL_TIME = 1 * 60 * 60 * 1000;
    int hoursCompleted = 0;
    CountDownTimer abortTimer;
    CircularSeekBar seekbar;
    Button btnStart;
    boolean timerRunnning = false;
    ImageView ivSkillStage;
    int[] stagesPic;
    String skillAsString;

    private void startAbortTimer(){

        abortTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {
                btnStart.setText("Abort in " + Long.toString(l/1000));
            }

            @Override
            public void onFinish() {
                this.cancel();

                //goto skillCount Activity
                Intent intent = new Intent(getApplicationContext(), SkillCountActivity.class);
                intent.putExtra("skill", skillAsString);
                intent.putExtra("selectedTime", selectedTime);
                startActivity(intent);

                finish(); //finish the activity
            }
        }.start();
    }

    public void formatTimeAndShow(long millis){
        int seconds = (int)millis/(1000);
        int h = seconds/3600;
        int rem = seconds%3600;
        int m = rem/60;
        String hr = Integer.toString(h), min = Integer.toString(m);
        if(h<=9) hr = "0" + Integer.toString(h);
        if(m<=9) min = "0" + Integer.toString(m);
        tvTime.setText(hr + ":" + min);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);

        //get reference to controls
        tvSkillTitle = findViewById(R.id.tvSCTitle);
        seekbar = findViewById(R.id.circularSeekBar);
        tvTime = findViewById(R.id.tvTime);
        tvProgress = findViewById(R.id.tvProgress);
        btnStart = findViewById(R.id.btnStart);
        ivSkillStage = findViewById(R.id.ivSkillStage);

        //set initial values
        seekbar.setProgress(1);
        selectedTime = MIN_TIME;
        formatTimeAndShow(MIN_TIME);
        stagesPic = new int[]{R.drawable.s1, R.drawable.s2, R.drawable.s3, R.drawable.s4, R.drawable.s5};
        ivSkillStage.setImageResource(stagesPic[0]);

        //get selected skill
        Gson gson = new Gson();
        skillAsString = getIntent().getStringExtra("skill");
        skill = gson.fromJson(skillAsString, Skill.class);

        //check if skill is completed
        if(skill.getTimeSpent() == TOTAL_TIME){
            //show completed message
            //maybe move to another congrats activity
        }


        tvSkillTitle.setText(skill.getTitle());
        hoursCompleted = (int)(skill.getTimeSpent()/(1000*60*60));
        tvProgress.setText(Integer.toString(hoursCompleted) + " hrs of 20 hours completed");


        //set Event Handlers
        seekbar.setOnSeekBarChangeListener(new CircleSeekBarListener());
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerRunnning){
                    abortTimer.cancel();
                    btnStart.setText("Start");
                    timerRunnning = false;
                }
                else{
                    startAbortTimer();
                    timerRunnning = true;
                }
            }
        });
    }

    public void handleBackBtnClick(View view) {
        super.onBackPressed();
    }

    public class CircleSeekBarListener implements CircularSeekBar.OnCircularSeekBarChangeListener {
        @Override
        public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
            //change color in min and more percentage
            if(progress <= 1){
                progress = 1;
                seekbar.setProgress(1);
            }
            selectedTime = progress*5*60*1000;
            formatTimeAndShow(selectedTime);
            int percent = (99/24)*progress;
            ivSkillStage.setImageResource(stagesPic[(percent/20)]);
        }
        @Override
        public void onStopTrackingTouch(CircularSeekBar seekBar) {}
        @Override
        public void onStartTrackingTouch(CircularSeekBar seekBar) {}
    }

    public void showSkillInfo(View view) {
        Intent intent = new Intent(getApplicationContext(), SkillInfo.class);
        intent.putExtra("skill", skillAsString);
        startActivity(intent);
    }

}
