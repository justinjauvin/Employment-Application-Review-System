package com.javatpoint.repos;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Document(collection = "comment")
public class Comment {
	@Id
	private ObjectId id;
	private String comment;
	private ObjectId userId;
	private ObjectId applicationId;
	private String fname;
	private String lname;
	@Field
	private Instant postingTime;
	
	
	public Comment(String comment, ObjectId userId, ObjectId applicationId) {
		super();
		this.id = new ObjectId();
		this.comment = comment;
		this.userId = userId;
		this.applicationId = applicationId;
		setPostingTime();

	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ObjectId getUserId() {
		return userId;
	}

	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}

	public ObjectId getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(ObjectId applicationId) {
		this.applicationId = applicationId;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public void setPostingTime() {
		this.postingTime = Instant.now();
	}

	public Instant getPostingTime(){
		return this.postingTime;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", comment=" + comment + ", userId=" + userId + ", applicationId=" + applicationId
				+ "]";
	}

	public String getTime(){
		Date date = Date.from(this.postingTime);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd/MM/yyyy");
		String time = formatter.format(date);
		return time;
	}
}