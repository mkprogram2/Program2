package com.program2.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.program2.table.Holiday;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Integer>
{
	public List<Holiday> findAllByOrderByIdAsc();
	
	@Query(value = "SELECT * FROM mtr3.holidays WHERE EXTRACT(month FROM date) = ? AND EXTRACT(year FROM date) = ?",  nativeQuery = true)
	public List<Holiday> findAllByMonthByYear(double month, double year);
	
	public List<Holiday> findByDateBetween(Date start, Date end);
	
//	@Query("SELECT * FROM mtr3.holidays WHERE EXTRACT(month FROM 'date') = ? AND EXTRACT(year FROM 'date') = ?")
//	public List<Holiday> findAllByMonth(String month, String year);
}