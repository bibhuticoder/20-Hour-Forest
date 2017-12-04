package com.example.dell.a20hour;

import java.util.Date;

/**
 * Created by DELL on 12/3/2017.
 */

public class SkillTask{
    private long dateCreated;
    private long timespan;
    private long targetTime;

    public SkillTask(){}

    public long getTargetTime() {
        return targetTime;
    }

    public SkillTask(long dateCreated, long timespan, long targetTime) {
        this.dateCreated = dateCreated;
        this.timespan = timespan;
        this.targetTime = targetTime;

    }

    public long getDateCreated() {
        return dateCreated;
    }

    public long getTimespan() {
        return timespan;
    }
}
