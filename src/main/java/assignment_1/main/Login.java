/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment_1.main;

import java.util.regex.Pattern;

public class Login {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhone;
    private boolean isLoggedIn;

    public Login() {
        isLoggedIn = false;
    }

    public boolean loginUser(String inputUsername, String inputPassword) {
        if (username != null && username.equals(inputUsername) && password != null && password.equals(inputPassword)) {
            isLoggedIn = true;
            return true;
        } else {
            isLoggedIn = false;
            return false;
        }
    }

    public String registerUser(String firstName, String lastName, String username, String password, String cellPhone) {
        // Validate first – only store if all checks pass
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellPhone)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        // All validations passed – store credentials
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhone = cellPhone;
        return "Registration successful! You can now log in.";
    }

    // Public methods for unit testing
    public boolean checkUserName() {
        return checkUserName(this.username);
    }

    public boolean checkPasswordComplexity() {
        return checkPasswordComplexity(this.password);
    }

    public boolean checkCellPhoneNumber() {
        return checkCellPhoneNumber(this.cellPhone);
    }

    // Private validation helpers
    private boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    private boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasUpper = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasUpper && hasDigit && hasSpecial;
    }

    private boolean checkCellPhoneNumber(String cellPhone) {
        String regex = "^\\+27[0-9]{9}$";
        return cellPhone != null && Pattern.matches(regex, cellPhone);
    }

    public String getPasswordMessage() {
        if (password != null && checkPasswordComplexity(password)) {
            return "Password successfully captured.";
        } else {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
    }

    public String getCellPhoneMessage() {
        if (cellPhone != null && checkCellPhoneNumber(cellPhone)) {
            return "Cell number successfully captured.";
        } else {
            return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }
    }

    public String returnLoginStatus() {
        if (isLoggedIn) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}