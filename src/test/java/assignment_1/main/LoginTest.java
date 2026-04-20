/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package assignment_1.main;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    @Test
    void testUsernameCorrect_Equals() {
        Login login = new Login();
        login.registerUser("Shammah", "Sadiki", "kyl_2", "Ch&sec@ke99!", "+27838968976");
        login.loginUser("kyl_1", "Ch&sec@ke99!");
        assertEquals("Welcome Shammah, Sadiki it is great to see you.", login.returnLoginStatus());
    }

    @Test
    void testUsernameIncorrect_Equals() {
        Login login = new Login();
        String msg = login.registerUser("Shammah", "Sadiki", "kyle!!!!!!!", "Ch&sec@ke99!", "+27838968976");
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.", msg);
    }

    @Test
    void testPasswordCorrect_Equals() {
        Login login = new Login();
        login.registerUser("Shammah", "Sadiki", "kyl_1", "Ch&sec@ke99!", "+27838968976");
        assertEquals("Password successfully captured.", login.getPasswordMessage());
    }

    @Test
    void testPasswordIncorrect_Equals() {
        Login login = new Login();
        login.registerUser("Shammah", "Sadiki", "kyl_1", "password", "+27838968976");
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", login.getPasswordMessage());
    }

    @Test
    void testCellPhoneCorrect_Equals() {
        Login login = new Login();
        login.registerUser("Shammah", "Sadiki", "kyl_1", "Ch&sec@ke99!", "+27838968976");
        assertEquals("Cell number successfully captured.", login.getCellPhoneMessage());
    }

    @Test
    void testCellPhoneIncorrect_Equals() {
        Login login = new Login();
        login.registerUser("Shammah", "Sadiki", "kyl_1", "Ch&sec@ke99!", "08966553");
        assertEquals("Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.", login.getCellPhoneMessage());
    }

    @Test
    void testLoginSuccessful_True() {
        Login login = new Login();
        login.registerUser("Shammah", "Sadiki", "kyl_1", "Ch&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&sec@ke99!"));
    }

    @Test
    void testLoginFailed_False() {
        Login login = new Login();
        login.registerUser("Shammah", "Sadiki", "kyl_1", "Ch&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("wrong", "wrong"));
    }

    @Test
    void testUsernameCorrect_True() {
        Login login = new Login();
        login.registerUser("Shammah", "Sadiki", "kyl_1", "Ch&sec@ke99!", "+27838968976");
        assertTrue(login.checkUserName());
    }

    @Test
    void testUsernameIncorrect_False() {
        Login login = new Login();
        login.registerUser("Shammah", "Sadiki", "kyle!!!!!!!", "Ch&sec@ke99!", "+27838968976");
        assertFalse(login.checkUserName());
    }

    @Test
    void testPasswordCorrect_True() {
        Login login = new Login();
        login.registerUser("Shammah", "Sadiki", "kyl_1", "Ch&sec@ke99!", "+27838968976");
        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    void testPasswordIncorrect_False() {
        Login login = new Login();
        login.registerUser("Shammah", "Sadiki", "kyl_1", "password", "+27838968976");
        assertFalse(login.checkPasswordComplexity());
    }

    @Test
    void testCellPhoneCorrect_True() {
        Login login = new Login();
        login.registerUser("Shammah", "Sadiki", "kyl_1", "Ch&sec@ke99!", "+27838968976");
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    void testCellPhoneIncorrect_False() {
        Login login = new Login();
        login.registerUser("Shammah", "Sadiki", "kyl_1", "Ch&sec@ke99!", "08966553");
        assertFalse(login.checkCellPhoneNumber());
    }
}