package com.javatpoint.Controllers;

import com.javatpoint.repos.ApplicationRepository;
import com.javatpoint.repos.CommentRepository;
import com.javatpoint.repos.User;
import com.javatpoint.repos.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Controller
public class ProfileController {
    UserRepository userRepository;
    ApplicationRepository applicationRepository;
    CommentRepository commentRepository;
    User loggedInUser;

    //must initialize repositories
    public ProfileController(UserRepository userRepository, ApplicationRepository applicationRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.commentRepository = commentRepository;
        loggedInUser = UtilityMethods.getLoggedInUser();
    }

    @RequestMapping(value = "/profile")
    public ModelAndView profile(@ModelAttribute User user,
                                @RequestParam(value = "confirmPassword", required=false) String confirmPassword,
                                @RequestParam(value = "oldPassword", required=false) String oldPassword,
                                @RequestParam(value = "newPassword", required = false) String newPassword) {
        ModelAndView modelAndView = new ModelAndView();
        loggedInUser = UtilityMethods.getLoggedInUser();
        modelAndView.addObject("user", loggedInUser);


        modelAndView.setViewName("profile");
        return modelAndView;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String handle(@ModelAttribute User user, BindingResult result,
                         RedirectAttributes redirectAttributes,
                         @RequestParam(value = "confirmPassword", required=false) String confirmPassword,
                         @RequestParam(value = "oldPassword", required=false) String oldPassword,
                         @RequestParam(value = "newPassword", required = false) String newPassword) throws NoSuchAlgorithmException, NoSuchProviderException {

        if(updateProfile(user, confirmPassword, oldPassword, newPassword)){
            redirectAttributes.addFlashAttribute("message", "Profile update successful!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Profile update failed");
        }

        return "redirect:/profile";
    }

    //method to update profile - needs to be fixed up after ID objects and password hashing
    boolean updateProfile(User user, String confirmPassword, String oldPassword, String newPassword) throws NoSuchAlgorithmException, NoSuchProviderException {
		boolean check = false;

        if(confirmPassword != null && oldPassword != null && newPassword != null){
        	if(!confirmPassword.equals("") && !oldPassword.equals("") && !newPassword.equals("")) {
	            if(loggedInUser.getPassword(oldPassword) && newPassword.equals(confirmPassword)){
	                loggedInUser.setPassword(newPassword);
	                check=true;
	            } else
	                return false;
        	}
        }


        if(loggedInUser == null){
            loggedInUser = UtilityMethods.getLoggedInUser();
        }
        if(user.getUsername() != null && user.getUsername().length()>0) {
            loggedInUser.setUsername(user.getUsername());
            check=true;
        }

        if(user.getEmail() != null && user.getEmail().length()>0) {
            loggedInUser.setEmail(user.getEmail());
            check=true;
        }

        if(user.getRoles() != null && user.getRoles().length()>0) {
            loggedInUser.setRoles(user.getRoles());
            check=true;
        }

        if(user.getFirstName() != null && user.getFirstName().length()>0) {
            loggedInUser.setFirstName(user.getFirstName());
            check=true;
        }

        if(user.getLastName() != null && user.getLastName().length()>0) {
            loggedInUser.setLastName(user.getLastName());
            check=true;
        }
        userRepository.save(loggedInUser);
        return check;
    }

}
