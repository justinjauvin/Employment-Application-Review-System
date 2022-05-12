package com.javatpoint;
import com.javatpoint.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainEARS implements CommandLineRunner{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ApplicationRepository applicationRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(MainEARS.class, args);		
	}
	
	@Override
	public void run(String... args) throws Exception {	}

}