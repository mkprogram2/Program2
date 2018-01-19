package com.program2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.program2.repository.RoleRepository;
import com.program2.table.Role;

@RestController
@RequestMapping(value = "/roles")
public class RoleController {
	@Autowired
	private RoleRepository RoleRepository;
	@GetMapping
	public List<Role> GetRoles()
	{
		return RoleRepository.findAll();
	}
}
