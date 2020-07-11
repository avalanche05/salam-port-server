package com.goodcompany.salamport.rest_controllers;

import com.goodcompany.salamport.models.Message;
import com.goodcompany.salamport.models.User;
import com.goodcompany.salamport.repository.MessageRepository;
import com.goodcompany.salamport.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("message/send")
    public void sendMessage(@RequestBody Message message) {
        messageRepository.save(message);
    }

    @PostMapping("message/all")
    public ArrayList<User> allMessage(@RequestParam long idUser) {
        ArrayList<User> users = new ArrayList<>();
        users.addAll(userRepository.findAllById(idUser));
        for (Message temp : messageRepository.findAllByTo(idUser)){
            users.add(userRepository.findById(temp.getFrom()).get());
        }
        return users;
    }

    @PostMapping("message/get")
    public ArrayList<Message> getMessage(@RequestParam long from, @RequestParam long to){
        return messageRepository.findAllByFromAndTo(from,to);
    }
}
