package com.program2.controller;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.program2.service.WorkhourService;
import com.program2.repository.WorkhourRepository;
import com.program2.table.Workhour;



@RestController
@RequestMapping(value = "/workhours")
public class WorkhourController {
	
	@Autowired
	private WorkhourService WorkhourService;
	@Autowired
	private WorkhourRepository WorkhourRepository;
	
	
	@PostMapping
	public String saveWorkhour(@RequestBody Workhour wh)
	{
		WorkhourRepository.save(wh);
		return "sfd";
	}
	
	@GetMapping
	public List<Workhour> getWorkhours()
	{
		return WorkhourRepository.findAll();
	}

	@GetMapping("/{month}/{year}/{id}")
	public List<Workhour> getWorkhoursByMonthByYear(@PathVariable("month") double month, @PathVariable("year") double year, @PathVariable("id") String id)
	{
		return WorkhourRepository.findAllByMonthByYearById(month, year,id);
	}
	
	@PostMapping("/checkin")
	public int Checkin(@RequestBody String personid)
	{
		return WorkhourService.Checkin(personid);
	}
	
//	@PutMapping("/checkout")
//	public int CheckoutUser(@RequestBody String personid)
//	{
//		return WorkhourService.CheckoutUser(personid);
//	}
	
	@GetMapping("/check/{id}")
	public Workhour Check(@PathVariable("id") String personid)
	{
		return WorkhourService.WorkhourByDateById(personid, new Date());
	}
}

//status = WorkhourRepository.tes(personid,new Timestamp(DateNow.getTime()), Long.toString(Interval));