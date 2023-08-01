package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
//abstraction
public abstract class SocialMediaManager extends DBConnection {
    protected static User currentUser = null;
    protected Scanner scanner;

    public SocialMediaManager() {
        scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            if (currentUser == null) {
                // Prompt for login if no user is currently logged in
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("Social Media Management System");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("1. Login");
                System.out.println("2. Signup");
                System.out.println("3. Exit");

                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        currentUser = login();
                        break;
                    case 2:
                        signUp();
                        break;
                    case 3:
                        System.out.println("Exiting the application.");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                // User is logged in, show options for managing posts
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("Social Media Management System");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("1. Add Post");
                System.out.println("2. Edit Post");
                System.out.println("3. Schedule Post");
                System.out.println("4. Delete Post");
                System.out.println("5. View All Posts");
                System.out.println("6. Logout");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        addPost();
                        break;
                    case 2:
                        editPost();
                        break;
                    case 3:
                        schedulePost();
                        break;
                    case 4:
                        deletePost();
                        break;
                    case 5:
                        viewAllPosts();
                        break;
                    case 6:
                        currentUser = null;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    
    protected abstract User login();
    protected abstract void signUp();
    protected abstract void addPost();
    protected abstract void editPost();
    protected abstract void schedulePost();
    protected abstract void deletePost();
    protected abstract void viewAllPosts();
}
