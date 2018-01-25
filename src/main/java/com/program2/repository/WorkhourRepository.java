package com.program2.repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.program2.table.Workhour;

@Repository
public interface WorkhourRepository extends JpaRepository<Workhour,String>
{
	public Workhour findByPersonidAndWorkstartBetween(String personid, Timestamp workstart1,Timestamp workstart2); 
	public List<Workhour> findByWorkstartBetween(Timestamp workstart1,Timestamp workstart2); 
	public List<Workhour> findByPersonid(String personid);
	
	@Query(value = "SELECT * FROM mtr3.workhours WHERE EXTRACT(month FROM work_start) = ? AND EXTRACT(year FROM work_start) = ? AND person_id = ?",  nativeQuery = true)
	public List<Workhour> findAllByMonthByYearById(double month, double year, String id);
	
	@Modifying
	@Transactional
	 @Query(value = "Insert into mtr3.workhours (person_id, work_start, work_start_interval) values (?,?, CAST (? as interval))", nativeQuery = true)
	 public int tes(String person_id, Timestamp work_start, String interval);
}
