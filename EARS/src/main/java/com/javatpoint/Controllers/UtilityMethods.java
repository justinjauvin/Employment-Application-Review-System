package com.javatpoint.Controllers;

import com.javatpoint.repos.*;
import org.bson.types.ObjectId;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class UtilityMethods {
    static UserRepository userRepository;
    static ApplicationRepository applicationRepository;
    static CommentRepository commentRepository;
    static User loggedInUser;

    static void sendEmail(String email, String type) {
        System.out.println("Email sent...");
        final String username = "earsnoreply@gmail.com";
        final String password = "sctkygxldoukvlci";

        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("earsnoreply@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));

            if(type.equals("newAccountLink")) {
                message.setSubject("EARS New Account Creation");

                //need to switch this to new account link
                message.setText("Welcome friend, please click the following link to create an account!"
                        + "\nhttp://localhost:8080/newAccount");
            }
            else if(type.equals("changePassword")) {
                ObjectId userId = userRepository.findByEmail(email).getId();

                message.setSubject("EARS Forgot Password");

                //need to switch this to new account link
                message.setText("Please click the following link to change your password!"
                        + "\nhttp://localhost:8080/changepassword/" + userId);
            }

            Transport.send(message);


        } catch (SendFailedException e) {
            //error wrong email or no such email exists

        } catch (MessagingException e) {
            //something else went wrong
            e.printStackTrace();
        }
        System.out.println("Done!");
    }

    static ObjectId getRandomIdNumber(User user, Application application, Comment comment) {
        ObjectId randomId = new ObjectId();

        if(user != null) {
            if(userRepository.findByid(randomId) != null) {
                return getRandomIdNumber(user, application, comment);
            }
        }
        else if(application != null) {
            if(applicationRepository.findByid(randomId) != null) {
                return getRandomIdNumber(user, application, comment);
            }
        }
        else if(comment != null) {
            if(commentRepository.findByid(randomId) != null) {
                return getRandomIdNumber(user, application, comment);
            }
        }

        return randomId;
    }

    static User getUserByEmailOrUsername(String input) {
        if(userRepository.findByUsername(input) != null) {
            return userRepository.findByUsername(input);
        }

        if(userRepository.findByEmail(input) != null) {
            return userRepository.findByEmail(input);
        }

        return null;
    }

    static void setLoggedInUser(User user){
        loggedInUser = user;
    }

    static User getLoggedInUser(){
        return loggedInUser;
    }

}
