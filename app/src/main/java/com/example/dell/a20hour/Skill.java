package com.example.dell.a20hour;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by DELL on 12/2/2017.
 */

public class Skill {
    private String id;
    private String title;
    private long milllisDone;
    private long date_created;
    private long date_updated;
    public List<SkillTask> skillTasks;

    public List getSkillTasks() {
        return skillTasks;
    }

    public Skill(){}

    public long getMillisDone() {
        return milllisDone;
    }

    public void setMilllisDone(long milllisDone) {
        this.milllisDone = milllisDone;
    }

    public void setDate_updated(long date_updated) {
        this.date_updated = date_updated;
    }

    public void setSkillTasks(ArrayList<SkillTask> skillTasks) {
        this.skillTasks = skillTasks;
    }

    public Skill(String id, String title, long milllisDone, long date_created, long date_updated, List<SkillTask> skillTasks) {
        this.id = id;
        this.title = title;
        this.milllisDone = milllisDone;
        this.date_created = date_created;
        this.date_updated = date_updated;
        this.skillTasks = skillTasks;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public long getDate_created() {
        return date_created;
    }

    public long getDate_updated() {
        return date_updated;
    }

}
