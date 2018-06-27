package com.example.athandile.dear_diary.models;

import java.util.Date;

public class JournalEntry {
    private String id;
    private String uid;
    private String heading;
    private String description;
    private Date timestamp;

    public JournalEntry() { }

    public JournalEntry(String id, String uid, String heading, String description, Date timestamp) {
        this.id = id;
        this.uid = uid;
        this.heading = heading;
        this.description = description;
        this.timestamp = timestamp;
    }
    public JournalEntry(String uid, String heading, String description, Date timestamp) {
        this.id = id;
        this.uid = uid;
        this.heading = heading;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
