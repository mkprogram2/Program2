package com.program2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.program2.table.Behavior;
import com.program2.table.Overtime;

@Repository
public interface BehaviorRepository extends JpaRepository<Behavior, Integer>{
	public List<Behavior> findByPersonid (String personid);
	public Behavior findFirstByPersonidOrderByCheckinDesc (String personid);
}
