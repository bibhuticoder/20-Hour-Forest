package com.example.dell.a20hour.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.dell.a20hour.db.dao.SkillDao;
import com.example.dell.a20hour.db.dao.TaskDao;
import com.example.dell.a20hour.db.entity.Skill;
import com.example.dell.a20hour.db.entity.Task;

/**
 * Created by DELL on 12/5/2017.
 */

@Database(entities = {Skill.class, Task.class}, version = 1)
public abstract class  AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract SkillDao skillDao();
}
