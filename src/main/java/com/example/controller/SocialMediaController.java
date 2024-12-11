package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 // I should be able to create a new Account on the endpoint POST localhost:8080/register. The body will contain a representation of a JSON Account, but will not contain an accountId.
 // I should be able to verify my login on the endpoint POST localhost:8080/login. The request body will contain a JSON representation of an Account.
 // I should be able to submit a new post on the endpoint POST localhost:8080/messages. The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain a messageId.
 // I should be able to submit a GET request on the endpoint GET localhost:8080/messages.
 // I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{messageId}.
 // I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{messageId}.
 // I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{messageId}. The request body should contain a new messageText values to replace the message identified by messageId. The request body can not be guaranteed to contain any other information.
 // I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{accountId}/messages.

 @RestController
public class SocialMediaController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;

    // response ok = 200, duplicate username = 409, other = 400
    @PostMapping("/register")
    public ResponseEntity<Account> createAccount(@RequestBody Account account){

            Account createdAccount = accountService.createAccount(account);
            if (createdAccount == null) {
                return ResponseEntity.status(400).header("content-type", "application/json").body(null);
            }

            if (createdAccount.getUsername() == null) {
                return ResponseEntity.status(409).header("content-type", "application/json").body(null);
            }
            return ResponseEntity.status(200).header("content-type", "application/json").body(createdAccount);
    }

    // response ok = 200, other = 401
    @PostMapping("/login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account account) {

        Account loggedInAccount = accountService.loginAccount(account);

        if (loggedInAccount == null) {
            return ResponseEntity.status(401).header("content-type", "application/json").body(null);
        }
        return ResponseEntity.status(200).header("content-type", "application/json").body(loggedInAccount);
    }

    // response ok = 200, other = 400
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {

        Message newMessage = messageService.createMessage(message);

        if (newMessage == null) {
            return ResponseEntity.status(400).header("content-type", "application/json").body(null);
        }

        return ResponseEntity.status(200).header("content-type", "application/json").body(newMessage);

    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{messageId}")
    public Message getMessageById(@PathVariable Integer messageId) {
        return messageService.getMessageById(messageId);
    }

    // 200, 200 but response body empty
    @DeleteMapping("/messages/{messageId}")
    public Integer deleteMessage(@PathVariable Integer messageId) {

        Message message = messageService.deleteMessageById(messageId);
        if (message == null) {
            return null;
        }

        return 1; // because 1 means one row was impacted apparently

    }

    // 200, other = 400
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, @RequestBody Message message) {
        Message updatedMessage = messageService.updatemessageById(messageId, message.getMessageText());

    if (updatedMessage == null) {
        return ResponseEntity.status(400).body(null);
    }

    return ResponseEntity.status(200).body(1);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getAllMessagesByAccount(@PathVariable Integer accountId) {
        return messageService.getAllMessagesByAccountId(accountId);
    }
}
