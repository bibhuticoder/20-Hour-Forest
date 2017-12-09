package com.example.dell.a20hour.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 12/2/2017.
 */

@Entity(tableName = "skills")
public class Skill {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "time_spent")
    private long timeSpent;

    @ColumnInfo(name = "date_created")
    private long dateCreated;

    @ColumnInfo(name = "date_updated")
    private long dateUpdated;

    @ColumnInfo(name = "twenty_hr")
    private boolean twentyHr;

    public Skill(String title, long timeSpent, long dateCreated, long dateUpdated, boolean twentyHr) {
        this.title = title;
        this.timeSpent = timeSpent;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.twentyHr = twentyHr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public boolean isTwentyHr() {
        return twentyHr;
    }

    public void setTwentyHr(boolean twentyHr) {
        this.twentyHr = twentyHr;
    }
}
