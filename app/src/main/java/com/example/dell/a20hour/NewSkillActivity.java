package com.example.dell.a20hour;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewSkillActivity extends AppCompatActivity {

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();

    private EditText edtTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_skill);

        edtTitle = findViewById(R.id.edtTitle);

    }

    public void addNewSkill(View view) {

        String title = edtTitle.getText().toString();

        if(title.length() > 0){
            String key = root.push().getKey();
            List tmp = new ArrayList<SkillTask>();
            tmp.add(new SkillTask(System.currentTimeMillis(), 0, 0));
            root.child(key).setValue(new Skill(key, title, 0, System.currentTimeMillis(), System.currentTimeMillis(), tmp));
            Intent intent = new Intent(getApplicationContext(), AllSkillsActivity.class);
            startActivity(intent);

        }else{
            Toast.makeText(getApplicationContext(), "Add a title", Toast.LENGTH_SHORT).show();
        }

    }
}
