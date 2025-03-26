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
        message.setSubject("Welcome to Quick Reports!");
        message.setText(
            "Dear " + name + ",\n\n" +
            "Welcome to Quick Reports! We're thrilled to have you join our community. Your account has been successfully created, and we're excited to support you in exploring all that Quick Reports has to offer.\n\n" +
            "Feel free to dive in and start schoomzing numbers. If you have any questions or need assistance, our team is here to help—just reach out to us anytime.\n\n" +
            "Warm regards,\n" +
            "The VTTP Team"
        );
        try{
            mailSender.send(message);
        }catch(Exception e){
            System.out.println("failed to send email...");
        }


    }



}
