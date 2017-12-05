package com.example.dell.a20hour.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by DELL on 12/5/2017.
 */
@Dao
public interface SkillDao {
    @Query("SELECT * FROM skills")
    List<Skill> getAll();

    @Query("SELECT * FROM skills WHERE id = :id")
    Skill getById(int id);

    @Insert
    void insertAll(Skill... skills);

    @Delete
    void delete(Skill skill);

    @Update
    void update(Skill skill);

}
