package com.example.dell.a20hour;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class AllSkillsActivity extends AppCompatActivity {

    private GridView gvSkills;
    private ArrayList<Skill> skills;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    ImageButton ibtnForest, ibtnAbout, ibtnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_skills);

        gvSkills = findViewById(R.id.gvSkills);
        ibtnForest = findViewById(R.id.ibtnForest);
        ibtnAbout = findViewById(R.id.ibtnAbout);
        ibtnSettings = findViewById(R.id.ibtnSettings);

        skills = new ArrayList<>();
        final CustomAdapter customAdapter = new CustomAdapter();
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

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<Skill> set = new HashSet<>();
                for (DataSnapshot skillSnapshot: dataSnapshot.getChildren()) {
                    set.add((skillSnapshot.getValue(Skill.class)));
                }
                skills.clear();
                skills.addAll(set);
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        ibtnForest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Forest.class);
                startActivity(intent);
            }
        });

    }


    public void gotoNewSkill(View view) {
        Intent in = new Intent(getApplicationContext(), NewSkillActivity.class);
        startActivity(in);
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
