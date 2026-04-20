/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package assignment_1.main;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login login = new Login();

        
        System.out.println("---- Registration ----");
        System.out.print("Enter first name: ");
        String firstName = input.nextLine();
        System.out.print("Enter last name: ");
        String lastName = input.nextLine();
        System.out.print("Enter username: ");
        String username = input.nextLine();
        System.out.print("Enter password: ");
        String password = input.nextLine();
        System.out.print("Enter cell phone number (e.g., +27831234567): ");
        String cellPhone = input.nextLine();

        String regResult = login.registerUser(firstName, lastName, username, password, cellPhone);
        System.out.println(regResult);

        
        if (!regResult.equals("Registration successful! You can now log in.")) {
            System.out.println("Registration failed. Exiting program.");
            input.close();
            return;
        }

        
        System.out.println("\n---- Login ----");
        boolean loggedIn = false;
        int attempts = 0;
        while (!loggedIn && attempts < 3) {
            System.out.print("Enter username: ");
            String loginUser = input.nextLine();
            System.out.print("Enter password: ");
            String loginPass = input.nextLine();

            boolean success = login.loginUser(loginUser, loginPass);
            if (success) {
                System.out.println(login.returnLoginStatus());
                loggedIn = true;
            } else {
                attempts++;
                if (attempts < 3) {
                    System.out.println("Incorrect credentials. Try again. ");
                } else {
                    System.out.println("Too many failed attempts. Exiting.");
                }
            }
        }

        input.close();
    }
}