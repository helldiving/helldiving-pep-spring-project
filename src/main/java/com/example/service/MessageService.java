package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountRepository accountRepository;
    
    // if message_text not blank, < 255 char, posted_by refers to an existing user
    public Message createMessage(Message message) {

        if (message.getMessageText().isBlank() || message.getMessageText().length() > 255 || message.getMessageText() == null) {

            return null;
        }

        if (accountRepository.existsById(message.getPostedBy())) {
            return messageRepository.save(message);
        }

        return null;
    }

    // list of all messages
    public List<Message> getAllMessages() {

        return messageRepository.findAll();
    }

    // get message with message_id
    public Message getMessageById(Integer messageId) {

        return messageRepository.findById(messageId).orElse(null);
    }

    // delete should remove an existing message from database 
    public Message deleteMessageById(Integer messageId) {

        Message message = messageRepository.findById(messageId).orElse(null);

        if (message != null) {
            messageRepository.deleteById(messageId);
        }

        return message;
    }
    
    // message id exists, message_text not blank, < 255 char
    public Message updatemessageById(Integer messageId, String messageText) {

        Message message = messageRepository.findById(messageId).orElse(null);

        if (messageText.isBlank() || messageText.length() > 255) {
            return null;
        }

        if (message != null) {
            message.setMessageText(messageText);
            messageRepository.save(message);
            return message;
        }

        return null;
    }

    // all messages from an accountId
    public List<Message> getAllMessagesByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }

}
