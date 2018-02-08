package com.program2.controller;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.program2.repository.BehaviorRepository;
import com.program2.service.WorkhourService;
import com.program2.table.Behavior;

@RestController
@RequestMapping(value = "/behaviors")
public class BehaviorController
{
	@Autowired
	private BehaviorRepository BehaviorRepository;
	@Autowired
	private WorkhourService WorkhourService;
	
	@PostMapping
	public int SetBehavior(@RequestBody String personid)
	{
		Timestamp now = new Timestamp(System.currentTimeMillis()); 
		Behavior Behavior = BehaviorRepository.findFirstByPersonidOrderByCheckinDesc(personid);
		
		if (Behavior==null) 
		{
			Behavior = new Behavior();
			Behavior.checkin = now;
			Behavior.checkout = now;
			Behavior.personid = personid;
			BehaviorRepository.save(Behavior);
		}
		else
		{
			long a = now.getTime() - Behavior.checkout.getTime();
			if(a > 70000) 
			{
				Behavior New = new Behavior();
				New.checkin = now;
				New.checkout = now;
				New.personid = personid;
				BehaviorRepository.save(New);
			}
			else 
			{
				Behavior.checkout = now;
				BehaviorRepository.save(Behavior);
			}
			
		}
		int status = WorkhourService.Checkin(personid);
		WorkhourService.CheckoutUser(personid);
		return status;
	}
}
