package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//inherit the functionality from the abstract class
public class SocialMediaApp extends SocialMediaManager {
    public static void main(String[] args) {
        SocialMediaApp app = new SocialMediaApp();
        app.run();
    }

    // Implement abstract methods from the abstract class
    @Override
    protected User login() {
        User user = new User();

        while (true) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            if (username.isEmpty()) {
                System.out.println("Username is mandatory. Please try again.");
            } else {
                user.setUsername(username);
                break;
            }
        }

        while (true) {
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            if (password.isEmpty()) {
                System.out.println("Password is mandatory. Please try again.");
            } else {
                user.setPassword(password);
                break;
            }
        }

        Connection connection = DBConnection.getConnection();
        if (connection != null) {
            try {
                String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPassword());
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    user.setId(resultSet.getInt("id"));
                    System.out.println("Login successful. Welcome, " + user.getUsername() + "!");

                    return user;
                } else {
                    System.out.println("Invalid username or password. Login failed.");
                }

                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void signUp() {
        User newUser = new User();

        while (true) {
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            if (name.isEmpty()) {
                System.out.println("Name is mandatory. Please try again.");
            } else {
                newUser.setName(name);
                break;
            }
        }

        while (true) {
            System.out.print("Enter a username: ");
            String username = scanner.nextLine();
            if (username.isEmpty()) {
                System.out.println("Username is mandatory. Please try again.");
            } else {
                newUser.setUsername(username);
                break;
            }
        }

        while (true) {
            System.out.print("Enter a password: ");
            String password = scanner.nextLine();
            if (password.isEmpty()) {
                System.out.println("Password is mandatory. Please try again.");
            } else {
                newUser.setPassword(password);
                break;
            }
        }

        while (true) {
            System.out.print("Enter your mobile number: ");
            String mobileNumber = scanner.nextLine();
            if (mobileNumber.isEmpty()) {
                System.out.println("Mobile number is mandatory. Please try again.");
            } else {
                newUser.setMobileNumber(mobileNumber);
                break;
            }
        }

        Connection connection = DBConnection.getConnection();
        if (connection != null) {
            try {
                // Check if the mobile number or username already exists in the database
                String checkQuery = "SELECT * FROM user WHERE username = ? OR mobilenumber = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                checkStatement.setString(1, newUser.getUsername());
                checkStatement.setString(2, newUser.getMobileNumber());
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    System.out.println("Signup failed. The provided mobile number or username already exists.");
                    System.out.println("----------------------------------------");
                } else {
                    // If the result set is empty, the mobile number and username are unique, proceed with signup
                    String sql = "INSERT INTO user (name, username, password, mobilenumber) VALUES (?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, newUser.getName());
                    preparedStatement.setString(2, newUser.getUsername());
                    preparedStatement.setString(3, newUser.getPassword());
                    preparedStatement.setString(4, newUser.getMobileNumber());

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Signup successful. Welcome, " + newUser.getUsername() + "!");
                        currentUser = newUser; // Set the currentUser after successful signup
                        System.out.println("----------------------------------------");
                    } else {
                        System.out.println("Failed to sign up. Please try again.");
                    }

                    preparedStatement.close();
                }

                resultSet.close();
                checkStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    

 // addPost() method is overloaded to handle both scenarios by creating a new Post
    protected void addPost() {
        Post newPost = new Post();

        System.out.print("Enter platform (e.g., Twitter, Facebook): ");
        newPost.setPlatform(scanner.nextLine());

        System.out.print("Enter post content: ");
        newPost.setContent(scanner.nextLine());

        // Check if the user wants to schedule the post
        System.out.print("Do you want to schedule this post? (yes/no): ");
        String scheduleOption = scanner.nextLine();

        if (scheduleOption.equalsIgnoreCase("yes")) {
            System.out.print("Enter scheduled time (YYYY-MM-DD HH:mm:ss): ");
            newPost.setScheduledTime(scanner.nextLine());
        } else {
            newPost.setScheduledTime(null); // Set scheduled time as null if the user doesn't want to schedule
        }

        // Delegate to the main addPost() method with the Post object
        addPost(newPost);
    }

    // Overloaded addPost() method for a scheduled post
    private void addPost(String platform, String content, String scheduledTime) {
        Post newPost = new Post();
        newPost.setPlatform(platform);
        newPost.setContent(content);
        newPost.setScheduledTime(scheduledTime);

        //handles the common functionality of adding a post
        addPost(newPost);
    }

    // The main addPost() method that handles the common functionality
    private void addPost(Post newPost) {
        Connection connection = DBConnection.getConnection();
        if (connection != null) {
            try {
                String sql;
                PreparedStatement preparedStatement;

                if (newPost.getScheduledTime() != null) {
                    // Scheduled post - include the scheduled_time column in the query
                    sql = "INSERT INTO posts (platform, content, scheduled_time) VALUES (?, ?, ?)";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, newPost.getPlatform());
                    preparedStatement.setString(2, newPost.getContent());
                    preparedStatement.setString(3, newPost.getScheduledTime());
                } else {
                    // Regular post - set a default value (e.g., current timestamp) for the scheduled_time column
                    sql = "INSERT INTO posts (platform, content, scheduled_time) VALUES (?, ?, CURRENT_TIMESTAMP)";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, newPost.getPlatform());
                    preparedStatement.setString(2, newPost.getContent());
                }

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("**************************************");
                    System.out.println("Post added to the database!");
                    System.out.println("**************************************");
                } else {
                    System.out.println("**************************************");
                    System.out.println("Failed to add the post.");
                    System.out.println("**************************************");
                }

                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void editPost() {
        if (currentUser == null) {
            System.out.println("Please log in to edit a post.");
            return;
        }

        System.out.print("Enter the ID of the post to edit: ");
        int postId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Connection connection = DBConnection.getConnection();
        if (connection != null) {
            try {
                String selectQuery = "SELECT * FROM posts WHERE id = ?";
                PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                selectStatement.setInt(1, postId);
                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    Post post = new Post();
                    post.setId(resultSet.getInt("id"));
                    post.setPlatform(resultSet.getString("platform"));
                    post.setContent(resultSet.getString("content"));
                    post.setScheduledTime(resultSet.getString("scheduled_time"));

                    System.out.println("Existing Post:");
                    System.out.println(post);

                    System.out.println("Select what you want to edit:");
                    System.out.println("1. Content");
                    System.out.println("2. Scheduled Time");
                    System.out.print("Enter your choice: ");
                    int editChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    switch (editChoice) {
                        case 1:
                            System.out.print("Enter new post content: ");
                            String newContent = scanner.nextLine();
                            post.setContent(newContent);
                            break;
                        case 2:
                            System.out.print("Enter new scheduled time (YYYY-MM-DD HH:mm:ss): ");
                            String newScheduledTime = scanner.nextLine();
                            post.setScheduledTime(newScheduledTime);
                            break;
                        default:
                            System.out.println("Invalid choice. Nothing will be edited.");
                    }

                    String updateQuery = "UPDATE posts SET content = ?, scheduled_time = ? WHERE id = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setString(1, post.getContent());
                    updateStatement.setString(2, post.getScheduledTime());
                    updateStatement.setInt(3, post.getId());

                    int rowsAffected = updateStatement.executeUpdate();

                    if (rowsAffected > 0) {
                    	System.out.println("**************************************");
                        System.out.println("Post updated successfully!");
                        System.out.println("**************************************");
                    } else {
                        System.out.println("Failed to update the post.");
                    }

                } else {
                    System.out.println("Post not found with ID: " + postId);
                }

                resultSet.close();
                selectStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void schedulePost() {
        if (currentUser == null) {
            System.out.println("Please log in to schedule a post.");
            return;
        }

        Post newPost = new Post();

        System.out.print("Enter platform (e.g., Twitter, Facebook): ");
        newPost.setPlatform(scanner.nextLine());

        System.out.print("Enter post content: ");
        newPost.setContent(scanner.nextLine());

        System.out.print("Enter scheduled time (YYYY-MM-DD HH:mm:ss): ");
        newPost.setScheduledTime(scanner.nextLine());

        Connection connection = DBConnection.getConnection();
        if (connection != null) {
            try {
                String sql = "INSERT INTO posts (platform, content, scheduled_time) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, newPost.getPlatform());
                preparedStatement.setString(2, newPost.getContent());
                preparedStatement.setString(3, newPost.getScheduledTime());

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                	System.out.println("**************************************");
                    System.out.println("Scheduled Post added to the database!");
                    System.out.println("**************************************");
                } else {
                	System.out.println("**************************************");
                    System.out.println("Failed to add the Scheduled post.");
                    System.out.println("**************************************");
                }

                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void deletePost() {
        if (currentUser == null) {
            System.out.println("Please log in to delete a post.");
            return;
        }

        System.out.print("Enter the ID of the post to delete: ");
        int postId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        Connection connection = DBConnection.getConnection();
        if (connection != null) {
            try {
                String selectQuery = "SELECT * FROM posts WHERE id = ?";
                PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                selectStatement.setInt(1, postId);
                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    Post post = new Post();
                    post.setId(resultSet.getInt("id"));
                    post.setPlatform(resultSet.getString("platform"));
                    post.setContent(resultSet.getString("content"));
                    post.setScheduledTime(resultSet.getString("scheduled_time"));

                    System.out.println("Post to Delete:");
                    System.out.println(post);

                    System.out.print("Are you sure you want to delete this post? (yes/no): ");
                    String confirmation = scanner.nextLine();

                    if (confirmation.equalsIgnoreCase("yes")) {
                        String deleteQuery = "DELETE FROM posts WHERE id = ?";
                        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                        deleteStatement.setInt(1, postId);
                        int rowsAffected = deleteStatement.executeUpdate();

                        if (rowsAffected > 0) {
                        	System.out.println("**************************************");
                            System.out.println("Post deleted successfully!");
                            System.out.println("**************************************");
                        } else {
                        	System.out.println("**************************************");
                            System.out.println("Failed to delete the post.");
                            System.out.println("**************************************");
                        }
                    } else {
                    	System.out.println("**************************************");
                        System.out.println("Post deletion cancelled.");
                        System.out.println("**************************************");
                    }

                } else {
                	System.out.println("**************************************");
                    System.out.println("Post not found with ID: " + postId);
                    System.out.println("**************************************");
                }

                resultSet.close();
                selectStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void viewAllPosts() {
    	Connection connection = DBConnection.getConnection();
        if (connection != null) {
            try {
                String selectQuery = "SELECT * FROM posts";
                PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                ResultSet resultSet = selectStatement.executeQuery();

                while (resultSet.next()) {
                    int postId = resultSet.getInt("id");
                    String platform = resultSet.getString("platform");
                    String content = resultSet.getString("content");
                    String scheduledTime = resultSet.getString("scheduled_time");

                    System.out.println("Post:");
                    System.out.println("ID: " + postId);
                    System.out.println("Platform: " + platform);
                    System.out.println("Content: " + content);
                    System.out.println("Scheduled Time: " + scheduledTime);
                    System.out.println("----------------------------------------");
                }

                resultSet.close();
                selectStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }}