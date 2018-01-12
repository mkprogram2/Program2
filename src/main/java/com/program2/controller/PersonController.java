package com.program2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.program2.table.Person;
import com.program2.repository.PersonRepository;

@RestController
@RequestMapping(value = "/persons")
public class PersonController 
{
	@Autowired
	private PersonRepository PersonRepository;
	@PostMapping
	public Person savePerson(@RequestBody Person person)
	{
		return PersonRepository.save(person);
	}
	
	@GetMapping
	public List<Person> getPersons()
	{
		return PersonRepository.findAllByOrderByIdAsc();
	}
	
	@PostMapping("/login")
	public Person login(@RequestBody Person in)
	{
		System.out.println(in.name);
		System.out.println(in.password);
		Person Login = PersonRepository.findByNameAndPassword(in.name, in.password);
		return Login;
	}
	
	@GetMapping("/name")
	public List<String> getPersonNames()
	{
		
		return PersonRepository.findAllName();
	}
	
	@GetMapping("/{id}")
	public Person getPerson(@PathVariable("id") String id)
	{
		return PersonRepository.findById(id);
	}
	
	@PutMapping
	public Person updatePerson(@RequestBody Person person)
	{
		PersonRepository.save(person);
		return PersonRepository.findById(person.id);
	}
	
	@DeleteMapping("/{id}")
	public void deletePerson(@PathVariable("id") String id)
	{
		PersonRepository.delete(id);
	}
}

//class Login 
//{
//	public String name;
//	public String password;
//}
