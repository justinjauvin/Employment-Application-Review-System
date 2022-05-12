package com.javatpoint.repos;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
	//public Application findByUsername(String username);
	public Comment findByid(ObjectId id);
	
	public List<Comment> findByApplicationId(ObjectId applicationId);
}