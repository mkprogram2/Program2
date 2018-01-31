package com.program2.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.program2.repository.WorkhourRepository;
import com.program2.repository.PersonRepository;
import com.program2.repository.RemunerationRepository;
import com.program2.table.Remuneration;
import com.program2.table.Workhour;
import com.program2.service.RemunerationService;

@RestController
@RequestMapping(value = "/remunerations")
public class RemunerationController {
	SimpleDateFormat DateFormat = new SimpleDateFormat("M-yyyy");
	SimpleDateFormat DateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	private WorkhourRepository WorkhourRepository;
	@Autowired
	private PersonRepository PersonRepository;
	@Autowired
	private RemunerationRepository RemunerationRepository;
	@Autowired
	private RemunerationService RemunerationService;
	
	@PostMapping
	public Remuneration saveRemun(@RequestBody Remuneration Remun)
	{
		return RemunerationRepository.save(Remun);
	}
	
	@GetMapping("/{id}/{month}/{year}")
	public	Remuneration GetRemuneration (@PathVariable("id") String id, @PathVariable("month") int month, @PathVariable("year") int year) 
	{
		return  RemunerationRepository.findByPersonIdAndMonthAndYear(id, month, year);
	}
	
	@GetMapping("/totalwim/{month}/{year}")
	public	int GetWorkhoursInMonth (@PathVariable("month") double month, @PathVariable("year") double year) 
	{
		return  RemunerationService.TotalWorkDaysInMonth(month,year);
	}
	
	@GetMapping("/totalwfy/{month}/{year}")
	public	int TotalWorkDaysFromNow (@PathVariable("month") double month, @PathVariable("year") double year) 
	{
		return RemunerationService.TotalWorkDaysFromNow(month, year);
	}
}
