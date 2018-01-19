package com.program2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.program2.table.Role;
import com.program2.table.Workhour;

public interface RoleRepository  extends JpaRepository<Role,String>{

}
