package com.example.dell.a20hour;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.Date;

public class SkillCountActivity extends AppCompatActivity {

    CircularProgressBar progressBar;
    ImageView ivSCStage;
    int[] stagesPic;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    CountDownTimer mainTimer;
    long selectedTime, mainTimerCount = 0,  MIN_TIME = 5*60*1000;
    Skill skill;
    TextView tvSCTime, tvMusicMessage;
    String skillAsString;
    int hoursCompleted;
    MediaPlayer mediaPlayer;
    boolean music = false;
    ImageButton ibtnMusic;
    Button btnAbort;
    ListView listView;
    AlertDialog musicListDialog;
    int selectedMusic = 0;
    int musicList[];
    String musicNameList[];
    boolean unExpectedPause = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_count);

        //get reference to controls
        tvSCTime = findViewById(R.id.tvSCTime);
        ivSCStage = findViewById(R.id.ivSCStage);
        progressBar = findViewById(R.id.progressBar);
        ibtnMusic = findViewById(R.id.ibtnMusic);
        btnAbort = findViewById(R.id.btnAbort);
        tvMusicMessage = findViewById(R.id.tvMusicMessage);

        //get skill selected_time
        Gson gson = new Gson();
        skillAsString = getIntent().getStringExtra("skill");
        skill = gson.fromJson(skillAsString, Skill.class);
        selectedTime = getIntent().getLongExtra("selectedTime", MIN_TIME);
        tvMusicMessage.setVisibility(View.INVISIBLE);

        listView = new ListView(this);
        musicNameList = new String[]{"Forest", "Cricket Chirping", "Ocean Waves"};
        musicList = new int[]{R.raw.rainforest, R.raw.cricket, R.raw.ocean_waves};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, musicNameList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMusic = i;
                setMusic(selectedMusic);
            }
        });




        //ibtnMusic Longpress
        ibtnMusic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showMusicListDialog();
                return false;
            }
        });

        ibtnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(music){
                    mediaPlayer.stop();
                    ibtnMusic.setImageResource(R.drawable.sound_off);
                    music = false;
                }
                else{
                    setMusic(selectedMusic);
                    ibtnMusic.setImageResource(R.drawable.sound_on);
                    music = true;
                    tvMusicMessage.setText("[Playing '"+ musicNameList[selectedMusic] +"'] Longpress for others");
                    tvMusicMessage.setVisibility(View.VISIBLE);
                    new CountDownTimer(2000, 1000) {
                        @Override
                        public void onTick(long l) {}
                        @Override
                        public void onFinish() {
                            tvMusicMessage.setVisibility(View.INVISIBLE);
                            this.cancel();
                        }
                    }.start();
                }
            }
        });

        //btnAbort
        btnAbort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelTimerDialog();
            }
        });
        startMainTimer();

        //notificataion
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
    }

    public void setMusic(int musicId){
        if(mediaPlayer != null) mediaPlayer.stop(); //stop if music playing
        mediaPlayer = MediaPlayer.create(getApplicationContext(), musicList[musicId]);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        music = true;
        ibtnMusic.setImageResource(R.drawable.sound_on);
    }

    public void startMainTimer(){
        mainTimer = new CountDownTimer(selectedTime, 1000) {
            @Override
            public void onTick(long l) {
                mainTimerCount += 1000;
                int progress = 100 - (int)((l*100)/selectedTime);
                progressBar.setProgress(progress);
                formatTimeAndShow(l);
            }

            @Override
            public void onFinish() {
                //store skill task in database and set skilltask as true
                stopMainTimer();
            }
        }.start();
    }

    public void stopMainTimer(){
        mainTimer.cancel();
        //store skill task in database and set skilltask as false
        skill.setMilllisDone(skill.getMillisDone() + mainTimerCount);
        skill.getSkillTasks().add(new SkillTask(System.currentTimeMillis(), mainTimerCount, selectedTime));
        root.child(skill.getId()).setValue(skill);
    }

    public void formatTimeAndShow(long millis){

        int seconds = (int)millis/(1000);
        int h = seconds/3600;
        int rem = seconds%3600;
        int m = rem/60;
        rem = rem%60;
        int s = rem;
        String hr = Integer.toString(h), min = Integer.toString(m), sec = Integer.toString(s);
        if(h<=9) hr = "0" + Integer.toString(h);
        if(m<=9) min = "0" + Integer.toString(m);
        if(s<=9) sec = "0" + Integer.toString(s);
        tvSCTime.setText(hr + ":" + min + "." + sec);
    }

    private  void showCancelTimerDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to abort ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stopMainTimer();
                        if(music) mediaPlayer.stop();
                        goBack();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).show();
    }

    private void showMusicListDialog(){
        AlertDialog.Builder builder = null;
        if(musicListDialog == null){
            builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setMessage("Select the music you want to listen to");
            builder.setPositiveButton("Ok",null);
            builder.setView(listView);
            musicListDialog = builder.create();
            musicListDialog.show();
        }else{
            musicListDialog.show();
        }
    }

    private void goBack(){
        unExpectedPause = false;
        Intent intent = new Intent(getApplicationContext(), SkillActivity.class);
        intent.putExtra("skill", skillAsString);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        showCancelTimerDialog();
    }


    NotificationCompat.Builder notification;
    private static final int UNIQUE = 12121212;

    @Override
    protected void onPause() {
        super.onPause();

        if(!unExpectedPause) return;

        notification.setPriority(Notification.PRIORITY_MAX);
        notification.setSmallIcon(R.drawable.forest);
        notification.setTicker("This is a ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Return to to yor work.");
        notification.setContentText("Your plant is going to die. Return quickly or you won't be able to eat mango if the mango tree dies");

        //delay: 0
        //vibrate: 1000
        //sleep: 1000
        //vibrate: 100
        //sleep: 1000
        //vibrate: 1000
        notification.setVibrate(new long[] { 0, 1000, 1000, 500, 1000, 1000});

        Intent intent = new Intent(this, SkillCountActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(UNIQUE, notification.build());


        //Toast.makeText(getApplicationContext(), "i am paused", Toast.LENGTH_LONG).show();
    }
}
