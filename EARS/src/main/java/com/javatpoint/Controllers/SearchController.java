package com.javatpoint.Controllers;

import com.javatpoint.repos.*;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class SearchController {
    UserRepository userRepository;
    ApplicationRepository applicationRepository;
    CommentRepository commentRepository;
    User loggedInUser;

    //must initialize repositories
    public SearchController(UserRepository userRepository, ApplicationRepository applicationRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.commentRepository = commentRepository;
    }
    @RequestMapping("/search")
    public ModelAndView search(@ModelAttribute User user,
                               @RequestParam(value = "search", required=false) String search,
                               @RequestParam(value = "searchFilter", required=false) String searchFilter,
                               @RequestParam(value = "sortVal", required = false) String sortVal){
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = UtilityMethods.getLoggedInUser();
        modelAndView.addObject("user", loggedInUser);

        List<Application> result = applicationRepository.findAll();

        if(sortVal != null) {
            switch (sortVal) {
                case "fNameAsc":
                    result = applicationRepository.findAllByOrderByFirstNameAsc();
                    break;
                case "fNameDesc":
                    result = applicationRepository.findAllByOrderByFirstNameDesc();
                    break;
                case "lNameAsc":
                    result = applicationRepository.findAllByOrderByLastNameAsc();
                    break;
                case "lNameDesc":
                    result = applicationRepository.findAllByOrderByLastNameDesc();
                    break;
                case "roleAsc":
                    result = applicationRepository.findAllByOrderByRoleAsc();
                    break;
                case "roleDesc":
                    result = applicationRepository.findAllByOrderByRoleDesc();
                    break;
                case "committeeAsc":
                    result = applicationRepository.findAllByOrderByCommitteeAsc();
                    break;
                case "committeeDesc":
                    result = applicationRepository.findAllByOrderByCommitteeDesc();
                    break;
                case "startAsc":
                    result = applicationRepository.findAllByOrderByStartDateAsc();
                    break;
                case "startDesc":
                    result = applicationRepository.findAllByOrderByStartDateDesc();
                    break;
                case "endAsc":
                    result = applicationRepository.findAllByOrderByEndDateAsc();
                    break;
                case "endDesc":
                    result = applicationRepository.findAllByOrderByEndDateDesc();
                    break;
                default:
            }
        }

        //if there's text in the search bar, call the search method, add the applications list to the html
        if(search != null && search.length()>0) {
            result = newSearch(search, searchFilter);

            modelAndView.addObject("applications", result);
        } else { //revert to all applications -- TODO error message saying none found.
            modelAndView.addObject("applications", applicationRepository.findAll());
        }


        modelAndView.addObject("applications", result);
        modelAndView.setViewName("search");

        return modelAndView;
    }
    //search method to populate list with applications
    private List<Application> newSearch(String search, String filter) {
        List<Application> result = applicationRepository.findAll();
        Iterator<Application> it = result.iterator();

        System.out.println(filter);
        System.out.println(search);

        switch(filter){
            case "role":
                while(it.hasNext()) {
                    Application app = it.next();
                    if(!app.getRole().equalsIgnoreCase(search)) {
                        it.remove();
                    }
                }
                break;
            case "committee":
                while(it.hasNext()) {
                    Application app = it.next();
                    if(!app.getCommittee().equalsIgnoreCase(search)) {
                        it.remove();
                    }
                }
                break;
            case "startDate":
                while(it.hasNext()) {
                    Application app = it.next();
                    if(!app.getStartDate().equalsIgnoreCase(search)) {
                        it.remove();
                    }
                }
                break;
            case "endDate":
                while(it.hasNext()) {
                    Application app = it.next();
                    if(!app.getEndDate().equalsIgnoreCase(search)) {
                        it.remove();
                    }
                }
                break;
            case "email":
                while(it.hasNext()) {
                    Application app = it.next();
                    if(!app.getEmail().equalsIgnoreCase(search)) {
                        it.remove();
                    }
                }
                break;
            case "firstName":
                while(it.hasNext()) {
                    Application app = it.next();
                    if(!app.getFirstName().equalsIgnoreCase(search)) {
                        it.remove();
                    }
                }
                break;
            case "lastName":
                while(it.hasNext()) {
                    Application app = it.next();
                    if(!app.getLastName().equalsIgnoreCase(search)) {
                        it.remove();
                    }
                }
                break;
        }

        return result;
    }
}
