package com.javatpoint.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LogoutController {

    @RequestMapping(value = "/logout")
    public ModelAndView logout() {
        ModelAndView modelAndView = new ModelAndView("redirect:/");

        UtilityMethods.setLoggedInUser(null);

        return modelAndView;
    }
}
