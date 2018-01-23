package com.program2.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.program2.repository.RemunerationRepository;
import com.program2.table.Remuneration;


public class SalarySchedule {

	    public void startScheduleTask() 
	    {
	    	 LocalDateTime localNow = LocalDateTime.now();
	         
	         LocalDateTime date= LocalDateTime.now();
	         LocalDateTime date5= date.withHour(23).withMinute(59).withSecond(0);
	         if(date.compareTo(date5) > 0)
	             date5 = date5.plusDays(1);

	         java.time.Duration duration = java.time.Duration.between(date, date5);
	         long initalDelay = duration.getSeconds();

	         ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);            
	         scheduler.scheduleAtFixedRate( new Runnable() {
	             public void run() {
	                 try {
	                     getDataFromDatabase();
	                 }catch(Exception ex) {
	                     ex.printStackTrace();
	                 }
	             }
	         }, initalDelay, 24*60*60, TimeUnit.SECONDS);
	    }

	    private void getDataFromDatabase() {
	        
	    }
}
