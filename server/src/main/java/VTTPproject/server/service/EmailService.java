package VTTPproject.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String myEmail;

    @Autowired
    private JavaMailSender mailSender;

    public void welcomeEmail(String newEmail, String name){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("VTTP Team <" + myEmail + ">");
        message.setTo(newEmail);
        message.setSubject("Welcome to the stock thingy!"); //***************** update this subject
        message.setText("Hi " + name + ",\n\nYour account has been successfully created. Welcome aboard!");

        try{
            mailSender.send(message);
        }catch(Exception e){
            System.out.println("failed to send email...");
        }


    }



}
