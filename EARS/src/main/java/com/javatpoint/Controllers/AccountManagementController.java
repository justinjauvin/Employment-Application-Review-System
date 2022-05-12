package com.javatpoint.Controllers;

import com.javatpoint.repos.ApplicationRepository;
import com.javatpoint.repos.CommentRepository;
import com.javatpoint.repos.User;
import com.javatpoint.repos.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Controller
public class AccountManagementController {
    UserRepository userRepository;
    ApplicationRepository applicationRepository;
    CommentRepository commentRepository;
    User loggedInUser;

    //must initialize repositories
    public AccountManagementController(UserRepository userRepository, ApplicationRepository applicationRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.commentRepository = commentRepository;
    }

    @RequestMapping("/create")
    public String login(@ModelAttribute User user, @RequestParam(value = "confirmPassword", required=false) String confirmPassword) throws NoSuchAlgorithmException, NoSuchProviderException {
        loggedInUser = null;
        //no duplicate username or email!!
        //need to check to make sure not same user or email in the db must check for that

        if (user != null && user.checkIfAllFieldsFilled() && user.getPassword(confirmPassword)){
            //set the admin value
            user.setId(UtilityMethods.getRandomIdNumber(user, null, null));
            userRepository.save(user);
            System.out.println("account created");
            user = null;
            return "login";
        }
        else {
            return "newAccount";
        }
    }

    @RequestMapping(value = "newAccount", method = RequestMethod.GET)
    public ModelAndView newAccount(@ModelAttribute User use){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("newAccount");
        return modelAndView;
    }
    //no duplicate username or email!!
    @RequestMapping(value = "/newAccount", method = RequestMethod.POST)
    public ModelAndView newAccount(@ModelAttribute User user,
                                   @RequestParam(value="username", required = false) String formUsername,
                                   @RequestParam(value="password", required = false) String formPassword,
                                   @RequestParam(value="confirmPassword", required = false) String confirmPassword,
                                   @RequestParam(value="email", required = false) String formEmail,
                                   @RequestParam(value="roles", required = false) String formRoles,
                                   @RequestParam(value="fname", required = false) String formFname,
                                   @RequestParam(value="lname", required = false) String formLname
                                   ) throws NoSuchAlgorithmException, NoSuchProviderException {
        ModelAndView modelAndView = new ModelAndView();
        List<User> userList = userRepository.findAll();
        boolean fail = false;
        String reason = "";

        for(User testUser : userList){
            if(testUser.getUsername().equalsIgnoreCase(formUsername)){
                fail = true;
                reason = "username";
            }
            if(testUser.getEmail().equalsIgnoreCase(formEmail)){
                fail = true;
                reason = "email";
            }
        }
        System.out.println(formFname);
        //check if passwords match, if they do make new account, if not return to newaccount page
        if(formPassword != null){
            if(!formPassword.equals(confirmPassword)){
                String msg = "Passwords don't match.";
                modelAndView.addObject("message", msg);
                modelAndView.setViewName("newAccount");
                return modelAndView;
            } else if (fail && reason.equals("username")){
                String msg = "Username already exists";
                modelAndView.addObject("message", msg);
                modelAndView.setViewName("newAccount");
                return modelAndView;
            } else if(fail && reason.equalsIgnoreCase("email")){
                String msg = "Email already in use.";
                modelAndView.addObject("message", msg);
                modelAndView.setViewName("newAccount");
                return modelAndView;
            }
            else {
                System.out.println("TEST");
                User newUser = new User(formUsername, formRoles, "0", formFname, formLname, formEmail,"");
                newUser.setPassword(formPassword);
                userRepository.save(newUser);
            }
        }

        //need to check to make sure not same user or email in the db must check for that
        System.out.println(user);

        modelAndView.setViewName("login");

        return modelAndView;
    }

    @RequestMapping("/forgotpassword")
    public ModelAndView forgotpassword(@RequestParam(value = "input", required=false) String input){
        ModelAndView modelAndView = new ModelAndView();

        System.out.println(input);
        
        if(userRepository == null) {
        	System.out.println(true);
        }

        if(input != null && input.length()>0) {
            if(getUserByEmailOrUsername(input) != null) {

                //send change password email
                sendEmail(getUserByEmailOrUsername(input).getEmail(), "changePassword");

                modelAndView.addObject("error", "false");
            }
            
            else {
                modelAndView.addObject("error", "true");
            }
        }

        modelAndView.setViewName("forgotpassword");

        return modelAndView;
    }

    @RequestMapping("/changepassword/{id}")
    public ModelAndView changepassword(@PathVariable("id")ObjectId id, @RequestParam(value = "newPassword", required=false) String newPassword, @RequestParam(value = "confirmPassword", required=false) String confirmPassword) throws NoSuchAlgorithmException, NoSuchProviderException {
        ModelAndView modelAndView = new ModelAndView();


//        modelAndView.addObject("id", Long.toString(id));

        System.out.println(id);
        //change password
        if(newPassword != null && newPassword.length()>0 && confirmPassword != null && confirmPassword.length()>0) {
            if(newPassword.equals(confirmPassword)) {
                User user = userRepository.findByid(id);
                user.setPassword(newPassword);
                userRepository.save(user);
                modelAndView = new ModelAndView("redirect:/");
            }
            else {
                //error do not match
                modelAndView.setViewName("changepassword");
            }
        }

        else {
            //error field incomplete
            modelAndView.setViewName("changepassword");
        }
        //make both params null after
        return modelAndView;
    }
    
   User getUserByEmailOrUsername(String input) {
        if(userRepository.findByUsername(input) != null) {
            return userRepository.findByUsername(input);
        }

        if(userRepository.findByEmail(input) != null) {
            return userRepository.findByEmail(input);
        }

        return null;
    }
   
   void sendEmail(String email, String type) {
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
}
