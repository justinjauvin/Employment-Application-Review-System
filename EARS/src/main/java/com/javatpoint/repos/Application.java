package com.javatpoint.repos;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "application")
public class Application {
	@Id
	private ObjectId id;
	public String firstName;
	String lastName;
	String role;
	String committee;
	String startDate;
	String endDate;
	String email;
	String work;
	String education;
	String skills;
	String appStatus = "In progress";
	public boolean flagged = false;
	int totalComments;
	int newComments;

	
	public Application(String firstName, String lastName, String role, String committee, String startDate, String endDate,
					   String email, String work, String education, String skills) {
		this.id = new ObjectId();
		this.role = role;
		this.flagged = flagged;
		this.firstName = firstName;
		this.lastName = lastName;
		this.committee = committee;
		this.startDate = startDate;
		this.endDate = endDate;
		this.email = email;
		this.work = work;
		this.education = education;
		this.skills = skills;

	}

	public ObjectId getid() {
		return id;
	}

	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}

	public void setTotalComments(int totalComments){ this.totalComments = totalComments; }
	public void setNewComments(int newComments){ this.newComments = newComments;}
	public int getTotalComments() {return this.totalComments;}
	public int getNewComments(){return this.newComments;}


	public boolean getFlagged() {
		return flagged;
	}


	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return this.firstName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return this.lastName;
	}

	public void setWork(String work){
		this.work = work;
	}

	public String getWork(){
		return work;
	}

	public void setEducation(String education){
		this.education = education;
	}

	public String getEducation(){
		return education;
	}

	public void setSkills(String skills){
		this.skills = skills;
	}

	public String getSkills(){
		return skills;
	}

	public String getAppStatus(){
		return appStatus;
	}

	public String getStartDate(){ return startDate; }

	public String getEndDate() { return endDate; }

	public String getCommittee() { return committee; }

	public String getEmail() { return email; }

	@Override
	public String toString() {
		return "Application: role=" + role + ", flagged=" + flagged	+ "]";
	}

	public void setAppStatus(String status) {
		this.appStatus = status;
	}
}