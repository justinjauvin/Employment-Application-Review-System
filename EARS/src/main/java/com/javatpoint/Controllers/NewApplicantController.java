package com.javatpoint.Controllers;

import com.javatpoint.repos.Application;
import com.javatpoint.repos.ApplicationRepository;
import com.javatpoint.repos.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class NewApplicantController {
    ApplicationRepository applicationRepository;
    User loggedInUser;

    public NewApplicantController(ApplicationRepository applicationRepository){
        this.applicationRepository = applicationRepository;
        loggedInUser = UtilityMethods.getLoggedInUser();
    }
    //Initial page load
    @RequestMapping(value = "/newapplication")
        public ModelAndView newApplication(){

            ModelAndView modelAndView = new ModelAndView();
            loggedInUser = UtilityMethods.getLoggedInUser();
            modelAndView.addObject("user", loggedInUser);
            return modelAndView;
        }

        //get text from all inputs and make an application object
    @RequestMapping(value = "/newapplication", method = RequestMethod.POST)
        public ModelAndView getData(@RequestParam(value = "fname", required = false) String fName,
                            @RequestParam(value = "lname", required = false) String lName,
                            @RequestParam(value = "role", required = false) String role,
                            @RequestParam(value = "education", required = false) String education,
                            @RequestParam(value = "work", required = false) String work,
                            @RequestParam(value = "skills", required = false) String skills,
                            @RequestParam(value = "committee", required = false) String committee,
                            @RequestParam(value = "startDate", required = false) String startDate,
                            @RequestParam(value = "endDate", required = false) String endDate,
                            @RequestParam(value = "email", required = false) String email){
        ModelAndView modelAndView = new ModelAndView("redirect:/newapplication");
        loggedInUser = UtilityMethods.getLoggedInUser();
        modelAndView.addObject("user", loggedInUser);
        applicationRepository.save(new Application(fName, lName, role, committee, startDate, endDate, email, work, education, skills));
        loggedInUser = UtilityMethods.getLoggedInUser();

        return modelAndView;
    }


}
