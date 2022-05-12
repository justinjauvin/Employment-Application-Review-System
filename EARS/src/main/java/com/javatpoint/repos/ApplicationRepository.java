package com.javatpoint.repos;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
	public Application findByid(ObjectId id);
	public List<Application> findAllByRole(String role);
	public List<Application> findAllByEmail(String email);
	public List<Application> findAllByFirstName(String firstName);
	public List<Application> findAllByLastName(String lName);
	public List<Application> findAllByCommittee(String committee);
	public List<Application> findAllByStartDate(String startDate);
	public List<Application> findAllByEndDate(String endDate);

	//sorting methods
	public List<Application> findAllByOrderByFirstNameAsc();
	public List<Application> findAllByOrderByFirstNameDesc();
	public List<Application> findAllByOrderByLastNameAsc();
	public List<Application> findAllByOrderByLastNameDesc();
	public List<Application> findAllByOrderByRoleAsc();
	public List<Application> findAllByOrderByRoleDesc();
	public List<Application> findAllByOrderByEmailAsc();
	public List<Application> findAllByOrderByEmailDesc();
	public List<Application> findAllByOrderByCommitteeAsc();
	public List<Application> findAllByOrderByCommitteeDesc();
	public List<Application> findAllByOrderByStartDateAsc();
	public List<Application> findAllByOrderByStartDateDesc();
	public List<Application> findAllByOrderByEndDateAsc();
	public List<Application> findAllByOrderByTotalCommentsAsc();
	public List<Application> findAllByOrderByTotalCommentsDesc();
	public List<Application> findAllByOrderByNewCommentsAsc();
	public List<Application> findAllByOrderByNewCommentsDesc();

	public List<Application> findAllByOrderByEndDateDesc();
}