package com.goodcompany.salamport.rest_controllers;

import com.goodcompany.salamport.messages.ResponseMessage;
import com.goodcompany.salamport.models.ConfirmCode;
import com.goodcompany.salamport.models.FileInfo;
import com.goodcompany.salamport.models.User;
import com.goodcompany.salamport.repository.ConfirmCodeRepository;
import com.goodcompany.salamport.repository.UserRepository;
import com.goodcompany.salamport.services.FilesStorageService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    FilesStorageService storageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmCodeRepository confirmCodeRepository;

    @PostMapping("user/login")
    public boolean loginUser(@RequestParam String email) {

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
            message.setFrom(new InternetAddress("salam.port.plus@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Код для входа в приложение");
            // генерируем случайный токен
            int confirmCode;
            while (true) {
                confirmCode = ThreadLocalRandom.current().nextInt(100000, 999999 + 1);
                if (!confirmCodeRepository.existsByCode(confirmCode))
                    break;
            }

            confirmCodeRepository.save(new ConfirmCode(confirmCode, email));

            message.setText("Добро пожаловать!" +
                    "\n\n Код для входа в приложение: " + confirmCode);
            Transport.send(message);

            return true;
        } catch (MessagingException e) {
            return false;
        }

    }

    @PostMapping("user/code")
    public User userCode(@RequestParam int code) {
        ConfirmCode confirmCode = confirmCodeRepository.findByCode(code);

        if (confirmCode == null) {
            return null;
        }

        confirmCodeRepository.delete(confirmCode);

        User user = userRepository.findByEmail(confirmCode.getEmail());
        if (user == null) {
            return new User(confirmCode.getEmail());
        }
        return user;
    }

    @PostMapping("user/create")
    public User userCreate(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            storageService.save(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return message;
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return message;
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(UserController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


}
