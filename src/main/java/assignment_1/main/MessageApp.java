/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */


package assignment_1.main;

import assignment_1.main.Login;
import assignment_1.main.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;


public class MessageApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static Login login;
    private static int sentCount = 0;
    private static int messageLimit = 0;

    private static List<Message> sentMessages = new ArrayList<>();
    private static List<Message> disregardedMessages = new ArrayList<>();
    private static List<Message> storedMessages = new ArrayList<>();
    private static List<String> messageHashes = new ArrayList<>();
    private static List<Integer> messageIds = new ArrayList<>();

    private static int nextId = 1;
    private static final String JSON_FILE = "stored_messages.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        System.out.println("=== QuickChat Application ===\n");

        loadStoredMessages();
        login = new Login();

        // Registration
        System.out.println("---- Registration ----");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter cell phone number (Use this Cellphone number: +27831234567): ");
        String cellPhone = scanner.nextLine();

        String regResult = login.registerUser(firstName, lastName, username, password, cellPhone);
        System.out.println(regResult);
        if (!regResult.equals("Registration successful! You can now log in.")) {
            System.out.println("Registration failed. Exiting program.");
            scanner.close();
            return;
        }

        // Login
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
                if (attempts < 3) System.out.println("Incorrect credentials. Try again.");
                else {
                    System.out.println("Too many failed attempts. Exiting.");
                    scanner.close();
                    return;
                }
            }
        }

        System.out.println("\nWelcome to QuickChat.");
        messageLimit = setMessageLimit();
        preloadTestData();

        boolean quit = false;
        while (!quit) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Send Messages");
            System.out.println("2. View all messages (by category)");
            System.out.println("3. Stored Messages ");
            System.out.println("4. Quit");
            System.out.print("Select an option (1-4): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": sendMessages(); break;
                case "2": viewAllMessages(); break;
                case "3": storedMessagesMenu(); break;
                case "4": quit = true; System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }

    private static void loadStoredMessages() {
        File file = new File(JSON_FILE);
        if (!file.exists()) return;
        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<List<Message>>(){}.getType();
            List<Message> loaded = gson.fromJson(reader, type);
            if (loaded != null) {
                storedMessages.clear();
                messageHashes.clear();
                messageIds.clear();
                for (Message m : loaded) {
                    storedMessages.add(m);
                    messageHashes.add(m.getHash());
                    messageIds.add(m.getId());
                    if (m.getId() >= nextId) nextId = m.getId() + 1;
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read stored_messages.json: " + e.getMessage());
        }
    }

    private static void saveStoredMessages() {
        try (Writer writer = new FileWriter(JSON_FILE)) {
            gson.toJson(storedMessages, writer);
        } catch (IOException e) {
            System.err.println("Could not save stored_messages.json: " + e.getMessage());
        }
    }

    private static void preloadTestData() {
        String sender = "System";
        if (login != null) {
            String logged = login.getLoggedInUser();
            if (logged != null && !logged.isEmpty()) sender = logged;
        }

        Message m1 = new Message(nextId++, sender, "+27834557896", "Did you get the cake?", "Sent");
        sentMessages.add(m1);
        Message m2 = new Message(nextId++, sender, "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");
        storedMessages.add(m2);
        messageHashes.add(m2.getHash());
        messageIds.add(m2.getId());
        Message m3 = new Message(nextId++, sender, "+27834484567", "Yohoooo, I am at your gate.", "Disregard");
        disregardedMessages.add(m3);
        Message m4 = new Message(nextId++, "0838884567", "Friend", "It is dinner time !", "Sent");
        sentMessages.add(m4);
        Message m5 = new Message(nextId++, sender, "+27838884567", "Ok, I am leaving without you.", "Stored");
        storedMessages.add(m5);
        messageHashes.add(m5.getHash());
        messageIds.add(m5.getId());

        saveStoredMessages();
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

            String recipient;
            while (true) {
                System.out.print("Recipient (+27 followed by 9 digits): ");
                recipient = scanner.nextLine().trim();
                if (recipient.matches("^\\+27[0-9]{9}$")) break;
                System.out.println("Invalid format. Must be +27 and 9 digits (e.g., +27123456789).");
            }

            String messageText;
            while (true) {
                System.out.print("Message (max 250 chars): ");
                messageText = scanner.nextLine().trim();
                if (messageText.length() <= 250 && !messageText.isEmpty()) break;
                System.out.println("Please enter a message of less than 250 characters.");
            }

            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Send Message");
            System.out.println("2. Disregard Message");
            System.out.println("3. Store Message to send later");
            System.out.print("Choice: ");
            String action = scanner.nextLine().trim();

            String sender = (login != null && login.getLoggedInUser() != null) ? login.getLoggedInUser() : "Unknown";

            if (action.equals("1")) {
                Message msg = new Message(nextId++, sender, recipient, messageText, "Sent");
                sentMessages.add(msg);
                sentCount++;
                long msgId = generateMessageId();
                String oldHash = generateMessageHash(msgId, sentCount, messageText);
                displayMessageDetails(msgId, oldHash, recipient, messageText);
                System.out.println("Message successfully sent");
                if (sentCount == messageLimit) {
                    System.out.println("\nAll " + messageLimit + " message(s) have been sent.");
                    System.out.println("Total number of messages sent: " + sentCount);
                    break;
                }
            } else if (action.equals("2")) {
                Message msg = new Message(nextId++, sender, recipient, messageText, "Disregard");
                disregardedMessages.add(msg);
                System.out.println("Message disregarded.");
            } else if (action.equals("3")) {
                Message msg = new Message(nextId++, sender, recipient, messageText, "Stored");
                storedMessages.add(msg);
                messageHashes.add(msg.getHash());
                messageIds.add(msg.getId());
                saveStoredMessages();
                System.out.println("Message successfully stored.");
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

    private static void viewAllMessages() {
        System.out.println("\n--- Sent Messages ---");
        if (sentMessages.isEmpty()) System.out.println("(none)");
        else sentMessages.forEach(System.out::println);
        System.out.println("\n--- Disregarded Messages ---");
        if (disregardedMessages.isEmpty()) System.out.println("(none)");
        else disregardedMessages.forEach(System.out::println);
        System.out.println("\n--- Stored Messages ---");
        if (storedMessages.isEmpty()) System.out.println("(none)");
        else storedMessages.forEach(System.out::println);
    }

    private static void storedMessagesMenu() {
        while (true) {
            System.out.println("\n===== STORED MESSAGES =====");
            System.out.println("a. Display sender & recipient of all stored messages");
            System.out.println("b. Display the longest stored message");
            System.out.println("c. Search for a message ID -> show recipient & message");
            System.out.println("d. Search all messages for a particular recipient");
            System.out.println("e. Delete a message using its hash");
            System.out.println("f. Display full report of all stored messages");
            System.out.println("g. Back to main menu");
            System.out.print("Choose: ");
            String opt = scanner.nextLine().toLowerCase();

            switch (opt) {
                case "a":
                    System.out.println("\n--- Sender & Recipient of All Stored Messages ---");
                    if (storedMessages.isEmpty()) System.out.println("No stored messages.");
                    else storedMessages.forEach(m -> System.out.println("Sender: " + m.getSender() + " | Recipient: " + m.getRecipient()));
                    break;
                case "b":
                    if (storedMessages.isEmpty()) System.out.println("No stored messages.");
                    else {
                        int maxLen = storedMessages.stream().mapToInt(m -> m.getMessage().length()).max().orElse(0);
                        List<Message> longest = storedMessages.stream().filter(m -> m.getMessage().length() == maxLen).collect(Collectors.toList());
                        System.out.println("\n--- Longest Stored Message(s) ---");
                        longest.forEach(m -> System.out.println(m.getMessage()));
                    }
                    break;
                case "c":
                    System.out.print("Enter Message ID: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    storedMessages.stream().filter(m -> m.getId() == id).findFirst().ifPresentOrElse(
                        m -> { System.out.println("Recipient: " + m.getRecipient()); System.out.println("Message: " + m.getMessage()); },
                        () -> System.out.println("No stored message with ID " + id));
                    break;
                case "d":
                    System.out.print("Enter recipient (e.g., +27838884567): ");
                    String recip = scanner.nextLine();
                    List<Message> matches = storedMessages.stream().filter(m -> m.getRecipient().equals(recip)).collect(Collectors.toList());
                    if (matches.isEmpty()) System.out.println("No stored messages for recipient " + recip);
                    else matches.forEach(m -> System.out.println(m.getMessage()));
                    break;
                case "e":
                    System.out.print("Enter message hash: ");
                    String hash = scanner.nextLine();
                    boolean removed = storedMessages.removeIf(m -> m.getHash().equals(hash));
                    if (removed) {
                        messageHashes.clear(); messageIds.clear();
                        for (Message m : storedMessages) { messageHashes.add(m.getHash()); messageIds.add(m.getId()); }
                        saveStoredMessages();
                        System.out.println("Message with hash " + hash + " successfully deleted.");
                    } else System.out.println("No message found with that hash.");
                    break;
                case "f":
                    System.out.println("\n========== FULL REPORT OF STORED MESSAGES ==========");
                    if (storedMessages.isEmpty()) System.out.println("No stored messages.");
                    else storedMessages.forEach(m -> { System.out.println(m.fullReport()); System.out.println("----------------------------------------"); });
                    break;
                case "g": return;
                default: System.out.println("Invalid option.");
            }
        }
    }

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

    // ========== TEST SUPPORT METHODS ==========
    public static List<Message> getSentMessages() { return sentMessages; }
    public static List<Message> getStoredMessages() { return storedMessages; }
    public static List<Message> getDisregardedMessages() { return disregardedMessages; }
    public static Message searchStoredById(int id) {
        return storedMessages.stream().filter(m -> m.getId() == id).findFirst().orElse(null);
    }
    public static List<Message> searchStoredByRecipient(String recipient) {
        return storedMessages.stream().filter(m -> m.getRecipient().equals(recipient)).collect(Collectors.toList());
    }
    public static boolean deleteStoredByHash(String hash) {
        boolean removed = storedMessages.removeIf(m -> m.getHash().equals(hash));
        if (removed) {
            messageHashes.clear(); messageIds.clear();
            for (Message m : storedMessages) { messageHashes.add(m.getHash()); messageIds.add(m.getId()); }
            saveStoredMessages();
        }
        return removed;
    }
    public static List<Message> getAllTestMessages() {
        List<Message> all = new ArrayList<>();
        for (Message m : sentMessages) {
            if (m.getMessage().equals("Did you get the cake?") || m.getMessage().equals("It is dinner time !"))
                all.add(m);
        }
        for (Message m : storedMessages) {
            if (m.getMessage().equals("Where are you? You are late! I have asked you to be on time.")) {
                all.add(m);
                break;
            }
        }
        for (Message m : disregardedMessages) {
            if (m.getMessage().equals("Yohoooo, I am at your gate.")) {
                all.add(m);
                break;
            }
        }
        return all;
    }
    public static void resetForTesting() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIds.clear();
        nextId = 1;
        sentCount = 0;
        login = null;
        preloadTestData();
    }
}