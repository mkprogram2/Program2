package com.program2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.program2.table.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, String>
{
	@Query("select name from Person")
	public List<String> findAllName();
	
	
	public Person findById(String id);
	
	public List<Person> findAllByOrderByIdAsc();
	public Person findByEmailAndPersondetailSerialkey(String email, String serial_key);
	
	
}
