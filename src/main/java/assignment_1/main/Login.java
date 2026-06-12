package assignment_1.main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.regex.Pattern;

public class Login {
    private String registeredFirstName;
    private String registeredLastName;
    private String registeredUsername;
    private String registeredPassword;
    private String registeredCellPhone;
    private String loggedInUser;   // stores the username after successful login

    public Login() {
        loggedInUser = "";
    }

    public String registerUser(String firstName, String lastName, String username, String password, String cellPhone) {
        
        // Validate username
        if (!username.contains("_") || username.length() > 5) {
            return "Username is not correctly formatted. Please ensure it contains an underscore and is at most 5 characters long.";
        }

        // Validate password
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            if (Character.isDigit(c)) hasNumber = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        if (password.length() < 8 || !hasCapital || !hasNumber || !hasSpecial) {
            return "Password is not correctly formatted. Please ensure it is at least 8 characters long, contains a capital letter, a number, and a special character.";
        }

        // Validate cell phone number
        if (!cellPhone.matches("^\\+27[0-9]{9}$")) {
            return "Cell phone number is not correctly formatted. Please use +27 followed by 9 digits (e.g., +27831234567).";
        }

        // Store data
        registeredFirstName = firstName;
        registeredLastName = lastName;
        registeredUsername = username;
        registeredPassword = password;
        registeredCellPhone = cellPhone;

        return "Registration successful! You can now log in.";
    }

    public boolean loginUser(String username, String password) {
        if (registeredUsername != null && registeredUsername.equals(username) &&
            registeredPassword != null && registeredPassword.equals(password)) {
            loggedInUser = username;
            return true;
        }
        return false;
    }

    public String returnLoginStatus() {
        if (loggedInUser == null || loggedInUser.isEmpty()) {
            return "Login failed. Please check your credentials.";
        } else {
            return "Welcome " + loggedInUser + "!";
        }
    }

    public String getLoggedInUser() {
        return loggedInUser == null ? "" : loggedInUser;
    }
}