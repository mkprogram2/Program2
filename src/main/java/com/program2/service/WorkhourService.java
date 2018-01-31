package com.program2.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.program2.repository.HolidayRepository;
import com.program2.repository.PersonRepository;
import com.program2.repository.WorkhourRepository;
import com.program2.table.Holiday;
import com.program2.table.Person;
import com.program2.table.Workhour;

@Service
public class WorkhourService {
	@Autowired
	private WorkhourRepository WorkhourRepository;
	@Autowired
	private PersonRepository PersonRepository;
	@Autowired
	private HolidayRepository HolidayRepository;

	private SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private SimpleDateFormat DateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	
	
	
	public void CheckoutSystem ()
	{
		System.out.println("Asd");
		List<Workhour> Workhour = WorkhourOnlyByDate(new Date());
		for (Workhour temp : Workhour) {
			System.out.println(temp.personid);
			if(temp.workend==null) {
				Checkout(new Date(),temp,2);
			}
		}
	}
	
	public int CheckoutUser(@RequestBody String personid)
	{
		Date DateNow = new Date();
		Workhour WorkhourCheck = WorkhourByDateById(personid, DateNow);
		
		if (WorkhourCheck == null) {
			return 2;
		}
		else if (WorkhourCheck.workend != null && WorkhourCheck.status == 1) 
		{
			return 3;
		}
		else 
		{
			long interval = DateNow.getTime() - WorkhourCheck.workstart.getTime();
			if (interval >= 1800000) 
			{
				Checkout(DateNow,WorkhourCheck,1);
				return 1;
			} else { return 4; }
		}
	}
	
	public void Checkout(Date DateNow, Workhour Workhour, int status) {
		Workhour.workend = new Timestamp(DateNow.getTime());
		Workhour.workendinterval = (int)Interval(Workhour.personid,DateNow,false);
		long work_interval = TimeUnit.MILLISECONDS.toSeconds(Workhour.workend.getTime() - Workhour.workstart.getTime());
		Workhour.workinterval = (int) work_interval;
		Workhour.status = status;
		WorkhourRepository.save(Workhour);
	}
	
	public int Checkin(String personid)
	{
		Date DateNow = new Date();
		Calendar CalendarNow = Calendar.getInstance();
		CalendarNow.setTime(DateNow);
		int day = CalendarNow.get(Calendar.DAY_OF_WEEK);
		List<Holiday>  Holidays= HolidayRepository.findAllByMonthByYear(Calendar.MONTH + 1, Calendar.YEAR);
		List<Date> HolidayDates = Holidays.stream()
                .map(Holiday->Holiday.date)
                .collect(Collectors.toList());
        if (!HolidayDates.contains(CalendarNow.getTime()) && (day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)) 
        {
        	Workhour WorkhourCheck = WorkhourByDateById(personid, DateNow);
    		if (WorkhourCheck == null)
    		{
    			Workhour insert = new Workhour();
    			insert.personid = personid;
    			insert.workstart = new Timestamp(DateNow.getTime());
    			insert.workstartinterval = (int)Interval(personid,DateNow,true);
    			WorkhourRepository.save(insert);
    			return (1);
    		}
    		else
    		{
    			WorkhourCheck.personid = null;
    			return (2);
    		}
        }
        else {return (3);}	
	}
	
	public Workhour WorkhourByDateById(String personid, Date Date) 
	{
		Workhour WorkhourCheck = new Workhour();
		Date DateNow = Date;
		Calendar Cal = Calendar.getInstance();
		Cal.setTime(DateNow);
		Date DateA = Cal.getTime();
		Cal.add(Calendar.DATE, 1);
		Date DateB = Cal.getTime();
		try
		{
			Date DateS1 = DateFormat1.parse(DateFormat1.format(DateA));
			Date DateS2 = DateFormat1.parse(DateFormat1.format(DateB));
			Timestamp Timestamp1 = new Timestamp(DateS1.getTime());
			Timestamp Timestamp2 = new Timestamp(DateS2.getTime());
			WorkhourCheck = WorkhourRepository.findByPersonidAndWorkstartBetween(personid, Timestamp1,Timestamp2);
		}
		catch(Exception w) {}
		return WorkhourCheck;
	}
	
	public long Interval (String personid, Date Date, boolean checkin) {
		long Interval = 0;
		String SDatefull = "";
		Person shift = PersonRepository.findById(personid);
		Date DateNow = Date;
		if (checkin) 
		{
			SDatefull = DateFormat1.format(DateNow)+" "+shift.PersonDetail.Shift.workstart;
		}
		else 
		{
			SDatefull = DateFormat1.format(DateNow)+" "+shift.PersonDetail.Shift.workend;
		}
		try
		{
			Date DateStart = DateFormat.parse(SDatefull);
			Interval = TimeUnit.MILLISECONDS.toSeconds(DateStart.getTime() - DateNow.getTime());
		}
		catch(Exception w) {}
		return Interval;
	}
	
	public  List<Workhour> WorkhourOnlyByDate(Date Date) 
	{
		List<Workhour> WorkhourCheck = new ArrayList<Workhour>();
		Date DateNow = Date;
		Calendar Cal = Calendar.getInstance();
		Cal.setTime(DateNow);
		Date DateA = Cal.getTime();
		Cal.add(Calendar.DATE, 1);
		Date DateB = Cal.getTime();
		try
		{
			Date DateS1 = DateFormat1.parse(DateFormat1.format(DateA));
			Date DateS2 = DateFormat1.parse(DateFormat1.format(DateB));
			Timestamp Timestamp1 = new Timestamp(DateS1.getTime());
			Timestamp Timestamp2 = new Timestamp(DateS2.getTime());
			WorkhourCheck = WorkhourRepository.findByWorkstartBetween(Timestamp1,Timestamp2);
		}
		catch(Exception w) {w.printStackTrace();}
		return WorkhourCheck;
	}
}
