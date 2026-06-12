/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package assignment_1.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

public class MessageAppTest {

    @BeforeEach
    void setUp() {
        MessageApp.resetForTesting();
    }

    // 1. Sent Messages array 
    @Test
    void testSentMessagesArrayCorrectlyPopulated() {
        List<Message> sent = MessageApp.getSentMessages();
        assertEquals(2, sent.size(), "There should be exactly 2 sent messages from test data 1-4");

        List<String> sentTexts = sent.stream().map(Message::getMessage).collect(Collectors.toList());
        assertTrue(sentTexts.contains("Did you get the cake?"));
        assertTrue(sentTexts.contains("It is dinner time !"));
    }

    // 2. Display the longest stored message 
    @Test
    void testLongestMessage() {
        List<Message> stored = MessageApp.getStoredMessages();
        int maxLen = stored.stream().mapToInt(m -> m.getMessage().length()).max().orElse(0);
        List<String> longest = stored.stream()
                .filter(m -> m.getMessage().length() == maxLen)
                .map(Message::getMessage)
                .collect(Collectors.toList());
        assertEquals(1, longest.size());
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                longest.get(0));
    }

    // 3. Search for messageID 
    @Test
    void testSearchByMessageId() {
        List<Message> stored = MessageApp.getStoredMessages();
        Message msg2 = stored.stream()
                .filter(m -> m.getMessage().contains("Where are you?"))
                .findFirst()
                .orElse(null);
        assertNotNull(msg2, "Message 2 not found in stored messages");

        Message found = MessageApp.searchStoredById(msg2.getId());
        assertNotNull(found);
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                found.getMessage());
    }

    // 4. Search all messages for recipient +27838884567 
    void testSearchByRecipient() {
        String recipient = "+27838884567";
        List<Message> matches = MessageApp.searchStoredByRecipient(recipient);
        assertEquals(2, matches.size());
        List<String> texts = matches.stream().map(Message::getMessage).collect(Collectors.toList());
        assertTrue(texts.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(texts.contains("Ok, I am leaving without you."));
    }

    // 5. Delete a message using a message hash 
    @Test
    void testDeleteByHash() {
        List<Message> storedBefore = MessageApp.getStoredMessages();
        Message msg2 = storedBefore.stream()
                .filter(m -> m.getMessage().contains("Where are you?"))
                .findFirst()
                .orElse(null);
        assertNotNull(msg2);
        String hash = msg2.getHash();

        boolean deleted = MessageApp.deleteStoredByHash(hash);
        assertTrue(deleted, "Message should be deleted successfully");

        List<Message> storedAfter = MessageApp.getStoredMessages();
        assertFalse(storedAfter.stream().anyMatch(m -> m.getHash().equals(hash)),
                "Message with hash should no longer exist");
    }
}