package com.example.dell.a20hour.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.dell.a20hour.db.entity.Task;

import java.util.List;

/**
 * Created by DELL on 12/5/2017.
 */
@Dao
public interface TaskDao {

    @Query("SELECT * FROM tasks WHERE skill_id = :skillId")
    List<Task> getAll(int skillId);

    @Query("SELECT * FROM tasks WHERE date_created BETWEEN :p1 AND :p2")
    List<Task> getAllBetween(Long p1, Long p2);

    @Query("SELECT * FROM tasks WHERE id = :id")
    Task getById(int id);

    @Insert
    void insertAll(Task... tasks);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);

}
