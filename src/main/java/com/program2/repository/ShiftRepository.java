package com.program2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.program2.table.Person;
import com.program2.table.Shift;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, String>
{
	public Shift findById(int id);

	public List<Shift> findAllByOrderByIdAsc();

}
