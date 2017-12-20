package com.example.anh.itenki.model;

import java.io.Serializable;

/**
 * Created by anh on 2017/12/18.
 */

public class AlarmNote implements Serializable {
    private int alarmId;
    private String alarmContent;
    private String alarmTime;
    private int pendingId;

    public AlarmNote() {

    }

    public AlarmNote(String content, String time) {
        this.alarmContent = content;
        this.alarmTime = time;
    }

    public AlarmNote(String content, String time, int pendingId) {
        this.pendingId = pendingId;
        this.alarmContent = content;
        this.alarmTime = time;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public int getPendingId() {
        return pendingId;
    }

    public void setPendingId(int pendingId) {
        this.pendingId = pendingId;
    }
}
