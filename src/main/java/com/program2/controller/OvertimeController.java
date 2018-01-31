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

import com.program2.table.Overtime;
import com.program2.repository.OvertimeRepository;

@RestController
@RequestMapping(value = "/overtimes")
public class OvertimeController
{

	@Autowired
	private OvertimeRepository OvertimeRepository;
	
	@PostMapping
	public Overtime SaveOvertime(@RequestBody Overtime Overtime)
	{
		return OvertimeRepository.save(Overtime);
	}
	
	@PutMapping
	public Overtime UpdateOvertime(@RequestBody Overtime Overtime)
	{
		return OvertimeRepository.save(Overtime);
	}
	
	@GetMapping("/{personid}")
	public List<Overtime> GetOvertimeByPersonid (@PathVariable("personid") String personid) 
	{
		return  OvertimeRepository.findByPersonidOrderByDateDesc(personid);
	}
	
	@DeleteMapping("/{id}")
	public void DeleteOvertime(@RequestBody int id)
	{
		OvertimeRepository.delete(id);
	}
}
