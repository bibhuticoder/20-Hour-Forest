package com.example.dell.a20hour;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.a20hour.db.AppDatabase;
import com.example.dell.a20hour.db.Skill;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class AllSkillsActivity extends AppCompatActivity {

    private GridView gvSkills;
    private List<Skill> skills;
    ImageButton ibtnForest, ibtnAbout, ibtnSettings, ibtnNew;
    AppDatabase db;
    CustomAdapter customAdapter;
    AlertDialog newSkillDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_skills);

        gvSkills = findViewById(R.id.gvSkills);
        ibtnForest = findViewById(R.id.ibtnForest);
        ibtnAbout = findViewById(R.id.ibtnAbout);
        ibtnSettings = findViewById(R.id.ibtnSettings);
        ibtnNew = findViewById(R.id.ibtnNew);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "20hours")
                .allowMainThreadQueries()
                .build();

        skills = db.skillDao().getAll();
        customAdapter = new CustomAdapter();
        gvSkills.setAdapter(customAdapter);


        gvSkills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), SkillActivity.class);

                //convert skill to string
                Gson gson = new Gson();
                String skillAsString = gson.toJson(skills.get(i));

                //pass string to intent
                intent.putExtra("skill", skillAsString);
                startActivity(intent);
            }
        });


        ibtnForest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Forest.class);
                startActivity(intent);
            }
        });

        ibtnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AllSkillsActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.new_skill_layout, null);
                final EditText edtNewSkill = mView.findViewById(R.id.edtNewSkill);
                CheckBox cbTwentyHr = mView.findViewById(R.id.cbTwentyHr);
                Button btnAdd = mView.findViewById(R.id.btnAdd);
                Button btnNewSkillCancel = mView.findViewById(R.id.btnNewSkillCancel);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = edtNewSkill.getText().toString();
                        if(title.length() > 0){
                            Skill skill = new Skill(title, 0, System.currentTimeMillis(), System.currentTimeMillis(), false);
                            db.skillDao().insertAll(skill);
                            skills = db.skillDao().getAll();
                            customAdapter.notifyDataSetChanged();
                            newSkillDialog.cancel();

                        }else{
                            Toast.makeText(getApplicationContext(), "Add a title", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btnNewSkillCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtNewSkill.setText("");
                        newSkillDialog.cancel();
                    }
                });

                mBuilder.setView(mView);
                newSkillDialog = mBuilder.create();
                newSkillDialog.show();

            }
        });


    }


    public void gotoNewSkill(View view) {
//        Intent in = new Intent(getApplicationContext(), NewSkillActivity.class);
//        startActivity(in);
    }
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return skills.size();
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
            view = getLayoutInflater().inflate(R.layout.skill_adapter, null);
            TextView tvSkillName = view.findViewById(R.id.tvSCTitle);
            ProgressBar pbPercent = view.findViewById(R.id.pbPercentage);

            tvSkillName.setText(skills.get(i).getTitle());


            //calculate percentage
            // x % of 20 hour = skills.get(i).getMillisDone()

            pbPercent.setProgress(0);

            return view;
        }
    }
}
