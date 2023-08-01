package com.example.demo;

public class Post {
    private int id;
    private String platform;
    private String content;
    private String scheduledTime;

    public Post() {
    }

    public Post(int id, String platform, String content, String scheduledTime) {
        this.id = id;
        this.platform = platform;
        this.content = content;
        this.scheduledTime = scheduledTime;
    }

    // Getter and Setter for 'id'
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for 'platform'
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    // Getter and Setter for 'content'
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getter and Setter for 'scheduledTime'
    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    @Override
    // overridden to provide a user-friendly representation of a Post object.
    public String toString() {
        return "ID: " + id +
                ", Platform: " + platform +
                ", Content: " + content +
                ", Scheduled Time: " + scheduledTime;
    }
}
