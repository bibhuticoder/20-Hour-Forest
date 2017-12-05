package com.example.dell.a20hour;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.a20hour.db.AppDatabase;
import com.example.dell.a20hour.db.Skill;
import com.example.dell.a20hour.db.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class NewSkillActivity extends AppCompatActivity {

    private AppDatabase db;

    private EditText edtTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_skill);

        edtTitle = findViewById(R.id.edtTitle);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "20hours")
                .allowMainThreadQueries()
                .build();

    }

    public void addNewSkill(View view) {

        String title = edtTitle.getText().toString();

        if(title.length() > 0){
           Skill skill = new Skill(title, 100, 100, 100, false);
           db.skillDao().insertAll(skill);
           Intent intent = new Intent(getApplicationContext(), AllSkillsActivity.class);
           startActivity(intent);

        }else{
            Toast.makeText(getApplicationContext(), "Add a title", Toast.LENGTH_SHORT).show();
        }

    }
}
