package com.javatpoint.repos;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	@Query
	User findByUsername(String username);
	public List<User> findAllByUsername(String username);
	public User findByPassword(String password);
	public User findByid(ObjectId id);
	public User findByEmail(String email);
	public List<User> findAllByRoles(String roles);
	public List<User> findAllByEmail(String email);
	public List<User> findAllByFirstName(String FirstName);
	public List<User> findAllByLastName(String LastName);
	public List<User> findAllByCommittee(String committee);



	public List<User> findAllByOrderByEmailAsc();

	public List<User> findAllByOrderByEmailDesc();

	public List<User> findAllByOrderByFirstNameAsc();

	public List<User> findAllByOrderByFirstNameDesc();

	public List<User> findAllByOrderByLastNameAsc();

	public List<User> findAllByOrderByLastNameDesc();

//	public List<User> findAllBystartDate(String startDate);
//	public List<User> findAllByendDate(String endDate);
	
	//below is an example of a db search to return all matching strings in a list
	//public List<User> findByPassword(String password);	
}