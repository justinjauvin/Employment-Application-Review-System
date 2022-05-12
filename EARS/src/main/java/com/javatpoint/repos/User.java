package com.javatpoint.repos;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "user")
public class User {
	@Id
	private ObjectId id;
	private String username;
	@Field
	private String password;
	private String email;
	private String roles;
	private String admin;
	private String firstName;
	private String lastName;
	private String committee;
	private String salt;
	@Field
	private Instant lastLogin;
	@Field
	private Instant currentLogin;


	
	public User(String username, String roles,
				String admin, String firstName, String lastName, String email, String committee) throws NoSuchAlgorithmException, NoSuchProviderException {
		this.id = new ObjectId();
		this.username = username;
		this.salt = getSalt();
		this.roles = roles;
		this.admin = admin;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.committee = committee;


		updateLoginTime();
		if(admin==null) {
			admin="0";
		}
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCommittee() { return committee; };
	public void setCommittee(String committee) {this.committee = committee;}

	public void setPassword(String password) throws NoSuchAlgorithmException, NoSuchProviderException {
		try{
			this.password = passwordHash(password);
		} catch(NoSuchAlgorithmException e){
			System.out.println(e);
		} catch (NoSuchProviderException f) {
			System.out.println(f);
		}
	}
	public boolean getPassword(String password) throws NoSuchAlgorithmException, NoSuchProviderException {
		return passwordHash(password).equals(this.password);
	}


	public String getHashedPassword(){
		return this.password;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	public String getEmail() {  
		return email;  
	} 
	
	public void setEmail(String email) {  
		this.email = email;  
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + ", roles="
				+ roles + ", admin=" + admin + ", firstName=" + firstName + ", lastName=" + lastName + ", salt=" + salt + "]";
	}

	public List<String> getRoleList() {
		if (this.roles.length() > 0) {
			return Arrays.asList(this.roles.split(","));
		}
		return new ArrayList<>();
	}

	public boolean getAdmin() {
		return this.getRoles().equals("admin");
	}

	public boolean canHire() { return this.roles.equals("chair");}

	public void setAdmin(String admin) {
		this.admin = admin;
	}
	
	public boolean checkIfProfileChanged() {
		if(username == null && password == null && email == null && roles == null &&firstName == null && lastName == null) {
			return false;
		}
		else if(username.equals("") && password.equals("") &&  email.equals("") && roles.equals("") && firstName.equals("") && lastName.equals("")) {
			return false;
		}
		
		return true;
	}
	
	public boolean checkIfAllFieldsFilled() {
		if(username == null || username.length()<=0 || password == null || password.length()<=0 || email == null || email.length()<=0  || roles == null || roles.length()<=0  || firstName == null  || firstName.length()<=0 || lastName == null || lastName.length()<=0) {
			return false;
		}

		return true;
	}

	public void updateLoginTime() {
		if(lastLogin == null){
			lastLogin = Instant.now();
		} else{
			this.lastLogin = currentLogin;
		}
		currentLogin = Instant.now();
	}

	public Instant getLastLogin() {
		return this.lastLogin;
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
		String securePassword = getSecurePassword(password, this.salt);
		return securePassword;
	}
}