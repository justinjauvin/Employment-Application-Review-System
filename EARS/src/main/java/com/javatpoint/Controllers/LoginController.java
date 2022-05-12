package com.javatpoint.Controllers;
import com.javatpoint.DatabasePopulator;
import com.javatpoint.repos.ApplicationRepository;
import com.javatpoint.repos.CommentRepository;
import com.javatpoint.repos.User;
import com.javatpoint.repos.UserRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller  
public class LoginController {
	UserRepository userRepository;
	ApplicationRepository applicationRepository;
	CommentRepository commentRepository;
	User loggedInUser;
	boolean success;

	//must initialize repositories
	public LoginController(UserRepository userRepository, ApplicationRepository applicationRepository, CommentRepository commentRepository) {
		this.userRepository = userRepository;
		this.applicationRepository = applicationRepository;
		this.commentRepository = commentRepository;
	}
	
	//must add loggedIn user check to make sure only logged in people can access the page  
	//check if findByUser(loggedinUser) != null on every view 
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();
		loggedInUser = null;
		System.out.println("test");
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView loginMethod(@ModelAttribute User user,
							  @RequestParam(value = "email", required=false) String email,
							  @RequestParam(value = "username", required = false) String username,
							  @RequestParam(value = "password", required = false) String password,
							  @RequestParam(value = "database", required = false) String database) throws NoSuchAlgorithmException, NoSuchProviderException {
		ModelAndView modelAndView = new ModelAndView();
		loggedInUser = null;
		User attemptUser;
		UtilityMethods.setLoggedInUser(null);
		DatabasePopulator db = new DatabasePopulator(userRepository,applicationRepository,commentRepository);
		System.out.println(database);
		if(database != null){
			if(database.equals("seed")){
				db.populateDatabase();
				String msg = "Database seeded.";
				modelAndView.addObject("message", msg);
				modelAndView.setViewName("login");
				modelAndView.addObject("user", loggedInUser);
				return modelAndView;
			} else if(database.equals("clear")){
				db.clearDatabase();
				String msg = "Database cleared.";
				modelAndView.addObject("message", msg);
				modelAndView.setViewName("login");
				modelAndView.addObject("user", loggedInUser);
				return modelAndView;
			}
		}


		//if no one is logged in
		if(loggedInUser == null) {
			if(userRepository.findByUsername(username)!=null) {
				//if username is found in db then put that info in a temp user to check for password next
				attemptUser = userRepository.findByUsername(username);

				//check password if good return
				if(attemptUser.getPassword(password)) {
					//loggedInUser will be the access to the logged in user
					loggedInUser = attemptUser;
					UtilityMethods.setLoggedInUser(loggedInUser);
					if(loggedInUser.getAdmin()){
						modelAndView = new ModelAndView("redirect:/adminHome");
					} else {
						modelAndView = new ModelAndView("redirect:/home");
					}

					modelAndView.addObject("user", loggedInUser);

					return modelAndView;
				}
				//else bad password
				else {
					String msg = "Username or password not found in our records.";
					modelAndView.addObject("message", msg);
					modelAndView.setViewName("login");
				}
			}//if username doesn't exist jump here
			else {
				String msg = "Username or password not found in our records.";
				modelAndView.addObject("message", msg);
				modelAndView.setViewName("login");
			}
		} //someone is logged in redirect to home
		else {
			UtilityMethods.setLoggedInUser(loggedInUser);
			if(loggedInUser.getAdmin()){
				modelAndView = new ModelAndView("redirect:/adminHome");
			} else {
				modelAndView = new ModelAndView("redirect:/home");
			}
			modelAndView.addObject("user", loggedInUser);
			return modelAndView;
		}
		return modelAndView;
	}


	
	//Add salt for MD5 hashing (you will need to store this salt value along with the hash)
	private static String getSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
		//Create an array for salt
		byte[] salt = new byte[16];
		//Get a random salt
		sr.nextBytes(salt);
		//Return salt
		return salt.toString();
	}
	
	//Creates the hash for the password
	private static String getSecurePassword(String passwordToHash, String salt) {
		String generatedPassword = null;
		try {
			//Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			//Add password bytes to digest
			md.update(salt.getBytes());
			//Get the hash's bytes
			byte[] bytes = md.digest(passwordToHash.getBytes());
			//Convert bytes[] into hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			//Get complete hashed password in hexadecimal format
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}
	
	//Call this to hash a password
	public String passwordHash(String password) throws NoSuchAlgorithmException, NoSuchProviderException {
		String salt = getSalt();  
        String securePassword = getSecurePassword(password, salt);
        return securePassword;
	}
}  