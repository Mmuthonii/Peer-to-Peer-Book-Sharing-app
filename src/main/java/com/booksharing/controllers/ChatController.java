package com.booksharing.controllers;

import com.booksharing.models.ChatMessage;
import com.booksharing.utils.JsonStorage;
import com.google.gson.reflect.TypeToken;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController // ✅ Ensure this is a REST controller
@RequestMapping("/chat") // ✅ Ensures all chat-related endpoints start with /chat
public class ChatController {
    private final JsonStorage<ChatMessage> jsonStorage;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(JsonStorage<ChatMessage> jsonStorage, SimpMessagingTemplate messagingTemplate) {
        this.jsonStorage = jsonStorage;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        message.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        // ✅ Ensure sender name is correct
        if (message.getSender().equals("You")) {
            System.out.println("⚠️ Fixing sender name from 'You' to actual username.");
            message.setSender(getLoggedInUsername()); // Replace with actual session retrieval logic
        }

        Type listType = new TypeToken<List<ChatMessage>>() {}.getType();
        List<ChatMessage> messages = jsonStorage.loadData(listType);
        messages.add(message);
        jsonStorage.saveData(messages);

        System.out.println("✅ Message saved: " + message);

        // ✅ Notify both sender and recipient
        messagingTemplate.convertAndSendToUser(message.getRecipient(), "/queue/messages", message);
        messagingTemplate.convertAndSendToUser(message.getSender(), "/queue/messages", message);

        return message;
    }

    // ✅ Helper method to get the logged-in user's username (Modify based on your session logic)
    private String getLoggedInUsername() {
        return "Nakai"; // Replace this with session retrieval logic
    }

    @GetMapping("/history")
    public List<ChatMessage> getChatHistory(@RequestParam String user1, @RequestParam String user2) {
        System.out.println("🔎 Retrieving chat history for: " + user1 + " and " + user2);

        Type listType = new TypeToken<List<ChatMessage>>() {}.getType();
        List<ChatMessage> messages = jsonStorage.loadData(listType);

        List<ChatMessage> fixedMessages = messages.stream()
                .peek(msg -> {
                    if (msg.getSender().equals("You")) {
                        msg.setSender(user1);
                    }
                })
                .collect(Collectors.toList());

        List<ChatMessage> filteredMessages = fixedMessages.stream()
                .filter(msg ->
                        (msg.getSender().equals(user1) && msg.getRecipient().equals(user2)) ||
                                (msg.getSender().equals(user2) && msg.getRecipient().equals(user1))
                )
                .collect(Collectors.toList());

        System.out.println("✅ Filtered Messages for " + user1 + " and " + user2 + ": " + filteredMessages.size());

        return filteredMessages;
    }

    @GetMapping("/active")
    public List<String> getActiveChats(@RequestParam String username) {
        System.out.println("🔎 Retrieving active chats for: " + username);

        Type listType = new TypeToken<List<ChatMessage>>() {}.getType();
        List<ChatMessage> messages = jsonStorage.loadData(listType);

        Set<String> chatPartners = messages.stream()
                .filter(msg -> msg.getSender().equals(username) || msg.getRecipient().equals(username))
                .map(msg -> msg.getSender().equals(username) ? msg.getRecipient() : msg.getSender())
                .collect(Collectors.toSet());

        System.out.println("✅ Active Chats for " + username + ": " + chatPartners);

        return new ArrayList<>(chatPartners);
    }
}
