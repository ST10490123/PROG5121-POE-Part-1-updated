/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment_1.main;

/**
 *
 * @author Student
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Message {
    private int id;
    private String sender;
    private String recipient;
    private String message;
    private String flag;       
    private String hash;      
    
    //Message Array
    public Message(int id, String sender, String recipient, String message, String flag) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.flag = flag;
        this.hash = generateHash();
    }
    
    //Generate Hash
    private String generateHash() {
        String raw = sender + recipient + message + flag + System.currentTimeMillis();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(raw.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : digest) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            return Integer.toHexString(raw.hashCode());
        }
    }

    // Getters
    public int getId() { return id; }
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public String getFlag() { return flag; }
    public String getHash() { return hash; }

    @Override
    public String toString() {
        return String.format("ID:%d | %s -> %s | %s | [%s] | hash:%.8s...",
                id, sender, recipient, message, flag, hash);
    }

    public String fullReport() {
        return String.format("ID: %d\nHash: %s\nSender: %s\nRecipient: %s\nMessage: %s\nFlag: %s\n",
                id, hash, sender, recipient, message, flag);
    }
}