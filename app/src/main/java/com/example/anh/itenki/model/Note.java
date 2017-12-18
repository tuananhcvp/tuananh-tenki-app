package com.example.anh.itenki.model;

import java.io.Serializable;

/**
 * Created by anh on 2017/12/18.
 */

public class Note implements Serializable {
    private int id;
    private String content;
    private String createTime;
    private String modifyTime;

    public Note() {

    }

    public Note(String content, String createTime, String modifyTime) {
        this.content = content;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
}
