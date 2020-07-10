package com.goodcompany.salamport.rest_controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.UUID;

@RestController
public class UserController {

    @PostMapping("user/login")
    public boolean loginUser(String email){

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("salam.port.plus@gmail.com", "ServerPass");
                    }
                });

        try {
            // отправляем владельцу на электронную почту письмо
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("restaurantrent0@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Подтверждение электронной почты");
            // генерируем случайный токен
            String token = UUID.randomUUID().toString();
            message.setText("Добро пожаловать!" +
                    "\n\n Чтобы подтвердить адрес электронной почты, перейдите по ссылке https://restaurant-rent-server.herokuapp.com/account/confirm/" + token);
            Transport.send(message);

            return true;
        } catch (MessagingException e) {
            return false;
        }

    }
}
