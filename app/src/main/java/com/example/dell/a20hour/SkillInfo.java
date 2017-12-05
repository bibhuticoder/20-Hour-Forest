package com.example.dell.a20hour;

import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
    ImageButton ibtnDeleteSkill, ibtnEditSkill;
    Dialog editSkillDialog;

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
        ibtnEditSkill = findViewById(R.id.ibtnEditSkill);


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

                AlertDialog.Builder builder = new AlertDialog.Builder(SkillInfo.this);
                //builder.setTitle("Are you sure you want to delete ?");
                builder.setMessage("Deleting the task will delete all related trees from forest.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.skillDao().delete(skill);
                            Intent intent = new Intent(getApplicationContext(), AllSkillsActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).show();
            }
        });

        ibtnEditSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(SkillInfo.this);
                final View mView = getLayoutInflater().inflate(R.layout.edit_skill_layout, null);
                final EditText edtEditSkillTitle = mView.findViewById(R.id.edtEditSkillTitle);
                CheckBox cbEditSkillTwentyHr = mView.findViewById(R.id.cbEditSkillTwentyHr);
                Button btnEditSkillUpdate = mView.findViewById(R.id.btnEditSkillUpdate);
                Button btnEditSkillCancel = mView.findViewById(R.id.btnEditSkillCancel);

                edtEditSkillTitle.setText(skill.getTitle());

                btnEditSkillUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = edtEditSkillTitle.getText().toString();
                        if(title.length() > 0){
                            skill.setTitle(title);
                            db.skillDao().update(skill);
                            Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                            editSkillDialog.cancel();
                        }else Toast.makeText(getApplicationContext(), "Title is empty", Toast.LENGTH_SHORT).show();
                    }
                });

                btnEditSkillCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtEditSkillTitle.setText("");
                        editSkillDialog.cancel();
                    }
                });

                mBuilder.setView(mView);
                editSkillDialog = mBuilder.create();
                editSkillDialog.show();
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
