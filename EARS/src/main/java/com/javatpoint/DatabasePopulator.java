package com.javatpoint;

import com.javatpoint.Controllers.UtilityMethods;
import com.javatpoint.repos.*;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class DatabasePopulator {
    UserRepository userRepository;
    ApplicationRepository applicationRepository;
    CommentRepository commentRepository;

    public DatabasePopulator(UserRepository userRepository, ApplicationRepository applicationRepository, CommentRepository commentRepository) throws NoSuchAlgorithmException, NoSuchProviderException {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.commentRepository = commentRepository;
    }

    public void populateDatabase() throws NoSuchAlgorithmException, NoSuchProviderException {

        UserRepository userRepo = this.userRepository;
        ApplicationRepository appRepo = this.applicationRepository;
        CommentRepository commentRepo = this.commentRepository;

        User a = new User( "admin",  "admin", "1", "Justin", "Jauvin", "jjauvin@algomau.ca", "COSC");
        a.setPassword("admin");
        userRepo.save(a);
        User b = new User( "drpatti",  "chair", "1", "Amandeep", "Patti", "amandeep.patti@algomau.ca", "COSC");
        b.setPassword("cosc3506");
        userRepo.save(b);
        User c = new User( "dlauzon", "admin", "1", "Dylan", "Lauzon", "dylauzon@algomau.ca", "MATH");
        c.setPassword("123Dylan");
        userRepo.save(c);
        User d = new User("megan", "chair", "0", "Megan", "Chamberlin", "meganchamberlin@hotmail.com", "COSC");
        d.setPassword("hellothere");
        userRepo.save(d);
        User e = new User( "tyler", "user", "0", "Tyler", "Martyn", "tmartyn@algomau.ca", "COSC");
        e.setPassword("ears");
        userRepo.save(e);

        String workLatin = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam";
        String eduLatin = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. ";
        String skillsLatin = "At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga.";
        Application app1 = new Application("John", "Doe", "TA COSC3506", "COSC", "05/05/22", "12/12/23", "johndoe@mail.com",workLatin,eduLatin,skillsLatin);
        Application app2 = new Application("Jane", "Johnson", "TA COSC3506", "COSC", "05/05/22", "12/12/23", "jane@mail.com",workLatin,eduLatin,skillsLatin);
        Application app3 = new Application("Craig", "Wright", "Math Club President", "MATH", "05/05/22", "12/12/23", "craig@mail.com",workLatin,eduLatin,skillsLatin);
        Application app4 = new Application("Mohammad", "Sala", "Professor COSC3406", "COSC", "04/25/22", "12/01/23", "mohammad@mail.com",workLatin,eduLatin,skillsLatin);
        Application app5 = new Application("Etienne", "Larivierre", "TA MATH2056", "MATH", "07/02/22", "09/12/22", "etienne@mail.com",workLatin,eduLatin,skillsLatin);
        Application app6 = new Application("Yifi", "Xu", "TA MATH2056", "MATH", "05/05/22", "12/12/23", "xuyifi@mail.com",workLatin,eduLatin,skillsLatin);

        appRepo.save(app1);
        appRepo.save(app2);
        appRepo.save(app3);
        appRepo.save(app4);
        appRepo.save(app5);
        appRepo.save(app6);

        Comment com1 = new Comment("Great candidate!", a.getId(), app1.getid());
        com1.setFname(a.getFirstName());
        com1.setLname(a.getLastName());
        Comment com2 = new Comment("Not bad!", b.getId(), app1.getid());
        com2.setFname(b.getFirstName());
        com2.setLname(b.getLastName());
        Comment com3 = new Comment("Needs more education.", c.getId(), app2.getid());
        com3.setFname(c.getFirstName());
        com3.setLname(c.getLastName());
        Comment com4 = new Comment("Are you sure?", d.getId(), app2.getid());
        com4.setFname(d.getFirstName());
        com4.setLname(d.getLastName());
        Comment com5 = new Comment("Absolutely!", c.getId(), app3.getid());
        com5.setFname(c.getFirstName());
        com5.setLname(c.getLastName());
        Comment com6 = new Comment("Let's keep looking...", e.getId(), app5.getid());
        com6.setFname(e.getFirstName());
        com6.setLname(e.getLastName());
        Comment com7 = new Comment("We should hire this one.", a.getId(), app3.getid());
        com7.setFname(a.getFirstName());
        com7.setLname(a.getLastName());

        commentRepo.save(com1);
        commentRepo.save(com2);
        commentRepo.save(com3);
        commentRepo.save(com4);
        commentRepo.save(com5);
        commentRepo.save(com6);
        commentRepo.save(com7);
    }

    public void clearDatabase(){
        UserRepository userRepo = this.userRepository;
        ApplicationRepository appRepo = this.applicationRepository;
        CommentRepository commentRepo = this.commentRepository;

        userRepo.deleteAll();
        appRepo.deleteAll();
        commentRepo.deleteAll();
    }

}
