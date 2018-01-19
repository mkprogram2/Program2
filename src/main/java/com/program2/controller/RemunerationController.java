package com.program2.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.program2.repository.HolidayRepository;
import com.program2.repository.WorkhourRepository;
import com.program2.repository.PersonRepository;
import com.program2.table.Holiday;
import com.program2.table.Person;
import com.program2.table.Shift;
import com.program2.table.Workhour;

@RestController
@RequestMapping(value = "/remunerations")
public class RemunerationController {
	SimpleDateFormat DateFormat = new SimpleDateFormat("M-yyyy");
	SimpleDateFormat DateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	@Autowired
	private WorkhourRepository WorkhourRepository;
	@Autowired
	private HolidayRepository HolidayRepository;
	@Autowired
	private PersonRepository PersonRepository;
	
	@GetMapping("/salary/{month}/{year}/{id}")
	public Double Netsalary (@PathVariable("month") double month, @PathVariable("year") double year, @PathVariable("id") String id) 
	{
		List<Workhour>  Workhours= WorkhourRepository.findAllByMonthByYearById(month, year,id);
		int attends = Workhours.size();
		double gross_salary =  PersonRepository.findById(id).salary;
		double net_salary = gross_salary * attends / WorkhoursInMonth(month,year);
		return net_salary;
	}
	
	@GetMapping("/totalwim/{month}/{year}")
	public	int GetWorkhoursInMonth (@PathVariable("month") double month, @PathVariable("year") double year) 
	{
		System.out.println(month+" "+year);
		return  WorkhoursInMonth(month,year);
	}
	
	@PutMapping("/salary")
	public	Person UpdateSalary (@RequestBody Person person) 
	{
		return  PersonRepository.save(person);
	}
	
	public	int WorkhoursInMonth (double month, double year) 
	{
		List<Holiday>  Holidays= HolidayRepository.findAllByMonthByYear(month, year);
		List<Date> HolidayDates = Holidays.stream()
                .map(Holiday->Holiday.date)
                .collect(Collectors.toList());
		int workhours = 0;
		Calendar StartDayCalendar = Calendar.getInstance();
		Calendar EndDayCalendar = Calendar.getInstance();
		try
		{
			StartDayCalendar.setTime(DateFormat.parse((int)month+"-"+(int)year));
			EndDayCalendar.setTime(DateFormat.parse((int)month+"-"+(int)year));
			EndDayCalendar.add(Calendar.MONTH, 1);
			EndDayCalendar.add(Calendar.DATE, -1);
		}
		catch(Exception e) {}
		while(!StartDayCalendar.after(EndDayCalendar)) 
	    {
			int day = StartDayCalendar.get(Calendar.DAY_OF_WEEK);
            if (!HolidayDates.contains(StartDayCalendar.getTime())) 
            {
	              if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)) 
	              {
	            	  	workhours++;
	              }
            }
			StartDayCalendar.add(Calendar.DATE, 1);
	    }
		return workhours;
	}
}
