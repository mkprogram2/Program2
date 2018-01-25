package com.program2.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.program2.Schedule;
import com.program2.repository.HolidayRepository;
import com.program2.table.Holiday;
import com.program2.table.Remuneration;
import com.program2.table.Workhour;
import com.program2.repository.RemunerationRepository;
import com.program2.repository.WorkhourRepository;

@Service
public class RemunerationService {
	@Autowired
	private HolidayRepository HolidayRepository;
	@Autowired
	private RemunerationRepository RemunerationRepository;
	@Autowired
	private WorkhourRepository WorkhourRepository;
	
	SimpleDateFormat DateFormat = new SimpleDateFormat("M-yyyy");
	SimpleDateFormat DateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	
	public	void RenewRemunerationSystem () 
	{
		Date date= new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH)+1;
		int year = cal.get(Calendar.YEAR);
		List<Remuneration> Remun = RemunerationRepository.findMonthAndYear(month, year);
		for (Remuneration temp : Remun) {
			List<Workhour>  Workhours= WorkhourRepository.findAllByMonthByYearById(month, year,temp.person.id);
			int attends = Workhours.size();
			int workdays = TotalWorkDaysInMonth(month,year);
			temp.minsalary = temp.salary - (temp.salary * attends / workdays);
			temp.minmeal = temp.meal - (temp.meal * attends / workdays);
			temp.mintrans = temp.trans - (temp.trans * attends / workdays);
			temp.deduction = temp.minsalary+temp.minmeal+temp.mintrans+temp.mindiligent;
			temp.netsalary = temp.income - temp.deduction;
			RemunerationRepository.save(temp);
		}
	}
	
	public	int TotalWorkDaysInMonth (double month, double year) 
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
	
	public	int TotalWorkDaysFromNow ( double month, double year) 
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
			EndDayCalendar.setTime(new Date());
			if (EndDayCalendar.get(Calendar.HOUR_OF_DAY) < Schedule.servercheck) {
					EndDayCalendar.add(Calendar.DATE, -1);
				}
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
