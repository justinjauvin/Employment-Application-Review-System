package com.javatpoint.Controllers;

import com.javatpoint.repos.*;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Controller



public class AdminHomeController {
    UserRepository userRepository;
    ApplicationRepository applicationRepository;
    List<User> users;
    List<Application> applications;
    User loggedInUser;

    public AdminHomeController(UserRepository userRepository, ApplicationRepository applicationRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;

        users = userRepository.findAll();
        applications = applicationRepository.findAll();
        loggedInUser = UtilityMethods.getLoggedInUser();
    }

    @RequestMapping(value = "/adminHome")
    public ModelAndView adminHome(@ModelAttribute User user,
                                  @RequestParam(value = "email", required=false) String email,
                                  @RequestParam(value = "search", required=false) String search,
                                  @RequestParam(value = "searchFilter", required=false) String searchFilter,
                                  @RequestParam(value = "sortVal", required = false) String sortVal) {
        ModelAndView modelAndView = new ModelAndView();

        User loggedInUser = UtilityMethods.getLoggedInUser();

        users = userRepository.findAll();
        System.out.println(email);

        // send new user email if admin - needs to move to admin home page email here
        if(email != null && email.length()>0) {
            System.out.println("Sent email...");
            UtilityMethods.sendEmail(email, "newAccountLink");
            System.out.println("Done");
        }

        if(sortVal != null) {
            switch (sortVal) {
                case "fNameAsc":
                    System.out.println("firstnameasc");
                    users = userRepository.findAllByOrderByFirstNameAsc();
                    break;
                case "fNameDesc":
                    users = userRepository.findAllByOrderByFirstNameDesc();
                    break;
                case "lNameAsc":
                    users = userRepository.findAllByOrderByLastNameAsc();
                    break;
                case "lNameDesc":
                    users = userRepository.findAllByOrderByLastNameDesc();
                    break;
                case "emailAsc":
                    users = userRepository.findAllByOrderByEmailAsc();
                    break;
                case "emailDesc":
                    users = userRepository.findAllByOrderByEmailDesc();
                    break;
                default:
            }
        }

        //if there's text in the search bar, call the search method, add the users list to the html
        if(search != null && search.length()>0) {
            users = newSearch(search, searchFilter);
        }

        modelAndView.addObject("users", users);
        modelAndView.addObject("user", loggedInUser);
        modelAndView.setViewName("adminHome");
        return modelAndView;
    }

    @RequestMapping(value = "/adminHome", method= RequestMethod.POST)
    public ModelAndView updateAccounts(@ModelAttribute User user,
                                       @RequestParam(value="action", required = false) String action,
                                       @RequestParam(value="delete", required = false) String delete){
        ModelAndView modelAndView = new ModelAndView("redirect:/adminHome");
        User loggedInUser = UtilityMethods.getLoggedInUser();
        modelAndView.addObject("user", loggedInUser);

        if(action != null){
            String[] permissionsTask = action.split(",");
            ObjectId userId = new ObjectId(permissionsTask[1]);
            User toChange = userRepository.findByid(userId);
            toChange.setRoles(permissionsTask[0]);
            userRepository.save(toChange);
        }

        if(delete != null) {
            String[] deleteTask = delete.split(",");
            ObjectId deleteId = new ObjectId(deleteTask[1]);
            User toDelete = userRepository.findByid(deleteId);
            userRepository.delete(toDelete);
        }






        return modelAndView;
    }

    private List<User> newSearch(String search, String filter) {
        List<User> result = userRepository.findAll();
        Iterator<User> it = result.iterator();

        switch(filter){
            case "email":
                while(it.hasNext()) {
                    User user = it.next();
                    if(!user.getEmail().equalsIgnoreCase(search)) {
                        it.remove();
                    }
                }
                break;
            case "firstName":
                while(it.hasNext()) {
                    User user = it.next();
                    if(!user.getFirstName().equalsIgnoreCase(search)) {
                        it.remove();
                    }
                }
                break;
            case "lastName":
                while(it.hasNext()) {
                    User user = it.next();
                    if(!user.getLastName().equalsIgnoreCase(search)) {
                        it.remove();
                    }
                }
                break;
        }

        return result;
    }
}
