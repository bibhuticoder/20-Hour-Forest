package com.example.dell.a20hour;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.a20hour.db.AppDatabase;
import com.example.dell.a20hour.db.Skill;
import com.example.dell.a20hour.db.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SkillInfo extends AppCompatActivity {


    Skill skill;
    List<Task> tasks;
    TextView tvSkillInfoTitle, tvSkillPercentage, tvSkillDateCreated, tvSkillDateUpdated;
    ListView lvActivities;
    String skillAsString;
    SimpleDateFormat dateFormat;
    SimpleDateFormat timeFormat;
    AppDatabase db;
    ImageButton ibtnDeleteSkill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_info);

        //get reference to controls
        tvSkillInfoTitle = findViewById(R.id.tvSCTitle);
        tvSkillPercentage = findViewById(R.id.tvSkillPercentage);
        tvSkillDateCreated = findViewById(R.id.tvSkillDateCreated);
        tvSkillDateUpdated = findViewById(R.id.tvSkillDateUpdated);
        lvActivities = findViewById(R.id.lvActivities);
        ibtnDeleteSkill = findViewById(R.id.ibtnDeleteSkill);


        Gson gson = new Gson();
        skillAsString = getIntent().getStringExtra("skill");
        skill = gson.fromJson(skillAsString, Skill.class);
        tvSkillInfoTitle.setText(skill.getTitle());
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'-'hh:mm:ss");
        timeFormat = new SimpleDateFormat("hh:mm:ss");
        tvSkillPercentage.setText("Completed: " + Long.toString(skill.getTimeSpent()/(1000*60*60)) + " hours");
        tvSkillDateCreated.setText("Date Started: " + dateFormat.format(skill.getDateCreated()));
        tvSkillDateUpdated.setText("Last Active at : " + dateFormat.format(skill.getDateUpdated()));

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "20hours")
                .allowMainThreadQueries()
                .build();
        tasks = db.taskDao().getAll(skill.getId());

        //activities
        final CustomAdapter customAdapter = new CustomAdapter();
        lvActivities.setAdapter(customAdapter);

        ibtnDeleteSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.skillDao().delete(skill);
                Intent intent = new Intent(getApplicationContext(), AllSkillsActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
            }
        });


    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return tasks.size();
        }

        @Override
        public Object getItem(int i) {
            return R.layout.skill_adapter;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.skilltask_adapter, null);
            TextView tvTaskDate = view.findViewById(R.id.tvTaskDate);
            TextView tvTaskDone = view.findViewById(R.id.tvTaskDone);
            TextView tvTaskTarget = view.findViewById(R.id.tvTaskTarget);

            Task task = tasks.get(i);

            tvTaskDate.setText(dateFormat.format(task.getDateCreated()));

            if(task.getTargetTime() != task.getTimeSpent()) tvTaskDate.setTextColor(Color.RED);

            tvTaskDone.setText("Done: " + timeFormat.format(task.getTimeSpent()));
            tvTaskTarget.setText("Target: " + timeFormat.format(task.getTargetTime()));

            return view;
        }
    }
}
