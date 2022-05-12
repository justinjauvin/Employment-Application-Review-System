package com.javatpoint.Controllers;

import com.javatpoint.repos.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedList;
import java.util.List;

@Controller
public class ApplicationViewerController {

    UserRepository userRepository;
    ApplicationRepository applicationRepository;
    CommentRepository commentRepository;
    User loggedInUser;
    ObjectId appId;
    private MongoOperations mongoOperations;

    //must initialize repositories
    public ApplicationViewerController(UserRepository userRepository, ApplicationRepository applicationRepository, CommentRepository commentRepository, MongoOperations mongoOperations) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.commentRepository = commentRepository;
        this.mongoOperations = mongoOperations;
        loggedInUser = UtilityMethods.getLoggedInUser();
    }

    //GetMapping
    @RequestMapping(value = "/viewapplication/{id}", method = RequestMethod.GET)
    public ModelAndView viewapplications(@ModelAttribute User user, @PathVariable("id") ObjectId id){
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = UtilityMethods.getLoggedInUser();
        modelAndView.addObject("user", loggedInUser);
        //get and populate all comments for this application on load
        appId = id;
        Application app = applicationRepository.findByid(id);

        List<Comment> comments = new LinkedList<Comment>();

        comments = commentRepository.findByApplicationId(id);

        modelAndView.addObject("app", app);

        modelAndView.addObject("comments", comments);

        if(modelAndView.getViewName() == null) {
            modelAndView.setViewName("viewapplication");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/viewapplication/{id}", method = RequestMethod.POST)
    public ModelAndView viewerPost(@PathVariable("id") ObjectId id ,
                                   @RequestParam (value = "comment", required = false) String comment,
                                   @RequestParam (value = "action", required = false) String action){

        ObjectId thisId = id;
        if(loggedInUser == null){
            loggedInUser = UtilityMethods.getLoggedInUser();
        }
        //set first and last name of commenter
        if(comment != null){
            Comment newComment = new Comment(comment, loggedInUser.getId(), appId);
            newComment.setFname(loggedInUser.getFirstName());
            newComment.setLname(loggedInUser.getLastName());
            commentRepository.save(newComment);
        }

        Criteria c = Criteria.where("_id").is(thisId);
        Query query = new Query().addCriteria(c);

        System.out.println(mongoOperations.find(query, Application.class));

        Application app = applicationRepository.findByid(id);

        if(action != null){
            switch(action){
                case "flagged ":
                    if(!app.getFlagged()){
                        app.setFlagged(true);
                    } else {
                        app.setFlagged(false);
                    }
                    break;
                case "hired":
                    System.out.println("hiring");
                    app.setAppStatus("Hired");
                    break;
                case "rejected":
                    System.out.println("rejecting");
                    app.setAppStatus("Rejected");
                    break;
            }
        }

        applicationRepository.save(app);
        //back to initial application viewer page
        ModelAndView modelAndView = new ModelAndView("redirect:/viewapplication/{id}");
        return modelAndView;
    }

}
