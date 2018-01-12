package com.program2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.program2.repository.ShiftRepository;
import com.program2.table.Shift;

@RestController
@RequestMapping(value = "/shifts")
public class ShiftController {
	@Autowired
	private ShiftRepository ShiftRepository;
	
	@GetMapping
	public List<Shift> getShifts()
	{
		return ShiftRepository.findAllByOrderByIdAsc();
	}
	
	@GetMapping("/{id}")
	public Shift getPerson(@PathVariable("id") int id)
	{
		return ShiftRepository.findById(id);
	}
}
