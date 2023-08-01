package com.example.demo;

public class User {
	//encapsulation
    private int id;
    private String name;
    private String username;
    private String password;
    private String mobileNumber;

    public User() {
    }

    public User(int id, String name, String username, String password, String mobileNumber) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.mobileNumber = mobileNumber;
    }

 //encapsulation
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // encapsulation
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

   
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for 'mobileNumber'
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ", Name: " + name +
                ", Username: " + username +
                ", Mobile Number: " + mobileNumber;
    }
}
