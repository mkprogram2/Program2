package com.program2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.program2.table.Overtime;;

@Repository
public interface OvertimeRepository extends JpaRepository<Overtime, Integer>
{
	public List<Overtime> findByPersonidOrderByDateDesc (String personid);
}
