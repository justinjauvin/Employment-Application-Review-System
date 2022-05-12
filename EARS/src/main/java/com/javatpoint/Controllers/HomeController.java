package com.javatpoint.Controllers;

import com.javatpoint.repos.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HomeController {
    UserRepository userRepository;
    ApplicationRepository applicationRepository;
    CommentRepository commentRepository;
    User loggedInUser;
    List<Comment> comments;
    List<User> users;
    List<Application> applications;
    boolean success = false;

    //must initialize repositories
    public HomeController(UserRepository userRepository, ApplicationRepository applicationRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.commentRepository = commentRepository;
        comments = commentRepository.findAll();
        users = userRepository.findAll();
        applications = applicationRepository.findAll();
        loggedInUser = UtilityMethods.getLoggedInUser();
    }

    @RequestMapping(value = "/home")
    public ModelAndView home(@ModelAttribute User user,
                             @RequestParam(value = "email", required=false) String email,
                             @RequestParam(value="sortVal", required = false) String sortVal){
        ModelAndView modelAndView = new ModelAndView();
        user = UtilityMethods.getLoggedInUser();
        modelAndView.addObject("user", user);
        modelAndView.addObject("users", users);


        //find user that logged in to get data
        if(loggedInUser == null){
            loggedInUser = UtilityMethods.getLoggedInUser();
        }

        //load lists of all comments, users, and apps for tables
        comments = commentRepository.findAll();
        users = userRepository.findAll();
        applications = applicationRepository.findAll();



        //loop through applications and comment repos to count total comments per app, and total new comments per app.
        int totalComments = 0;
        int newComments = 0;

    if(sortVal != null){
        boolean newComment = false;
        switch(sortVal){
            case "fNameAsc":
                applications = applicationRepository.findAllByOrderByFirstNameAsc();
                break;
            case "fNameDesc":
                applications = applicationRepository.findAllByOrderByFirstNameDesc();
                break;
            case "lNameAsc":
                applications = applicationRepository.findAllByOrderByLastNameAsc();
                break;
            case "lNameDesc":
                applications = applicationRepository.findAllByOrderByLastNameDesc();
                break;
            case "roleAsc":
                applications = applicationRepository.findAllByOrderByRoleAsc();
                break;
            case "roleDesc":
                applications = applicationRepository.findAllByOrderByRoleDesc();
                break;
            case "committeeAsc":
                applications = applicationRepository.findAllByOrderByCommitteeAsc();
                break;
            case "committeeDesc":
                applications = applicationRepository.findAllByOrderByCommitteeDesc();
                break;
            case "startAsc":
                applications = applicationRepository.findAllByOrderByStartDateAsc();
                break;
            case "startDesc":
                applications = applicationRepository.findAllByOrderByStartDateDesc();
                break;
            case "endAsc":
                applications = applicationRepository.findAllByOrderByEndDateAsc();
                break;
            case "endDesc":
                applications = applicationRepository.findAllByOrderByEndDateDesc();
                break;
            case "commentsAsc":
                newComment = false;
                for(Application app: applicationRepository.findAll()) {
                    if (app.getNewComments() != 0) {
                        newComment = true;
                        break;
                    }
                }

                if(newComment){
                    applications = applicationRepository.findAllByOrderByNewCommentsAsc();
                } else {
                    applications = applicationRepository.findAllByOrderByTotalCommentsAsc();
                    newComment = false;
                }
                break;
            case "commentsDesc":
                newComment = false;
                for(Application app: applicationRepository.findAll()) {
                    if (app.getNewComments() != 0) {
                        newComment = true;
                        break;
                    }
                }

                if(newComment){
                    applications = applicationRepository.findAllByOrderByNewCommentsDesc();
                } else {
                    applications = applicationRepository.findAllByOrderByTotalCommentsDesc();
                    newComment = false;
                }
                break;
        }
    }

        for(Application app : applications){
            newComments = 0;
            totalComments = 0;
            for(Comment comment : comments){
                if(comment.getApplicationId().equals(app.getid())){
                    totalComments++;
                }
                if(comment.getApplicationId().equals(app.getid()) && comment.getPostingTime().compareTo(loggedInUser.getLastLogin()) > 0){
                    newComments++;
                }
            }
            app.setTotalComments(totalComments);
            app.setNewComments(newComments);
        }




        //send variables back to html for /home
        modelAndView.addObject("totalComments", totalComments);
        modelAndView.addObject("applications", applications);
        modelAndView.addObject("newComments", newComments);
        modelAndView.addObject("comments", comments);
        return modelAndView;
    }



}
