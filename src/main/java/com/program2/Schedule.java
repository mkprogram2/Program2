package com.program2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static java.time.temporal.TemporalAdjusters.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.program2.service.WorkhourService;
import com.program2.service.RemunerationService;


public class Schedule {

		@Autowired
		private WorkhourService WorkhourService;
		@Autowired
		private RemunerationService RemunerationService;
		public static int servercheck = 18;
	    public void startScheduleTask() 
	    {
	         
	         LocalDateTime date= LocalDateTime.now();
	         LocalDateTime date5= date.withHour(servercheck).withMinute(0).withSecond(0);
	         if(date.compareTo(date5) > 0)
	             date5 = date5.plusDays(1);

	         java.time.Duration duration = java.time.Duration.between(date, date5);
	         long InitDelay = duration.getSeconds();

	         ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);            
	         scheduler.scheduleAtFixedRate( new Runnable() {
	             public void run() {
	                 try 
	                 {
	                	 WorkhourService.CheckoutSystem();
	                	 RemunerationService.RenewRemunerationSystem();
	                	 if(LocalDate.now().getDayOfMonth() == LocalDate.now().with(lastDayOfMonth()).getDayOfMonth()) {
	                		 RemunerationService.NextMonthRemunerationSystem();
	                	 }
	                 }
	                 catch(Exception ex) {
	                     ex.printStackTrace();
	                 }
	             }
	         }, InitDelay, 24*60*60, TimeUnit.SECONDS);
	    }

//	    private void getDataFromDatabase() {
//	        
//	    }
}
