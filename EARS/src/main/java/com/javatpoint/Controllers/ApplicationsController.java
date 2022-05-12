package com.javatpoint.Controllers;

import com.javatpoint.repos.ApplicationRepository;
import com.javatpoint.repos.CommentRepository;
import com.javatpoint.repos.User;
import com.javatpoint.repos.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ApplicationsController {

    UserRepository userRepository;
    ApplicationRepository applicationRepository;
    CommentRepository commentRepository;
    User loggedInUser;

    //must initialize repositories
    public ApplicationsController(UserRepository userRepository, ApplicationRepository applicationRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.commentRepository = commentRepository;
    }

    @RequestMapping("/applications")
    public ModelAndView applications(@ModelAttribute User user){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);

        if(loggedInUser != null) {
            System.out.println(loggedInUser.getUsername() + " here");
            modelAndView.addObject("user", loggedInUser);
        }

        modelAndView.addObject("users", userRepository.findAll());
        modelAndView.addObject("applications", applicationRepository.findAll());

        modelAndView.setViewName("applications");

        return modelAndView;
    }
}
