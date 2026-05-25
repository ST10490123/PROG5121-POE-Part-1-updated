/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package assignment_1.main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class QuickChatApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static Login login;
    private static int sentCount = 0;
    private static int messageLimit = 0;

    public static void main(String[] args) {
        System.out.println("=== QuickChat Application ===\n");

        login = new Login();

        // ---- Registration ----
        System.out.println("---- Registration ----");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter cell phone number (e.g., +27831234567): ");
        String cellPhone = scanner.nextLine();

        String regResult = login.registerUser(firstName, lastName, username, password, cellPhone);
        System.out.println(regResult);

        if (!regResult.equals("Registration successful! You can now log in.")) {
            System.out.println("Registration failed. Exiting program.");
            scanner.close();
            return;
        }

        // ---- Login (max 3 attempts) ----
        System.out.println("\n---- Login ----");
        boolean loggedIn = false;
        int attempts = 0;
        while (!loggedIn && attempts < 3) {
            System.out.print("Enter username: ");
            String loginUser = scanner.nextLine();
            System.out.print("Enter password: ");
            String loginPass = scanner.nextLine();

            boolean success = login.loginUser(loginUser, loginPass);
            if (success) {
                System.out.println(login.returnLoginStatus());
                loggedIn = true;
            } else {
                attempts++;
                if (attempts < 3) {
                    System.out.println("Incorrect credentials. Try again.");
                } else {
                    System.out.println("Too many failed attempts. Exiting.");
                    scanner.close();
                    return;
                }
            }
        }

        // ---- QuickChat messaging system ----
        System.out.println("\nWelcome to QuickChat.");

        // Ask for message limit
        messageLimit = setMessageLimit();

        boolean quit = false;
        while (!quit) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages (Coming Soon)");
            System.out.println("3. Quit");
            System.out.print("Select an option (1-3): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    sendMessages();
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    quit = true;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }

    private static int setMessageLimit() {
        while (true) {
            System.out.print("How many messages do you want to send? ");
            try {
                int limit = Integer.parseInt(scanner.nextLine().trim());
                if (limit > 0) return limit;
                System.out.println("Please enter a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }

    private static void sendMessages() {
        while (sentCount < messageLimit) {
            System.out.println("\n--- Compose Message #" + (sentCount + 1) + " ---");

            // Recipient validation
            String recipient;
            while (true) {
                System.out.print("Recipient (+27 followed by 9 digits): ");
                recipient = scanner.nextLine().trim();
                if (recipient.matches("^\\+27[0-9]{9}$")) break;
                System.out.println("Invalid format. Must be +27 and 9 digits (e.g., +27123456789).");
            }

            // Message text validation (max 250 chars)
            String messageText;
            while (true) {
                System.out.print("Message (max 250 chars): ");
                messageText = scanner.nextLine().trim();
                if (messageText.length() <= 250 && !messageText.isEmpty()) break;
                System.out.println("Please enter a message of less than 250 characters.");
            }

            // Action: Send, Disregard, Store
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Send Message");
            System.out.println("2. Disregard Message");
            System.out.println("3. Store Message to send later");
            System.out.print("Choice: ");
            String action = scanner.nextLine().trim();

            if (action.equals("1")) {
                long msgId = generateMessageId();
                sentCount++;
                String msgHash = generateMessageHash(msgId, sentCount, messageText);
                displayMessageDetails(msgId, msgHash, recipient, messageText);
                System.out.println("Message successfully sent");
                writeMessageToJsonFile(msgId, msgHash, recipient, messageText, sentCount);

                if (sentCount == messageLimit) {
                    System.out.println("\nAll " + messageLimit + " message(s) have been sent.");
                    System.out.println("Total number of messages sent: " + sentCount);
                    break;
                }
            } else if (action.equals("2")) {
                System.out.println("Message disregarded.");
            } else if (action.equals("3")) {
                long msgId = generateMessageId();
                writeStoredMessageToJsonFile(msgId, recipient, messageText);
                System.out.println("Message successfully stored in JSON file.");
            } else {
                System.out.println("Invalid action. Message not processed.");
            }

            if (sentCount < messageLimit) {
                System.out.print("\nCompose another message? (y/n): ");
                String again = scanner.nextLine().trim().toLowerCase();
                if (!again.equals("y")) break;
            }
        }
    }

    // ----- Helper methods -----
    private static long generateMessageId() {
        return 1_000_000_000L + (long)(Math.random() * 9_000_000_000L);
    }

    private static String generateMessageHash(long msgId, int sentNumber, String message) {
        String idStr = Long.toString(msgId);
        String firstTwo = idStr.substring(0, 2);
        String[] words = message.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 0 ? words[words.length - 1] : "";
        String hashBody = (firstWord + " " + lastWord).toUpperCase();
        return firstTwo + ":" + sentNumber + ":" + hashBody;
    }

    private static void displayMessageDetails(long msgId, String hash, String recipient, String message) {
        System.out.println("\n--- Message Details ---");
        System.out.println("Message ID: " + msgId);
        System.out.println("Message Hash: " + hash);
        System.out.println("Recipient: " + recipient);
        System.out.println("Message: " + message);
        System.out.println("------------------------\n");
    }

    private static void writeMessageToJsonFile(long msgId, String hash, String recipient, String text, int sentNum) {
        String json = String.format(
            "{\"messageId\":%d,\"hash\":\"%s\",\"recipient\":\"%s\",\"message\":\"%s\",\"sentNumber\":%d,\"status\":\"sent\"}",
            msgId, escapeJson(hash), escapeJson(recipient), escapeJson(text), sentNum
        );
        appendToFile("message.json", json);
    }

    private static void writeStoredMessageToJsonFile(long msgId, String recipient, String text) {
        String json = String.format(
            "{\"messageId\":%d,\"recipient\":\"%s\",\"message\":\"%s\",\"status\":\"stored\"}",
            msgId, escapeJson(recipient), escapeJson(text)
        );
        appendToFile("message.json", json);
    }

    private static void appendToFile(String filename, String line) {
        try (FileWriter fw = new FileWriter(filename, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(line);
        } catch (IOException e) {
            System.err.println("Error writing to " + filename + ": " + e.getMessage());
        }
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}