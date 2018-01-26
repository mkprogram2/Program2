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

import com.program2.repository.HolidayRepository;
import com.program2.table.Holiday;

@RestController
@RequestMapping(value = "/holidays")
public class HolidayController {
	
	@Autowired
	private HolidayRepository HolidayRepository;
	
	@PostMapping
	public Holiday SaveHoliday(@RequestBody Holiday Holiday)
	{
		return HolidayRepository.save(Holiday);
	}
	
	@PutMapping
	public Holiday UpdateHoliday(@RequestBody Holiday Holiday)
	{
		return HolidayRepository.save(Holiday);
	}
	
	@GetMapping("/{month}/{year}")
	public List<Holiday> GetHoliday(@PathVariable("month") double month, @PathVariable("year") double year)
	{
		return HolidayRepository.findAllByMonthByYear(month, year);
	}
	
	@DeleteMapping
	public void DeleteHoliday(@RequestBody String id)
	{
		HolidayRepository.delete(id);
	}
	
}
