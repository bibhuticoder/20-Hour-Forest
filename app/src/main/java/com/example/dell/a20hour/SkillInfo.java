package com.example.dell.a20hour;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SkillInfo extends AppCompatActivity {


    Skill skill;
    ArrayList<SkillTask> activities;
    TextView tvSkillInfoTitle, tvSkillPercentage, tvSkillDateCreated, tvSkillDateUpdated;
    ListView lvActivities;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    String skillAsString;
    SimpleDateFormat dateFormat;

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


        Gson gson = new Gson();
        skillAsString = getIntent().getStringExtra("skill");
        skill = gson.fromJson(skillAsString, Skill.class);
        tvSkillInfoTitle.setText(skill.getTitle());
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'-'hh:mm:ss");
        tvSkillPercentage.setText("Completed: " + Long.toString(skill.getMillisDone()/(1000*60*60)) + " hours");
        tvSkillDateCreated.setText("Date Started: " + dateFormat.format(skill.getDate_created()));
        tvSkillDateUpdated.setText("Last Active at : " + dateFormat.format(skill.getDate_updated()));

        //activities
        final CustomAdapter customAdapter = new CustomAdapter();
        lvActivities.setAdapter(customAdapter);


    }

    public void deleteSkill(View view) {

    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return skill.getSkillTasks().size();
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
            TextView tvActivityDate = view.findViewById(R.id.tvActivityDate);
            TextView tvActivityTimestamp = view.findViewById(R.id.tvActivityTimestamp);
            TextView tvSTTarget = view.findViewById(R.id.tvSTTarget);

            SkillTask sk = (SkillTask)skill.getSkillTasks().get(i);

            tvActivityDate.setText(dateFormat.format(sk.getDateCreated()));
            tvActivityTimestamp.setText(Long.toString(sk.getTimespan()));
            tvSTTarget.setText(Long.toString(sk.getTargetTime()));

            return view;
        }
    }
}
